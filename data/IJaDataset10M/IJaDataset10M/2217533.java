package com.belmont.backup.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.*;

public class BackupHelpPanel extends PopupPanel {

    public BackupHelpPanel(String msg) {
        super(true);
        HTML mh = new HTML(msg);
        mh.setStyleName("backup-popup-help");
        setWidget(mh);
    }
}
