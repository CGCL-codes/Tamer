package daw.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "objetotipo", catalog = "defensor", schema = "")
@NamedQueries({ @NamedQuery(name = "Objetotipo.findAll", query = "SELECT o FROM Objetotipo o"), @NamedQuery(name = "Objetotipo.findByIdObjetoTipo", query = "SELECT o FROM Objetotipo o WHERE o.idObjetoTipo = :idObjetoTipo"), @NamedQuery(name = "Objetotipo.findByNombre", query = "SELECT o FROM Objetotipo o WHERE o.nombre = :nombre") })
public class Objetotipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idObjetoTipo")
    private Integer idObjetoTipo;

    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "idObjetoTipo")
    private List<Objeto> objetoCollection;

    public Objetotipo() {
    }

    public Objetotipo(Integer idObjetoTipo) {
        this.idObjetoTipo = idObjetoTipo;
    }

    public Objetotipo(Integer idObjetoTipo, String nombre) {
        this.idObjetoTipo = idObjetoTipo;
        this.nombre = nombre;
    }

    public Integer getIdObjetoTipo() {
        return idObjetoTipo;
    }

    public void setIdObjetoTipo(Integer idObjetoTipo) {
        this.idObjetoTipo = idObjetoTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Objeto> getObjetoCollection() {
        return objetoCollection;
    }

    public void setObjetoCollection(List<Objeto> objetoCollection) {
        this.objetoCollection = objetoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObjetoTipo != null ? idObjetoTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Objetotipo)) {
            return false;
        }
        Objetotipo other = (Objetotipo) object;
        if ((this.idObjetoTipo == null && other.idObjetoTipo != null) || (this.idObjetoTipo != null && !this.idObjetoTipo.equals(other.idObjetoTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "daw.bean.Objetotipo[idObjetoTipo=" + idObjetoTipo + "]";
    }

    public String toXML() {
        String xml = "<objetotipo>";
        xml += "<idobjetotipo>" + getIdObjetoTipo() + "</idobjetotipo>";
        xml += "<nombre>" + getNombre() + "</nombre>";
        return xml;
    }

    public static String ObjetoTipoCollectionToXML(List<Objetotipo> objetostipo) {
        String xml = "<objetostipo>";
        Iterator i = objetostipo.iterator();
        while (i.hasNext()) {
            Objetotipo c = (Objetotipo) i.next();
            xml += c.toXML();
        }
        xml += "</objetostipo>";
        return xml;
    }
}
