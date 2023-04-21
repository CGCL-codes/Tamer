package org.commonmap.cmarender;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.batik.transcoder.TranscoderException;
import org.commonmap.turtleeggs.Distribution;
import org.commonmap.cmarender.gui.RenderMainFrame;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.commonmap.gui.ConfigurationPanel;
import org.commonmap.turtleeggs.TurtleEggs;
import org.commonmap.util.ConfigurationListener;
import org.commonmap.util.Main;
import org.commonmap.util.Tile;

/**
 *
 * @author nazotoko
 * @version $Id: Cmarender.java 48 2010-02-24 00:29:31Z nazotoko $
 */
public class Cmarender extends Main implements Runnable, ConfigurationListener {

    private ConfigurationCmarender config;

    private Request req;

    private Distribution dist = null;

    private TurtleEggs turtleEggs = null;

    private RenderMainFrame mainFrame = null;

    private boolean loop = false;

    private Thread mainloop = null;

    private List<CmarenderEventListener> listeners = new LinkedList<CmarenderEventListener>();

    private int status = 0;

    public static final int STATUS_NOTREADY = 1;

    public static final int STATUS_READY = 2;

    public static final int STATUS_RUN = 3;

    public Cmarender() {
        turtleEggs = new TurtleEggs();
        try {
            mainFrame = new RenderMainFrame(this, turtleEggs);
        } catch (HeadlessException ex) {
        }
        dist = turtleEggs.getDist();
        config = new ConfigurationCmarender();
        try {
            config.load();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO ERROR when loading config.txt of cmarender", ex);
        }
        try {
            req = new Request(this, config, logger);
        } catch (TransformerConfigurationException ex) {
            logger.log(Level.SEVERE, "ERROR: Check Saxon", ex);
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, "ERROR: Check batik", ex);
        }
        config.addListener(this);
        if (config.get("username") != null && config.get("password") != null && config.get("username").length() != 0 && config.get("password").length() != 0) {
            setStatus(STATUS_READY);
        }
    }

    /**　main of cmarender. If you start cmarender from commnand line, it is called.
     *  This main thread is soon to end.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cmarender cmr = new Cmarender();
        if (args.length > 0 && args[0].equals("xy")) {
            cmr.debugMode(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } else if (cmr.mainFrame == null) {
            cmr.start();
        }
    }

    /**
     * Main looping start
     */
    public void start() {
        setStatus(STATUS_RUN);
        mainloop = new Thread(this, "cmarender loop");
        mainloop.start();
    }

    @Override
    public void run() {
        loop = true;
        try {
            while (loop) {
                if (req.take()) {
                    do {
                        req.routin(dist);
                    } while (!((turtleEggs.getStatus() != TurtleEggs.STATUS_RUN) ? req.upload(dist) : req.update(turtleEggs)));
                }
                if (loop) {
                    logger.log(Level.INFO, "Sleep about 10 second to give you timing to stop this process.");
                    System.gc();
                    Thread.sleep(10000);
                }
            }
        } catch (TransformerException ex) {
            logger.log(Level.SEVERE, "TransformerException " + ex);
        } catch (TranscoderException ex) {
            logger.log(Level.SEVERE, "TranscoderException " + ex);
        } catch (InterruptedException ex) {
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IO Error: server is down?:" + ex.getMessage());
        }
        logger.log(Level.INFO, "Loop is stopped!");
        setStatus(STATUS_READY);
    }

    private void debugMode(int x, int y, int z) {
        Tile t = new Tile();
        t.setLayer("tile");
        t.setZXY(z, x, y);
        try {
            req.setRequest(t);
            req.routin(dist);
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Interrupted Exception:" + ex.getLocalizedMessage());
        } catch (TranscoderException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * call before exit;
     */
    public void preexit() {
        stop();
        turtleEggs.preexit();
        try {
            config.save();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "cmarender: config file cannot be saved.");
        }
        if (status == STATUS_RUN) {
            logger.log(Level.SEVERE, "Waiting for the end of the job. (Max 60 seconds)");
            try {
                mainloop.join(60000);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * stop the 
     */
    public synchronized void stop() {
        if (status == STATUS_RUN) {
            loop = false;
            mainloop.interrupt();
        }
    }

    public void addEventListener(CmarenderEventListener l) {
        listeners.add(l);
    }

    private void setStatus(int s) {
        status = s;
        for (CmarenderEventListener cmrl : listeners) {
            cmrl.cmarenderStatusChanged(s);
        }
    }

    @Override
    public void addLogHandler(Handler lh) {
        logger.addHandler(lh);
        logger.setUseParentHandlers(false);
    }

    public ConfigurationCmarender getConfig() {
        return config;
    }

    public void configurationChanged(String key) {
        if (status != STATUS_RUN && config.get("username") != null && config.get("password") != null && config.get("username").length() != 0 && config.get("password").length() != 0) {
            setStatus(STATUS_READY);
        }
    }
}
