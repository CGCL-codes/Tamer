package org.rvsnoop.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.rvsnoop.Application;
import org.rvsnoop.NLSUtils;
import rvsnoop.RvConnection;
import rvsnoop.State;

/**
 * Start or resume a connection.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 393 $, $Date: 2008-06-02 10:22:38 -0400 (Mon, 02 Jun 2008) $
 * @since 1.7
 */
public final class StartConnection extends RvSnoopAction implements PropertyChangeListener {

    static {
        NLSUtils.internationalize(StartConnection.class);
    }

    private static final long serialVersionUID = -5725285124054367339L;

    public static final String COMMAND = "startConnection";

    static String MNEMONIC, NAME, TOOLTIP;

    private final RvConnection connection;

    public StartConnection(Application application, RvConnection connection) {
        super(NAME, application);
        this.connection = connection;
        putValue(Action.ACTION_COMMAND_KEY, COMMAND);
        putSmallIconValue(COMMAND);
        putValue(Action.SHORT_DESCRIPTION, TOOLTIP);
        putMnemonicValue(MNEMONIC);
        connection.addPropertyChangeListener(State.PROP_STATE, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        connection.start();
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (!State.PROP_STATE.equals(e.getPropertyName())) {
            return;
        }
        setEnabled(!State.STARTED.equals(e.getNewValue()));
    }
}
