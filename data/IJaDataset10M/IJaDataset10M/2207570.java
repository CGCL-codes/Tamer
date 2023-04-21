package org.nees.rbnb;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Enumeration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nees.daq.*;
import com.rbnb.sapi.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This takes the information from a file and puts in into ring buffers on an RBNB
 * server, using the channel names implied by the file. Timestamp information is
 * also taken from the file. See RbnbToFile for information on format and missing
 * values.
 * 
 * 
 */
public class FileToRbnb extends RBNBBase {

    static Log log = LogFactory.getLog(FileToRbnb.class.getName());

    private static final String SOURCE_NAME = "FileDump";

    private static final String CHANNEL_NAME = "data.txt";

    private static final String ARCHIVE_DIRECTORY = ".";

    private static final String DATA_FILE_NAME = "Data.txt";

    static final String DELIMITER = "\t";

    private static final long REPLAY_INTERVAL = 0;

    private String sourceName = SOURCE_NAME;

    private String channelName = CHANNEL_NAME;

    private static final int DEFAULT_CACHE_SIZE = 900;

    private int cacheSize = DEFAULT_CACHE_SIZE;

    private static final int DEFAULT_ARCHIVE_SIZE = DEFAULT_CACHE_SIZE * 2;

    private int archiveSize = DEFAULT_ARCHIVE_SIZE;

    private String archiveDirectory = ARCHIVE_DIRECTORY;

    private String dataFileName = DATA_FILE_NAME;

    private String delimiter = DELIMITER;

    private BufferedReader rd;

    private double timeOffset = 0.0;

    /** LJM 060519
  * variable to hold the time (in hours) desired for the length of the ring buffer
  * used to calculate cache and archive.
  */
    private double rbTime = -1.0;

    /** a variable to set what percentage of the archived frames are to be
  * cached by the rbnb server.
  */
    private static final double DEFAULT_CACHE_PERCENT = 10;

    private double rbCachePercent = DEFAULT_CACHE_PERCENT;

    Source source = null;

    ChannelMap sMap;

    int index;

    boolean connected = false;

    boolean detach = true;

    long replayInterval = REPLAY_INTERVAL;

    Properties headers = new Properties();

