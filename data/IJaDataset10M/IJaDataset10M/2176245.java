package org.openmi.standard2.timespace;

import org.openmi.standard2.IBaseExchangeItem;

/**
 * A time / space dependent item that can be exchanged, either as input or as output.
 */
public interface ITimeSpaceExchangeItem extends IBaseExchangeItem {

    /**
     * Time information on the values that are available in an output exchange
     * item, or required by an input exchange item.
     *
     * @return ITimeSet containing the time information
     */
    public ITimeSet getTimeSet();

    /**
     * Spatial information (usually an element set) on the values that are available
     * in an output exchange item, or required by an input exchange item.
     *
     * @return ISpatialDefinition containing the information
     */
    public ISpatialDefinition getSpatialDefinition();
}
