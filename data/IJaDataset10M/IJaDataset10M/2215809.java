package net.sf.rcpforms.binding;

import net.sf.rcpforms.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * abstract implementation of a model validator for properties.
 * 
 * @author Marco van Meegen
 */
public abstract class AbstractModelValidator implements IModelValidator {

    public String getId() {
        return super.toString();
    }

    public Object[] getProperties() {
        return null;
    }

    /**
     * TODO: refine concept of what is passed. How to implement model-spanning
     * validations ?
     * 
     * @param model
     *            model to pass to the validator. The whole data model is passed
     *            to the validator
     * @return validation status of this validator.
     */
    public abstract IStatus validate(Object model);

    /**
     * convenience method to return an error status.
     */
    public static IStatus error(String message) {
        return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus warning(String message) {
        return new Status(IStatus.WARNING, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus info(String message) {
        return new Status(IStatus.INFO, Activator.PLUGIN_ID, IStatus.OK, message, null);
    }

    public static IStatus ok() {
        return Status.OK_STATUS;
    }
}
