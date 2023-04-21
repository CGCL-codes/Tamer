package com.cosmos.swingb;

import com.cosmos.beansbinding.EntityProperty;
import com.cosmos.swingb.binding.EntityBinder;
import com.cosmos.swingb.validation.Validatable;
import com.cosmos.util.BooleanUtils;
import javax.swing.JCheckBox;
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

/**
 *
 * @author Miro
 */
public class JBCheckBox extends JCheckBox implements Validatable, EntityBinder {

    private Application application;

    private ApplicationContext applicationContext;

    private ApplicationActionMap applicationActionMap;

    private ResourceMap resourceMap;

    private String propertyName;

    private Object beanEntity;

    private Binding binding;

    @Override
    public Binding bind(BindingGroup bindingGroup, Object beanEntity, EntityProperty propertyDetails) {
        return bind(bindingGroup, beanEntity, propertyDetails, AutoBinding.UpdateStrategy.READ_WRITE);
    }

    @Override
    public Binding bind(BindingGroup bindingGroup, Object beanEntity, EntityProperty propertyDetails, AutoBinding.UpdateStrategy updateStrategy) {
        if (propertyDetails == null || propertyDetails.isHiden()) {
            setEnabled(false);
            return null;
        }
        bind(bindingGroup, beanEntity, propertyDetails.getPropertyName(), updateStrategy);
        setEnabled(!propertyDetails.isReadOnly());
        return binding;
    }

    public Binding bind(BindingGroup bindingGroup, Object beanEntity, String propertyName) {
        return bind(bindingGroup, beanEntity, propertyName, AutoBinding.UpdateStrategy.READ_WRITE);
    }

    public Binding bind(BindingGroup bindingGroup, Object beanEntity, String propertyName, AutoBinding.UpdateStrategy updateStrategy) {
        this.propertyName = propertyName;
        this.beanEntity = beanEntity;
        ELProperty elProperty = ELProperty.create("${" + propertyName + "}");
        BeanProperty beanProperty = BeanProperty.create("selected");
        binding = Bindings.createAutoBinding(updateStrategy, beanEntity, elProperty, this, beanProperty);
        bindingGroup.addBinding(binding);
        return binding;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public Object getBeanEntity() {
        return beanEntity;
    }

    @Override
    public Binding getBinding() {
        return binding;
    }

    protected boolean getPropertyValue() {
        return getPropertyValue(beanEntity);
    }

    protected boolean getPropertyValue(Object beanEntity) {
        try {
            Object propertyValue;
            if ((propertyValue = PropertyUtils.getProperty(beanEntity, propertyName)) != null) {
                if (propertyValue instanceof Boolean) {
                    return (Boolean) propertyValue;
                }
                return BooleanUtils.parseBoolean(propertyName);
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("beanEntity=" + beanEntity + ", propertyName=" + propertyName, ex);
        }
    }

    public Application getApplication() {
        if (application == null) {
            application = Application.getInstance();
        }
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
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

    @Override
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

    @Override
    public void setStyleRequired(String tooltip) {
        setToolTipText(tooltip);
        setBackground(getResourceMap().getColor("validation.field.required.background"));
    }

    @Override
    public void setStyleInvalid(String tooltip) {
        setToolTipText(tooltip);
        setBackground(getResourceMap().getColor("validation.field.invalid.background"));
    }

    @Override
    public void setStyleValid() {
        setToolTipText(null);
        setBackground(getResourceMap().getColor("validation.field.valid.background"));
    }

    @Override
    public void setStyleNormal() {
        setToolTipText(null);
        setBackground(getResourceMap().getColor("validation.field.normal.background"));
    }

    @Override
    public Task refresh() {
        RefreshTask task = new RefreshTask();
        task.run();
        return task;
    }

    @Override
    public void clear() {
        setSelected(false);
    }

    private class RefreshTask extends Task<Object, Void> {

        public RefreshTask() {
            super(Application.getInstance());
        }

        @Override
        protected Object doInBackground() throws Exception {
            boolean value = getPropertyValue();
            setSelected(value);
            return value;
        }
    }
}
