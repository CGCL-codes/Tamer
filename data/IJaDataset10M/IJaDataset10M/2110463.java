package com.hack23.cia.model.impl.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.common.TypeContext;

/**
 * The Class EntityTestObject.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityTestObject extends BaseEntity {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2831588433419103923L;

    /** The id. */
    private Long id;

    /** The version. */
    private Long version = 1L;

    /**
	 * Instantiates a new entity test object.
	 */
    public EntityTestObject() {
    }

    @Override
    @Transient
    protected TypeContext getApplicationTypeContext() {
        return TypeContext.CommandCenter;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    @Override
    @Version
    public Long getVersion() {
        return version;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setVersion(final Long version) {
        this.version = version;
    }
}
