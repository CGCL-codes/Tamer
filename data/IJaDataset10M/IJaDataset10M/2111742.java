package com.genia.toolbox.web.gwt.form.client.widget.item;

import java.util.List;
import com.genia.toolbox.web.gwt.form.client.manager.FormManager;
import com.genia.toolbox.web.gwt.form.client.value.FormValues;

/**
 * this interface represents the widget associated to an SimpleItem from a Form.
 * It allows to retrieve the current value of the item and to display the
 * validation errors to the user.
 */
public interface ComplexItemWidget extends ItemWidget {

    /**
   * returns the current
   * {@link com.genia.toolbox.web.gwt.form.client.manager.FormManager}s of this
   * item.
   * 
   * @return a {@link List} of
   *         {@link com.genia.toolbox.web.gwt.form.client.manager.FormManager}
   *         representing the current values of this item
   */
    public List<FormManager> getFormManagers();

    /**
   * returns the current values of this item.
   * 
   * @return a {@link List} of
   *         {@link com.genia.toolbox.web.gwt.form.client.value.FormValues}
   *         representing the current values of this item
   */
    public List<FormValues> getValues();

    /**
   * set the current values of this item.
   * 
   * @param values
   *          a {@link List} of
   *          {@link com.genia.toolbox.web.gwt.form.client.value.FormValues}
   *          representing the new values of this item
   */
    public void setValues(List<FormValues> values);
}
