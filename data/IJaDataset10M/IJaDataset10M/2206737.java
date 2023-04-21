package uk.ac.ebi.intact.view.webapp;

import org.h2.tools.Server;
import uk.ac.ebi.intact.dataexchange.psimi.solr.server.SolrJettyRunner;
import java.io.File;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
public class StartH2Server {

    public static void main(String[] args) throws Exception {
        System.out.println("////////////////////////////");
        System.out.println("Starting H2 database...");
        final Server server = Server.createTcpServer("-tcpPort", "39101", "-baseDir", "target", "-tcpDaemon");
        server.start();
        System.out.println("URL: " + server.getURL());
        System.out.println("Port: " + server.getPort());
        System.out.println("Status: " + server.getStatus());
        System.out.println("Type: " + server.getService().getType());
        System.out.println("Allow others: " + server.getService().getAllowOthers());
        System.out.println("////////////////////////////");
    }
}
