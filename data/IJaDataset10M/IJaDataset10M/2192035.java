package net.sourceforge.omov.app.gui.preferences;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.VersionMajorMinor;
import net.sourceforge.omov.guicore.GuiActionListener;
import net.sourceforge.omov.logic.tools.ApplicationVersionFetcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class VersionCheckDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(VersionCheckDialog.class);

    private static final long serialVersionUID = 6136505135179155308L;

    private static final String DIALOG_TITLE = "Checking Version";

    private final VersionCheckWorker worker = new VersionCheckWorker();

    private final boolean shouldSuccessfullDialogDisplayed;

    public VersionCheckDialog() {
        this.shouldSuccessfullDialogDisplayed = false;
        this.setTitle(DIALOG_TITLE);
        this.pseudoConstructor();
    }

    public VersionCheckDialog(JDialog owner) {
        super(owner, DIALOG_TITLE, true);
        this.shouldSuccessfullDialogDisplayed = true;
        this.pseudoConstructor();
    }

    private void pseudoConstructor() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        PtGuiUtil.setCenterLocation(this);
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        final JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new GuiActionListener() {

            @Override
            public void action(ActionEvent e) {
                doCancel();
            }
        });
        panel.add(progressBar);
        panel.add(btnCancel);
        return panel;
    }

    public void startCheck() {
        LOG.debug("Starting check by executing swing worker.");
        this.worker.execute();
    }

    private void doCancel() {
        LOG.debug("do cancel pressed by user.");
        this.worker.cancel(true);
    }

    private void workerFinished() {
        LOG.debug("Worker finished work (isCancelled = " + worker.isCancelled() + ").");
        if (this.worker.isCancelled() == false) {
            VersionMajorMinor versionFetched = null;
            try {
                versionFetched = this.worker.get();
            } catch (Exception e) {
                LOG.error("Could not get result from swing worker!", e);
            }
            if (versionFetched == null) {
                PtGuiUtil.warning(this, "Application Software Update", "Could not connect to the internet.");
            } else {
                final VersionMajorMinor versionInUse = BeanFactory.getInstance().getCurrentApplicationVersion();
                if (versionInUse.equals(versionFetched) == true) {
                    LOG.debug("Application in use (v" + versionInUse + ") is up to date.");
                    if (this.shouldSuccessfullDialogDisplayed == true) {
                        PtGuiUtil.info(this, "Application Software Update", "Your version " + versionInUse + " is up to date.");
                    }
                } else {
                    PtGuiUtil.info(this, "Application Software Update", "It seems as you were running an old application version.\n" + "You are using " + versionInUse + " but version " + versionFetched + " is available.\n" + "Check the website to download the most recent release:\n" + Constants.getWebUrl());
                }
            }
        }
        this.dispose();
    }

    private class VersionCheckWorker extends SwingWorker<VersionMajorMinor, String> {

        @Override
        protected VersionMajorMinor doInBackground() throws Exception {
            LOG.debug("worker is working in background");
            return ApplicationVersionFetcher.fetchVersion();
        }

        @Override
        protected void done() {
            VersionCheckDialog.this.workerFinished();
        }
    }
}
