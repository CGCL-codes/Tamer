package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.BorderedTextBox;
import net.sourceforge.huntforgold.graphics.renders.ContinueBox;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.logic.GameState;
import net.sourceforge.huntforgold.model.player.Missions;
import net.sourceforge.huntforgold.util.ResourceManager;
import org.apache.log4j.Logger;

/**
 * Renders the retire game screen
 */
public class RetireGameGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(RetireGameGUI.class);

    /** Gfx x offset */
    private final String x = "retiregame.gfx.x";

    /** Gfx y offset */
    private final String y = "retiregame.gfx.y";

    /**
   * Creates a new RetireGameGUI
   * @param gs The gamestate associated with this screen.
   */
    public RetireGameGUI(GameState gs) {
        super(gs);
    }

    /**
   * Set options
   * @param gold The personal gold
   * @param acres The personal acres
   * @param cName The career name
   * @param cImage The career image name
   * @param missions The missions
   */
    public void setOptions(int gold, int acres, String cName, String cImage, Missions missions) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        String text = "You retire from the game. You manage ";
        if (gold < 0) {
            text += "to get in dept with " + (-gold);
        } else {
            text += "to collect " + gold;
        }
        text += " pieces of gold and own " + acres + " acres of land.";
        if (missions.isTitle()) {
            text += " During your life you received one or more titles.";
        }
        if (missions.isPirate()) {
            text += " During your life you managed to catch one or more pirates.";
        }
        text += " You therefore spend the rest of your life as " + cName + ".";
        BorderedTextBox header = new BorderedTextBox(100, 40, text, GameScreen.getGameScreen().getWidth() - 200);
        subRenders.add(header);
        subRenders.add(new ImageRenderer(ResourceManager.getInteger(x), ResourceManager.getInteger(y), cImage));
        ContinueBox continueBox = new ContinueBox(gs, 450, 500, "Press to exit game");
        subRenders.add(continueBox);
    }
}
