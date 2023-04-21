package CADI.Proxy;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import CADI.Common.Log.CADILog;
import CADI.Common.Util.ArgumentsParser;
import CADI.Proxy.Core.ProxyPrefetching;
import CADI.Common.Network.TrafficShaping;
import GiciException.ErrorException;
import GiciException.ParameterException;

/**
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.0.1 2011/03/04
 */
public class ProxyParser extends ArgumentsParser {

    private static final String[][] serverArguments = { { "-p", "--ports", "{int[int [int [ ...]]]}", ProxyDefaultValues.PORT + "", "0", "1", "Ports where server will listen to the client request." }, { "-nt", "--numThreads", "{int}", ProxyDefaultValues.NUM_THREADS + "", "0", "1", "Number of threads that will be launched to process the request." }, { "-t", "--type", "{int}", ProxyDefaultValues.PROXY_TYPE + "", "0", "1", "Indicates the type of proxy that will be used. Allowed values are:\n" + "\t1- transparent proxy\n" + "\t2- cached proxy\n" + "\t3- cache proxy with prefetching\n" }, { "-pdh", "--prefetchingDataHistory", "{int}", "0", "0", "1", "Is the data, previous windows of interest requested, used to build the actual WOI to be prefetched. Allowed values:\n" + "\t" + ProxyPrefetching.MODE_ONLY_IMAGE_HISTORY + "- uses the history of the windows of interest requested by all clients over an image. View windows are sorted following a FIFO strategy for all clients.\n" + "\t" + ProxyPrefetching.MODE_ONLY_CLIENT_HISTORY + "- prediction of windows of interest to be downloaded are done for each client taken into account only the historic wois for that client.\n" + "\t" + ProxyPrefetching.MODE_LAST_WOI_ALL_CLIENTS + "- computes the prefetching woi using the lastest window of interest requested by all clients over each image.\n " + "\tOBS: This option can only be set when the --type parameter is 3 (cached proxy with prefetching)." }, { "-pwt", "--prefetchingWOIType", "{int}", "0", "0", "1", "Allows to choose how is built the Windows of Interest to be prefetching from de data selected with the option \"-pm\" . Allowed values:\n" + "\t" + ProxyPrefetching.WOI_TYPE_WEIGHTED_WOI + "- prediction of the window of interest to be prefetched  is base on weighted woi of the historic window of interest.\n" + "\t" + ProxyPrefetching.WOI_TYPE_BOUNDING_BOX + "- the window of interext to be prefected is the bounding box of the historic window of interest.\n" + "\tOBS: This option can only be set when the --type parameter is 3 (cached proxy with prefetching)." }, { "-mp", "--movementProbabilities", "{float float float float float float float float float float}", "0.1", "0", "1", "Probabilities of the movements to be used by the prefetching. Values must be sorted according with the following criterion: " + "right, up-right, up, up-left, left, down-left, down, down_right, zoom in, zoom out.\n" + "\tOBS: The sum of all values must be less or equal than 1.\n" + "\tOBS: This option can only be set when the --type parameter is 3 (cached proxy with prefetching)." }, { "-lf", "--logFile", "{string}", "", "0", "1", "File where logs are saved." }, { "-lx", "--logXML", "{boolean}", ProxyDefaultValues.XML_LOGFILE_FORMAT ? "1" : "0", "0", "1", "XML format is used in the log file. Value is a boolean: 0 indicates simple file format is used and 1 indicates XML format is used." }, { "-le", "--logEnabled", "{boolean}", "", "0", "1", "Enables or disables the log. See the \"-ll\" parameter for more information about the detail level of logs." }, { "-ll", "--logLevel", "{int}", CADILog.LEVEL_INFO + "", "0", "1", "Is the severity of the messages which will be logged. The \"-le\" parameter is set automatically. Available values are:\n" + "\t" + CADILog.LEVEL_INFO + "- logs informative messages\n" + "\t" + CADILog.LEVEL_WARNING + "- logs warning messages\n" + "\t" + CADILog.LEVEL_ERROR + "- logs error messages\n" + "when a log level is set, all upper levels are automatically set but lower severity messages are filtered." }, { "-cd", "--cacheDirectory", "{string}", "", "0", "1", "Directory used as a temporal directory to save the cache data (not implemented yet)." }, { "-mr", "--maxRate", "{int}", "0", "0", "1", "Specifies the maximum rate (bytes per second) which will be used to delivery data (0 means unlimited)." }, { "-ts", "--trafficShaping", "{int}", "0", "0", "1", "Allows to choose a trafic shaping algorithm. Allowed values are:" + "\t" + TrafficShaping.NONE + "- None algoritm is applied." + "\t" + TrafficShaping.TOKEN_BUCKET + "- The token-bucket algoritm is applied. Data are transmitted at the constant rate fixed by the \"-mr\" parameter rate, but it also allows data busts." + "\t" + TrafficShaping.LEAKY_BUCKET + "- The leaky-bucket algoritm is applied. Data are delivered at the constant rate defined in the \"-mr\" option." + "OBS: This parameter requires the \"-mr\" parameter." }, { "-h", "--help", "", "", "0", "1", "Displays this help and exits program." }, { "-w", "--warranty", "", "", "0", "1", "" }, { "-l", "--liability", "", "", "0", "1", "" }, { "-c", "--copyright", "", "", "0", "1", "" } };

