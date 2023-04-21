package org.eclipse.emf.edit.provider;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The base interface for a value wrapper that can implement item provider interfaces.
 */
public interface IWrapperItemProvider extends IDisposable {

    /**
   * Returns the wrapped value.
   */
    Object getValue();

    /**
   * Returns the object that owns the value.
   */
    Object getOwner();

    /**
   * Returns the structural feature through which the value can be set and retrieved, or null if the feature is
   * unknown or not applicable.
   */
    EStructuralFeature getFeature();

    /**
   * The index at which the value is located, or {@link org.eclipse.emf.edit.command.CommandParameter#NO_INDEX} if the
   * index isn't known to the wrapper. If {@link #getFeature} is non-null, this index is within that feature. 
   */
    int getIndex();

    /**
   * Sets the index. Has no effect if the index isn't known to the wrapper.
   */
    void setIndex(int index);
}
