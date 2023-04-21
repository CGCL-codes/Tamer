package net.cattaka.rdbassistant.test;

import javax.swing.JButton;
import net.cattaka.swing.util.ButtonsBundle;

public class ButtonBundleTest {

    public static void main(String[] args) {
        JButton button = new JButton();
        ButtonsBundle.applyButtonDifinition(button, "save");
        System.out.println(button.getText());
        System.out.println((char) button.getMnemonic());
        System.out.println(button.getToolTipText());
    }
}
