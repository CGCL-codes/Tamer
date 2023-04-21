package greatestgame;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipException;
import org.lwjgl.util.Point;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;

/**
 * Class that initializes the game and calls the render and update methods constantly
 * @author Matt
 */
public class gameMain extends BasicGame implements ComponentListener {

    static final long MENYOO_LENGTH = 18200000000L;

    static final long NUFEELD_LENGTH = 55600000000L;

    public static String path = "";

    public static String charecterPath = "";

    public static ArrayList<Tile> currentMap = new ArrayList<Tile>();

    public static ArrayList<Inventory> currentIOG = new ArrayList<Inventory>();

    public static ArrayList<Inventory> badIOG = new ArrayList<Inventory>();

    public static ArrayList<Inventory> itemInI = new ArrayList<Inventory>();

    public static ArrayList<Character> currentCharecters = new ArrayList<Character>();

    public static ArrayList<PartyC> partyCharecters = new ArrayList<PartyC>();

    public static ArrayList<String> compleatedTasks = new ArrayList<String>();

    public static ArrayList<Character> combatants = new ArrayList<Character>();

    public static CommandParsing commands;

    public static TextField field;

    public static TrueTypeFont font;

    public static TrueTypeFont font15;

    public static UserInterfaceGame gameUI;

    private static boolean hasPlayed;

    public static boolean intro = false;

    public static boolean end = false;

    public static Grid items;

    public static Grid map;

    public static Grid charecters;

    public static Quest quest;

    public static Combat combat;

    public static String message;

    public static String[] lines;

    public static String[] main;

    FileRead menyoo = new FileRead("music/menyoo2.wav");

    FileRead nufeeld = new FileRead("music/nufeeld.wav");

    public static String[] introT;

    public static String[] endT;

    public static long time = System.nanoTime();

    public static UserInterface mainMenu;

    public static Conversation convo;

    public static PopupText popup;

    public static PopupText popupQuest;

    public static String name;

    public static boolean playSong = true;

    public static int combatMMove[] = { 0, 0, 0, 0 };

    /**
     * generates the Application
     */
    public gameMain() {
        super("newZork");
    }

