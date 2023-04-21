package com.tysanclan.site.projectewok.entities.twitter;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import com.jeroensteenbeeke.hyperion.data.DomainObject;
import com.tysanclan.site.projectewok.beans.ActionResolver;

/**
 * @author Jeroen Steenbeeke
 */
@Entity
@AccessType("field")
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.TRANSACTIONAL, region = "main")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TwitterAction implements DomainObject {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TwitterAction")
    @SequenceGenerator(name = "TwitterAction", sequenceName = "SEQ_ID_TwitterAction")
    private Long id;

    @Column
    private Date queueTime;

    /**
	 * Creates a new TwitterAction object
	 */
    public TwitterAction() {
    }

    /**
	 * Returns the ID of this TwitterAction
	 */
    @Override
    public Long getId() {
        return this.id;
    }

    /**
	 * Sets the ID of this TwitterAction
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the queueTime
	 */
    public Date getQueueTime() {
        return queueTime;
    }

    /**
	 * @param queueTime
	 *            the queueTime to set
	 */
    public void setQueueTime(Date queueTime) {
        this.queueTime = queueTime;
    }

    public abstract Class<? extends ActionResolver<? extends TwitterAction>> getResolverType();
}
