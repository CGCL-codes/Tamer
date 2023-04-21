package com.agentfactory.afapl2.interpreter.control.cms;

import com.agentfactory.afapl2.interpreter.mentalState.Commitment;

/**
 *
 * @author Administrator
 */
public class NoActivityHandler extends ActivityHandler {

    /** Creates a new instance of PARActivityHandler */
    public NoActivityHandler() {
    }

    @Override
    public boolean handle() {
        if (commitment.getState() == Commitment.STATE_NEW) {
            System.out.println("No Plan Handler For: " + commitment);
            commitment.update(Commitment.EVENT_FAILED);
            return true;
        } else if (commitment.getState() == Commitment.STATE_ACTIVE) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not be in state: STATE_ACTIVE");
        } else if (commitment.getState() == Commitment.STATE_WAITING) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not be in state: STATE_WAITING");
        } else if (commitment.getState() == Commitment.STATE_FAILED) {
            if (!commitment.hasChildren()) {
                Commitment parent = commitment.getParent();
                if (parent != null) {
                    parent.update(Commitment.EVENT_CHILD_FAILED);
                }
                return false;
            }
        } else if (commitment.getState() == Commitment.STATE_REDUNDANT) {
            return false;
        }
        return super.handle();
    }

    @Override
    public void handleEvent(int event) {
        if (event == Commitment.EVENT_ACTIVATED) {
            commitment.setState(this, Commitment.STATE_ACTIVE);
        } else if (event == Commitment.EVENT_SUCCEEDED) {
            commitment.setState(this, Commitment.STATE_SUCCEEDED);
        } else if (event == Commitment.EVENT_FAILED) {
            commitment.setState(this, Commitment.STATE_FAILED);
        } else if (event == Commitment.EVENT_CHILD_SUCCEEDED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_CHILD_SUCCEEDED");
        } else if (event == Commitment.EVENT_CHILD_FAILED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_CHILD_FAILED");
        } else if (event == Commitment.EVENT_PARENT_FAILED) {
            throw new RuntimeException("Commitment: " + commitment + "\nShould not receive event: EVENT_PARENT_FAILED");
        } else if (event == Commitment.EVENT_REDUNDANT) {
            commitment.setState(this, Commitment.STATE_REDUNDANT);
        }
        super.handleEvent(event);
    }
}