    /**
     * Saves the game in a folder derived from the name of the adventure running
     * @return returns true if the save was successful
     */
    public static boolean save() {
        String tempinv = "", tempbad = "";
        for (Inventory i : itemInI) {
            tempinv += i + "/";
        }
        if (tempinv.length() == 0) {
            tempinv += "/";
        }
        for (Inventory i : badIOG) {
            tempbad += i.getCoordinates() + "/";
        }
        if (tempbad.length() == 0) {
            tempbad += "/";
        }
        String write = tempinv + ":" + tempbad + ":" + map.getLocation().getX() + ":" + map.getLocation().getY();
        write = toHex(write);
        System.out.println(name);
        FileWrite fw = new FileWrite("saves/" + name + ".pbz", 0);
        return fw.writeToFile(write);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        try {
            FileInputStream fis = new FileInputStream(new File(path + "resources/Emulator.ttf"));
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, fis);
            awtFont = awtFont.deriveFont(50f);
            font = new TrueTypeFont(awtFont, false);
            awtFont = awtFont.deriveFont(15f);
            font15 = new TrueTypeFont(awtFont, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainMenu = new UserInterface(new Image(path + "resources/gui/mainback.png"), font, new MouseOverArea(gc, new Image(path + "resources/gui/new.png"), 64, 64), new MouseOverArea(gc, new Image(path + "resources/gui/load.png"), 64, 256), new MouseOverArea(gc, new Image(path + "resources/gui/quit.png"), 64, 448), new MouseOverArea(gc, new Image(path + "resources/gui/next.png"), 384, 1225), new MouseOverArea(gc, new Image(path + "resources/gui/prev.png"), 384, 288), false, false, true, 0);
        mainMenu.setComponentListners();
        popup = new PopupText("", Color.green.multiply(Color.darkGray), new Image(path + "null.png"), "popup", font15, 0);
        popupQuest = new PopupText("", Color.green.multiply(Color.darkGray), new Image(path + "null.png"), "popup", font15, 0);
        field = new TextField(gc, font, 64, 576, 1216, 64, new ComponentListener() {

            @Override
            public void componentActivated(AbstractComponent source) {
                message = field.getText();
            }
        });
        field.setBackgroundColor(Color.black);
        field.setBorderColor(Color.black);
        field.setFocus(true);
        try {
            mainMenu.setCreates(gc);
            mainMenu.setLoads(gc);
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when a specific New Game file is clicked to instantiate all the appropriate data for a New Game
     * @throws SlickException 
     */
    public static void adventureInit() throws SlickException {
        currentCharecters.clear();
        gameUI = new UserInterfaceGame(new Image(path + "extracted/images/gui/gameUI.png"), new Image(path + "extracted/images/gui/12.png"));
        convo = new Conversation(new Image(path + "extracted/images/gui/convoUI.png"), new Image(path + "null.png"), font15, Color.white, new Image(path + "null.png"), "Convo", false);
        combat = new Combat(new Image(path + "extracted/images/gui/convoUI.png"), new Image(path + "null.png"), font15, Color.white, new Image(path + "null.png"), "Convo", false, combatants);
        FileRead file = new FileRead(path + "extracted/text/keys/allowedCommands.txt");
        String[] temp = file.readTxt();
        ArrayList<String> tempS = new ArrayList<String>();
        tempS.addAll(Arrays.asList(temp));
        map = new Grid(Color.white, true, new Image(path + "null.png"), "Map", new Point(0, 0));
        FileRead fileT = new FileRead(gameMain.path + "/extracted/text/maps/mapde/" + map.location.getX() + "," + map.location.getY() + ".txt");
        String[] qText = fileT.readTxt();
        String qTextConcat = "Go Away, \ntheres nothing here";
        if (qText != null) {
            qTextConcat = "";
            for (String i : qText) {
                qTextConcat += (i + "\n");
            }
        }
        popupQuest.setMessage(qTextConcat);
        items = new Grid(Color.white, true, new Image(path + "null.png"), "Items", new Point(0, 0));
        charecters = new Grid(Color.white, true, new Image(path + "null.png"), "Charecters", new Point(0, 0));
        commands = new CommandParsing("", map.location);
        FileRead fileQ = new FileRead(path + "extracted/text/quest/main.txt");
        String[] tempQ = fileQ.readTxt();
        ArrayList<String> tempQ2 = new ArrayList<String>();
        tempQ2.addAll(Arrays.asList(tempQ));
        FileRead fileI = new FileRead(path + "extracted/text/quest/begin.txt");
        introT = fileI.readTxt();
        FileRead fileE = new FileRead(path + "extracted/text/quest/end.txt");
        endT = fileE.readTxt();
        quest = new Quest(false, tempQ2);
        commands.setCommands(tempS);
        map.loadMap();
        items.loadInventory();
        charecters.loadCharecters();
    }

    /**
     * Called when a specific Load Game File is selected
     * @param name the name of the loadGame
     * @throws SlickException 
     */
    public static void loadAdventureInit(String name) throws SlickException {
        currentCharecters.clear();
        ArrayList<String> temp = loadGame(name);
        itemInI.clear();
        String[] invTemp = temp.get(0).split("/");
        for (String i : invTemp) {
            String[] invTemp2 = i.split(",");
            if (invTemp2.length >= 3) {
                String invName = invTemp2[0];
                Point invPoint = new Point(CommandParsing.isNumber(invTemp2[1]) ? Integer.parseInt(invTemp2[1]) : 0, CommandParsing.isNumber(invTemp2[1]) ? Integer.parseInt(invTemp2[2]) : 0);
                itemInI.add(new Inventory(1, new Image(path + "/null.png"), true, true, true, true, new Point(0, 0), Color.white, true, new Image(path + "/null.png"), new Point(0, 0), invName, invPoint, true, "This is the description"));
            }
        }
        badIOG.clear();
        String[] badIOGTemp = temp.get(1).split("/");
        for (String i : badIOGTemp) {
            String[] badIOGTemp2 = i.split(",");
            if (badIOGTemp2.length >= 3) {
                String badIOGName = badIOGTemp2[0];
                Point badIOGPoint = new Point(CommandParsing.isNumber(badIOGTemp2[1]) ? Integer.parseInt(badIOGTemp2[1]) : 0, CommandParsing.isNumber(badIOGTemp2[1]) ? Integer.parseInt(badIOGTemp2[2]) : 0);
                badIOG.add(new Inventory(1, new Image(path + "/null.png"), true, true, true, true, new Point(0, 0), Color.white, true, new Image(path + "/null.png"), new Point(0, 0), badIOGName, badIOGPoint, true, "This is the description"));
            }
        }
        map = new Grid(Color.white, true, new Image(path + "null.png"), "Map", new Point(0, 0));
        map.setLocation(new Point(CommandParsing.isNumber(temp.get(2)) ? Integer.parseInt(temp.get(2)) : 0, CommandParsing.isNumber(temp.get(3)) ? Integer.parseInt(temp.get(3)) : 0));
        convo = new Conversation(new Image(path + "extracted/images/gui/convoUI.png"), new Image(path + "null.png"), font15, Color.white, new Image(path + "null.png"), "Convo", false);
        gameUI = new UserInterfaceGame(new Image(path + "extracted/images/gui/gameUI.png"), new Image(path + "extracted/images/gui/12.png"));
        FileRead file = new FileRead(path + "extracted/text/keys/allowedCommands.txt");
        String[] temp2 = file.readTxt();
        ArrayList<String> tempS = new ArrayList<String>();
        tempS.addAll(Arrays.asList(temp2));
        FileRead fileT = new FileRead(gameMain.path + "/extracted/text/maps/mapde/" + map.location.getX() + "," + map.location.getY() + ".txt");
        String[] qText = fileT.readTxt();
        String qTextConcat = "Go Away, \ntheres nothing here";
        if (qText != null) {
            qTextConcat = "";
            for (String i : qText) {
                qTextConcat += (i + "\n");
            }
        }
        FileRead fileQ = new FileRead(path + "extracted/text/quest/main.txt");
        String[] tempQ = fileQ.readTxt();
        popupQuest.setMessage(qTextConcat);
        items = new Grid(Color.white, true, new Image(path + "null.png"), "Items", new Point(0, 0));
        charecters = new Grid(Color.white, true, new Image(path + "null.png"), "Charecters", new Point(0, 0));
        commands = new CommandParsing("", map.location);
        commands.setCommands(tempS);
        ArrayList<String> tempQ2 = new ArrayList<String>();
        tempQ2.addAll(Arrays.asList(tempQ));
        quest = new Quest(false, tempQ2);
        map.loadMap();
        items.loadInventory();
        charecters.loadCharecters();
    }

    @Override
    public void update(GameContainer gc, int delta) throws SlickException {
        if (!field.hasFocus()) {
            field.setFocus(true);
        }
        if (mainMenu.isMenuIsActive()) {
            nufeeld.stopSongAS();
            if (System.nanoTime() >= time) {
                if (playSong) {
                    menyoo.playSongAS();
                }
                time = System.nanoTime() + MENYOO_LENGTH;
            }
        } else {
            if (menyoo != null) menyoo.stopSongAS();
            if (!hasPlayed && playSong) {
                nufeeld.playSongAS();
                time = System.nanoTime() + NUFEELD_LENGTH;
                hasPlayed = true;
            }
            if (!mainMenu.isMenuIsActive()) {
                if (!playSong) {
                    nufeeld.stopSongAS();
                }
                if (System.nanoTime() >= time) {
                    nufeeld.stopSongAS();
                    if (playSong) {
                        nufeeld.playSongAS();
                    }
                    time = System.nanoTime() + NUFEELD_LENGTH;
                }
            }
            Input input = gc.getInput();
            if (intro || end) {
                if (input.isKeyPressed(Input.KEY_ENTER)) {
                    if (intro) field.setText("");
                    intro = false;
                    if (end) {
                        end = false;
                        field.setText("");
                        time = System.nanoTime();
                        mainMenu.menuIsActive = true;
                    }
                }
            } else if (!quest.isCompleate) {
                if (!convo.isActive) {
                    if (!field.isAcceptingInput()) {
                        field.setAcceptingInput(true);
                    }
                    if (input.isKeyPressed(Input.KEY_ENTER)) {
                        commands.setCommandEntered(field.getText());
                        quest.checkAction(field.getText());
                        popup.setMessage(commands.checkCommandEntry());
                        field.setText("");
                    }
                }
                if (convo.isActive) {
                    if (field.isAcceptingInput()) {
                        field.setAcceptingInput(false);
                    }
                    commands.checkCommandConvo(input);
                }
                if (combat.isActive) {
                    if (field.isAcceptingInput()) {
                        field.setAcceptingInput(false);
                    }
                    commands.checkCommandCombat(input);
                    combat.loadMenu(combatMMove);
                }
            }
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (mainMenu.menuIsActive) {
            if (mainMenu.getCreates().length != mainMenu.getAdventures().length) {
                try {
                    mainMenu.setCreates(gc);
                } catch (ZipException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            if (mainMenu.getLoads().length != mainMenu.getSaves().length) {
                try {
                    mainMenu.setLoads(gc);
                } catch (ZipException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            mainMenu.renderAllUI(gc, g);
        } else if (intro) {
            convo.renderI();
        } else if (end) {
            convo.renderE();
        } else if (convo.isActive) {
            convo.render();
        } else if (combat.isActive) {
            combat.render();
        } else if (!quest.isCompleate) {
            gameUI.renderUI(gc, g);
            field.render(gc, g);
            map.renderMap();
            items.renderInventory();
            charecters.renderCharecters();
            for (Character i : currentCharecters) {
                i.attemptMove();
            }
            popup.renderTextBottem();
            popupQuest.renderTextSide();
        }
    }

    /**
     * Loads a saved game and returns its information
     * @param save The name of the saved game with respect to the program's place of execution
     * @return An array list containing the saved data in the order Inventory:badIOG:Xlocation:Ylocation Items in Arraylists are seperated by '/'
     */
    public static ArrayList<String> loadGame(String save) {
        ArrayList<String> temp = new ArrayList<String>();
        String saveText = "", decrypt = "";
        FileRead fr = new FileRead(gameMain.path + "saves/" + name + ".pbz");
        for (String i : fr.readTxt()) {
            saveText += i;
        }
        decrypt = fromHex(saveText);
        String temp1 = "";
        String sections[] = decrypt.split(":");
        for (int i = 0; i < sections.length; i++) {
            temp1 += sections[i];
            temp.add(temp1);
            temp1 = "";
        }
        return temp;
    }

    /**
     * Translates a given string into Hexadecimal from ASCII
     * @param temp The string to be translated
     * @return The hex interpretation of that string
     */
    public static String toHex(String temp) {
        String temp2 = "";
        ArrayList<Integer> numTemp = new ArrayList<Integer>();
        for (int i = 0; i < temp.length(); i++) {
            numTemp.add((int) temp.charAt(i));
        }
        for (int i = 0; i < numTemp.size(); i++) {
            temp2 += " " + Integer.toHexString(numTemp.get(i));
        }
        return temp2;
    }

    /**
     * Translates a given string from Hexadecimal to ASCII 
     * @param temp A string of Hexadecimal representations of ASCII characters
     * @return The ASCII decoded from Hexadecimal
     */
    public static String fromHex(String temp) {
        String temp2 = "", temp3 = "";
        String[] tempHex = temp.split(" ");
        String[] tempInt;
        for (int i = 1; i < tempHex.length; i++) {
            temp2 += " " + Integer.toString(Integer.parseInt(tempHex[i], 16), 10);
        }
        tempInt = temp2.split(" ");
        for (int i = 1; i < tempInt.length; i++) {
            temp3 += (char) Integer.parseInt(tempInt[i]);
        }
        return temp3;
    }

    /**
     * Checks if a certain component was activated
     * @param source 
     */
    @Override
    public void componentActivated(AbstractComponent source) {
    }

    /**
     * The distance equation, because I couldn't find one that I liked
     * @param x1 X of point1
     * @param y1 Y of point1
     * @param x2 X of point2
     * @param y2 Y of point2
     * @return  The distance from (X1,Y1) to (X2,Y2)
     */
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * The main method, grabs the path that the game is running from and stores it in String gameMain.path
     * @param args commandLine arguments, unused
     * @throws SlickException 
     */
    public static void main(String[] args) throws SlickException {
        hasPlayed = false;
        FileRead fr = new FileRead("");
        path = fr.pathRun();
        if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            path = "/" + path;
        }
        System.out.println("Running in " + path);
        path = path.substring(0, path.length() - 8);
        AppGameContainer app = new AppGameContainer(new gameMain());
        app.setDisplayMode(1280, 640, false);
        app.setIcon(path + "resources/32x32.png");
        app.start();
    }
}
