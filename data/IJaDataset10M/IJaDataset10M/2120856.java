package catchemrpg.base;

import catchemrpg.functions.KeyboardInputMap;
import catchemrpg.enums.WindowState;
import catchemrpg.functions.MouseInputMap;
import catchemrpg.gameobjects.BattleInstance;
import catchemrpg.language.Language;
import catchemrpg.persistency.CharacterFileIO;
import catchemrpg.persistency.MonsterIO;
import java.awt.Color;

/**
 * A dumping class for variables. Try not to do small ones that aren't important,
 * unless they are static final x's as these are parsed automatically by the compiler
 * and have no runtime overhead.
 * @author Toby Pinder (Gigitrix)
 */
public class BaseVars {

    /**
     * The currently in-use save file. Main menu will change this to allow 
     * players to have many save files/folders.
     */
    public static String saveFile = "";

    /**
     * The current language strings for this system. Tese are dynamically loaded
     * in and can be accessed easily ussing BaseVars.lang.*
     */
    public static Language lang = new Language();

    /**
     * The enemy's monsters. If null, battle is won.
     */
    public static BattleInstance aiTeam;

    /**
     * A constant identifying who is accessed: You or the enemy in a battle. 
     * Could be replaced with an enum I guess
     */
    public static final int AITEAM_YOU = 1;

    /**
     * A constant identifying who is accessed: You or the enemy in a battle. 
     * Could be replaced with an enum I guess
     */
    public static final int AITEAM_ENEMY = 2;

    /**
     * The current state that the window is in. This means whether it is 
     * displaying a map, battle, transition animation, shell screen, gaem over 
     * screen etc.
     */
    public static WindowState state = WindowState.map;

    /**
     * The maximum number of "reserve" monsters the player is allowed in the 
     * game. Think the "Pokemon box system": the player can swap in any of his/her
     * caught creatures into their main party at will. Mostly just a limit to save 
     * people from creating large save files when they should just release creatures
     * that are unneccessary. This limit can be raised if you wish.
     */
    public static final int MAXRESERVEMONSTERS = 200;

    /**
     * The total width of the game window.
     */
    public static final int WINDOWWIDTH = 800;

    /**
     * The total height of the game window.
     */
    public static final int WINDOWHEIGHT = 600;

    /**
     * The width of the map portion of the gamewindow, when the map is displayed.
     */
    public static final int MAPWIDTH = 500;

    /**
     * The height of the map portion of the gamewindow, when the map is displayed.
     */
    public static final int MAPHEIGHT = 500;

    /**
     * The width of each map tile in the game.
     */
    public static final int MAPTILEWIDTH = 20;

    /**
     * The height of each map tile in the game.
     */
    public static final int MAPTILEHEIGHT = 20;

    /**
     * The number of tiles to offset from the top left corner to get to the main
     * characters location (ie the middle of the map). Since map scrolling 
     * scripts work in relation to the corner, just add this to MapViewer.setBlockX()
     * or whatever and you will make it work in relation to the character.
     */
    public static final int MAPTILEOFFSETX = 12;

    /**
     * The number of tiles to offset from the top left corner to get to the main
     * characters location (ie the middle of the map). Since map scrolling 
     * scripts work in relation to the corner, just add this to MapViewer.setBlockY()
     * or whatever and you will make it work in relation to the character.
     */
    public static final int MAPTILEOFFSETY = 12;

    /**
     * The target number of milliseconds taken per frame. For target FPS, 
     * calculate. 1000/TARGETMILLISPERFRAME
     */
    public static final int TARGETMILLISPERFRAME = 30;

    /**
     * Keyboard Listener.
     */
    public static final KeyboardInputMap keyboardInput = new KeyboardInputMap();

    /**
     * Mouse Listener.
     */
    public static final MouseInputMap mouseInput = new MouseInputMap();

    /**
     * A grey color, used alongside GREY2 for a stripe pattern in menus.
     */
    public static final Color GREY1 = new Color(50, 50, 50, 255);

    /**
     * A grey color, used alongside GREY2 for a stripe pattern in menus.
     */
    public static final Color GREY2 = new Color(30, 30, 30, 255);

    /**
     * Initialise the game. Currently does nothing, as language files are now handled (more sanely) in the Language() constructor!
     */
    public static void initialiseGame() {
        Language.init();
        MonsterIO monIO = new MonsterIO();
        monIO.loadProperties();
        CharacterFileIO load = new CharacterFileIO();
        load.loadProperties();
    }
}
