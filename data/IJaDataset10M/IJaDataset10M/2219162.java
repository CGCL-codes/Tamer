package mu.nu.nullpo.gui.slick;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Options screen
 */
public class StateConfigMainMenu extends DummyMenuChooseState {

    /** This state's ID */
    public static final int ID = 5;

    /** UI Text identifier Strings */
    private static final String[] UI_TEXT = { "ConfigMainMenu_General", "ConfigMainMenu_Rule", "ConfigMainMenu_GameTuning", "ConfigMainMenu_AI", "ConfigMainMenu_Keyboard", "ConfigMainMenu_KeyboardNavi", "ConfigMainMenu_KeyboardReset", "ConfigMainMenu_Joystick" };

    /** Player number */
    protected int player = 0;

    public StateConfigMainMenu() {
        maxCursor = 7;
        minChoiceY = 3;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    protected void renderImpl(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(ResourceHolderSlick.imgMenu, 0, 0);
        NormalFontSlick.printFontGrid(1, 1, "OPTIONS", NormalFontSlick.COLOR_ORANGE);
        NormalFontSlick.printFontGrid(1, 3 + cursor, "b", NormalFontSlick.COLOR_RED);
        NormalFontSlick.printFontGrid(2, 3, "[GENERAL OPTIONS]", (cursor == 0));
        NormalFontSlick.printFontGrid(2, 4, "[RULE SELECT]:" + (player + 1) + "P", (cursor == 1));
        NormalFontSlick.printFontGrid(2, 5, "[GAME TUNING]:" + (player + 1) + "P", (cursor == 2));
        NormalFontSlick.printFontGrid(2, 6, "[AI SETTING]:" + (player + 1) + "P", (cursor == 3));
        NormalFontSlick.printFontGrid(2, 7, "[KEYBOARD SETTING]:" + (player + 1) + "P", (cursor == 4));
        NormalFontSlick.printFontGrid(2, 8, "[KEYBOARD NAVIGATION SETTING]:" + (player + 1) + "P", (cursor == 5));
        NormalFontSlick.printFontGrid(2, 9, "[KEYBOARD RESET]:" + (player + 1) + "P", (cursor == 6));
        NormalFontSlick.printFontGrid(2, 10, "[JOYSTICK SETTING]:" + (player + 1) + "P", (cursor == 7));
        NormalFontSlick.printTTFFont(16, 432, NullpoMinoSlick.getUIText(UI_TEXT[cursor]));
    }

    @Override
    protected void onChange(GameContainer container, StateBasedGame game, int delta, int change) {
        player += change;
        if (player < 0) player = 1;
        if (player > 1) player = 0;
        ResourceHolderSlick.soundManager.play("change");
    }

    @Override
    protected boolean onDecide(GameContainer container, StateBasedGame game, int delta) {
        ResourceHolderSlick.soundManager.play("decide");
        switch(cursor) {
            case 0:
                game.enterState(StateConfigGeneral.ID);
                break;
            case 1:
                NullpoMinoSlick.stateConfigRuleStyleSelect.player = player;
                game.enterState(StateConfigRuleStyleSelect.ID);
                break;
            case 2:
                NullpoMinoSlick.stateConfigGameTuning.player = player;
                game.enterState(StateConfigGameTuning.ID);
                break;
            case 3:
                NullpoMinoSlick.stateConfigAISelect.player = player;
                game.enterState(StateConfigAISelect.ID);
                break;
            case 4:
                NullpoMinoSlick.stateConfigKeyboard.player = player;
                NullpoMinoSlick.stateConfigKeyboard.isNavSetting = false;
                game.enterState(StateConfigKeyboard.ID);
                break;
            case 5:
                NullpoMinoSlick.stateConfigKeyboardNavi.player = player;
                game.enterState(StateConfigKeyboardNavi.ID);
                break;
            case 6:
                NullpoMinoSlick.stateConfigKeyboardReset.player = player;
                game.enterState(StateConfigKeyboardReset.ID);
                break;
            case 7:
                NullpoMinoSlick.stateConfigJoystickMain.player = player;
                game.enterState(StateConfigJoystickMain.ID);
                break;
        }
        return false;
    }

    @Override
    protected boolean onCancel(GameContainer container, StateBasedGame game, int delta) {
        game.enterState(StateTitle.ID);
        return false;
    }
}
