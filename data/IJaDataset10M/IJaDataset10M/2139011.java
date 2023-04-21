package jtelmob;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author sefit
 */
@Entity
@Table(name = "utilizatori", schema = "public")
@NamedQueries({ @NamedQuery(name = "Utilizatori.findByNumarTelefon", query = "SELECT u FROM Utilizatori u WHERE u.numarTelefon = :numarTelefon"), @NamedQuery(name = "Utilizatori.findByNumePrenume", query = "SELECT u FROM Utilizatori u WHERE u.numePrenume = :numePrenume"), @NamedQuery(name = "Utilizatori.findByFunctie", query = "SELECT u FROM Utilizatori u WHERE u.functie = :functie"), @NamedQuery(name = "Utilizatori.findByZona", query = "SELECT u FROM Utilizatori u WHERE u.zona = :zona"), @NamedQuery(name = "Utilizatori.findByLocalitate", query = "SELECT u FROM Utilizatori u WHERE u.localitate = :localitate"), @NamedQuery(name = "Utilizatori.findByDeductibil", query = "SELECT u FROM Utilizatori u WHERE u.deductibil = :deductibil"), @NamedQuery(name = "Utilizatori.findByStampila", query = "SELECT u FROM Utilizatori u WHERE u.stampila = :stampila"), @NamedQuery(name = "Utilizatori.findByAnulat", query = "SELECT u FROM Utilizatori u WHERE u.anulat = :anulat"), @NamedQuery(name = "Utilizatori.findByZonaId", query = "SELECT u FROM Utilizatori u WHERE u.zonaId = :zonaId"), @NamedQuery(name = "Utilizatori.findByZonaN", query = "SELECT u FROM Utilizatori u WHERE u.zonaN = :zonaN") })
public class Utilizatori implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "numar_telefon", nullable = false)
    private Long numarTelefon;

    @Column(name = "nume_prenume")
    private String numePrenume;

    @JoinColumn(name = "functie", referencedColumnName = "function_name")
    @ManyToOne
    private Nfunctions functie;

    @Column(name = "zona", nullable = false)
    private String zona;

    @Column(name = "localitate")
    private String localitate;

    @Column(name = "deductibil")
    private Double deductibil;

    @Column(name = "stampila")
    @Temporal(TemporalType.TIMESTAMP)
    private Date stampila;

    @Column(name = "anulat")
    private Boolean anulat;

    @Column(name = "zona_id", nullable = false)
    private BigDecimal zonaId;

    @Column(name = "zona_n", nullable = false)
    private String zonaN;

    public Utilizatori() {
    }

    public Utilizatori(Long numarTelefon) {
        this.numarTelefon = numarTelefon;
    }

    public Utilizatori(Long numarTelefon, String zona, BigDecimal zonaId, String zonaN) {
        this.numarTelefon = numarTelefon;
        this.zona = zona;
        this.zonaId = zonaId;
        this.zonaN = zonaN;
    }

    public Long getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(Long numarTelefon) {
        Long oldNumarTelefon = this.numarTelefon;
        this.numarTelefon = numarTelefon;
        changeSupport.firePropertyChange("numarTelefon", oldNumarTelefon, numarTelefon);
    }

    public String getNumePrenume() {
        return numePrenume;
    }

    public void setNumePrenume(String numePrenume) {
        String oldNumePrenume = this.numePrenume;
        this.numePrenume = numePrenume;
        changeSupport.firePropertyChange("numePrenume", oldNumePrenume, numePrenume);
    }

    public Nfunctions getFunctie() {
        return functie;
    }

    public void setFunctie(Nfunctions functie) {
        Nfunctions oldFunctie = this.functie;
        this.functie = functie;
        changeSupport.firePropertyChange("functie", oldFunctie, functie);
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        String oldZona = this.zona;
        this.zona = zona;
        changeSupport.firePropertyChange("zona", oldZona, zona);
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        String oldLocalitate = this.localitate;
        this.localitate = localitate;
        changeSupport.firePropertyChange("localitate", oldLocalitate, localitate);
    }

    public Double getDeductibil() {
        return deductibil;
    }

    public void setDeductibil(Double deductibil) {
        Double oldDeductibil = this.deductibil;
        this.deductibil = deductibil;
        changeSupport.firePropertyChange("deductibil", oldDeductibil, deductibil);
    }

    public Date getStampila() {
        return stampila;
    }

    public void setStampila(Date stampila) {
        Date oldStampila = this.stampila;
        this.stampila = stampila;
        changeSupport.firePropertyChange("stampila", oldStampila, stampila);
    }

    public Boolean getAnulat() {
        return anulat;
    }

    public void setAnulat(Boolean anulat) {
        Boolean oldAnulat = this.anulat;
        this.anulat = anulat;
        changeSupport.firePropertyChange("anulat", oldAnulat, anulat);
    }

    public BigDecimal getZonaId() {
        return zonaId;
    }

    public void setZonaId(BigDecimal zonaId) {
        BigDecimal oldZonaId = this.zonaId;
        this.zonaId = zonaId;
        changeSupport.firePropertyChange("zonaId", oldZonaId, zonaId);
    }

    public String getZonaN() {
        return zonaN;
    }

    public void setZonaN(String zonaN) {
        String oldZonaN = this.zonaN;
        this.zonaN = zonaN;
        changeSupport.firePropertyChange("zonaN", oldZonaN, zonaN);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numarTelefon != null ? numarTelefon.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Utilizatori)) {
            return false;
        }
        Utilizatori other = (Utilizatori) object;
        if ((this.numarTelefon == null && other.numarTelefon != null) || (this.numarTelefon != null && !this.numarTelefon.equals(other.numarTelefon))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jtelmob.Utilizatori[numarTelefon=" + numarTelefon + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
