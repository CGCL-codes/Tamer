package dsb.bar.tks.server.persistence.model;

import java.io.Serializable;

/**
 * An entity which JPA/Hibernate treats as optimistically versioned.
 * 
 * @param ID
 *            The primary key identifier type.
 * @param V
 *            The versioning type.
 * 
 * @see Java Persistence with Hibernate, p. 325 and 464
 */
public interface VersionedEntity<ID extends Serializable, V extends Serializable> extends Serializable {

    /**
	 * Get the database primary key identifier for this entity.
	 * 
	 * @return The PK of this entity.
	 * 
	 * @see Java Persistence with Hibernate, p. 325
	 */
    public ID getId();

    /**
	 * Set the database primary key for this entity.
	 * 
	 * @param id
	 *            The PK of this entity.
	 * 
	 * @see Java Persistence with Hibernate, p. 325
	 */
    public void setId(ID id);

    /**
	 * Get the version number of this entity.
	 * 
	 * @return The version number of this entity.
	 * 
	 * @see Java Persistence with Hibernate, p. 464.
	 */
    public V getVersion();

    /**
	 * Set the version number of this entity.
	 * 
	 * @param version
	 *            The version number of this entity.
	 * 
	 * @see Java Persistence with Hibernate, p. 464.
	 */
    public void setVersion(V version);
}
