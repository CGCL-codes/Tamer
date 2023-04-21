package phex.actions.config;

import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;
import phex.dialogues.*;
import phex.common.*;

public class ActionConfigIgnore extends ActionBase {

    public ActionConfigIgnore(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        DlgConfigIgnore dlg = new DlgConfigIgnore(mFrame, "", ServiceManager.sCfg.mNetIgnoredHosts);
        dlg.setVisible(true);
        dlg.dispose();
        if (!dlg.getCancel()) {
            ServiceManager.sCfg.mNetIgnoredHosts = dlg.getIgnoredHosts();
            ServiceManager.sCfg.save();
        }
    }

    public void refresh() {
        setEnabled(true);
    }
}
