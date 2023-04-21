package de.mogwai.common.client.binding;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.border.Border;
import de.mogwai.common.client.binding.configuration.BindingConfiguration;
import de.mogwai.common.client.binding.configurator.Configurator;
import de.mogwai.common.client.binding.converter.DefaultConverter;
import de.mogwai.common.client.binding.tools.BeanUtils;
import de.mogwai.common.client.binding.validator.ValidationError;
import de.mogwai.common.client.binding.validator.Validator;

/**
 * @author msertic
 */
public class PropertyAdapter {

    protected JComponent component;

    protected String destinationPropertyName;

    protected Converter converter;

    protected Configurator configurator;

    protected Validator validator;

    protected BindingInfo bindingInfo;

    private Border oldBorder;

    private boolean borderSaved;

    private String oldTooltip;

    /**
     * Constructor.
     * 
     * @param aComponent
     * @param aDestinationPropertyName
     */
    public PropertyAdapter(JComponent aComponent, String aDestinationPropertyName) {
        this(aComponent, aDestinationPropertyName, DefaultConverter.getInstance(), null, null);
    }

    /**
     * Constructor.
     * 
     * @param aComponent
     * @param aDestinationPropertyName
     * @param aConfigurator
     */
    public PropertyAdapter(JComponent aComponent, String aDestinationPropertyName, Converter aConverter, Configurator aConfigurator, Validator aValidator) {
        component = aComponent;
        destinationPropertyName = aDestinationPropertyName;
        converter = aConverter;
        configurator = aConfigurator;
        validator = aValidator;
    }

    /**
     * Transfer the data from the model to the view component.
     * 
     * @param aModel
     * @param aPropertyName
     */
    public void model2view(Object aModel, String aPropertyName) {
        Class type = String.class;
        try {
            Object value = "";
            try {
                value = BeanUtils.getProperty(aModel, aPropertyName);
                type = BeanUtils.getPropertyType(aModel, aPropertyName);
            } catch (Exception e) {
            }
            if (converter != null) {
                value = converter.convertFromModel2View(value, type);
            }
            BeanUtils.setProperty(component, destinationPropertyName, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot transfer model2view for property " + aPropertyName + ", type is " + type, e);
        }
    }

    /**
     * Get the current value from the view.
     * 
     * @return the current value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public Object getViewValue() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object value = BeanUtils.getProperty(component, destinationPropertyName);
        if (converter != null) {
            value = converter.convertFromView2Model(value);
        }
        return value;
    }

    /**
     * Transfer the data from the view to the model.
     * 
     * @param aModel
     * @param aPropertyName
     */
    public void view2model(Object aModel, String aPropertyName) {
        try {
            Object value = getViewValue();
            BeanUtils.setProperty(aModel, aPropertyName, value);
        } catch (Exception e) {
            throw new RuntimeException("Cannot transfer view2model for property " + aPropertyName, e);
        }
    }

    public void configure() {
        if (configurator != null) {
            configurator.configure(this);
        }
    }

    public JComponent[] getComponent() {
        return new JComponent[] { component };
    }

    public boolean isReadonly() {
        return false;
    }

    public Validator getValidator() {
        return validator;
    }

    public BindingInfo getBinding() {
        return bindingInfo;
    }

    public void setBinding(BindingInfo aBindingInfo) {
        bindingInfo = aBindingInfo;
    }

    public void markInvalid(List<ValidationError> aErrors) {
        JComponent theComponent = getComponent()[0];
        if (!borderSaved) {
            oldBorder = theComponent.getBorder();
            oldTooltip = theComponent.getToolTipText();
            theComponent.setBorder(BindingConfiguration.getInstance().getErrorBorder());
            theComponent.setToolTipText(aErrors.get(0).toString());
            borderSaved = true;
        }
        getBinding().markInvalid(this, aErrors);
    }

    public void markValid() {
        JComponent theComponent = getComponent()[0];
        if (borderSaved) {
            borderSaved = false;
            theComponent.setBorder(oldBorder);
            theComponent.setToolTipText(oldTooltip);
            getBinding().markValid(this);
        }
    }

    public List<ValidationError> validate() {
        try {
            Validator theValidator = getValidator();
            if (theValidator != null) {
                List<ValidationError> theErrors = theValidator.validate(this);
                if (theErrors.size() > 0) {
                    markInvalid(theErrors);
                } else {
                    markValid();
                }
                return theErrors;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return BindingConfiguration.EMPTY_LIST;
    }
}
