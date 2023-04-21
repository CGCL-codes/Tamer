package com.googlecode.pondskum.gui.swing.notifyer;

import java.awt.Color;

public final class RemainderRenderer extends AbstractRenderer {

    RemainderRenderer(final double displayRatio) {
        super(displayRatio);
    }

    @Override
    protected Color getProgressColor() {
        return Color.RED;
    }

    @Override
    protected Color getTextColor() {
        return Color.WHITE;
    }

    @Override
    protected boolean isRaised() {
        return true;
    }

    @Override
    protected String getText() {
        return "Remainder";
    }
}
