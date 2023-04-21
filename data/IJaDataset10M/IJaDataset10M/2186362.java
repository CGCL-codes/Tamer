package com.ibm.tuningfork.oscilloscope;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import com.ibm.tuningfork.core.figure.axis.Axis;
import com.ibm.tuningfork.core.graphics.Area;
import com.ibm.tuningfork.core.graphics.Graphics;
import com.ibm.tuningfork.core.graphics.LabeledDrawingPlane;
import com.ibm.tuningfork.core.graphics.RGBColor;
import com.ibm.tuningfork.core.graphics.TextStyle;
import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.data.AbstractTimeInterval;
import com.ibm.tuningfork.infra.data.ValueInterval;
import com.ibm.tuningfork.infra.stream.UnionTimeIntervalStream;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.stream.core.TimeIntervalStream;
import com.ibm.tuningfork.infra.stream.precise.TimeIntervalStreamCursor;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;
import com.ibm.tuningfork.infra.units.Dimensionless;
import com.ibm.tuningfork.infra.units.ITimeConverter;
import com.ibm.tuningfork.infra.units.Time;
import com.ibm.tuningfork.infra.util.MiscUtils;

/**
 *
 */
public class Analyzer {

    public static final int ALGORITHM_CLASSIC = 1;

    public static final int ALGORITHM_QUADRATIC = 2;

    public static final int ALGORITHM_ALPHABETIC = 3;

    public static final int ALGORITHM_EXPERIMENTAL = 4;

    IntervalPeakData gcPeakData;

    IntervalPeakData appPeakData;

    IntervalPeakData peaksdata;

    private static final boolean DEBUG = false;

    private static final boolean FILE_DEBUG = false;

    private static final boolean DUMP = false;

    private static final boolean VERBOSE = false;

    private static final boolean TWIN_PEAKS_DATA = false;

    private Oscilloscope osc = null;

    public Analyzer(Oscilloscope oscil) {
        osc = oscil;
    }

    public void paintPhaseData(Graphics g, Area area, RGBColor borderColor, RGBColor planeColor, TextStyle labelStyle) {
        if (TWIN_PEAKS_DATA) {
            Area top = new Area(area.x, area.y, area.width, area.height / 2);
            Area bottom = new Area(area.x, top.bottom(), area.width, area.height / 2);
            paintPhaseData(g, top, borderColor, planeColor, labelStyle, gcPeakData, "JVM Intervals");
            paintPhaseData(g, bottom, borderColor, planeColor, labelStyle, appPeakData, "Application Intervals");
        } else {
            Area all = new Area(area.x, area.y, area.width, area.height);
            paintPhaseData(g, all, borderColor, planeColor, labelStyle, peaksdata, "alignment periods");
        }
    }

    public void paintPhaseData(Graphics g, Area area, RGBColor borderColor, RGBColor planeColor, TextStyle labelStyle, IntervalPeakData peaks, String name) {
        ValueInterval yRange = peaks.scoreRange();
        double xMax = peaks.intervalLengths[peaks.intervalLengths.length - 1] + 1;
        Axis xAxis = new Axis(new ValueInterval(0, xMax), new Time(Time.SCALE_NANOSECONDS));
        Axis yAxis = new Axis(new ValueInterval(0, yRange.getMax()), new Dimensionless());
        LabeledDrawingPlane plane = new LabeledDrawingPlane(g, area, xAxis, name + ": Width in ", yAxis, "Score", borderColor, labelStyle);
        if (true) {
            for (int i = 0; i < peaks.getCount(); i++) {
                plane.addCoord(peaks.intervalLengths[i], peaks.intervalScores[i]);
            }
        } else {
            plane.addCoord(1, 1);
            plane.addCoord(10, 10);
            plane.addCoord(100, 100);
            plane.addCoord(1000, 1000);
        }
        plane.paint(g, planeColor, Graphics.SOLID_ALPHA);
    }

    public void clearPeakData() {
        gcPeakData = appPeakData = peaksdata = null;
    }

