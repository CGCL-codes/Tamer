package org.digitall.apps.taxes092.classes;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.digitall.lib.sql.LibSQL;

public class Bonificacion {

    private int idBonificacion = 0;

    private String nombre = "";

    private String descripcion = "";

    private Double porcentaje = 0.0;

    private int idCuentaBonificacion = -1;

    private int idTipoImpuesto = -1;

    public Bonificacion() {
    }

    public void setIdBonificacion(int idBonificacion) {
        this.idBonificacion = idBonificacion;
    }

    public int getIdBonificacion() {
        return idBonificacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setIdCuentaBonificacion(int idCuentaBonificacion) {
        this.idCuentaBonificacion = idCuentaBonificacion;
    }

    public int getIdCuentaBonificacion() {
        return idCuentaBonificacion;
    }

    public void setIdTipoImpuesto(int idTipoImpuesto) {
        this.idTipoImpuesto = idTipoImpuesto;
    }

    public int getIdTipoImpuesto() {
        return idTipoImpuesto;
    }

    public void retrieveData() {
        ResultSet result = LibSQL.exFunction("taxes.getBonificacion", idBonificacion);
        try {
            if (result.next()) {
                setIdBonificacion(result.getInt("idbonificacion"));
                setNombre(result.getString("nombre"));
                setDescripcion(result.getString("descripcion"));
                setPorcentaje(result.getDouble("porcentaje"));
                setIdCuentaBonificacion(result.getInt("idcuentabonificacion"));
                setIdTipoImpuesto(result.getInt("idtipoimpuesto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }
}
