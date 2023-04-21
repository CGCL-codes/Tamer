package net.sf.echobinding.binding;

import java.util.HashSet;
import java.util.Set;
import net.sf.echobinding.decorator.Decorator;
import net.sf.echobinding.decorator.DefaultDecorator;
import net.sf.echobinding.format.Format;
import net.sf.echobinding.validation.*;

/**
 * @see net.sf.echobinding.binding.PropertyAdapter
 */
public abstract class AbstractPropertyAdapter implements PropertyAdapter {

    private Set<Validator> _validators;

    private Format _format;

    private Decorator _decorator;

    private String _label;

    private String _id;

    private ValidationHandler _validationHandler;

    private BindingContext _subContext;

    protected Object format(Object value) {
        if (getFormat() != null) return getFormat().format(value);
        return value;
    }

    protected Object parse(Object value) {
        if (getFormat() != null) return getFormat().parse((String) value);
        return value;
    }

    public ValidationReport validate(Object bean, Object value) {
        ValidationReport validationReport = new ValidationReport(true, null);
        if (getValidators() != null) {
            for (Validator validator : getValidators()) {
                validationReport = validator.validate(bean, parse(value));
                if (validationReport != null && !validationReport.isValid()) return validationReport;
            }
        }
        return validationReport;
    }

    public PropertyAdapter setValidators(Set<Validator> validators) {
        _validators = validators;
        return this;
    }

    public PropertyAdapter addValidator(Validator validator) {
        if (_validators == null) _validators = new HashSet<Validator>();
        _validators.add(validator);
        return this;
    }

    public PropertyAdapter setFormat(Format format) {
        _format = format;
        return this;
    }

    public PropertyAdapter setDecorator(Decorator decorator) {
        _decorator = decorator;
        return this;
    }

    public Decorator getDecorator() {
        if (_decorator == null) return new DefaultDecorator();
        return _decorator;
    }

    public String getLabel() {
        return _label;
    }

    public PropertyAdapter setLabel(String label) {
        _label = label;
        return this;
    }

    public String getId() {
        return _id;
    }

    public PropertyAdapter setId(String id) {
        _id = id;
        return this;
    }

    public Format getFormat() {
        return _format;
    }

    /**
	 * @return Returns the validator.
	 */
    public Set<Validator> getValidators() {
        return _validators;
    }

    public PropertyAdapter setValidationHandler(ValidationHandler validationHandler) {
        _validationHandler = validationHandler;
        return this;
    }

    public ValidationHandler getValidationHandler() {
        return _validationHandler;
    }

    public PropertyAdapter setSubContext(BindingContext context) {
        _subContext = context;
        return this;
    }

    public BindingContext getSubContext() {
        return _subContext;
    }
}
