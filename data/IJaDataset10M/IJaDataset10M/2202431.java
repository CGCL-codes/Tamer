package net.wsnware.platform.tinyos2.jio;

import net.wsnware.core.utils.Utils;
import java.util.Dictionary;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

/**
 * TODO HERE
 *
 * @author  Alessandro Polo <contact@alessandropolo.name>
 * @version 1.0.0
 * @date    2011-05-17
 */
public class TinyPacketSink extends net.wsnware.core.utils.MessageListenerDefault {

    public static final String ComponentPID = "wsnware.platform.tinyos2.sink.packet";

    public TinyPacketSink() {
        pAdapter = new TinyPacketBridge(Logger.getLogger(this.toString()));
        initDefaults();
    }

    public TinyPacketSink(String pid) {
        pAdapter = new TinyPacketBridge(Logger.getLogger(pid));
        initDefaults();
    }

    public TinyPacketSink(final Logger handlerLog) {
        pAdapter = new TinyPacketBridge(handlerLog);
        initDefaults();
    }

    public TinyPacketSink(final TinyPacketBridge pAdapter) {
        this.pAdapter = pAdapter;
        initDefaults();
    }

    private void initDefaults() {
    }

    protected final TinyPacketBridge pAdapter;

    public TinyPacketBridge getpAdapter() {
        return pAdapter;
    }

    @Override
    public void messageReceived(net.wsnware.core.Message msg, Object source) {
        pAdapter.logger.log(Level.FINE, "Received WSNWARE Message..");
        byte[] bytes = null;
        try {
            bytes = pAdapter.getMessageAdapter().toPacket(msg);
        } catch (Exception ex) {
            pAdapter.logger.log(Level.SEVERE, "Error Creating packet..", ex);
            return;
        }
        if (bytes == null) {
            pAdapter.logger.log(Level.WARNING, "Unable to create packet: {0}", msg.toString(true));
            return;
        }
        try {
            pAdapter.phoenixSource.writePacket(bytes);
        } catch (IOException ex) {
            pAdapter.logger.log(Level.SEVERE, "Error Sending packet..", ex);
            ex.printStackTrace();
        }
    }

    protected boolean reconfigureBridge = true;

    @Override
    public void reconfigure(Dictionary properties) {
        if (!reconfigureBridge) return;
        pAdapter.reconfigure(properties);
    }

    @Override
    public Dictionary getConfiguration() {
        if (!reconfigureBridge) return null;
        return pAdapter.getConfiguration();
    }
}
