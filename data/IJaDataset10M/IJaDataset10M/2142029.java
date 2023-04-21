package com.bluebrim.swing.client;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

/**
	Subklass till CoDropShadowBorder som anv�nds av 
	CoDropShadowButton och CoDropShadowToggleButton.
*/
public class CoDropShadowButtonBorder extends CoDropShadowBorder {

    public CoDropShadowButtonBorder() {
        super();
    }

    public CoDropShadowButtonBorder(int dropX, int dropY) {
        super(dropX, dropY);
    }

    protected void doPaintBorder(Component component, Graphics g, int x, int y, int width, int height) {
        AbstractButton tButton = (AbstractButton) component;
        ButtonModel tButtonModel = (ButtonModel) tButton.getModel();
        if ((!tButtonModel.isArmed() || !tButtonModel.isPressed()) && !tButtonModel.isSelected()) super.doPaintBorder(component, g, x, y, width, height);
    }

    protected void prepareBorderPaint(Component component, Graphics g, int x, int y, int width, int height) {
        AbstractButton tButton = (AbstractButton) component;
        ButtonModel tButtonModel = (ButtonModel) tButton.getModel();
        if ((tButtonModel.isArmed() && tButtonModel.isPressed()) || tButtonModel.isSelected()) {
            g.setColor(component.getParent().getBackground());
            g.fillRect(0, 0, m_dropX, height);
            g.fillRect(0, 0, width, m_dropY);
        } else super.prepareBorderPaint(component, g, x, y, width, height);
    }
}
