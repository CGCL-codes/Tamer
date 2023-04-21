package org.starobjects.jpa.runtime.persistence.objectstore.load.manyToOneEager;

import java.text.MessageFormat;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import org.nakedobjects.applib.annotation.Optional;

@DiscriminatorValue("ROB")
@Entity
public class ReferencedObjectB {

    private Long id;

    @Optional
    @Id
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    private Long version;

    @Optional
    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(final Long version) {
        this.version = version;
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
