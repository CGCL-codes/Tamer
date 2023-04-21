package openadmin.model.territory;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import openadmin.dao.Base;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "province", schema = "territory", uniqueConstraints = { @UniqueConstraint(columnNames = "codeProvince"), @UniqueConstraint(columnNames = "description") })
public class Province implements Base, java.io.Serializable {

    /** attribute that contains the identifier*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** attribute that contains the description, unique value*/
    @NotNull
    private Integer codeprovince;

    /** attribute that contains the name province, unique value*/
    @Length(max = 25)
    @NotNull
    private String description;

    /** Transient attribute that means that the system should make a log on any JPA operation of this class*/
    @Transient
    private boolean debuglog = true;

    /** Transient attribute that means that the system should make a historic on any JPA operation of this class*/
    @Transient
    boolean historiclog = false;

    /** attribute that contains the relationship with Country*/
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    /**
	 * Constructor of the class Province.
	 * without parameters
	 */
    public Province() {
    }

    /**
	 * Constructor of the class Province.
	 * @param pDescription, is the description, (unique value), of the Province
	 */
    public Province(String pDescription) {
        this.description = pDescription.toUpperCase();
    }

    /** Getters and setters*/
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String pDescription) {
        this.description = pDescription.toUpperCase();
    }

    public Integer getCodeProvince() {
        return codeprovince;
    }

    public void setCodeProvince(Integer codeProvince) {
        this.codeprovince = codeProvince;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    /**
	public Set<Municipality> getMunicipality() {
		return municipality;
	}

	public void setMunicipality(Set<Municipality> pMunicipality) {
		this.municipality = pMunicipality;
	}*/
    public boolean getDebugLog() {
        return debuglog;
    }

    public void setDebugLog(boolean pDebuglog) {
        debuglog = pDebuglog;
    }

    public boolean getHistoricLog() {
        return historiclog;
    }

    public void setHistoricLog(boolean historiclog) {
        this.historiclog = historiclog;
    }

    @Override
    public String toString() {
        return codeprovince + " " + description;
    }
}
