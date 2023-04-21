package org.dvdcatalog.dvdc.ui.wizard.imdb;

import javax.swing.JProgressBar;

/**
 *  This class will make a new thread so the downloading can run sepratly.
 *
 * @author     lars
 * @created    November 21, 2004
 */
public class Downloader extends Thread {

    private ImdbInfo info;

    private ImdbSearch imdbSearch;

    private JProgressBar bar;

    /**
	 *  Constructor for the Downloader object
	 *
	 * @param  info  the ImdbInfo.
	 */
    public Downloader(ImdbInfo info, JProgressBar bar) {
        this.info = info;
        this.bar = bar;
    }

    public Downloader(ImdbSearch is) {
        imdbSearch = is;
    }

    /**
	 *  This is called when the new thread starts to run.
	 */
    public void run() {
        if (info != null) {
            info.setBar(bar);
            info.start();
        } else if (imdbSearch != null) {
            imdbSearch.start();
        }
    }
}
