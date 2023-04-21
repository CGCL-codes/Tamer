package org.digitall.apps.sueldos.classes;

import java.sql.ResultSet;
import org.digitall.lib.sql.LibSQL;

public class ConceptosLegajos {

    private int idconceptolegajo = -1;

    private int idlegajo = -1;

    private int idconcepto = -1;

    private double valor = 0.0;

    private String estado = "";

    private int cantidad = -1;

    private boolean issetforlegajo = false;

    public ConceptosLegajos() {
    }

    public int saveData() {
        String params = idconceptolegajo + "," + idlegajo + "," + idconcepto + "," + valor + ",'" + estado + "'," + cantidad + "," + issetforlegajo;
        int result = -1;
        if (idconceptolegajo == -1) {
            result = LibSQL.getInt("sueldos.addConceptoLegajo", params);
        } else {
            result = LibSQL.getInt("sueldos.setConceptoLegajo", params);
        }
        return result;
    }

    public void retrieveData() {
        String params = "" + idconceptolegajo;
        ResultSet data = LibSQL.exFunction("sueldos.getConceptoLegajo", params);
        try {
            if (data.next()) {
                idconceptolegajo = data.getInt("idconceptolegajo");
                idlegajo = data.getInt("idlegajo");
                idconcepto = data.getInt("idconcepto");
                valor = data.getDouble("valor");
                estado = data.getString("estado");
                cantidad = data.getInt("cantidad");
                issetforlegajo = data.getBoolean("issetforlegajo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setIdconceptolegajo(int idconceptolegajo) {
        this.idconceptolegajo = idconceptolegajo;
    }

    public int getIdconceptolegajo() {
        return idconceptolegajo;
    }

    public void setIdlegajo(int idlegajo) {
        this.idlegajo = idlegajo;
    }

    public int getIdlegajo() {
        return idlegajo;
    }

    public void setIdconcepto(int idconcepto) {
        this.idconcepto = idconcepto;
    }

    public int getIdconcepto() {
        return idconcepto;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setIssetforlegajo(boolean issetforlegajo) {
        this.issetforlegajo = issetforlegajo;
    }

    public boolean isIssetforlegajo() {
        return issetforlegajo;
    }
}
