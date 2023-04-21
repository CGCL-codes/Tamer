package net.sf.jzeno.echo.databinding;

import net.sf.jzeno.echo.EchoSupport;
import net.sf.jzeno.util.ImageReferenceCache;
import nextapp.echo.Color;
import nextapp.echo.EchoConstants;
import nextapp.echo.Font;
import nextapp.echo.HttpImageReference;
import nextapp.echo.ImageReference;
import org.apache.log4j.Logger;
import echopoint.Label;

/**
 * @author ddhondt
 */
public class DynaLabel extends Label implements PropertyComponent {

    protected static Logger log = Logger.getLogger(DynaLabel.class);

    private static final long serialVersionUID = 1L;

    private PropertyComponentSupport propertyComponentSupport = null;

    private boolean transparency = true;

    private String nullLabel = "n/a";

    public DynaLabel() {
        this(null, null, null);
    }

    public DynaLabel(String label) {
        this(null, null, null);
        setText(label);
    }

    public DynaLabel(Class beanClass, String property, String constructionHints) {
        propertyComponentSupport = new PropertyComponentSupport(beanClass, property, this);
        setHorizontalAlignment(EchoConstants.LEFT);
        setFont(new Font("Arial", Font.PLAIN, 8));
        setRequired(false);
        getToolTipPopUp().setComplexRendering(false);
        EchoSupport.executeHints(this, constructionHints);
    }

    public void setIconName(String iconName) {
        if (iconName != null) {
            ImageReference ir = ImageReferenceCache.getHttpImageReference("images/" + iconName);
            setIcon(ir);
        } else {
            setIcon(null);
        }
    }

    public String getIconName() {
        String ret = "";
        ImageReference ir = getIcon();
        if (ir instanceof HttpImageReference) {
            HttpImageReference httpir = (HttpImageReference) ir;
            String uri = httpir.getUri();
            if (uri.startsWith("images/") && uri.length() > 7) {
                ret = uri.substring(7);
            }
        }
        return ret;
    }

    public void rebind() {
        Object value = propertyComponentSupport.getValue();
        if (value != null) {
            setText(value.toString());
        } else if (propertyComponentSupport.getProperty() != null) {
            setText(nullLabel);
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

    public void setDecorator(Decorator decoratorClassName) {
        propertyComponentSupport.setDecorator(decoratorClassName);
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

    public boolean getTransparency() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public Color getBackground() {
        Color ret = null;
        if (getTransparency() && getParent() != null) {
            ret = getParent().getBackground();
        } else {
            ret = super.getBackground();
        }
        return ret;
    }

    public String getNullLabel() {
        return nullLabel;
    }

    public void setNullLabel(String nullLabel) {
        this.nullLabel = nullLabel;
    }

    public boolean isReadOnly() {
        return true;
    }

    public void setReadOnly(boolean readOnly) {
    }

    public void preRender() {
    }
}
