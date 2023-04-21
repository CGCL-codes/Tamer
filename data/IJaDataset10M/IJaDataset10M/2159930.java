package org.jowidgets.tools.validation;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import org.jowidgets.util.Assert;
import org.jowidgets.validation.IValidateable;
import org.jowidgets.validation.IValidationConditionListener;
import org.jowidgets.validation.IValidationResult;

public class ValidationCache implements IValidateable {

    private final IValidationResultCreator validationResultCreator;

    private final Set<IValidationConditionListener> validationConditionListener;

    private boolean chacheDirty;

    private IValidationResult validationResult;

    public ValidationCache(final IValidationResultCreator validationResultCreator) {
        Assert.paramNotNull(validationResultCreator, "validationResultCreator");
        this.validationConditionListener = new LinkedHashSet<IValidationConditionListener>();
        this.validationResultCreator = validationResultCreator;
        this.chacheDirty = true;
    }

    /**
	 * Marks the cache to be dirty. The currently cached value will be rejected.
	 */
    public final void setDirty() {
        this.chacheDirty = true;
        for (final IValidationConditionListener listener : new LinkedList<IValidationConditionListener>(validationConditionListener)) {
            listener.validationConditionsChanged();
        }
    }

    @Override
    public final IValidationResult validate() {
        if (chacheDirty) {
            this.validationResult = validationResultCreator.createValidationResult();
            this.chacheDirty = false;
        }
        return validationResult;
    }

    @Override
    public final void addValidationConditionListener(final IValidationConditionListener listener) {
        Assert.paramNotNull(listener, "listener");
        validationConditionListener.add(listener);
    }

    @Override
    public final void removeValidationConditionListener(final IValidationConditionListener listener) {
        Assert.paramNotNull(listener, "listener");
        validationConditionListener.remove(listener);
    }

    public final void dispose() {
        validationConditionListener.clear();
    }

    public interface IValidationResultCreator {

        IValidationResult createValidationResult();
    }
}
