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
import javax.persistence.Transient;
import radiadores.persistencia.IPersistente;

/**
 * Contiene la información de las horas de trabajo por centro
 * 
 * @author Franco Catena, Mario Mariani, Lisandro Nieto, Sebastián Torres
 * @version 1.0
 */
@Entity
@Table(name = "horas_laborales")
public class HoraLaboral implements Serializable, IPersistente {

    private static final long serialVersionUID = 1L;

    private static final List<String> CAMPOS_UNICOS = new ArrayList<String>(0);

    private String id;

    private int cantidad;

    private Empleado empleado;

    private NodoRuta nodoRuta;

    private CentroDeTrabajo centroDeTrabajo;

    private boolean borrado;

    /**
     * Constructor
     */
    public HoraLaboral() {
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

    @Column(name = "cantidad")
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @JoinColumn(name = "empleado_id")
    @ManyToOne(targetEntity = Empleado.class, cascade = CascadeType.ALL)
    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @JoinColumn(name = "centro_de_trabajo_id")
    @ManyToOne(targetEntity = CentroDeTrabajo.class, cascade = CascadeType.ALL)
    public CentroDeTrabajo getCentroDeTrabajo() {
        return centroDeTrabajo;
    }

    public void setCentroDeTrabajo(CentroDeTrabajo centroDeTrabajo) {
        this.centroDeTrabajo = centroDeTrabajo;
    }

    @Column(name = "borrado")
    public boolean isBorrado() {
        return borrado;
    }

    public void setBorrado(boolean borrado) {
        this.borrado = borrado;
    }

    @Override
    @Transient
    public List<String> getCamposUnicos() {
        return CAMPOS_UNICOS;
    }

    @JoinColumn(name = "nodo_ruta_id")
    @ManyToOne(targetEntity = NodoRuta.class, cascade = CascadeType.ALL)
    public NodoRuta getNodoRuta() {
        return nodoRuta;
    }

    public void setNodoRuta(NodoRuta nodoRuta) {
        this.nodoRuta = nodoRuta;
    }
}
