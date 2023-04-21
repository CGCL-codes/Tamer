package diuf.diva.hephaistk.undercity.agents;

import java.util.Vector;
import diuf.diva.hephaistk.config.LoggingManager;
import diuf.diva.hephaistk.recognizers.AbstractRecognizerConnector;
import diuf.diva.hephaistk.recognizers.ClientStringRecognizerConnector;
import diuf.diva.hephaistk.recognizers.RecognizerConnection;
import diuf.diva.hephaistk.recognizers.datatypes.ClientData;
import diuf.diva.hephaistk.undercity.HephaisTKAgentDirectory;
import jade.core.behaviours.CyclicBehaviour;

public class ClientAppBehaviour extends CyclicBehaviour implements RecognizerConnection {

    private static final long serialVersionUID = -5149081423137677282L;

    private ClientStringRecognizerConnector connector = null;

    @SuppressWarnings("unused")
    private String currentClient = null;

    public ClientAppBehaviour(String currentClient, HephaistkAgent agent) {
        super();
        this.currentClient = currentClient;
        connector = new ClientStringRecognizerConnector(currentClient, agent);
    }

    @Override
    public void action() {
        Vector<Object> messages = HephaisTKAgentDirectory.getClientMessages();
        if (messages != null) {
            for (Object m : messages) {
                if (m == null) continue;
                LoggingManager.getLogger().debug("ClientAppAgent - got new message: " + m);
                connector.sendDataToPostman(new ClientData(m.toString()));
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LoggingManager.getLogger().error(e);
        }
    }

    public AbstractRecognizerConnector getRecognizerConnector() {
        return connector;
    }
}
