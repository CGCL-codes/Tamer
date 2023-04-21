package org.chartsy.main.datafeed;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.Preferences;
import org.chartsy.main.intervals.DailyInterval;
import org.chartsy.main.intervals.FifteenMinuteInterval;
import org.chartsy.main.intervals.FiveMinuteInterval;
import org.chartsy.main.intervals.Interval;
import org.chartsy.main.intervals.MonthlyInterval;
import org.chartsy.main.intervals.OneMinuteInterval;
import org.chartsy.main.intervals.SixtyMinuteInterval;
import org.chartsy.main.intervals.ThirtyMinuteInterval;
import org.chartsy.main.intervals.WeeklyInterval;
import org.chartsy.main.util.VersionUtil;
import org.openide.util.NbPreferences;

/**
 *
 * @author Viorel
 */
public abstract class DataFeed implements Serializable {

    private static final long serialVersionUID = VersionUtil.APPVERSION;

    public static final Interval ONE_MINUTE = new OneMinuteInterval();

    public static final Interval FIVE_MINUTE = new FiveMinuteInterval();

    public static final Interval FIFTEEN_MINUTE = new FifteenMinuteInterval();

    public static final Interval THIRTY_MINUTE = new ThirtyMinuteInterval();

    public static final Interval SIXTY_MINUTE = new SixtyMinuteInterval();

    public static final Interval DAILY = new DailyInterval();

    public static final Interval WEEKLY = new WeeklyInterval();

    public static final Interval MONTHLY = new MonthlyInterval();

    public static final Interval[] INTRA_DAY_INTERVALS = { ONE_MINUTE, FIVE_MINUTE, FIFTEEN_MINUTE, THIRTY_MINUTE, SIXTY_MINUTE };

    public static final Interval[] INTERVALS = { DAILY, WEEKLY, MONTHLY };

    public static final Interval[] CUSTOM_INTERVALS = {};

    public String DEFAULT_EXCHANGE = "Default";

    protected Preferences dataFeedPreferences;

    private final Map<String, Dataset> datasets;

    private final Map<String, AtomicInteger> datasetUsage;

    protected String name;

    protected Exchange[] exchanges;

    protected int refreshInterval;

    protected boolean supportsIntraDay = false;

    protected boolean supportsCustomInterval = false;

    protected boolean needsRegistration = false;

    protected boolean isRegistered = false;

    public DataFeed(ResourceBundle bundle) {
        this(bundle, false, false);
    }

    public DataFeed(ResourceBundle bundle, boolean supportsIntraDay) {
        this(bundle, supportsIntraDay, false);
    }

    public DataFeed(ResourceBundle bundle, boolean supportsIntraDay, boolean supportsCustomIntervals) {
        this.name = bundle.getString("DataFeed.name");
        this.supportsIntraDay = supportsIntraDay;
        this.supportsCustomInterval = supportsCustomIntervals;
        this.dataFeedPreferences = NbPreferences.root().node("/org/chartsy/datafeeds/" + name.toLowerCase().replace(" ", "_"));
        this.datasets = Collections.synchronizedMap(new HashMap<String, Dataset>());
        this.datasetUsage = Collections.synchronizedMap(new HashMap<String, AtomicInteger>());
        String[] exgs = bundle.getString("DataFeed.exchanges").split(":");
        exchanges = new Exchange[exgs.length];
        for (int i = 0; i < exgs.length; i++) {
            String exchange = exgs[i];
            String resName = exgs[i].replace(" ", "") + ".suffix";
            String suffix = bundle.getString(resName);
            if (suffix.equals("null")) {
                suffix = "";
            }
            exchanges[i] = new Exchange(exchange, suffix);
        }
    }

    public void initialize() {
    }

    public String getName() {
        return name;
    }

    public Exchange[] getExchanges() {
        return exchanges;
    }

    public Interval getDefaultInterval() {
        return DAILY;
    }

    public Interval[] getIntraDayIntervals() {
        return INTRA_DAY_INTERVALS;
    }

    public Interval[] getIntervals() {
        return INTERVALS;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public boolean supportsIntraday() {
        return supportsIntraDay;
    }

    public boolean supportsCustomInterval() {
        return supportsCustomInterval;
    }

    public boolean supportsAnyInterval() {
        return false;
    }

    public boolean needsRegistration() {
        return needsRegistration;
    }

    public boolean isRegistred() {
        return isRegistered;
    }

    public String getRegistrationMessage() {
        return "";
    }

    public String getRegistrationURL() {
        return "";
    }

    protected boolean isStockCached(String symbol) {
        return !dataFeedPreferences.get(symbol, "").equals("");
    }

    protected String getStockFromCache(String symbol) {
        return dataFeedPreferences.get(symbol, "");
    }

    protected void cacheStock(String symbol, String companyName) {
        dataFeedPreferences.put(symbol, companyName);
    }

    public abstract String fetchStockCompanyName(Stock stock);

    public String getDatasetKey(String symbol, Interval interval) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("_").append(symbol);
        sb.append("_").append(interval.getName());
        return sb.toString();
    }

    protected boolean isDatasetInUse(String symbol, Interval interval) {
        String key = getDatasetKey(symbol, interval);
        synchronized (datasetUsage) {
            return datasetUsage.containsKey(key);
        }
    }

    protected void addDataset(String symbol, Interval interval, Dataset dataset) {
        String key = getDatasetKey(symbol, interval);
        synchronized (datasetUsage) {
            if (datasetUsage.containsKey(key)) {
                datasetUsage.get(key).incrementAndGet();
            } else {
                datasetUsage.put(key, new AtomicInteger(1));
            }
        }
        synchronized (datasets) {
            datasets.put(key, dataset);
        }
    }

    public void removeDataset(String symbol, Interval interval) {
        String key = getDatasetKey(symbol, interval);
        boolean remove = false;
        synchronized (datasetUsage) {
            AtomicInteger ai = datasetUsage.get(key);
            if (ai != null && ai.decrementAndGet() == 0) {
                remove = true;
                datasetUsage.remove(key);
            }
        }
        synchronized (datasets) {
            if (remove) {
                datasets.remove(key);
            }
        }
    }

    public Dataset getDataset(String symbol, Interval interval) {
        String key = getDatasetKey(symbol, interval);
        synchronized (datasetUsage) {
            if (datasetUsage.containsKey(key)) {
                datasetUsage.get(key).incrementAndGet();
            } else {
                fetchDataset(symbol, interval);
            }
        }
        synchronized (datasets) {
            return datasets.get(key);
        }
    }

    public abstract void fetchDataset(String symbol, Interval interval);

    public abstract void fetchLastTrade(String symbol, Interval interval);

    public abstract String[] fetchAutocomplete(String text);

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DataFeed)) {
            return false;
        }
        DataFeed that = (DataFeed) obj;
        if (!name.equals(that.getName())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
