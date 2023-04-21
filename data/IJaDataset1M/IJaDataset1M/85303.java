package com.gwtent.client.uibinder;

import java.util.Set;
import javax.validation.ConstraintViolation;

/**
 * 
 * @author JamesLuo@gmail.com
 *
 * @param <T> the editor type
 * @param <D> the data type which editor supposed
 */
public interface UIBinder<T, D> {

    public void binder(T widget, ModelValue<D> value, boolean autoValidate, Class<?>... validateGroups);

    public Set<ConstraintViolation<Object>> validate(boolean showMessagesToUI, Class<?>... validateGroups);

    public boolean isAutoValidate();

    public T getWidget();

    public void hideValidateMessageBox();
}