    private int[] ports = null;

    private int prefetchingDataHistory = -1;

    private int prefetchingWOIType = -1;

    private int numThreads = 0;

    private int proxyType = ProxyDefaultValues.PROXY_TYPE;

    private String logFile = null;

    private boolean XMLLogFormat = false;

    private boolean logEnabled = false;

    private int logLevel = -1;

    private String cacheDirectory = null;

    private int maxRate = 0;

    private int trafficShaping = TrafficShaping.NONE;

    private float[] movProbabilities = null;

    /**
   * Receives program arguments and parses it, setting to arguments variables.
   *
   * @param arguments the array of strings passed at the command line
   *
   * @throws ParameterException when an invalid parsing is detected
   * @throws ErrorException when some problem with method invocation occurs
   */
    public ProxyParser(String[] arguments) throws ParameterException, ErrorException {
        logLevel = (new CADILog()).getLogLevel();
        try {
            Method m = this.getClass().getMethod("parseArgument", new Class[] { int.class, String[].class });
            parse(serverArguments, arguments, this, m);
        } catch (NoSuchMethodException e) {
            throw new ErrorException("Coder parser error invoking parse function.");
        }
        if (!(proxyType == ProxyDefaultValues.CACHED_PREFETCHING_PROXY) && ((prefetchingDataHistory != -1) || (prefetchingWOIType != -1))) {
            throw new ParameterException("\"pdh\" or \"pdt\" ara only allowed with \"-t\" equals to " + ProxyDefaultValues.CACHED_PREFETCHING_PROXY);
        }
        if (proxyType == ProxyDefaultValues.CACHED_PREFETCHING_PROXY) {
            if (prefetchingDataHistory == -1) {
                prefetchingDataHistory = ProxyPrefetching.MODE_ONLY_IMAGE_HISTORY;
            }
            if (prefetchingWOIType == -1) {
                prefetchingWOIType = ProxyPrefetching.WOI_TYPE_WEIGHTED_WOI;
            }
        }
        if (movProbabilities != null) {
            float sum = 0;
            for (float prob : movProbabilities) {
                sum += prob;
            }
            if (sum > 1.00001F) {
                throw new ParameterException("The sum of the movement probabilities must be less or equal than 1");
            }
        }
    }

