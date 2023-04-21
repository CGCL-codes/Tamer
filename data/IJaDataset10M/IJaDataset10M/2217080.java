package Servicio;

import Model.Permiso;
import Model.Usuario;

public class ManejadorPermiso extends ManejadorAbstracto {

    public ManejadorPermiso() {
    }

    protected String consulta() {
        String aux = "";
        switch(opcion) {
            case 0:
                aux = "select * from Permisos ";
                break;
            case 1:
                aux = "Select * from Permis where Usuario ='" + valor + "'";
        }
        return aux;
    }

    protected Object nuevo() {
        Object aux = null;
        try {
            aux = new Permiso(resultSet.getInt("Id_Permiso"), resultSet.getString("Permiso"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aux;
    }

    protected String table() {
        return "UsuariosXPermisos(Id_Permiso,Usuario)";
    }

    protected String values(Object objeto) {
        String aux;
        Permiso permiso = (Permiso) objeto;
        aux = "(" + permiso.getIdPermiso() + ",'" + valor + "')";
        return aux;
    }

    protected String table_u() {
        return "hola";
    }

    protected String campos_u(Object objeto) {
        return "hola";
    }

    protected String ides_u(Object objeto) {
        return "hola";
    }

    protected String table_d() {
        return "UsuariosXPermisos";
    }

    protected String campos_d(Object objeto) {
        return "usuario='" + ((Usuario) objeto).getNombreUsuario() + "'";
    }
}
