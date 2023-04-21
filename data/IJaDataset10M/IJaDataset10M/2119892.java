package no.hal.bubble;

import no.hal.gridgame.ImageSelector;

public class BubbleImageSelector extends ImageSelector<Bubble> {

    public BubbleImageSelector() {
        super("/toolbarButtonGraphics/general/");
        addNameMapping("1", "Save24.gif");
        addNameMapping("2", "Open24.gif");
        addNameMapping("3", "Print24.gif");
        addNameMapping("4", "Cut24.gif");
        addNameMapping("5", "Copy24.gif");
        addNameMapping("6", "Paste24.gif");
        addNameMapping("7", "Delete24.gif");
        addNameMapping("8", "Find24.gif");
        addNameMapping("9", "Undo24.gif");
        addNameMapping("10", "Help24.gif");
    }

    public String getImageName(Bubble bubble) {
        return (bubble != null ? bubble.getName() : null);
    }
}
