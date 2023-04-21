package openadmin.model.domicili;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Length;
import openadmin.dao.Base;

@Entity
@Table(name = "compteBancari", schema = "domicili", uniqueConstraints = @UniqueConstraint(columnNames = { "description" }))
public class CompteBancari implements Base, Comparable, java.io.Serializable {

    /** attribute that contain the identifier*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Number id;

    @Length(min = 0, max = 30)
    @NotNull
    private String description;

    @JoinColumn(name = "sucursal", nullable = false)
    @ManyToOne
    @NotNull
    private Sucursal sucursal;

    @NotNull
    private int dc;

    @NotNull
    private int compte;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataInici;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataFi;

    @NotNull
    private int estat;

    @Transient
    private boolean debuglog = true;

    @Transient
    private boolean historiclog = false;

    public CompteBancari() {
    }

    public void setId(Number id) {
        this.id = id;
    }

    public Number getId() {
        return this.id;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Sucursal getSucursalt() {
        return this.sucursal;
    }

    public void setDC(int dc) {
        this.dc = dc;
    }

    public int getDC() {
        return this.dc;
    }

    public void setCompte(int compte) {
        this.compte = compte;
    }

    public int getCompte() {
        return this.compte;
    }

    public void setDataInici(Date dataInici) {
        this.dataInici = dataInici;
    }

    public Date getDataInici() {
        return this.dataInici;
    }

    public void setDataFi(Date dataFi) {
        this.dataFi = dataFi;
    }

    public Date getDataFi() {
        return this.dataFi;
    }

    public void setEstat(int estat) {
        this.estat = estat;
    }

    public int getEstat() {
        return this.estat;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean getHistoricLog() {
        return historiclog;
    }

    public boolean getDebugLog() {
        return debuglog;
    }

    public int compareTo(Object o) {
        Entitat entitat = (Entitat) o;
        return description.compareTo(entitat.getDescription());
    }

    @Override
    public String toString() {
        return description;
    }
}
