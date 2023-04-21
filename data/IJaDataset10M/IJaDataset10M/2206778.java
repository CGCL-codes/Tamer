package phex.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import phex.common.address.AddressUtils;
import phex.common.address.IpAddress;
import phex.utils.IOUtil;
import phex.utils.NLogger;
import phex.utils.NLoggerNames;

/**
 * 
 */
public class Ip2CountryManager extends AbstractManager {

    /**
     * Indicates if the database is fully loaded or not.
     */
    private boolean isLoaded;

    private List<IpCountryRange> ipCountryRangeList;

    private Ip2CountryManager() {
        isLoaded = false;
        ipCountryRangeList = new ArrayList<IpCountryRange>();
    }

    private static class Holder {

        protected static final Ip2CountryManager manager = new Ip2CountryManager();
    }

    public static Ip2CountryManager getInstance() {
        return Ip2CountryManager.Holder.manager;
    }

    /**
     * This method is called in order to initialize the manager.  Inside
     * this method you can't rely on the availability of other managers.
     * @return true if initialization was successful, false otherwise.
     */
    public boolean initialize() {
        return true;
    }

    /**
     * This method is called in order to perform post initialization of the
     * manager. This method includes all tasks that must be done after initializing
     * all the several managers. Inside this method you can rely on the
     * availability of other managers.
     * @return true if initialization was successful, false otherwise.
     */
    public boolean onPostInitialization() {
        return true;
    }

    /**
     * This method is called after the complete application including GUI completed
     * its startup process. This notification must be used to activate runtime
     * processes that needs to be performed once the application has successfully
     * completed startup.
     */
    public void startupCompletedNotify() {
        Runnable runnable = new Runnable() {

            public void run() {
                loadIp2CountryDB();
            }
        };
        ThreadPool.getInstance().addJob(runnable, "IP2CountryLoader");
    }

    /**
     * This method is called in order to cleanly shutdown the manager. It
     * should contain all cleanup operations to ensure a nice shutdown of Phex.
     */
    public void shutdown() {
    }

    /**
     * Returns the country code if found, empty string if not found, and null
     * if DB has not been loaded yet.
     * @param address
     * @return
     */
    public String getCountryCode(IpAddress address) {
        if (!isLoaded) {
            return null;
        }
        IpCountryRange range = binarySearch(address.getHostIP());
        if (range == null) {
            return "";
        }
        return range.countryCode;
    }

    private void loadIp2CountryDB() {
        InputStream inStream = ClassLoader.getSystemResourceAsStream("phex/resources/ip2country.csv");
        if (inStream == null) {
            NLogger.debug(Ip2CountryManager.class, "Default GWebCache file not found.");
            return;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        ArrayList<IpCountryRange> initialList = new ArrayList<IpCountryRange>(5000);
        IpCountryRange range;
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                range = new IpCountryRange(line);
                initialList.add(range);
                line = reader.readLine();
            }
        } catch (IOException exp) {
            NLogger.error(NLoggerNames.GLOBAL, exp, exp);
        } finally {
            IOUtil.closeQuietly(reader);
        }
        initialList.trimToSize();
        Collections.sort(initialList);
        ipCountryRangeList = Collections.unmodifiableList(initialList);
        isLoaded = true;
    }

    private IpCountryRange binarySearch(byte[] hostIp) {
        int low = 0;
        int high = ipCountryRangeList.size() - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            IpCountryRange midVal = ipCountryRangeList.get(mid);
            int cmp = midVal.compareHostAddress(hostIp);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midVal;
            }
        }
        return null;
    }

    private class IpCountryRange implements Comparable<IpCountryRange> {

        byte[] from;

        byte[] to;

        String countryCode;

        public IpCountryRange(String line) {
            int startIdx, endIdx;
            startIdx = 0;
            endIdx = line.indexOf((int) ',', startIdx);
            from = AddressUtils.parseIntIP(line.substring(startIdx, endIdx));
            startIdx = endIdx + 1;
            endIdx = line.indexOf((int) ',', startIdx);
            to = AddressUtils.parseIntIP(line.substring(startIdx, endIdx));
            startIdx = endIdx + 1;
            countryCode = line.substring(startIdx);
        }

        public int compareHostAddress(byte[] hostIp) {
            long hostIpL;
            hostIpL = IOUtil.unsignedInt2Long(IOUtil.deserializeInt(hostIp, 0));
            long fromIpL = IOUtil.unsignedInt2Long(IOUtil.deserializeInt(from, 0));
            long cmp = hostIpL - fromIpL;
            if (cmp == 0) {
                return 0;
            }
            if (cmp < 0) {
                return 1;
            }
            long toIpL = IOUtil.unsignedInt2Long(IOUtil.deserializeInt(to, 0));
            cmp = hostIpL - toIpL;
            if (cmp == 0 || cmp < 0) {
                return 0;
            } else {
                return -1;
            }
        }

        public int compareTo(IpCountryRange range) {
            if (range == this) {
                return 0;
            }
            byte[] ip1 = from;
            byte[] ip2 = range.from;
            long ip1l = IOUtil.unsignedInt2Long(IOUtil.deserializeInt(ip1, 0));
            long ip2l = IOUtil.unsignedInt2Long(IOUtil.deserializeInt(ip2, 0));
            if (ip1l < ip2l) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
