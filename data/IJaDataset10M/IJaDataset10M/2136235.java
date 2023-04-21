package com.gwtext.client.widgets.form.event;

import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.form.ComboBox;

/**
 *
 * @author Sanjiv Jivan
 */
public interface ComboBoxListener extends FieldListener {

    /**
     * Fires before all queries are processed. Return false to cancel the query.
     *
     * @param comboBox this
     * @param cb       callback
     * @return false to cancel the query
     */
    boolean doBeforeQuery(ComboBox comboBox, ComboBoxCallback cb);

    /**
     * Fires before a list item is selected. Return false to cancel the selection.
     *
     * @param comboBox this
     * @param record   the data record returned from the underlying store
     * @param index    the data record returned from the underlying store
     * @return false to cancel
     */
    boolean doBeforeSelect(ComboBox comboBox, Record record, int index);

    /**
     * Fires when the dropdown list is collapsed.
     *
     * @param comboBox this
     */
    void onCollapse(ComboBox comboBox);

    /**
     * Fires when the dropdown list is expanded.
     *
     * @param comboBox this
     */
    void onExpand(ComboBox comboBox);

    /**
     * Fires when a list item is selected.
     *
     * @param comboBox this
     * @param record   the data record returned from the underlying store
     * @param index    the data record returned from the underlying store
     */
    void onSelect(ComboBox comboBox, Record record, int index);
}
