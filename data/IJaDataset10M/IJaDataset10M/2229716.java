package net.sf.statcvs.output;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.Map.Entry;
import net.sf.statcvs.Messages;
import net.sf.statcvs.charts.ChartImage;
import net.sf.statcvs.charts.SymbolicNameAnnotation;
import net.sf.statcvs.model.Revision;
import net.sf.statcvs.output.ReportConfig;
import net.sf.statcvs.pages.NavigationNode;
import net.sf.statcvs.pages.Page;
import net.sf.statcvs.reports.LOCSeriesBuilder;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

/**
 * A LOC and Churn Chart shows both the LOC and the number of lines touched per
 * day, this allows you to see the evolution of lines of code and the amount of
 * changes. A flat LOC with a lot of Churn implies a lot of refactoring, an
 * increase in LOC in line with churn implies new functionality.
 * 
 * @author Benoit Xhenseval (www.ObjectLab.co.uk)
 */
public class ChurnPageMaker {

    private final ReportConfig config;

    /**
     * @see net.sf.statcvs.output.HTMLPage#HTMLPage(Repository)
     */
    public ChurnPageMaker(final ReportConfig config) {
        this.config = config;
    }

    public NavigationNode toFile() {
        final Page page = this.config.createPage("churn", Messages.getString("CHURN_TITLE"), Messages.getString("CHURN_TITLE"));
        page.addRawContent("\n\n<!-- The LOC and Churn Report was designed by Benoit Xhenseval (http://www.objectlab.co.uk/open)-->");
        page.addRawContent("\n<!-- Initially part of StatSVN -->\n\n");
        page.addRawContent("<p>" + Messages.getString("CHURN_DESCRIPTION") + "</p>");
        page.add(buildChart());
        return page;
    }

    private ChartImage buildChart() {
        final Map changePerRevision = new HashMap();
        final SortedSet revisions = config.getRepository().getRevisions();
        for (final Iterator it = revisions.iterator(); it.hasNext(); ) {
            final Revision rev = (Revision) it.next();
            final Date dateToUse = blastTime(rev.getDate());
            final Integer changes = (Integer) changePerRevision.get(dateToUse);
            if (changes == null) {
                changePerRevision.put(dateToUse, new Integer(Math.abs(getLineChanges(rev))));
            } else {
                changePerRevision.put(dateToUse, new Integer(Math.abs(changes.intValue()) + getLineChanges(rev)));
            }
        }
        final List annotations = SymbolicNameAnnotation.createAnnotations(config.getRepository().getSymbolicNames());
        final TimeSeries timeLine = new TimeSeries(Messages.getString("CHURN_TOUCHED_LINE"), Day.class);
        for (final Iterator it = changePerRevision.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry entry = (Entry) it.next();
            timeLine.add(new Day((Date) entry.getKey()), ((Integer) entry.getValue()).intValue());
        }
        final TimeSeries locSeries = getLOCTimeSeries(revisions, Messages.getString("TIME_LOC_SUBTITLE"));
        final LOCChurnChartMaker chart = new LOCChurnChartMaker(config, timeLine, locSeries, Messages.getString("LOC_CHURN_CHART_TITLE"), "locandchurn.png", config.getLargeChartSize(), annotations);
        return chart.toFile();
    }

    private Date blastTime(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private int getLineChanges(final Revision rev) {
        if (rev.isDead()) {
            return rev.getLinesDelta();
        }
        return Math.abs(rev.getLinesDelta()) + 2 * rev.getReplacedLines();
    }

    private TimeSeries getLOCTimeSeries(final SortedSet revisions, final String title) {
        final Iterator it = revisions.iterator();
        final LOCSeriesBuilder locCounter = new LOCSeriesBuilder(title, true);
        while (it.hasNext()) {
            locCounter.addRevision((Revision) it.next());
        }
        return locCounter.getTimeSeries();
    }
}