    public long phaseLock(StreamBundle streams, final long effectiveStart, final long effectiveEnd, final ITimeConverter timeConverter) {
        try {
            final Vector<Long> datar = new Vector<Long>();
            final ArrayList<TimeIntervalStream> streamsToLock = new ArrayList<TimeIntervalStream>();
            for (Stream stream : streams) {
                streamsToLock.add((TimeIntervalStream) stream);
            }
            UnionTimeIntervalStream union = new UnionTimeIntervalStream("Phase Lock", streamsToLock, effectiveStart, effectiveEnd);
            union.unregister();
            union.start();
            while (!union.isClosed()) {
                MiscUtils.milliSleep(100);
            }
            TimeIntervalStreamCursor cursor = union.newCursor();
            long lastEndInterval = 0;
            outer: while (true) {
                AbstractTimeInterval interval = cursor.getNext();
                while (interval == null) {
                    if (cursor.eof()) {
                        break outer;
                    } else {
                        cursor.blockForMore();
                        interval = cursor.getNext();
                    }
                }
                long startInterval = interval.getStart();
                long endInterval = interval.getEnd();
                if (endInterval < startInterval) {
                    continue;
                }
                if (startInterval < effectiveStart) {
                    startInterval = effectiveStart;
                }
                if (endInterval > effectiveEnd) {
                    endInterval = effectiveEnd;
                }
                if (lastEndInterval != 0 && startInterval - lastEndInterval < timeConverter.microSecondToTick(0.05)) {
                    datar.removeElementAt(datar.size() - 1);
                } else {
                    datar.add(new Long(startInterval));
                }
                datar.add(new Long(endInterval));
                lastEndInterval = endInterval;
            }
            Enumeration<Long> datarenum = datar.elements();
            long datararray[] = new long[datar.size()];
            int index = 0;
            while (datarenum.hasMoreElements()) {
                datararray[index++] = datarenum.nextElement().longValue();
            }
            if (osc.phaseAnalysisAlgorithm == ALGORITHM_CLASSIC) {
                return analyseIntervals(datararray, effectiveStart, effectiveEnd, timeConverter);
            }
            if (osc.phaseAnalysisAlgorithm == ALGORITHM_QUADRATIC) {
                return alignment(datararray);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public long phaseLockXX(TimeIntervalStream[] streams, final long effectiveStart, final long effectiveEnd, final ITimeConverter timeConverter) {
        try {
            final Vector<Long> datar = new Vector<Long>();
            TimeIntervalStreamCursor cursor = null;
            for (int i = 0; i < streams.length; i++) {
                if (streams[i] instanceof TimeIntervalStream) {
                    cursor = streams[i].newCursor(effectiveStart, effectiveEnd);
                }
            }
            if (cursor == null) {
                return 0;
            }
            long lastEndInterval = 0;
            while (cursor.hasMore()) {
                AbstractTimeInterval interval = cursor.getNext();
                if (interval == null) {
                    continue;
                }
                long startInterval = interval.getStart();
                long endInterval = interval.getEnd();
                if (endInterval < startInterval) {
                    continue;
                }
                if (startInterval < effectiveStart) {
                    startInterval = effectiveStart;
                }
                if (endInterval > effectiveEnd) {
                    endInterval = effectiveEnd;
                }
                if (lastEndInterval != 0 && startInterval - lastEndInterval < timeConverter.microSecondToTick(0.05)) {
                    datar.removeElementAt(datar.size() - 1);
                } else {
                    datar.add(new Long(startInterval));
                }
                datar.add(new Long(endInterval));
                lastEndInterval = endInterval;
            }
            Enumeration<Long> datarenum = datar.elements();
            long datararray[] = new long[datar.size()];
            int index = 0;
            while (datarenum.hasMoreElements()) {
                datararray[index++] = ((Long) datarenum.nextElement()).longValue();
            }
            analyseIntervals2(datararray, effectiveStart, effectiveEnd, timeConverter);
            return analyseIntervals(datararray, effectiveStart, effectiveEnd, timeConverter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private long analyseIntervals(long[] datar, final long effectiveStart, final long effectiveEnd, final ITimeConverter timeConverter) {
        Logging.verboseln(1, "Oscilloscope.analyzeIntervals called");
        if (DEBUG) {
            System.out.println("Oscilloscope.analyseInterval called");
        }
        if (datar.length < 2) {
            if (DEBUG) {
                Logging.msgln(" datar.length < 2");
            }
            return 0;
        }
        int[] gcint = new int[datar.length / 2];
        int[] appint = new int[(datar.length - 1) / 2];
        for (int i = 0; i < datar.length - 1; i += 2) {
            gcint[i / 2] = i;
            if (i + 1 < datar.length - 1) {
                appint[i / 2] = i + 1;
            }
        }
        if ((gcint[gcint.length - 1] == 0) || (appint[appint.length - 1] == 0)) {
            if (DEBUG) {
                Logging.msgln("The gcint or appint has zero in the end vtrerr  datar.length = " + datar.length);
            }
            return 0;
        }
        quicksortIntervals(datar, gcint, 0, gcint.length - 1);
        quicksortIntervals(datar, appint, 0, appint.length - 1);
        long length = 10;
        for (int i = 0; i < appint.length; i++) {
            if (i > 10 && i < appint.length - 10) {
                if ((datar[appint[i] + 1] - datar[appint[i]]) > (length + length / 2)) {
                    Logging.verbose(1, "Found a discontinuity in intervals   " + timeConverter.tickToMicroSecond(length) + "  ");
                    Logging.verboseln(1, Double.toString(timeConverter.tickToMicroSecond((datar[appint[i] + 1] - datar[appint[i]]))));
                    long start = -1;
                    long sum = 0;
                    long sum2 = 0;
                    int count = 0;
                    long nlength = 0;
                    long clength = 0;
                    Vector<Long> clengths = new Vector<Long>();
                    for (int j = 1; j < datar.length - 1; j += 2) {
                        if (start != -1) {
                            Logging.verbose(1, " " + timeConverter.tickToMicroSecond(datar[j] - start));
                        }
                        if ((datar[j + 1] - datar[j] > length)) {
                            if (start == -1) {
                                start = datar[j];
                            } else {
                                Logging.verboseln(1);
                                if (nlength == 0) {
                                    nlength = datar[j] - start;
                                }
                                clength = datar[j] - start;
                                if (true && (clength > (2 * nlength) / 3) && (clength < (3 * nlength) / 2)) {
                                    if (DEBUG) {
                                        System.out.println("clength=  " + clength);
                                    }
                                    clengths.add(new Long(datar[j] - start));
                                    sum += datar[j] - start;
                                    sum2 += (datar[j] - start) * (datar[j] - start);
                                    ++count;
                                    start = datar[j];
                                }
                            }
                        }
                    }
                    long avgWidth = sum / count;
                    long sd = Math.round(Math.sqrt((sum2 / count - avgWidth * avgWidth)));
                    Logging.verboseln(1, "avgWidth= " + avgWidth + "  sd=  " + sd);
                    if (DEBUG) {
                        System.out.println("avgWidth= " + avgWidth + "  sd=  " + sd);
                    }
                    int size = clengths.size();
                    long[] clengtharray = new long[size];
                    int[] cpointers = new int[size - 1];
                    for (int ii = 0; ii < size; ii++) {
                        clengtharray[ii] = (clengths.elementAt(ii)).longValue();
                        if (ii < size - 1) {
                            cpointers[ii] = ii;
                        }
                    }
                    quicksortIntervals(clengtharray, cpointers, 0, cpointers.length - 1);
                    avgWidth = clengtharray[size / 2];
                    Logging.verboseln(1, "avgWidth= " + avgWidth + "  sd=  " + sd);
                    if (DEBUG) {
                        System.out.println("avgWidth= " + avgWidth + "  sd=  " + sd);
                    }
                    Logging.verboseln(1, "There are " + count + " intervals with average width " + avgWidth + " (" + timeConverter.tickToMicroSecond(avgWidth) + " us) and standard diviation " + timeConverter.tickToMicroSecond(sd) + " us");
                    if (DEBUG) {
                        System.out.println("There are " + count + " intervals with average width " + avgWidth + " (" + timeConverter.tickToMicroSecond(avgWidth) + " us) and standard diviation " + timeConverter.tickToMicroSecond(sd) + " us");
                    }
                    return avgWidth;
                } else {
                    length = (datar[appint[i] + 1] - datar[appint[i]]);
                }
            } else {
                length = (datar[appint[i] + 1] - datar[appint[i]]);
            }
        }
        return 0;
    }

    private long analyseIntervals2(long[] datar, final long effectiveStart, final long effectiveEnd, final ITimeConverter timeConverter) throws IOException {
        Logging.verboseln(1, "Oscilloscope.analyzeIntervals called");
        if (DEBUG) {
            System.out.println("Oscilloscope.analyseInterval called");
        }
        if (datar.length < 2) {
            if (DEBUG) {
                Logging.msgln(" datar.length < 2");
            }
            return 0;
        }
        int[] gcint = new int[datar.length / 2];
        int[] appint = new int[(datar.length - 1) / 2];
        for (int i = 0; i < datar.length - 1; i += 2) {
            gcint[i / 2] = i;
            if (i + 1 < datar.length - 1) {
                appint[i / 2] = i + 1;
            }
        }
        if ((gcint[gcint.length - 1] == 0) || (appint[appint.length - 1] == 0)) {
            if (DEBUG) {
                Logging.msgln("The gcint or appint has zero in the end vtrerr  datar.length = " + datar.length);
            }
            return 0;
        }
        quicksortIntervals(datar, gcint, 0, gcint.length - 1);
        quicksortIntervals(datar, appint, 0, appint.length - 1);
        gcPeakData = findPeaks(datar, gcint, 0);
        appPeakData = findPeaks(datar, appint, 1);
        int[][] peaksgc = gcPeakData.intervalPeaks;
        int[][] peaksapp = appPeakData.intervalPeaks;
        char[] datarc = new char[datar.length - 1];
        char x;
        x = '@';
        for (int i = 0; i < datarc.length; i++) {
            datarc[i] = '*';
        }
        long max = 0;
        long min = 0;
        int number = 0;
        for (int i = 0; i < peaksgc.length; i++) {
            min = datar[gcint[peaksgc[i][0]] + 1] - datar[gcint[peaksgc[i][0]]];
            max = datar[gcint[peaksgc[i][1]] + 1] - datar[gcint[peaksgc[i][1]]];
            number = peaksgc[i][1] - peaksgc[i][0] + 1;
            ++x;
            if (VERBOSE) {
                System.out.println("gcPeak at  " + peaksgc[i][0] + "  in the range " + min + "  to  " + max + "  number=  " + number + " symbol:  " + x);
            }
            for (int j = peaksgc[i][0]; j <= peaksgc[i][1]; j++) {
                datarc[gcint[j]] = x;
            }
        }
        if (peaksgc.length < 32) {
            x = '`';
        }
        for (int i = 0; i < peaksapp.length; i++) {
            min = datar[appint[peaksapp[i][0]] + 1] - datar[appint[peaksapp[i][0]]];
            max = datar[appint[peaksapp[i][1]] + 1] - datar[appint[peaksapp[i][1]]];
            number = peaksapp[i][1] - peaksapp[i][0] + 1;
            ++x;
            if (VERBOSE) {
                System.out.println("apppeak at  " + peaksapp[i][0] + "  in the range " + min + "  to  " + max + "  number=  " + number + " symbol:  " + x);
            }
            for (int j = peaksapp[i][0]; j <= peaksapp[i][1]; j++) {
                datarc[appint[j]] = x;
            }
        }
        if (VERBOSE) {
            for (int i = 0; i < datarc.length; i++) {
                System.out.print(Character.toString(datarc[i]));
                if (i % 100 == 99) {
                    System.out.println();
                }
            }
            System.out.println();
        }
        return 0;
    }

    private IntervalPeakData findPeaks(long[] datar, int[] intervals, int counter) throws IOException {
        if (osc.analysisParameterB < 0.0) {
            return findPeaks1(datar, intervals, counter);
        }
        return findPeaks2(datar, intervals, counter);
    }

    private IntervalPeakData findPeaks1(long[] datar, int[] intervals, int counter) throws IOException {
        int[] peak = new int[2];
        Vector<int[]> peakv = new Vector<int[]>();
        long max = datar[intervals[intervals.length - 1] + 1] - datar[intervals[intervals.length - 1]];
        long min = datar[intervals[0] + 1] - datar[intervals[0]];
        long sum = 0;
        for (int i = 0; i < intervals.length; i++) {
            sum += datar[intervals[i] + 1] - datar[intervals[i]];
        }
        if (max == min) {
            int[][] peakout = new int[1][2];
            peakout[0][0] = 0;
            peakout[0][1] = intervals.length - 1;
            return new IntervalPeakData(peakout);
        }
        double lamda = ((double) sum) / ((double) intervals.length);
        int it = 5;
        double[][] prob = new double[it][intervals.length - 1];
        double[] xArr = new double[prob[0].length];
        double[] yArr = new double[prob[0].length];
        double[] yArrAlt = new double[prob[0].length];
        double coeff = (intervals.length - 1) / Math.log((double) max / (double) min);
        if (DUMP) {
            System.out.println("lamda=  " + lamda + "  sum=  " + sum + "  length=  " + intervals.length + "  max=  " + max + "  min=  " + min + "  coeff=  " + coeff);
        }
        double psum = 0.0;
        for (int i = 0; i < prob[0].length; i++) {
            prob[0][i] = coeff * Math.log(((datar[intervals[i + 1] + 1] - datar[intervals[i + 1]]) + 0.5) / ((datar[intervals[i] + 1] - datar[intervals[i]]) - 0.5));
            xArr[i] = (((datar[intervals[i + 1] + 1] - datar[intervals[i + 1]]) + (datar[intervals[i] + 1] - datar[intervals[i]]))) / 2.0;
            yArr[i] = 1.0 / prob[0][i];
            psum += prob[0][i];
        }
        for (int iit = 1; iit < it; ++iit) {
            psum = 0.0;
            for (int i = 0; i < prob[0].length; i++) {
                if (i == 0) {
                    prob[iit][i] = 0.75 * prob[iit - 1][i] + 0.25 * prob[iit - 1][i + 1];
                } else if (i == prob[0].length - 1) {
                    prob[iit][i] = 0.25 * prob[iit - 1][i - 1] + 0.75 * prob[iit - 1][i];
                } else {
                    prob[iit][i] = 0.25 * prob[iit - 1][i - 1] + 0.5 * prob[iit - 1][i] + 0.25 * prob[iit - 1][i + 1];
                }
                psum += prob[iit][i];
                if (iit == (it - 1)) {
                    yArrAlt[i] = 1.0 / prob[iit][i];
                }
            }
        }
        if (FILE_DEBUG) {
            String filename = "C:\\vtrajan\\metronome\\prob" + counter + ".data";
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
                out.writeInt(prob[0].length);
                for (int i = 0; i < prob[0].length; i++) {
                    out.writeDouble(xArr[i]);
                    out.writeDouble(yArr[i]);
                    out.writeDouble(yArrAlt[i]);
                }
                out.close();
            } catch (FileNotFoundException e) {
                Logging.msgln("Tried to write file on Rajan's C: drive, but failed: " + filename);
            }
        }
        double cutoff = 0.2;
        if (osc.analysisParameterA >= 0.0) {
            cutoff = osc.analysisParameterA;
        }
        boolean peakb = false;
        int is = 0;
        int ie = 0;
        for (int i = 0; i < prob[0].length; i++) {
            if (!peakb && (prob[it - 1][i] < cutoff)) {
                is = i;
                while ((is > 0) && (prob[0][is - 1] < cutoff)) {
                    --is;
                }
                peak[0] = is;
                peakb = true;
            } else if (peakb && ((prob[it - 1][i] > cutoff) || (i == prob[0].length - 1))) {
                ie = i;
                while ((ie < intervals.length - 2) && (prob[0][ie + 1] < cutoff)) {
                    ++ie;
                }
                ++ie;
                peak[1] = ie;
                peakv.add(peak);
                peak = new int[2];
                peakb = false;
                i = ie;
            }
        }
        int[][] peakout = new int[peakv.size()][2];
        for (int i = 0; i < peakv.size(); i++) {
            peakout[i] = peakv.elementAt(i);
        }
        return new IntervalPeakData(xArr, yArrAlt, yArr, peakout);
    }

    private IntervalPeakData findPeaks2(long[] datar, int[] intervals, int counter) throws IOException {
        int[] peak = new int[2];
        Vector<int[]> peakv = new Vector<int[]>();
        long max = datar[intervals[intervals.length - 1] + 1] - datar[intervals[intervals.length - 1]];
        long min = datar[intervals[0] + 1] - datar[intervals[0]];
        long sum = 0;
        for (int i = 0; i < intervals.length; i++) {
            sum += datar[intervals[i] + 1] - datar[intervals[i]];
        }
        if (max == min) {
            int[][] peakout = new int[1][2];
            peakout[0][0] = 0;
            peakout[0][1] = intervals.length - 1;
            return new IntervalPeakData(peakout);
        }
        double lamda = ((double) sum) / ((double) intervals.length);
        int it = 5;
        double[][] prob = new double[it][intervals.length - 1];
        double[] xArr = new double[prob[0].length];
        double[] yArr = new double[prob[0].length];
        double[] yArrAlt = new double[prob[0].length];
        double coeff = (intervals.length - 1) / Math.log((double) max / (double) min);
        if (DUMP) {
            System.out.println("lamda=  " + lamda + "  sum=  " + sum + "  length=  " + intervals.length + "  max=  " + max + "  min=  " + min + "  coeff=  " + coeff);
        }
        double psum = 0.0;
        for (int i = 0; i < prob[0].length; i++) {
            prob[0][i] = Math.exp(-coeff * Math.log(((double) (datar[intervals[i + 1] + 1] - datar[intervals[i + 1]])) / ((double) (datar[intervals[i] + 1] - datar[intervals[i]]))));
            xArr[i] = (((datar[intervals[i + 1] + 1] - datar[intervals[i + 1]]) + (datar[intervals[i] + 1] - datar[intervals[i]]))) / 2.0;
            yArr[i] = prob[0][i];
            psum += prob[0][i];
        }
        if (DUMP) {
            System.out.println("iit=  " + 0 + "  psum=  " + psum);
        }
        for (int iit = 1; iit < it; ++iit) {
            psum = 0.0;
            for (int i = 0; i < prob[0].length; i++) {
                if (i == 0) {
                    prob[iit][i] = 0.75 * prob[iit - 1][i] + 0.25 * prob[iit - 1][i + 1];
                } else if (i == prob[0].length - 1) {
                    prob[iit][i] = 0.25 * prob[iit - 1][i - 1] + 0.75 * prob[iit - 1][i];
                } else {
                    prob[iit][i] = 0.25 * prob[iit - 1][i - 1] + 0.5 * prob[iit - 1][i] + 0.25 * prob[iit - 1][i + 1];
                }
                psum += prob[iit][i];
                if (iit == (it - 1)) {
                    yArrAlt[i] = prob[iit][i];
                }
            }
            if (DUMP) {
                System.out.println("iit=  " + iit + "  psum=  " + psum);
            }
        }
        if (DUMP) {
            for (int i = 0; i < prob[0].length; i++) {
            }
        }
        if (FILE_DEBUG) {
            String filename = "C:\\vtrajan\\metronome\\prob" + counter + ".data";
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
                out.writeInt(prob[0].length);
                for (int i = 0; i < prob[0].length; i++) {
                    out.writeDouble(xArr[i]);
                    out.writeDouble(yArr[i]);
                    out.writeDouble(yArrAlt[i]);
                }
                out.close();
            } catch (FileNotFoundException e) {
                Logging.msgln("Tried to write file on Rajan's C: drive, but failed: " + filename);
            }
        }
        double cutoff = 0.8;
        if (osc.analysisParameterA > 0.0) {
            cutoff = 1.0 - osc.analysisParameterA;
        }
        boolean peakb = false;
        int is = 0;
        int ie = 0;
        for (int i = 0; i < prob[0].length; i++) {
            if (!peakb && (prob[it - 1][i] > cutoff)) {
                is = i;
                while ((is > 0) && (prob[0][is - 1] > cutoff)) {
                    --is;
                }
                if (VERBOSE) {
                    System.out.println("Peak starting at is=  " + is + "  value = " + (datar[intervals[i] + 1] - datar[intervals[i]]));
                }
                peak[0] = is;
                peakb = true;
            } else if (peakb && ((prob[it - 1][i] < cutoff) || (i == prob[0].length - 1))) {
                ie = i;
                while ((ie < intervals.length - 2) && (prob[0][ie + 1] > cutoff)) {
                    ++ie;
                }
                ++ie;
                if (VERBOSE) {
                    System.out.println("Peak ending at ie=  " + ie + "  value = " + (datar[intervals[i] + 1] - datar[intervals[i]]));
                }
                peak[1] = ie;
                peakv.add(peak);
                peak = new int[2];
                peakb = false;
                i = ie;
            }
        }
        int[][] peakout = new int[peakv.size()][2];
        for (int i = 0; i < peakv.size(); i++) {
            peakout[i] = peakv.elementAt(i);
        }
        return new IntervalPeakData(xArr, yArrAlt, yArr, peakout);
    }

    private long alignment(long[] datar) throws IOException {
        double c[][] = new double[2][];
        c[0] = new double[datar.length];
        c[1] = new double[datar.length];
        for (int i = 0; i < c.length; i++) {
            c[i][0] = 0.0;
        }
        int iv = 0;
        double cm = 0.0;
        int jm = 0;
        long[] periods = new long[datar.length];
        int[] periodp = new int[datar.length - 1];
        int[] iva = new int[datar.length - 1];
        int[] jma = new int[datar.length - 1];
        periods[0] = 0;
        for (int i = 1; i < datar.length; i++) {
            for (int j = 1; j < i; j++) {
                c[1][j] = max(c[0][j - 1] + similarity((datar[i] - datar[i - 1]), (datar[j] - datar[j - 1])), (c[0][j] + similarity((datar[i] - datar[i - 1]), 0)), (c[1][j - 1] + similarity(0, (datar[j] - datar[j - 1]))), 0);
            }
            iv = i;
            cm = c[1][iv - 1];
            for (int j = iv - 2; j >= 0; j--) {
                if (c[1][j] >= cm) {
                    jm = j;
                    cm = c[1][jm];
                }
            }
            if ((iv > 0) && (jm > 0)) {
                periods[i] = periods[i - 1] + (datar[iv - 1] - datar[jm - 1]);
                iva[i - 1] = iv - 1;
                jma[i - 1] = jm - 1;
            } else {
                periods[i] = periods[i - 1] + 10000;
            }
            periodp[i - 1] = i - 1;
            c[0] = c[1];
            c[1] = new double[datar.length];
            c[1][0] = 0.0;
        }
        quicksortIntervals(periods, periodp, 0, periodp.length - 1);
        peaksdata = findPeaks(periods, periodp, 2);
        int[][] peaksperiods = peaksdata.intervalPeaks;
        if (VERBOSE) {
            for (int i = 0; i < peaksperiods.length; i++) {
                System.out.println(peaksperiods[i][0] + "   " + peaksperiods[i][1]);
            }
        }
        int[] temp;
        PeriodsData periodsdata = new PeriodsData(peaksperiods.length);
        for (int i = 0; i < peaksperiods.length; i++) {
            for (int j = peaksperiods.length - 1; i < j; j--) {
                if ((peaksdata.intervalPeaks[j][1] - peaksdata.intervalPeaks[j][0]) > (peaksdata.intervalPeaks[j - 1][1] - peaksdata.intervalPeaks[j - 1][0])) {
                    temp = peaksdata.intervalPeaks[j];
                    peaksdata.intervalPeaks[j] = peaksdata.intervalPeaks[j - 1];
                    peaksdata.intervalPeaks[j - 1] = temp;
                }
            }
            periodsdata.periodsize[i] = (peaksperiods[i][1] - peaksperiods[i][0] + 1);
            periodsdata.periodmedian[i] = (periods[periodp[(peaksperiods[i][0] + peaksperiods[i][1]) / 2] + 1] - periods[periodp[(peaksperiods[i][0] + peaksperiods[i][1]) / 2]]);
            periodsdata.periodmin[i] = (periods[periodp[peaksperiods[i][0]] + 1] - periods[periodp[peaksperiods[i][0]]]);
            periodsdata.periodmax[i] = (periods[periodp[peaksperiods[i][1]] + 1] - periods[periodp[peaksperiods[i][1]]]);
            long periodmean = 0;
            for (int j = peaksperiods[0][0]; j <= peaksperiods[0][1]; j++) {
                periodmean += (periods[periodp[j] + 1] - periods[periodp[j]]);
            }
            periodsdata.periodmean[i] = periodmean / periodsdata.periodsize[i];
            if (VERBOSE) {
                System.out.println(peaksperiods[i][0] + "   " + peaksperiods[i][1]);
            }
            if (VERBOSE) {
                System.out.println("period of size:   " + (peaksperiods[i][1] - peaksperiods[i][0] + 1) + "  from:  " + (periods[periodp[peaksperiods[i][0]] + 1] - periods[periodp[peaksperiods[i][0]]]) + "  to:  " + (periods[periodp[peaksperiods[i][1]] + 1] - periods[periodp[peaksperiods[i][1]]]) + "  median:  " + (periods[periodp[(peaksperiods[i][0] + peaksperiods[i][1]) / 2] + 1] - periods[periodp[(peaksperiods[i][0] + peaksperiods[i][1]) / 2]]));
            }
        }
        long period = 0;
        if (VERBOSE) {
            System.out.println("period median=  " + periodsdata.periodmedian[0] + "   mean=  " + periodsdata.periodmean[0]);
        }
        if (osc.analysisParameterC > 0.0) {
            period = periodsdata.periodmedian[0];
        } else {
            period = periodsdata.periodmin[0];
        }
        appPeakData = peaksdata;
        Logging.errorln("PERIOD " + period);
        return period;
    }

    private static double max(double a, double b, double c, double d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    private static double similarity(long a, long b) {
        double max = 1.0;
        double min = -1.0;
        double sim = 0.0;
        if ((a == 0) || (b == 0)) {
            sim = min;
        }
        sim = max - Math.abs(Math.log((double) a / (double) b));
        if (sim < min) {
            sim = min;
        }
        return sim;
    }

    private void quicksortIntervals(long[] datar, int[] intervals, int l, int r) {
        int i = l;
        int j = r;
        long x;
        int w;
        x = datar[intervals[(l + r) / 2] + 1] - datar[intervals[(l + r) / 2]];
        do {
            while ((datar[intervals[i] + 1] - datar[intervals[i]]) < x) {
                i = i + 1;
            }
            while ((datar[intervals[j] + 1] - datar[intervals[j]]) > x) {
                j = j - 1;
            }
            if (i <= j) {
                w = intervals[i];
                intervals[i] = intervals[j];
                intervals[j] = w;
                i++;
                j--;
            }
        } while (i <= j);
        if (l < j) {
            quicksortIntervals(datar, intervals, l, j);
        }
        if (i < r) {
            quicksortIntervals(datar, intervals, i, r);
        }
    }
}
