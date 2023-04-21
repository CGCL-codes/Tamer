package com.bastaware.geoshrine;

import com.sun.svg.util.ProgressiveInputStream;
import java.util.Vector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import com.bastaware.components.Answer;
import com.bastaware.components.QuestionCanvas;
import com.bastaware.geoshrine.data.Cache;
import com.bastaware.geoshrine.data.CacheHeader;
import com.bastaware.geoshrine.data.CacheNameComparator;
import com.bastaware.geoshrine.data.DistanceComparator;
import com.bastaware.geoshrine.data.GCComparator;
import com.bastaware.geoshrine.data.GPXParser;
import com.bastaware.geoshrine.location.PositioningRunner;
import com.bastaware.geoshrine.data.ShrineCoordinates;
import com.bastaware.geoshrine.location.GpsAvailable;
import com.bastaware.util.IComparator;
import com.bastaware.util.QuickSort;

/**
 * @author B
 */
public class GeoShrine extends MIDlet implements Runnable, Answer {

    private static final int ANIMTHREAD_SPEED = 30;

    public static final String APPLICATION_NAME = "GEOSHRINE";

    private static final int SORT_AZ = 0;

    private static final int SORT_GC = 1;

    private static final int SORT_DIST = 2;

    private Display myDisplay;

    private MainScreenCanvas mainScreen;

    private MapCanvas mapCanvas;

    private DisplayCacheCanvas cacheCanvas;

    private LoadDataCanvas loadScreen;

    private SettingsCanvas settingsCanvas;

    private IOCanvas ioCanvas;

    private AlertCanvas alertCanvas;

    private QuestionCanvas questionCanvas;

    private Vector cacheList;

    private CacheHeader targetCache;

    private GPXParser gpxParser;

    private int sortOrder = 0;

    private IComparator azComparator;

    private IComparator distComparator;

    private IComparator gcComparator;

    /**
     * GPS Stuff
     */
    private boolean gpsAvailable;

    private Thread gpsServiceThread;

    private PositioningRunner positioningRunner;

    private boolean running = true;

    private int avgTimeBetweenReadings;

    private static ShrineCoordinates currentCoordinates;

    private int coordinatesReceived;

    private StringBuffer log;

    public static int logPos = 0;

    public GeoShrine() {
        myDisplay = Display.getDisplay(this);
        try {
            logPos = 100;
            azComparator = new CacheNameComparator();
            logPos = 101;
            distComparator = new DistanceComparator();
            gcComparator = new GCComparator();
            logPos = 102;
            GUIFactory.initFonts();
            logPos = 103;
            gpsAvailable = GpsAvailable.gpsAvailable();
            logPos = 104;
            ImageFactory.init();
            logPos = 105;
            mainScreen = new MainScreenCanvas(this);
            logPos = 106;
            mapCanvas = new MapCanvas(this);
            logPos = 107;
            settingsCanvas = new SettingsCanvas(this, gpsAvailable);
            logPos = 108;
            Thread thread = new Thread(this, "Animation");
            logPos = 109;
            thread.start();
            logPos = 110;
            loadScreen = new LoadDataCanvas(this);
        } catch (Throwable t) {
            t.printStackTrace();
            displayAlert("Error while constructor() LP:" + logPos, t);
        }
    }

