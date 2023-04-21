package com.incors.plaf.kunststoff;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class KunststoffTextAreaUI extends BasicTextAreaUI {

    protected JComponent myComponent;

    public KunststoffTextAreaUI(JComponent c) {
        super();
        myComponent = c;
    }

    public static ComponentUI createUI(JComponent c) {
        return new KunststoffTextAreaUI(c);
    }

    protected void paintBackground(Graphics g) {
        super.paintBackground(g);
        Rectangle editorRect = getVisibleEditorRect();
        Color colorReflection = KunststoffLookAndFeel.getTextComponentGradientColorReflection();
        if (colorReflection != null) {
            Color colorReflectionFaded = KunststoffUtilities.getTranslucentColor(colorReflection, 0);
            Rectangle rect = new Rectangle(editorRect.x, editorRect.y, editorRect.width, editorRect.height / 2);
            KunststoffUtilities.drawGradient(g, colorReflection, colorReflectionFaded, rect, true);
        }
        Color colorShadow = KunststoffLookAndFeel.getTextComponentGradientColorShadow();
        if (colorShadow != null) {
            Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(colorShadow, 0);
            Rectangle rect = new Rectangle(editorRect.x, editorRect.y + editorRect.height / 2, editorRect.width, editorRect.height / 2);
            KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow, rect, true);
        }
    }
}
