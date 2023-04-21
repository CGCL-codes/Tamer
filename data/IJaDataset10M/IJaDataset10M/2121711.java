package com.bitgate.util.rtsp;

import java.util.HashMap;
import com.bitgate.util.constants.Defines;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.services.Worker;

/**
 * This is the Controller class for RTSP Services, handling the playback and control of requests made by an RTSP-connected
 * client.  All connections on RTSP are done using a Keep-Alive method, which means subsequent messages and commands are
 * given in realtime instead of per connection.
 * <p/>
 * This code was never successfully completed, as there were problems with RTSP synchronization, and problems with the
 * JMF library - namely, the JMF project is no longer being worked on.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/rtsp/Controller.java#9 $
 */
public class Controller {

    private long ID;

    private int CSeq;

    private int playerPort, controlPort;

    private Thread transmitterThread;

    private RTSPTransmitter rt;

    /**
     * Creates a controller object for the specified ID.
     *
     * @param ID Identifier.
     */
    public Controller(long ID) {
        this.ID = ID;
        this.CSeq = 0;
        playerPort = 0;
        controlPort = 0;
        transmitterThread = null;
        Debug.debug("Instantiated for ID '" + ID + "'");
    }

    /**
     * Issues a "PLAY" command.
     *
     * @param ww Web worker object.
     * @param file Filename to play.
     * @param docRoot The document root directory.
     * @return boolean True on success, false otherwise.
     */
    public boolean PLAY(Worker ww, String file, String docRoot) {
        String parsedRequest = file.substring(file.indexOf(' ') + 1, file.lastIndexOf(' '));
        String protocol = parsedRequest.substring(0, parsedRequest.indexOf(':'));
        parsedRequest = parsedRequest.substring(7);
        String hostname = parsedRequest.substring(0, parsedRequest.indexOf('/'));
        String filename = parsedRequest.substring(parsedRequest.indexOf('/') + 1);
        if (filename.endsWith("/")) {
            filename = filename.substring(0, filename.lastIndexOf('/') - 1);
        }
        String fileURL = "file:" + docRoot + "/" + filename;
        StringBuffer response = new StringBuffer();
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "'");
        getClientSequence(ww);
        getTransport(ww);
        String contentBaseFilename = new String(filename);
        if (docRoot.endsWith("/")) {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot, "");
        } else {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot + "/", "");
        }
        response.append("RTSP/1.0 200 OK\r\n");
        response.append("CSeq: " + CSeq + "\r\n");
        response.append("Content-Base: rtsp://" + ww.getSocket().getLocalAddress().getHostAddress() + "/\r\n");
        response.append("Range: npt=0-" + rt.getPlaytime() + "\r\n");
        response.append("\r\n");
        Debug.debug("Playing.  Response='" + response + "'");
        ww.getPrintStream().print(response.toString());
        rt.play();
        return true;
    }

    /**
     * Runs a "PLAY" with no response.
     *
     * @param ww Web worker object.
     * @param file Filename to play.
     * @return boolean True on success, false otherwise.
     */
    public boolean PLAY_SILENT(Worker ww, String file, String docRoot) {
        String parsedRequest = file.substring(file.indexOf(' ') + 1, file.lastIndexOf(' '));
        String protocol = parsedRequest.substring(0, parsedRequest.indexOf(':'));
        parsedRequest = parsedRequest.substring(7);
        String hostname = parsedRequest.substring(0, parsedRequest.indexOf('/'));
        String filename = parsedRequest.substring(parsedRequest.indexOf('/') + 1);
        if (filename.endsWith("/")) {
            filename = filename.substring(0, filename.lastIndexOf('/') - 1);
        }
        String fileURL = "file:" + docRoot + "/" + filename;
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "'");
        rt.play();
        return true;
    }

    /**
     * Pauses the current stream.
     *
     * @param ww Web worker object.
     * @param file Filename of the stream to pause.
     * @return boolean True on success, false otherwise.
     */
    public boolean PAUSE(Worker ww, String file, String docRoot) {
        String parsedRequest = file.substring(file.indexOf(' ') + 1, file.lastIndexOf(' '));
        String protocol = parsedRequest.substring(0, parsedRequest.indexOf(':'));
        parsedRequest = parsedRequest.substring(7);
        String hostname = parsedRequest.substring(0, parsedRequest.indexOf('/'));
        String filename = parsedRequest.substring(parsedRequest.indexOf('/') + 1);
        String fileURL = "file:" + docRoot + "/" + filename;
        StringBuffer response = new StringBuffer();
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "'");
        getClientSequence(ww);
        getTransport(ww);
        String contentBaseFilename = new String(filename);
        if (docRoot.endsWith("/")) {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot, "");
        } else {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot + "/", "");
        }
        response.append("RTSP/1.0 200 OK\r\n");
        response.append("CSeq: " + CSeq + "\r\n");
        response.append("Content-Base: rtsp://" + ww.getSocket().getLocalAddress().getHostAddress() + "/\r\n");
        response.append("\r\n");
        ww.getPrintStream().print(response.toString());
        rt.stop();
        return true;
    }

    /**
     * Sends a "PAUSE" with no result returned to the client.
     *
     * @param ww Web worker object.
     * @param file Filename to pause.
     * @return boolean True on success, false otherwise.
     */
    public boolean PAUSE_SILENT(Worker ww, String file, String docRoot) {
        String parsedRequest = file.substring(file.indexOf(' ') + 1, file.lastIndexOf(' '));
        String protocol = parsedRequest.substring(0, parsedRequest.indexOf(':'));
        parsedRequest = parsedRequest.substring(7);
        String hostname = parsedRequest.substring(0, parsedRequest.indexOf('/'));
        String filename = parsedRequest.substring(parsedRequest.indexOf('/') + 1);
        String fileURL = "file:" + docRoot + "/" + filename;
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "'");
        rt.stop();
        return true;
    }

    /**
     * SET PARAMETER is unused.
     */
    public boolean SET_PARAMETER(String option, String value) {
        return false;
    }

    /**
     * GET PARAMETER is unused.
     */
    public boolean GET_PARAMETER(String option) {
        return false;
    }

    /**
     * Sets up a stream for the specified file.
     *
     * @param ww Web worker object.
     * @param file Filename to start.
     * @return boolean True on success, false otherwise.
     */
    public boolean SETUP(Worker ww, String file, String docRoot) {
        Debug.log("file='" + file + "' docRoot='" + docRoot + "'");
        String protocol = "rtsp";
        String hostname = ww.getCurrentRequestedHost();
        String filename = ww.getRequestedDocument();
        String mediaType = "audio";
        HashMap extraData = new HashMap();
        String fileURL = "file:" + docRoot + "/" + filename;
        int trackType = 0;
        if (rt != null) {
            rt.teardown();
            rt = null;
        }
        if (transmitterThread != null) {
            try {
                Debug.debug("Interrupting RTP Transmission thread.");
                transmitterThread.interrupt();
                Debug.debug("Sleeping 1 second to ensure RTP thread interruption.");
                Thread.sleep(1000);
                if (!transmitterThread.interrupted()) {
                    Debug.debug("Unable to interrupt thread; continuing blindly.");
                } else {
                    Debug.debug("RTP Thread interrupted.");
                }
                transmitterThread.destroy();
                Debug.debug("RTP Thread destroyed.");
            } catch (Exception e) {
                Debug.debug("Failed to teardown running thread: " + e.getMessage());
            }
            transmitterThread = null;
        }
        if (mediaType == null || mediaType.equals("")) {
            mediaType = (String) extraData.get("track");
        }
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "', mediaType='" + mediaType + "'");
        getClientSequence(ww);
        getTransport(ww);
        StringBuffer sendBuffer = new StringBuffer();
        if (mediaType.equalsIgnoreCase("audio")) {
            trackType = Defines.TRACK_AUDIO;
        } else if (mediaType.equalsIgnoreCase("video")) {
            trackType = Defines.TRACK_VIDEO;
        }
        try {
            rt = new RTSPTransmitter(trackType, fileURL, ww.getSocket().getInetAddress().getHostAddress(), playerPort, null);
        } catch (Exception e) {
            Debug.debug("Cannot continue: " + e.getMessage());
            return false;
        }
        String contentBaseFilename = new String(filename);
        if (docRoot.endsWith("/")) {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot, "");
        } else {
            contentBaseFilename = contentBaseFilename.replaceFirst(docRoot + "/", "");
        }
        sendBuffer.append("RTSP/1.0 200 OK\r\n");
        sendBuffer.append("CSeq: " + CSeq + "\r\n");
        sendBuffer.append("Session: " + ID + "\r\n");
        sendBuffer.append("Content-Base: rtsp://" + ww.getSocket().getLocalAddress().getHostAddress() + "/\r\n");
        sendBuffer.append("Transport: " + (String) ww.request_headers.get("ORIGINAL_transport") + ";server_port=" + playerPort + "-" + controlPort + "\r\n");
        sendBuffer.append("\r\n");
        transmitterThread = new Thread(rt, "[Nuklees:RTSP] Transmitting RTP to " + ww.getSocket().getInetAddress().toString() + ":" + playerPort + ", ID=" + ID + " - Stateful");
        transmitterThread.start();
        Debug.log("Waiting for transmitter thread to initialize.");
        int tries = 0;
        try {
            if (tries > 10) {
                Debug.log("Initialization not stable after 5 seconds.  May not be a valid transmitter.");
            } else if (tries > 20) {
                Debug.log("Initialization not stable after 10 seconds.  Failing initialization.");
                return false;
            }
            if (!rt.isInitialized()) {
                Thread.sleep(500);
            }
            tries++;
        } catch (Exception e) {
        }
        ww.getPrintStream().print(sendBuffer.toString());
        return true;
    }

    /**
     * Close a currently running stream.
     *
     * @param ww Web worker object.
     * @param file Filename to shutdown
     * @return boolean True on success, false otherwise.
     */
    public boolean TEARDOWN(Worker ww, String file, String docRoot) {
        String parsedRequest = file.substring(file.indexOf(' ') + 1, file.lastIndexOf(' '));
        String protocol = parsedRequest.substring(0, parsedRequest.indexOf(':'));
        parsedRequest = parsedRequest.substring(7);
        String hostname = parsedRequest.substring(0, parsedRequest.indexOf('/'));
        String filename = parsedRequest.substring(parsedRequest.indexOf('/') + 1);
        String fileURL = "file:" + docRoot + "/" + filename;
        StringBuffer response = new StringBuffer();
        Debug.debug("Protocol='" + protocol + "', Host='" + hostname + "', File='" + filename + "', " + "fileURL='" + fileURL + "'");
        getClientSequence(ww);
        getTransport(ww);
        response.append("RTSP/1.0 200 OK\r\n");
        response.append("CSeq: " + CSeq + "\r\n");
        response.append("\r\n");
        rt.teardown();
        rt = null;
        if (transmitterThread != null) {
            try {
                Debug.debug("Interrupting RTP Transmission thread.");
                transmitterThread.interrupt();
                Debug.debug("Sleeping 1 second to ensure RTP thread interruption.");
                Thread.sleep(1000);
                if (!transmitterThread.interrupted()) {
                    Debug.debug("Unable to interrupt thread; continuing blindly.");
                } else {
                    Debug.debug("RTP Thread interrupted.");
                }
                try {
                    transmitterThread.destroy();
                } catch (NoSuchMethodError e) {
                    Debug.debug("No such method 'destroy' in thread: Dying.");
                }
                transmitterThread = null;
                Debug.debug("RTP Thread destroyed.");
            } catch (Exception e) {
                Debug.debug("Failed to teardown running thread: " + e.getMessage());
            }
            transmitterThread = null;
        }
        ww.getPrintStream().print(response.toString());
        return true;
    }

    private void getClientSequence(Worker ww) {
        CSeq = Integer.parseInt((String) ww.request_headers.get("cseq"));
        Debug.debug("Got client sequence #'" + CSeq + "'");
    }

    private void getTransport(Worker ww) {
        if (ww.request_headers.get("transport") != null) {
            String firstHeader = (String) ww.request_headers.get("transport");
            String clientPorts = (String) ww.request_headers.get("transport-client_port");
            if (firstHeader.equalsIgnoreCase("rtp/avp")) {
                Debug.debug("Realtime Transport Protocol (Audio/Video) transport requested.");
            } else if (firstHeader.equalsIgnoreCase("unicast")) {
                Debug.debug("Unicast (TCP) stream requested.");
            }
            if (clientPorts != null) {
                if (clientPorts.indexOf("-") != -1) {
                    playerPort = Integer.parseInt(clientPorts.substring(0, clientPorts.indexOf("-")));
                    controlPort = Integer.parseInt(clientPorts.substring(clientPorts.indexOf("-") + 1));
                } else {
                    playerPort = Integer.parseInt(clientPorts);
                    controlPort = playerPort + 1;
                }
                Debug.debug("Client ports: Play=" + playerPort + " Control=" + controlPort);
            } else {
                Debug.debug("Transport: " + firstHeader);
            }
        } else {
            Debug.debug("Transport may be malformed or not specified.");
        }
    }
}
