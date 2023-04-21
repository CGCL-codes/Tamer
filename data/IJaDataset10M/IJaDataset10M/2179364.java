package gumbo.app.awt.widget.impl;

import gumbo.app.awt.widget.AwtWidgetManager;
import gumbo.app.awt.widget.AwtWindow;
import gumbo.core.util.AssertUtils;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;

/**
 * A WindowWidget with a JDialog peer.
 * @author jonb
 */
public class AwtDialog extends AwtContainerWidgetImpl<JDialog> implements AwtWindow<JDialog> {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance, with a default peer.
	 * @param title Title for this dialog. Never null. Used in title bar and
	 * action name.
	 * @param host Shared exposed window host. None if null.
	 * @param content Shared exposed contents. Never null.
	 */
    public AwtDialog(String title, Frame host, Container content) {
        this(new JDialog(host, title));
        getPeer().setContentPane(content);
    }

    /**
	 * Creates an instance, with the specified peer.
	 * @param peer Peer widget. Never null.
	 */
    public AwtDialog(JDialog peer) {
        AssertUtils.assertNonNullArg(peer);
        setPeer(peer);
        _showAction = new AbstractAction(peer.getTitle()) {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
            }
        };
    }

    @Override
    public Action getShowAction() {
        return _showAction;
    }

    @Override
    public void setResizable(boolean isResizable) {
        getPeer().setResizable(isResizable);
    }

    @Override
    public AwtWindow<?> getOwner() {
        Window ownerP = getPeer().getOwner();
        AwtWindow<?> owner = (AwtWindow<?>) AwtWidgetManager.getInstance().getPeerMap().get(ownerP);
        return owner;
    }

    @Override
    public void setVisible(boolean isVisible) {
        if (!getPeer().isValid()) {
            getPeer().pack();
        }
        getPeer().setVisible(true);
    }

    @Override
    public final void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        getPeer().setEnabled(isEnabled);
    }

    private Action _showAction;
}
