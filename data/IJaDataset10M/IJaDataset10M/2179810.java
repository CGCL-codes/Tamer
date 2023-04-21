package com.tysanclan.site.projectewok.entities;

import javax.persistence.*;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Index;
import com.jeroensteenbeeke.hyperion.data.DomainObject;

/**
 *
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
public class MobileUserAgent implements DomainObject {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MobileUserAgent")
    @SequenceGenerator(name = "MobileUserAgent", sequenceName = "SEQ_ID_MobileUserAgent")
    private Long id;

    @Column(nullable = false, length = 255)
    @Index(name = "IDX_MUA_IDENT")
    private String identifier;

    @Column(nullable = true)
    @Index(name = "IDX_MUA_MOBILE")
    private Boolean mobile;

    /**
	 * Creates a new MobileUserAgent object
	 */
    public MobileUserAgent() {
    }

    /**
	 * Returns the ID of this MobileUserAgent
	 */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
	 * Sets the ID of this MobileUserAgent
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return The Identifier of this MobileUserAgent
	 */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
	 * Sets the Identifier of this MobileUserAgent
	 * @param identifier The Identifier of this MobileUserAgent
	 */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Boolean getMobile() {
        return mobile;
    }

    public void setMobile(Boolean mobile) {
        this.mobile = mobile;
    }
}
