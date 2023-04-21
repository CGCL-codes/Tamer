package checkers3d.logic;

import checkers3d.presentation.GUIButton;
import checkers3d.presentation.GUIContainer;
import checkers3d.presentation.GUILabel;
import checkers3d.presentation.GUIPictureBox;
import checkers3d.presentation.GUITextbox;
import checkers3d.presentation.GameEngine;
import checkers3d.presentation.IDrawable;
import checkers3d.presentation.IInputObserver;
import checkers3d.presentation.UtilGUI;
import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author rherbzi
 * @author Ruben Acuna
 */
public class MenuAdminLogin extends GUIContainer {

    private final int CONTROL_BUTTON_AXIS = GameEngine.WINDOW_SIZE.y - 100;

    GUITextbox loginName;

    GUITextbox loginPassword;

    GUILabel loginValidation;

    GUIButton buttonNext;

    GUIButton buttonReturn;

    public MenuAdminLogin(Point size) {
        super(size);
        LinkedList<IDrawable> buttons = new LinkedList<IDrawable>();
        add(new GUIPictureBox("Background.png", new Point()));
        buttonNext = new GUIButton(".\\images\\button-next.png", null, new ObserverNull());
        buttons.add(buttonNext);
        buttonReturn = new GUIButton("button-return.png", null, new ObserverReturn());
        buttons.add(buttonReturn);
        add(new GUILabel("Administrator Login:", Color.WHITE, new Point(124 + 260, 230)));
        add(new GUILabel("Name:", Color.WHITE, new Point(124 + 260, 250)));
        add(new GUILabel("Password:", Color.WHITE, new Point(102 + 260, 270)));
        loginValidation = new GUILabel("Ready", Color.RED, new Point(235 + 260, 230));
        add(loginValidation);
        loginName = new GUITextbox(new Point(166 + 260, 250), 25, this);
        loginPassword = new GUITextbox(new Point(166 + 260, 270), 25, this);
        loginPassword.setSecure(true);
        for (IDrawable component : buttons) add(component);
        UtilGUI.computeLayoutSpanning(getSize(), CONTROL_BUTTON_AXIS, buttons);
    }

    private void validatePlayers() {
        String username = loginName.getText();
        String password = loginPassword.getText();
        if (username.equals("admin") && password.equals("admin")) loginValidation.setColor(Color.GREEN); else loginValidation.setColor(Color.RED);
    }

    @Override
    public void onKeyPress(char key) {
        super.onKeyPress(key);
        validatePlayers();
        if (loginValidation.getColor() == Color.GREEN) {
            buttonNext.setObserver(new ObserverChangeStats());
        } else {
            buttonNext.setObserver(new ObserverNull());
        }
    }

    private class ObserverReturn implements IInputObserver {

        /**
         * Returns the loginName to the scenario select screen.
         *
         * @param position Location of click within button.
         */
        public void onClick(Point position) {
            GameMaster.getInstance().removeTopGUIContainer();
        }

        /**
         * Key press observer that does nothing.
         *
         * @param key Key that was pressed.
         */
        public void onKeyPress(char key) {
        }
    }

    private class ObserverNull implements IInputObserver {

        /**
         * Returns the loginName to the scenario select screen.
         *
         * @param position Location of click within button.
         */
        public void onClick(Point position) {
        }

        /**
         * Key press observer that does nothing.
         *
         * @param key Key that was pressed.
         */
        public void onKeyPress(char key) {
        }
    }

    private class ObserverChangeStats implements IInputObserver {

        public void onClick(Point position) {
            GameMaster.getInstance().addGUIContainer(new MenuAdminChangeStats(getSize()));
        }

        /**
         * Key press observer that does nothing.
         *
         * @param key Key that was pressed.
         */
        public void onKeyPress(char key) {
        }
    }
}
