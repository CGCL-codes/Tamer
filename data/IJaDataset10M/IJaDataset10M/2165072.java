package com.antilia.web.field.factory;

import java.io.Serializable;
import org.apache.wicket.Component;
import com.antilia.web.field.IFieldModel;
import com.antilia.web.field.impl.TextAreaField;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class TextAreaFieldFactory<B extends Serializable> implements IFieldFactory<B> {

    @SuppressWarnings("unchecked")
    private static TextAreaFieldFactory instance;

    private TextAreaFieldFactory() {
    }

    public boolean canHandleField(IFieldModel<B> model, FieldMode mode) {
        if (String.class.isAssignableFrom(model.getFieldClass()) && model.getLength() >= 200 && mode.equals(FieldMode.EDIT)) return true;
        return false;
    }

    public Component newField(String id, IFieldModel<B> fieldModel, FieldMode mode) {
        return new TextAreaField<B>(id, fieldModel, mode);
    }

    @SuppressWarnings("unchecked")
    public static TextAreaFieldFactory getInstance() {
        if (instance == null) instance = new TextAreaFieldFactory();
        return instance;
    }
}
