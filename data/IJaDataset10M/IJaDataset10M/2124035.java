package org.starobjects.jpa.runtime.persistence.objectstore.execute;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.nakedobjects.applib.annotation.Optional;

@Entity
@DiscriminatorValue("SIA")
public class SimpleAutoAssignedObject {

    private Long id;

    @Id
    @Optional
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    private String someString;

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(final String someString) {
        this.someString = someString;
    }
}
