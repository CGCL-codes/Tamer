package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.update.oneToManyBidir;

import java.text.MessageFormat;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("ROB")
public class ReferencedObjectB {

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

    private ReferencingObject referencingObject;

    @OneToOne(fetch = FetchType.LAZY)
    public ReferencingObject getReferencingObject() {
        return referencingObject;
    }

    public void setReferencingObject(final ReferencingObject referencingObject) {
        this.referencingObject = referencingObject;
    }

    public void associateReferencingObject(final ReferencingObject referencingObject) {
        if (referencingObject == null || getReferencingObject() == referencingObject) {
            return;
        }
        dissociateReferencingObject(null);
        referencingObject.addToReferencedObjectBs(this);
    }

    public void dissociateReferencingObject(final ReferencingObject referencingObject) {
        if (getReferencingObject() == null) {
            return;
        }
        getReferencingObject().removeFromReferencedObjectBs(this);
    }

    @Override
    public String toString() {
        return MessageFormat.format("id: {0}  someString: {1}", id, description);
    }
}
