package jscorch;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A utility class providing convenience methods for
 * creating dialogs. Dialog methods should be self-contained.
 */
public class OptionDialogs {

    /**
	 * Creates a dialog allowing the setting of preferences for a Player
	 */
    public static void showPlayerPrefsDialog(Frame parent, Player player) {
        new PlayerPrefsDialog(parent, player);
    }

    /**
	 * Creates a dialog allowing the setting of preferences for the game
	 */
    public static void showGamePrefsDialog(Frame parent) {
        new GamePrefsDialog(parent);
    }

    /**
	 * Creates a dialog for use at the end of a game. It provides
	 * options to start a new game or quit JScorch.
	 * @param parent the container that the dialog will belong to
	 */
    public static void showGameOverDialog(Frame parent) {
        String message = "This game is over.\n\nWould you like to play a new game?";
        String title = "Game Over";
        String[] options = { "New Game", "Quit" };
        int n = JOptionPane.showOptionDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (n == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else {
            JScorch.newGame();
            parent.dispose();
        }
    }

    /**
	 * Creates a dialog to show a list of weapons for the user to pick from.
	 * After a weapon is chosen, the proper Player instance is informed.
	 * @param parent the container that the dialog will belong to
	 * @param p the Player instance to be informed
	 */
    public static void showWeaponChooser(Frame parent, Player p) {
        new WeaponDialog(parent, p);
    }
}
