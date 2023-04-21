package fr.antifirewall.client;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import fr.antifirewall.client.service.Listener;
import fr.antifirewall.client.service.ListenerManager;
import fr.antifirewall.util.LogManager;

/**
 * @author Kawets
 */
public class Launcher {

    static Logger logger = LogManager.getLogger("Launcher");

    Config config = null;

    public Launcher(String fichierConfig) {
        configure(fichierConfig);
    }

    public void run() {
        int nbListeners = config.getListeners().size();
        for (int i = 0; i < nbListeners; i++) {
            logger.info("Demarrage du listener " + (i + 1));
            Listener listener = ListenerManager.getListener(i);
            new Thread(listener).start();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Launcher <fichier config>");
            System.exit(1);
        }
        Launcher launcher = new Launcher(args[0]);
        launcher.run();
    }

    public void configure(String nom) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = null;
        try {
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            logger.error("Erreur de configuration du parser :" + e.toString());
            System.exit(2);
        } catch (SAXException e) {
            logger.error("Erreur lors de la cr�ation du parser XML :" + e.toString());
            System.exit(2);
        }
        File fichier = new File(nom);
        if (!fichier.exists() && !fichier.isFile()) {
            logger.error("Le fichier " + nom + " n'existe pas.");
            System.out.println("Le fichier " + nom + " n'existe pas.");
            System.exit(2);
        }
        config = Config.getInstance();
        try {
            parser.parse(fichier, config);
        } catch (SAXException e) {
            logger.error("Erreur lors de la lecture du fichier :" + e.toString());
            System.exit(2);
        } catch (IOException e) {
            logger.error("Erreur lors de la lecture du fichier :" + e.toString());
            System.exit(2);
        }
    }
}
