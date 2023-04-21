package brainlink.gui;

import java.awt.event.ActionEvent;
import javax.swing.Action;

/**
 * This action moves the currently selected block to the front of the
 * stack.
 * @author iain
 */
public class BringToFrontAction extends BrainlinkGUIAction {

    private static final String NAME_BTF = "Bring to front...";

    private static final String SMALL_ICON_BTF = "bringtofront.png";

    private static final String SHORT_DESCRIPTION_BTF = "bring selected block to front";

    private static final String LONG_DESCRIPTION_BTF = "brings the currently selected block to the front of the stack";

    private static final int MNEMONIC_KEY_BTF = 'F';

    private static final String ACTION_COMMAND_KEY_BTF = "bringToFront";

    public BringToFrontAction(Brainlink brainlink) {
        super(brainlink);
        putValue(Action.NAME, NAME_BTF);
        putValue(Action.SMALL_ICON, getIcon(SMALL_ICON_BTF));
        putValue(Action.SHORT_DESCRIPTION, SHORT_DESCRIPTION_BTF);
        putValue(Action.LONG_DESCRIPTION, LONG_DESCRIPTION_BTF);
        putValue(Action.MNEMONIC_KEY, new Integer(MNEMONIC_KEY_BTF));
        putValue(Action.ACTION_COMMAND_KEY, ACTION_COMMAND_KEY_BTF);
    }

    public void actionPerformed(ActionEvent e) {
        brainlinkGUI.documentView.moveSelectedBlockToFront();
    }
}
