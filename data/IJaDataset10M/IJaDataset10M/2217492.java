package org.gestionabierta.dominio.modelo.presupuesto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Franky Villadiego
 * @author Valdemar Sotillo
 * 
 */
@Entity
@Table(name = "auxiliares")
public class Auxiliar implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @SequenceGenerator(name = "secuencia", sequenceName = "asignacion_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    private int id;

    @ManyToOne
    private Asignacion asignacion;

    private String codigo;

    private String nombre;

    private boolean obra;

    private String origen;

    private BigDecimal monto;

    private BigDecimal disponible;

    private BigDecimal compromiso;

    private BigDecimal causado;

    private BigDecimal pagado;

    private BigDecimal credito;

    private BigDecimal traspaso;

    private BigDecimal traspasod;

    private BigDecimal insubsistencia;

    private BigDecimal disminucion;

    public Auxiliar() {
    }

    public Auxiliar(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public Asignacion getAsignacion() {
        return asignacion;
    }

    public void setAsignacion(Asignacion asignacion) {
        this.asignacion = asignacion;
    }

    public BigDecimal getCausado() {
        return causado;
    }

    public void setCausado(BigDecimal causado) {
        this.causado = causado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(BigDecimal compromiso) {
        this.compromiso = compromiso;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }

    public BigDecimal getDisminucion() {
        return disminucion;
    }

    public void setDisminucion(BigDecimal disminucion) {
        this.disminucion = disminucion;
    }

    public BigDecimal getDisponible() {
        return disponible;
    }

    public void setDisponible(BigDecimal disponible) {
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getInsubsistencia() {
        return insubsistencia;
    }

    public void setInsubsistencia(BigDecimal insubsistencia) {
        this.insubsistencia = insubsistencia;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isObra() {
        return obra;
    }

    public void setObra(boolean obra) {
        this.obra = obra;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public BigDecimal getPagado() {
        return pagado;
    }

    public void setPagado(BigDecimal pagado) {
        this.pagado = pagado;
    }

    public BigDecimal getTraspaso() {
        return traspaso;
    }

    public void setTraspaso(BigDecimal traspaso) {
        this.traspaso = traspaso;
    }

    public BigDecimal getTraspasod() {
        return traspasod;
    }

    public void setTraspasod(BigDecimal traspasod) {
        this.traspasod = traspasod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Auxiliar other = (Auxiliar) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return codigo;
    }
}
