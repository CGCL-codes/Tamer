package es.eucm.eadventure.editor.gui.elementpanels.book;

import java.awt.Color;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IconTextPanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ImageIcon icon;

    public IconTextPanel(String iconPath, String text, boolean selected) {
        icon = new ImageIcon(iconPath);
        JLabel labelText = new JLabel(text);
        JLabel iconLabel = new JLabel(icon);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(Box.createHorizontalStrut(2));
        this.add(iconLabel);
        this.add(Box.createHorizontalStrut(2));
        this.add(labelText);
        if (selected) this.setBackground(new JEditorPane().getSelectionColor()); else this.setBackground(Color.WHITE);
    }
}
