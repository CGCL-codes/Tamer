package creid.mythos;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JOptionPane;
import creid.mythos.action.Movement;
import creid.mythos.actor.Actor;
import creid.mythos.actor.ActorQueue;
import creid.mythos.actor.BodyPart;
import creid.mythos.actor.Player;
import creid.mythos.data.CreatureData;
import creid.mythos.data.GameData;
import creid.mythos.data.ItemData;
import creid.mythos.data.ItemData.ItemID;
import creid.mythos.data.RegionData;
import creid.mythos.data.SpecialRoomData;
import creid.mythos.data.TerrainData;
import creid.mythos.data.TerrainData.TID;
import creid.mythos.graphics.Colors;
import creid.mythos.graphics.GameWindow;
import creid.mythos.graphics.Sprite;
import creid.mythos.item.Item.Slot;
import creid.mythos.place.Cell;
import creid.mythos.place.Map;
import creid.mythos.util.CommandProcessor;
import creid.mythos.util.KeyInput;
import creid.mythos.util.MessageLog;
import creid.mythos.util.CommandProcessor.InputMode;

public class Mythos {

    public static final String VERSION = "0.6.0";

    public static final String TITLE = "Mythos " + VERSION;

    public static final boolean RELEASE_VERSION = false;

    public static final boolean DEBUG_MAP_LEVEL = false;

    public static final boolean DEBUG_NO_MONSTER_GEN = false;

    public static final boolean DEBUG_NO_ITEM_GEN = false;

    public static final boolean DEBUG_NO_FEATURE_GEN = false;

    public static final boolean DEBUG_NO_LIGHTING = false;

    public static final boolean DEBUG_RESPAWN = false;

    public static final boolean DEBUG_FOV = false;

    public static final boolean DEBUG_AI = false;

    public static final boolean DEBUG_IMMORTAL = false;

    public static final boolean DEBUG_RNG = false;

    public static final boolean DEBUG_FIX_RNG = false;

    public static final String DATA_FILE_DIR_NAME = "data";

    public static final String MAIN_MENU_FILE = "title.txt";

    public enum State {

        STARTUP, MAIN_MENU, NEW, LOAD, PLAY, WAIT_INPUT, SAVE, QUIT, EXIT
    }

    public static final int NORMAL_EXIT = 0;

    public static final int NEW_GAME = 0;

    public static Mythos game = null;

    private State state;

    private CommandProcessor commander;

    private GameWindow display;

    private Map currentLevel;

    private long turn;

    private Player player;

    private ActorQueue actors;

    private MessageLog msgHistory;

    private static Random RNG = null;

    public Cell winningCircle;

    public Mythos() {
        setState(State.STARTUP);
        setCommander(new CommandProcessor(this));
        initGUI();
    }

    private void initGUI() {
        setDisplay(new GameWindow());
        getDisplay().addKeyListener(new KeyInput(getCommander()));
        getDisplay().setFocusable(true);
        if (!getDisplay().hasFocus()) getDisplay().requestFocus();
        new Thread(getDisplay()).start();
    }

    public void setDisplay(GameWindow display) {
        this.display = display;
    }

    public GameWindow getDisplay() {
        return display;
    }

    public void setCommander(CommandProcessor commander) {
        this.commander = commander;
    }

    public CommandProcessor getCommander() {
        return commander;
    }

    public void setCurrentLevel(Map currentLevel) {
        this.currentLevel = currentLevel;
        getDisplay().changeMap(currentLevel);
    }

    public Map getCurrentLevel() {
        return currentLevel;
    }

    public void setHistory(MessageLog history) {
        this.msgHistory = history;
    }

