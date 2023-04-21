package net.majorkernelpanic.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * 
 *   RtspServer (RFC 2326)
 *   One client handled at a time only
 * 
 */
public class RtspServer extends Thread implements Runnable {

    private final String TAG = "RTSPServer";

    private static final String STATUS_OK = "200 OK";

    private static final String STATUS_BAD_REQUEST = "400 Bad Request";

    private static final String STATUS_NOT_FOUND = "404 Not Found";

    public static final int MESSAGE_H264_TEST = 1;

    public static final int MESSAGE_LOG = 2;

    public static final int MESSAGE_START = 3;

    public static final int MESSAGE_STOP = 4;

    private ServerSocket server = null;

    private Socket client = null;

    private InputStream is = null;

    private OutputStream os = null;

    private Handler handler = null;

    private String request, response;

    private byte[] buffer = new byte[4096];

    private int port, seqid = 1;

    public StreamingManager streamingManager = new StreamingManager();

    public RtspServer(int port, Handler handler) {
        this.port = port;
        this.handler = handler;
    }

    public void run() {
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            log(e.getMessage());
            return;
        }
        while (handleClient()) ;
    }

    public boolean handleClient() {
        int len = 0;
        try {
            client = server.accept();
            is = client.getInputStream();
            os = client.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            log(e.getMessage());
            return true;
        }
        streamingManager.setDestination(getClientAddress());
        streamingManager.flush();
        log("Connection from " + getClientAddress().getHostAddress());
        while (true) {
            try {
                len = is.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                break;
            }
            if (len <= 0) break;
            request = new String(buffer, 0, len);
            Log.d(TAG, request);
            if (request.startsWith("DESCRIBE")) commandDescribe(); else if (request.startsWith("OPTIONS")) commandOptions(); else if (request.startsWith("SETUP")) commandSetup(); else if (request.startsWith("PLAY")) commandPlay(); else if (request.startsWith("PAUSE")) commandPause(); else if (request.startsWith("TEARDOWN")) {
                commandTeardown();
                break;
            } else commandUnknown();
        }
        handler.obtainMessage(MESSAGE_STOP).sendToTarget();
        streamingManager.stopAll();
        try {
            client.close();
            log("Client disconnected");
        } catch (IOException e) {
            return true;
        }
        return true;
    }

    private void commandDescribe() {
        handler.obtainMessage(MESSAGE_H264_TEST).sendToTarget();
        streamingManager.addAMRNBTrack(MediaRecorder.AudioSource.CAMCORDER, 5004);
    }

    public void h264TestResult(VideoQuality videoQuality, String[] params, SurfaceHolder holder) {
        streamingManager.addH264Track(MediaRecorder.VideoSource.CAMERA, 5006, params, videoQuality, holder);
        respondDescribe();
    }

    private void respondDescribe() {
        boolean error = false;
        String requestContent = streamingManager.getSessionDescriptor();
        String requestAttributes = "Content-Base: " + getServerAddress() + ":" + port + "/\r\n" + "Content-Type: application/sdp\r\n";
        writeHeader(STATUS_OK, requestContent.length(), requestAttributes);
        writeContent(requestContent);
        try {
            streamingManager.prepareAll();
            streamingManager.startAll();
        } catch (IllegalStateException e) {
            error = true;
        } catch (IOException e) {
            error = true;
        } catch (RuntimeException e) {
            error = true;
        }
        if (error) {
            streamingManager.stopAll();
            log("Something went wrong when starting streaming :/");
        } else {
            handler.obtainMessage(MESSAGE_START).sendToTarget();
        }
    }

    private void commandOptions() {
        writeHeader(STATUS_OK, 0, "Public: DESCRIBE,SETUP,TEARDOWN,PLAY,PAUSE\r\n");
        writeContent("");
    }

    private void commandSetup() {
        String p2, p1;
        Pattern p;
        Matcher m;
        int ssrc, trackId;
        p = Pattern.compile("trackID=(\\w+)", Pattern.CASE_INSENSITIVE);
        m = p.matcher(request);
        if (!m.find()) {
            writeHeader(STATUS_BAD_REQUEST, 0, "");
            writeContent("");
            return;
        }
        trackId = Integer.parseInt(m.group(1));
        if (!streamingManager.trackExists(trackId)) {
            writeHeader(STATUS_NOT_FOUND, 0, "");
            writeContent("");
            return;
        }
        p = Pattern.compile("client_port=(\\d+)-(\\d+)", Pattern.CASE_INSENSITIVE);
        m = p.matcher(request);
        if (!m.find()) {
            int port = streamingManager.getTrackPort(trackId);
            p1 = String.valueOf(port);
            p2 = String.valueOf(port + 1);
        } else {
            p1 = m.group(1);
            p2 = m.group(2);
        }
        ssrc = streamingManager.getTrackSSRC(trackId);
        String attributes = "Transport: RTP/AVP/UDP;unicast;client_port=" + p1 + "-" + p2 + ";server_port=54782-54783;ssrc=" + Integer.toHexString(ssrc) + ";mode=play\r\n" + "Session: " + "1185d20035702ca" + "\r\n" + "Cache-Control: no-cache\r\n";
        writeHeader(STATUS_OK, 0, attributes);
        writeContent("");
    }

    private void commandPlay() {
        String requestAttributes = "RTP-Info: ";
        requestAttributes += "url=rtsp://" + getServerAddress() + ":" + port + "/trackID=" + 0 + ";seq=0;rtptime=0,";
        requestAttributes = requestAttributes.substring(0, requestAttributes.length() - 1) + "\r\nSession: 1185d20035702ca\r\n";
        writeHeader(STATUS_OK, 0, requestAttributes);
        writeContent("");
    }

    private void commandPause() {
        writeHeader(STATUS_OK, 0, "");
        writeContent("");
    }

    private void commandTeardown() {
        writeHeader(STATUS_OK, 0, "");
        writeContent("");
    }

    private void commandUnknown() {
        Log.e(TAG, "Command unknown: " + request);
        writeHeader(STATUS_BAD_REQUEST, 0, "");
        writeContent("");
    }

    private void writeHeader(String requestStatus, int requestLength, String requestAttributes) {
        boolean match;
        Pattern p = Pattern.compile("CSeq: (\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(request);
        match = m.find();
        if (match) seqid = Integer.parseInt(m.group(1));
        response = "RTSP/1.0 " + requestStatus + "\r\n" + (match ? ("Cseq: " + seqid + "\r\n") : "") + "Content-Length: " + requestLength + "\r\n" + requestAttributes + "\r\n";
    }

    private void writeContent(String requestContent) {
        response += requestContent;
        Log.d(TAG, response);
        try {
            os.write(response.getBytes(), 0, response.length());
        } catch (IOException e) {
        }
    }

    /**
	 * 
	 * @return Returns local address
	 */
    private String getServerAddress() {
        return client.getLocalAddress().getHostAddress();
    }

    /**
	 * 
	 * @return Returns client address
	 */
    private InetAddress getClientAddress() {
        return client.getInetAddress();
    }

    /**
	  * 
	  * @param msg String to send to the UI Thread
	  */
    private void log(String msg) {
        handler.obtainMessage(MESSAGE_LOG, msg).sendToTarget();
    }
}
