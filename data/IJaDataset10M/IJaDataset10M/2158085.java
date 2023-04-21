package net.sf.dz.view.udp.producer;

import net.sf.dz.Model;
import net.sf.dz.device.model.Thermostat;
import net.sf.dz.device.model.Unit;
import net.sf.dz.event.ThermostatListener;
import net.sf.dz.util.HostHelper;
import net.sf.dz.view.View;
import net.sf.jukebox.conf.Configuration;
import net.sf.jukebox.service.PassiveService;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * The xAP server view.
 *
 * Presents the {@link Model model} to the remote.
 *
 * <p>
 *
 * This object represents a simplified view at the system - at the moment of
 * writing, the only things exposed are the {@link net.sf.dz.device.model
 * virtual thermostats}.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2005
 * @version $Id: UdpBroadcaster.java,v 1.3 2007-03-01 21:34:29 vtt Exp $
 */
public abstract class UdpBroadcaster extends PassiveService implements View, ThermostatListener {

    /**
     * The model to reflect on.
     */
    private Model model;

    /**
     * Socket port to broadcast on.
     *
     * Default value is 3639 - reserved for xAP.
     */
    private int port = 3639;

    /**
     * Socket to broadcast to.
     */
    private DatagramSocket socket;

    /**
     * Host name.
     *
     * It is resolved at {@link #startup}, to avoid repetitive invocations.
     */
    private String hostname;

    protected final String getSource() {
        return hostname;
    }

    /**
     * {@inheritDoc}
     */
    public final synchronized void attach(Model model) {
        if (model == null) {
            throw new IllegalArgumentException("Model can't be null");
        }
        if (this.model != null) {
            throw new IllegalStateException("Already attached to the model");
        }
        this.model = model;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure() throws Throwable {
        if (model == null) {
            throw new IllegalStateException("Model must be attached first");
        }
        Configuration cf = getConfiguration();
        String cfroot = getConfigurationRoot();
        port = cf.getInteger(cfroot + ".udp.port", getDefaultPort());
    }

    protected abstract int getDefaultPort();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void startup() throws Throwable {
        getConfiguration();
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        hostname = InetAddress.getLocalHost().getHostName();
        int dotOffset = hostname.indexOf(".");
        if (dotOffset != -1) {
            hostname = hostname.substring(0, dotOffset);
        }
        logger.info("Using host name: " + hostname);
        for (Iterator<? extends Unit> i = model.iterator(); i.hasNext(); ) {
            Unit unit = i.next();
            for (Iterator<? extends Thermostat> i2 = unit.iterator(); i2.hasNext(); ) {
                Thermostat ts = i2.next();
                ts.addListener(this);
                logger.info("Broadcasting " + ts.getName());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final synchronized void shutdown() throws Throwable {
        socket.close();
        socket = null;
        for (Iterator<? extends Unit> i = model.iterator(); i.hasNext(); ) {
            Unit unit = i.next();
            for (Iterator<? extends Thermostat> i2 = unit.iterator(); i2.hasNext(); ) {
                Thermostat ts = i2.next();
                logger.debug("Not listening to " + ts.getName() + " any longer");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public final void controlSignalChanged(Thermostat source, Object signal) {
        broadcast(source);
    }

    /**
     * {@inheritDoc}
     */
    public final void enabledStateChanged(Thermostat source, boolean enabled) {
        broadcast(source);
    }

    /**
     * {@inheritDoc}
     */
    public final void votingStateChanged(Thermostat source, boolean voting) {
        broadcast(source);
    }

    /**
     * {@inheritDoc}
     */
    public final void holdStateChanged(Thermostat source, boolean hold) {
        broadcast(source);
    }

    /**
     * Broadcast a notification about a thermostat status.
     *
     * @param source Thermostat whose status needs to be broadcast.
     */
    private synchronized void broadcast(Thermostat source) {
        StringBuffer sb = new StringBuffer();
        writeHeader(sb);
        writeBlock(sb, source);
        String buffer = sb.toString();
        try {
            send(buffer);
        } catch (Throwable t) {
            logger.warn("Unable to send():", t);
        }
    }

    protected final String renderTimestamp() {
        Date now = new Date();
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            return df.format(now);
        } catch (Throwable t) {
            return now.toString();
        }
    }

    /**
     * Write a protocol header.
     *
     * @param sb String buffer to write the header to.
     */
    protected abstract void writeHeader(StringBuffer sb);

    /**
     * Write an xAP block representing the thermostat status.
     *
     * @param sb Buffer to write to.
     *
     * @param ts Thermostat to render.
     */
    protected abstract void writeBlock(StringBuffer sb, Thermostat ts);

    private void send(String message) throws IOException {
        logger.debug("Packet:\n" + message);
        byte[] data = message.getBytes();
        for (Iterator<InetAddress> i = HostHelper.getLocalAddresses().iterator(); i.hasNext(); ) {
            InetAddress address = i.next();
            DatagramPacket packet = new DatagramPacket(data, message.length(), address, port);
            socket.send(packet);
            logger.debug("Sent packet to " + address + ":" + port);
        }
    }
}
