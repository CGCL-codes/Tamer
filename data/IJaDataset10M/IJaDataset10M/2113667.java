package org.wings.template;

import org.wings.SAbstractIconTextCompound;
import org.wings.SComponent;
import org.wings.SURLIcon;

/**
 * 
 * @author armin
 * created at 05.03.2004 10:24:07
 */
public class SAbstractIconTextCompoundPropertyManager extends SComponentPropertyManager {

    static final Class[] classes = { SAbstractIconTextCompound.class };

    public SAbstractIconTextCompoundPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SAbstractIconTextCompound c = (SAbstractIconTextCompound) comp;
        if (name.equals("TEXT")) c.setText(value); else if (name.startsWith("ICON")) {
            if (name.equals("ICON")) c.setIcon(new SURLIcon(value)); else if (name.equals("ICONWIDTH")) {
                try {
                    int width = Integer.parseInt(value);
                    if (c.getIcon() != null) {
                        c.getIcon().setIconWidth(width);
                    }
                    if (c.getDisabledIcon() != null) {
                        c.getDisabledIcon().setIconWidth(width);
                    }
                    if (c.getSelectedIcon() != null) {
                        c.getSelectedIcon().setIconWidth(width);
                    }
                    if (c.getRolloverIcon() != null) {
                        c.getRolloverIcon().setIconWidth(width);
                    }
                    if (c.getRolloverSelectedIcon() != null) {
                        c.getRolloverSelectedIcon().setIconWidth(width);
                    }
                    if (c.getPressedIcon() != null) {
                        c.getPressedIcon().setIconWidth(width);
                    }
                } catch (NumberFormatException ex) {
                }
            } else if (name.equals("ICONHEIGHT")) {
                try {
                    int height = Integer.parseInt(value);
                    if (c.getIcon() != null) {
                        c.getIcon().setIconHeight(height);
                    }
                    if (c.getDisabledIcon() != null) {
                        c.getDisabledIcon().setIconHeight(height);
                    }
                    if (c.getSelectedIcon() != null) {
                        c.getSelectedIcon().setIconHeight(height);
                    }
                    if (c.getRolloverIcon() != null) {
                        c.getRolloverIcon().setIconHeight(height);
                    }
                    if (c.getRolloverSelectedIcon() != null) {
                        c.getRolloverSelectedIcon().setIconHeight(height);
                    }
                    if (c.getPressedIcon() != null) {
                        c.getPressedIcon().setIconHeight(height);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        } else if (name.equals("DISABLEDICON")) c.setDisabledIcon(new SURLIcon(value)); else if (name.equals("SELECTEDICON")) c.setSelectedIcon(new SURLIcon(value)); else if (name.equals("ROLLOVERSELECTEDICON")) c.setRolloverSelectedIcon(new SURLIcon(value)); else if (name.equals("PRESSEDICON")) c.setPressedIcon(new SURLIcon(value)); else super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}
