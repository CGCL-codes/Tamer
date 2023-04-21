package manejadores;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Statement;

public class ConexionDB {

    private Connection con = null;

    private String myError = "";

    private Logger logger = Logger.getLogger("manejadores");

    public ConexionDB() {
    }

    public String getError() {
        return this.myError;
    }

    /**
     * Se conecta a la base datos utilizando creando un pool de conexiones.
     * @return Retorna una conexion a la base de datos sacado del pool de conexiones.
     */
    public Connection ConectarDB() {
        try {
            Context ctx = new InitialContext();
            if (ctx == null) {
                this.myError = "No initial context";
                return null;
            }
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/poolDeConexion");
            if (ds == null) {
                this.myError = "No data source";
                return null;
            }
            this.con = ds.getConnection();
            if (this.con == null) {
                this.myError = "No connection";
                return null;
            }
            return this.con;
        } catch (SQLException ex) {
            this.myError = "SQLException " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return null;
        } catch (NamingException ex) {
            this.myError = "NamingException " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return null;
        } catch (Exception ex) {
            this.myError = "Exception " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
            return null;
        }
    }

    /**
     * Retorna una conexion manejada por el pool de conexiones.
     * @return Una conexion a la base de datos.
     */
    public Connection getConxion() {
        return this.con;
    }

    /**
     * Cierra la conexion a la base de datos creada por el pool de conexiones.
     */
    public void DesconectarDB() {
        try {
            this.con.close();
        } catch (SQLException ex) {
            this.myError = "SQLException " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
        } catch (Exception ex) {
            this.myError = "Exception " + ex.getMessage();
            logger.log(Level.SEVERE, "loginAction", ex);
        }
    }
}
