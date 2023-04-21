package com.antilia.web.field;

import java.io.Serializable;
import org.apache.wicket.model.Model;
import com.antilia.common.util.CommonUtils;
import com.antilia.web.filter.Operator;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class FieldModel<B extends Serializable> extends Model implements IFieldModel<B> {

    private static final long serialVersionUID = 1L;

    private Class<? extends B> beanClass;

    private Operator[] operators;

    private String propertyPath;

    private Operator selectedOperator;

    private Class<?> fieldClass;

    /**
	 * 
	 */
    public FieldModel(Class<? extends B> beanClass, String propertyPath) {
        super();
        if (beanClass == null) throw new IllegalArgumentException("Bean class cannot be null");
        this.beanClass = beanClass;
        if (CommonUtils.Strings.isEmpty(propertyPath)) throw new IllegalArgumentException("PropertyPath class cannot be null");
        this.propertyPath = propertyPath;
    }

    public Class<? extends B> getBeanClass() {
        return beanClass;
    }

    public Operator[] getOperators() {
        return operators;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public Operator getSelectedOperator() {
        return selectedOperator;
    }

    public void setOperators(Operator[] operators) {
        this.operators = operators;
    }

    public void setSelectedOperator(Operator operator) {
        this.selectedOperator = operator;
    }

    public void setBeanClass(Class<B> beanClass) {
        this.beanClass = beanClass;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public Class<?> getFieldClass() {
        if (fieldClass == null) {
            fieldClass = CommonUtils.Reflection.getPropertyClass(getBeanClass(), getPropertyPath());
        }
        return fieldClass;
    }
}
