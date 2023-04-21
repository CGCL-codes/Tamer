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
 * business entity driver
 */
@Entity
@Table(name = "driver")
@Access(AccessType.FIELD)
public class Driver implements Serializable {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = 7042016299911516404L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String firstname;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastname;

    @ManyToOne
    @JoinColumn(name = "countryId", nullable = true)
    private Country country;

    private boolean player;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Driver)) return false;
        final Driver driver = (Driver) other;
        if (!driver.getFirstname().equals(getFirstname())) return false;
        if (!driver.getLastname().equals(getLastname())) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = getFirstname().hashCode();
        result = 42 * result + getLastname().hashCode();
        return result;
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
}
