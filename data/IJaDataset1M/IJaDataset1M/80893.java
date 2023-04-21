package com.cidero.bridge.shoutcast;

import java.util.logging.Logger;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import com.cidero.bridge.MediaRendererException;
import com.cidero.bridge.MediaRendererBridge;
import com.cidero.upnp.AVTransport;
import com.cidero.upnp.ConnectionManager;
import com.cidero.upnp.RenderingControl;

/**
 * This class contains the Shoutcast implementation of the 
 * AbstractMediaRenderer* interface. Since there is no way to
 * control devices listing to the shoutcast stream, most of
 * the methods in AbstractMediaRendere are not implemented, 
 * and the methods default to the no-ops in the base class.
 * The exception is the getFriendlyName method, which is implemented.
 *
 */
public class ShoutcastMediaRenderer extends MediaRendererBridge {

    private static Logger logger = Logger.getLogger("com.cidero.bridge.shoutcast");

    private static final String DESCRIPTION_FILE_NAME = "com/mediarush/bridge/shoutcast/description/MediaRenderer.xml";

    /**
   * Constructor
   *
   * @param  friendlyName     Friendly name (e.g. 'HomeCast')
   *                          This is assigned in property file
   */
    public ShoutcastMediaRenderer(String friendlyName) throws InvalidDescriptionException {
        super(DESCRIPTION_FILE_NAME, "UnknownAddr", friendlyName);
        logger.fine("ShoutcastMediaRenderer constructor: Entered");
        getProperties();
    }

    /** 
   *  Get properties from propery file 
   */
    public void getProperties() {
        logger.fine("Loading properties for shoutcast renderer");
    }

    public String getProxyUrlPath() {
        return "/Shoutcast/" + getFriendlyName();
    }

    public RenderingControl getRenderingControl() {
        return null;
    }

    public AVTransport getAVTransport() {
        return null;
    }

    public ConnectionManager getConnectionManager() {
        return null;
    }

    public void avTransportSetTransportURI(String uri) throws MediaRendererException {
    }

    public void avTransportPlay(String speed) throws MediaRendererException {
    }

    public void avTransportPause() throws MediaRendererException {
    }

    public void avTransportStop() throws MediaRendererException {
    }
}
