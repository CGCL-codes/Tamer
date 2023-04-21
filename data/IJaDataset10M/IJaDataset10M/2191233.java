package gear.widgets.transition.implementations;

import gear.widgets.transition.TransitionCanvas;
import javax.microedition.lcdui.Image;

/**
 * Left sliding animation between two widget (the previous exits on the left)
 * @author Paolo Burelli
 * @see TransitionCanvas
 * 
 */
public class LeftSlideOut extends SlideAnimation {

    protected int getHorizontalStep() {
        return -width / framesToRender;
    }

    protected int getInitialHorizontalValue() {
        return getMaxHorizontalValue();
    }

    protected int getMinHorizontalValue() {
        return -width;
    }

    protected int getMaxHorizontalValue() {
        return 0;
    }

    public void setNewScreen(Image newScreen) {
        super.setNewScreen(newScreen);
    }
}
