package simple.framework.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import simple.framework.window.WindowContext;

public class CancelAction extends AbstractAction {

    private final CancelActionProvider provider;

    private WindowContext context;

    public CancelAction(CancelActionProvider provider, WindowContext context) {
        this.provider = provider;
        this.context = context;
        putValue(Action.NAME, "cancel");
        putValue(Action.ACTION_COMMAND_KEY, "cancel");
        putValue(Action.MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_CANCEL));
    }

    public void actionPerformed(ActionEvent e) {
        this.provider.windowModel.setVisible(false);
    }
}
