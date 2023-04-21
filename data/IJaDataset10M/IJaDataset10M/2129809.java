package net.entelijan.cobean.bind.impl;

import net.entelijan.cobean.bind.AbstractModelChangeListener;
import net.entelijan.cobean.bind.AbstractPropertyBinding;
import net.entelijan.cobean.bind.IBeanProperty;
import net.entelijan.cobean.bind.IBinding;
import net.entelijan.cobean.bind.IColumn;
import net.entelijan.cobean.bind.IColumnBinding;
import net.entelijan.cobean.bind.IModelChangeListener;
import net.entelijan.cobean.bind.IRemovableFactory;
import net.entelijan.cobean.core.impl.IPropertyChangeListenerAware;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class NestedBinding<C> extends AbstractPropertyBinding<C> implements IColumnBinding<C> {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(NestedBinding.class);

    private IBinding<C> innerBinding;

    private String name;

    public IModelChangeListener createModelChangeListener(IBeanProperty beanProperty, C comp, IRemovableFactory removableFactory) {
        return new ModelChangeListener(beanProperty, comp, removableFactory);
    }

    public <T> T bind(T model, C comp) {
        T re = super.bind(model, comp);
        IBeanProperty modelProp = this.propertyBindingStrategy.createBeanProperty(model, this.modelProperty);
        Object modelPropertyValue = modelProp.getValue();
        modelPropertyValue = this.propertyBindingStrategy.bind(modelPropertyValue, this.innerBinding, comp);
        modelProp.setValue(modelPropertyValue);
        return re;
    }

    @Override
    public String toString() {
        return "NestedBinding [name=" + name + " innerBinding=" + innerBinding + "]";
    }

    public IBinding<C> getInnerBinding() {
        return this.innerBinding;
    }

    public void setInnerBinding(IBinding<C> innerBinding) {
        this.innerBinding = innerBinding;
    }

    private class ModelChangeListener extends AbstractModelChangeListener<Object, C> {

        public ModelChangeListener(IBeanProperty beanProperty, C component, IRemovableFactory removableFactory) {
            super(beanProperty, component, removableFactory);
        }

        @Override
        public void prepareComponent(Object oldVal, Object newVal) {
            if (!(this.component instanceof IPropertyChangeListenerAware)) {
                throw new IllegalStateException("Finding of components that do not implement the IPropertyChangeListenerAware interface is currently not implemented");
            }
            log.debug("[ModelChangeListener#prepareComponent] oldVal='" + oldVal + "' newVal='" + newVal + "'");
            if (this.component instanceof IPropertyChangeListenerAware) {
                final String nam = getName();
                log.debug("[ModelChangeListener#prepareComponent] remove property change listener name='" + nam + "' from " + this.component);
                ((IPropertyChangeListenerAware) this.component).removePropertyChangeListener(nam);
            } else {
                throw new IllegalStateException("Cannot prepare component as it does not implement 'IPropertyChangeListenerAware'");
            }
        }

        public void setComponentValue(Object value) {
            if (value != null) {
                Object wVal = NestedBinding.this.propertyBindingStrategy.bind(value, NestedBinding.this.innerBinding, this.component);
                this.beanProperty.setValue(wVal);
            } else {
                Object placeholder = this.beanProperty.createPlaceholder();
                log.debug("[setComponentValue] created placeholder " + placeholder);
                NestedBinding.this.propertyBindingStrategy.bind(placeholder, NestedBinding.this.innerBinding, this.component);
            }
        }

        @Override
        public String toString() {
            return "ModelChangeListener [beanProperty=" + beanProperty + ", component=" + component + "]";
        }
    }

    public IColumn createColumn() {
        return this.propertyBindingStrategy.createColumn(this);
    }

    public String getName() {
        if (this.name == null) {
            return this.modelProperty;
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
