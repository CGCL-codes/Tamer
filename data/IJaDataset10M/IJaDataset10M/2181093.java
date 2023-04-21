package tochterUhr.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import base.gui.MenuHelper;
import base.gui.action.UpdateAction;
import tochterUhr.ClockSettings;
import base.resources.Resources;

public class SwitchClocktypeAction extends AbstractAction implements UpdateAction {

    private static final String ACTION_NAME = "action.switch_clocktype";

    public SwitchClocktypeAction(final Tochteruhr frame) {
        super();
        updateLocale();
        oFrame = frame;
    }

    public void actionPerformed(final ActionEvent e) {
        oFrame.setAnalog(!oFrame.isAnalog());
        ClockSettings.getInstance().setAnalogDefaultClock(oFrame.isAnalog());
        ClockSettings.getInstance().save();
    }

    public void updateLocale() {
        putValue(Action.NAME, Resources.getText(ACTION_NAME));
        putValue(Action.MNEMONIC_KEY, MenuHelper.getMnemonicFromString(Resources.getText(ACTION_NAME + ".mnemonic")));
    }

    private Tochteruhr oFrame;
}
