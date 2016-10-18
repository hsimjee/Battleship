import java.applet.*;
import java.net.*;
import java.io.*;
public class Battleship
{
    private static AudioClip explosion = null;
    private static AudioClip splash = null;
    static
    {
        try 
        {
            explosion = Applet.newAudioClip(new URL("file:" + new File(".").getCanonicalPath() 
                    + "/" + "explosion.wav")); 
            splash = Applet.newAudioClip(new URL("file:" + new File(".").getCanonicalPath() 
                    + "/" + "splash.wav"));
        } 
        catch (Exception e) 
        { 
            System.out.println(e.toString()); 
        }
    }
    private static boolean validInput = false;
    private static String name, toss = "";
    private static KeyboardReader reader = new KeyboardReader();
    private static int x, y, humanTotalHitsTaken = 0, GAME_OVER_LIMIT = 17, compTotalHitsTaken = 0, currentPlayer,
    HUMAN = 1, ShipRow, ShipColumn, orient, a = 0, b = 0, d = 0, s = 0, p = 0, compa = 0, compb = 0,
    compd = 0, comps = 0, compp = 0;
    private static String[][] compGrid = {  {"###", " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", " 10", "#   Name of Ship   |", " Status #"},
            {"#A ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#B ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Aircraft Carrier |", "  Alive #"},
            {"#C ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#D ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Battleship       |", "  Alive #"},
            {"#E ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#F ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Destroyer        |", "  Alive #"},
            {"#G ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#H ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Submarine        |", "  Alive #"},
            {"#I ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#J ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Patrol Boat      |", "  Alive #"}  };

    private static String[][] humanGrid = {  {"###", " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", " 10", "#   Name of Ship   |", " Status #"},
            {"#A ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#B ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Aircraft Carrier |", "  Alive #"},
            {"#C ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#D ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Battleship       |", "  Alive #"},
            {"#E ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#F ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Destroyer        |", "  Alive #"},
            {"#G ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#H ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Submarine        |", "  Alive #"},
            {"#I ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#J ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Patrol Boat      |", "  Alive #"}  };;

