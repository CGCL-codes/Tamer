package net.sourceforge.iwii.db.dev.persistence.entities.dictionary.phase6;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Transient;
import net.sourceforge.iwii.db.dev.persistence.entities.IEntity;

/**
 * Class represents entity mapped to database relation 'DI_REAL_WORLD_OBJECTS'.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
@Entity
@Table(name = "DI_REAL_WORLD_OBJECTS")
@NamedQueries({  })
public class RealWorldObjectEntity implements IEntity<Long> {

    @Transient
    private boolean initialized = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBJECT_ID", nullable = false)
    private Long id;

    @Column(name = "OBJECT_NAME", length = 100, nullable = false)
    private String name;

    public RealWorldObjectEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void initialize() {
        if (!this.initialized) {
            this.initialized = true;
        }
    }

    @Override
    public void resetInitialization() {
        this.initialized = false;
    }

    @Override
    public String toString() {
        return "entity://data-table/" + this.getClass().getName() + "[id=" + this.id + "]";
    }
}
