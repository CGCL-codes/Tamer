package es.realtimesystems.sim104;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.TTCCLayout;
import javax.xml.bind.*;

/**
 * La remota 104. Remota que utiliza el protocolo de Telecontrol IEC 60870-5-104.
 * </br>
 * La base de datos de la remota es din�mica. Se construye en base a un fichero de base de datos en xml.
 * El formato del fichero xml es determinado por el XML Schema "lru.xsd"
 * La clase RTU opera internamente con una remota l�gica o LRU. La LRU tal como se define en "lru.xsd" puede
 * constar de puntos simples (SP), dobles (DP), medidas anal�gicas (MS) y pasos de transformaci�n (ST).
 *
 * Una vez se ha construido la base de datos de puntos scada de forma din�mica, la remota espera las conexiones
 * del front-end de telecontrol del sistema de control o sistema scada.
 *
 *
 * <p>
 * <b>REVISIONES:</b>
 * </p>
 *
 * @author Alejandro Garcia (malejandrogarcia@yahoo.es)
 * @version 1.0
 */
public class RTU {

    /** Timeout de espera a conexi�n. Se vuelve a poner en escucha pasado ese tiempo */
    private static final int TIMEOUT_ESPERA_CONEXION = 2000;

    private Logger logger = null;

    private String[] args = null;

    private static String sXMLDatabase = null;

    /** Base de datos de puntos scada */
    private ScadaDataBase sdb = null;

    /** ServerSocket de escucha a conexion 104 */
    ServerSocket s = null;

    /** Socket del servidor conectado a esta RTU */
    Socket cliente = null;

    /**
	 * Construye la remota 104 en base a un fichero xml con la definici�n de la base de datos.
	 * El formato del fichero xml es determinado por el XML Schema "lru.xsd"
	 * Constructor arg0= remota.xml
	 *
	 * @param args
	 */
    public RTU(String arg0) {
        super();
        sXMLDatabase = arg0;
        logger = Logger.getLogger("sim104.RTU");
        logger.addAppender(new ConsoleAppender(new TTCCLayout()));
        logger.setLevel(Level.DEBUG);
        logger.info("*Constructor RTU*");
        paserXMLDatabase(sXMLDatabase);
    }

    /**
	 * Imprime el mensaje de uso de la aplicaci�n
	 */
    private static void uso() {
        System.out.println("");
        System.out.println("sim104 v1.0 by AGD");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Uso: java sim104.RTU <remota.xml>");
        System.out.println("");
        System.out.println("Programa de Simulaci�n de remota IEC-104");
        System.out.println("<remota.xml>:\tFichero .xml de configuraci�n de la remota (base de datos)");
        System.out.println("");
    }

    /**
     * Blucle principal de procesamiento.
     */
    private void run() {
        logger.debug("en run...");
        try {
            s = new ServerSocket(IEC104.PORT_104);
            s.setSoTimeout(TIMEOUT_ESPERA_CONEXION);
            while (true) {
                try {
                    cliente = s.accept();
                    while (cliente.isConnected()) {
                        logger.info("Conexion aceptada de: " + cliente.getRemoteSocketAddress());
                    }
                } catch (java.net.SocketTimeoutException e) {
                    logger.info("Esperando conexi�n 104 desde front-end...");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length != 1) {
            uso();
            return;
        }
        try {
            System.out.println("");
            System.out.println("[RTU] sim104 v1.0 by AGD");
            System.out.println("----------------------------------------------------------------------");
            RTU rtu = new RTU(args[0]);
            rtu.run();
        } finally {
        }
    }

    /**
     * Realiza el parser de la base de datos .xml y carga la base de datos
     * @param sFichero
     */
    private void paserXMLDatabase(String xmlFile) {
        sdb = new ScadaDataBase(xmlFile);
    }
}
