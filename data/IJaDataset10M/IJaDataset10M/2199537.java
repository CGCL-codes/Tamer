package org.etherpipe.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

@Entity
public abstract class MetaData implements Serializable {

    private long id;

    @Id
    @GeneratedValue
    protected long getId() {
        return id;
    }

    protected void setId(long id) {
        this.id = id;
    }
}
