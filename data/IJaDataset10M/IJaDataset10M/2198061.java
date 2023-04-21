package com.novasurv.turtle.frontend.swing.progress;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import javax.swing.*;

/**
 * @author Jason Dobies
 * @version $Revision$
 */
public class ProgressDialog extends JDialog implements PropertyChangeListener {

    private JLabel statusLabel;

    private JProgressBar progressBar;

    private ProgressTask task;

    public ProgressDialog(Dialog owner, ProgressTask task, int numberOfTasks) {
        super(owner);
        init(task, numberOfTasks);
    }

    public ProgressDialog(Frame owner, ProgressTask task, int numberOfTasks) {
        super(owner);
        init(task, numberOfTasks);
    }

    private void init(ProgressTask task, int numberOfTasks) {
        this.setModal(true);
        this.setTitle("Grading Progress");
        this.task = task;
        progressBar = new JProgressBar(0, numberOfTasks);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        statusLabel = new JLabel("Projects Progress", JLabel.CENTER);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 5));
        mainPanel.add(statusLabel, BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.SOUTH);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setContentPane(mainPanel);
        this.pack();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        progressBar.setValue(task.getCurrentValue());
        statusLabel.setText(task.getCurrentMessage());
    }

    public static void main(String[] args) {
        ProgressTask task = new ProgressTask() {

            private int value = 0;

            public int getCurrentValue() {
                return value;
            }

            public String getCurrentMessage() {
                return "Progress at " + value;
            }

            protected Void doInBackground() throws Exception {
                setProgress(value);
                Random random = new Random();
                while (value < 100) {
                    System.out.println("Task running " + value);
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException ignore) {
                    }
                    value += random.nextInt(10);
                    setProgress(Math.min(value, 100));
                }
                return null;
            }
        };
        JFrame parent = new JFrame();
        ProgressDialog dialog = new ProgressDialog(parent, task, 100);
        task.addPropertyChangeListener(dialog);
        task.execute();
        dialog.setVisible(true);
    }
}
