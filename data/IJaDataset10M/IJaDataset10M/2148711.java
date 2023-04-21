package net.sf.keytabgui.view.mediator.menu.file;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import net.sf.keytabgui.controller.CloseFileCommand;
import net.sf.keytabgui.model.KeytabProxy;
import net.sf.keytabgui.view.mediator.AbstractMenuMediator;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.facade.Facade;

public class CloseFileMediator extends AbstractMenuMediator {

    public CloseFileMediator(JComponent viewComponent) {
        super("CloseFileMediator " + viewComponent.getName(), viewComponent);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sendNotification(CloseFileCommand.NAME);
    }

    public String[] listNotificationInterests() {
        return new String[] { KeytabProxy.NAME };
    }

    public void handleNotification(INotification notification) {
        JComponent item = (JComponent) getViewComponent();
        KeytabProxy proxy = (KeytabProxy) Facade.getInstance().retrieveProxy(KeytabProxy.NAME);
        item.setEnabled(!proxy.isNull());
    }
}
