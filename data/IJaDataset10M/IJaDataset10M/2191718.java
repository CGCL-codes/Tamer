package StoMpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.SwingWorker;

/**
 *
 * @author espenk
 */
public class ChannelLurker extends SwingWorker<String, Void> {

    private ConfigHandler ch = ConfigHandler.getInstance();

    private LogHandler log = LogHandler.getInstance();

    private Socket mpdSocket = null;

    private PrintWriter out = null;

    private BufferedReader in = null;

    @Override
    protected String doInBackground() throws Exception {
        mpdConn();
        return waitForEvent();
    }

    @Override
    protected void done() {
        String c = null;
        try {
            c = get();
        } catch (Exception ignore) {
            c = ignore.getMessage();
        }
        if (mpdSocket != null) {
            try {
                mpdSocket.close();
            } catch (IOException ex) {
                log.p("MpdListener: Can't close mpd socket? " + ex.getMessage());
            }
        }
    }

    private Boolean mpdConn() throws IOException {
        String server = ch.mpdserver();
        int port = ch.mpdport();
        try {
            mpdSocket = new Socket(server, port);
            out = new PrintWriter(mpdSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(mpdSocket.getInputStream()));
            String s = in.readLine();
            out.println("subscribe stompd");
            s = in.readLine();
        } catch (UnknownHostException e) {
            log.error("ListenerThread failure: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void toMpd(String args) {
        out.println(args);
    }

    private String fromMpd() {
        try {
            return in.readLine();
        } catch (IOException e) {
            log.error(e.getMessage());
            return ("OK");
        }
    }

    private String waitForEvent() {
        if (mpdSocket == null) {
            return null;
        }
        String line = "";
        toMpd("idle message");
        while (!line.equals("OK")) {
            line = fromMpd();
        }
        return line;
    }
}
