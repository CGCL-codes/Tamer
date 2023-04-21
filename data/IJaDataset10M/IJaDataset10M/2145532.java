package com.phloc.types.datatype;

import javax.annotation.Nonnull;

/**
 * Interface for a complex data type that has one nested type (e.g. list or
 * set). For associative arrays please see {@link IMapDataType}.
 * 
 * @author philip
 */
public interface ICollectionDataType extends IDataType {

    /**
   * @return <code>true</code> if this is a list
   */
    boolean isList();

    /**
   * @return <code>true</code> if this is a set
   */
    boolean isSet();

    /**
   * @return The nested data type of this collection data type.
   */
    @Nonnull
    IDataType getNestedDataType();
}
