package iteradores;

import utils.*;
import java.sql.*;

public class IteradorTipoDocumento extends Iterador {

    private ResultSet rs_datos;

    public IteradorTipoDocumento() {
    }

    public IteradorTipoDocumento(ResultSet rs_datos) throws Exception {
        super(rs_datos);
        this.rs_datos = rs_datos;
    }

    public int getIdTipo() throws Exception {
        return rs_datos.getInt("idtipodocumento");
    }

    public String getDescripcion() throws Exception {
        return rs_datos.getString("descripcion");
    }

    public String getUsuario_Ing() throws Exception {
        return rs_datos.getString("usuario_ing");
    }

    public String getFecha_Ing() throws Exception {
        return rs_datos.getString("fecha_ing");
    }
}