    private static String[][] hitGrid = {  {"###", " 1", " 2", " 3", " 4", " 5", " 6", " 7", " 8", " 9", " 10", "#   Name of Ship   |", " Status #"},
            {"#A ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#B ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Aircraft Carrier |", "  Alive #"},
            {"#C ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#D ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Battleship       |", "  Alive #"},
            {"#E ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#F ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Destroyer        |", "  Alive #"},
            {"#G ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#H ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Submarine        |", "  Alive #"},
            {"#I ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " #------------------|", "--------#"},
            {"#J ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", "  ", " # Patrol Boat      |", "  Alive #"}  };

    public static void main (String [] args)
    {
        welcomeBanner();
        initializeGame();
        while (humanTotalHitsTaken < GAME_OVER_LIMIT && compTotalHitsTaken < GAME_OVER_LIMIT)
        {
            displayGame();
            if (currentPlayer == HUMAN)
                humanPlayerTakesTurn();
            else
                computerPlayerTakesTurn();
        }

        congratulateWinner();           
    }

    public static void welcomeBanner()
    {
        System.out.println("\n**********************************************************");
        System.out.println("* Welcome to Huzaifah Simjee's Awesome Battleship Game!! *");
        System.out.println("**********************************************************");
    }

    public static void initializeGame()
    {
        placeShips(humanGrid);
        placeShips(compGrid);
        KeyboardReader reader = new KeyboardReader();
        name = reader.readString("\nPlease enter your name: ");
        while(name.length() > 51)
            name = reader.readString("\nThat name is too long. Please enter a new name: ");
        String coin = reader.readString("\nIt's time for the coin toss to determine who will go first.\n" +
                name + " would you like heads or tails? ");
        while (!coin.equalsIgnoreCase("heads") && !coin.equalsIgnoreCase("tails"))
            coin = reader.readString("\nIt's time for the coin toss to determine who will go first.\n" +
                name + " would you like heads or tails? ");
        int ran = (int)(Math.random()*2);
        if (ran == 0)
            toss = "heads";
        else
            toss = "tails";
        System.out.println("\nThe coin came up " + toss + "."); 
        if (toss.equalsIgnoreCase(coin))
        {
            System.out.println("You won the coin toss and will fire first.");
            currentPlayer = HUMAN;
        }
        else
        {
            System.out.println("You lost the coin toss and Tyrone will fire first.");
            currentPlayer = 0;
        }
    }

    public static void placeShips(String [][] grid)
    {
        placeAll(" A", 5, 5, grid); //Aircraft Carrier
        placeAll(" B", 4, 6, grid); //Battleship
        placeAll(" D", 3, 7, grid); //Destroyer
        placeAll(" S", 3, 7, grid); //Submarine
        placeAll(" P", 2, 8, grid); //Patrol Boat
    }  

    public static void placeAll(String shipSymbol, int shipLength, int maxPos, String[][] grid)
    {
        orient = (int)(Math.random()*2);
        if (orient == 0) //Vertical
        {
            ShipRow = ((int)(Math.random()*(maxPos))+1);
            ShipColumn = ((int)(Math.random()*10)+1);
            for (int i = 0; i <= shipLength; i++)
                while (grid[ShipRow + i][ShipColumn] != "  ")
                {        
                    ShipRow = ((int)(Math.random()*(maxPos))+1);
                    ShipColumn = ((int)(Math.random()*10)+1);
                }

            for (int i = ShipRow; i < (ShipRow + shipLength); i++)
                grid[i][ShipColumn] = shipSymbol;
        }
        else if (orient == 1) //Horizontal
        {
            ShipColumn = ((int)(Math.random()*(maxPos))+1);
            ShipRow = ((int)(Math.random()*10)+1);
            for (int i = 0; i <= shipLength; i++)
                while (grid[ShipRow][ShipColumn + i] != "  ")
                {        
                    ShipColumn = ((int)(Math.random()*(maxPos))+1);
                    ShipRow = ((int)(Math.random()*10)+1);
                }

            for (int i = ShipColumn; i < (ShipColumn + shipLength); i++)
                grid[ShipRow][i] = shipSymbol;
        }
    }

    public static void displayGame()
    {
        System.out.println("#####################################################");
        System.out.println("#                      Tyrone                       #");
        System.out.println("#####################################################");
        for(int i = 0; i < 11; i++)
        {
            for(int j = 0; j < 13; j++)
            {
                System.out.print(hitGrid[i][j]);
            }
            System.out.println();
        }
        System.out.println("#####################################################");
        int j = ((51-name.length())/2);
        System.out.println("\n#####################################################");
        System.out.print("#");
        for (int i = 0; i < j; i++)
            System.out.print(" ");
        System.out.print(name);
        for (int i = 0; i < j; i++)
            System.out.print(" ");
        if (j*2 != (54-name.length()))
            System.out.print(" ");
        System.out.print("#");
        System.out.println("\n#####################################################");
        for(int g = 0; g < 11; g++)
        {
            for(int k = 0; k < 13; k++)
            {
                System.out.print(humanGrid[g][k]);
            }
            System.out.println();
        }
        System.out.println("#####################################################");
    }

    public static void humanPlayerTakesTurn()
    {
        while (validInput == false)
        {
            validInput = true;
            String loc = reader.readString("\n" + name + ", where would you like to hit? Example: E,3: ");
            loc.trim();
            if (loc.length() != 4 && loc.length() != 3 && loc.length() != 5)
            {
                System.out.println("Not Valid");
                validInput = false;
            }
            if (validInput == true)
            {
                String test = loc.substring(1,2);
                if (!test.equals(","))
                {
                    System.out.println("Not Valid");
                    validInput = false;
                }
                if (validInput == true)
                {
                    String tempx = loc.substring(0,1);
                    x = letterToNum(tempx);

                    if (x == 11)
                    {
                        System.out.println("Not Valid");
                        validInput = false;
                    }

                    if (validInput == true)
                    {
                        String temp = loc.substring(loc.length()-2, loc.length());

                        if (!temp.equals(",1") && !temp.equals(",2") && !temp.equals(",3") && !temp.equals(",4") && !temp.equals(",5") && 
                        !temp.equals(",6") && !temp.equals(",7") && !temp.equals(",8") && !temp.equals(",9") && !temp.equals("10") &&
                        !temp.equals(" 1") && !temp.equals(" 2") && !temp.equals(" 3") && !temp.equals(" 4") && !temp.equals(" 5") && 
                        !temp.equals(" 6") && !temp.equals(" 7") && !temp.equals(" 8") && !temp.equals(" 9") && !temp.equals("10"))
                        {
                            System.out.println("Not Valid");
                            validInput = false;
                        }
                        if (validInput == true)
                        {
                            if (!temp.equals("10"))
                                temp = loc.substring(loc.length()-1, loc.length());
                            y = Integer.parseInt(temp);
                            if (compGrid[x][y] == " X" || compGrid[x][y] == " *")
                            {
                                System.out.println("You already hit there.");
                                validInput = false;
                            }
                        }
                    }
                }
            }
        }

        if (compGrid[x][y] == " A")
            compa++;
        if (compGrid[x][y] == " B")
            compb++;
        if (compGrid[x][y] == " D")
            compd++;
        if (compGrid[x][y] == " S")
            comps++;
        if (compGrid[x][y] == " P")
            compp++;

        if (compGrid[x][y] != "  ")
        {
            System.out.println("It's a hit!!!");
            compGrid[x][y] = " *";
            hitGrid[x][y] = " *";
            compTotalHitsTaken++;
            explosion.play();
        }
        else
        {
            System.out.println("A swing and a miss!");
            compGrid[x][y] = " X";
            hitGrid[x][y] = " X";
            splash.play();
        }

        if (compa == 5)
        {
            compGrid[2][12] = "  Dead  #";
            hitGrid[2][12] = "  Dead  #";
            System.out.println("You have sunk Tyrone's Aircraft Carrier!");
            compa = 0;
        }
        if (compb == 4)
        {
            compGrid[4][12] = "  Dead  #";
            hitGrid[4][12] = "  Dead  #";
            System.out.println("You have sunk Tyrone's Battleship!");
            compb = 0;
        }
        if (compd == 3)
        {
            compGrid[6][12] = "  Dead  #";
            hitGrid[6][12] = "  Dead  #";
            System.out.println("You have sunk Tyrone's Destroyer!");
            compd = 0;
        }
        if (comps == 3)
        {
            compGrid[8][12] = "  Dead  #";
            hitGrid[8][12] = "  Dead  #";
            System.out.println("You have sunk Tyrone's Submarine!");
            comps = 0;
        }
        if (compp == 2)
        {
            compGrid[10][12] = "  Dead  #";
            hitGrid[10][12] = "  Dead  #";
            System.out.println("You have sunk Tyrone's Patrol Boat!");
            compp = 0;
        }
        currentPlayer = 0;
        validInput = false;
        reader.pause();
    }

    public static void computerPlayerTakesTurn()
    {
        int hitX = (int)((Math.random()*10)+1);
        int hitY = (int)((Math.random()*10)+1);
        while (humanGrid[hitY][hitX] == " X" || humanGrid[hitY][hitX] == " *")
        {
            hitX = (int)((Math.random()*10)+1);
            hitY = (int)((Math.random()*10)+1);
        }
        System.out.println("Tyrone has decided to shoot at " + numToLetter(hitY) + ", " + hitX + ".");
        if (humanGrid[hitY][hitX] == "  ")
        {
            System.out.println("Tyrone missed all your ships...");
            humanGrid[hitY][hitX] = " X";
            splash.play();
        }
        else
        {
            System.out.println("It's a hit!!!");
            humanGrid[hitY][hitX] = " *";
            humanTotalHitsTaken++;
            explosion.play();
            if (humanGrid[hitY][hitX] == " A")
                a++;
            if (humanGrid[hitY][hitX] == " B")
                b++;
            if (humanGrid[hitY][hitX] == " D")
                d++;
            if (humanGrid[hitY][hitX] == " S")
                s++;
            if (humanGrid[hitY][hitX] == " P")
                p++;
        }

        if (a == 5)
        {
            humanGrid[12][2] = "  Dead  #";
            System.out.println("Tyrone has sunk your Aircraft Carrier!");
            a = 0;
        }
        if (b == 4)
        {
            humanGrid[12][4] = "  Dead  #";
            System.out.println("Tyrone has sunk your Battleship!");
            b = 0;
        }
        if (d == 3)
        {
            humanGrid[12][6] = "  Dead  #";
            System.out.println("Tyrone has sunk your Destroyer!");
            d = 0;
        }
        if (s == 3)
        {
            humanGrid[12][8] = "  Dead  #";
            System.out.println("Tyrone has sunk your Submarine!");
            s = 0;
        }
        if (p == 2)
        {
            humanGrid[12][10] = "  Dead  #";
            System.out.println("Tyrone has sunk your Patrol Boat!");
            p = 0;
        }
        currentPlayer = HUMAN;
        reader.pause();
    }

    public static String numToLetter(int digit)
    {
        if (digit == 1)
            return "A";
        else if (digit == 2)
            return "B";
        else if (digit == 3)
            return "C";     
        else if (digit == 4)
            return "D";
        else if (digit == 5)
            return "E";
        else if (digit == 6)
            return "F";
        else if (digit == 7)
            return "G";
        else if (digit == 8)
            return "H";
        else if (digit == 9)
            return "I";
        else if (digit == 10)
            return "J";
        else
            return " ";
    }

    public static int letterToNum(String num)
    {
        if (num.equalsIgnoreCase("A"))
            return 1;
        else if (num.equalsIgnoreCase("B"))
            return 2;
        else if (num.equalsIgnoreCase("C"))
            return 3;     
        else if (num.equalsIgnoreCase("D"))
            return 4;
        else if (num.equalsIgnoreCase("E"))
            return 5;
        else if (num.equalsIgnoreCase("F"))
            return 6;
        else if (num.equalsIgnoreCase("G"))
            return 7;
        else if (num.equalsIgnoreCase("H"))
            return 8;
        else if (num.equalsIgnoreCase("I"))
            return 9;
        else if (num.equalsIgnoreCase("J"))
            return 10;
        else
            return 11;
    }

    public static void congratulateWinner()
    {
        System.out.println("\n\n**************");
        System.out.println("* Game Over! *");
        System.out.println("**************\n\n");
        displayGame();
        if (compTotalHitsTaken == GAME_OVER_LIMIT)
            System.out.println("\n\nCongratulations. You beat Tyrone! You should be proud for beating a completely random computer.");
        else if (humanTotalHitsTaken == GAME_OVER_LIMIT)
            System.out.println("\n\nTyrone Wins! How do you lose to a random computer. You're really bad at this game.");
        reader.pause();
    }
}