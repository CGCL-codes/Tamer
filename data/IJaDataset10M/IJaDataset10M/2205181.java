package Entidades;

public class Recepcion {

    private int numeroRecepcion;

    private java.util.Date fechaHoraRecepcion;

    private Operario persona;

    private Contrato contrato;

    private Transporte transporte;

    private Remito remito;

    private Transportista transportista;

    private Cargaempaque cargaempaque;

    public Recepcion() {
    }

    public Recepcion(int numeroRecepcion) {
        this.numeroRecepcion = numeroRecepcion;
    }

    public Recepcion(java.util.Date fechaHoraRecepcion, Operario persona, Contrato contrato, Transporte transporte, Remito remito, Transportista transportista, Cargaempaque cargaempaque) {
        this.fechaHoraRecepcion = fechaHoraRecepcion;
        this.persona = persona;
        this.contrato = contrato;
        this.transporte = transporte;
        this.remito = remito;
        this.transportista = transportista;
        this.cargaempaque = cargaempaque;
    }

    public int getNumeroRecepcion() {
        return numeroRecepcion;
    }

    public void setNumeroRecepcion(int numeroRecepcion) {
        this.numeroRecepcion = numeroRecepcion;
    }

    public java.util.Date getFechaHoraRecepcion() {
        return fechaHoraRecepcion;
    }

    public void setFechaHoraRecepcion(java.util.Date fechaHoraRecepcion) {
        this.fechaHoraRecepcion = fechaHoraRecepcion;
    }

    public Operario getPersona() {
        return persona;
    }

    public void setPersona(Operario persona) {
        this.persona = persona;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Transporte getTransporte() {
        return transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public Remito getRemito() {
        return remito;
    }

    public void setRemito(Remito remito) {
        this.remito = remito;
    }

    public Transportista getTransportista() {
        return transportista;
    }

    public void setTransportista(Transportista transportista) {
        this.transportista = transportista;
    }

    public Cargaempaque getCargaempaque() {
        return cargaempaque;
    }

    public void setCargaempaque(Cargaempaque cargaempaque) {
        this.cargaempaque = cargaempaque;
    }
}
