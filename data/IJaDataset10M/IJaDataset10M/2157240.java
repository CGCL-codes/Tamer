package edu.harvard.fas.rregan.requel.project;

/**
 * A stakeholder that is not a user of the system, this is basically a holder
 * for authorities that dictate rules that will be represented as goals.
 * 
 * @author ron
 */
public interface NonUserStakeholder extends Stakeholder {

    /**
	 * @return A description of the stakeholder
	 */
    public String getText();
}
