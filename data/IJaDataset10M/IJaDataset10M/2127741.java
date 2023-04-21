package com.ibm.tuningfork.oscilloscope;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Vector;
import com.ibm.tuningfork.core.bookmarks.BookmarkLocation;
import com.ibm.tuningfork.core.bookmarks.BookmarkManager;
import com.ibm.tuningfork.core.bookmarks.BookmarkMaster;
import com.ibm.tuningfork.core.figure.FigurePainter;
import com.ibm.tuningfork.core.figure.HoverState;
import com.ibm.tuningfork.core.graphics.AnnotationPainter;
import com.ibm.tuningfork.core.graphics.Area;
import com.ibm.tuningfork.core.graphics.Coord;
import com.ibm.tuningfork.core.graphics.Graphics;
import com.ibm.tuningfork.core.graphics.RGBColor;
import com.ibm.tuningfork.core.graphics.SWTGraphics;
import com.ibm.tuningfork.core.graphics.TFColor;
import com.ibm.tuningfork.infra.Logging;
import com.ibm.tuningfork.infra.data.AbstractTimeInterval;
import com.ibm.tuningfork.infra.data.Bookmark;
import com.ibm.tuningfork.infra.data.TimeInterval;
import com.ibm.tuningfork.infra.stream.core.BookmarkStream;
import com.ibm.tuningfork.infra.stream.core.TimeIntervalStream;
import com.ibm.tuningfork.infra.stream.precise.EventStreamCursor;
import com.ibm.tuningfork.infra.stream.precise.TimeIntervalStreamCursor;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;

/**
 * Draws the oscilloscope view.
 */
public class OscilloscopePainter extends FigurePainter {

    protected final Oscilloscope osc;

    protected double intervalStats[][][];

    protected RGBColor marginColor;

    protected static final boolean PERFORMANCE_DEBUG = false;

    protected static final boolean CORRECTNESS_DEBUG = false;

    protected static final int STANDARD_MARGIN = 1;

    protected static final int WIDE_MARGIN = 5;

    protected static final int MARGIN_SEPARATOR_WIDTH = 1;

    protected static final int MARGIN_ALPHA = 200;

    protected static final int FOLDED_DATA_BASE_ALPHA = 50;

    protected static final int FOLDED_DATA_ALPHA_RANGE = Graphics.SOLID_ALPHA - FOLDED_DATA_BASE_ALPHA;

    protected static final int PHASE_SEPARATOR_WIDTH = 3;

    protected static final int PHASE_SEPARATOR_WHITE = 5;

    protected static final int PHASE_SEPARATOR_DARK = 8;

    protected static final int MIN_PHASE_DISPLAY_WIDTH = 10;

    protected static final int MIN_PHASE_DISPLAY_FULL_SEPARATOR_WIDTH = 20 * PHASE_SEPARATOR_WIDTH;

    protected static final int PHASE_SEPARATOR_ALPHA = Graphics.SOLID_ALPHA;

    protected long minTime;

    protected long maxTime;

    protected int interleavedStripHeight;

    protected int yBase;

    protected int stripDataHeight;

    protected double ppt;

    protected int displayedStrips;

    protected int categories;

    protected int interStripMargin;

    protected int foldingFactor;

    protected int width;

    protected int height;

    protected int stripInterleaving;

    protected boolean splitScreen;

    protected int strips;

    protected int eventCount;

    public OscilloscopePainter(Oscilloscope oscilloscope, TFColor marginColor) {
        super(oscilloscope);
        this.osc = oscilloscope;
        this.marginColor = marginColor.toRGBColor();
    }

