package fr.uha.ensisa.ir.walther.milcityblue;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import fr.uha.ensisa.ir.walther.milcityblue.bluetooth.StartClient;
import fr.uha.ensisa.ir.walther.milcityblue.bluetooth.StartServer;
import fr.uha.ensisa.ir.walther.milcityblue.core.Config;
import fr.uha.ensisa.ir.walther.milcityblue.core.Game;
import fr.uha.ensisa.ir.walther.milcityblue.core.GameListener;
import fr.uha.ensisa.ir.walther.milcityblue.core.Grid;
import fr.uha.ensisa.ir.walther.milcityblue.core.GridBuilder;
import fr.uha.ensisa.ir.walther.milcityblue.core.Size;
import fr.uha.ensisa.ir.walther.milcityblue.core.gridbuilders.RandomWalkGridBuilder;
import fr.uha.ensisa.ir.walther.milcityblue.core.players.AIPlayer;
import fr.uha.ensisa.ir.walther.milcityblue.core.players.LocalPlayer;
import fr.uha.ensisa.ir.walther.milcityblue.gui.CursorView;
import fr.uha.ensisa.ir.walther.milcityblue.gui.DisplayManager;
import fr.uha.ensisa.ir.walther.milcityblue.gui.GameEngine;
import fr.uha.ensisa.ir.walther.milcityblue.gui.GridView;
import fr.uha.ensisa.ir.walther.milcityblue.gui.ImageDrawer;
import fr.uha.ensisa.ir.walther.milcityblue.gui.MenuView;
import fr.uha.ensisa.ir.walther.milcityblue.gui.MilCityModel;
import fr.uha.ensisa.ir.walther.milcityblue.gui.Splash;
import fr.uha.ensisa.ir.walther.milcityblue.tools.ParametersStore;

public class MilCityBlue extends MIDlet implements CommandListener, GameListener {

    private GameEngine engine;

    private Display display;

    private Command exit;

    private Command pause;

    private Command resume;

    private Command next;

    private Command back;

    private Game game;

    private MilCityModel mcModel;

    private ImageDrawer imageDrawer;

    private DisplayManager displayManager;

    private ParametersStore parametersStore;

    private Displayable mainMenu, soloMenu, multiMenu, optionMenu, helpMenu, howToPlayForm, commandsForm, buildingsAndWeaponsForm, aboutForm, bluetoothHelp;

    public MilCityBlue() {
        this.display = Display.getDisplay(this);
        this.imageDrawer = new ImageDrawer();
        this.displayManager = new DisplayManager(this.display);
    }

