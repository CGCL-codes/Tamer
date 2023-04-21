package edu.upc.entidades;

public class Egresos {

    private String gasto;

    private double importe;

    private String periodo;

    private String fechaPago;

    public Egresos(String egr_gasto, double egr_imp, String cut_periodo, String cut_fechaPago) {
        gasto = egr_gasto;
        importe = egr_imp;
        periodo = cut_periodo;
        fechaPago = cut_fechaPago;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getGasto() {
        return gasto;
    }

    public void setGasto(String gasto) {
        this.gasto = gasto;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}