    /**
   * Parse an argument using parse functions from super class and put its
   * value/s to the desired variable. This function is called from parse
   * function of the super class.
   *
   * @param argFound number of parameter (the index of the array coderArguments)
   * @param options the command line options of the argument
   *
   * @throws ParameterException when some error about parameters passed (type, number of params, etc.) occurs
   */
    public void parseArgument(int argFound, String[] options) throws ParameterException {
        switch(argFound) {
            case 0:
                ports = this.parseIntegerArray(options);
                break;
            case 1:
                numThreads = parseIntegerPositive(options);
                break;
            case 2:
                proxyType = parseIntegerPositive(options);
                break;
            case 3:
                prefetchingDataHistory = parseIntegerPositive(options);
                break;
            case 4:
                prefetchingWOIType = parseIntegerPositive(options);
                break;
            case 5:
                movProbabilities = parseFloatArray(options, 10);
                break;
            case 6:
                logFile = parseString(options);
                break;
            case 7:
                XMLLogFormat = parseBoolean(options);
                break;
            case 8:
                logEnabled = true;
                break;
            case 9:
                logLevel = parseIntegerPositive(options);
                break;
            case 10:
                cacheDirectory = parseString(options);
                break;
            case 11:
                maxRate = parseIntegerPositive(options);
                break;
            case 12:
                trafficShaping = parseIntegerPositive(options);
                break;
            case 13:
                try {
                    Properties cadiInfo = new Properties();
                    InputStream cadiInfoURL = getClass().getClassLoader().getResourceAsStream("CADI/Common/Info/cadiInfo.properties");
                    cadiInfo.load(cadiInfoURL);
                    System.out.println("CADIProxy version " + cadiInfo.getProperty("version") + "\n");
                } catch (Exception e) {
                    System.out.println("PARAMETERS ERROR: error reading properties file.");
                    System.out.println("Please report this error to: gici-dev@deic.uab.es");
                }
                showArgsInfo();
                System.exit(0);
                break;
            case 14:
                printWarranty();
                System.exit(0);
                break;
            case 15:
                printLiability();
                System.exit(0);
                break;
            case 16:
                printCopyright();
                System.exit(0);
                break;
            default:
                assert (true);
        }
    }

    public int[] getPorts() {
        return ports;
    }

    public int getPrefetchingDataHistory() {
        return prefetchingDataHistory;
    }

    public int getPrefetchingWOIType() {
        return prefetchingWOIType;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public int getProxyType() {
        return proxyType;
    }

    public String getLogFile() {
        return logFile;
    }

    public boolean isXMLLogFormat() {
        return XMLLogFormat;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public String getCacheDirectory() {
        return cacheDirectory;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public int getTrafficShaping() {
        return trafficShaping;
    }

    public float[] getMovementProbabilities() {
        return movProbabilities;
    }

    /**
   * Prints out the warranty.
   */
    private void printWarranty() {
        try {
            Properties cadiInfo = new Properties();
            InputStream cadiInfoURL = getClass().getClassLoader().getResourceAsStream("CADI/Common/Info/cadiInfo.properties");
            cadiInfo.load(cadiInfoURL);
            System.out.println("CADIClient version " + cadiInfo.getProperty("version") + "\n");
            System.out.println(cadiInfo.getProperty("disclaimerOfWarranty"));
        } catch (Exception e) {
            System.out.println("PARAMETERS ERROR: error reading the disclaimer of warranty");
            System.out.println("Please report this error to: gici-dev@deic.uab.es");
        }
    }

    /**
   * Prints out the liability.
   */
    private void printLiability() {
        try {
            Properties cadiInfo = new Properties();
            InputStream cadiInfoURL = getClass().getClassLoader().getResourceAsStream("CADI/Common/Info/cadiInfo.properties");
            cadiInfo.load(cadiInfoURL);
            System.out.println("CADIClient version " + cadiInfo.getProperty("version") + "\n");
            System.out.println(cadiInfo.getProperty("limitationsOfLiability"));
        } catch (Exception e) {
            System.out.println("PARAMETERS ERROR: error reading limitations of liability");
            System.out.println("Please report this error to: gici-dev@deic.uab.es");
        }
    }

    /**
   * Prints out the copyright.
   */
    private void printCopyright() {
        try {
            Properties cadiInfo = new Properties();
            InputStream cadiInfoURL = getClass().getClassLoader().getResourceAsStream("CADI/Common/Info/cadiInfo.properties");
            cadiInfo.load(cadiInfoURL);
            System.out.println("CADIClient version " + cadiInfo.getProperty("version") + "\n");
            System.out.println(cadiInfo.getProperty("copyright"));
        } catch (Exception e) {
            System.out.println("PARAMETERS ERROR: error reading copyright");
            System.out.println("Please report this error to: gici-dev@deic.uab.es");
        }
    }
}
