package com.threerings.jpkg.ant.dpkg.dependencies;

import com.threerings.antidote.field.BaseField;
import com.threerings.antidote.field.OptionalField;
import com.threerings.antidote.property.StringProperty;
import com.threerings.jpkg.ant.dpkg.dependencies.conditions.Condition;
import com.threerings.jpkg.debian.dependency.AbstractDependency;
import static com.threerings.antidote.MutabilityHelper.areMutablesSet;
import static com.threerings.antidote.MutabilityHelper.requiresValidation;

/**
 * Base class for all {@link Dependencies} fields.
 * @see AbstractDependency
 */
public abstract class BaseDependency<T extends AbstractDependency> extends BaseField implements PackageInfoDependency {

    /**
     * Ant setter field: package. The name of the package.
     */
    public void setPackage(String value) {
        _packageName.setValue(value);
    }

    /**
     * Ant adder field: condition. All {@link Condition} objects.
     */
    public void add(Condition condition) {
        _condition.setField(condition);
    }

    /**
     * Construct an instance of the {@link AbstractDependency} defined in this field with just
     * the package name defined.
     */
    protected abstract T createDependency(StringProperty packageName);

    /**
     * Construct an instance of the {@link AbstractDependency} defined in this field with a
     * package name and condition defined.
     */
    protected abstract T createDependency(StringProperty packageName, Condition condition);

    /**
     * Get the concrete {@link AbstractDependency} after it has been created.
     */
    protected T getDependency() {
        requiresValidation(_abstractDependency);
        return _abstractDependency;
    }

    @Override
    protected void validateField() {
        switch(validateProperties(_packageName)) {
            case SOME_INVALID:
            case ALL_INVALID:
                return;
            case ALL_VALID:
                break;
        }
        switch(areMutablesSet(_condition)) {
            case ALL_UNSET:
            case SOME_UNSET:
                _abstractDependency = createDependency(_packageName);
                return;
            case ALL_SET:
                break;
        }
        switch(validateChildFields(_condition)) {
            case SOME_INVALID:
            case ALL_INVALID:
                return;
            case ALL_VALID:
                _abstractDependency = createDependency(_packageName, _condition.getField());
                return;
        }
    }

    /** The AbstractDependency object representing the user supplied data. */
    private T _abstractDependency;

    /** Ant adder/setter fields. */
    private final StringProperty _packageName = new StringProperty("package", this);

    private final OptionalField<Condition> _condition = new OptionalField<Condition>("condition", this);
}
