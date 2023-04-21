package mu.nu.nullpo.gui.slick;

import java.util.LinkedList;
import org.apache.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Mode select screen
 */
public class StateSelectMode extends DummyMenuScrollState {

    /** Logger */
    static Logger log = Logger.getLogger(StateSelectMode.class);

    /** This state's ID */
    public static final int ID = 3;

    /** Number of game modes in one page */
    public static final int PAGE_HEIGHT = 24;

    /** true if top-level folder */
    public static boolean isTopLevel;

    /** Current folder name */
    protected String strCurrentFolder;

    /**
	 * Constructor
	 */
    public StateSelectMode() {
        super();
        pageHeight = PAGE_HEIGHT;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    /**
	 * Prepare mode list
	 */
    protected void prepareModeList() {
        strCurrentFolder = StateSelectModeFolder.strCurrentFolder;
        LinkedList<String> listMode = null;
        if (isTopLevel) {
            listMode = StateSelectModeFolder.listTopLevelModes;
            list = new String[listMode.size() + 1];
            for (int i = 0; i < listMode.size(); i++) {
                list[i] = listMode.get(i);
            }
            list[list.length - 1] = "[MORE...]";
        } else {
            listMode = StateSelectModeFolder.mapFolder.get(strCurrentFolder);
            if (listMode != null) {
                list = new String[listMode.size()];
                for (int i = 0; i < list.length; i++) {
                    list[i] = listMode.get(i);
                }
            } else {
                list = NullpoMinoSlick.modeManager.getModeNames(false);
            }
        }
        maxCursor = list.length - 1;
        String lastmode = null;
        if (isTopLevel) {
            lastmode = NullpoMinoSlick.propGlobal.getProperty("name.mode.toplevel", null);
        } else if (strCurrentFolder.length() > 0) {
            lastmode = NullpoMinoSlick.propGlobal.getProperty("name.mode." + strCurrentFolder, null);
        } else {
            lastmode = NullpoMinoSlick.propGlobal.getProperty("name.mode", null);
        }
        cursor = getIDbyName(lastmode);
        if (cursor < 0) cursor = 0;
        if (cursor > list.length - 1) cursor = list.length - 1;
    }

    /**
	 * Get mode ID (not including netplay modes)
	 * @param name Name of mode
	 * @return ID (-1 if not found)
	 */
    protected int getIDbyName(String name) {
        if ((name == null) || (list == null)) return -1;
        for (int i = 0; i < list.length; i++) {
            if (name.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * Get game mode description
	 * @param str Mode name
	 * @return Description
	 */
    protected String getModeDesc(String str) {
        String str2 = str.replace(' ', '_');
        str2 = str2.replace('(', 'l');
        str2 = str2.replace(')', 'r');
        String result = NullpoMinoSlick.propModeDesc.getProperty(str2);
        if (result == null) {
            result = NullpoMinoSlick.propDefaultModeDesc.getProperty(str2, str2);
        }
        return result;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        prepareModeList();
    }

    @Override
    public void onRenderSuccess(GameContainer container, StateBasedGame game, Graphics graphics) {
        if (!isTopLevel && (strCurrentFolder.length() > 0)) {
            NormalFontSlick.printFontGrid(1, 1, strCurrentFolder + " (" + (cursor + 1) + "/" + list.length + ")", NormalFontSlick.COLOR_ORANGE);
        } else {
            NormalFontSlick.printFontGrid(1, 1, "MODE SELECT (" + (cursor + 1) + "/" + list.length + ")", NormalFontSlick.COLOR_ORANGE);
        }
        NormalFontSlick.printTTFFont(16, 440, getModeDesc(list[cursor]));
    }

    @Override
    protected boolean onDecide(GameContainer container, StateBasedGame game, int delta) {
        ResourceHolderSlick.soundManager.play("decide");
        if (isTopLevel && (cursor == list.length - 1)) {
            NullpoMinoSlick.propGlobal.setProperty("name.mode.toplevel", list[cursor]);
            game.enterState(StateSelectModeFolder.ID);
        } else {
            if (isTopLevel) {
                NullpoMinoSlick.propGlobal.setProperty("name.mode.toplevel", list[cursor]);
            }
            if (strCurrentFolder.length() > 0) {
                NullpoMinoSlick.propGlobal.setProperty("name.mode." + strCurrentFolder, list[cursor]);
            }
            NullpoMinoSlick.propGlobal.setProperty("name.mode", list[cursor]);
            NullpoMinoSlick.saveConfig();
            game.enterState(StateSelectRuleFromList.ID);
        }
        return false;
    }

    @Override
    protected boolean onCancel(GameContainer container, StateBasedGame game, int delta) {
        if (isTopLevel) {
            game.enterState(StateTitle.ID);
        } else {
            game.enterState(StateSelectModeFolder.ID);
        }
        return false;
    }
}
