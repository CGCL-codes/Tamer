package org.systemsbiology.apps.gui.client.controller.request;

import java.util.List;
import org.systemsbiology.apps.gui.client.constants.RequestType;
import org.systemsbiology.apps.gui.domain.CandidateTransition;
import org.systemsbiology.apps.gui.domain.TransitionListGeneratorSetup;

/**
 * Request for saving a transition generator in a project.
 * 
 * @author Mark Christiansen
 * @version 1.0
 * 
 * @see org.systemsbiology.apps.gui.domain.CandidateTransition
 * @see org.systemsbiology.apps.gui.domain.TransitionListGeneratorSetup
 */
@SuppressWarnings("serial")
public class SaveTransitionListGeneratorSetupRequest extends Request {

    private TransitionListGeneratorSetup tlgSetup;

    private List<CandidateTransition> newList;

    private Boolean executeAfterSaved = false;

    /**
	 * Creates a request to save the transition list generator setup
	 */
    public SaveTransitionListGeneratorSetupRequest() {
    }

    /**
	 * Creates a request to save the transition list generator setup
	 * @param tlgSetup the transition list generator setup object to be saved 
	 * @param newList the new list of candidate transitions to be saved
	 * @param executeAfterSaved flag to indicate whether an server side script should execute after saving
	 */
    public SaveTransitionListGeneratorSetupRequest(TransitionListGeneratorSetup tlgSetup, List<CandidateTransition> newList, Boolean executeAfterSaved) {
        this.setTlgSetup(tlgSetup);
        this.setNewList(newList);
        this.setExecuteAfterSaved(executeAfterSaved);
    }

    /**
	 * Returns <code>RequestType.SET_TRAN_GEN</code>
	 */
    public RequestType getRequestType() {
        return RequestType.SET_TRAN_GEN;
    }

    /**
	 * Set the transition list generator setup to save
	 * @param tgSetup transition list generator setup
	 */
    public void setTlgSetup(TransitionListGeneratorSetup tgSetup) {
        this.tlgSetup = tgSetup;
    }

    /**
	 * Get the transition list generator setup to save
	 * @return transition list generator setup
	 */
    public TransitionListGeneratorSetup getTlgSetup() {
        return tlgSetup;
    }

    /**
	 * Set the new list of candidate transitions to save
	 * @param newList new list of candidate transitions
	 */
    public void setNewList(List<CandidateTransition> newList) {
        this.newList = newList;
    }

    /**
	 * Get the new list of candidate transitions to save
	 * @return new list of candidate transitions
	 */
    public List<CandidateTransition> getNewList() {
        return newList;
    }

    /**
	 * Set whether or not to execute transition list generator after saving
	 * @param executeAfterSaved boolean telling servlet to execute after saving
	 */
    public void setExecuteAfterSaved(Boolean executeAfterSaved) {
        this.executeAfterSaved = executeAfterSaved;
    }

    /**
	 * Get whether or not to execute transition list generator after saving
	 * @return boolean on whether to execute servlet algorithm
	 */
    public Boolean getExecuteAfterSaved() {
        return executeAfterSaved;
    }
}
