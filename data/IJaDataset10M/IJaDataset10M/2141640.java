package neoAtlantis.utilidades.ctrlAcceso.recursoBloqueo.interfaces;

import java.util.ArrayList;
import neoAtlantis.utilidades.ctrlAcceso.Usuario;
import neoAtlantis.utilidades.debuger.interfaces.Debuger;
import neoAtlantis.utilidades.logger.interfaces.Logger;

/**
 * Interface que define el comportamiento que debe de tener un <i>Bloqueador de Usuarios</i>.
 * @version 1.0
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public abstract class RecursoBloqueador {

    /**
     * Constante de indica un segundo en milisegundos
     */
    public static final long SEGUNDO_EN_MILIS = 1000;

    /**
     * Constante de indica un minuto en milisegundos
     */
    public static final long MINUTO_EN_MILIS = SEGUNDO_EN_MILIS * 60;

    /**
     * Constante de indica una hora en milisegundos
     */
    public static final long HORA_EN_MILIS = MINUTO_EN_MILIS * 60;

    /**
     * Constante de indica und&iacute;a en milisegundos
     */
    public static final long DIA_EN_MILIS = HORA_EN_MILIS * 24;

    /**
     * Constante de indica una semana en milisegundos
     */
    public static final long SEMANA_EN_MILIS = DIA_EN_MILIS * 7;

    /**
     * Veris&oacute;n de la clase.
     */
    public static final String VERSION = "1.0";

    /**
     * Debuger que da seguimiento a los procesos que realiza la clase.
     */
    protected Debuger mDebug;

    /**
     * Loger que registra los errores que se probocan en la clase.
     */
    protected Logger mLog;

    /**
     * Variable que indica el modo de bloqueo utilizado.
     */
    protected TipoBloqueo modoBloqueo = TipoBloqueo.USUARIO;

    /**
     * Variable que indica el tiempo de bloqueo a utilizar.
     */
    protected long tiempoBloqueo = RecursoBloqueador.DIA_EN_MILIS;

    /**
     * Definici&oacute;n del metodo para agregar bloqueos de usuario.
     * @param user Usuario a bloquear
     * @throws java.lang.Exception
     */
    public abstract void agregaBloqueo(Usuario user) throws Exception;

    /**
     * Definici&oacute;n del metodo para revisar lo bloqueos que ya hayan finalizado.
     * @return Colecci&oacute;n con los nombres de los usuarios de los cuales termino su bloqueo.
     * @throws java.lang.Exception
     */
    public abstract ArrayList<String> revisaBloqueosTerminados() throws Exception;

    /**
     * Definici&oacute;n del metodo que revisa si un usuario esta bloqueado.
     * @param user Usuario del que se desea verificar su bloqueo
     * @return true si existe bloqueo
     * @throws java.lang.Exception
     */
    public abstract boolean verificaBloqueo(Usuario user) throws Exception;

    /**
     * Definici&oacute;n del metodo para remover bloqueos de usuario.
     * @param user Usuario del que se desea terminar el bloqueo
     * @return true si se logro remover el bloqueo
     * @throws java.lang.Exception
     */
    public abstract boolean remueveBloqueo(Usuario user) throws Exception;

    /**
     * Definici&oacute;n del metodo para agregar la conexi&oacute;n de un usuario.
     * @param user Usuario del que se desea registrar su conexi&oacute;n
     * @throws java.lang.Exception
     */
    public abstract void agregaConexion(Usuario user) throws Exception;

    /**
     * Definici&oacute;n del metodo para remover la conexi&oacute;n de un usuario.
     * @param user Usuario del que se desea remover su conexi&oacute;n
     * @return true si se logro remover la conexi&oacute;n
     * @throws java.lang.Exception
     */
    public abstract boolean remueveConexion(Usuario user) throws Exception;

    /**
     * Definici&oacute;n del metodo que revisa si un usuario esta conectado.
     * @param user Usuario del que se desea revisar su conexi&oacute;n
     * @return true si tiene una conexi&oacute;n activa
     * @throws java.lang.Exception
     */
    public abstract boolean verificaConexion(Usuario user) throws Exception;

    /**
     * Asigna un Debuger a la clase para poder dar seguimiento a los procesos que realiza la clase.
     * @param mDebug the mDebug to set
     */
    public void setMDebug(Debuger mDebug) {
        this.mDebug = mDebug;
    }

    /**
     * Asigna un Loger a la clase para poder registrar los errores que se proboquen en la clase.
     * @param mLog the mLog to set
     */
    public void setMLog(Logger mLog) {
        this.mLog = mLog;
    }

    /**
     * Asigna el tipo de bloqueo a utilizar
     * @param modoBloqueo Tipo de bloqueo:
     */
    public void setModoBloqueo(TipoBloqueo modoBloqueo) {
        this.modoBloqueo = modoBloqueo;
    }

    /**
     * Asigna el tiempo de duración de un bloqueo.
     * @param tiempoBloqueo Tiempo en milisegundos que durar&aacute; un bloqueo
     */
    public void setTiempoBloqueo(long tiempoBloqueo) {
        this.tiempoBloqueo = tiempoBloqueo;
    }
}
