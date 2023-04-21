package com.phloc.json;

import javax.annotation.Nonnull;

/**
 * Interface for a JSON property value. The internal data type can be specified
 * via the template parameter
 * 
 * @author Boris Gregorcic
 * @param <DATATYPE>
 *        the internal data type of the value
 */
public interface IJSONPropertyValue<DATATYPE> extends IJSON {

    /**
   * @return the internal data value
   */
    DATATYPE getData();

    /**
   * Sets the passed data value
   * 
   * @param aValue
   *        the value to set
   */
    void setData(DATATYPE aValue);

    @Nonnull
    IJSONPropertyValue<DATATYPE> getClone();
}
