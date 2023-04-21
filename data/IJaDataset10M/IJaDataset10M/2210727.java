package proyecto.modelo;

import java.util.ArrayList;
import java.util.Collection;
import proyecto.modelo.enumeracion.CuotaEstado;

public class Cuota {

    private int idCuota, idPropiedad;

    private String moneda, fechaVencimiento, periodo;

    private double importe;

    private Vivienda vivienda;

    private Pago pago;

    private CuotaEstado estado;

    private Collection<Cuota> cuotas = new ArrayList<Cuota>();

    public Cuota() {
        super();
    }

    public Cuota(String periodo, String moneda, CuotaEstado estado, double importe, String fechaVencimiento, int idPropiedad) {
        super();
        this.periodo = periodo;
        this.estado = estado;
        this.importe = importe;
        this.fechaVencimiento = fechaVencimiento;
        this.moneda = moneda;
        this.estado = estado;
        this.idPropiedad = idPropiedad;
    }

    public Collection<Cuota> getCuotas() {
        return cuotas;
    }

    public void setCuotas(Collection<Cuota> cuotas) {
        this.cuotas = cuotas;
    }

    public int getIdPropiedad() {
        return idPropiedad;
    }

    public void setIdPropiedad(int idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public CuotaEstado getEstado() {
        return estado;
    }

    public void setEstado(CuotaEstado estado) {
        this.estado = estado;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Vivienda getVivienda() {
        return vivienda;
    }

    public void setVivienda(Vivienda vivienda) {
        this.vivienda = vivienda;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
