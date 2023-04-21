package org.stuarthardy.momentum.domain.types;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * momentum-domain
 * 
 * @author Stuart Hardy
 * 
 */
@Entity
public class Region extends AbstractType {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Country> countries;

    /**
	 * Zero Argument Constructor for Hibernate
	 */
    protected Region() {
        super();
    }

    /**
	 * @param code The Types Code
	 * @param description the Types Description
	 */
    public Region(String code, String description) {
        super(code, description);
    }

    /**
     * @return the countries property
     */
    public Set<Country> getCountries() {
        return this.countries;
    }

    /**
     * @param countries the countries property
     */
    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }
}