    public ArrayList<Integer> paint(final StreamBundle streams, final RGBColor[][] categoryColors) {
        boolean recompute = computeGeometry();
        recompute |= streamsChangedP(streams);
        int yMarginAdjustment = interStripMargin;
        if (osc.effectiveInterleaveFactor() > 1 && !osc.getSplitScreen()) {
            yMarginAdjustment = stripDataHeight / 2;
        }
        int totalCategories = osc.getCategoryCount();
        RGBColor[] flattenedCategories = new RGBColor[totalCategories];
        for (int i = 0, c = 0; i < streams.size(); i++) {
            for (int j = 0; j < streams.getStream(i).getCategoryCount(); j++) {
                flattenedCategories[c] = categoryColors[i][j];
                c++;
            }
        }
        long t0 = System.currentTimeMillis();
        if (recompute) {
            eventCount = 0;
            initializeDataArray(totalCategories);
            for (int i = 0, coff = 0; i < streams.size(); i++) {
                eventCount += computeOscilloscopeData((TimeIntervalStream) streams.getStream(i), coff);
                coff += streams.getStream(i).getCategoryCount();
            }
            rescaleData(displayedStrips, r.width, totalCategories, osc.getInterleaveFactor());
        }
        ArrayList<Integer> hoveredCategories = new ArrayList<Integer>();
        long t1 = System.currentTimeMillis();
        for (int c = 0; c < totalCategories; c++) {
            int yAdjustment = yAdjustment(c, totalCategories);
            drawOscilloscope(yBase, yAdjustment, c, flattenedCategories, interleavedStripHeight, stripDataHeight, eventCount, hoveredCategories);
        }
        long t2 = System.currentTimeMillis();
        drawStripMargins(yBase, yMarginAdjustment);
        long t3 = System.currentTimeMillis();
        drawPhaseBoundaries(ppt);
        long t4 = System.currentTimeMillis();
        if (PERFORMANCE_DEBUG) {
            Logging.errorln("total events " + eventCount);
            Logging.errorln("recompute " + (t1 - t0));
            Logging.errorln("yAdj+drawOsc " + (t2 - t1));
            Logging.errorln("drawMargins " + (t3 - t2));
            Logging.errorln("drawPhaseBoundaries " + (t4 - t3));
        }
        return hoveredCategories;
    }

    public boolean computeGeometry() {
        if (minTime == osc.effectiveStartTime() && maxTime == osc.effectiveEndTime() && displayedStrips == osc.displayedStrips() && foldingFactor == osc.getFoldFactor() && stripInterleaving == osc.effectiveInterleaveFactor() && splitScreen == osc.getSplitScreen() && width == r.width && height == r.height && strips == osc.getStrips() && categories == osc.getCategoryCount()) {
            return false;
        }
        minTime = osc.effectiveStartTime();
        maxTime = osc.effectiveEndTime();
        displayedStrips = osc.displayedStrips();
        width = r.width;
        height = r.height;
        foldingFactor = osc.getFoldFactor();
        categories = osc.getCategoryCount();
        stripInterleaving = osc.effectiveInterleaveFactor();
        splitScreen = osc.getSplitScreen();
        final int stripHeight = osc.stripHeight();
        final int actualStrips = osc.actualStrips();
        interleavedStripHeight = osc.getSplitScreen() ? stripHeight : stripHeight * stripInterleaving;
        final int wastedStrips = actualStrips - displayedStrips * stripInterleaving;
        computeMargins(stripHeight);
        yBase = r.y + r.height - stripHeight * (actualStrips - wastedStrips);
        ppt = (r.width / (double) (maxTime - minTime)) * (osc.virtualStrips());
        return true;
    }

    TimeInterval[] streamIntervals;

    protected boolean streamsChangedP(StreamBundle streams) {
        boolean changed = false;
        if (streamIntervals == null || streams.size() != streamIntervals.length) {
            changed = true;
        }
        TimeInterval[] oldIntervals = streamIntervals;
        streamIntervals = new TimeInterval[streams.size()];
        for (int i = 0; i < streams.size(); i++) {
            streamIntervals[i] = streams.getStream(i).getTimeRange();
            if ((!changed) && (!oldIntervals[i].contains(minTime, maxTime)) && (!streamIntervals[i].equals(oldIntervals[i]))) {
                changed = true;
            }
        }
        return changed;
    }

    int yAdjustment(final int catIndex, final int totalCategories) {
        final int stripHeight = osc.stripHeight();
        final int stripInterleaving = osc.effectiveInterleaveFactor();
        final int categoryStrip = osc.getCyclicInterleaving() ? catIndex % stripInterleaving : catIndex / (totalCategories / stripInterleaving);
        int yAdjustment = 0;
        if (stripInterleaving > 1) {
            if (osc.getSplitScreen()) {
                yAdjustment = stripHeight * categoryStrip * displayedStrips;
            } else {
                yAdjustment = stripHeight * categoryStrip;
            }
        }
        return yAdjustment;
    }

