package org.nakedobjects.viewer.swing.view;

/**
 * Interface to set some values on the different fields
 * Not needed anymore since views now offer their field component.
 * @author  Johan C. Stover
 * @deprecated
 */
public interface NakedField {

    public void setLabelSize(int width);

    public int getLabelSize();
}
