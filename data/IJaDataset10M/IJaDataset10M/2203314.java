package es.eucm.eadventure.editor.control.tools.animation;

import es.eucm.eadventure.common.data.animation.Animation;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

public class ChangeSlidesTool extends Tool {

    private Animation animation;

    private boolean newSlides;

    private boolean oldSlides;

    public ChangeSlidesTool(Animation animation, boolean slides) {
        this.animation = animation;
        this.newSlides = slides;
        this.oldSlides = animation.isSlides();
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }

    @Override
    public boolean doTool() {
        if (newSlides == oldSlides) return false;
        animation.setSlides(newSlides);
        return true;
    }

    @Override
    public boolean redoTool() {
        animation.setSlides(newSlides);
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean undoTool() {
        animation.setSlides(oldSlides);
        Controller.getInstance().updatePanel();
        return true;
    }
}