    protected int computeOscilloscopeData(final TimeIntervalStream stream, final int baseCategory) {
        int events = 0;
        TimeIntervalStreamCursor cursor = stream.newCursor(minTime, maxTime);
        while (cursor.hasMore()) {
            AbstractTimeInterval interval = cursor.getNext();
            if (interval == null) {
                continue;
            }
            int cat = 0;
            cat += baseCategory;
            long startTime = interval.getStart();
            long endTime = interval.getEnd();
            if (CORRECTNESS_DEBUG) {
                if (endTime < minTime) {
                    Logging.verboseln(2, "got interval before minTime");
                }
                if (startTime > maxTime) {
                    Logging.verboseln(2, "got interval after maxTime");
                }
                if (endTime < startTime) {
                    Logging.errorln("Interval goes backward in time");
                    continue;
                }
            }
            if (startTime > maxTime) {
                continue;
            }
            if (startTime < minTime) {
                startTime = minTime;
            }
            if (endTime > maxTime) {
                endTime = maxTime;
            }
            contributeEvent(ppt, cat, startTime, endTime);
            events++;
        }
        return events;
    }

    private void initializeDataArray(final int cats) {
        if (intervalStats == null || intervalStats.length < displayedStrips || intervalStats[0].length < r.width || intervalStats[0][0].length < cats) {
            intervalStats = new double[displayedStrips][r.width][cats];
        } else {
            for (int s = 0; s < displayedStrips; s++) {
                for (int b = 0; b < r.width; b++) {
                    for (int c = 0; c < cats; c++) {
                        intervalStats[s][b][c] = 0;
                    }
                }
            }
        }
    }

    private final void contributeEvent(final double ppt, int cat, long startTime, long endTime) {
        final long stripTime = osc.stripTime();
        int startStrip = (int) ((startTime - minTime) / stripTime);
        int endStrip = (int) ((endTime - minTime) / stripTime);
        long startOffset = (startTime - minTime) % stripTime;
        long endOffset = (endTime - minTime) % stripTime;
        double dStart = startOffset * ppt;
        double dEnd = endOffset * ppt;
        int xStart = (int) dStart;
        int xEnd = (int) dEnd;
        double startFrac = ((xStart + 1)) - dStart;
        double endFrac = dEnd - xEnd;
        if (xStart == xEnd && startStrip == endStrip) {
            startFrac = (endTime - startTime) * ppt;
            endFrac = 0;
        }
        if (startStrip >= intervalStats.length) {
            Logging.verboseln(1, "intervalStats.len = " + intervalStats.length + "   startStrip = " + startStrip);
            Logging.verboseln(1, "     minTime,maxTime   = " + minTime + "     " + maxTime);
            Logging.verboseln(1, "     startTime,endTime = " + startTime + "     " + endTime);
        }
        if (false) {
            System.err.println("  length " + (endTime - startTime) + "   pixlen " + ((endTime - startTime) * ppt) + "  xStart " + xStart + "  res " + startFrac + "  xEnd " + xEnd + "  endFrac " + endFrac + "  start " + startTime + "  dstart " + dStart + "  dend " + dEnd);
        }
        if (endStrip >= displayedStrips * foldingFactor) {
            endStrip = displayedStrips * foldingFactor - 1;
            xEnd = r.width - 1;
            endFrac = 0;
        } else if (xEnd >= r.width) {
            xEnd = r.width - 1;
            endFrac = 0;
        }
        if (osc.getFullPixelEvents()) {
            if (startStrip == endStrip) {
                for (int i = xStart; i <= xEnd; i++) {
                    intervalStats[startStrip / foldingFactor][i][cat] += 1.0;
                }
            } else {
                for (int i = xStart; i < r.width; i++) {
                    intervalStats[startStrip / foldingFactor][i][cat] += 1.0;
                }
                for (int s = startStrip + 1; s < endStrip; s++) {
                    for (int i = 0; i < r.width; i++) {
                        intervalStats[s / foldingFactor][i][cat] += 1.0;
                    }
                }
                for (int i = 0; i <= xEnd; i++) {
                    intervalStats[endStrip / foldingFactor][i][cat] += 1.0;
                }
            }
        } else {
            intervalStats[startStrip / foldingFactor][xStart][cat] += startFrac;
            if (startStrip == endStrip) {
                for (int i = xStart + 1; i < xEnd; i++) {
                    intervalStats[startStrip / foldingFactor][i][cat] += 1.0;
                }
            } else {
                for (int i = xStart + 1; i < r.width; i++) {
                    intervalStats[startStrip / foldingFactor][i][cat] += 1.0;
                }
                for (int s = startStrip + 1; s < endStrip; s++) {
                    for (int i = 0; i < r.width; i++) {
                        intervalStats[s / foldingFactor][i][cat] += 1.0;
                    }
                }
                for (int i = 0; i < xEnd; i++) {
                    intervalStats[endStrip / foldingFactor][i][cat] += 1.0;
                }
            }
            int lastRow = endStrip / foldingFactor + (xEnd < r.width ? 0 : 1);
            intervalStats[lastRow][xEnd][cat] += endFrac;
        }
    }

