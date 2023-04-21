package gnagck.util;

import java.awt.Color;
import gnagck.actor.Actor;
import gnagck.block.Block;
import gnagck.level.Level;

/**
 * @author royer
 *
 */
public class SelectionManager {

    private static SelectionManager instance;

    private Actor selectedActor;

    private Block selectedBlock;

    private Color selectedColor;

    private Level selectedLevel;

    private SelectionManager() {
    }

    public static SelectionManager getInstance() {
        if (instance == null) {
            instance = new SelectionManager();
        }
        return instance;
    }

    public Block getSelectedBlock() {
        return selectedBlock;
    }

    public void setSelectedBlock(Block selectedBlock) {
        this.selectedBlock = selectedBlock;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Level getSelectedLevel() {
        return selectedLevel;
    }

    public void setSelectedLevel(Level selectedLevel) {
        this.selectedLevel = selectedLevel;
    }

    public Actor getSelectedActor() {
        return selectedActor;
    }

    public void setSelectedActor(Actor selectedActor) {
        this.selectedActor = selectedActor;
    }
}
