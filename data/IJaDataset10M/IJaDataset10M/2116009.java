package net.sourceforge.huntforgold.gui;

import net.sourceforge.huntforgold.graphics.GameScreen;
import net.sourceforge.huntforgold.graphics.renders.BackgroundRenderer;
import net.sourceforge.huntforgold.graphics.renders.ImageRenderer;
import net.sourceforge.huntforgold.graphics.renders.OptionBox;
import net.sourceforge.huntforgold.logic.GameState;
import java.awt.Color;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Renders the skill menu screen
 */
public class SkillMenuGUI extends AbstractGUI {

    /** The logger */
    private static Logger log = Logger.getLogger(SkillMenuGUI.class);

    /** The game logo */
    private final String imageName = "gfx/logo.png";

    /**
   * Creates a new skill menu gui.
   * @param gs The gamestate associated with this screen.
   */
    public SkillMenuGUI(GameState gs) {
        super(gs);
    }

    /**
   * Set options
   * @param options A list of skill options
   * @param other A list of other options
   */
    public void setOptions(List options, List other) {
        subRenders.clear();
        subRenders.add(new BackgroundRenderer());
        subRenders.add(new ImageRenderer(20, 20, imageName));
        OptionBox skillBox = new OptionBox(gs, 280, 250, null, null, "Select skill", Color.red, options, GameScreen.getGameScreen().getWidth() - 350, GameScreen.getGameScreen().getHeight(), false, 22, true);
        subRenders.add(skillBox);
        OptionBox otherBox = new OptionBox(gs, 50, 540, null, null, null, null, other, GameScreen.getGameScreen().getWidth() - 350, GameScreen.getGameScreen().getHeight(), false, 22, false);
        subRenders.add(otherBox);
        skillBox.setLink(otherBox);
        otherBox.setLink(skillBox);
    }
}