    protected void rescaleData(int strips, int buckets, int categories, int interleave) {
        for (int s = 0; s < strips; s++) {
            for (int b = 0; b < buckets; b++) {
                for (int v = 0; v < interleave; v++) {
                    double total = 0;
                    int maxcat = 0;
                    for (int c = v; c < categories; c += interleave) {
                        total += intervalStats[s][b][c];
                        maxcat = c;
                    }
                    if (total == 0) {
                        continue;
                    }
                    for (int c = v; c < categories; c += interleave) {
                        double original = intervalStats[s][b][c];
                        double fraction = original / total;
                        double scaled = original * fraction;
                        intervalStats[s][b][c] = scaled;
                    }
                    double alphaScale = 1;
                    for (int c = maxcat; c >= 0; c -= interleave) {
                        double oldValue = intervalStats[s][b][c];
                        intervalStats[s][b][c] = oldValue * alphaScale;
                        alphaScale = alphaScale * (1 - oldValue);
                    }
                }
            }
        }
    }

    protected void drawOscilloscope(final int yBase, int yAdjustment, final int category, final RGBColor[] categoryColor, final int interleavedStripHeight, int stripDataHeight, int eventCount, ArrayList<Integer> hoveredCategories) {
        final double roughness = computeRoughness(eventCount);
        final AbstractList<Coord> hovers = new ArrayList<Coord>();
        if (osc.getDisplayHoverInfo() && osc.getLocalAndSharedHovers() != null) for (HoverState hoverState : osc.getLocalAndSharedHovers()) if (hoverState != null && hoverState.hover != null) hovers.add(hoverState.hover);
        int polygons = 0;
        for (int s = 0; s < displayedStrips; s++) {
            final int yStart = s * interleavedStripHeight + MARGIN_SEPARATOR_WIDTH + yBase + yAdjustment;
            for (int b = 0; b < r.width; b++) {
                if (intervalStats[s][b][category] == 0.0) {
                    continue;
                }
                final double intensity = intervalStats[s][b][category];
                final int xStart = b;
                while (++b < r.width && roughlyEqualIntensity(intervalStats[s][b][category], intensity, roughness)) {
                    ;
                }
                final int xEnd = --b;
                double shadeIndex = intensity;
                if (shadeIndex > foldingFactor) {
                    shadeIndex = foldingFactor;
                    Logging.verboseln(1, "Intensity value error: " + intensity);
                }
                final int alpha = FOLDED_DATA_BASE_ALPHA + (int) (shadeIndex / (foldingFactor) * (FOLDED_DATA_ALPHA_RANGE));
                final int x = xStart + r.x;
                final int width = xEnd - xStart + 1;
                g.fillRectangle(categoryColor[category], alpha, x, yStart, width, stripDataHeight);
                polygons++;
                for (Coord hover : hovers) if (hover.containedBy(x, yStart, width, stripDataHeight)) hoveredCategories.add(category);
            }
        }
        if (PERFORMANCE_DEBUG) {
            Logging.errorln("  At roughness " + roughness + " drew " + polygons + " polygons");
        }
    }

