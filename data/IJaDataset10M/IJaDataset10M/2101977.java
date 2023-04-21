package datos.coneccion.mysql;

import datos.coneccion.BaseDatos;

/**
 *
 * @author luciano
 */
public class BaseDatosMySQL extends BaseDatos {

    public BaseDatosMySQL() {
        super("com.mysql.jdbc.Driver", "jdbc:mysql://");
    }
}
