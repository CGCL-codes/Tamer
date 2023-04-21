package db.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author guybrush
 */
@Entity
@Table(name = "Examination", catalog = "Progetto2", schema = "")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Examination.findByPatId", query = "Select e From Examination e " + "Where e.id_pat = :id;"), @NamedQuery(name = "Examination.findByPatIdPerformed", query = "Select e From Examination e " + "Where e.id_pat = :id " + "AND e.effettuata = 1;"), @NamedQuery(name = "Examination.alreadyDoneByPat", query = "Select e From Examination e WHERE  e.scadenza < current_timestamp " + "AND e.id_pat = :id;"), @NamedQuery(name = "Examination.notYetDoneByPat", query = "Select e From Examination e WHERE  e.scadenza > current_timestamp " + "AND e.id_pat = :id;"), @NamedQuery(name = "Examination.findMaxId", query = "Select e From Examination e " + "Where e.id = (Select max(ex.id) From Examination ex);"), @NamedQuery(name = "Examination.findWaitingExamination", query = "Select e From Examination e,Medic m,Patient p " + "Where e.id_pat = p.id " + "AND p.id_med = m.id " + "AND e.effettuata = 0 " + "AND m.id = :id;"), @NamedQuery(name = "Examination.insideTimeLimit", query = "select ex  from Examination ex where ex.scadenza in (" + " select min(E.scadenza) from Examination E where E.id_pat = ex.id_pat and E.scadenza > current_timestamp and E.scadenza < :timeLimit )") })
public class Examination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "id_pat")
    private Integer id_pat;

    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;

    @Basic(optional = false)
    @Column(name = "scadenza")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scadenza;

    @Basic(optional = false)
    @Column(name = "tipo_scadenza")
    private String tipo_scadenza;

    @Basic(optional = false)
    @Column(name = "effettuata")
    private Boolean effettuata;

    @Basic(optional = true)
    @Column(name = "descrizione")
    private String descrizione;

    public Examination() {
    }

    public Examination(Integer id, Integer id_pat, String tipo, Date scadenza, String tipo_scadenza, Boolean effettuata, String descrizione) {
        this.id = id;
        this.id_pat = id_pat;
        this.tipo = tipo;
        this.scadenza = scadenza;
        this.tipo_scadenza = tipo_scadenza;
        this.effettuata = effettuata;
        this.descrizione = descrizione;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        if (descrizione != null) if (descrizione.contains("<br />")) {
            String tmp_r, tmp[] = descrizione.split("<br />");
            tmp_r = tmp[0];
            for (int x = 1; x < tmp.length; x++) tmp_r += " " + tmp[x];
            return tmp_r;
        }
        return descrizione;
    }

    public String getRealDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setRealDescrizione(String descrizione) {
        setDescrizione(descrizione);
    }

    public Boolean getEffettuata() {
        return effettuata;
    }

    public void setEffettuata(Boolean effettuata) {
        this.effettuata = effettuata;
    }

    public Integer getId_pat() {
        return id_pat;
    }

    public void setId_pat(Integer id_pat) {
        this.id_pat = id_pat;
    }

    public Date getScadenza() {
        return scadenza;
    }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo_scadenza() {
        return tipo_scadenza;
    }

    public void setTipo_scadenza(String tipo_scadenza) {
        this.tipo_scadenza = tipo_scadenza;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Examination other = (Examination) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.entities.Examination[ id=" + id + " ]";
    }
}
