package uk.ac.lkl.migen.system.ai.feedback.ui.deployer;

import javax.swing.SwingUtilities;
import uk.ac.lkl.common.ui.jft.Stencil;
import uk.ac.lkl.migen.system.ai.analysis.core.FeedbackLoopCounter;
import uk.ac.lkl.migen.system.ai.feedback.intervention.*;
import uk.ac.lkl.migen.system.ai.feedback.strategy.*;
import uk.ac.lkl.migen.system.ai.feedback.ui.callout.ModalPaperNoteCallOut;
import uk.ac.lkl.migen.system.ai.um.*;

public class ExistingOverlapsInterventionDeployer extends StencilInterventionDeployer {

    public ExistingOverlapsInterventionDeployer(Stencil stencil, ShortTermLearnerModel um, FeedbackLoopCounter counter) {
        super(stencil, FeedbackStrategyType.EXISTING_OVERLAPS, um, counter);
    }

    @Override
    protected void show(Intervention i) {
        final Intervention intervention = i;
        Runnable delayedPresent = new Runnable() {

            @Override
            public void run() {
                String interventionText = getMessageText(intervention);
                ModalPaperNoteCallOut dialog = new ModalPaperNoteCallOut(getStencil(), interventionText);
                dialog.showMessage();
            }
        };
        SwingUtilities.invokeLater(delayedPresent);
    }

    private String getText() {
        String text = "Your model has tiles on top of each other (marked <b>x</b>).<br>" + "You need to remove a pattern of the same colour tiles to<br>" + "leave just one layer of tiles.";
        return text;
    }

    @Override
    public String getCommentText(Intervention intervention) {
        return getText();
    }

    @Override
    public String getQuestionText(Intervention intervention) {
        return getText();
    }

    @Override
    public String getSuggestionText(Intervention intervention) {
        return getText();
    }
}
