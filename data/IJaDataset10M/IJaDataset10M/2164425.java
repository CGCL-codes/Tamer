package BaseDatos;

import Utilitarios.Utilitarios;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import monitoreo.sensores.Contactos;
import monitoreo.sensores.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kradac
 */
public class BaseDatos {

    private Connection conexion;

    /**
     * Nombre de la Base de datos
     */
    private String bd = "";

    private String ip = "";

    private String usr = "";

    private String pass = "";

    private String puerto = "";

    /**
     * Logger para guardar los log en un archivo y enviar por mail los de error
     */
    private static final Logger log = LoggerFactory.getLogger(BaseDatos.class);

    private Properties arcConfig;

    private Utilitarios funciones = new Utilitarios();

    public BaseDatos() {
        ConexionBase();
    }

    public BaseDatos(Properties arc) {
        ConexionBase(arc);
    }

    /**
     * Crea la conexion directamente a la base de datos de rastreosatelital
     * de kradac, parametros de la conexion quemados por defecto para la
     * maquina local
     */
    private void ConexionBase(Properties conf) {
        try {
            this.arcConfig = conf;
            String driver = "com.mysql.jdbc.Driver";
            this.ip = arcConfig.getProperty("ip_base");
            this.bd = arcConfig.getProperty("base");
            this.usr = arcConfig.getProperty("user");
            this.pass = arcConfig.getProperty("pass");
            this.puerto = arcConfig.getProperty("puerto_base");
            String url = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;
            try {
                try {
                    Class.forName(driver).newInstance();
                } catch (InstantiationException ex) {
                    log.trace("No se puede cargar el driver de la base de datos...", ex);
                } catch (IllegalAccessException ex) {
                    log.trace("No se puede cargar el driver de la base de datos acceso ilegal...", ex);
                }
            } catch (ClassNotFoundException ex) {
                log.trace("No se puede cargar el driver de la base de datos...", ex);
            }
            try {
                conexion = DriverManager.getConnection(url, usr, pass);
            } catch (SQLException ex) {
                if (ex.getMessage().equals("Communications link failure")) {
                    log.trace("Enlace de conexión con la base de datos falló, falta el archivo de configuración...");
                }
            }
            log.info("Iniciar conexion a la base de datos [" + this.bd + "]");
        } catch (NullPointerException ex) {
            throw new UnsupportedOperationException("null configuracion base");
        }
    }