    public void startApp() {
        try {
            logPos = 111;
            GUIFactory.setDetailedGfx(settingsCanvas.isDetailedGraphics());
            logPos = 112;
            GUIFactory.setMenusEnabled(settingsCanvas.areMenusEnabled());
            logPos = 113;
            currentCoordinates = settingsCanvas.getCoordinates();
            logPos = 114;
            cacheList = loadCacheDB();
            logPos = 115;
            if (cacheList != null) {
                logPos = 116;
                QuickSort.quicksort(cacheList, getCurrentComparator(), true);
                logPos = 117;
            }
            logPos = 118;
            mainScreen.cacheListUpdated();
            logPos = 119;
            displayMainScreen();
            logPos = 120;
            if (settingsCanvas.useGPS()) {
                logPos = 121;
                startGPSService();
                logPos = 122;
            }
            if (System.currentTimeMillis() > 61242559200000L) {
                mainScreen = null;
                displayAlert("This version of GeoShrine is outdated!\n\nPlease visit www.geoshrine.org and download a new version");
            } else if (System.currentTimeMillis() > 61241436000000L) {
                displayQuestion("This version of GeoShrine is outdated\n\nDo you promise to download a new version within 14 days?", this);
            }
        } catch (Throwable t) {
            displayAlert("Error while startApp() " + logPos, t);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        getDisplay().setCurrent(null);
        notifyDestroyed();
    }

    /**
     * Make sure that displayFileBrowser() was called before this
     * Don't call this from GUI Thread or everything is very slow on Nokia
     */
    public void loadGPX(FileConnection fc) {
        myDisplay.setCurrent(loadScreen);
        try {
            loadScreen.setStatus(null);
            loadScreen.setFilename(fc.getName());
            if (gpxParser == null) {
                gpxParser = new GPXParser(loadScreen);
            }
            Runtime.getRuntime().gc();
            ProgressiveInputStream pis = new ProgressiveInputStream(fc.openInputStream(), (int) fc.fileSize(), loadScreen);
            Vector readCaches = gpxParser.readCaches(pis);
            pis.close();
            fc.close();
            if (cacheList == null) {
                cacheList = readCaches;
            } else {
                for (int i = 0; i < readCaches.size(); i++) {
                    cacheList.addElement(readCaches.elementAt(i));
                }
            }
            QuickSort.quicksort(cacheList, getCurrentComparator(), true);
            displayMainScreen();
        } catch (Throwable ex) {
            displayAlert("Problem loading file", ex);
        }
    }

    public boolean azSorting() {
        return sortOrder == SORT_AZ;
    }

    public boolean distSorting() {
        return sortOrder == SORT_DIST;
    }

    public boolean gcSorting() {
        return sortOrder == SORT_GC;
    }

    void changeSortOrder() {
        if (cacheList != null && cacheList.size() > 0) {
            sortOrder++;
            if (sortOrder > SORT_DIST) {
                sortOrder = SORT_AZ;
            }
            QuickSort.quicksort(cacheList, getCurrentComparator(), true);
            mainScreen.cacheListUpdated();
        }
    }

    void clearCacheList() {
        cacheList = null;
        mainScreen.cacheListUpdated();
        Runtime.getRuntime().gc();
    }

    void deleteCache(Cache cache, CacheHeader cacheHeader) {
        try {
            PersistableManager.getInstance().delete(cacheHeader);
            PersistableManager.getInstance().delete(cache);
            cacheList.removeElement(cacheHeader);
            mainScreen.cacheListUpdated();
        } catch (FloggyException ex) {
            ex.printStackTrace();
        }
    }

    void displayMainScreen() {
        myDisplay.setCurrent(mainScreen);
    }

    void displayCache(CacheHeader cacheHeader, Cache cache) {
        if (cacheCanvas == null) {
            cacheCanvas = new DisplayCacheCanvas(this);
        }
        cacheCanvas.setCache(cacheHeader, cache);
        myDisplay.setCurrent(cacheCanvas);
    }

    void displayMap() {
        myDisplay.setCurrent(mapCanvas);
    }

    void displayQuestion(String question, Answer answerListener) {
        if (questionCanvas == null) {
            questionCanvas = new QuestionCanvas(answerListener, question);
        } else {
            questionCanvas.setQuestion(question);
            questionCanvas.setAnswerListener(answerListener);
        }
        myDisplay.setCurrent(questionCanvas);
    }

    Vector getCacheList() {
        return cacheList;
    }

    void setTarget(CacheHeader cacheHeader) {
        this.targetCache = cacheHeader;
    }

    CacheHeader getTarget() {
        return targetCache;
    }

    public Display getDisplay() {
        return myDisplay;
    }

    public void displayIO() {
        if (ioCanvas == null) {
            ioCanvas = new IOCanvas(this);
        }
        myDisplay.setCurrent(ioCanvas);
    }

    public void displaySettings() {
        settingsCanvas.init();
        myDisplay.setCurrent(settingsCanvas);
    }

    public void startGPSService() {
        if (gpsServiceThread == null) {
            if (positioningRunner == null) {
                positioningRunner = new PositioningRunner(this);
            }
            gpsServiceThread = new Thread(positioningRunner);
            gpsServiceThread.start();
        } else {
            displayAlert("GPS Service allready running?", null);
        }
    }

    public void stopGPSService() {
        if (gpsServiceThread != null && positioningRunner != null) {
            positioningRunner.quit();
            gpsServiceThread = null;
        }
    }

    public void displayAlert(String text) {
        displayAlert(text, null);
    }

    public void displayAlert(String text, Throwable e) {
        if (log == null) {
            log = new StringBuffer();
        }
        log.append(text);
        log.append("\n");
        if (e != null) {
            log.append(e.getClass());
            log.append("\n");
            log.append(e.getMessage());
        }
        if (alertCanvas == null) {
            alertCanvas = new AlertCanvas(this);
        }
        alertCanvas.setText(log.toString());
        myDisplay.setCurrent(alertCanvas);
        if (e != null) {
            e.printStackTrace();
        }
    }

    /**
     * Animation loop
     */
    public void run() {
        while (running) {
            try {
                long pause = System.currentTimeMillis() + ANIMTHREAD_SPEED;
                Displayable display = myDisplay.getCurrent();
                if (display instanceof Animator) {
                    ((Animator) display).animate();
                }
                pause -= System.currentTimeMillis();
                if (pause > 0) {
                    try {
                        Thread.sleep(pause);
                    } catch (InterruptedException ie) {
                    }
                }
            } catch (Exception e) {
                displayAlert("Error while animating", e);
            }
        }
    }

    public static ShrineCoordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(ShrineCoordinates coordinates) {
        coordinatesReceived++;
        currentCoordinates = coordinates;
    }

    public int getCoordinatesReceived() {
        return coordinatesReceived;
    }

    public void setAvgTimeBetweenReadings(int avg) {
        avgTimeBetweenReadings = avg;
    }

    public int getAvgTimeBetweenReadings() {
        return avgTimeBetweenReadings;
    }

    public boolean isGpsAvailable() {
        return gpsAvailable;
    }

    public boolean isGpsRunning() {
        return gpsServiceThread != null;
    }

    private IComparator getCurrentComparator() {
        if (sortOrder == SORT_AZ) {
            return azComparator;
        } else if (sortOrder == SORT_DIST) {
            return distComparator;
        } else {
            return gcComparator;
        }
    }

    private Vector loadCacheDB() throws FloggyException {
        ObjectSet cacheHeaders = PersistableManager.getInstance().find(CacheHeader.class, null, null);
        Vector readCaches = null;
        if (cacheHeaders != null && cacheHeaders.size() > 0) {
            loadScreen.setFilename("Database");
            myDisplay.setCurrent(loadScreen);
            readCaches = new Vector(cacheHeaders.size());
            for (int i = 0; i < cacheHeaders.size(); i++) {
                loadScreen.streamProgress((float) i / cacheHeaders.size());
                CacheHeader c = (CacheHeader) cacheHeaders.get(i);
                readCaches.addElement(c);
                loadScreen.setStatus(c.getName());
            }
        }
        return readCaches;
    }

    /**
     * Promise to download a new version
     * @param answer
     */
    public void answerQuestion(boolean answer) {
        if (answer) {
            displayMainScreen();
        } else {
            destroyApp(true);
        }
    }
}
