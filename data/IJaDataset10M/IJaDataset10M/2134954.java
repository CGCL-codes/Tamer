package org.nees.rbnb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Vector;
import java.util.Iterator;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import com.rbnb.sapi.*;

/**
 * Capture data from each of several channels to a seperate file; each channel is writen
 * to its own file. The application builds a directory structure that reflects the channel
 * structure of the sources. Unlike RbnbToFile No assumption is made about the relitive
 * time of the channels. The data is written to the files using this format:
 * 
 * Active channels: ATL1
 * Channel units: g
 * 
 * Time ATL1
 * 2002-11-13T15:48:55.26499 -0.006409
 * 2002-11-13T15:48:55.36499 -0.005798
 * 2002-11-13T15:48:55.46499 -0.005798
 * 2002-11-13T15:48:55.56499 -0.005798
 * 
 * @see FileToRbnb
 * @see RbnbToFile
 * 
 * @author terry
 */
public class GrabDataMultipleSink extends RBNBBase {

    private static final String SINK_NAME = "_GatherData";

    private static final String ARCHIVE_DIRECTORY = ".";

    private static final String ROOT_FILENAME = "Data.txt";

    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss.SSS");

    private static final SimpleDateFormat COMMAND = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss.SSS");

    private static final TimeZone TZ = TimeZone.getTimeZone("GMT");

    static {
        OUTPUT_FORMAT.setTimeZone(TZ);
        COMMAND.setTimeZone(TZ);
    }

    private String sinkName = SINK_NAME;

    private String archiveDirectory = ARCHIVE_DIRECTORY;

    private String fileName = ROOT_FILENAME;

    private String startTimeString = "now";

    private double startTime = 0.0;

    private String endTimeString = "forever";

    private double endTime = 0.0;

    private double duration = 0.0;

    private String channelPathPattern = null;

    private String channelPathListString = null;

    private String[] channelPathArray;

    private String[] shortNameArray;

    private String[] channelUnits;

    private boolean includeHidden = false;