    private static final double BASE_ROUGHNESS = 0.04;

    private static final double MAX_ROUGHNESS = 0.3;

    private static final double SWT_ROUGHING_FACTOR = 2.5;

    private static final double ROUGHNESS_SCALING_BASE = 9;

    private final double computeRoughness(int eventCount) {
        double graphicsScaling = g instanceof SWTGraphics ? SWT_ROUGHING_FACTOR : 1.0;
        double eventScaling = Math.log(eventCount) - ROUGHNESS_SCALING_BASE;
        eventScaling = eventScaling > 1 ? eventScaling : 1;
        double roughness = BASE_ROUGHNESS * graphicsScaling * eventScaling;
        roughness = Math.min(roughness, MAX_ROUGHNESS);
        if (PERFORMANCE_DEBUG) {
            Logging.errorln("roughness " + roughness + "  eventscale " + eventScaling + "   gscale " + graphicsScaling);
        }
        return roughness;
    }

    private static final boolean roughlyEqualIntensity(double i1, double i2, double roughness) {
        if (i1 == 0.0 || i2 == 0.0) {
            return i1 == i2;
        } else {
            double diff = i1 - i2;
            return -roughness <= diff && diff <= roughness;
        }
    }

    protected final double MARGIN_FRACTION = 0.1;

    protected final double SEPARATOR_FRACTION = 0.03;

    protected final double SPLIT_SCREEN_ALPHA_WEIGHT = 1.2;

    protected final double INTERLEAVED_ALPHA_WEIGHT = 1.75;

    protected int separatorSize = 1;

    protected int separatorAlpha = Graphics.SOLID_ALPHA;

    private void computeMargins(final int stripHeight) {
        if (displayedStrips == 1 && stripInterleaving == 1) {
            stripDataHeight = stripHeight;
            interStripMargin = 0;
            separatorSize = 0;
            separatorAlpha = 0;
            return;
        }
        if (false) {
            interStripMargin = STANDARD_MARGIN;
            if (stripHeight - 2 * WIDE_MARGIN > (int) (1.6 * WIDE_MARGIN)) {
                interStripMargin = WIDE_MARGIN;
            }
        } else {
            interStripMargin = Math.max((int) (stripHeight * MARGIN_FRACTION), 1);
            separatorSize = Math.max((int) (stripHeight * SEPARATOR_FRACTION), 1);
            double weight = (splitScreen ? SPLIT_SCREEN_ALPHA_WEIGHT : 1) * (stripInterleaving > 1 ? INTERLEAVED_ALPHA_WEIGHT : 1);
            separatorAlpha = Math.min(Graphics.SOLID_ALPHA, (int) (Graphics.SOLID_ALPHA * (((double) stripHeight * 2) / height) * weight));
            if (stripInterleaving > 1) {
                separatorAlpha = Graphics.SOLID_ALPHA;
            }
        }
        stripDataHeight = stripHeight - 2 * interStripMargin;
    }

    private static final int SPLIT_SCREEN_MARGIN_HACK = 30;

    private static final boolean USE_DOTTED_LINES = false;

    protected void drawStripMargins(int yBase, int yMarginAdjustment) {
        final int stripInterleaving = osc.effectiveInterleaveFactor();
        final int stripHeight = osc.stripHeight();
        final int actualStrips = osc.actualStrips();
        yBase -= (interStripMargin - 1);
        if (osc.getSplitScreen() && stripInterleaving > 1) {
            for (int i = 1; i < stripInterleaving; i++) {
                g.fillRectangle(g.black, r.x - SPLIT_SCREEN_MARGIN_HACK, i * displayedStrips * stripHeight + yBase - interStripMargin, r.width + SPLIT_SCREEN_MARGIN_HACK, 2 * interStripMargin);
            }
        } else {
            for (int i = stripInterleaving; i < actualStrips; i += stripInterleaving) {
                int solid = 1;
                int white = 0;
                if (stripInterleaving > 1) {
                    solid = separatorSize * 3;
                    white = separatorSize;
                }
                if (USE_DOTTED_LINES) {
                    g.drawDottedLine(g.black, separatorAlpha, r.x, i * stripHeight + yBase - separatorSize, r.width, 2 * separatorSize, solid, white);
                } else {
                    g.fillRectangle(g.black, separatorAlpha, r.x, i * stripHeight + yBase - separatorSize, r.width, 2 * separatorSize);
                }
            }
        }
    }

