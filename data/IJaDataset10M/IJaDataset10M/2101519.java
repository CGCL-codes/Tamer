package com.transfer_solutions.transversion.ui.screens;

import javax.swing.JPanel;
import com.transfer_solutions.transversion.ui.ITransVersionConstants;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class ApplicationScreen extends BaseScreen {

    int userAction = NO_ACTION;

    BorderLayout borderLayout2 = new BorderLayout();

    IScreen content = null;

    String actionString = null;

    Random someInt = new Random();

    public ApplicationScreen() {
        this(SELECT);
    }

    public ApplicationScreen(int action) {
        this.userAction = action;
        switch(this.userAction) {
            case NEW:
                this.actionString = "New Application";
                this.content = new NewApplicationScreen();
                break;
            case MODIFY:
                this.actionString = "Modify Application";
                this.content = new ModifyApplicationScreen();
                break;
            case DELETE:
                this.actionString = "delete Application";
                this.content = new DeleteApplicationScreen();
                break;
            case SELECT:
                this.actionString = "Display Application";
                this.content = new SelectApplicationScreen();
                break;
            default:
                this.actionString = "No action";
                this.content = new NewApplicationScreen();
                break;
        }
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        addToButtonBar(new String[] { OK, CANCEL });
        enableButtons(new String[] { OK, CANCEL }, false);
        enableButton(OK, true);
        setContentPanel((JPanel) this.content);
        setActionString(this.actionString);
        setProgress(-1);
    }

    /**
  * The buttonbar buttons deliver their events here.
  */
    protected void buttonBar_actionPerformed(ActionEvent e) {
        print("ButtonBar: " + e.getActionCommand());
        setMessageString("Button pressed: " + e.getActionCommand());
        setProgress(someInt.nextInt(100));
        if (e.getActionCommand().equals(OK)) {
            String result = this.content.commit();
            if (result == null) {
                setMessageString("Application Created");
                this.remove(layoutPanel);
                updateUI();
            } else {
                setMessageString(result);
            }
        } else if (e.getActionCommand().equals(CANCEL)) {
            setMessageString("Application Creation Canceled");
            this.content.rollBack();
            this.remove(layoutPanel);
            updateUI();
        }
    }
}
