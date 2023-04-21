package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.BorderedTextBox;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.graphics.renders.OptionBox;
import net.sourceforge.huntforgold.logic.GameState;
import net.sourceforge.huntforgold.model.Ship;
import net.sourceforge.huntforgold.model.Time;
import net.sourceforge.huntforgold.model.player.Player;
import net.sourceforge.huntforgold.util.ResourceManager;
import java.awt.Color;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Renders the ship up close screen
 */
public class InvestigateShipGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(InvestigateShipGUI.class);

    /** Outlook */
    private final String outlook = "investigateship.outlook";

    /** Outlook x offset */
    private final String outlookX = "investigateship.outlook.x";

    /** Outlook y offset */
    private final String outlookY = "investigateship.outlook.y";

    /** The player */
    private Player player;

    /**
   * Creates a ship up close.
   * @param gs The gamestate associated with this screen.
   */
    public InvestigateShipGUI(GameState gs) {
        super(gs);
        this.player = Player.getPlayer();
    }

    /**
   * Set options
   * @param options A list of options
   */
    public void setOptions(List options) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        Ship lastShip = player.getLastShip();
        BorderedTextBox header = new BorderedTextBox(100, 40, "When you come closer to the ship the lookout reports " + "that she looks like a " + lastShip.getName().toLowerCase() + ".", GameScreen.getGameScreen().getWidth() - 200);
        subRenders.add(header);
        subRenders.add(new ImageRenderer(ResourceManager.getInteger(outlookX), ResourceManager.getInteger(outlookY), outlook));
        ImageRenderer horizon = new ImageRenderer(430, 150, "gfx/outlook" + lastShip.getName().toLowerCase() + ".png");
        subRenders.add(horizon);
        Time time = Time.getTime();
        BorderedTextBox year = new BorderedTextBox(100, 450, time.getMonthAsString() + " " + time.getYear(), GameScreen.getGameScreen().getWidth() - 200);
        subRenders.add(year);
        OptionBox optionBox = new OptionBox(gs, 400, 450, null, null, "What will you do?", Color.red, options, GameScreen.getGameScreen().getWidth() - 400);
        subRenders.add(optionBox);
    }
}
