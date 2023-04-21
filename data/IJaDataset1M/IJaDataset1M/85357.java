package org.jpox.samples.annotations.abstractclasses;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Concrete subclass of abstract class, with single identity field.
 * 
 * @version $Revision: 1.1 $
 */
@Entity
@Table(name = "JPA_AN_CONCRETESIMP_SUB2")
public class ConcreteSimpleSub2 extends AbstractSimpleBase {

    @Basic
    @Column(name = "SUB2_FIELD")
    private String sub2Field;

    public ConcreteSimpleSub2(int id) {
        super(id);
    }

    public String getSub2Field() {
        return sub2Field;
    }

    public void setSub2Field(String fld) {
        this.sub2Field = fld;
    }
}
