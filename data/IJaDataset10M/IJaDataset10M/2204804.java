package org.gridbus.broker.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.gridbus.broker.common.ApplicationContext;
import org.gridbus.broker.common.BrokerBase;
import org.gridbus.broker.common.ComputeServer;
import org.gridbus.broker.common.JobStatistics;
import org.gridbus.broker.common.Qos;
import org.gridbus.broker.constants.Constants;
import org.gridbus.broker.constants.ServiceType;
import org.gridbus.broker.exceptions.GridBrokerException;

/**
 * @author krishna
 */
public class BrokerUtil {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(BrokerUtil.class);

    /**
	 * Converts a long value (elapsed time) to the format hh:mm:ss.SSS
	 * @param elapsedTimeMillis (in milliseconds)
	 * @return time formatted as hh:mm:ss.SSS
	 */
    public static String getHHMMSS(long elapsedTimeMillis) {
        String result = "";
        long h, m, s;
        h = Math.round(elapsedTimeMillis / 3600000);
        elapsedTimeMillis = elapsedTimeMillis - (h * 3600000);
        m = Math.round(elapsedTimeMillis / 60000);
        elapsedTimeMillis = elapsedTimeMillis - (m * 60000);
        s = Math.round(elapsedTimeMillis / 1000);
        elapsedTimeMillis = elapsedTimeMillis - (s * 1000);
        DecimalFormat df = new DecimalFormat("00");
        result = df.format(h) + ":" + df.format(m) + ":" + df.format(s) + "." + elapsedTimeMillis;
        return result;
    }

    /**
	 * Gets the class name from a fully qualified package.class name.
	 * For example, 
	 * an input:  org.gridbus.broker.MyClass 
	 * will give output: MyClass
	 * @param fullName
	 * @return className 
	 */
    public static String findClassName(String fullName) {
        String className = null;
        StringTokenizer tk = new StringTokenizer(fullName, ".");
        for (int i = 0; i < tk.countTokens() - 1; tk.nextToken()) ;
        className = tk.nextToken();
        return className;
    }

    /**
	 * Get the last file name of a path
	 * @param path
	 * @return
	 */
    public static String getFileName(String path) {
        String newPath = path.replace('\\', '/');
        File f = new File(newPath);
        String filename = f.getName();
        if (filename != null) filename = filename.replace('\\', '/');
        return filename;
    }

    /**
	 * Get the last directory name of a path, without the file name
	 * @param path
	 * @return
	 */
    public static String getFilePath(String pathAndFilename) {
        String newPath = pathAndFilename.replace('\\', '/');
        File f = new File(newPath);
        String pathName = f.getParent();
        if (pathName != null) pathName = pathName.replace('\\', '/');
        return pathName;
    }

    /**
	 * @param array
	 * @param delim
	 * @return
	 */
    public static String join(String[] array, String delim) {
        StringBuffer sb = join(array, delim, new StringBuffer());
        return sb.toString();
    }

    /**
	 * @param array
	 * @param delim
	 * @param sb
	 * @return
	 */
    public static StringBuffer join(String[] array, String delim, StringBuffer sb) {
        for (int i = 0; i < array.length; i++) {
            if (i != 0) sb.append(delim);
            sb.append(array[i]);
        }
        return sb;
    }

    /**
	 * combine list of paths
	 * 
	 * @param paths
	 * @return
	 */
    public static String combinePath(String[] paths) {
        String tmp = paths[0];
        for (int i = 1; i < paths.length; i++) {
            tmp = combinePath(tmp, paths[i]);
        }
        return tmp;
    }

    /**
	 * Combines the two input strings to produce a valid path.
	 * @param path1
	 * @param path2
	 * @return
	 */
    public static String combinePath(String path1, String path2) {
        return combinePath(path1, path2, "/");
    }

    /**
	 * @param path1
	 * @param path2
	 * @param pathSeperator
	 * @return
	 */
    public static String combinePath(String path1, String path2, String pathSeperator) {
        String path = null;
        if (pathSeperator == null || pathSeperator.trim().equals("")) {
            pathSeperator = File.separator;
        }
        if (path1 != null) path1 = path1.trim().replace('\\', '/'); else path1 = "";
        if (path2 != null) path2 = path2.trim().replace('\\', '/'); else path2 = "";
        if (path1.length() > 0 && path2.length() > 0) {
            if (path1.endsWith(pathSeperator)) {
                path = path1.substring(0, path1.length() - pathSeperator.length()) + pathSeperator + path2;
            } else {
                path = path1 + pathSeperator + path2;
            }
            if (path.endsWith(pathSeperator)) {
                path = path.substring(0, path.length() - pathSeperator.length());
            }
        } else {
            if (path1.length() == 0) path = path2; else if (path2.length() == 0) path = path1;
        }
        return path;
    }

    /**
	 * Pads a string s, with n spaces
	 * @param s
	 * @param n
	 * @return padded string
	 */
    public static String pad(String s, int n) {
        StringBuffer str = new StringBuffer(s);
        int strLength = str.length();
        if (n > 0 && n > strLength) {
            for (int i = strLength; i < n; i++) {
                str.append(' ');
            }
        } else {
            str.setLength(n);
        }
        return str.toString();
    }

