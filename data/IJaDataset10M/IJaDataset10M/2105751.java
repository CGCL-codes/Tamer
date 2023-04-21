package se.entitymanager.logic;

/**
 * Exception which is thrown to indicate, that a renaming of an entity failed.
 * 
 * @uml.stereotype name="tagged" isDefined="true" 
 */
public class EntityRenameFailedException extends Exception {

    /**
     * The entity whose renaming failed.
     * @see #getEntity()
     * 
     * @uml.property name="entity"
     * @uml.associationEnd 
     * @uml.property name="entity" multiplicity="(1 1)"
     */
    private EntityInterface entity;

    /**
     * The reason for why renaming failed.
     * @see #getReason()
     * 
     * @uml.property name="reason" 
     */
    private String reason;

    /**
	 * Constructs an exception to indicate , that renaming of an entity failed.
	 * @param entity the entity
	 * @param reason the reason why renaming failed
	 */
    public EntityRenameFailedException(EntityInterface entity, String reason) {
        this.entity = entity;
        this.reason = reason;
    }

    /**
     * Returns the entity whose renaming failed.
     * @return the entity
     * 
     * @uml.property name="entity"
     */
    public EntityInterface getEntity() {
        return entity;
    }

    /**
     * Returns the reason why renaming failed.
     * @return the reason
     * 
     * @uml.property name="reason"
     */
    public String getReason() {
        return reason;
    }
}
