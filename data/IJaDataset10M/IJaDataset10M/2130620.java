package org.mbari.vcr.udp02;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mbari.movie.Timecode;
import org.mbari.util.NumberUtilities;
import org.mbari.vcr.IVCRReply;
import org.mbari.vcr.IVCRUserbits;
import org.mbari.vcr.VCRAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This VCR implementation is for talking to a generic UDP based VCR proxy. It
 * sends 2 command:<br/>
 *
 * <b>timecode</b> -> proxy should respond with an ASCII timecode formatted as 'hh:mm:ss:ff'<br/>
 * <b>time</b> -> proxy should respond with an 4-byte little-endian int. The int value
 * should be epoch seconds (i.e. seconds since 1970-01-01 00:00:00 UTC)
 * @author brian
 */
public class VCR extends VCRAdapter {

    public static final byte[] REQUEST_TIMECODE = "timecode\r\n".getBytes();

    public static final byte[] REQUEST_TIME = "time\r\n".getBytes();

    private static final Pattern timePattern = Pattern.compile("\\d+");

    private static final Pattern timecodePattern = Pattern.compile("[0-2][0-9]:[0-5][0-9]:[0-5][0-9]:[0-2][0-9]");

    private static final Logger log = LoggerFactory.getLogger(VCR.class);

    private byte[] receiveMessage = new byte[1024];

    private final DatagramPacket incomingPacket;

    private final InetAddress inetAddress;

    private final int port;

    private final DatagramPacket requestTimePacket;

    private final DatagramPacket requestTimecodePacket;

    private DatagramSocket socket;

    /**
     * Creates a new instance of VCR
     *
     * @param port
     *
     * @throws SocketException
     * @throws UnknownHostException
     */
    public VCR(int port) throws UnknownHostException, SocketException {
        vcrReply = new VCRReply();
        this.port = port;
        inetAddress = InetAddress.getByAddress(new byte[] { (byte) 255, (byte) 255, (byte) 255, (byte) 255 });
        incomingPacket = new DatagramPacket(receiveMessage, receiveMessage.length);
        requestTimecodePacket = new DatagramPacket(REQUEST_TIMECODE, REQUEST_TIMECODE.length, inetAddress, port);
        requestTimePacket = new DatagramPacket(REQUEST_TIME, REQUEST_TIME.length, inetAddress, port);
        requestTimeCode();
    }

    @Override
    public void disconnect() {
        if (!socket.isClosed()) {
            socket.close();
        }
        getVcrTimecode().getTimecode().setTime(0, 0, 0, 0);
        ((VCRState) getVcrState()).setPlaying(false);
        ((VCRState) getVcrState()).setConnected(false);
        super.disconnect();
    }

    @Override
    public void kill() {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (!socket.isClosed()) {
            socket.close();
        }
        super.finalize();
    }

    /**
     * Returns a name for this connection
     */
    @Override
    public String getConnectionName() {
        DatagramSocket s = null;
        String connectionName = "Not connected";
        try {
            s = getSocket();
            connectionName = "UDP Broadcast:" + s.getPort();
        } catch (SocketException ex) {
            log.error("Failed to open a DatagramSocket", ex);
        }
        return connectionName;
    }

    public InetAddress getInetAddress() {
        return requestTimecodePacket.getAddress();
    }

    public int getPort() {
        return requestTimecodePacket.getPort();
    }

    /**
         * @return  the socket
         * @uml.property  name="socket"
         */
    private DatagramSocket getSocket() throws SocketException {
        if (socket == null) {
            socket = new DatagramSocket(port);
            socket.setSoTimeout(500);
        }
        return socket;
    }

    @Override
    public IVCRReply getVcrReply() {
        return vcrReply;
    }

    private void handleTimeResponse(String result) {
        IVCRUserbits userbits = getVcrUserbits();
        if (result != null) {
            try {
                Matcher matcher = timePattern.matcher(result);
                if (matcher.find()) {
                    userbits.setUserbits(NumberUtilities.toByteArray(Integer.parseInt(matcher.group(0)), true));
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Problem with parsing userbits '" + result + "'", e);
                }
            }
        }
    }

    private void handleTimecodeResponse(String result) {
        Timecode timecode = getVcrTimecode().getTimecode();
        if (result != null) {
            try {
                Matcher matcher = timecodePattern.matcher(result);
                if (matcher.find()) {
                    timecode.setTimecode(matcher.group(0));
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Problem with parsing timecode '" + result + "'", e);
                }
            }
        }
    }

    @Override
    public void requestLTimeCode() {
        requestTimeCode();
    }

    @Override
    public void requestLUserbits() {
        requestUserbits();
    }

    @Override
    public void requestStatus() {
        ((VCRState) getVcrState()).notifyObservers();
    }

    @Override
    public void requestTimeCode() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> '" + new String(REQUEST_TIMECODE) + "' to " + getConnectionName());
        }
        sendCommand(requestTimecodePacket);
    }

    @Override
    public void requestUserbits() {
        if (log.isDebugEnabled()) {
            log.debug("Sending VCR Command -> '" + new String(REQUEST_TIME) + "' to " + getConnectionName());
        }
        sendCommand(requestTimePacket);
    }

    @Override
    public void requestVTimeCode() {
        requestTimeCode();
    }

    @Override
    public void requestVUserbits() {
        requestUserbits();
    }

    private synchronized void sendCommand(final DatagramPacket packet) {
        byte[] response = null;
        try {
            final DatagramSocket s = getSocket();
            s.send(packet);
            s.receive(incomingPacket);
            s.receive(incomingPacket);
            final int numBytes = incomingPacket.getLength();
            response = new byte[numBytes];
            System.arraycopy(incomingPacket.getData(), 0, response, 0, numBytes);
            if (log.isDebugEnabled()) {
                log.debug("Received bytes: 0x[" + NumberUtilities.toHexString(response) + "]");
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("UDP connection failed.", e);
            }
        }
        String result = null;
        try {
            result = new String(response, "ASCII");
        } catch (UnsupportedEncodingException e) {
            try {
                result = new String(response, "8859_1");
            } catch (UnsupportedEncodingException ex) {
                if (log.isErrorEnabled()) {
                    log.error("Timecode over UDP is using an unknown encoding");
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Unable to get a valid response from " + getConnectionName());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Received Response: '" + result + "'");
        }
        final boolean isTime = result.indexOf(":") == -1;
        if (isTime) {
            handleTimeResponse(result);
        } else {
            handleTimecodeResponse(result);
        }
    }
}
