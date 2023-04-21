package net.derquinse.common.orm.hib;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import net.derquinse.common.orm.AbstractEntity;
import net.derquinse.common.orm.LongEntity;

/**
 * Abstract superclass for model entities with an long primary key.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class MappedLongEntity extends AbstractEntity<Long> implements LongEntity {

    /** Entity ID. */
    @Id
    @Column(name = "META_ID", nullable = false)
    private Long id;

    /** Default constructor. */
    public MappedLongEntity() {
    }

    /**
	 * Constructor.
	 * @param id Entity ID.
	 */
    public MappedLongEntity(Long id) {
        this.id = id;
    }

    /**
	 * Returns the entity id.
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the entity id.
	 */
    public void setId(Long id) {
        this.id = id;
    }
}
