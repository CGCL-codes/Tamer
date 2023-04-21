package com.golden.gamedev.gui.theme.custom;

import java.awt.image.BufferedImage;
import mrusanov.fantasyruler.resources.images.ImageCache;
import mrusanov.fantasyruler.resources.images.InterfaceComponent;
import com.golden.gamedev.gui.theme.basic.BButtonRenderer;
import com.golden.gamedev.gui.toolkit.TComponent;

public class BDownArrowRenderer extends BButtonRenderer {

    @Override
    public BufferedImage[] createUI(TComponent component, int width, int height) {
        BufferedImage[] ui = new BufferedImage[uiDescription().length];
        ui[0] = ImageCache.INST.getInterfaceComponent(InterfaceComponent.DOWNARROW_BUTTON, width, height);
        ui[1] = ImageCache.INST.getInterfaceComponent(InterfaceComponent.DOWNARROW_BUTTON_ACTIVE, width, height);
        ui[2] = ImageCache.INST.getInterfaceComponent(InterfaceComponent.DOWNARROW_BUTTON_PRESSED, width, height);
        ui[3] = ImageCache.INST.getInterfaceComponent(InterfaceComponent.DOWNARROW_BUTTON_DISABLED, width, height);
        return ui;
    }

    @Override
    public String uiName() {
        return "DownArrowButton";
    }
}