    public static void main(String[] args) {
        try {
            GrabDataMultipleSink s = new GrabDataMultipleSink();
            if (s.parseArgs(args)) s.exec();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    protected String getCVSVersionString() {
        return "  CVS information... \n" + "  $Revision: 36 $\n" + "  $Date: 2008-04-15 20:12:19 -0400 (Tue, 15 Apr 2008) $\n" + "  $RCSfile: GrabDataMultipleSink.java,v $ \n";
    }

    protected Options setOptions() {
        Options opt = setBaseOptions(new Options());
        opt.addOption("k", true, "Base Sink Name *" + SINK_NAME);
        opt.addOption("c", true, "Data Channels Path Pattern - Perl-like pattern to match");
        opt.addOption("C", true, "Data Channels list - comma seperated list of Paths");
        opt.addOption("d", true, "Archive directory root *" + ARCHIVE_DIRECTORY);
        opt.addOption("x", false, "Flag to include hidden channels");
        opt.addOption("S", true, "Start time (defauts to now)");
        opt.addOption("E", true, "End time (defaults to forever)");
        opt.addOption("D", true, "Duration, floating point seconds");
        setNotes("Writes data between start time and end time to " + "the a directory structure on the given directory " + "such that each directory reflects the channel path of origin. " + "Time format is yyyy-mm-dd:hh:mm:ss.nnn; " + "a zero or unspecified start time means 'now'; " + "a zero or unspecified end time means 'forever'. " + "If a duration is specifed, it overrides End time.");
        return opt;
    }

    protected boolean setArgs(CommandLine cmd) {
        if (!setBaseArgs(cmd)) return false;
        if (cmd.hasOption('k')) {
            String a = cmd.getOptionValue('k');
            if (a != null) sinkName = a;
        }
        if (cmd.hasOption('c')) {
            String a = cmd.getOptionValue('c');
            if (a != null) channelPathPattern = a;
        }
        if (cmd.hasOption('C')) {
            String a = cmd.getOptionValue('C');
            if (a != null) channelPathListString = a;
        }
        if (cmd.hasOption('x')) {
            includeHidden = true;
        }
        if (cmd.hasOption('d')) {
            String a = cmd.getOptionValue('d');
            if (a != null) archiveDirectory = a;
        }
        if (cmd.hasOption('S')) {
            String a = cmd.getOptionValue('S');
            if (a != null) {
                try {
                    startTimeString = a;
                    Date d = COMMAND.parse(a);
                    long t = d.getTime();
                    startTime = ((double) t) / 1000.0;
                } catch (Exception e) {
                    System.out.println("Parse of start time failed " + a);
                    printUsage();
                    return false;
                }
            }
        }
        if (cmd.hasOption('E')) {
            String a = cmd.getOptionValue('E');
            if (a != null) {
                try {
                    endTimeString = a;
                    Date d = COMMAND.parse(a);
                    long t = d.getTime();
                    endTime = ((double) t) / 1000.0;
                } catch (Exception e) {
                    System.out.println("Parse of end time failed " + a);
                    printUsage();
                    return false;
                }
            }
        }
        if (duration > 0.0) {
            long unixTime = System.currentTimeMillis();
            startTimeString = COMMAND.format(new Date(unixTime));
            startTime = ((double) unixTime) / 1000.0;
            double time = startTime + duration;
            endTimeString = COMMAND.format(new Date((long) (time * 1000.0)));
            if (endTime > 0.0) {
                System.out.println("Warning - both duration and end time were specified, " + "duration overrides endtime. New endtime is " + endTimeString);
            }
            endTime = time;
            System.out.println("Using duration updates endTime and set startTime when it is not set.");
            System.out.println("  StartTime = " + startTimeString + "; EndTime = " + endTimeString);
        }
        if ((startTime != 0.0) && (endTime != 0.0) && (startTime >= endTime)) {
            System.out.println("StartTime = " + startTimeString + "; EndTime = " + endTimeString);
            System.out.println("  End time (" + endTime + ") " + "does not come after start time (" + startTime + ").");
            return false;
        }
        System.out.println("User Supplied parameters (or relivent default values) are:");
        System.out.println("  Server (combines host and port) = " + getServer());
        System.out.println("  Sink name = " + sinkName);
        System.out.println("  Data Channel Path pattern = " + channelPathPattern);
        System.out.println("  Data Channel Path list = " + channelPathListString);
        System.out.println("  Flag to include hidden channel value = " + includeHidden);
        System.out.println("  Archive directory = " + archiveDirectory);
        System.out.println("  Start Time = " + startTimeString);
        System.out.println("  End Time = " + endTimeString);
        System.out.println("  Duration = " + duration);
        return true;
    }

    public void exec() throws IllegalArgumentException, SAPIException, IOException {
        setupChannelArrays();
        channelUnits = ChannelUtility.getUnits(getServer(), channelPathArray);
        if (channelPathArray.length == 0) {
            System.out.println("GrabDataMultipleSink: No channels to monitor.");
            return;
        }
        System.out.println("Monitor on channels:");
        for (int i = 0; i < channelPathArray.length; i++) System.out.println("  " + channelPathArray[i]);
        for (int i = 0; i < channelPathArray.length; i++) {
            String path = channelPathArray[i];
            String name = shortNameArray[i];
            String units = channelUnits[i];
            new GatherDataSink(path, name, units).exec();
        }
    }

    private void setupChannelArrays() throws IllegalArgumentException, SAPIException {
        Vector channelPathList = new Vector();
        Iterator channels;
        if ((channelPathPattern != null) && !channelPathPattern.equals("")) channelPathList = ChannelUtility.appendChannelListFromPattern(getServer(), includeHidden, channelPathPattern, channelPathList);
        if ((channelPathListString != null) && !channelPathListString.equals("")) channelPathList = ChannelUtility.appendChannelListFromString(getServer(), includeHidden, channelPathListString, channelPathList);
        if (channelPathList.size() == 0) {
            String message = "RbnbToFile: No data channels to monitor: ";
            if (channelPathPattern == null) message += "channelPathPattern is null"; else if (!channelPathPattern.equals("")) message += "channelPathPattern is empty (zero length)"; else message += "channelPathPattern = " + channelPathPattern;
            message += " and ";
            if (channelPathListString == null) message += "channelPathListString is null"; else if (!channelPathListString.equals("")) message += "channelPathListString is empty (zero length)"; else message += "channelPathListString = " + channelPathListString;
            throw new IllegalArgumentException(message);
        }
        channelPathArray = new String[channelPathList.size()];
        shortNameArray = new String[channelPathList.size()];
        channels = channelPathList.iterator();
        for (int i = 0; i < channelPathArray.length; i++) {
            ChannelUtility.NodeCover candidate = (ChannelUtility.NodeCover) channels.next();
            channelPathArray[i] = candidate.getFullName();
            shortNameArray[i] = candidate.getName();
        }
    }

    private class GatherDataSink {

        Sink sink = null;

        String gatherSinkName;

        PrintWriter out = null;

        boolean connected = false;

        Thread stringDataThread;

        boolean runit = false;

        String fullPath, shortName, units;

        public GatherDataSink(String fullPath, String shortName, String units) {
            this.fullPath = fullPath;
            this.shortName = shortName;
            this.units = units;
            gatherSinkName = sinkName + "_" + fullPath.replace('/', '_');
        }

        public void exec() throws IOException, SAPIException {
            connect(gatherSinkName);
            initializeFile();
            startThread();
        }

        private void initializeFile() throws IOException {
            String path = archiveDirectory + File.separator + fullPath.replace('/', File.separatorChar);
            File f = new File(path);
            if (!f.exists()) f.mkdirs();
            path = path + File.separator + fileName;
            System.out.println("Initializing file at " + path);
            File probe = new File(path);
            if (probe.exists()) throw new IOException("File already exists at: " + path);
            out = new PrintWriter(new FileWriter(path));
            out.println("Active channels: " + shortName);
            out.println("Channel units: " + units);
            out.println();
            out.println("Time " + shortName);
        }

        private void writeLineToFile(double time, double data) throws IOException {
            long unixTime = (long) (time * 1000.0);
            String timeStr = OUTPUT_FORMAT.format(new Date(unixTime));
            timeStr = timeStr.substring(0, 10) + "T" + timeStr.substring(11);
            out.println(timeStr + " " + data);
            out.flush();
        }

        private void closeFile() {
            out.close();
        }

        private void connect(String name) throws SAPIException {
            sink = new Sink();
            sink.OpenRBNBConnection(getServer(), name);
            ChannelMap sMap = new ChannelMap();
            sMap.Add(fullPath);
            sink.Subscribe(sMap, 0.0, 0.0, "newest");
            connected = true;
            System.out.println("Sucessful connection for " + name);
        }

        private void disconnect() {
            sink.CloseRBNBConnection();
            connected = false;
        }

        private void startThread() {
            if (!connected) return;
            Runnable r = new Runnable() {

                public void run() {
                    runWork();
                }
            };
            runit = true;
            stringDataThread = new Thread(r, shortName);
            stringDataThread.start();
            System.out.println("GrabDataSink: Started thread for " + fullPath);
        }

        public void stopThread() {
            runit = false;
            stringDataThread.interrupt();
            System.out.println("GrabDataSink: Stopped thread for " + fullPath);
        }

        private void runWork() {
            try {
                while (isRunning()) {
                    System.out.println("Before fetch " + sink.GetServerName() + " " + sink.GetClientName());
                    ChannelMap m = sink.Fetch(-1);
                    int index = m.GetIndex(fullPath);
                    if (index < 0) {
                        System.out.println("Data fetch failed for " + fullPath);
                        continue;
                    }
                    double[] times = m.GetTimes(index);
                    double[] data = m.GetDataAsFloat64(index);
                    System.out.println("fetch for " + fullPath + " " + times.length + " " + data.length);
                    for (int i = 0; i < times.length; i++) writeLineToFile(times[i], data[i]);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            stringDataThread = null;
            disconnect();
            runit = false;
        }

        private boolean isRunning() {
            return (connected && runit);
        }
    }
}
