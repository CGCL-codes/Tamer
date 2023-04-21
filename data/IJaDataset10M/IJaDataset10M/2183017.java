package gr.aueb.cs.nlg.Communications.OntologyServer;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import com.hp.hpl.jena.ontology.*;
import gr.aueb.cs.nlg.Communications.*;

public class OntologyServer extends Thread {

    static Logger logger = Logger.getLogger("gr.aueb.cs.nlg.Communications.OntologyServer.OntologyServer");

    boolean flagout;

    private gr.aueb.cs.nlg.Communications.OntologyServer.ProcessRequest ONT_PR;

    private DialogueManagerInfo DMI;

    private OntModel m;

    CommunicationModule client;

    public OntologyServer() {
        super("OntologyServer");
        client = new CommunicationModule();
    }

    public OntologyServer(String nav_server_IP, int port) {
        super("OntologyServer");
        logger.setLevel(Level.INFO);
        client = new CommunicationModule(nav_server_IP, port);
    }

    public void die() throws Exception {
        flagout = true;
        client.disconnect();
    }

    public void setOntology(OntModel m) {
        this.m = m;
    }

    public void setDialogueManagerInfo(DialogueManagerInfo DMI) {
        this.DMI = DMI;
    }

    public void run() {
        ONT_PR = new gr.aueb.cs.nlg.Communications.OntologyServer.ProcessRequest();
        logger.debug("Initializing Communications with server");
        try {
            flagout = false;
            client.connect();
        } catch (Exception e) {
            logger.info("Couldn't get I/O for the connection to server");
            flagout = true;
        }
        logger.debug("Sending Module Information to server");
        try {
            int[] consumed = { ProcessRequest.PACKETCODE_Ontology_Req };
            int[] produced = { ProcessRequest.PACKETCODE_Ontology_Res };
            client.declarePackets(1, 1, produced, consumed, "OntologyServer");
        } catch (Exception e) {
            logger.info("Couldn't write module info");
            flagout = true;
        }
        if (!flagout) logger.info("Ontology Server: I was succesfully connected to Communication Server");
        try {
            while (!flagout) {
                Message msg = null;
                int packetcode = -1;
                logger.info("Waiting...");
                try {
                    msg = client.receive();
                    logger.debug("a message was read!!!");
                } catch (Exception e) {
                    flagout = true;
                    logger.debug("Couldn't read frame");
                }
                packetcode = msg.getPacketCode();
                switch(packetcode) {
                    case ProcessRequest.PACKETCODE_Ontology_Req:
                        String response = ONT_PR.processRequest2(msg.getXmlContent(), m, DMI);
                        client.send(response, ProcessRequest.PACKETCODE_Ontology_Res);
                        break;
                    default:
                        logger.debug("There is something wrong!!");
                }
            }
        } catch (Exception e) {
            logger.info("Problem on sending or receiving");
        }
        try {
            client.disconnect();
            logger.info("disconnected");
        } catch (Exception e) {
            logger.info("Couldn't close connection");
        }
    }
}
