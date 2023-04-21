package org.jpox.samples.annotations.many_many;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Oil Supplier.
 *
 * @version $Revision: 1.1 $    
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "JPA_AN_OILSUPP")
public class OilSupplier extends PetroleumSupplier {

    protected String brandName = null;

    public OilSupplier(long id, String name) {
        super(id, name);
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String toString() {
        return super.toString() + " [brand name : " + brandName + "]";
    }
}
