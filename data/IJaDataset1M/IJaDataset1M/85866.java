package net.jgf.logic.action.control;

/**
 *
 */
public class ActionStep {

    protected ActionStepType type;

    protected String ref;

    public ActionStep() {
        super();
    }

    /**
	 * @return the type
	 */
    public ActionStepType getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(ActionStepType type) {
        this.type = type;
    }

    /**
     * @param type the type to set
     */
    public ActionStep(ActionStepType type, String ref) {
        super();
        this.type = type;
        this.ref = ref;
    }

    /**
	 * @return the ref
	 */
    public String getRef() {
        return ref;
    }

    /**
	 * @param ref the ref to set
	 */
    public void setRef(String ref) {
        this.ref = ref;
    }
}
