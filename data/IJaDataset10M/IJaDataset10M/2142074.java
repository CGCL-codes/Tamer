package phex.actions.host;

import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;

public class ActionHostFilter extends ActionBase {

    public ActionHostFilter(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        MainFrame frame = (MainFrame) mFrame;
        if (frame.isSearchTabSelected()) {
            frame.getSearchTab().filterDownloadHost();
        }
        if (frame.isMonitorTabSelected()) {
            frame.getMonitorTab().filterPassiveDownloadHost();
        }
    }

    public void refresh() {
        MainFrame frame = (MainFrame) mFrame;
        if (frame.isSearchTabSelected()) {
            setEnabled(frame.getResultSelectedCount() > 0);
            return;
        }
        if (frame.isMonitorTabSelected()) {
            setEnabled(frame.getMonitorTab().isPassiveResultSelected());
            return;
        }
        setEnabled(false);
    }
}
