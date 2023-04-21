package edu.bsu.monopoly.game.actions;

/**
 * This class lets you quit the game.
 * @author Team-Tan
 *
 */
public class Quit implements Command {

    public String command() {
        return ".gx";
    }
}
