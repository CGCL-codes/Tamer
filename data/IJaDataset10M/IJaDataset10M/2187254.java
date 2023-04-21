package phex.actions.view;

import java.awt.event.*;
import java.util.*;
import java.net.*;
import javax.swing.*;
import phex.*;
import phex.actions.base.*;

public class ActionViewIrc extends ActionBase {

    public ActionViewIrc(BaseFrame frame, String label, Icon icon) {
        super(frame, label, icon);
    }

    public void actionPerformed(ActionEvent event) {
        ((MainFrame) mFrame).selectTab(6);
    }

    public void refresh() {
        setEnabled(true);
    }
}
