package org.fao.geonet.util;

import jeeves.utils.Log;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.setting.SettingManager;
import java.util.Map;

public class ThreadUtils {

    private static SettingManager settingMan;

    private static boolean dbCanUseMultipleThreads = false;

    private static String dbUrl;

    private ThreadUtils() {
    }

    /** Set thread count from settings. i
   *
	 * @return threadCount
   */
    private static int setCountFromSettings() {
        int threadCount = 1;
        String nrThreadsStr = settingMan.getValue("system/threadedindexing/maxthreads");
        if (nrThreadsStr == null) {
            Log.error(Geonet.GEONETWORK, "Number of Threads for indexing setting is missing from settings table. Using *one* thread");
            nrThreadsStr = "1";
        }
        try {
            threadCount = Integer.parseInt(nrThreadsStr);
        } catch (NumberFormatException nfe) {
            Log.error(Geonet.GEONETWORK, "Number of Threads for indexing setting is not valid. Using *one* thread");
        }
        return threadCount;
    }

    /** Initialize threadUtils during GeoNetwork startup.
   *
	 * @param props Map of database properties. Used to make decisions about 
	 *              whether a database supports threaded access.
	 * @param sm SettingManager. Used to find settings for threaded methods.
	 */
    public static void init(Map<String, String> props, SettingManager sm) throws Exception {
        settingMan = sm;
        dbUrl = props.get("url");
        if (dbUrl != null) {
            if (dbUrl.toLowerCase().contains("postgres")) {
                dbCanUseMultipleThreads = true;
            } else if (dbUrl.toLowerCase().contains("oracle")) {
                dbCanUseMultipleThreads = true;
            }
        }
    }

    /** Get number of threads calc'd from runtime or settings and 
	 * restrict if not using capable database or threaded processing
	 * not available.
	 * 
	 * @return threadCount
	 */
    public static int getNumberOfThreads() {
        int threadCount = setCountFromSettings();
        if (!dbCanUseMultipleThreads && threadCount > 1) {
            threadCount = 1;
            Log.error(Geonet.GEONETWORK, "Theaded Indexing for " + dbUrl + " not supported or hasn't been tested - so only *one* thread will be used");
        }
        Log.info(Geonet.GEONETWORK, "Using " + threadCount + " thread(s) to process indexing job");
        return threadCount;
    }

    /** Get thread priority calc'd from runtime or settings.
	 * 
	 * @return threadPriority
	 */
    public static int getPriority() {
        return Thread.NORM_PRIORITY;
    }

    /** Get number of processors allocated to JVM if the database supports
   * threaded access. 
	 * 
	 * @return number of processors allocated to JVM.
	 */
    public static String getNumberOfProcessors() {
        int result = 1;
        if (dbCanUseMultipleThreads) {
            result = Runtime.getRuntime().availableProcessors();
        }
        return result + "";
    }
}
