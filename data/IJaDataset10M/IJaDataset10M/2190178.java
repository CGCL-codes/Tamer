package org.cgtools.anttasks.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public abstract class ValidableInputTask extends Task {

    private String message;

    private String title = "Input task";

    private boolean launchGui = true;

    protected boolean closeGuiBeforeProceed = false;

    public void createAndShowGui() throws Exception, InterruptedException, IOException, BadLocationException {
        final JFrame mainFrame = new JFrame();
        mainFrame.setTitle(title);
        if (message != null) {
            final JPanel labelPanel = new JPanel(new BorderLayout());
            labelPanel.add(new JLabel(message, JLabel.CENTER), "Center");
            mainFrame.getContentPane().add(labelPanel, "North");
        }
        mainFrame.getContentPane().add(getUserPane(), "Center");
        final ValidationChoice choice = new ValidationChoice();
        mainFrame.getContentPane().add(choice, "South");
        mainFrame.pack();
        setSize(mainFrame);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        int result = choice.waitForChoice();
        switch(result) {
            case ValidationChoice.OK:
                if (closeGuiBeforeProceed) {
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                }
                proceed();
                if (!closeGuiBeforeProceed) {
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                }
                break;
            case ValidationChoice.CANCEL:
                throw new BuildException("Cancel");
            default:
                throw new BuildException("Internal Error");
        }
    }

    protected abstract void precheck();

    protected abstract JComponent getUserPane() throws IOException, BadLocationException;

    protected abstract void proceed() throws Exception;

    protected void setSize(JFrame mainFrame) {
    }

    ;

    public void execute() throws BuildException {
        try {
            precheck();
            if (launchGui) {
                createAndShowGui();
            } else {
                getUserPane();
                proceed();
            }
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    public void setLaunchGui(boolean launchGui) {
        this.launchGui = launchGui;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ValidationChoice extends JPanel {

        public static final int OK = 0;

        public static final int CANCEL = 1;

        Object sync = new Object();

        int result;

        public ValidationChoice() {
            addButton(new Action(OK, "OK"), "West");
            addButton(new Action(CANCEL, "Cancel"), "East");
        }

        public int waitForChoice() throws InterruptedException {
            synchronized (sync) {
                sync.wait();
            }
            return result;
        }

        private void addButton(final Action action, final String pos) {
            add(new JButton(new AbstractAction(action.name) {

                public void actionPerformed(ActionEvent e) {
                    result = action.val;
                    synchronized (sync) {
                        sync.notifyAll();
                    }
                }
            }), pos);
        }

        public static class Action {

            int val;

            String name;

            public Action(int val, String name) {
                this.val = val;
                this.name = name;
            }
        }
    }
}
