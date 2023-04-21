package diuf.diva.hephaistk.recognizers;

import diuf.diva.hephaistk.recognizers.datatypes.AbstractRecognizerDataType;
import diuf.diva.hephaistk.recognizers.datatypes.ClientData;
import diuf.diva.hephaistk.undercity.agents.HephaistkAgent;

public class ClientStringRecognizerConnector extends AbstractRecognizerConnector {

    private String clientName = null;

    public ClientStringRecognizerConnector(String currentClient, HephaistkAgent myAgent) {
        super(currentClient, myAgent, currentClient);
        this.clientName = currentClient;
    }

    @Override
    public Class<? extends AbstractRecognizerDataType> getContentType() {
        return ClientData.class;
    }

    @Override
    public String getModality() {
        return clientName;
    }
}
