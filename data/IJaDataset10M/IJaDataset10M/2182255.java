package org.pachyderm.migrationtool;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressScreen extends JPanel implements CardInfo {

    private static final long serialVersionUID = 3108427713725598627L;

    private JProgressBar progressBar = null;

    private JLabel progressMsg = null;

    private MigrationProperties mp = null;

    private MigrationToolController controller = null;

    /**
   * Creates a new ProgressScreen object.
   *
   * @param controller
   *          Controller class for controller
   */
    public ProgressScreen(MigrationProperties mp, MigrationToolController controller) {
        this.mp = mp;
        this.controller = controller;
        layoutComponents();
    }

    /**
   * The installation is done, set the cancel button to say done and be enabled to quit the
   * controller
   */
    public void migrationDone() {
        progressBar.setValue(progressBar.getMaximum());
        progressMsg.setText("Done");
        controller.disableContinueButton();
    }

    /**
   * Start doing the migration tasks
   */
    public void startMigrationTasks() {
        DoMigrationTasks dmt = new DoMigrationTasks(progressBar, progressMsg, this, controller);
        dmt.start();
    }

    /**
   * Layout the components in this screen
   */
    private void layoutComponents() {
        final int SIDE_INSETS = 20;
        setLayout(new GridBagLayout());
        progressBar = new JProgressBar(0, 30);
        progressMsg = new JLabel(mp.getString("ps.starting"), JLabel.CENTER);
        add(progressBar, new Gbc(0, 0, Gbc.CENTER, Gbc.HORIZONTAL, new Insets(0, SIDE_INSETS, 4, SIDE_INSETS)));
        add(progressMsg, new Gbc(0, 1, Gbc.CENTER, Gbc.HORIZONTAL, new Insets(4, SIDE_INSETS, 0, SIDE_INSETS)));
        progressMsg.setFont(new Font("Sans-Serif", Font.PLAIN, 18));
    }

    public String getCardName() {
        return "progress";
    }
}
