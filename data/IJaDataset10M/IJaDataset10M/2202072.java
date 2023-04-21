package org.jenmo.common.multiarray;

/**
 * Interface for multidimensional arrays. Includes introspection by extending
 * {@link IMultiArrayInfo} and data access by extending {@link IAccessor}.
 * 
 * @author Nicolas Ocquidant (thanks to Bill Hibbard)
 * @since 1.0
 */
public interface IMultiArray extends IMultiArrayInfo, IAccessor {

    /**
    * @return a the original Array containing all the elements in this {@link IMultiArray}
    */
    Object getStorage();
}
