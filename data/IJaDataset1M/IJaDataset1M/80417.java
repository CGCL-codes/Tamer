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
@Table(name = "reto")
@NamedQueries({ @NamedQuery(name = "Reto.findAll", query = "SELECT r FROM Reto r") })
public class Reto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    @Basic(optional = false)
    @Column(name = "dificultad")
    private short dificultad;

    @Column(name = "record")
    private Integer record;

    @Column(name = "limite")
    private Integer limite;

    @Basic(optional = false)
    @Column(name = "resuelto")
    private int resuelto;

    @Basic(optional = false)
    @Column(name = "jugado")
    private int jugado;

    @Basic(optional = false)
    @Column(name = "numSitios")
    private int numSitios;

    @Basic(optional = false)
    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReto")
    private List<Sitio> sitioList;

    @JoinColumn(name = "creador", referencedColumnName = "login")
    @ManyToOne(optional = false)
    private Jugador creador;

    @JoinColumn(name = "recordman", referencedColumnName = "login")
    @ManyToOne
    private Jugador recordman;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reto")
    private List<Partida> partidaList;

    public Reto() {
    }

    public Reto(Integer id) {
        this.id = id;
    }

    public Reto(Integer id, String titulo, short dificultad, int resuelto, int jugado, int numSitios, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.dificultad = dificultad;
        this.resuelto = resuelto;
        this.jugado = jugado;
        this.numSitios = numSitios;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public short getDificultad() {
        return dificultad;
    }

    public void setDificultad(short dificultad) {
        this.dificultad = dificultad;
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public int getResuelto() {
        return resuelto;
    }

    public void setResuelto(int resuelto) {
        this.resuelto = resuelto;
    }

    public int getJugado() {
        return jugado;
    }

    public void setJugado(int jugado) {
        this.jugado = jugado;
    }

    public int getNumSitios() {
        return numSitios;
    }

    public void setNumSitios(int numSitios) {
        this.numSitios = numSitios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Sitio> getSitioList() {
        return sitioList;
    }

    public void setSitioList(List<Sitio> sitioList) {
        this.sitioList = sitioList;
    }

    public Jugador getCreador() {
        return creador;
    }

    public void setCreador(Jugador creador) {
        this.creador = creador;
    }

    public Jugador getRecordman() {
        return recordman;
    }

    public void setRecordman(Jugador recordman) {
        this.recordman = recordman;
    }

    public List<Partida> getPartidaList() {
        return partidaList;
    }

    public void setPartidaList(List<Partida> partidaList) {
        this.partidaList = partidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reto)) {
            return false;
        }
        Reto other = (Reto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tw.modelo.Reto[id=" + id + "]";
    }
}
