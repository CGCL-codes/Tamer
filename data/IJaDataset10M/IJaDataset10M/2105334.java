package phex.actions.query;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;
import phex.config.*;

public class ActionQueryEnableMonitor extends ActionBase {

    public ActionQueryEnableMonitor(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        boolean flag = ServiceManager.sCfg.monitorSearchHistory;
        ServiceManager.sCfg.monitorSearchHistory = (!flag);
        ServiceManager.sCfg.save();
        mFrame.refreshAllActions();
    }

    public void refresh() {
        setEnabled(ServiceManager.getNetworkManager().getJoined() && ((MainFrame) mFrame).isMonitorTabSelected());
        boolean flag = !ServiceManager.sCfg.monitorSearchHistory;
        for (int i = 0; i < mRefreshComponents.size(); i++) {
            AbstractButton comp = (AbstractButton) mRefreshComponents.elementAt(i);
            comp.setSelected(flag);
        }
    }
}
