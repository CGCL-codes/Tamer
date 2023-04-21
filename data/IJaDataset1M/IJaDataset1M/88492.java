package at.racemgr.be;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * business entity track
 */
@Entity
@Table(name = "track")
@Access(AccessType.FIELD)
public class Track implements Serializable {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 5155944371933399368L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "countryId", nullable = true)
    private Country country;

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
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Country)) return false;
        final Track track = (Track) other;
        if (!track.getName().equals(getName())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = getName().hashCode();
        return result;
    }
}
