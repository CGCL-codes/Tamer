package fr.antifirewall.client.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
import fr.antifirewall.client.Config;
import fr.antifirewall.communication.Connexion;
import fr.antifirewall.communication.ConnexionControler;
import fr.antifirewall.communication.SimpleConnexion;
import fr.antifirewall.communication.canal.decoder.CommunicationDecoder;
import fr.antifirewall.communication.canal.decoder.SimpleCommunicationDecoder;
import fr.antifirewall.communication.canal.encoder.CommunicationEncoder;
import fr.antifirewall.communication.canal.encoder.SimpleCommunicationEncoder;
import fr.antifirewall.util.LogManager;

/**
 * Listener interceptant les connexions et communicant directement
 * avec le serveur sans encodage
 * @author Kawets
 */
public class SimpleListener extends Thread implements Listener {

    private static Logger logger = LogManager.getLogger(SimpleListener.class.getName());

    /**
	 * Instance de la classe contenant la configuration du client
	 */
    Config config = null;

    /**
	 * Index de la configuration du listener
	 */
    int index = -1;

    public SimpleListener() {
        config = Config.getInstance();
    }

    public void utiliseConfiguration(int index) {
        logger.debug("Configuration " + index + " : " + config.getListener(index));
        this.index = index;
    }

    /**
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(config.getListener(index).getLocalPort());
        } catch (IOException e) {
            logger.error("Impossible d'ouvrir le port.", e);
            return;
        }
        while (true) {
            logger.debug("Listener " + index + " en attente de connexion.");
            try {
                Socket socket = listeningSocket.accept();
                logger.debug("Listener " + index + " connexion accept�e.");
                Connexion connexionClient = new SimpleConnexion(socket);
                logger.debug("Ouverture d'une connexion simple vers " + config.getServeur() + ":" + config.getPortServeur());
                Socket serveurSocket = new Socket(config.getServeur(), config.getPortServeur());
                CommunicationDecoder serveurDecoder = new SimpleCommunicationDecoder();
                CommunicationEncoder serveurEncoder = new SimpleCommunicationEncoder();
                serveurDecoder.initialise(serveurSocket.getInputStream(), serveurSocket.getOutputStream());
                serveurEncoder.initialise(serveurSocket.getInputStream(), serveurSocket.getOutputStream());
                logger.debug("Mise en relation du client et du serveur");
                ConnexionControler controler = new ConnexionControler(serveurDecoder, serveurEncoder, connexionClient);
                controler.run();
            } catch (IOException e) {
                logger.error(e);
                return;
            }
        }
    }
}
