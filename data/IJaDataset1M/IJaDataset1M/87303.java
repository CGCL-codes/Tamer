package org.broadleafcommerce.profile.core.domain;

import org.broadleafcommerce.presentation.AdminPresentation;
import org.broadleafcommerce.presentation.AdminPresentationClass;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_COUNTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "baseCountry")
public class CountryImpl implements Country {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ABBREVIATION")
    protected String abbreviation;

    @Column(name = "NAME", nullable = false)
    @AdminPresentation(friendlyName = "Country", order = 12, group = "Address")
    protected String name;

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String Abbreviation) {
        this.abbreviation = Abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CountryImpl other = (CountryImpl) obj;
        if (abbreviation == null) {
            if (other.abbreviation != null) return false;
        } else if (!abbreviation.equals(other.abbreviation)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abbreviation == null) ? 0 : abbreviation.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
}
