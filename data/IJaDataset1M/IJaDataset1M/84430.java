package org.openmi.alterra.standard;

/**
 * Parameterized IResource version of the generic IValueSetV2.
 */
public interface IResourceSet extends IValueSetV2<IResource> {

    /**
     * Gets the IResource at the specified index.
     *
     * @param index The index to get the IResource for
     * @return IResource The IResource from the collection
     */
    public IResource getResource(int index);
}
