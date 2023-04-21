package phex.actions.download;

import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;

public class ActionDownloadStop extends ActionBase {

    public ActionDownloadStop(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        ((MainFrame) mFrame).getDownloadTab().stopDownload();
    }

    public void refresh() {
        setEnabled(((MainFrame) mFrame).isDownloadTabSelected() && ((MainFrame) mFrame).getDownloadTab().isDownloadSelected());
    }
}
