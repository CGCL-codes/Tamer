package net.sf.jpasecurity.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Raffaela Ferrari
 *
 */
@Entity
public class FieldAccessPerIDTestclass extends PropertyAccessTestclass {

    @Id
    @GeneratedValue
    @Basic(optional = false)
    @Column(nullable = false)
    protected Integer id;

    public Integer getId() {
        return id;
    }
}
