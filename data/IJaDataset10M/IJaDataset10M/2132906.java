package radiadores.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import radiadores.persistencia.IPersistente;

/**
 * Contiene los datos de un componente de un nodo, puede ser una materia prima,
 * un producto, etc.
 *
 * @author Franco Catena, Mario Mariani, Lisandro Nieto, Sebastián Torres
 * @version 1.0
 */
@Entity
@Table(name = "partes_de_nodo")
public class ParteDeNodo implements Serializable, IPersistente {

    private static final long serialVersionUID = 1L;

    private static final List<String> CAMPOS_UNICOS = new ArrayList<String>(0);

    private String id;

    private Componente componente;

    private double cantidad;

    private NodoRuta nodoRuta;

    private boolean borrado;

    /**
     * Constructor
     */
    public ParteDeNodo() {
        setId(UUID.randomUUID().toString());
    }

    /**
     * Devuelve el identificador único de la clase
     * 
     * @return El identificador único de la clase
     */
    @Id
    @Column(name = "id", length = 36)
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único de la clase
     * 
     * @param id Nuevo valor del identificador único de la clase
     */
    public void setId(String id) {
        this.id = id;
    }

    @JoinColumn(name = "componente_id")
    @ManyToOne(targetEntity = Componente.class, cascade = CascadeType.ALL)
    public Componente getComponente() {
        return componente;
    }

    public void setComponente(Componente componente) {
        this.componente = componente;
    }

    @Column(name = "cantidad")
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @JoinColumn(name = "nodo_ruta_id")
    @ManyToOne(targetEntity = NodoRuta.class, cascade = CascadeType.ALL)
    public NodoRuta getNodoRuta() {
        return nodoRuta;
    }

    public void setNodoRuta(NodoRuta nodoRuta) {
        this.nodoRuta = nodoRuta;
    }

    @Column(name = "borrado")
    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    public List<String> getCamposUnicos() {
        return CAMPOS_UNICOS;
    }
}
