package es.eucm.eadventure.common.data;

/**
 * The object has an ID
 */
public interface HasId {

    /**
     * Get the id of the object
     * 
     * @return The objects id
     */
    public String getId();

    /**
     * Set the id of the object
     * 
     * @param id
     *            The new id
     */
    public void setId(String id);
}
