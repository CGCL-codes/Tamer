package limfic.persistencia.mysql.ciudad;

import limfic.dominio.Ciudad;
import limfic.persistencia.mysql.RepositorioGenerico;
import limfic.persistencia.RepositorioException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Implementa las operaciones del repositorio ciudad. 
 * @author: OpenCodes (Nicolas Tenca, Luis Soto)
 * @see ICiudadRepositorio
 * @param Ciudad
 */
public class CiudadRepositorio extends RepositorioGenerico<Ciudad> implements ICiudadRepositorio {

    public CiudadRepositorio(Connection connection) {
        super(connection);
    }

    /**
     *
     * Mapear fila devuelve el resultset con las ciudades
     */
    @Override
    public Ciudad mapearFila(ResultSet resultSet) throws SQLException {
        int i = 1;
        int id = resultSet.getInt(i++);
        String nombre = resultSet.getString(i++);
        Ciudad ciudad = null;
        ciudad = new Ciudad(id, nombre);
        return ciudad;
    }

    @Override
    protected String sqlAgregar(Ciudad ciudad) {
        String sql = "Insert into ciudad (nombre) values ('" + ciudad.getNombre() + "')";
        return sql;
    }

    @Override
    protected String sqlModificar(Ciudad ciudad) {
        String sql = "Update ciudad set nombre='" + ciudad.getNombre() + "' where id=" + ciudad.getId();
        return sql;
    }

    @Override
    protected String sqlEliminar(Ciudad ciudad) {
        String sql = "Delete from ciudad where id=" + ciudad.getId();
        return sql;
    }

    @Override
    protected String sqlObtenerUno(String id) {
        String sql = "Select * from ciudad where idciudad=" + id;
        return sql;
    }

    @Override
    protected String sqlObtenerTodos() {
        String sql = "Select * from ciudad";
        return sql;
    }

    @Override
    protected String sqlObtenerAlgunos(String[] parametrosBusqueda) {
        String sql = "Select * from ciudad where nombre like '%" + parametrosBusqueda[0] + "%'";
        return sql;
    }

    @Override
    protected String sqlExiste(String[] parametrosChekeo) {
        String sql = "Select id from ciudad where nombre='" + parametrosChekeo[0] + "'";
        return sql;
    }
}
