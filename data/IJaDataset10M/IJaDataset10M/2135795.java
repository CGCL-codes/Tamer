package uk.ac.lkl.migen.system.ai.analysis.core.shape;

import java.util.*;
import uk.ac.lkl.migen.system.ai.analysis.Verifier;
import uk.ac.lkl.migen.system.ai.analysis.trigger.AnalysisUpdateTrigger;
import uk.ac.lkl.migen.system.ai.analysis.trigger.AnalysisUpdateTriggerListener;
import uk.ac.lkl.migen.system.ai.um.IndicatorClass;
import uk.ac.lkl.migen.system.ai.um.VerificationStateIndicator;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.event.*;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.task.ConstructionExpressionTask;

/**
 * This class verifies whether the construction of the student 
 * comprises only single tiles. 
 * 
 * @author sergut
 *
 */
public class AllSingleTilesVerifier extends Verifier {

    /**
     * A new AllSingleTilesVerifier
     * 
     * @param model the construction of the student 
     * @param task the task
     * @param trigger the trigger (if any)
     */
    public AllSingleTilesVerifier(ExpresserModel model, ConstructionExpressionTask task, AnalysisUpdateTrigger trigger) {
        super(model, task, trigger);
        if (trigger == null) attachListenersToModel(); else attachListenersToTrigger();
    }

    /**
     * A new AllSingleTilesVerifier
     * 
     * For testing.
     * 
     * @param model the construction of the student 
     */
    public AllSingleTilesVerifier(ExpresserModel model) {
        super(model, null, null);
    }

    private void attachListenersToTrigger() {
        addUpdateListener(new AnalysisUpdateTriggerListener() {

            public void updateAnalysis() {
                verify();
            }
        });
    }

    private void attachListenersToModel() {
        ExpresserModel model = this.getModel();
        model.addObjectListener(new ObjectListener() {

            public void objectAdded(ObjectEvent e) {
                verify();
            }

            public void objectRemoved(ObjectEvent e) {
                verify();
            }
        });
    }

    @Override
    public boolean verify() {
        Collection<BlockShape> objectList = getModel().getShapes();
        if (objectList.isEmpty()) {
            setValue(false);
            return false;
        }
        for (BlockShape shape : objectList) {
            if (shape.isWizardOpen()) continue;
            if (!shape.isTile()) {
                setValue(false);
                return false;
            }
        }
        setValue(true);
        return true;
    }

    @Override
    @Deprecated
    public String getOutputName() {
        return "AllSingleTilesVerification";
    }

    @Override
    public VerificationStateIndicator getOutputIndicator() {
        return IndicatorClass.ALL_SINGLE_TILES;
    }
}
