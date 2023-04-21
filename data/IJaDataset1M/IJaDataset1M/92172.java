package com.peusoft.peucal.util.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Collection;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Provides utility methods for Swing's widgets.
 * @author Yauheni Prykhodzka
 * @version 1.0
 *
 */
public class GUIUtil {

    /**
     * Default constructor.
     */
    public GUIUtil() {
        super();
    }

    /**
     * Sets the font of a component to bold.
     * @param comp component
     */
    public static void setBoldFont(Component comp) {
        comp.setFont(comp.getFont().deriveFont(Font.BOLD));
    }

    /**
     * Sets mnemonics.
     * All objects different then AbstratButton and JLabel are ignored.
     * Calls {@link GUIUtil#assignMnemonic(AbstratButton)}, {@link GUIUtil#assignMnemonic(JLabel)}.
     * @param buttons buttons.
     */
    public static void assignMnemonics(List<JComponent> buttons) {
        for (JComponent cmp : buttons) {
            if (cmp instanceof AbstractButton) {
                assignMnemonic((AbstractButton) cmp);
            } else if (cmp instanceof JLabel) {
                assignMnemonic((JLabel) cmp);
            }
        }
    }

    /**
     * Sets button's mnemonic.
     * It looks for the charachter '&' and gets the next <br>
     * charachter as mnemonic.
     * @param button a button.
     */
    public static void assignMnemonic(AbstractButton button) {
        String txt = button.getText();
        String[] mnemonic = findMnemonic(txt);
        if (mnemonic[0] != null && mnemonic[1] != null) {
            button.setMnemonic(mnemonic[0].charAt(0));
            button.setText(mnemonic[1]);
        }
    }

    /**
     * Sets label's mnemonic.
     * It looks for the charachter '&' and gets the next <br>
     * charachter as mnemonic.
     * @param button a button.
     */
    public static void assignMnemonic(JLabel label) {
        String txt = label.getText();
        String[] mnemonic = findMnemonic(txt);
        if (mnemonic[0] != null && mnemonic[1] != null) {
            label.setDisplayedMnemonic(mnemonic[0].charAt(0));
            label.setText(mnemonic[1]);
        }
    }

    /**
     * Finds out the biggest preferred size of a lable widget in the collection of JLabel.
     * @param labels collection with JLabel
     * @return the biggest preffered size
     */
    public static Dimension calcMaxLabelPreferredSize(Collection<JLabel> labels) {
        Dimension dim = new Dimension();
        for (JLabel l : labels) {
            Dimension ld = l.getPreferredSize();
            if (ld.height > dim.height) {
                dim.height = ld.height;
            }
            if (ld.width > dim.width) {
                dim.width = ld.width;
            }
        }
        return dim;
    }

    /**
     * Finds the mnemonic in a text.
     * It looks for the charachter '&' and gets the next <br>
     * charachter as mnemonic.
     * @param text text.
     * @return array of strings: mnemonic and the text without the mnemonic
     *         character
     */
    protected static String[] findMnemonic(String text) {
        String[] res = new String[2];
        int idx = text.indexOf('&');
        if ((idx != -1) && (idx != (text.length() - 1))) {
            res[0] = "" + text.charAt(idx + 1);
            res[1] = text.substring(0, idx) + text.substring(idx + 1);
        }
        return res;
    }
}
