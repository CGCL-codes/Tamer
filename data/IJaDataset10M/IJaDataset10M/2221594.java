package org.briareo.jms.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.briareo.exception.QueueSenderException;
import org.briareo.jms.client.QueueSender;
import org.briareo.model.SyslogAuditMessage;
import org.scoja.server.core.Event;
import org.scoja.server.parser.ParsedEvent;

/**
 * @author Javier Aparicio
 * 
 */
public class SysLogRedirector extends Thread {

    /**
   * Loggger de clase
   */
    private static Logger logger = Logger.getLogger(SysLogRedirector.class);

    /**
   * SysLog port to listen to
   */
    private static final int PORT = 514;

    /**
   * Max Syslog datagram size.
   */
    private static final int BUFFER_SIZE = 10000;

    /**
   * Daemon instance
   */
    private static SysLogRedirector instance = null;

    /**
   * Nombre de la cola por defecto.
   */
    private static String DEFAULT_QUEUE_NAME = "SYSTEM";

    /**
   * Cliente JMS para la redireccion de los mensajes a la cola de ActiveMQ
   */
    private QueueSender sender = null;

    /**
   * Listening Socket
   */
    private DatagramSocket socket = null;

    /**
   * 
   * @param providerURL
   * @param queueName
   * @return
   */
    public static synchronized SysLogRedirector getInstance(String providerURL, String queueName) {
        if (instance == null) {
            instance = new SysLogRedirector(providerURL, queueName);
        }
        logger.info("[getInstance.retorna]:: " + instance);
        return instance;
    }

    /**
   * Constructor por defecto. Redirige a la cola por defecto.
   * 
   */
    protected SysLogRedirector(String providerURL) {
        this(providerURL, null);
    }

    /**
   * Constructor con seleccion de cola.
   * 
   */
    protected SysLogRedirector(String providerURL, String queueName) {
        super();
        if ((queueName == null) || "".equals(queueName)) {
            logger.debug("[SysLogRedirector]:: Utilizando cola por defecto: '" + DEFAULT_QUEUE_NAME + "'");
            queueName = DEFAULT_QUEUE_NAME;
        }
        logger.debug("\n**************************\nEl SysLogRedirector redirigira los mensajes " + "recibidos por el puerto '" + PORT + "' hacia la cola '" + queueName + "' del activeMQ de '" + providerURL + "'.\n\n");
        this.setName("SYSLOG_REDIRECTOR");
        this.setDaemon(true);
        this.sender = new QueueSender(providerURL, queueName);
    }

    /**
   * Proceso principal del Thread de redireccion.
   * 
   */
    public void run() {
        try {
            this.socket = new DatagramSocket(PORT);
            logger.debug("Socket '" + PORT + "' Creado.");
            byte[] data = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            while (this.sender != null) {
                logger.debug("Esperando...");
                packet.setLength(BUFFER_SIZE);
                this.socket.receive(packet);
                Event event = new ParsedEvent(packet.getAddress(), data, 0, packet.getLength());
                logger.debug("[run]:: Priority: " + event.getPriority());
                logger.debug("[run]:: Message: " + event.getMessage());
                logger.debug("[run]:: Level: " + event.getLevel() + " " + event.getLevelName());
                logger.debug("[run]:: CanonicalHost: " + event.getCanonicalHost());
                logger.debug("[run]:: CanonicalHostName: " + event.getCanonicalHostName());
                logger.debug("[run]:: Data: " + event.getData());
                logger.debug("[run]:: Energy: " + event.getEnergy());
                logger.debug("[run]:: Facility: " + event.getFacility());
                logger.debug("[run]:: FacilityName: " + event.getFacilityName());
                logger.debug("[run]:: Host: " + packet.getAddress());
                logger.debug("[run]:: HostName: " + packet.getAddress().getHostName());
                logger.debug("[run]:: CanonicalHostName: " + packet.getAddress().getCanonicalHostName());
                logger.debug("[run]:: Program: " + event.getProgram());
                logger.debug("[run]:: ReceivedTimestamp: " + event.getReceivedTimestamp());
                logger.debug("[run]:: SendTimestamp: " + event.getSendTimestamp());
                SyslogAuditMessage sam = new SyslogAuditMessage(event);
                try {
                    logger.info("[run]:: Redireccionando mensaje...");
                    this.sender.sendMessage(sam);
                    logger.debug("[run]:: OK");
                } catch (QueueSenderException e) {
                    logger.error("[run]:: ", e);
                }
                yield();
            }
            logger.debug("\n\n\n---");
        } catch (Throwable e) {
            logger.warn("[run]:: Error en SysLogRedirector... ", e);
        }
    }

    /**
   * Finalizacion del Thread.
   * 
   */
    public void end() {
        logger.info("[end.entrada]:: ");
        this.sender = null;
        this.setDaemon(false);
        if (this.socket != null) {
            this.socket.disconnect();
        }
        this.socket = null;
        logger.info("Socket '" + PORT + "' liberado.");
        this.getThreadGroup().interrupt();
        logger.info("[end.retorna]:: ");
    }

    public static void main(String[] args) {
        PropertyConfigurator.configure("etc/log4j.properties");
        logger.debug("[main]:: Logger configurado.");
        StringBuffer sbUsage = new StringBuffer();
        sbUsage.append("\n\n*************************************************************************************");
        sbUsage.append("\n\n  USO:\n");
        sbUsage.append("  start_syslog_redirector <provider_url> <SYSLOG_queue_name>\n");
        sbUsage.append("  Ej: start_syslog_redirector tcp://localhost:61616 SYSTEM\n\n");
        sbUsage.append("  NOTA: Para detener el servidor de redireccion de mensajes, pulsar en la consola <Ctrl>+C\n\n");
        sbUsage.append("*************************************************************************************\n\n");
        try {
            if ((args == null) || (args.length < 2)) {
                logger.warn("[main]:: PARAMETROS INCORRECTOS.");
                throw new Exception(sbUsage.toString());
            }
            if ((args[0] == null) || "".equals(args[0].trim())) {
                logger.warn("[main]:: VALOR INVALIDO PARA EL PARAMETRO 0 [" + args[0] + "].");
                throw new Exception(sbUsage.toString());
            }
            if ((args[1] == null) || "".equals(args[1].trim())) {
                logger.warn("[main]:: VALOR INVALIDO PARA EL PARAMETRO 1 [" + args[1] + "].");
                throw new Exception(sbUsage.toString());
            }
            logger.debug("\nINICIALIZANDO DEMONIO DE REDIRECCION...");
            SysLogRedirector redirector = SysLogRedirector.getInstance(args[0], args[1]);
            redirector.start();
            redirector.join();
        } catch (Throwable e) {
            logger.warn("\n\n" + e);
        }
        logger.debug("[main]:: FIN.");
    }
}
