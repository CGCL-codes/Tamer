package uy.gub.imm.sae.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "ae_valores_del_dato")
public class ValorPosible implements Serializable {

    private static final long serialVersionUID = -5197426783029830293L;

    private Integer id;

    private String etiqueta;

    private String valor;

    private Integer orden;

    private Date fechaDesde;

    private Date fechaHasta;

    private DatoASolicitar dato;

    public ValorPosible() {
    }

    /**
	 * Constructor por copia no en profundidad.
	 */
    public ValorPosible(ValorPosible v) {
        id = v.getId();
        etiqueta = v.getEtiqueta();
        valor = v.getValor();
        orden = v.getOrden();
        fechaDesde = v.getFechaDesde();
        fechaHasta = v.getFechaHasta();
        dato = null;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_valorposibleid")
    @SequenceGenerator(name = "seq_valorposibleid", initialValue = 1, sequenceName = "s_ae_valorposible", allocationSize = 1)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(nullable = false, length = 50)
    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Column(nullable = false, length = 50)
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Column(nullable = false)
    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Column(name = "fecha_desde", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFechaDesde() {
        return fechaDesde;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    @XmlTransient
    @ManyToOne(optional = false)
    @JoinColumn(name = "aeds_id", nullable = false)
    public DatoASolicitar getDato() {
        return dato;
    }

    public void setDato(DatoASolicitar dato) {
        this.dato = dato;
    }
}
