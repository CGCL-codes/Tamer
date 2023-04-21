package at.racemgr.be;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * business entity country
 */
@Entity
@Table(name = "country")
@Access(AccessType.FIELD)
public class Country implements Serializable {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 1733210840567058623L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 2, max = 2)
    private String isocode;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the isocode
     */
    public String getIsocode() {
        return isocode;
    }

    /**
     * @param isocode
     *            the isocode to set
     */
    public void setIsocode(String isocode) {
        this.isocode = isocode;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Country)) return false;
        final Country country = (Country) other;
        if (!country.getIsocode().equals(getIsocode())) return false;
        if (!country.getName().equals(getName())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = getIsocode().hashCode();
        result = 42 * result + getName().hashCode();
        return result;
    }
}
