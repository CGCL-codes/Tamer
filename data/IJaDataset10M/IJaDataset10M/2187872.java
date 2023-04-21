package de.flingelli.scrum.gui.util;

import java.awt.Color;
import junit.framework.Assert;
import org.junit.Test;

public class TestCompletionCellRenderer {

    @Test
    public void colorNoCompletion() {
        CompletionCellRenderer renderer = new CompletionCellRenderer(false);
        Color color = renderer.getGradientColor(0.0);
        Assert.assertTrue(checkRGB(255, 0, 0, color));
    }

    @Test
    public void colorFullCompletion() {
        CompletionCellRenderer renderer = new CompletionCellRenderer(false);
        Color color = renderer.getGradientColor(1.0);
        Assert.assertTrue(checkRGB(0, 255, 0, color));
    }

    private boolean checkRGB(int red, int green, int blue, Color color) {
        return red == color.getRed() && green == color.getGreen() && blue == color.getBlue();
    }
}
