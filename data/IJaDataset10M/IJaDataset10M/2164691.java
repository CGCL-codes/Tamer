package org.shopformat.domain.order.sales.tax;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.shopformat.domain.shared.DomainObject;

/**
 * Each product falls under a particular tax class e.g. kids clothing / books. 
 * When combined with a tax zone the tax rate can be calculated.
 * 
 * @author Stephen Vangasse
 * @version $Id$
 */
@Entity
public class TaxClass implements DomainObject<Long> {

    private Long id;

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
