package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.BorderedTextBox;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.graphics.renders.OptionBox;
import net.sourceforge.huntforgold.logic.GameState;
import net.sourceforge.huntforgold.util.ResourceManager;
import java.awt.Color;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Renders the ship yard
 */
public class ShipYardGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(ShipYardGUI.class);

    /** Ships */
    private final String ships = "shipyard.ships";

    /** Ships x offset */
    private final String shipsX = "shipyard.ships.x";

    /** Ships y offset */
    private final String shipsY = "shipyard.ships.y";

    /**
   * Creates a new ShipYardGUI.
   * @param gs The gamestate associated with this screen.
   */
    public ShipYardGUI(GameState gs) {
        super(gs);
    }

    /**
   * Set options
   * @param options A list of options
   */
    public void setOptions(List options) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        subRenders.add(new ImageRenderer(ResourceManager.getInteger(shipsX), ResourceManager.getInteger(shipsY), ships));
        BorderedTextBox header = new BorderedTextBox(100, 40, "The ship yard", GameScreen.getGameScreen().getWidth() - 200);
        subRenders.add(header);
        OptionBox optionBox = new OptionBox(gs, 400, 400, null, Color.black, "Do you want to...", Color.red, options, GameScreen.getGameScreen().getWidth() - 400);
        subRenders.add(optionBox);
    }
}