    protected void startApp() throws MIDletStateChangeException {
        this.back = new Command("Back", Command.EXIT, 1);
        this.next = new Command("OK", Command.ITEM, 0);
        this.next = new Command("Select", Command.ITEM, 0);
        this.mainMenu = getMainScreen();
        this.displayManager.setFirst(mainMenu);
        Thread displaysplashs = new Thread() {

            public void run() {
                Splash splash1 = new Splash("/Splash_MilCity_Koojoo.png", 0xFFFFFF);
                splash1.show(display, mainMenu, 3000);
            }
        };
        displaysplashs.start();
        this.imageDrawer.init();
        this.parametersStore = new ParametersStore("MilCityBlue_Config");
        String tmp;
        if ((tmp = this.parametersStore.get(1)) != null) Config.ENABLE_SOUND = (tmp.equals("TRUE")) ? true : false;
        if ((tmp = this.parametersStore.get(2)) != null) Config.ENABLE_VIBRATION = (tmp.equals("TRUE")) ? true : false;
        if ((tmp = this.parametersStore.get(3)) != null) Config.DEFAULT_PERCENT_OF_WATER = (Integer.parseInt(tmp));
        if ((tmp = this.parametersStore.get(4)) != null) Config.DEFAULT_SCORE_TO_WIN = (Integer.parseInt(tmp));
        if ((tmp = this.parametersStore.get(5)) != null) Config.DEFAULT_MONEY_AT_START = (Integer.parseInt(tmp));
        this.parametersStore.close();
        this.parametersStore = null;
        this.soloMenu = getSoloScreen();
        this.multiMenu = getMultiScreen();
        this.optionMenu = getOptionScreen();
        this.helpMenu = getHelpMenu();
        this.commandsForm = getCommandsForm();
        this.howToPlayForm = getHowToPlayForm();
        this.buildingsAndWeaponsForm = getBuildingsAndWeaponsForm();
        this.aboutForm = getAboutForm();
        this.bluetoothHelp = getBluetoothHelpForm();
        this.resume = new Command("Resume", Command.SCREEN, 2);
        this.pause = new Command("Pause", Command.SCREEN, 1);
        this.exit = new Command("Exit", Command.EXIT, 1);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this.exit) {
            game.stop();
        } else if (c == this.resume) this.game.resume(); else if (c == this.pause) this.game.pause(); else if (c == this.back && d == this.mainMenu) try {
            this.destroyApp(true);
        } catch (MIDletStateChangeException e) {
            e.printStackTrace();
        } finally {
            this.notifyDestroyed();
        } else if (c == this.back) this.displayManager.back(); else if (c == this.next) {
            if (d == this.mainMenu) switch(((List) this.mainMenu).getSelectedIndex()) {
                case 0:
                    this.displayManager.next(soloMenu);
                    break;
                case 1:
                    this.displayManager.next(multiMenu);
                    break;
                case 2:
                    this.displayManager.next(optionMenu);
                    break;
                case 3:
                    this.displayManager.next(helpMenu);
                    break;
                case 4:
                    this.displayManager.next(aboutForm);
                    break;
                case 5:
                    this.notifyDestroyed();
                    break;
            } else if (d == this.soloMenu) switch(((List) this.soloMenu).getSelectedIndex()) {
                case 0:
                    startSoloGame(0);
                    break;
                case 1:
                    startSoloGame(1);
                    break;
                case 2:
                    startSoloGame(2);
                    break;
                case 3:
                    startSoloGame(3);
                    System.out.println("This is Madness !\nMadness ? THIS IS SPARTAAAAAAAAA !");
                    break;
                case 4:
                    startSoloGame(4);
                    break;
            } else if (d == this.multiMenu) switch(((List) this.multiMenu).getSelectedIndex()) {
                case 0:
                    StartServer startServer = new StartServer(this);
                    this.displayManager.next(startServer);
                    startServer.start();
                    break;
                case 1:
                    StartClient startClient = new StartClient(this);
                    this.displayManager.next(startClient);
                    startClient.start();
                    break;
                case 2:
                    this.displayManager.next(bluetoothHelp);
                    break;
            } else if (d == this.optionMenu) {
                switch(((List) this.optionMenu).getSelectedIndex()) {
                    case 0:
                        Config.ENABLE_VIBRATION = !Config.ENABLE_VIBRATION;
                        String tmp1 = (Config.ENABLE_VIBRATION) ? "Yes" : "No";
                        ((List) this.optionMenu).set(0, "Vibration : " + tmp1, null);
                        break;
                    case 1:
                        Config.DEFAULT_PERCENT_OF_WATER += 5;
                        if (Config.DEFAULT_PERCENT_OF_WATER > 95) Config.DEFAULT_PERCENT_OF_WATER = 5;
                        ((List) this.optionMenu).set(1, "% of Water : " + String.valueOf(Config.DEFAULT_PERCENT_OF_WATER) + "%", null);
                        break;
                    case 2:
                        Config.DEFAULT_SCORE_TO_WIN += 500;
                        if (Config.DEFAULT_SCORE_TO_WIN > 10000) Config.DEFAULT_SCORE_TO_WIN = 500;
                        ((List) this.optionMenu).set(2, "Max Score : " + String.valueOf(Config.DEFAULT_SCORE_TO_WIN), null);
                        break;
                    case 3:
                        Config.DEFAULT_MONEY_AT_START += 50;
                        if (Config.DEFAULT_MONEY_AT_START > 1000) Config.DEFAULT_MONEY_AT_START = 50;
                        ((List) this.optionMenu).set(3, "Default Money : " + String.valueOf(Config.DEFAULT_MONEY_AT_START), null);
                        break;
                }
                saveParameters();
            } else if (d == this.helpMenu) {
                switch(((List) this.helpMenu).getSelectedIndex()) {
                    case 0:
                        this.displayManager.next(commandsForm);
                        break;
                    case 1:
                        this.displayManager.next(howToPlayForm);
                        break;
                    case 2:
                        this.displayManager.next(buildingsAndWeaponsForm);
                        break;
                }
            } else this.displayManager.next(mainMenu);
        }
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        saveParameters();
        if (this.game != null) this.game.stop();
    }

    protected void pauseApp() {
    }

    public void startGameBegin() {
        this.game = new Game();
        this.game.setGameListener(this);
        this.mcModel = new MilCityModel(this.game, 0, 0, 240, 240, 8, new Size(2, 1));
        this.engine = new GameEngine(mcModel, 10);
        this.game.setEngine(this.engine);
        Displayable drawingArea = this.engine.getDrawingArea();
        drawingArea.setCommandListener(this);
        drawingArea.addCommand(this.resume);
        drawingArea.addCommand(this.pause);
        drawingArea.addCommand(this.exit);
    }

    public void startGameEnd() {
        MenuView mView = new MenuView(game, mcModel, GameEngine.keyToString(GameCanvas.GAME_A), GameEngine.keyToString(GameCanvas.GAME_B), GameEngine.keyToString(GameCanvas.GAME_C), GameEngine.keyToString(GameCanvas.GAME_D), imageDrawer);
        CursorView cView = new CursorView(this.mcModel);
        GridView gView = new GridView(8, imageDrawer, mcModel);
        this.engine.attach(mView);
        this.engine.attach(gView);
        this.engine.attach(cView);
        this.display.setCurrent(this.engine.getDrawingArea());
        this.game.start();
    }

    private void startSoloGame(int mode) {
        startGameBegin();
        this.game.registerLocalPlayer(new LocalPlayer());
        Grid grid;
        GridBuilder gb = new RandomWalkGridBuilder();
        grid = gb.build(Config.DEFAULT_GRID_SIZE_X, Config.DEFAULT_GRID_SIZE_Y, Config.DEFAULT_PERCENT_OF_WATER);
        gb = null;
        this.game.getLocalPlayer().setPlayerIndex(this.game.getLocalPlayerIndex());
        this.game.getLocalPlayer().setGame(this.game);
        this.game.getLocalPlayer().setGrid(grid);
        if (mode <= AIPlayer.LEVEL_MADNESS) {
            AIPlayer aiPlayer = new AIPlayer((byte) mode);
            aiPlayer.setGame(this.game);
            aiPlayer.setPlayerIndex(this.game.registerRemotePlayer(aiPlayer));
            aiPlayer.setGrid(new Grid(Config.DEFAULT_GRID_SIZE_X, Config.DEFAULT_GRID_SIZE_Y, grid.getGrid()));
            System.out.println(aiPlayer.getStatusGrid());
        }
        startGameEnd();
    }

    private List getMainScreen() {
        List list = new List("MilCity - Blue Edition", List.IMPLICIT);
        list.append("Solo Game", null);
        list.append("Bluetooth Game", null);
        list.append("Options", null);
        list.append("Help", null);
        list.append("About", null);
        list.append("Exit", null);
        list.setCommandListener(this);
        list.addCommand(this.back);
        list.addCommand(this.next);
        list.setSelectCommand(this.next);
        return list;
    }

    private List getSoloScreen() {
        List list = new List("MilCity :: Solo Game", List.IMPLICIT);
        list.append("Easy Mode", null);
        list.append("Medium Mode", null);
        list.append("Hard Mode", null);
        list.append("Madness Mode", null);
        list.append("Training Mode", null);
        list.setCommandListener(this);
        list.addCommand(this.back);
        list.addCommand(this.next);
        list.setSelectCommand(this.next);
        return list;
    }

    private List getMultiScreen() {
        List list = new List("MilCity :: Multiplayer Game", List.IMPLICIT);
        list.append("As Server", null);
        list.append("As Client", null);
        list.append("What's that ?", null);
        list.setCommandListener(this);
        list.addCommand(this.back);
        list.addCommand(this.next);
        list.setSelectCommand(this.next);
        return list;
    }

    private List getOptionScreen() {
        String tmp;
        List list = new List("MilCity :: Options", List.IMPLICIT);
        tmp = (Config.ENABLE_VIBRATION) ? "Yes" : "No";
        list.append("Vibration : " + tmp, null);
        list.append("% of Water : " + String.valueOf(Config.DEFAULT_PERCENT_OF_WATER) + "%", null);
        list.append("Max Score : " + String.valueOf(Config.DEFAULT_SCORE_TO_WIN), null);
        list.append("Default Money : " + String.valueOf(Config.DEFAULT_MONEY_AT_START), null);
        list.setCommandListener(this);
        list.addCommand(this.back);
        list.addCommand(this.next);
        list.setSelectCommand(this.next);
        return list;
    }

    private List getHelpMenu() {
        List list = new List("MilCity :: Help", List.IMPLICIT);
        list.append("Commands", null);
        list.append("How To Play", null);
        list.append("Buildings", null);
        list.setCommandListener(this);
        list.addCommand(this.back);
        list.addCommand(this.next);
        list.setSelectCommand(this.next);
        return list;
    }

    private Form getCommandsForm() {
        Form form = new Form("MilCity :: Commands");
        StringBuffer sb = new StringBuffer();
        sb.append("Commands available in game :\n   ").append(GameEngine.keyToString(GameCanvas.FIRE)).append(" : Build / Fire.\n   ").append(GameEngine.keyToString(GameCanvas.UP)).append(" : Up.\n   ").append(GameEngine.keyToString(GameCanvas.DOWN)).append(" : Down.\n   ").append(GameEngine.keyToString(GameCanvas.LEFT)).append(" : Left.\n   ").append(GameEngine.keyToString(GameCanvas.RIGHT)).append(" : Right.\n   ").append(GameEngine.keyToString(GameCanvas.GAME_A)).append(" : Previous building / weapon.\n   ").append(GameEngine.keyToString(GameCanvas.GAME_B)).append(" : Next building / weapon.\n   ").append(GameEngine.keyToString(GameCanvas.GAME_C)).append(" : Previous playing mode.\n   ").append(GameEngine.keyToString(GameCanvas.GAME_D)).append(" : Next playing mode.\n\n").append("In CITY mode, you can create buildings, and place shields if any.\n").append("In MILITARY mode, you can fire on target player with selected weapons.\n\n").append("Enjoy ^^");
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.back);
        return form;
    }

    private Form getHowToPlayForm() {
        Form form = new Form("MilCity :: How to play");
        StringBuffer sb = new StringBuffer();
        sb.append("In MilCity, you have to build your own city and to destroy your opponents. The game is divided in two main modes : CITY mode, and MILITARY mode.\n\n").append("CITY MODE :\n").append("In CITY mode, you can build the city. For that, select building type you want to construct, and press the action key to build. The cursor is red when you can't build there, or when you don't have enough money.\n").append("Each building costs money and consumes energy. Amounts are located after the building name in building type selection menu.\n").append("If your energy amount went bellow 0, you cannot produce any weapons/shields, and construction times are doubled. If you can't construct enough PowerPlants to raise your energy amount over 0, you'll lose. Moreover, you have only ").append(Config.MAX_TIME_BELLOW_ZERO).append(" seconds to raise your energy level over 0, otherwise you'll lose.\n\n").append("MILITARY MODE :\n").append("In MILITARY mode, you can view a radar map which represents status of your opponent. Current player targeted is indicated after the word MILITARY in mode selection.\n").append("A BLUE cell means that there is water here, so it is useless to fire here.\n").append("A BLACK cell means that you haven't fired there.\n").append("A GREY cell mends that you've fired there, but nothing has been hit (or maybe there was a shield?). Grey intensity grows with time.\n").append("A RED cell meands that a building has been hit there, but not destroyed yet. \n").append("A GREEN cell means that a building has been destroyed there.\n").append("A YELLOW cell meands that something has been discovered there.\n").append("Cursor is green when you can fire with selected weapon, and red when you can't. You must have enough ammos to fire, and, regarding weapon power, you have to wait a certain time between each shots.\n\n").append("Enjoy ^^");
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.back);
        return form;
    }

    private Form getBuildingsAndWeaponsForm() {
        Form form = new Form("MilCity :: Buildings");
        StringBuffer sb = new StringBuffer();
        sb.append("BUILDINGS :\n").append("   PowerPlant : Produces energy\n").append("   Refinery : Produces money\n").append("   House : Accelerates construction times, production and reuse delays\n").append("   Missile Silo : Produces missiles\n").append("   Factory : Produces mortars\n").append("   Air Field : Produces air crafts\n").append("   Nuke Silo : Produces nukes\n").append("   Laboratory : Produces spy satellites\n").append("   Shield Generator : Produces shields\n").append("   Shield : Proctects selected cells with a shield for ").append(Config.PRODUCTION_TIME_SHIELD).append("s\n\n").append("Enjoy ^^");
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.back);
        return form;
    }

    private Form getAboutForm() {
        Form form = new Form("MilCity :: About");
        StringBuffer sb = new StringBuffer();
        sb.append("MilCity - Blue :\n").append("   Version ").append(Config.GAME_VERSION).append("\n\n").append("Developed by :\n").append("   J�r�my WALTHER\n").append("   golflima5@gmail.com\n\n").append("Website :\n").append("   https://sourceforge.net/projects/milcity/\n\n").append("This game is free, and distributed under GPL v3 license.");
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.back);
        return form;
    }

    private Form getEndGameForm() {
        Form form = new Form("MilCity :: Game Over");
        StringBuffer sb = new StringBuffer();
        sb.append("Game Over\n\n");
        if (game.getWinnerIndex() != -1) {
            if (game.getWinnerIndex() == game.getLocalPlayerIndex()) sb.append("   You WIN !\n\nYour Score is : ").append(game.getLocalPlayer().getScore()); else sb.append("   You LOSE !\n\nYour Score is : ").append(game.getLocalPlayer().getScore()).append("\nWinner score is : ").append(game.getWinnerScore()).append("\n\nWINNER IS PLAYER ").append(game.getWinnerIndex() + 1);
        }
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.next);
        return form;
    }

    private Form getBluetoothHelpForm() {
        Form form = new Form("MilCity :: Bluetooth");
        StringBuffer sb = new StringBuffer();
        sb.append("Bluetooth help :\n\n").append("As Server : Host the game, and allow other players to join your party. You should host only if you have a recent phone.\n\n").append("As Client : Join a party hosted by another player. Suitable for all phones.\n\nEnjoy ^^");
        form.append(sb.toString());
        form.setCommandListener(this);
        form.addCommand(this.back);
        return form;
    }

    protected void saveParameters() {
        this.parametersStore = new ParametersStore("MilCityBlue_Config");
        String tmp;
        tmp = (Config.ENABLE_SOUND) ? "TRUE" : "FALSE";
        this.parametersStore.set(1, tmp);
        tmp = (Config.ENABLE_VIBRATION) ? "TRUE" : "FALSE";
        this.parametersStore.set(2, tmp);
        this.parametersStore.set(3, String.valueOf(Config.DEFAULT_PERCENT_OF_WATER));
        this.parametersStore.set(4, String.valueOf(Config.DEFAULT_SCORE_TO_WIN));
        this.parametersStore.set(5, String.valueOf(Config.DEFAULT_MONEY_AT_START));
        this.parametersStore.close();
        this.parametersStore = null;
    }

    public GameEngine getEngine() {
        return engine;
    }

    public void setEngine(GameEngine engine) {
        this.engine = engine;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public MilCityModel getMcModel() {
        return mcModel;
    }

    public void setMcModel(MilCityModel mcModel) {
        this.mcModel = mcModel;
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public ImageDrawer getImageDrawer() {
        return imageDrawer;
    }

    public void notifyGameEvent(Game sender, byte eventType, int arg) {
        if (eventType == GameListener.GAMEEVENT_STOPPED || eventType == GameListener.GAMEEVENT_ENDED) {
            this.displayManager.next(getEndGameForm());
        }
        if (eventType == GameListener.GAMEEVENT_VIBRATE) if (Config.ENABLE_VIBRATION) display.vibrate(arg);
        System.out.println("GameListener : eventType = " + eventType);
    }
}
