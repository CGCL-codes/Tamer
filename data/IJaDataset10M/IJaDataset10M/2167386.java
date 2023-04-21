package org.ourgrid.broker.ui.async.gui.graphical.actions;

import java.awt.event.ActionEvent;
import org.ourgrid.broker.ui.async.client.BrokerAsyncInitializer;

public class ClearSetGridHistoryAction extends AbstractBrokerAction {

    private static final long serialVersionUID = 1L;

    private static final String ACTION_NAME = "Clear set grid history";

    public ClearSetGridHistoryAction() {
        this(ACTION_NAME);
    }

    public ClearSetGridHistoryAction(String title) {
        super(title);
    }

    public void actionPerformed(ActionEvent e) {
        BrokerAsyncInitializer.getInstance().getComponentClient().cleanSetGridHistory();
    }
}
