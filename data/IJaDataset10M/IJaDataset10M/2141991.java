package jshm.gui.wizards.scoreupload;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jshm.Score;
import jshm.gui.LoginDialog;
import jshm.gui.datamodels.ScoresTreeTableModel;
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

    public static Wizard createWizard(ScoresTreeTableModel model) {
        final ScoreUploadWizard me = new ScoreUploadWizard();
        return WizardPage.createWizard("Upload Scores to ScoreHero", new WizardPage[] { new VerifyScoresPage(model) }, me.resultProducer);
    }

    private ScoreUploadWizard() {
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
                boolean uploadUnknown = false;
                try {
                    uploadUnknown = (Boolean) settings.get("uploadUnknown");
                } catch (Exception e) {
                }
                ScoresTreeTableModel model = (ScoresTreeTableModel) settings.get("treeTableData");
                int uploaded = 0, possiblyUploaded = 0, notUploaded = 0;
                List<String> resultStrings = new ArrayList<String>();
                if (uploadUnknown) resultStrings.add("Going to upload scores with unknown status");
                int scoreCount = model.getScoreCount();
                int curIndex = -1;
                for (Score s : model.getScores()) {
                    String scoreStr = s.getSong().getTitle() + " - " + s.getScore();
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
                    if (s.getStatus() == Score.Status.UNKNOWN && !uploadUnknown) continue;
                    try {
                        s.submit();
                    } catch (Exception e) {
                        notUploaded++;
                        LOG.log(Level.WARNING, "Error uploading score " + s, e);
                        resultStrings.add("Submit failed: " + scoreStr + ": " + e);
                        continue;
                    }
                    if (s.getStatus() == Score.Status.UNKNOWN) possiblyUploaded++; else uploaded++;
                    resultStrings.add((s.getStatus() == Score.Status.UNKNOWN ? "Possibly submitted" : "Submitted") + ": " + scoreStr);
                }
                resultStrings.add(String.format("Submitted: %s, uncertain about: %s, failed: %s", uploaded, possiblyUploaded, notUploaded));
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
