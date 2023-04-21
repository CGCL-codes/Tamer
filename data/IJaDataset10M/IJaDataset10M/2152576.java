package org.nexopenframework.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nexopenframework.persistence.entity.BaseEntityImpl;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:magdaleno@mystrands.com">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "example")
public class CEntity extends BaseEntityImpl {

    private static final long serialVersionUID = 1L;

    private String value;

    private String description;

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return super.getId();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }
}
