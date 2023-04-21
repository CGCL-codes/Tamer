package com.golden.gamedev.gui.theme.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.golden.gamedev.gui.TTabPanelButton;
import com.golden.gamedev.gui.toolkit.TComponent;

public class BTabPanelButtonRenderer extends BButtonRenderer {

    public BTabPanelButtonRenderer() {
        put(BACKGROUND_COLOR, new Color(0x151B54));
        put(BACKGROUND_BORDER_COLOR, Color.BLACK);
    }

    @Override
    public void renderUI(Graphics2D g, int x, int y, TComponent component, BufferedImage[] ui) {
        TTabPanelButton button = (TTabPanelButton) component;
        if (button.isActive()) {
            g.drawImage(ui[2], x, y, null);
        } else {
            g.drawImage(ui[0], x, y, null);
        }
    }

    @Override
    public String uiName() {
        return "TabPanelButton";
    }
}
