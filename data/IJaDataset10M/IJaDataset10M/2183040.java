package monitor.server;

import monitor.Diccionario;
import monitor.modelo.PBX;
import monitor.io.IOManager;
import monitor.modelo.Agent;
import monitor.modelo.Queue;
import monitor.modelo.Terminal;
import monitor.plugin.PluginManager;
import monitor.report.DailyReport;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Properties;

public class AsteriskMonitorServerImpl {

    public PrintStream logger, loggerError, loggerConexion;

    public PBX centralita;

    public IOManager iomanager;

    public String version;

    private Properties configuracion;

    public DailyReport resumenHoy;

    private long startTime;

    public AsteriskMonitorServer serverFrontend;

    private Diccionario diccionario;

    private PluginManager pluginManager;

    public AsteriskMonitorServerImpl() {
        try {
            serverFrontend = new AsteriskMonitorServerFrontend(this);
        } catch (RemoteException e) {
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) new AsteriskMonitorServerImpl().init("/etc/asteriskmonitor/asteriskmonitor.properties"); else {
            new AsteriskMonitorServerImpl().init(args[0]);
        }
    }

    private void init(String archivoConfig) {
        startTime = System.currentTimeMillis();
        configuracion = new Properties();
        cargaConfig(archivoConfig);
        iniciaLogs();
        diccionario = new Diccionario(this);
        diccionario.connect(configuracion);
        cargaDatosCentralita();
        diccionario.disconnect();
        pluginManager = new PluginManager(configuracion, this);
        Thread pluginThread = new Thread(pluginManager);
        pluginThread.start();
        generateActionEvent(Tools.AST_MON_START, null);
        iomanager = new IOManager(configuracion, this);
        Thread hilo = new Thread(iomanager);
        hilo.start();
    }

    public void shutdown() {
        log("Shuwdown process started");
        log("Stoping IOManager");
        iomanager.stopAndDie(true);
        log("Shutdown process finished succesfully");
        System.exit(0);
    }

    private boolean cargaDatosCentralita() {
        try {
            boolean datosCargados = true;
            LinkedList<Queue> colas = diccionario.cargaColas();
            LinkedList<Terminal> puestos = diccionario.cargaPuestos();
            LinkedList<Agent> agentes = diccionario.cargaAgentes();
            centralita = new PBX(puestos, colas, agentes);
            return datosCargados;
        } catch (Exception e) {
            e.printStackTrace(loggerError);
            return false;
        }
    }

    public void iniciaLogs() {
        try {
            if (logger != null) logger.close();
            if (loggerConexion != null) loggerConexion.close();
            if (loggerError != null) loggerError.close();
        } catch (Exception e) {
        }
        boolean logErrorCargado = false;
        GregorianCalendar c = new GregorianCalendar();
        String fecha = c.get(Calendar.YEAR) + "_" + (c.get(Calendar.MONTH) + 1) + "_" + c.get(Calendar.DAY_OF_MONTH) + "_" + c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE);
        String tipoLogNormal = configuracion.getProperty("log.eventos.tipo", "archivo");
        String tipoLogConexion = configuracion.getProperty("log.conexion.tipo", "archivo");
        String tipoLogError = configuracion.getProperty("log.error.tipo", "archivo");
        String dirLog = configuracion.getProperty("log.dir", "log");
        try {
            if (tipoLogError.equalsIgnoreCase("archivo")) {
                loggerError = new PrintStream(new File(dirLog + "/error-" + fecha + ".log"));
            } else loggerError = System.out;
            logErrorCargado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (tipoLogNormal.equalsIgnoreCase("archivo")) logger = new PrintStream(new File(dirLog + "/eventos-" + fecha + ".log")); else logger = System.out;
        } catch (Exception e) {
            if (logErrorCargado) e.printStackTrace(loggerError);
            e.printStackTrace();
        }
        try {
            if (tipoLogConexion.equalsIgnoreCase("archivo")) loggerConexion = new PrintStream(new File(dirLog + "/conexion-" + fecha + ".log")); else loggerConexion = System.out;
        } catch (Exception e) {
            if (logErrorCargado) e.printStackTrace(loggerError);
            e.printStackTrace();
        }
        log("Starting AsteriskMonitorServer version " + version);
        logConexion("Starting AsteriskMonitorServer version " + version);
        logError("Starting AsteriskMonitorServer version " + version);
    }

    private void cargaConfig(String archivoConfig) {
        try {
            File archivo = new File(archivoConfig);
            configuracion.load(new FileInputStream(archivo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        version = configuracion.getProperty("version", "unknown");
    }

    public long runningMillis() {
        return System.currentTimeMillis() - startTime;
    }

    public void log(String s) {
        try {
            SimpleDateFormat fecha = new SimpleDateFormat(configuracion.getProperty("fecha.formato", "dd/MM/yyyy HH:mm:ss"));
            logger.println(fecha.format(new java.util.Date()) + " " + s);
            logger.flush();
        } catch (Exception e) {
            e.printStackTrace(loggerError);
        }
    }

    public void logConexion(String s) {
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("ddMMyyyyHHmmss");
            loggerConexion.println(fecha.format(new java.util.Date()) + " " + s);
            loggerConexion.flush();
        } catch (Exception e) {
            e.printStackTrace(loggerError);
        }
    }

    public void logError(String s) {
        try {
            SimpleDateFormat fecha = new SimpleDateFormat("ddMMyyyyHHmmss");
            loggerConexion.println(fecha.format(new java.util.Date()) + " " + s);
            loggerConexion.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logController(String s) {
        log(s);
    }

    public PBX getPBX() {
        return centralita;
    }

    public void generateActionEvent(int actionId, Object[] args) {
        pluginManager.proccessActionEvent(actionId, args);
    }
}
