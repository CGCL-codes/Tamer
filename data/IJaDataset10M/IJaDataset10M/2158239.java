package org.jcvi.vics.web.gwt.common.client.ui;

/**
 * Same as SelectionListener, but can return multiple values.  The number and meaning of the values returned
 * is specified by the class accepting the SelectionListener and calling onSelect(String[] values).
 *
 * @author Michael Press
 */
public interface MultiValueSelectionListener {

    public void onSelect(String[] values);

    public void onUnSelect(String[] values);
}
