package exformation.net;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import exformation.core.BaseClass;
import processing.core.PApplet;

public class XMLSocket extends BaseClass implements PropertyChangeListener {

    private static final String METHOD = "onXMLPacketReceived";

    /**
	 * The last XMLPacket received from the server
	 */
    public XMLPacket lastPacket;

    private PApplet parent;

    private XMLPacketParser parser;

    private XMLClient client;

    public XMLSocket(PApplet parent, XMLPacketParser parser, String url, int port) {
        client = new XMLClient(url, port);
        this.parser = parser;
        this.parent = parent;
        getListenerMethod();
        client.addPropertyChangeListener(this);
        parent.registerPre(this);
    }

    public XMLSocket(PApplet parent, String url, int port) {
        this(parent, new XMLPacketParser(), url, port);
    }

    public void pre() {
        client.run();
    }

    private Method getListenerMethod() {
        Method m = null;
        try {
            m = parent.getClass().getMethod(METHOD);
        } catch (NoSuchMethodException e) {
            String msg = "To use XMLSocket you should implement the method '" + METHOD + "' in your PApplet root";
            System.err.println(msg);
            throw (new RuntimeException(msg));
        }
        return m;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        try {
            if (evt.getPropertyName().equals(XMLClient.NEW_MESSAGE)) {
                client.parseXML(parser, evt.getNewValue().toString());
                lastPacket = parser.packet;
                getListenerMethod().invoke(parent);
            }
        } catch (Throwable any) {
            debug(any);
        }
    }
}
