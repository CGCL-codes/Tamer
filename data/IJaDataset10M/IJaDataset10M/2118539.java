package fr.esrf.tangoatk.widget.command;

import fr.esrf.tangoatk.core.DeviceFactory;
import fr.esrf.tangoatk.core.EndGroupExecutionEvent;
import fr.esrf.tangoatk.core.ErrorEvent;
import fr.esrf.tangoatk.core.ICommandGroup;
import fr.esrf.tangoatk.core.IEndGroupExecutionListener;
import fr.esrf.tangoatk.core.command.VoidVoidCommandGroup;
import java.awt.event.*;
import javax.swing.*;

/**
 * Simple command button.
 */
public class GroupCommandViewer extends JButton implements IEndGroupExecutionListener {

    protected ICommandGroup cmdgModel;

    private String buttonLabel = "Not Specified";

    public GroupCommandViewer() {
        setText("command-group");
        addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeButtonActionPerformed(evt);
            }
        });
    }

    public void setButtonLabel(String lab) {
        if (lab == null) {
            buttonLabel = "Not Specified";
            return;
        }
        if (lab.length() <= 0) {
            buttonLabel = "Not Specified";
            return;
        }
        buttonLabel = lab;
        setText(lab);
    }

    public String getButtonLabel() {
        return (buttonLabel);
    }

    protected void executeButtonActionPerformed(ActionEvent actionevent) {
        if (cmdgModel != null) {
            setEnabled(false);
            cmdgModel.execute();
        }
    }

    public void endGroupExecution(EndGroupExecutionEvent evt) {
        setEnabled(true);
    }

    public void errorChange(ErrorEvent errorevent) {
    }

    public void setModel(ICommandGroup cmdg) {
        clearModel();
        if (cmdg == null) return;
        if (cmdg.size() <= 0) return;
        if (!(cmdg instanceof VoidVoidCommandGroup)) return;
        cmdgModel = cmdg;
        cmdgModel.addEndGroupExecutionListener(this);
        if (!buttonLabel.equalsIgnoreCase("Not Specified")) {
            setText(buttonLabel);
            return;
        }
        String cmdname = cmdg.getCmdName();
        if (cmdname != null) setText(cmdname);
    }

    public void clearModel() {
        if (cmdgModel != null) {
            cmdgModel.removeEndGroupExecutionListener(this);
            if (buttonLabel.equalsIgnoreCase("Not Specified")) setText("command-group");
            cmdgModel = null;
        }
    }

    public static void main(String[] args) {
        ICommandGroup cmdg = new VoidVoidCommandGroup();
        GroupCommandViewer gcv = new GroupCommandViewer();
        try {
            cmdg.add("tl2/ps-c1/cv0/Reset");
            cmdg.add("tl2/ps-c1/cv1/Reset");
            cmdg.add("tl2/ps-c1/cv2/Reset");
            cmdg.add("tl2/ps-c1/cv3/Reset");
            cmdg.add("tl2/ps-c1/cv4/Reset");
            cmdg.add("tl2/ps-c1/cv5/Reset");
            cmdg.add("tl2/ps-c1/cv6/Reset");
            cmdg.add("tl2/ps-c1/cv7/Reset");
            cmdg.add("tl2/ps-c1/cv8/Reset");
            cmdg.add("tl2/ps-c1/cv9/Reset");
            cmdg.add("tl2/ps-c1/ch1/Reset");
            cmdg.add("tl2/ps-c1/ch2/Reset");
            cmdg.add("tl2/ps-c1/ch3/Reset");
            cmdg.add("tl2/ps-c1/ch4/Reset");
            cmdg.add("tl2/ps-c1/ch5/Reset");
            cmdg.add("tl2/ps-c1/ch6/Reset");
            cmdg.add("tl2/ps-c1/ch7/Reset");
            cmdg.add("tl2/ps-c1/ch8/Reset");
            gcv.setModel(cmdg);
        } catch (Exception e) {
        }
        DeviceFactory.getInstance().setTraceMode(DeviceFactory.TRACE_COMMAND);
        javax.swing.JFrame f = new javax.swing.JFrame();
        f.getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc;
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.insets = new java.awt.Insets(0, 0, 0, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        f.getContentPane().add(gcv, gbc);
        f.pack();
        f.setVisible(true);
    }
}
