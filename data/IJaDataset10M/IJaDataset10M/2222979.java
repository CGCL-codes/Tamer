package net.sf.jzeno.echo.databinding;

import net.sf.jzeno.echo.EchoSupport;
import org.apache.log4j.Logger;
import echopoint.HtmlContainer;

/**
 * @author ddhondt
 * 
 * HtmlContainer subclass that supports databinding.
 */
public class DynaHtmlContainer extends HtmlContainer implements PropertyComponent {

    protected static Logger log = Logger.getLogger(DynaHtmlContainer.class);

    private static final long serialVersionUID = 1L;

    private PropertyComponentSupport propertyComponentSupport = null;

    public DynaHtmlContainer() {
        this(null, null, null);
    }

    public DynaHtmlContainer(Class beanClass, String property, String constructionHints) {
        propertyComponentSupport = new PropertyComponentSupport(beanClass, property, this);
        setRequired(false);
        EchoSupport.executeHints(this, constructionHints);
    }

    public void rebind() {
        String html = "";
        Object value = propertyComponentSupport.getValue();
        if (value != null && value instanceof String) {
            html = (String) value;
            setTemplate(html);
            firePropertyChange(null, null, null);
        }
    }

    public void doValidation() {
    }

    public boolean isValid() {
        return true;
    }

    /** ******************** */
    public Object getBean() {
        return propertyComponentSupport.getBean();
    }

    public String getProperty() {
        return propertyComponentSupport.getProperty();
    }

    public Object getValue() {
        return propertyComponentSupport.getValue();
    }

    public boolean isRequired() {
        return propertyComponentSupport.isRequired();
    }

    public void setBean(Object bean) {
        propertyComponentSupport.setBean(bean);
    }

    public void setProperty(String propertyPath) {
        propertyComponentSupport.setProperty(propertyPath);
    }

    public void setRequired(boolean required) {
        propertyComponentSupport.setRequired(required);
    }

    public void setValue(Object value) {
        propertyComponentSupport.setValue(value);
    }

    public void setDecorator(Decorator decorator) {
        propertyComponentSupport.setDecorator(decorator);
    }

    public Decorator getDecorator() {
        return propertyComponentSupport.getDecorator();
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

    public boolean isReadOnly() {
        return true;
    }

    public void setReadOnly(boolean readOnly) {
    }

    public void preRender() {
    }
}
