package net.sf.jzeno.echo.databinding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jzeno.aop.ServletSupport;
import net.sf.jzeno.echo.EchoSupport;
import nextapp.echo.Color;
import nextapp.echo.TextField;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;

/**
 * @author ddhondt
 */
public class DynaTextField extends TextField implements PropertyComponent, PropertyChangeListener {

    public static final long serialVersionUID = 1L;

    protected static Logger log = Logger.getLogger(DynaTextField.class);

    protected PropertyComponentSupport propertyComponentSupport = null;

    private String nullValue = null;

    protected boolean ignoreUpdates = true;

    private String invalidMessageKey = "dynatextfield.invalid";

    private int exactLength = 0;

    private boolean autoTrim = true;

    public DynaTextField() {
        this(null, null, null);
    }

    public DynaTextField(Class beanClass, String propertyName, String constructionHints) {
        propertyComponentSupport = new PropertyComponentSupport(beanClass, propertyName, this);
        setBackground(Color.WHITE);
        setRequired(false);
        EchoSupport.executeHints(this, constructionHints);
        addPropertyChangeListener(this);
    }

    public void rebind() {
        try {
            ignoreUpdates = true;
            Object value = getValue();
            if (value != null) {
                if (autoTrim) {
                    setText(getValue().toString().trim());
                } else {
                    setText(getValue().toString());
                }
            } else {
                if (nullValue != null) {
                    setText(nullValue);
                } else {
                    setText("");
                }
            }
        } finally {
            ignoreUpdates = false;
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (!ignoreUpdates && (evt.getPropertyName() == null || evt.getPropertyName().equals("text"))) {
            String text = getText();
            if (isValid()) {
                if (text != null && autoTrim) {
                    text = text.trim();
                }
                if (nullValue != null && text != null && text.equals(nullValue)) {
                    setValue(null);
                } else {
                    setValue(text);
                }
            }
        }
    }

    public boolean isValid() {
        boolean ret = false;
        String text = getText();
        if (!isRequired() && text != null && nullValue != null && text.equalsIgnoreCase(nullValue)) {
            ret = true;
        } else {
            if (isRequired() && GenericValidator.isBlankOrNull(text)) {
                ret = false;
            } else {
                ret = true;
            }
            if (getExactLength() > 0) {
                if (GenericValidator.isBlankOrNull(text)) {
                    ret = !isRequired();
                } else {
                    ret = (text.length() == getExactLength());
                }
            }
        }
        return ret;
    }

    /**
     * @return Returns the nullValue.
     */
    public String getNullValue() {
        return nullValue;
    }

    /**
     * @param nullValue
     *            The nullValue to set.
     */
    public void setNullValue(String nullValue) {
        String oldNullValue = this.nullValue;
        this.nullValue = nullValue;
        firePropertyChange("nullValue", oldNullValue, nullValue);
    }

    public Decorator getDecorator() {
        return propertyComponentSupport.getDecorator();
    }

    public void setDecorator(Decorator decorator) {
        propertyComponentSupport.setDecorator(decorator);
    }

    public void doValidation() {
        if (!isValid()) {
            setBorderColor(EchoSupport.COLOR_INVALID);
            setBorderSize(4);
            if (getInvalidMessageKey() != null && getInvalidMessageKey().trim().length() > 0) ServletSupport.addError(getInvalidMessageKey());
        } else {
            setBorderColor(null);
            setBorderSize(2);
        }
    }

    public String getToolTipText() {
        String ret = super.getToolTipText();
        if (!isValid() && isRequired()) {
            ret = "This field is required";
        }
        return ret;
    }

    public String getInvalidMessageKey() {
        return invalidMessageKey;
    }

    public void setInvalidMessageKey(String invalidMessageKey) {
        this.invalidMessageKey = invalidMessageKey;
    }

    /** **************************** */
    public Object getBean() {
        return propertyComponentSupport.getBean();
    }

    public String getProperty() {
        return propertyComponentSupport.getProperty();
    }

    public Object getValue() {
        return propertyComponentSupport.getValue();
    }

    public void setBean(Object bean) {
        propertyComponentSupport.setBean(bean);
    }

    public void setProperty(String propertyPath) {
        propertyComponentSupport.setProperty(propertyPath);
    }

    public void setValue(Object value) {
        propertyComponentSupport.setValue(value);
    }

    public boolean isRequired() {
        return propertyComponentSupport.isRequired();
    }

    public void setRequired(boolean required) {
        propertyComponentSupport.setRequired(required);
    }

    protected void setValid(boolean valid) {
        propertyComponentSupport.setValid(valid);
    }

    public Class getBeanClass() {
        return propertyComponentSupport.getBeanClass();
    }

    public void setBeanClass(Class beanClass) {
        propertyComponentSupport.setBeanClass(beanClass);
    }

    public Object getBrokenPathObject() {
        return propertyComponentSupport.getBrokenPathObject();
    }

    public Object getNullObject() {
        return propertyComponentSupport.getNullObject();
    }

    public void setBrokenPathObject(Object brokenPathObject) {
        propertyComponentSupport.setBrokenPathObject(brokenPathObject);
    }

    public void setNullObject(Object nullObject) {
        propertyComponentSupport.setNullObject(nullObject);
    }

    public int getExactLength() {
        return exactLength;
    }

    public void setExactLength(int exactLength) {
        this.exactLength = exactLength;
    }

    public boolean isAutoTrim() {
        return autoTrim;
    }

    public void setAutoTrim(boolean autoTrim) {
        this.autoTrim = autoTrim;
    }

    public boolean isReadOnly() {
        return !isEditable();
    }

    public void setReadOnly(boolean readOnly) {
        setEditable(!readOnly);
    }

    public Color getBackground() {
        if (isReadOnly()) {
            return EchoSupport.COLOR_READONLY;
        } else if (isRequired()) {
            return EchoSupport.COLOR_REQUIRED;
        } else {
            return super.getBackground();
        }
    }

    public void preRender() {
    }
}
