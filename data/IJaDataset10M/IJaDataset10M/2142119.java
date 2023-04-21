package jshm.wts.gui.wizards.scoreupload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jshm.gui.LoginDialog;
import jshm.wts.WTScore;
import jshm.wts.gui.GUI;
import jshm.wts.sh.Api;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.netbeans.spi.wizard.ResultProgressHandle;
import org.netbeans.spi.wizard.Summary;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

@SuppressWarnings("unchecked")
public class ScoreUploadWizard {

    static final Logger LOG = Logger.getLogger(ScoreUploadWizard.class.getName());

    public static Wizard createWizard(GUI gui) {
        final ScoreUploadWizard me = new ScoreUploadWizard(gui);
        return WizardPage.createWizard("Upload Scores to ScoreHero", new WizardPage[] { new VerifyScoresPage(gui) }, me.resultProducer);
    }

    GUI gui;

    private ScoreUploadWizard(GUI gui) {
        this.gui = gui;
    }

    private WizardResultProducer resultProducer = new WizardResultProducer() {

        @Override
        public boolean cancel(Map settings) {
            return true;
        }

        @Override
        public Object finish(Map wizardData) throws WizardException {
            return deferredResult;
        }
    };

    private DeferredWizardResult deferredResult = new DeferredWizardResult() {

        @Override
        public void start(Map settings, ResultProgressHandle progress) {
            try {
                if (!jshm.sh.Client.hasAuthCookies()) {
                    LoginDialog.showDialog();
                }
                int uploaded = 0, notUploaded = 0;
                List<String> resultStrings = new ArrayList<String>();
                List<WTScore> scores = gui.getScores();
                int scoreCount = scores.size();
                int curIndex = -1;
                WTScore s = null;
                for (int i = scores.size() - 1; i >= 0; i--) {
                    s = scores.get(i);
                    String scoreStr = s.getSong().title + " - " + s.getScore();
                    curIndex++;
                    try {
                        Thread.sleep(250);
                        if (curIndex % 5 == 4) {
                            LOG.fine("Sleeping so we don't spam SH");
                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException e) {
                    }
                    progress.setProgress(String.format("Uploading score %s of %s", curIndex + 1, scoreCount), curIndex, scoreCount);
                    try {
                        Api.submitWTScore(gui.getCurrentGame(), s);
                        gui.removeScores(i);
                    } catch (Exception e) {
                        notUploaded++;
                        LOG.log(Level.WARNING, "Error uploading score " + s, e);
                        resultStrings.add("Submit failed: " + scoreStr + ": " + e);
                        continue;
                    }
                    uploaded++;
                    resultStrings.add("Submitted: " + scoreStr);
                }
                resultStrings.add(String.format("Submitted: %s, failed: %s", uploaded, notUploaded));
                progress.finished(Summary.create(resultStrings.toArray(new String[0]), null));
            } catch (Throwable e) {
                LOG.log(Level.WARNING, "Failed to upload score", e);
                progress.failed("Failed to upload score: " + e.getMessage(), false);
                ErrorInfo ei = new ErrorInfo("Error", "Failed to upload score", null, null, e, null, null);
                JXErrorPane.showDialog(null, ei);
                return;
            }
        }
    };
}
