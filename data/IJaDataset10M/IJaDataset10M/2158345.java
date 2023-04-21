package org.commonmap.turtleeggs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.commonmap.util.ConfigurationListener;
import org.commonmap.util.Main;
import org.commonmap.util.Tile;
import static org.commonmap.util.Translator.tr;

/**
 *
 * @author nazotoko <nazotoko@commonmap.info>
 * @version $Id: TurtleEggs.java 43 2010-02-19 11:03:20Z nazotoko $
 */
public class TurtleEggs extends Main implements Runnable, ConfigurationListener {

    private Distribution dist;

    private ConfigurationTurtleEggs config = null;

    private int port;

    private Thread turtleEggsServer;

    /** TurtleEggs status listener */
    private List<TurtleEggsEventListener> listener;

    /** server */
    private ServerSocket serverSocket = null;

    private int status;

    public static final int STATUS_NOTREADY = 0;

    public static final int STATUS_READY = 1;

    public static final int STATUS_RUN = 2;

    /**
     * TurtleEggs server
     *
     */
    public TurtleEggs() {
        listener = new LinkedList<TurtleEggsEventListener>();
        config = new ConfigurationTurtleEggs();
        try {
            config.load();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, tr("Configureation file cannot be read."), ex.getMessage());
        }
        setPort(Integer.parseInt(config.get("port")));
        dist = new Distribution(config);
        config.addListener(this);
    }

    public int getStatus() {
        return status;
    }

    private void setStatus(int s) {
        status = s;
        for (TurtleEggsEventListener li : listener) {
            li.turtleEggsStatusChanged(s);
        }
    }

    public void setPort(int port) {
        this.port = port;
        if (port != 0) {
            setStatus(STATUS_READY);
        } else {
            setStatus(STATUS_NOTREADY);
            logger.log(Level.CONFIG, tr("Configure port number to open."));
        }
    }

    public int getPort() {
        return port;
    }

    public Distribution getDist() {
        return dist;
    }

    public void addEventActionListener(TurtleEggsEventListener l) {
        listener.add(l);
    }

    public void start() {
        if (status != STATUS_READY) {
            logger.log(Level.INFO, "not ready");
            return;
        }
        setStatus(STATUS_RUN);
        turtleEggsServer = new Thread(this, "TurtleEggs");
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not listen on port: " + port);
            setStatus(STATUS_READY);
            return;
        }
        logger.log(Level.INFO, "Serving start");
        turtleEggsServer.start();
        try {
            Thread.sleep(1000);
            if (!pingpong()) {
                stop();
            } else {
                logger.log(Level.INFO, "Connected to TurtleEggs tracker.");
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Program ERROR", ex);
        }
    }

    public void stop() {
        if (status == STATUS_RUN) {
            turtleEggsServer.interrupt();
            ping();
        }
    }

    /** Send ping to the localhost.
     * @return ture if TurtleEggs is alived.
     */
    public boolean ping() {
        try {
            URL url = new URL("http://localhost:" + port + "/?ping");
            url.openStream();
        } catch (MalformedURLException e) {
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    private boolean pingpong() {
        InputStream is = null;
        boolean ret = false;
        int id;
        String temp;
        try {
            if ((temp = config.get("id")) != null) {
                id = Integer.parseInt(temp);
            } else {
                id = 0;
            }
        } catch (NumberFormatException ex) {
            id = 0;
        }
        try {
            URL url = new URL(config.get("pingURL") + "?id=" + id + "&port=" + config.get("port"));
            is = url.openStream();
            String ss = "";
            byte[] s = new byte[256];
            int i = 0;
            while ((i = is.read(s, 0, 256)) >= 0) {
                ss += new String(s, 0, i);
            }
            if (ss.startsWith("Welcome to TurtleEggs")) {
                ret = true;
                if (id == 0) {
                    i = ss.indexOf("new id: ");
                    int j = ss.indexOf("\n", i + 1);
                    id = Integer.parseInt(ss.substring(i + 8, j));
                    config.put("id", Integer.toString(id));
                }
            } else {
                logger.log(Level.WARNING, "Ping-Pong Failed: " + ss);
            }
            is.close();
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "ERROR: check pingURL in config.txt");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "check internet connection");
        }
        return ret;
    }

    private void stopToTracker() throws IOException {
        InputStream is = null;
        try {
            URL url = new URL(config.get("stopURL") + "?id=" + config.get("id"));
            is = url.openStream();
            byte[] s = new byte[256];
            int i = 0;
            while ((i = is.read(s, 0, 256)) >= 0) {
            }
            is.close();
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, "ERROR: check stopURL in config.txt");
        }
    }

    @Override
    public void run() {
        try {
            routin();
        } catch (IOException ex) {
        }
        try {
            stopToTracker();
            serverSocket.close();
        } catch (IOException ex) {
        }
        logger.log(Level.INFO, "Serving stopped");
        setStatus(STATUS_READY);
    }

    private void routin() throws IOException {
        Socket clientSocket = null;
        DataOutputStream out;
        BufferedReader in;
        String firstLine;
        String tmp;
        String path;
        int a, b;
        while (!turtleEggsServer.isInterrupted()) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Cannot open server socket.", ex);
                return;
            }
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            firstLine = in.readLine();
            tmp = new String(firstLine).toUpperCase();
            int method = 0;
            if (tmp.startsWith("GET")) {
                method = 1;
            } else if (tmp.startsWith("HEAD")) {
                method = 2;
            }
            a = firstLine.indexOf(" ");
            b = firstLine.indexOf(" ", a + 1);
            if (b < 0) {
                b = firstLine.length();
            }
            path = firstLine.substring(a + 1, b);
            while ((tmp = in.readLine()) != null) {
                if (tmp.length() == 0) {
                    break;
                }
            }
            logger.log(Level.INFO, "From: " + clientSocket.getRemoteSocketAddress() + " request: " + path);
            if (method == 0) {
                response(501, out);
            } else {
                construct_contents(path, out);
            }
            out.flush();
            out.close();
        }
    }

    private void construct_contents(String s, DataOutputStream out) throws FileNotFoundException, IOException {
        Tile t = new Tile();
        File f;
        FileInputStream fis;
        try {
            if (s.equals("/?ping")) {
                response(200, out);
                file_type_header(0, out);
                out.write("pong".getBytes());
                return;
            }
            if (t.fromCode(s)) {
                fis = dist.getFromLocal(t);
            } else {
                response(404, out);
                return;
            }
        } catch (NumberFormatException ex) {
            response(404, out);
            return;
        } catch (FileNotFoundException ex) {
            response(404, out);
            return;
        }
        response(200, out);
        file_type_header((t.getLayer().equals("data")) ? 3 : 2, out);
        byte[] buf = new byte[16384];
        int len;
        while ((len = fis.read(buf)) >= 0) {
            out.write(buf, 0, len);
        }
        fis.close();
    }

    private void response(int return_code, DataOutputStream out) throws IOException {
        out.write(new byte[] { 'H', 'T', 'T', 'P', '/', '1', '.', '0', ' ' });
        switch(return_code) {
            case 200:
                out.write("200 OK".getBytes());
                break;
            case 400:
                out.write("400 Bad Request".getBytes());
                break;
            case 403:
                out.write("403 Forbidden".getBytes());
                break;
            case 404:
                out.write("404 Not Found".getBytes());
                break;
            case 500:
                out.write("500 Internal Server Error".getBytes());
                break;
            case 501:
                out.write("501 Not Implemented".getBytes());
                break;
        }
        out.write(new byte[] { '\r', '\n' });
        out.write("Connection: close\r\n".getBytes());
        out.write("Server: false map TurtleEggs (phase 0)\r\n".getBytes());
        if (return_code != 200) {
            out.write(("\r\n " + return_code + " Error").getBytes());
        }
    }

    private void file_type_header(int file_type, DataOutputStream out) throws IOException {
        String s;
        switch(file_type) {
            case 0:
                s = "Content-Type: text/plain-text\r\n";
                break;
            case 1:
                s = "Content-Type: image/jpeg\r\n";
                break;
            case 2:
                s = "Content-Type: image/png\r\n";
                break;
            case 3:
                s = "Content-Type: application/x-zip-compressed\r\n";
                break;
            default:
                s = "Content-Type: text/html\r\n";
                break;
        }
        s = s + "\r\n";
        out.write(s.getBytes());
    }

    public void preexit() {
        if (status == STATUS_RUN) {
            stop();
        }
        try {
            config.save();
            if (status == STATUS_RUN) {
                stopToTracker();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "TurtleEggs: config file cannot be saved.");
        }
    }

    public String getID() {
        return config.get("id");
    }

    public ConfigurationTurtleEggs getConfig() {
        return config;
    }

    public void configurationChanged(String key) {
        if (key.equals("port")) {
            setPort(Integer.parseInt(config.get("port")));
        }
    }
}