    /**
     * Crea una conexion a cualquier base de datos mysql, con parametros
     * de conexion indepenientes
     * @param ip - IP del servidor
     * @param bd - Nombre de la Base de datos
     * @param usr -  Nombre de Usuario
     * @param pass - Clave de la Base de datos
     */
    private void ConexionBase() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + "/" + bd;
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException ex) {
                log.trace("No se puede cargar el driver de la base de datos...", ex);
            } catch (IllegalAccessException ex) {
                log.trace("No se puede cargar el driver de la base de datos acceso ilegal...", ex);
            }
        } catch (ClassNotFoundException ex) {
            log.trace("No se puede cargar el driver de la base de datos...", ex);
        }
        try {
            conexion = DriverManager.getConnection(url, usr, pass);
        } catch (SQLException ex) {
            if (ex.getMessage().equals("Communications link failure")) {
                log.trace("Enlace de conexión con la base de datos falló, falta el archivo de configuración...");
            }
        }
        log.info("Iniciar conexion a la base de datos [" + this.bd + "]");
    }

    /**
     * Ejecuta una consulta en la base de datos, que devuelve valores
     * es necesario recorrer el resultset
     * @param sql - debe ser Select
     * @return ResultSet
     */
    public ResultSet ejecutarConsulta(String sql) {
        ResultSet r = null;
        try {
            Statement stat = (Statement) conexion.createStatement();
            r = stat.executeQuery(sql);
            log.trace("Consultar:" + sql);
        } catch (SQLException ex) {
            int intCode = ex.getErrorCode();
            if (intCode == 0) {
                log.trace("Error de comunicación con la BD, mucho tiempo esperando respuesta...");
                reconectarBaseDatos();
            } else {
                log.trace("Error al consultar codigo[" + intCode + "]", ex);
            }
        } catch (NullPointerException ex) {
            log.trace("No se pudo crear el statement...", ex);
        }
        return r;
    }

    /**
     * Ejecuta una consulta en la base de datos, que devuelve valores
     * no es necesario recorrer el resultset
     * @param sql - debe ser Select
     * @return ResultSet
     */
    public ResultSet ejecutarConsultaUnDato(String sql) {
        ResultSet rsCUD = null;
        try {
            Statement sta = (Statement) conexion.createStatement();
            rsCUD = sta.executeQuery(sql);
            log.trace("Consultar:" + sql);
            rsCUD.next();
        } catch (SQLException ex) {
            int intCode = ex.getErrorCode();
            if (intCode == 0) {
                log.trace("Error de comunicación con la BD, mucho tiempo esperando respuesta...");
                reconectarBaseDatos();
            } else if (intCode == 1146) {
                log.trace("Tabla no existe: {}", ex.getMessage());
            } else {
                log.trace("Error al consultar codigo[" + intCode + "]", ex);
            }
        } catch (NullPointerException npx) {
            log.info("No se puede aceceder a la base de datos...");
        }
        return rsCUD;
    }

    /**
     * Ejecuta una consulta en la base de datos, que devuelve valores
     * no es necesario recorrer el resultset
     * @param sql - debe ser Select
     * @return ResultSet
     */
    public ResultSet ejecutarConsultaUnDatoSinImprimir(String sql) {
        ResultSet rsCUD = null;
        try {
            Statement sta = (Statement) conexion.createStatement();
            rsCUD = sta.executeQuery(sql);
            rsCUD.next();
        } catch (SQLException ex) {
            int intCode = ex.getErrorCode();
            if (intCode == 0) {
                log.trace("Error de comunicación con la BD, mucho tiempo esperando respuesta...");
                reconectarBaseDatos();
            } else if (intCode == 1146) {
                log.trace("Tabla no existe: {}", ex.getMessage());
            } else {
                log.trace("Error al consultar codigo[" + intCode + "]", ex);
            }
        } catch (NullPointerException npx) {
            log.info("No se puede aceceder a la base de datos...");
        }
        return rsCUD;
    }

    /**
     * Ejecuta una sentencia en la base esta puede ser de INSERT, UPDATE O
     * DELETE
     * @param sql - Sentencias INSERT, UPDATE, DELETE
     * @return int - confirmacion del resultado 1 valido || 0 invalido
     */
    public boolean ejecutarSentencia(String sql) {
        try {
            Statement st = (Statement) conexion.createStatement();
            log.trace("Ejecutar:" + sql);
            int rta = st.executeUpdate(sql);
            if (rta >= 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            int intCode = ex.getErrorCode();
            log.trace("Error al ejecutar sentecia codigo[" + intCode + "]", ex);
            return false;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Cierra la conexion con la base de datos
     * @return Connection
     * @throws SQLException
     */
    public void CerrarConexion() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            System.out.println("No se pudo completar el cierre de la BD...");
        } catch (NullPointerException ex) {
            System.out.println("NO está abierta la base de datos...");
        }
    }

    /**
     * Inserta el dato medido de un sensor determinado
     * @param id_sen
     * @param parametro
     * @param fecha
     * @param hora
     * @return boolean true si inserta correctamente
     */
    public boolean insertarDatosSensor(String id_sen, double parametro, String fecha, String hora) {
        String sql = "INSERT INTO DATOS(ID_SEN,PARAMETRO_DAT,FECHA_DAT,HORA_DAT) " + "VALUES('" + id_sen + "'," + parametro + ",'" + fecha + "','" + hora + "')";
        return ejecutarSentencia(sql);
    }

    /**
     * Obtiene la lista de sensores pertenecientes a un modulo, con los parametros
     * a monitorear minimos y maximos de cada uno de los sensores.
     * @param modulo
     * @return ArrayList<Sensor> 
     */
    public ArrayList<Sensor> getListaParametrosMaxMinModulo(String modulo) {
        ArrayList<Sensor> lista = new ArrayList<Sensor>();
        String sql = "SELECT ID_SEN,PAR_MIN_SEN,PAR_MAX_SEN,TIPO_SEN FROM SENSORES " + "WHERE MODULO_SEN = '" + modulo + "'";
        ResultSet r = ejecutarConsulta(sql);
        try {
            while (r.next()) {
                lista.add(new Sensor(r.getString("ID_SEN"), r.getDouble("PAR_MIN_SEN"), r.getDouble("PAR_MAX_SEN"), r.getString("TIPO_SEN")));
            }
        } catch (SQLException ex) {
            log.trace("COD[{}]", ex.getErrorCode(), ex);
        } catch (NullPointerException ex) {
        }
        return lista;
    }

    /**
     * Obtiene los contactos de las personas a las que se les tiene que enviar
     * la notificación de que ha ocurrido un problema
     * @param id_sen
     * @return ArrayList<String>
     */
    public ArrayList<Contactos> getListaContactosReportar(String id_sen) {
        ArrayList<Contactos> lista = new ArrayList<Contactos>();
        String sql = "SELECT ID_CON,MAIL_CON,NOMBRE_CON,TELEFONO FROM CONTACTOS";
        ResultSet r = ejecutarConsulta(sql);
        try {
            while (r.next()) {
                lista.add(new Contactos(r.getInt("ID_CON"), r.getString("MAIL_CON"), r.getString("NOMBRE_CON"), r.getString("TELEFONO")));
            }
        } catch (SQLException ex) {
            log.trace("COD[{}]", ex.getErrorCode(), ex);
        }
        return lista;
    }

    /**
     * Obtiene el id de un dato ingresado, esto sirve para insertar en las notificaciones
     * a que dato pertenece una notificación.
     * @param id_sen
     * @param hora
     * @param param_sen
     * @return int
     */
    public int getIDDatoInsertado(String id_sen, String hora, double param_sen) {
        String sql = "SELECT SF_OBTENER_ID_DATO('" + id_sen + "','" + hora + "'," + param_sen + ")";
        ResultSet r = ejecutarConsultaUnDato(sql);
        try {
            return r.getInt(1);
        } catch (SQLException ex) {
            log.trace("COD[{}]", ex.getErrorCode(), ex);
        } catch (NullPointerException ex) {
        }
        return 0;
    }

    /**
     * Inserta el registro de una notificación al mail de algun contacto
     * @param id_con
     * @param id_dato
     */
    public void insertarNotificacion(int id_con, int id_dato) {
        String sql = "CALL SP_INSERTAR_NOTIFICACION(" + id_con + "," + id_dato + ")";
        ejecutarSentencia(sql);
    }

    /**
     * Permite controlar si desde la ultima alerta ha pasado un tiempo definido
     * para que no sature el mail con correos en caso de inconvenientes...
     * @param id_sen
     * @return boolean true si hay que enviar alerta
     */
    public boolean esTiempoNuevaAlerta(String id_sen) {
        String sql = "SELECT SF_ENVIAR_ALERTA('" + id_sen + "')";
        ResultSet rs = ejecutarConsultaUnDato(sql);
        try {
            return rs.getBoolean(1);
        } catch (SQLException ex) {
            return false;
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
     * Consulta cual es el puerto que debe escuchar el servidor
     * @return int
     */
    public int getPuertoEscucharServidor() throws NullPointerException, SQLException {
        String sql = "SELECT VALOR FROM CONFIGURACION WHERE NOMBRE='puerto_server'";
        return ejecutarConsultaUnDato(sql).getInt("VALOR");
    }

    /**
     * obtener el nombre del sensor
     * @param id_sen
     * @return String
     */
    public String getNombreSensor(String id_sen) {
        try {
            String sql = "SELECT NOMBRE_SEN FROM SENSORES WHERE ID_SEN='" + id_sen + "'";
            return ejecutarConsultaUnDato(sql).getString("NOMBRE_SEN");
        } catch (SQLException ex) {
            log.trace("COD[{}]", ex.getErrorCode(), ex);
        } catch (NumberFormatException ex) {
            log.trace("{}", ex.getMessage(), ex);
        }
        return "";
    }

    /**
     * Obtener el nombre del modulo
     * @param modulo
     * @return String
     */
    public String getNombreModulo(String modulo) {
        try {
            String sql = "SELECT NOMBRE_MOD FROM MODULOS WHERE MODULO_SEN='" + modulo + "'";
            return ejecutarConsultaUnDato(sql).getString("NOMBRE_MOD");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 0) {
                log.trace("No se ha ingresado los nombres de los módulos...", ex);
            } else {
                log.trace("COD[{}]", ex.getErrorCode(), ex);
            }
        } catch (NumberFormatException ex) {
            log.trace("{}", ex.getMessage(), ex);
        } catch (NullPointerException ex) {
        }
        return "";
    }

    /**
     * Obtiene el valor de la tabla de configuraciones de una determinada llave,
     * este metodo se utiliza para utilizar las configuraciones de la base de datos
     * y no del archivo de configuración
     * @param key
     * @return String
     */
    public String getValorConfiguracion(String key) {
        try {
            String sql = "SELECT VALOR FROM CONFIGURACION WHERE NOMBRE='" + key + "'";
            ResultSet rsConfig = ejecutarConsultaUnDato(sql);
            return rsConfig.getString("VALOR");
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 0) {
                log.info("No se encontró valores para esta llave [{}]", key);
            } else {
                log.info("[COD {}]{}", ex.getErrorCode(), ex.getMessage());
            }
        }
        return null;
    }

    private void reconectarBaseDatos() {
        CerrarConexion();
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + ip + "/" + bd;
        try {
            try {
                Class.forName(driver).newInstance();
            } catch (InstantiationException ex) {
                log.trace("No se puede cargar el driver de la base de datos...", ex);
            } catch (IllegalAccessException ex) {
                log.trace("No se puede cargar el driver de la base de datos acceso ilegal...", ex);
            }
        } catch (ClassNotFoundException ex) {
            log.trace("No se puede cargar el driver de la base de datos...", ex);
        }
        try {
            conexion = DriverManager.getConnection(url, usr, pass);
        } catch (SQLException ex) {
            if (ex.getMessage().equals("Communications link failure")) {
                log.trace("Enlace de conexión con la base de datos falló, falta el archivo de configuración...");
            }
        }
        log.info("Iniciar RECONEXION BD [" + this.bd + "]");
    }
}
