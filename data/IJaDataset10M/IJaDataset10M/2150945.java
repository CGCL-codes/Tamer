package net.sf.keytabgui.view.mediator;

import javax.swing.JLabel;
import net.sf.keytabgui.model.KeytabProxy;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

public class StatusbarMediator extends Mediator {

    private JLabel statusBar;

    public StatusbarMediator(JLabel statusBar) {
        super("StatusbarMediator", statusBar);
        this.statusBar = statusBar;
    }

    public String[] listNotificationInterests() {
        return new String[] { KeytabProxy.MSG_FOR_USER };
    }

    public void handleNotification(INotification notification) {
        Object body = notification.getBody();
        if (body != null) {
            statusBar.setText(body.toString());
        }
    }
}
