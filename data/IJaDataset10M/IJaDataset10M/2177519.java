package org.xmatthew.mypractise;

import org.xmatthew.spy2servers.annotation.AlertComponent;
import org.xmatthew.spy2servers.core.AbstractAlertComponent;
import org.xmatthew.spy2servers.core.Message;

/**
 * @author Matthew Xie
 *
 */
@AlertComponent(name = "myAlertComponent")
public class SimpleAlertComponet extends AbstractAlertComponent {

    private boolean started;

    @Override
    protected void onAlert(Message message) {
        if (started) {
            System.out.println(message);
        }
    }

    public void startup() {
        started = true;
        setStatusRun();
    }

    public void stop() {
        started = false;
        setStatusStop();
    }
}
