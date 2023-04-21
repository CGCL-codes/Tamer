package org.magnesia.client.gui.actions;

import static org.magnesia.client.misc.Utils.i18n;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import org.magnesia.client.gui.ImageArea;

public class Select extends AbstractAction {

    private static final long serialVersionUID = -6737415094101619501L;

    private ImageArea[] areas;

    private final boolean invert;

    public Select(boolean invert, ImageArea... areas) {
        super(i18n(invert ? "action_select_invert" : "action_select_all"));
        this.invert = invert;
        this.areas = areas;
        if (invert) {
            putValue(Action.SHORT_DESCRIPTION, i18n("action_select_invert_tooltip"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/magnesia/client/icons/select-invert.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        } else {
            putValue(Action.SHORT_DESCRIPTION, i18n("action_select_all_tooltip"));
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/org/magnesia/client/icons/select-all.png")));
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        }
    }

    public void actionPerformed(ActionEvent arg0) {
        for (ImageArea ia : areas) {
            if (ia.isVisible()) {
                if (invert) {
                    ia.toggleSelection();
                } else {
                    ia.selectAll();
                }
            }
        }
    }
}
