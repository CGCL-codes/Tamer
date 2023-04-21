package de.shandschuh.jaolt.gui.dialogs;

import de.shandschuh.jaolt.core.Member;
import de.shandschuh.jaolt.gui.Lister;
import de.shandschuh.jaolt.gui.StatusFormManager;
import de.shandschuh.jaolt.gui.core.StatusableJDialog;
import de.shandschuh.jaolt.gui.dialogs.initialqueryjdialog.WhatToDoStatusFormManager;

public class InitialQueryJDialog extends StatusableJDialog<Member> {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    public InitialQueryJDialog(String title, StatusFormManager<Member> startStatusFormManager) {
        super(title, true, startStatusFormManager);
        setSize(480, 500);
        setLocationRelativeTo(Lister.getCurrentInstance());
        setResizable(false);
    }

    public InitialQueryJDialog(StatusFormManager<Member> startStatusFormManager) {
        this(startStatusFormManager.getName(), startStatusFormManager);
    }

    public InitialQueryJDialog() {
        this(new WhatToDoStatusFormManager());
    }

    public boolean hasMember() {
        return isFinished() && getObject() != null;
    }
}
