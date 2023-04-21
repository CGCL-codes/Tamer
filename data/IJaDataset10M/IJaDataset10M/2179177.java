package org.jboss.tutorial.jndibinding.bean;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Customer
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
@Entity
public class Customer implements Serializable {

    private long id;

    private String name;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