    protected void drawPhaseBoundaries(final double ppt) {
        final long timeWindowSize = osc.getTimeWindow();
        final long phaseLength = osc.getPhaseLength();
        if (phaseLength > 0) {
            final long displayedPhases = timeWindowSize / phaseLength;
            if (displayedPhases > 1 && r.width / displayedPhases > MIN_PHASE_DISPLAY_WIDTH) {
                int sepwidth = PHASE_SEPARATOR_WIDTH;
                int sepwhite = PHASE_SEPARATOR_WHITE;
                int sepdark = PHASE_SEPARATOR_DARK;
                if (r.width / displayedPhases < MIN_PHASE_DISPLAY_FULL_SEPARATOR_WIDTH) {
                    sepwidth = 1;
                    sepwhite = 1;
                    sepdark = 1;
                }
                for (int i = 1; i < displayedPhases; i++) {
                    int xx = r.x + (int) ((i * phaseLength) * ppt);
                    g.drawDottedLine(marginColor, PHASE_SEPARATOR_ALPHA, xx - sepwidth / 2, r.y, sepwidth, r.height, sepdark, sepwhite);
                }
            }
        }
    }

    public void paintBookmarks(final Oscilloscope osc, final Graphics g, final Area r, BookmarkManager bookmarkManager) {
        BookmarkStream bookmarkStream = bookmarkManager.getBookmarks();
        if (bookmarkStream == null) {
            return;
        }
        final long stripTime = osc.stripTime();
        Vector<BookmarkLocation> bookmarks = new Vector<BookmarkLocation>();
        final boolean interleavedUnsplit = osc.getInterleaveFactor() > 1 && !osc.getSplitScreen();
        int label = 0;
        int firstDisplayedLabel = 0;
        EventStreamCursor cursor = bookmarkStream.newCursor(osc.getMinimumTimestamp(), maxTime);
        while (cursor.hasMore()) {
            Bookmark bookmark = (Bookmark) cursor.getNext();
            if (bookmark == null) {
                continue;
            }
            label++;
            long time = bookmark.getTime();
            if (time < minTime || time > maxTime) {
                continue;
            }
            if (firstDisplayedLabel == 0) {
                firstDisplayedLabel = label;
            }
            boolean overflow = label - firstDisplayedLabel > BookmarkMaster.MAX_BOOKMARKS_TO_DISPLAY;
            String labelString = overflow ? "More..." : bookmark.getAnnotation();
            int startStrip = (int) ((time - minTime) / stripTime);
            long startOffset = (time - minTime) % stripTime;
            int xStart = (int) (startOffset * ppt);
            int xPos = r.x + xStart;
            int yPos;
            if (!interleavedUnsplit) {
                yPos = startStrip / osc.getFoldFactor() * interleavedStripHeight + MARGIN_SEPARATOR_WIDTH + yBase + stripDataHeight;
            } else {
                yPos = (startStrip / osc.getFoldFactor() + 1) * interleavedStripHeight + MARGIN_SEPARATOR_WIDTH + yBase;
            }
            AnnotationPainter ap = new AnnotationPainter(g);
            boolean isCurrent = (time == bookmarkManager.getCurrent());
            Area bookmarkArea = ap.paintBookmark(g, Graphics.SOLID_ALPHA, labelString, new Coord(xPos, yPos), !isCurrent, AnnotationPainter.BELOW_UPWARD);
            bookmarks.add(new BookmarkLocation(bookmarkArea, time, labelString));
            if (interleavedUnsplit) {
                bookmarkManager.drawBookmarkLine(g, xPos, yPos - interleavedStripHeight, interleavedStripHeight);
            }
            if (overflow) {
                break;
            }
        }
        bookmarkManager.bookmarkLocations = bookmarks;
    }
}
