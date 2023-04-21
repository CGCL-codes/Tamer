package com.agentfactory.afapl2.interpreter.control.cms;

import com.agentfactory.afapl2.interpreter.mentalState.Commitment;
import com.agentfactory.logic.lang.FOS;

/**
 * This plan operator replaces the existing dropBelief(...) action to allow
 * semantic checking of the content of the mental attitude being drop.
 * 
 * TODO (Rem Collier): Need to support other modalities (e.g. GOAL)
 * 
 * @author Rem Collier
 */
public class MAINTAINActivityHandler extends ActivityHandler {

    private long time;

    private long delay;

    /** Creates a new instance of PARActivityHandler */
    public MAINTAINActivityHandler() {
    }

    @Override
    public boolean handle() {
        if (commitment.getState() == Commitment.STATE_NEW) {
            FOS arg = commitment.getActivity().argAt(0);
            if (arg.getFunctor().equals("GOAL")) {
                agent.getGoalManager().mantainGoal(arg.toString());
                commitment.update(Commitment.EVENT_SUCCEEDED);
            } else {
                commitment.update(Commitment.EVENT_FAILED);
            }
        } else if (commitment.getState() == Commitment.STATE_ACTIVE) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not be in state: STATE_WAITING");
        } else if (commitment.getState() == Commitment.STATE_WAITING) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not be in state: STATE_WAITING");
        } else if (commitment.getState() == Commitment.STATE_FAILED) {
            return false;
        } else if (commitment.getState() == Commitment.STATE_REDUNDANT) {
            return false;
        }
        return super.handle();
    }

    @Override
    public void handleEvent(int event) {
        if (event == Commitment.EVENT_ACTIVATED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_ACTIVATED");
        } else if (event == Commitment.EVENT_SUCCEEDED) {
            commitment.setState(this, Commitment.STATE_SUCCEEDED);
        } else if (event == Commitment.EVENT_FAILED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_FAILED");
        } else if (event == Commitment.EVENT_CHILD_SUCCEEDED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_CHILD_SUCCEEDED");
        } else if (event == Commitment.EVENT_CHILD_FAILED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_CHILD_FAILED");
        } else if (event == Commitment.EVENT_PARENT_FAILED) {
            commitment.setState(this, Commitment.STATE_FAILED);
        } else if (event == Commitment.EVENT_REDUNDANT) {
            commitment.setState(this, Commitment.STATE_REDUNDANT);
        }
        super.handleEvent(event);
    }
}
