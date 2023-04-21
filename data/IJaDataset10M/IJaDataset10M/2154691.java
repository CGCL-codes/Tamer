package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.graphics.renders.TextBox;
import net.sourceforge.huntforgold.logic.GameState;
import java.awt.Color;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Renders the new game screen
 */
public class NewGameGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(NewGameGUI.class);

    /** The game logo */
    private final String imageName = "gfx/logo.png";

    /**
   * Creates a new NewGameGUI
   * @param gs The gamestate associated with this screen.
   */
    public NewGameGUI(GameState gs) {
        super(gs);
    }

    /**
   * Set options
   * @param options A list of options
   */
    public void setOptions(List options) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        subRenders.add(new ImageRenderer(20, 20, imageName));
        TextBox header = new TextBox(300, 300, "Creating new game...", Color.red, GameScreen.getGameScreen().getWidth() - 200, 22);
        subRenders.add(header);
    }
}
