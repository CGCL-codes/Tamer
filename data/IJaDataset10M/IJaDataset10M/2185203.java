package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.*;

/**
 * @author Frank Staudinger
 *
 */
public class GotoStepAction extends Action {

    protected int m_nGotoStep;

    protected int m_nLastStep;

    /**
	 * @param ctrl
	 * @param nGotoStepIndex zero-based index for the next step in the algo 
	 * @param bExcuteNow true if the action should do it now
	 */
    public GotoStepAction(Controller ctrl, int nGotoStepIndex, boolean bExcuteNow) throws ActionException {
        this(ctrl, nGotoStepIndex);
        if (bExcuteNow == true) super.registerAndDo(true);
    }

    /**
	 * @param ctrl
	 * @param nGotoStepIndex zero-based index for the next step in the algo 
	 * 
	 */
    protected GotoStepAction(Controller ctrl, int nGotoStepIndex) throws ActionException {
        super(ctrl);
        m_nGotoStep = nGotoStepIndex;
        m_nLastStep = getController().getCurrentStep();
    }

    public boolean doAction() throws ActionException {
        State state = getController().getState(m_nGotoStep);
        if (state != null) {
            getController().setStatusbarText(state.getDescriptionEx());
            getController().setModifiedFlag();
        }
        return true;
    }

    public boolean undoAction() throws ActionException {
        State state = getController().getState(m_nLastStep);
        if (state != null) {
            getController().setStatusbarText(state.getDescriptionEx());
            getController().setModifiedFlag();
        }
        return true;
    }

    /**
	 * @return Returns the m_nGotoStep.
	 */
    protected int getGotoStepIndex() {
        return m_nGotoStep;
    }

    /**
	 * @param gotoStep The m_nGotoStep to set.
	 */
    protected void setGotoStepIndex(int gotoStep) {
        m_nGotoStep = gotoStep;
    }
}
