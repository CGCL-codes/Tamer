package com.phloc.commons.xml.schema;

import javax.annotation.Nonnull;
import javax.xml.validation.Schema;

/**
 * A simple interface, indicating that an item has a Schema object.
 * 
 * @author philip
 */
public interface IHasSchema {

    /**
   * @return The non-<code>null</code> Schema object
   */
    @Nonnull
    Schema getSchema();
}
