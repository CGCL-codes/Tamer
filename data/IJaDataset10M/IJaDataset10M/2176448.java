package com.antilia.web.field;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import com.antilia.common.util.AnnotationUtils;

/**
 * 
 * A configurator that uses Hibernate-JPA annotations to 
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class HibernateJpaAutoFieldConfigurator<B extends Serializable> implements IAutoFieldConfigurator<B> {

    private static final long serialVersionUID = 1L;

    public void configureFieldModel(IFieldModel<B> model) {
        try {
            Column column = AnnotationUtils.findFieldAnnotation(model.getBeanClass(), model.getPropertyPath(), Column.class);
            if (column != null) {
                model.setLength(column.length());
                model.setRequiered(!column.nullable());
            } else {
                JoinColumn joinColumn = AnnotationUtils.findFieldAnnotation(model.getBeanClass(), model.getPropertyPath(), JoinColumn.class);
                if (joinColumn != null) {
                    model.setRequiered(!joinColumn.nullable());
                }
            }
        } catch (Exception e) {
        }
    }
}
