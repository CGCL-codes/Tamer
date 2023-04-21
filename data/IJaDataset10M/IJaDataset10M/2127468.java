package org.hyperimage.client.gui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.hyperimage.client.HIRuntime;

/**
 * @author Jens-Martin Loebel
 */
public class EditButton extends JButton {

    private static final long serialVersionUID = -4208046959379193627L;

    public void setEnabled(boolean enabled) {
        if (HIRuntime.getGui().checkEditAbility(true)) super.setEnabled(enabled); else super.setEnabled(false);
    }

    public EditButton() {
        super();
        setActionCommand("edit");
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setIcon(new ImageIcon(getClass().getResource("/resources/icons/edit.png")));
        setPressedIcon(new ImageIcon(getClass().getResource("/resources/icons/edit-active.png")));
        setDisabledIcon(new ImageIcon(getClass().getResource("/resources/icons/edit-disabled.png")));
        setPreferredSize(new Dimension(24, 24));
        setEnabled(this.isEnabled());
    }
}
