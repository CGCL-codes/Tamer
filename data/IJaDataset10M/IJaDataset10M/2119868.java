package openadmin.model.territory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import openadmin.dao.Base;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "country", schema = "territory", uniqueConstraints = @UniqueConstraint(columnNames = "description"))
public class Country implements Base, java.io.Serializable {

    /** attribute that contains the identifier*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** attribute that contains the description, unique value*/
    @Length(max = 40)
    @NotNull
    private String description;

    /** Transient attribute that means that the system should make a log on any JPA operation of this class*/
    @Transient
    private boolean debuglog = true;

    /** Transient attribute that means that the system should make a historic on any JPA operation of this class*/
    @Transient
    boolean historiclog = false;

    /**
	 * Constructor of the class Country.
	 * without parameters
	 */
    public Country() {
    }

    /**
	 * Constructor of the class Country.
	 * @param pDescription, is the description, (unique value), of the Country
	 */
    public Country(String pDescription) {
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

    /**
	public Set<Province> getProvince() {
		return this.province;
	}

	public void setProvince(Set<Province> pProvince) {
		this.province = pProvince;
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
        return description;
    }
}
