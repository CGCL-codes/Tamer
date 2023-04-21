package com.cosmos.swingb;

import com.cosmos.beansbinding.EntityProperty;
import com.cosmos.resource.BeanResource;
import com.cosmos.resource.EnumResource;
import com.cosmos.resource.TextResource;
import com.cosmos.swingb.binding.EntityBinder;
import com.cosmos.swingb.validation.Validatable;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JPasswordField;
import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.Validator;

/**
 *
 * @author Bozhidar Bozhanov
 */
public class JBPasswordField extends JPasswordField implements Validatable, EntityBinder {

    private Application application;

    private ApplicationContext applicationContext;

    private ApplicationActionMap applicationActionMap;

    private ResourceMap resourceMap;

    private Binding binding;

    private String propertyName;

    private Object beanEntity;

    private BeanResource beanResource;

    private boolean maskable;

    private int dummyCharsAdded = 0;

    private static final char DUMMY_CHAR = 'a';

    private static final int SHOW_CHARS = 32;

    @Override
    public void setText(String password) {
        super.setText(maskPassword(password));
    }

    @Override
    public char[] getPassword() {
        return unmaskPassword(super.getPassword());
    }

    @Override
    public void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
        dummyCharsAdded = 0;
    }

    public void setMaskable(boolean maskable) {
        this.maskable = maskable;
    }

    public boolean isMaskable() {
        return maskable;
    }

    private String maskPassword(String password) {
        if (password == null || password.length() == 0) {
            dummyCharsAdded = 0;
            return password;
        }
        String maskedPassword = new String(password);
        dummyCharsAdded = 32 - password.length();
        for (int i = 0; i < dummyCharsAdded; i++) {
            maskedPassword += DUMMY_CHAR;
        }
        return maskedPassword;
    }

    private char[] unmaskPassword(char[] maskedPassword) {
        if (dummyCharsAdded == 0) return maskedPassword;
        char[] password = Arrays.copyOf(maskedPassword, SHOW_CHARS - dummyCharsAdded);
        return password;
    }

    public Binding bind(BindingGroup bindingGroup, Object beanEntity, EntityProperty propertyDetails) {
        return bind(bindingGroup, beanEntity, propertyDetails, AutoBinding.UpdateStrategy.READ_WRITE);
    }

    public Binding bind(BindingGroup bindingGroup, Object beanEntity, EntityProperty propertyDetails, AutoBinding.UpdateStrategy updateStrategy) {
        if (propertyDetails == null || propertyDetails.isHiden()) {
            setEditable(false);
            setEnabled(false);
            return null;
        }
        bind(bindingGroup, beanEntity, propertyDetails.getPropertyName(), updateStrategy);
        setEditable(propertyDetails.isEditable());
        setEnabled(!propertyDetails.isReadOnly());
        Validator validator = propertyDetails.getValidator();
        if (validator != null) {
            binding.setValidator(validator);
        }
        binding.addBindingListener(new BindingValidationListener(this));
        return binding;
    }

    private Binding bind(BindingGroup bindingGroup, Object beanEntity, String propertyName, AutoBinding.UpdateStrategy updateStrategy) {
        this.propertyName = propertyName;
        this.beanEntity = beanEntity;
        ELProperty elProperty = ELProperty.create("${" + propertyName + "}");
        BeanProperty beanProperty = BeanProperty.create("text");
        binding = Bindings.createAutoBinding(updateStrategy, beanEntity, elProperty, this, beanProperty);
        bindingGroup.addBinding(binding);
        return binding;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getBeanEntity() {
        return beanEntity;
    }

    public ApplicationContext getContext() {
        if (applicationContext == null) {
            Application app = getApplication();
            if (app != null) {
                applicationContext = app.getContext();
            }
        }
        return applicationContext;
    }

    public ApplicationActionMap getApplicationActionMap() {
        if (applicationActionMap == null) {
            ApplicationContext context = getContext();
            if (context != null) {
                applicationActionMap = context.getActionMap(this);
            }
        }
        return applicationActionMap;
    }

    public ResourceMap getResourceMap() {
        if (resourceMap == null) {
            ApplicationContext context = getContext();
            if (context != null) {
                resourceMap = context.getResourceMap(this.getClass());
            }
        }
        return resourceMap;
    }

    public void setResourceMap(ResourceMap resourceMap) {
        this.resourceMap = resourceMap;
    }

    public Application getApplication() {
        if (application == null) application = Application.getInstance();
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setStyleRequired(String tooltip) {
        setToolTipText(tooltip);
        setBackground(getResourceMap().getColor("validation.field.required.background"));
    }

    public void setStyleInvalid(String tooltip) {
        setToolTipText(tooltip);
        setBackground(getResourceMap().getColor("validation.field.invalid.background"));
    }

    public void setStyleValid() {
        setToolTipText(null);
        setBackground(getResourceMap().getColor("validation.field.valid.background"));
    }

    public void setStyleNormal() {
        setToolTipText(null);
        setBackground(getResourceMap().getColor("validation.field.normal.background"));
    }

    protected BeanResource getBeanResource() {
        if (beanResource == null) {
            beanResource = new BeanResource(getApplication());
        }
        return beanResource;
    }

    protected String getPropertyValue() {
        return getPropertyValue(beanEntity);
    }

    protected String getPropertyValue(Object beanEntity) {
        try {
            Object propertyValue;
            if ((propertyValue = PropertyUtils.getProperty(beanEntity, propertyName)) != null) {
                if (propertyValue instanceof EnumResource) {
                    return getBeanResource().getName((EnumResource) propertyValue);
                } else if (propertyValue instanceof TextResource) {
                    return getBeanResource().getName((TextResource) propertyValue);
                } else {
                    return String.valueOf(propertyValue);
                }
            }
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("beanEntity=" + beanEntity + ", propertyName=" + propertyName, ex);
        }
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    @Override
    public Task refresh() {
        RefreshTask task = new RefreshTask();
        task.run();
        return task;
    }

    @Override
    public void clear() {
        setText(null);
    }

    private class RefreshTask extends Task<Object, Void> {

        public RefreshTask() {
            super(Application.getInstance());
        }

        @Override
        protected Object doInBackground() throws Exception {
            String value = getPropertyValue();
            setText(value);
            return value;
        }
    }
}
