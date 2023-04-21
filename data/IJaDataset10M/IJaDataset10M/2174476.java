package tw.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author alex952
 */
@Entity
@Table(name = "sitio")
@NamedQueries({ @NamedQuery(name = "Sitio.findAll", query = "SELECT s FROM Sitio s") })
public class Sitio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "lat")
    private double lat;

    @Basic(optional = false)
    @Column(name = "lng")
    private double lng;

    @Basic(optional = false)
    @Column(name = "esUltimo")
    private boolean esUltimo;

    @Basic(optional = false)
    @Column(name = "orden")
    private int orden;

    @Basic(optional = false)
    @Column(name = "tamMapa")
    private int tamMapa;

    @Column(name = "imagen")
    private String imagen;

    @Basic(optional = false)
    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @JoinColumn(name = "idReto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Reto idReto;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSitio")
    private List<Pista> pistaList;

    public Sitio() {
    }

    public Sitio(Integer id) {
        this.id = id;
    }

    public Sitio(Integer id, double lat, double lng, boolean esUltimo, int orden, int tamMapa, String descripcion) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.esUltimo = esUltimo;
        this.orden = orden;
        this.tamMapa = tamMapa;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean getEsUltimo() {
        return esUltimo;
    }

    public void setEsUltimo(boolean esUltimo) {
        this.esUltimo = esUltimo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getTamMapa() {
        return tamMapa;
    }

    public void setTamMapa(int tamMapa) {
        this.tamMapa = tamMapa;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Reto getIdReto() {
        return idReto;
    }

    public void setIdReto(Reto idReto) {
        this.idReto = idReto;
    }

    public List<Pista> getPistaList() {
        return pistaList;
    }

    public void setPistaList(List<Pista> pistaList) {
        this.pistaList = pistaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Sitio)) {
            return false;
        }
        Sitio other = (Sitio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tw.modelo.Sitio[id=" + id + "]";
    }
}
