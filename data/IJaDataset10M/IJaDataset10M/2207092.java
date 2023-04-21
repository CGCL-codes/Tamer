package bfpl.gui.config;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import base.gui.MenuHelper;
import base.resources.Resources;
import bfpl.gui.DefaultBfplGuiModel;

public class TimeScrollAction extends GuiAction {

    private static final String ACTION_NAME = "action.bfpl_autoScroll";

    public TimeScrollAction(final DefaultBfplGuiModel m) {
        super(Resources.getText(ACTION_NAME));
        updateLocale();
        setBfplGuiModel(m);
    }

    public void actionPerformed(final ActionEvent e) {
        getBfplGuiModel().setAutoscrolling(!getBfplGuiModel().isAutoscrolling());
    }

    public void updateLocale() {
        putValue(Action.NAME, Resources.getText(ACTION_NAME));
        putValue(Action.MNEMONIC_KEY, MenuHelper.getMnemonicFromString(Resources.getText(ACTION_NAME + ".mnemonic")));
    }
}
