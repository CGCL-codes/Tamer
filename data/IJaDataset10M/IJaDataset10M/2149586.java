package modelo.capas;

public class DAOLibros {

    Conexion conexion;

    protected Conexion getConexion() {
        return conexion;
    }

    protected void setConexion(Conexion conexion) {
        this.conexion = conexion;
    }
}
