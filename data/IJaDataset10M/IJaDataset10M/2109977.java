package factory;

import java.io.InputStream;
import javax.xml.parsers.SAXParserFactory;
import model.PlayerFactory;
import network.Hotseat;
import network.NetworkLayer;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author barkholt
 * 
 */
public class NetworkFactory {

    private NetworkParser networkParser;

    private final PlayerFactory playerFactory;

    public NetworkFactory(InputStream stream, PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
        networkParser = new NetworkParser();
        try {
            SAXParserFactory.newInstance().newSAXParser().parse(stream, networkParser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NetworkLayer getNetworkLayer() {
        return networkParser.getNetworkLayer();
    }

    private class NetworkParser extends DefaultHandler {

        private static final String NETWORK = "network";

        private static final String HOTSEAT = "hotseat";

        private NetworkLayer networkLayer;

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals(NETWORK)) {
                networkLayer = new NetworkLayer(playerFactory);
            } else if (qName.equals(HOTSEAT)) {
                networkLayer.setTransportLayer(new Hotseat());
            }
        }

        public NetworkLayer getNetworkLayer() {
            return networkLayer;
        }
    }
}
