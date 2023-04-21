package org.spamerdos.app;

import java.io.*;
import java.net.*;
import java.util.*;

/**
@author Thomas Cherry jceaser@mac.com
*/
public class SpamDOS implements Observer {

    private Preferences prefs;

    private List downloaders;

    private Timer timer;

    private GUI gui;

    private URL currentUrl = null;

    public SpamDOS() {
        prefs = new Preferences(this);
        init();
        downloaders = new Vector();
        this.timer = new Timer(this);
        if (this.gui != null) {
            this.gui.setStatusView("Ready");
        }
    }

    public SpamDOS(Preferences pref) {
        this.prefs = pref;
        this.prefs.addObserver(this);
        init();
        this.downloaders = new Vector();
        this.timer = new Timer(this);
        if (this.gui != null) {
            this.gui.setStatusView("Ready");
        }
    }

    private void init() {
        int dCount = this.prefs.getThreadCount();
        int mode = this.prefs.getGuiMode();
        switch(mode) {
            case Preferences.GUI_MODE_NONE:
                System.out.println("Command line mode");
                break;
            case Preferences.GUI_MODE_FULL:
            case Preferences.GUI_MODE_SMALL:
                this.gui = new GUI(this.prefs);
                break;
        }
    }

    private void log(String msg) {
        if (this.prefs.isHistoryLogged()) {
            System.err.println("SpamDos Log: " + msg);
            if (this.gui != null) {
                this.gui.appendLog(msg);
            }
        }
    }

    /**
	will accept the following parameters:<br>
	-v verbose<br>
	-g GUI<br>
	-G CLI (default)<br>
	*/
    public static void main(String args[]) {
        boolean verbose = false;
        boolean gui = false;
        boolean vOveride = false;
        boolean gOveride = false;
        SpamDOS spamDOS = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].indexOf("v") > -1) {
                vOveride = true;
                verbose = true;
            } else if (args[i].indexOf("V") > -1) {
                vOveride = true;
                verbose = false;
            } else if (args[i].indexOf("g") > -1) {
                gOveride = true;
                gui = true;
            } else if (args[i].indexOf("G") > -1) {
                gOveride = true;
                gui = false;
            }
        }
        if (!(vOveride || gOveride)) {
            spamDOS = new SpamDOS();
        } else {
            Preferences prefs = new Preferences();
            if (gOveride) {
                prefs.setGuiMode(gui ? 1 : 0);
            }
            spamDOS = new SpamDOS(prefs);
        }
    }

    public void doDownloads(String sUrl) {
        if (sUrl.equals("")) {
            if (this.currentUrl != null) {
                System.out.println("StopDownload");
                this.log("Download Stoped");
                this.downloaders.clear();
                this.currentUrl = null;
            }
        } else if (this.currentUrl == null || !sUrl.equals(this.currentUrl.toString())) {
            this.log("Downloading " + sUrl);
            URL url = null;
            try {
                url = new URL(sUrl);
            } catch (java.net.MalformedURLException mue) {
                mue.printStackTrace();
            }
            if (url != null) {
                this.currentUrl = url;
                for (int i = 0; i < this.prefs.getThreadCount(); i++) {
                    this.downloaders.add(new Downloader(url, this.gui));
                }
            }
        }
    }

    public void update(Observable o, Object arg) {
        if (o == this.prefs) {
            if (arg.equals(Preferences.NOTIFY_ACTIVE)) {
                if (this.prefs.isActive()) {
                    this.timer.start();
                } else {
                    this.timer.stop();
                    Iterator i = this.downloaders.iterator();
                    while (i.hasNext()) {
                        ((Downloader) i.next()).stop();
                    }
                    this.downloaders = null;
                }
            } else if (arg.equals(Preferences.NOTIFY_PARTICIPATE)) {
            } else if (arg.equals(Preferences.NOTIFY_TCOUNT)) {
                if (currentUrl != null) {
                    int tCount = this.prefs.getThreadCount();
                    int dCount = this.downloaders.size();
                    if (tCount > dCount) {
                        while (tCount > dCount) {
                            URL url = null;
                            this.downloaders.add(new Downloader(currentUrl, this.gui));
                            dCount++;
                            this.log("Added Downloader");
                        }
                    } else if (tCount < dCount) {
                        while (tCount < dCount) {
                            Downloader d = (Downloader) this.downloaders.remove(0);
                            d.stop();
                            d = null;
                            dCount--;
                            this.log("Removed Downloader");
                        }
                    }
                }
            } else {
                System.err.println(arg.toString());
            }
        } else if (o == this.timer) {
            doDownloads((String) arg);
        } else if (o.getClass().getName().equals("org.spamerdos.app.Downloader")) {
            if (arg.equals("count")) {
                System.err.println("Got it in SpamDos");
            }
        } else {
            System.out.println("Observable=" + o);
            System.out.println("arg=" + arg);
        }
    }
}
