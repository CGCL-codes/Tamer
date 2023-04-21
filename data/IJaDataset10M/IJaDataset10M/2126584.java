package net.sf.connect5d.osf.donorsvc.pub.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import net.sf.connect5d.osf.donorsvc.fw.LibraryException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class ScreenScrapeController {

    private static ScreenScrapeController instance = new ScreenScrapeController();

    private static Logger log = Logger.getLogger(ScreenScrapeController.class);

    private static final String CONFIG_FILE = "ScreenScrapeConfig.xml";

    private int connect_timemout = 2 * 60 * 1000;

    private int maxLicenses = 0;

    private int rollback = 0;

    private URL url;

    private ArrayBlockingQueue<License> licenseQueue = null;

    /**
     * a placeholder class and 1 instance so we can put references to it in the
     * licenseQueue
     *
     * initially tried to use an int for this purpose, but the collections
     * framework complain that it ints are unchecked.
     */
    private class License {

        License() {
        }
    }

    private License license = new License();

    /**
     * private constructor for singleton
     *
     * configures the controller
     */
    private ScreenScrapeController() {
        Document configDoc;
        String urlString;
        String maxLString;
        String timeoutString;
        int maxLInt;
        int timeoutInt;
        try {
            configDoc = XMLUtil.parseStream(getClass().getResourceAsStream("/" + CONFIG_FILE));
            urlString = XMLUtil.getString("ScreenScrapeConfig/Property[@name=\"serverURL\"]/@value", configDoc);
            maxLString = XMLUtil.getString("ScreenScrapeConfig/Property[@name=\"maxLicenses\"]/@value", configDoc);
            timeoutString = XMLUtil.getString("ScreenScrapeConfig/Property[@name=\"timeout\"]/@value", configDoc);
        } catch (Exception e) {
            log.error("invalid configuration file");
            throw new LibraryException(LibraryException.Code.CONFIGURATION, e);
        }
        try {
            maxLInt = Integer.parseInt(maxLString);
        } catch (Exception e) {
            log.error("Invalid integer in maxLicenses property in config file");
            throw new LibraryException(LibraryException.Code.CONFIGURATION, e);
        }
        try {
            url = new URL(urlString);
        } catch (Exception e) {
            log.error("Invalid urlString property in config file");
            throw new LibraryException(LibraryException.Code.CONFIGURATION, e);
        }
        if (timeoutString != null && !"".equals(timeoutString)) {
            try {
                connect_timemout = Integer.parseInt(timeoutString) * 1000;
            } catch (Exception e) {
                log.error("Invalid integer in timeout property in config file");
            }
        }
        setLicenses(maxLInt);
    }

    /**
     * @return the singleton instance of the controller
     */
    public static ScreenScrapeController getInstance() {
        return instance;
    }

    /**
     * Adds the integer 1 to the license queue 'to' times
     */
    private void fillLicenses(int to) {
        for (int i = 0; i < to; i++) {
            licenseQueue.add(license);
        }
    }

    /**
     * Update the maximum licenses available for use for running scripts
     * without blocking. This method may be called at any time, but it really
     * only needs to be called once while configuring the controller.
     */
    public void setLicenses(int newMax) {
        if (newMax == maxLicenses) {
            return;
        }
        int loaned = 0;
        if (licenseQueue != null) {
            loaned = licenseQueue.remainingCapacity();
        }
        if (newMax > 0) {
            licenseQueue = new ArrayBlockingQueue<License>(newMax, true);
        } else {
            licenseQueue = null;
        }
        if (newMax > maxLicenses) {
            fillLicenses(newMax - loaned);
        } else if (newMax < maxLicenses) {
            int reduceBy = maxLicenses - newMax;
            if (loaned > reduceBy) {
                rollback = rollback + loaned - reduceBy;
            } else {
                fillLicenses(newMax - loaned);
            }
        }
        maxLicenses = newMax;
    }

    /**
     * Runs the given script with the given parameters
     * Blocks until a license is available
     */
    public Document processScript(String scriptName, Document parameters) {
        if (maxLicenses < 1) {
            return processScript0(scriptName, parameters);
        }
        Document retValue = XMLUtil.emptyDocument();
        try {
            licenseQueue.take();
            retValue = processScript0(scriptName, parameters);
            if (rollback > 0) {
                rollback--;
            } else {
                licenseQueue.add(license);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     */
    private Document processScript0(String scriptName, Document parameters) {
        try {
            Document ret;
            long start = System.nanoTime();
            ret = XMLOverHTTP.send(url.toString() + scriptName, parameters);
            long end = System.nanoTime();
            log.trace("Execution of script " + scriptName + " completed in " + (end - start) / 1000000 + " milliseconds.");
            return ret;
        } catch (MalformedURLException e) {
            log.error("Invalid script name", e);
            throw new LibraryException(e);
        } catch (IOException e) {
            log.error("IO exception accessing screenscrape url", e);
            throw new LibraryException(e);
        }
    }
}
