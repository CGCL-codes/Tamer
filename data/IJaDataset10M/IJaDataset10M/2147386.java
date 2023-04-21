package br.ufpb.ngrams.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class ButtonFactory {

    private static final String PATH = "br/ufpb/ngrams/resources/icons/";

    private static final String IMAGE_SUFFIX = ".png";

    static JButton createButton(ButtonConfig config) {
        Icon icon = new ImageIcon(ClassLoader.getSystemResource(PATH + config.name + IMAGE_SUFFIX));
        JButton button = new JButton();
        button.setIcon(icon);
        button.setFocusable(false);
        button.setToolTipText(config.toolTipText);
        return button;
    }
}
