package com.softaspects.jsf.component.base;

import com.softaspects.jsf.component.base.BeanPreprocessor;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ManagedBeanPreprocessor extends BeanPreprocessor {

    public Object preprocessBean(String beanName) {
        if (UIComponentTag.isValueReference(beanName)) {
            ValueBinding vb = FacesContext.getCurrentInstance().getApplication().createValueBinding(beanName);
            return vb.getValue(FacesContext.getCurrentInstance());
        }
        return null;
    }
}
