package org.yjchun.hanghe.chart;

import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yjchun.hanghe.Global;
import org.yjchun.hanghe.chart.bsb.BsbChart;
import org.yjchun.hanghe.projection.LonLatPoint;

/**
 * Manage charts.
 * 
 * Chart files can be loaded from any directories.
 * When chart files are scanned, chart header info is read and Chart object is created.
 * 
 * Used properties:
 *  chart.search.list = comma separated chart search path list.
 *  chart.search.list.default = default of above property
 * @author yjchun
 *
 */
public class ChartManager {

    protected static final Logger log = LoggerFactory.getLogger(ChartManager.class);

    /** Search path list */
    List<String> searchList = new LinkedList<String>();

    /** Loaded chart list */
    HashSet<Chart> chartList = new HashSet<Chart>();

    /** Canonical paths of the chart files loaded.
	 *  Same file won't be loaded twice */
    HashSet<String> chartPathList = new HashSet<String>();

    boolean isInitialized = false;

    static ChartManager instance = null;

    public static ChartManager getInstance() {
        if (instance == null) instance = new ChartManager();
        return instance;
    }

    protected ChartManager() {
    }

    public boolean initialize() {
        if (isInitialized) return true;
        log.debug("Initializing chart manager");
        try {
            searchList.clear();
            String[] slist = Global.config.getStringArray("chart.search.list.default");
            for (String s : slist) {
                addSearchPath(s);
            }
            slist = Global.config.getStringArray("chart.search.list");
            for (String s : slist) {
                addSearchPath(s);
            }
            if (searchList.size() == 0) {
                log.warn("No chart to load! chart.search.list is not set");
            }
            scanSearchPath();
            isInitialized = true;
            return true;
        } catch (Exception e) {
            log.error("Error initializing chart manager: ", e);
        }
        return false;
    }

    public void addSearchPath(String path) {
        if (searchList.contains(path)) return;
        log.info("Adding chart directory: {}", path);
        searchList.add(path);
    }

    /**
	 * Scan search path for charts and returns number of newly loaded charts. 
	 * @return Number of newly loaded charts.
	 */
    public int scanSearchPath() {
        int nadded = 0;
        log.debug("Scanning chart search paths");
        for (String path : searchList) {
            nadded += scanSearchPath(new File(path));
        }
        {
            log.info("{} chart files are loaded", nadded);
        }
        return nadded;
    }

    protected int scanSearchPath(File file) {
        int nadded = 0;
        if (!file.exists()) return 0;
        if (file.isHidden()) return 0;
        if (file.isDirectory()) {
            String abs = file.getAbsolutePath();
            if (abs.equals("/") || abs.substring(1).equals(":\\")) {
                log.warn("Root file system ({}) cannot be added to chart search path", file.getPath());
                return 0;
            }
            for (File f : file.listFiles()) {
                nadded += scanSearchPath(f);
            }
            return nadded;
        }
        try {
            if (chartPathList.contains(file.getCanonicalPath())) {
                return 0;
            }
        } catch (IOException e1) {
            log.error("Cannot get canonical path: {}", e1.toString());
            return 0;
        }
        try {
            Chart chart;
            if (BsbChart.canDecode(file.getPath())) chart = new BsbChart(file); else return 0;
            if (!chart.load()) return 0;
            synchronized (chartList) {
                for (Chart c : chartList) {
                    if (c.isIdentical(chart)) {
                        log.info("Identical chart already loaded. Not loading this one");
                        return 0;
                    }
                }
                chartPathList.add(chart.getFile().getCanonicalPath());
                chartList.add(chart);
            }
            return 1;
        } catch (Exception e) {
            log.error("Cannot add chart: {}", e.toString());
        }
        return 0;
    }

    /**
	 * @param area
	 * @return newly allocated List of charts which intersects with 
	 */
    public HashSet<Chart> copyChartList() {
        synchronized (chartList) {
            return new HashSet<Chart>(chartList);
        }
    }

    /**
	 * NOTE: Need to synchronize on the returned object to access it.
	 * @return
	 */
    public HashSet<Chart> getChartList() {
        return chartList;
    }
}
