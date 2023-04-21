package transport.action;

import transport.IAction;

/**
 * Represents the say action
 * 
 * @author rem
 */
public class SayAction implements IAction {

    private String message;

    public SayAction(String message) {
        this.message = message;
    }

    public String toMessageString() {
        return "(say \"" + message + "\")\0";
    }
}
