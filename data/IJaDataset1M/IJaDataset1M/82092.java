package net.community.apps.common.test.chart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import net.community.chest.io.FileUtil;
import net.community.chest.io.IOCopier;
import net.community.chest.swing.options.BaseOptionPane;
import net.community.chest.util.datetime.ByCalendarFieldsComparator;
import net.community.chest.util.logging.LoggerWrapper;
import net.community.chest.util.logging.factory.WrapperFactoryManager;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import au.com.bytecode.opencsv.CSVReader;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Feb 22, 2009 7:42:28 AM
 */
class ChartPopulator extends SwingWorker<Void, TimeSeries> {

    private static final LoggerWrapper _logger = WrapperFactoryManager.getLogger(ChartPopulator.class);

    private final TestCSVChartFrame _frame;

    public final TestCSVChartFrame getMainFrame() {
        return _frame;
    }

    private final File _f;

    public final File getDataFile() {
        return _f;
    }

    public final Calendar getStartDate() {
        return _frame.getStartDate();
    }

    public final Calendar getEndDate() {
        return _frame.getEndDate();
    }

    public final Calendar getStartTime() {
        return _frame.getStartTime();
    }

    public final Calendar getEndTime() {
        return _frame.getEndTime();
    }

    public final Boolean getBikingState() {
        return _frame.getBikingState();
    }

    ChartPopulator(TestCSVChartFrame frame, File f) {
        if ((null == (_frame = frame)) || (null == (_f = f))) throw new IllegalArgumentException("Incomplete arguments provided");
    }

    protected void populateSeries(final CSVReader r, final List<? extends TimeSeries> tsl, final int dtIndex, final int tmIndex, final int sisIndex, final int diaIndex, final int hrIndex, final int bkIndex) throws Exception {
        final Calendar startDate = getStartDate(), endDate = getEndDate(), startTime = getStartTime(), endTime = getEndTime();
        final Boolean bkState = getBikingState();
        for (int lineIndex = 1; lineIndex > 0; lineIndex++) {
            final String[] values = r.readNext();
            if (null == values) break;
            final String dtv = (dtIndex < 0) ? null : values[dtIndex], tmv = (tmIndex < 0) ? null : values[tmIndex], sisv = (sisIndex < 0) ? null : values[sisIndex], diav = (diaIndex < 0) ? null : values[diaIndex], hrv = (hrIndex < 0) ? null : values[hrIndex], bks = (bkIndex < 0) ? null : values[bkIndex];
            try {
                final BPEntry entry = BPEntry.fromValue(dtv, tmv, sisv, diav, hrv);
                if (_logger.isDebugEnabled()) _logger.debug("populateSeries(" + entry + ")");
                final Calendar c = (null == entry) ? null : entry.getTimestamp();
                if (null == c) continue;
                if ((startDate != null) && (ByCalendarFieldsComparator.BY_DATE_ASCENDING.compare(c, startDate) < 0)) continue;
                if ((endDate != null) && (ByCalendarFieldsComparator.BY_DATE_ASCENDING.compare(c, endDate) > 0)) continue;
                if ((startTime != null) && ByCalendarFieldsComparator.BY_TIME_ASCENDING.compare(c, startTime) < 0) continue;
                if ((endTime != null) && ByCalendarFieldsComparator.BY_TIME_ASCENDING.compare(c, endTime) > 0) continue;
                if ((bkState != null) && (bkState.booleanValue() != "bike".equalsIgnoreCase(bks))) continue;
                final Minute m = new Minute(c.get(Calendar.MINUTE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.DAY_OF_MONTH), 1 + (c.get(Calendar.MONTH) - Calendar.JANUARY), c.get(Calendar.YEAR));
                final int[] vals = { entry.getSisValue(), entry.getDiaValue(), entry.getHrValue() };
                for (int vIndex = 0; vIndex < vals.length; vIndex++) {
                    final TimeSeries ts = tsl.get(vIndex);
                    if (null == ts) continue;
                    final int v = vals[vIndex];
                    ts.add(m, Integer.valueOf(v));
                }
            } catch (Exception e) {
                final String msg = e.getMessage() + " while parse line #" + lineIndex + " continue ?";
                if (JOptionPane.showConfirmDialog(getMainFrame(), msg, e.getClass().getName(), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) break;
            }
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        CSVReader r = null;
        try {
            final File f = getDataFile();
            _logger.info("processing(" + f.getAbsolutePath() + ") start");
            r = new CSVReader(new BufferedReader(new FileReader(f), IOCopier.DEFAULT_COPY_SIZE));
            final String[] ttl = r.readNext();
            if ((null == ttl) || (ttl.length <= 0)) throw new StreamCorruptedException("No title provided");
            int dtIndex = (-1), tmIndex = (-1), sisIndex = (-1), diaIndex = (-1), hrIndex = (-1), bkIndex = (-1);
            for (int tIndex = 0; tIndex < ttl.length; tIndex++) {
                final String tv = ttl[tIndex], t = (null == tv) ? null : tv.toLowerCase().trim();
                if ((null == t) || (t.length() <= 0)) continue;
                if ("Date".equalsIgnoreCase(t)) {
                    if (dtIndex < 0) {
                        dtIndex = tIndex;
                        continue;
                    }
                } else if ("Time".equalsIgnoreCase(t)) {
                    if (tmIndex < 0) {
                        tmIndex = tIndex;
                        continue;
                    }
                } else if (t.startsWith("sis")) {
                    if (sisIndex < 0) {
                        sisIndex = tIndex;
                        continue;
                    }
                } else if (t.startsWith("dia")) {
                    if (diaIndex < 0) {
                        diaIndex = tIndex;
                        continue;
                    }
                } else if (t.startsWith("hr")) {
                    if (hrIndex < 0) {
                        hrIndex = tIndex;
                        continue;
                    }
                } else if ("Remarks".equalsIgnoreCase(t)) {
                    if (bkIndex < 0) {
                        bkIndex = tIndex;
                        continue;
                    }
                } else continue;
                throw new IllegalStateException("Column=" + tv + " respecified");
            }
            if (((dtIndex < 0) || (tmIndex < 0)) || ((sisIndex < 0) && (diaIndex < 0) && (hrIndex < 0))) throw new IllegalStateException("Missing data columns");
            final int[] idxArray = { sisIndex, diaIndex, hrIndex };
            final List<TimeSeries> series = new ArrayList<TimeSeries>(idxArray.length);
            for (final int tIndex : idxArray) {
                final String tv = ((tIndex < 0) || (tIndex >= ttl.length)) ? null : ttl[tIndex];
                final TimeSeries ts = ((null == tv) || (tv.length() <= 0)) ? null : new TimeSeries(tv);
                if (null == ts) continue;
                series.add(ts);
            }
            populateSeries(r, series, dtIndex, tmIndex, sisIndex, diaIndex, hrIndex, bkIndex);
            _logger.info("processing(" + f.getAbsolutePath() + ") publishing");
            publish(series.toArray(new TimeSeries[idxArray.length]));
        } catch (Exception e) {
            BaseOptionPane.showMessageDialog(getMainFrame(), e);
        } finally {
            FileUtil.closeAll(r);
        }
        return null;
    }

    @Override
    protected void process(List<TimeSeries> chunks) {
        getMainFrame().populateCharts(chunks);
    }

    @Override
    protected void done() {
        getMainFrame().signalChartPopulatorEnd(this);
    }
}
