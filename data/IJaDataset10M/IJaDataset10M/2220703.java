package backend.validation;

import java.util.Iterator;
import java.util.Vector;
import backend.event.ValidatorEvent;
import backend.event.ValidatorListener;
import backend.event.type.EventType;

/**
 * Abstract implementation of an ONDEX validator, 
 * manages listener handling.
 * 
 * @author taubertj
 *
 */
public abstract class AbstractONDEXValidator {

    private Vector<ValidatorListener> listeners = new Vector<ValidatorListener>();

    /**
	 * Returns the name of the ONDEX validator.
	 * 
	 * @return name of validator
	 */
    public abstract String getName();

    /**
     * Returns the version number of the ONDEX validator.
     * 
     * @return version number of validator
     */
    public abstract String getVersion();

    /**
     * Sets the arguments the validator should use.
     * 
     * @param va - ValidatorArguments
     */
    public abstract void setValidatorArguments(ValidatorArguments va);

    /**
     * Returns the actual validator arguments.
     * 
     * @return actual ValidatorArguments
     */
    public abstract ValidatorArguments getValidatorArguments();

    /**
	 * Validates a given object and returns the results.
	 * 
	 * @param o - Object to validate
	 * @return results
	 */
    public abstract Object validate(Object o);

    /**
	 * Cleans all temporary files associated with the validator.
	 *
	 */
    public abstract void cleanup();

    /**
	 * Adds a validator listener to the list.
	 * 
	 * @param l - ValidatorListener
	 */
    public void addValidatorListener(ValidatorListener l) {
        listeners.add(l);
    }

    /**
	 * Removes a validator listener from the list.
	 * 
	 * @param l - ValidatorListener
	 */
    public void removeValidatorListener(ValidatorListener l) {
        listeners.remove(l);
    }

    /**
	 * Returns the list of validator listeners.
	 * 
	 * @return list of ValidatorListeners
	 */
    public ValidatorListener[] getValidatorListeners() {
        return listeners.toArray(new ValidatorListener[0]);
    }

    /**
	 * Notify all listeners that have registered with this class.
	 * 
	 * @param eventName - name of event
	 */
    protected void fireEventOccurred(EventType e) {
        if (listeners.size() > 0) {
            ValidatorEvent ve = new ValidatorEvent(this, e);
            Iterator<ValidatorListener> it = listeners.iterator();
            while (it.hasNext()) {
                it.next().eventOccurred(ve);
            }
        }
    }
}