    public MessageLog getHistory() {
        return msgHistory;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setActors(ActorQueue actors) {
        this.actors = actors;
    }

    public ActorQueue getActors() {
        return actors;
    }

    private void setTurn(long turn) {
        this.turn = turn;
    }

    public long getTurn() {
        return turn;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    private void createPlayerCharacter() {
        BodyPart[] playerParts = { new BodyPart("Main Hand", Slot.WEAPON, null, "(fists)"), new BodyPart("Off Hand", Slot.OFFHAND, null, "(nothing)"), new BodyPart("Head", Slot.HEAD, null, "(nothing)"), new BodyPart("Body", Slot.BODY, null, "(clothes)"), new BodyPart("Hands", Slot.HANDS, null, "(nothing)"), new BodyPart("Feet", Slot.FEET, null, "(nothing)"), new BodyPart("Left Ring", Slot.RING, null, "(nothing)"), new BodyPart("Right Ring", Slot.RING, null, "(nothing)"), new BodyPart("Neck", Slot.NECK, null, "(nothing)") };
        setPlayer(new Player("Player", new Sprite('@', Colors.WHITE), Movement.WALK, 1L, Actor.AVERAGE, playerParts));
        getPlayer().getInventory().stowItem(GameData.items.get(ItemID.POTION_HEALTH1));
    }

    public void newGame() {
        initRNG(NEW_GAME);
        currentLevel = null;
        turn = 0;
        setHistory(new MessageLog());
        setActors(new ActorQueue());
        TerrainData.initTerrain();
        CreatureData.initCreatures();
        ItemData.initItems();
        SpecialRoomData.initSpecialRooms();
        RegionData.initRegions();
        winningCircle = new Cell(null, GameData.terrainTypes.get(TID.FLOOR), -100, -100);
        setTurn(1);
        RegionData.initRegions();
        createPlayerCharacter();
        loadMap(GameData.regions.get(RegionData.RegionID.SHORT_DUNGEON).getMap(0));
        if (!getCurrentLevel().randomlyPlaceCreature(getPlayer())) {
            debugLog("Failed to place player on map!");
            System.exit(1);
        }
        getPlayer().calcStats();
        getDisplay().showPlayerStats();
        getDisplay().showMap();
        setState(State.PLAY);
        play();
    }

    private int play() {
        getCurrentLevel().updateFOV(getPlayer().getLocation());
        getDisplay().update(this);
        getCommander().setCommandMode(InputMode.READY);
        while (getState() == State.PLAY) {
            while (!getActors().isEmpty() && getActors().peek().getNextTurn() <= getTurn() && getState() == State.PLAY) {
                Actor actor = getActors().peek();
                long lastTurn = actor.getNextTurn();
                if (actor.getNextTurn() == Actor.INACTIVE) {
                    getActors().poll();
                    continue;
                }
                actor.takeTurn();
                Mythos.debugLog(actor.getName("The") + " acted on turn " + getTurn() + ", next turn is " + actor.getNextTurn());
                if (actor.getNextTurn() > lastTurn) {
                    getActors().poll();
                    getActors().add(actor);
                }
                getDisplay().update(this);
            }
            tick();
        }
        setState(State.QUIT);
        clearMessages();
        getDisplay().hideStats();
        setHistory(new MessageLog());
        return 0;
    }

    public void gameOver(String cause) {
        JOptionPane.showMessageDialog(null, cause, "Game Over", JOptionPane.PLAIN_MESSAGE);
        setState(State.QUIT);
    }

    private void tick() {
        turn++;
    }

    public void clearMessages() {
        for (int i = 0; i < 4; i++) Mythos.log("");
    }

    public static void log(String message) {
        Mythos.log(message, Colors.WHITE);
    }

    public static void transientLog(String message, Color color) {
        Mythos.game.getDisplay().writeTransientMessage(message, color);
    }

    public static void log(String message, Color color) {
        Mythos.game.getDisplay().writeGameMessage(message, color);
        Mythos.game.getHistory().logMessage(message, color);
        debugLog(message);
    }

    public static void debugLog(String message) {
        if (!RELEASE_VERSION) {
            System.out.println(Mythos.game.getTurn() + "." + message);
        }
    }

    public void loadMap(Cell c) {
        if (getCurrentLevel() != null) {
            getActors().clear();
        }
        setCurrentLevel(c.getMap());
        getCurrentLevel().placeCreature(getPlayer(), c.getX(), c.getY());
        for (Actor a : getCurrentLevel().getActors()) getActors().add(a);
        getDisplay().setPlaceName(getCurrentLevel().getName());
    }

    public void loadMap(Map map) {
        if (getCurrentLevel() != null) {
            getActors().clear();
        }
        setCurrentLevel(map);
        getCurrentLevel().randomlyPlaceCreature(getPlayer());
        for (Actor a : getCurrentLevel().getActors()) getActors().add(a);
        getDisplay().setPlaceName(getCurrentLevel().getName());
    }

    public static BufferedReader openDataFile(String name) {
        BufferedReader fin = null;
        try {
            if (RELEASE_VERSION) {
                String path = "/" + DATA_FILE_DIR_NAME + "/" + name;
                InputStream downStream = Mythos.class.getResourceAsStream(path);
                fin = new BufferedReader(new InputStreamReader(downStream));
            } else {
                fin = new BufferedReader(new FileReader(DATA_FILE_DIR_NAME + File.separator + name));
            }
        } catch (FileNotFoundException ex) {
            Mythos.debugLog("Error: could not open file " + name + ":\n" + ex);
        }
        return fin;
    }

    public String[] getFileText(String file) {
        BufferedReader reader = Mythos.openDataFile(file);
        LinkedList<String> text = new LinkedList<String>();
        try {
            while (reader.ready()) {
                String line = reader.readLine();
                switch(getState()) {
                    case MAIN_MENU:
                        if (line.contains("%VERSION%")) line = line.replace("%VERSION%", VERSION);
                        break;
                }
                text.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toArray(new String[0]);
    }

    public void shutDown(int code) {
        getDisplay().stop();
        System.exit(code);
    }

    public void saveGame(Mythos game) {
    }

    public void loadGame(String saveFile) {
    }

    void initRNG(long seed) {
        if (seed == NEW_GAME) if (DEBUG_FIX_RNG) seed = 1321303409800L; else seed = System.currentTimeMillis();
        Mythos.RNG = new Random(seed);
        if (DEBUG_RNG) Mythos.debugLog("Seed=" + seed);
    }

    public static int callRNG(int min, int max) {
        int minMod = 0;
        int maxMod = 0;
        int value;
        if (min > max) {
            Mythos.debugLog("Bad values for callRNG(" + min + "," + max + ")");
            return -777;
        }
        if (min < 0) {
            minMod = min * -1;
            min += minMod;
            max += minMod;
        }
        if (max < 0) {
            maxMod = max * -1;
            min += maxMod;
            max += maxMod;
        }
        value = min + RNG.nextInt(max + 1 - min);
        value -= minMod;
        value -= maxMod;
        return value;
    }

    public static void main(String[] args) {
        Mythos.game = new Mythos();
        game.setState(State.MAIN_MENU);
        game.getDisplay().showText(game.getFileText(MAIN_MENU_FILE));
        while (game.getState() != State.EXIT) {
            game.getCommander().setCommandMode(InputMode.MAIN_MENU);
            game.setCurrentLevel(null);
            game.getDisplay().showMap();
            game.getDisplay().showText(game.getFileText(MAIN_MENU_FILE));
            while (game.getState() == State.MAIN_MENU) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            switch(game.getState()) {
                case NEW:
                    game.newGame();
                    game.setState(State.MAIN_MENU);
                    break;
                case LOAD:
                    JOptionPane.showMessageDialog(null, "Save/Load functionality is not yet implemented. Sorry.", "Under Construction", JOptionPane.PLAIN_MESSAGE);
                    game.setState(State.MAIN_MENU);
                    break;
                case EXIT:
                    break;
                default:
                    game.setState(State.MAIN_MENU);
            }
        }
        game.shutDown(NORMAL_EXIT);
    }
}
