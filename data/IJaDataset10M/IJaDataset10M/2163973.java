package org.systemsbiology.apps.gui.client.data.transitionList;

import java.util.List;
import org.systemsbiology.apps.gui.domain.CandidateTransition;

/**
 * Interface for the transition list model
 * Contains methods for accessing necessary transition list information
 * 
 * @author Mark Christiansen
 *
 */
public interface ITransitionListModel {

    /**
	 * Set a possible transtion list 
	 * @param tranList list of candidate transitions
	 */
    public abstract void setPossibleTransitionList(List<CandidateTransition> tranList);

    /**
	 * Add a transition list listener
	 * @param listener transition list listener
	 */
    public abstract void addTransitionListListener(ITransitionListModelListener listener);

    /**
	 * Remove a transition list listener
	 * @param listener transition list listener
	 */
    public abstract void removeTransitionListListener(ITransitionListModelListener listener);
}
