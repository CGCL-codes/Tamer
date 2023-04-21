package descriptors;

/**
 * This interface embodies the property of being able
 * to provide a description of an object.
 * 
 * @author Lars Samuelsson
 */
public interface Descriptor {

    /**
     * This method is used for checking whether this is 
     * a descriptor for the specified type.
     *
     * @param type The type that we need to check if
     *             it is handled or not
     * @return     true if the type is handled
     */
    public boolean describes(String type);

    /**
     * For fetching the description of this Descriptor.
     *
     * @return A nice description 
     */
    public String describe(Object obj);
}
