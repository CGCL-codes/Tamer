package com.panopset.flywheel.gui;

import static com.panopset.Util.*;
import com.panopset.flywheel.Command;
import com.panopset.flywheel.Flywheel;
import javax.swing.JTextArea;

/**
 *
 * @author Karl Dinwiddie
 */
public class ScriptFileContainer {

    private Flywheel flywheel;

    public FlywheelInfrastruture infrastructure;

    public JTextArea textArea;

    public Command resolvingCommand;

    private Thread stepper;

    public boolean isValid() {
        if (flywheel == null) {
            return false;
        }
        return flywheel.targetDirectory != null && flywheel.targetDirectory.isDirectory() && flywheel.targetDirectory.exists();
    }

    public void setFlywheel(Flywheel flywheel) {
        this.flywheel = flywheel;
    }

    public void step() {
        if (flywheel == null) return;
        if (flywheel.isStepping) {
            flywheel.isWaiting = false;
        } else {
            if (stepper != null) {
                throw new RuntimeException("Logic error");
            }
            flywheel.isStepping = true;
            flywheel.isWaiting = true;
            stepper = new Thread(new Runnable() {

                public void run() {
                    flywheel.exec();
                    stepper = null;
                }
            }, "Flywheel Stepping");
            stepper.start();
        }
    }

    public void exec() {
        if (flywheel == null) return;
        if (flywheel.isStepping) {
            flywheel.isStepping = false;
            flywheel.isWaiting = false;
        } else {
            flywheel.exec();
        }
    }

    public void load(Flywheel flywheel) {
        setFlywheel(flywheel);
        if (isValid()) {
            textArea.setText(flywheel.getTemplate().sls.getText());
            getInfrastructure().setFlywheel(flywheel);
        } else {
            dspmsg(x("Please specify a valid script file and target."));
        }
    }

    public void quit() {
        getInfrastructure().quit();
    }

    public FlywheelInfrastruture getInfrastructure() {
        if (infrastructure == null) {
            infrastructure = new FlywheelInfrastruture();
        }
        return infrastructure;
    }

    public JTextArea getTextArea() {
        if (textArea == null) {
            textArea = new JTextArea();
        }
        return textArea;
    }

    public void save() {
        if (isValid()) {
            dspmsg(x("NOT implemented yet..."));
        } else {
            dspmsg(x("Script(s) NOT saved, due to invalid configuration."));
        }
    }

    /**
     * For debugging
     * @param resolvingCommand
     */
    public void setResolvingCommand(Command resolvingCommand) {
        this.resolvingCommand = resolvingCommand;
    }
}