    /**
     * Pads all the strings in the string array s with n spaces each.
     * @param s
     * @param n
     * @return
     */
    public static String padAll(String[] s, int n) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < s.length; i++) {
            str.append(pad(s[i], n));
        }
        return str.toString();
    }

    /**
	 * Reads the given inputstream till the end and returns the result.
	 * @param timeout
	 * @param output
	 * @param in
	 * @throws IOException
	 */
    public static String getResultFromInputStream(InputStream in, long timeout) throws IOException {
        StringBuffer output = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        long start = System.currentTimeMillis();
        String line = null;
        while ((line = br.readLine()) != null) {
            if (output.length() != 0) output.append("\n");
            output.append(line);
        }
        return output.toString();
    }

    /**
	 * Creates unique keys for each objectName in an application
	 * @param objectName
	 * @param applicationID
	 * @return key
	 */
    public static String makeKey(String objectName, String applicationID) {
        if (applicationID == null) throw new IllegalArgumentException("Application ID is null.");
        return objectName + "." + applicationID;
    }

    public static final String getSummaryStatistics(String applicationId, BrokerBase store) throws GridBrokerException {
        int columnWidth = 10;
        int numCols = 15;
        String headingStart = " : ";
        String lineBreak = "\n";
        StringBuffer sb = new StringBuffer();
        sb.append("\n\n --- Application performance statistics---- \n");
        sb.append(lineBreak).append(BrokerUtil.pad("Job Status", columnWidth)).append(headingStart);
        sb.append(BrokerUtil.padAll(new String[] { "Done", "Failed", "Active", "Pending", "Submtd", "Schedld", "StageIn", "StageOut", "Unknown", "Ready", "Total", "IsAlive", "JobLimit", "AvgCompT" }, columnWidth));
        sb.append(lineBreak);
        for (int i = 0; i <= (headingStart.length() + columnWidth * numCols); i++) sb.append("-");
        Collection servers = store.getServices(applicationId, ServiceType.COMPUTE);
        for (Iterator it = servers.iterator(); it.hasNext(); ) {
            ComputeServer cs = (ComputeServer) it.next();
            String serverHostname = cs.getHostname();
            JobStatistics stats = store.getJobStatistics(applicationId, cs.getName());
            int max = (serverHostname.length() - 1 > columnWidth) ? columnWidth : serverHostname.length();
            sb.append(lineBreak).append(BrokerUtil.pad(serverHostname.substring(0, max), columnWidth)).append(headingStart);
            sb.append(BrokerUtil.padAll(new String[] { stats.getDoneJobs() + "", stats.getFailedJobs() + "", stats.getActiveJobs() + "", stats.getPendingJobs() + "", stats.getSubmittedJobs() + "", stats.getScheduledJobs() + "", stats.getStageInJobs() + "", stats.getStageOutJobs() + "", stats.getUnknownJobs() + "", "-", stats.getTotalJobs() + "", (cs.isAvailable() ? "Y" : "-"), cs.getJobLimit() + "", cs.getAvgJobComputationTime() + "" }, columnWidth));
        }
        sb.append(lineBreak);
        for (int i = 0; i <= (headingStart.length() + columnWidth * numCols); i++) sb.append("-");
        JobStatistics totalStats = store.getJobStatistics(applicationId, Constants.ANY_SERVER);
        sb.append(lineBreak).append(BrokerUtil.pad("Total", columnWidth)).append(headingStart);
        sb.append(BrokerUtil.padAll(new String[] { totalStats.getDoneJobs() + "", totalStats.getFailedJobs() + "", totalStats.getActiveJobs() + "", totalStats.getPendingJobs() + "", totalStats.getSubmittedJobs() + "", totalStats.getScheduledJobs() + "", totalStats.getStageInJobs() + "", totalStats.getStageOutJobs() + "", totalStats.getUnknownJobs() + "", totalStats.getReadyJobs() + "", totalStats.getTotalJobs() + "" }, columnWidth));
        ApplicationContext app = store.getApplication(applicationId);
        Qos qos = app.getQos();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.SSS EEE, dd MMM yyyy");
        String deadline = df.format(new Date(qos.getDeadline()));
        sb.append(lineBreak).append(lineBreak);
        sb.append(BrokerUtil.pad("Deadline", columnWidth)).append(headingStart).append(deadline);
        sb.append(lineBreak);
        sb.append(BrokerUtil.pad("Time left", columnWidth)).append(headingStart).append(BrokerUtil.getHHMMSS(qos.getDeadline() - System.currentTimeMillis()));
        sb.append(lineBreak).append(lineBreak);
        sb.append(BrokerUtil.pad("Budget", columnWidth)).append(headingStart).append(qos.getBudget()).append(" G$");
        sb.append(lineBreak);
        sb.append(BrokerUtil.pad("Budg.spent", columnWidth)).append(headingStart).append(qos.getBudgetSpent()).append(" G$");
        sb.append(lineBreak);
        sb.append(lineBreak).append("Current Schedule - time spent so far: ").append(BrokerUtil.getHHMMSS(System.currentTimeMillis() - app.getStartTime()));
        return sb.toString();
    }

    public static String parseHostname(String serviceURL) {
        String hostname = null;
        try {
            if (serviceURL != null && serviceURL.startsWith("http")) {
                URL url = new URL(serviceURL);
                hostname = url.getHost();
            } else {
                hostname = serviceURL;
            }
        } catch (Exception e) {
        }
        return hostname;
    }
}
