package com.mtp.pounder.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import com.mtp.pounder.*;
import java.awt.Frame;
import java.awt.Component;
import com.mtp.pounder.assrt.AssertDialog;
import com.mtp.pounder.assrt.WindowShowingPresenter;
import com.mtp.pounder.assrt.WindowShowingPanel;
import i18n.Strings;

/**

Action for closing the main frame.

@author Matthew Pekar

**/
public class AssertWindowShowingAction extends AbstractAction {

    protected PounderModel model;

    protected WindowShowingPresenter presenter;

    protected WindowShowingPanel panel;

    protected Frame frame;

    public AssertWindowShowingAction(PounderModel pm, Frame pounderFrame) {
        super(Strings.getString("WindowShowing"));
        this.frame = pounderFrame;
        this.presenter = new WindowShowingPresenter(pm.getRecord());
        this.panel = new WindowShowingPanel(presenter);
    }

    public void actionPerformed(ActionEvent e) {
        AssertDialog dialog = new AssertDialog(panel, presenter, frame, true);
        dialog.setVisible(true);
    }
}
