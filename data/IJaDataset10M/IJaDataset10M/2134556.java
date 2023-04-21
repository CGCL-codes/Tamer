package org.digitall.lib.geo.gaia.components.infrastructure;

import java.sql.ResultSet;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.sql.LibSQL;

public class GaiaTipoCultoyCultura {

    private int idTipoCultoyCultura = -1;

    private String nombre;

    public GaiaTipoCultoyCultura(int _idTipoCultoyCultura, String _Nombre) {
        idTipoCultoyCultura = _idTipoCultoyCultura;
        nombre = _Nombre;
    }

    public GaiaTipoCultoyCultura(int _idTipoCultoyCultura) {
        idTipoCultoyCultura = _idTipoCultoyCultura;
    }

    public GaiaTipoCultoyCultura() {
    }

    public void setIdTipoCultoyCultura(int idTipoCultoyCultura) {
        this.idTipoCultoyCultura = idTipoCultoyCultura;
    }

    public int getIdTipoCultoyCultura() {
        return idTipoCultoyCultura;
    }

    public void setNombre(String _nombre) {
        nombre = _nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void loadData(int _idTipoCultoyCultura) {
        idTipoCultoyCultura = _idTipoCultoyCultura;
        String params = String.valueOf(idTipoCultoyCultura);
        ResultSet data = LibSQL.exFunction("tabs.getTipocultoyCultura", params);
        try {
            if (data.next()) {
                nombre = data.getString("nombre");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int saveData() {
        if (!nombre.trim().equals("")) {
            String params = idTipoCultoyCultura + ",'" + nombre + "'";
            int result = LibSQL.getInt("tabs.saveTipoCultoyCultura", params);
            if (idTipoCultoyCultura == -1) {
                idTipoCultoyCultura = result;
            }
            return result;
        } else {
            Advisor.messageBox("Debe ingresar un nombre para el Tipo de culto/Cultura", "Nombre no v�lido");
            return -1;
        }
    }
}
