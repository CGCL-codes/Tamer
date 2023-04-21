package org.tcpfile.net;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.SwingUtilities;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.main.EntryPoint;
import org.tcpfile.main.Misc;
import org.tcpfile.utils.PeriodChecker;

/**
 * Analyzes the Bandwidth that is currently in use.
 * It can display the amount of bytes sent of any number of seconds, if the data has not been deleted in between.
 * @author Stivo
 *
 */
public class BandwidthAnalyzer {

    private static Logger log = LoggerFactory.getLogger(BandwidthAnalyzer.class);

    public static final Vector<BandwidthAnalyzer> analyzers = new Vector<BandwidthAnalyzer>();

    public final Vector<Integer> seconds = new Vector<Integer>();

    private PeriodChecker[] updatePeriod = new PeriodChecker[61];

    public final Vector<Update>[] outUpdates = new Vector[61];

    public final Vector<Update>[] inUpdates = new Vector[61];

    public final Object vectorSync = new Object();

    public int messagesOut = 0;

    public int messagesIn = 0;

    public long maxMinute = 0;

    private PeriodChecker echoPeriod = new PeriodChecker(60000);

    private PeriodChecker displayPeriod = new PeriodChecker(100);

    private PeriodChecker update = new PeriodChecker(100);

    public static interface BandwidthAnalyzerSource {

        public long getWrittenBytes();

        public long getReadBytes();
    }

    public static class IoSessionBandwidthAnalyzerSource implements BandwidthAnalyzerSource {

        private IoSession io;

        public IoSessionBandwidthAnalyzerSource(IoSession io) {
            this.io = io;
        }

        @Override
        public long getReadBytes() {
            return io.getReadBytes();
        }

        @Override
        public long getWrittenBytes() {
            return io.getWrittenBytes();
        }
    }

    private final BandwidthAnalyzerSource source;

    public BandwidthAnalyzer(IoSession ses) {
        this(new IoSessionBandwidthAnalyzerSource(ses), true);
    }

    public BandwidthAnalyzer(BandwidthAnalyzerSource ses, boolean add) {
        source = ses;
        seconds.add(1);
        update();
        if (add) synchronized (analyzers) {
            analyzers.add(this);
        }
    }

    public static int totalUpload(int seconds) {
        int totalout = 0;
        synchronized (analyzers) {
            for (BandwidthAnalyzer ba : analyzers) {
                totalout += ba.getBytesFromSeconds(seconds, false);
            }
        }
        return totalout;
    }

    public static int totalDownload(int seconds) {
        int totalin = 0;
        synchronized (analyzers) {
            for (BandwidthAnalyzer ba : analyzers) {
                totalin += ba.getBytesFromSeconds(seconds, true);
            }
        }
        return totalin;
    }

    public void remove() {
        synchronized (analyzers) {
            analyzers.remove(this);
        }
    }

    public void update() {
        if (!update.check()) return;
        Update out = null;
        Update in = null;
        synchronized (vectorSync) {
            for (int i = 0; i < seconds.size(); i++) {
                Integer j = seconds.get(i);
                if (updatePeriod[j] == null) {
                    updatePeriod[j] = new PeriodChecker(j * 100);
                    outUpdates[j] = new Vector<Update>();
                    outUpdates[j].add(new Update(0));
                    inUpdates[j] = new Vector<Update>();
                    inUpdates[j].add(new Update(0));
                }
                if (updatePeriod[j].check()) {
                    if (out == null) out = new Update(source.getWrittenBytes());
                    outUpdates[j].add(out);
                    if (in == null) in = new Update(source.getReadBytes());
                    inUpdates[j].add(in);
                    if (outUpdates[j].size() > 50) {
                        getBytesFromSeconds(j, false);
                    }
                    if (inUpdates[j].size() > 50) {
                        getBytesFromSeconds(j, true);
                    }
                }
            }
        }
        if (echoPeriod.check()) echo();
        if (!displayPeriod.check()) return;
        Runnable r = new Runnable() {

            public void run() {
                EntryPoint.gui.tableModelConnections.fireTableDataChanged();
                EntryPoint.gui.updateActualSpeedDisplay(totalDownload(5) / 5, totalUpload(5) / 5, 0);
            }
        };
        if (EntryPoint.gui.tableModelConnections != null) SwingUtilities.invokeLater(r);
    }

    public void echo() {
        String out = "" + Misc.NEWLINE;
        out += ("Sent Bytes total:" + outUpdates[seconds.get(0)].lastElement().bytes + Misc.NEWLINE);
        out += ("Sent Bytes in last minute:" + getSeconds(60, false) + Misc.NEWLINE);
        out += ("Sent Bytes in last 5 seconds:" + getSeconds(5, false) + Misc.NEWLINE);
        out += ("Received Bytes total:" + inUpdates[seconds.get(0)].lastElement().bytes + Misc.NEWLINE);
        out += ("Received Bytes in last minute:" + getSeconds(60, true) + Misc.NEWLINE);
        out += ("Received Bytes in last 5 seconds:" + getSeconds(5, true));
        log.debug(out);
    }

    public Size getSeconds(int secs, boolean in) {
        return new Size(getBytesFromSeconds(secs, in));
    }

    public long getBytesFromSeconds(int secs, boolean in) {
        Vector<Update> analyze = in ? inUpdates[secs] : outUpdates[secs];
        return getBytesFromSeconds(analyze, secs);
    }

    public String getSpeedFromSeconds(int secs, boolean in) {
        Size s = new Size(getBytesFromSeconds(secs, in) / secs);
        return s.toString() + "/s";
    }

    private long getBytesFromSeconds(Vector<Update> u, int secs) {
        assert (secs <= 60 && secs >= 0);
        long time = System.currentTimeMillis() - secs * 1000L;
        long startBytes = 0;
        synchronized (vectorSync) {
            if (u == null) {
                if (!seconds.contains(secs)) seconds.add(secs);
                return 0;
            }
            if (u.size() <= 1) return 0;
            for (Iterator<Update> iterator = u.iterator(); iterator.hasNext(); ) {
                Update update = iterator.next();
                if (u.size() > 1) {
                    if (update.time < time) {
                        iterator.remove();
                    } else {
                        startBytes = update.bytes;
                        break;
                    }
                } else {
                    startBytes = update.bytes;
                    break;
                }
            }
            if (startBytes == 0) return 0;
            return u.lastElement().bytes - startBytes;
        }
    }

    public int getSendSizeRecommendation(int speedlimit) {
        long outNow = getBytesFromSeconds(60, false);
        maxMinute = Misc.max(outNow, maxMinute);
        int halfsecond = (int) maxMinute / 60 / 2;
        maxMinute -= 0.2 * maxMinute;
        if (speedlimit != 0 && halfsecond > speedlimit / 5) halfsecond = speedlimit / 5;
        if (halfsecond < 1300) return 1300;
        if (halfsecond > 600000) return 600000;
        return halfsecond;
    }

    public void resetRecommendation() {
        maxMinute = 0;
    }
}

class Update {

    public long time;

    public long bytes = 0;

    public Update(long bytes) {
        this(bytes, System.currentTimeMillis());
    }

    public Update(long bytes, long time) {
        this.bytes = bytes;
        this.time = time;
    }
}