    public FileToRbnb() {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                try {
                    disconnect();
                    System.out.println("Shutdown hook for " + FileToRbnb.class.getName());
                } catch (Exception e) {
                    System.out.println("Unexpected error closing " + FileToRbnb.class.getName());
                }
            }
        });
    }

    private void computeDefaultTimeOffset() {
        Calendar calendar = new GregorianCalendar();
        long tz = calendar.get(Calendar.ZONE_OFFSET);
        long dt = calendar.get(Calendar.DST_OFFSET);
        System.out.println("Default time: Time Zone offset: " + (-((double) (tz / 1000)) / (60.0 * 60.0)));
        System.out.println("Default time: Daylight Savings Time offset (in hours): " + (-((double) (dt / 1000)) / (60.0 * 60.0)));
        timeOffset = -(double) ((tz + dt) / 1000);
    }

    public static void main(String[] args) {
        FileToRbnb w = new FileToRbnb();
        if (w.parseArgs(args)) {
            w.connect();
            w.doIt();
        }
    }

    protected String getCVSVersionString() {
        return ("$LastChangedDate: 2008-04-15 20:12:19 -0400 (Tue, 15 Apr 2008) $\n" + "$LastChangedRevision: 36 $" + "$LastChangedBy: ljmiller.ucsd $" + "$HeadURL: http://oss-dataturbine.googlecode.com/svn/trunk/apps/oss-apps/src/org/nees/rbnb/FileToRbnb.java $");
    }

    protected Options setOptions() {
        Options opt = setBaseOptions(new Options());
        opt.addOption("n", true, "source_name *" + SOURCE_NAME);
        System.out.println("SOURCE_NAMe: " + SOURCE_NAME);
        opt.addOption("c", true, "channel_name *" + CHANNEL_NAME);
        opt.addOption("d", true, "Archive directory root *" + ARCHIVE_DIRECTORY);
        opt.addOption("f", true, "Data input file name *" + DATA_FILE_NAME);
        opt.addOption("S", true, "Data item seperator (default is tab, \\t)");
        opt.addOption("R", true, "Replay interval in microseconds *" + REPLAY_INTERVAL);
        opt.addOption("Q", false, "Supply this flag to force the RBNB ring buffer " + "to close when the application quits.");
        double hours = timeOffset / (60.0 * 60.0);
        opt.addOption("o", true, "time offset, floating point, hours to GMT *" + hours);
        opt.addOption("z", true, "cache size *" + DEFAULT_CACHE_SIZE);
        opt.addOption("Z", true, "archive size *" + DEFAULT_ARCHIVE_SIZE);
        setNotes("The reply interval pauses between each data sent. " + "To send all the data without pause set the replay interval to zero.");
        return opt;
    }

    protected boolean setArgs(CommandLine cmd) {
        if (!setBaseArgs(cmd)) return false;
        if (cmd.hasOption('n')) {
            String a = cmd.getOptionValue('n');
            if (a != null) sourceName = a;
        }
        if (cmd.hasOption('c')) {
            String a = cmd.getOptionValue('c');
            if (a != null) channelName = a;
        }
        if (cmd.hasOption('d')) {
            String a = cmd.getOptionValue('d');
            if (a != null) archiveDirectory = a;
        }
        if (cmd.hasOption('f')) {
            String a = cmd.getOptionValue('f');
            if (a != null) dataFileName = a;
        }
        if (cmd.hasOption('Q')) {
            detach = false;
        }
        if (cmd.hasOption('R')) {
            String a = cmd.getOptionValue('R');
            if (a != null) try {
                long value = Long.parseLong(a);
                replayInterval = value;
            } catch (NumberFormatException nfe) {
                System.out.println("Please ensure to enter a numeric value for -R option. " + a + " is not valid!");
                return false;
            }
        }
        if (cmd.hasOption('z')) {
            String a = cmd.getOptionValue('z');
            if (a != null) try {
                Integer i = new Integer(a);
                int value = i.intValue();
                cacheSize = value;
            } catch (NumberFormatException nfe) {
                System.out.println("Please ensure to enter a numeric value for -z option. " + a + " is not valid!");
                return false;
            }
        }
        if (cmd.hasOption('Z')) {
            String a = cmd.getOptionValue('Z');
            if (a != null) try {
                Integer i = new Integer(a);
                int value = i.intValue();
                archiveSize = value;
            } catch (NumberFormatException nfe) {
                System.out.println("Please ensure to enter a numeric value for -Z option. " + a + " is not valid!");
                return false;
            }
        }
        if (detach && (archiveSize == 0)) {
            System.out.println("Warning, the detach flag is true and there is " + "no archive; you will may not be able to attach to this Source");
        }
        if ((archiveSize > 0) && (archiveSize < cacheSize)) {
            System.out.println("Archive size = " + archiveSize + "; it must be " + "bigger than chache size; chache size = " + cacheSize);
            return false;
        }
        if (cmd.hasOption('o')) {
            String a = cmd.getOptionValue('o');
            if (a != null) try {
                double value = Double.parseDouble(a);
                timeOffset = (long) (value * 60.0 * 60.0);
            } catch (NumberFormatException nf) {
                System.out.println("Please ensure to enter a numeric value for -o option. " + a + " is not valid!");
                return false;
            }
        }
        System.out.println("  Channel name = " + channelName + "  Cache Size = " + cacheSize + "; Archive size = " + archiveSize);
        if (replayInterval > 0) System.out.println("Replay pause interval = " + replayInterval);
        if (detach) System.out.println("The Source will be detached on exit");
        System.out.println("  Use FileToRbnb -h to see optional parameters");
        return true;
    }

    public void postFileWithDefaults(String serverName, String serverPort, File file, String postChannelName) {
        if (setArgs(serverName, serverPort, SOURCE_NAME, postChannelName, file.getParent(), file.getName(), REPLAY_INTERVAL, true, DEFAULT_CACHE_SIZE, DEFAULT_ARCHIVE_SIZE)) {
            connect();
            doIt();
            disconnect();
        }
    }

    /**
   * Set the class instance parameters. 
   * 
   * @param serverName - the name of the RBNB server (e.g. neestpm.sdsc.edu)
   * @param serverPort - the (String) RBNB server port (e.g. 3333)
   * @param sourceName - the name of the RBNB source to generate
   * @param channelName - the name of the RBNB channel to generate
   * @param archiveDirectory - the name of the archive directory to use
   * @param dataFileName - the name of the file on the archive directory
   *      to use
   * @param replayInterval - if > 0, the milliseconds to pause between
   *      each data item (to simulate real time sending of data, e.g. 
   *      a replay)
   * @param keepLive - (booelan) if true, then keep the RBNB source and
   *      channel up after the call quits, that is "detach" instead of 
   *      close; if false, close (and loose) the source/channel at the
   *      end of the send
   * @param rbnbCacheSize - (long) the RBNB cache size
   * @param rbnbArchiveSize - (long) the RBNB archive size (if > 0, it
   *      must be >= the sache size)
   * 
   * @return (boolean) true if the parameters are valid; false otherwise
   * 
   */
    public boolean setArgs(String serverName, String serverPort, String sourceName, String channelName, String archiveDirectory, String dataFileName, long replayInterval, boolean keepLive, int rbnbCacheSize, int rbnbArchiveSize) {
        this.setServerName(serverName);
        this.setServerName(serverPort);
        this.sourceName = sourceName;
        this.channelName = channelName;
        this.archiveDirectory = archiveDirectory;
        this.dataFileName = dataFileName;
        this.replayInterval = replayInterval;
        this.detach = !keepLive;
        this.cacheSize = rbnbCacheSize;
        this.archiveSize = rbnbArchiveSize;
        if (detach && (archiveSize == 0)) {
            System.out.println("Warning, the detach flag is true and there is " + "no archive; you will may not be able to attach to this Source");
        }
        if ((archiveSize > 0) && (archiveSize < cacheSize)) {
            System.out.println("Archive size = " + archiveSize + "; it must be " + "bigger than cache size; cache size = " + cacheSize);
            return false;
        }
        System.out.println("  Channel name = " + channelName + "  Cache Size = " + cacheSize + "; Archive size = " + archiveSize);
        if (replayInterval > 0) System.out.println("Replay pause interval = " + replayInterval);
        if (detach) System.out.println("The Source will be detached on exit");
        System.out.println("  Use FileToRbnb -h to see optional parameters");
        return true;
    }

    public void connect() {
        System.out.println("FileToRbnb: Attempting to connect to server = " + getServer() + " as " + sourceName + " with " + channelName + ".");
        try {
            if (archiveSize > 0) source = new Source(cacheSize, "append", archiveSize); else source = new Source(cacheSize, "append", 0);
            source.OpenRBNBConnection(getServer(), sourceName);
            connected = true;
            System.out.println("FileToRbnb - connect() : Connection made to server = " + getServer() + " as " + sourceName + " with " + channelName + ".");
            System.out.println("with RBNB Cache Size = " + cacheSize + "and RBNB Archive Size = " + archiveSize);
        } catch (SAPIException se) {
            se.printStackTrace();
        }
        if (connected) {
            System.out.println("Connected!");
            String path = archiveDirectory + "/" + dataFileName;
            System.out.println("connect() - path: " + path);
            File probe = new File(path);
            if (probe != null && probe.exists() && probe.canRead()) {
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (IOException ignore) {
                    }
                    rd = null;
                }
                try {
                    rd = new BufferedReader(new FileReader(probe));
                    System.out.println("Sucessfully connected to " + path);
                } catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                }
                if (rd == null) {
                    System.out.println("Failed to open file stream " + path);
                    disconnect();
                }
            } else {
                System.out.println("Data unavailable: path...");
                if (probe == null) System.out.println("Could not open file."); else if (!probe.exists()) System.out.println("File does not exist... " + path); else if (!probe.canRead()) System.out.println("File is unreadable");
                disconnect();
            }
        }
    }

    public void disconnect() {
        try {
            if (detach) source.Detach(); else source.CloseRBNBConnection();
            connected = false;
            source = null;
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException ignore) {
                }
                rd = null;
                System.out.println("disconnect() - normal disconnect");
            }
        } catch (Exception e) {
            System.out.println("disconnect() - error: " + e.getMessage());
        }
    }

    public void doIt() {
        String in = "";
        boolean header = true;
        if (!connected) return;
        String[] units = new String[0];
        try {
            parseHeaders();
            String[] channelNameArray = new String[getChannelCount()];
            ChannelMap map = new ChannelMap();
            populateChannelMap(map, channelNameArray);
            source.Register(map);
            String[] st = null;
            String tsString;
            while (((in = rd.readLine()) != null) && connected) {
                int index = 0;
                st = in.split(delimiter);
                while (st[index].length() == 0) index++;
                tsString = st[index++];
                ISOtoRbnbTime timestamp = new ISOtoRbnbTime(tsString);
                if (!timestamp.is_valid) {
                    if (log.isWarnEnabled()) {
                        log.warn("Warning: timestamp not valid: " + tsString);
                    }
                }
                double time = timestamp.getValue();
                map.PutTime(time + timeOffset, 0.0);
                if (log.isDebugEnabled()) {
                }
                double[] data = new double[1];
                int chanIndex;
                String item;
                for (int j = 0; index < st.length; j++, index++) {
                    data = new double[1];
                    item = st[index].trim();
                    data[0] = Double.parseDouble(item);
                    try {
                        chanIndex = map.GetIndex(channelNameArray[j]);
                        map.PutDataAsFloat64(chanIndex, data);
                    } catch (SAPIException se) {
                        log.error("SAPI exception: " + se.getMessage());
                    } catch (NumberFormatException ignore) {
                        log.error("NumberFormatException " + ignore.getMessage());
                    } catch (Exception ex) {
                        log.error("Exception " + ex.getMessage());
                    }
                }
                source.Flush(map, true);
                Thread.sleep(0);
                try {
                    if (replayInterval > 0) Thread.sleep(replayInterval);
                } catch (Exception ignore) {
                }
            }
            String unitsArray[];
            unitsArray = getUnitsArray(channelNameArray.length);
            postUnits(channelNameArray, unitsArray);
        } catch (Exception e) {
            log.error("Genral error parsing the file : " + e.getMessage());
        }
    }

    private void parseHeaders() throws IOException {
        String in;
        int colonIndex;
        String name, value;
        while ((in = rd.readLine()) != null && in.length() > 0) {
            colonIndex = in.indexOf(":");
            if (colonIndex > -1) {
                name = in.substring(0, colonIndex);
                value = in.substring(colonIndex + 2);
                headers.setProperty(name, value);
            }
        }
        Enumeration en = headers.propertyNames();
        while (en.hasMoreElements()) {
            name = (String) en.nextElement();
            value = headers.getProperty(name);
        }
    }

    public int getChannelCount() {
        if (headers.containsKey("CHAN_COUNT")) return (Integer.parseInt(headers.getProperty("CHAN_COUNT")));
        return 0;
    }

    void populateChannelMap(ChannelMap map, String channelNames[]) throws SAPIException {
        String token;
        String chanList = headers.getProperty("CHAN_NAMES");
        String[] st = chanList.split(delimiter);
        int chanIndex = 0;
        for (int i = 0; i < st.length; i++) {
            token = st[i];
            if (token.compareToIgnoreCase("time") != 0 && token.compareToIgnoreCase("time_ms") != 0 && token.length() > 0) {
                channelNames[chanIndex++] = token;
                map.Add(token);
            }
        }
    }

    private void postUnits(String[] channelNames, String[] units) throws SAPIException {
        int index[] = new int[channelNames.length];
        ChannelMap cMap = new ChannelMap();
        for (int i = 0; i < channelNames.length; i++) {
            index[i] = cMap.Add(channelNames[i]);
            System.out.println("Added channel " + channelNames[i] + " with index " + index[i]);
        }
        for (int i = 0; i < units.length; i++) {
            try {
                cMap.PutUserInfo(index[i], "units=" + units[i] + ",scale=1,offset=0");
            } catch (SAPIException se) {
                log.error("postUnits error : " + se.getMessage());
            }
        }
        source.Register(cMap);
    }

    private String[] getUnitsArray(int channelsSize) {
        String newUnits;
        String unitsStr = headers.getProperty("CHAN_UNITS");
        StringTokenizer stk = new StringTokenizer(unitsStr, "\t ,");
        String unitsArray[] = new String[channelsSize];
        int idx = 0;
        while (stk.hasMoreTokens()) {
            newUnits = stk.nextToken();
            if (newUnits.length() > 0) {
                unitsArray[idx++] = newUnits;
            }
        }
        return unitsArray;
    }

    private void getUnitsString(String nameArray[], StringBuffer retBuf) {
        String newUnits;
        String unitsList = headers.getProperty("CHAN_UNITS");
        StringTokenizer st = new StringTokenizer(unitsList, "\t ,");
        String unitsArray[] = new String[nameArray.length];
        int i = 0;
        while (st.hasMoreTokens()) {
            newUnits = st.nextToken();
            if (newUnits.length() > 0) {
                unitsArray[i++] = newUnits;
            }
        }
        for (i = 0; i < nameArray.length; i++) {
            retBuf.append(nameArray[i]);
            retBuf.append('=');
            retBuf.append(unitsArray[i]);
            if (i < nameArray.length - 1) retBuf.append(',');
        }
    }

    private void putUnitsString(String units) throws SAPIException {
        Source s;
        s = new Source(100, "append", 400);
        try {
            s.OpenRBNBConnection(getServer(), "_Units");
        } catch (SAPIException e) {
            s = new Source(100, "create", 400);
            s.OpenRBNBConnection(getServer(), "_Units");
        }
        System.out.println("opened connection to " + getServer() + " for units");
        ChannelMap cMap = new ChannelMap();
        int index = cMap.Add(source.GetClientName());
        System.out.println("Units - channelMap added to " + source.GetClientName());
        cMap.PutMime(index, "text/plain");
        cMap.PutTimeAuto("timeofday");
        cMap.PutDataAsString(index, units);
        s.Flush(cMap);
        s.Detach();
        System.out.println("(units)PutDataAsString " + units);
    }
}
