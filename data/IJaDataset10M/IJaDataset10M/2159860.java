package phex.actions.host;

import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;

public class ActionHostResetCatcher extends ActionBase {

    public ActionHostResetCatcher(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        ServiceManager.getHostManager().resetCaughtHosts();
    }

    public void refresh() {
        setEnabled(ServiceManager.getNetworkManager().getJoined() && ((MainFrame) mFrame).isNetTabSelected());
    }
}
