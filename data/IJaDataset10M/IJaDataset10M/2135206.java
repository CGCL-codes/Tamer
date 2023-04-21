package plugins;

import core.ImageFunctions;
import core.Plugin;
import core.Stack;
import core.gui.Theba;

public class ErodePlugin implements Plugin {

    Theba control;

    public void setup(Theba f) {
        this.control = f;
    }

    public String getCategory() {
        return "Morphology";
    }

    public void process(Stack stack) {
        for (int z = 0; z < stack.getDepth(); z++) {
            control.setProgress(z);
            stack.putSlice(ImageFunctions.erode(stack.getSlice(z), stack.getWidth(), stack.getHeight()), z);
        }
    }

    public String getName() {
        return "2D Erode";
    }

    public String getAbout() {
        return "Returns a 2D erosion of each slice on the entire stack";
    }
}
