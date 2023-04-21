package org.starobjects.jpa.runtime.persistence.objectstore.load.anyLazy;

import java.text.MessageFormat;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

@DiscriminatorValue("ROA")
@Entity
public class ReferencedObjectA implements ISomeInterfaceA {

    private Long id;

    @Id
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return MessageFormat.format("id: {0}  someString: {1}", id, description);
    }
}
