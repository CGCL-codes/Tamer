package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.graphics.renders.OptionBox;
import net.sourceforge.huntforgold.logic.GameState;
import net.sourceforge.huntforgold.util.ResourceManager;
import java.awt.Color;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Renders the captains cabin screen
 */
public class CaptainsCabinGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(CaptainsCabinGUI.class);

    /** Cabin */
    private final String cabin = "captainscabin.cabin";

    /** Cabin x offset */
    private final String cabinX = "captainscabin.cabin.x";

    /** Cabin y offset */
    private final String cabinY = "captainscabin.cabin.y";

    /**
   * Creates a captains cabin GUI.
   * @param gs The gamestate associated with this screen.
   */
    public CaptainsCabinGUI(GameState gs) {
        super(gs);
    }

    /**
   * Set the options
   * @param options The options
   */
    public void setOptions(List options) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        subRenders.add(new ImageRenderer(ResourceManager.getInteger(cabinX), ResourceManager.getInteger(cabinY), cabin));
        OptionBox optionBox = new OptionBox(gs, 100, 40, "What will you do ?", Color.black, "Do you...", Color.red, options, GameScreen.getGameScreen().getWidth() - 200);
        subRenders.add(optionBox);
    }
}
