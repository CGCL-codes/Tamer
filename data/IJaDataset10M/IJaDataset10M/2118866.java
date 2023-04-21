package ch.ethz.inf.vs.wot.autowot.project.resources;

import ch.ethz.inf.vs.wot.autowot.project.handlers.HandlerCallbackType;

/**
 * Implementation of a Putter
 * 
 * @author Simon Mayer, simon.mayer@inf.ethz.ch, ETH Zurich
 * @author Claude Barthels, cbarthels@student.ethz.ch, ETH Zurich
 * 
 */
public class PutterItem extends AbstractResourceItem {

    protected String callbackMethod;

    protected HandlerCallbackType callbackMethodType;

    protected String argumentType;

    protected String presentationType;

    /**
	 * Create a new PutterItem from the name, method, argument type, presentation type and parent resource
	 * @param putterName - The name of the Putter
	 * @param callbackMethod - The Putter's callback method name
	 * @param putterArgumentType - The Putter's argument type
	 * @param putterPresentationType - The Putter's presentation type
	 * @param parent - The parent of the Putter
	 */
    public PutterItem(String putterName, String callbackMethod, HandlerCallbackType callbackMethodType, String putterArgumentType, String putterPresentationType, ResourceItem parent) {
        this.resourceName = putterName;
        this.callbackMethod = callbackMethod;
        this.callbackMethodType = callbackMethodType;
        this.argumentType = putterArgumentType;
        this.presentationType = putterPresentationType;
        this.parent = parent;
        this.setAsPutter();
    }

    /**
	 * Get the Putter's callback method name
	 * @return the Putter's callback method name
	 */
    public String getCallbackMethod() {
        return this.callbackMethod;
    }

    /**
	 * Set the Putter's callback method name
	 * @param callbackMethod - The callback method name to be set
	 */
    public void setCallbackMethod(String callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public HandlerCallbackType getCallbackMethodType() {
        return callbackMethodType;
    }

    public void setCallbackMethodType(HandlerCallbackType callbackMethodType) {
        this.callbackMethodType = callbackMethodType;
    }

    /**
	 * Get the Putter's argument type
	 * @return the Putter's argument type
	 */
    public String getPutterArgumentType() {
        return this.argumentType;
    }

    /**
	 * Get the Putter's presentation type
	 * @return the Putter's presentation type
	 */
    public String getPutterPresentationType() {
        return this.presentationType;
    }
}
