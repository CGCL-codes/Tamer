package org.apache.isis.extensions.jpa.runtime.persistence.objectstore.load.oneToOneEagerJoin;

import java.text.MessageFormat;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("REF")
public class ReferencingObject {

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

    private ReferencedObjectA referencedObjectA;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public ReferencedObjectA getReferencedObjectA() {
        return referencedObjectA;
    }

    public void setReferencedObjectA(final ReferencedObjectA referencedObject) {
        this.referencedObjectA = referencedObject;
    }

    private ReferencedObjectB referencedObjectB;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    public ReferencedObjectB getReferencedObjectB() {
        return referencedObjectB;
    }

    public void setReferencedObjectB(final ReferencedObjectB referencedObject) {
        this.referencedObjectB = referencedObject;
    }

    @Override
    public String toString() {
        return MessageFormat.format("id: {0}  someString: {1}", id, description);
    }
}
