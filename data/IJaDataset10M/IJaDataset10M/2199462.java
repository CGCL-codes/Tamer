package com.elibera.ccs.buttons;

import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import com.elibera.ccs.parser.InterfaceDocContainer;

/**
 * @author meisi
 *
 * wird für Style-Buttons benötigt, wie Zb Fett, Kursiv, die aktiv werden sollen
 * wenn die Styles am Cursor im Editor für die Styles dieses Buttons passen
 * implementiert InterfaceActionElement, diese Buttons sind im ActionButtonPanel zu finden
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class ButtonRoot extends ButtonAllRoot implements InterfaceActionElement {

    protected java.awt.Color c;

    protected java.awt.Color co;

    public ButtonRoot(InterfaceDocContainer conf, JPanel panel) {
        super(conf, panel);
        c = this.getBackground();
        co = new java.awt.Color(255, 255, 255);
    }

    public void checkActiveAndSetMode(AttributeSet s) {
        if (s == null) return;
        checkForYourSelfForActiveState(s);
        String[][] a = this.getStylesKeysAndValues();
        if (a == null) return;
        boolean setActiveFlag = true;
        for (int i = 0; i < a.length; i++) {
            String value = (String) s.getAttribute(a[i][0]);
            if (value == null) {
                setActiveFlag = false;
                break;
            } else if (a[i][1] == null) {
            } else if (value.compareTo(a[i][1]) != 0) {
                setActiveFlag = false;
                break;
            }
        }
        setActiveColor(setActiveFlag, s);
        this.setSelected(setActiveFlag);
    }

    protected void checkForYourSelfForActiveState(AttributeSet s) {
    }

    protected void setActiveColor(boolean isActive, AttributeSet s) {
        if (isActive) this.setBackground(co); else this.setBackground(c);
    }

    protected abstract String[][] getStylesKeysAndValues();
}
