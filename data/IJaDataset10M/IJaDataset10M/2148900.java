package com.gustozzi.domain;

/**
 * @author FABRIZIO
 * 
 */
public class DocumentoTributario {

    private String numero;

    private TipoDocumento tipoDocumento;

    private String fechaEmision;

    private String fechaCancelacion;

    private Cliente cliente;

    private double montoTotal;

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @return the tipoDocumento
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @return the fechaEmision
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * @return the fechaCancelacion
     */
    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @return the montoTotal
     */
    public double getMontoTotal() {
        return montoTotal;
    }

    /**
     * @param numero
     *            the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param fechaEmision
     *            the fechaEmision to set
     */
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /**
     * @param fechaCancelacion
     *            the fechaCancelacion to set
     */
    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    /**
     * @param cliente
     *            the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @param montoTotal
     *            the montoTotal to set
     */
    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}
