package com.kescom.matrix.tags.a;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.kescom.matrix.core.access.AccessGate;
import com.kescom.matrix.core.chart.ChartDisplayOptions;
import com.kescom.matrix.core.chart.ChartServlet;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.i18n.I18nWrapper;
import com.kescom.matrix.core.i18n.MessageBankUtils;
import com.kescom.matrix.core.opts.OptionsUtils;
import com.kescom.matrix.core.series.IOwned;
import com.kescom.matrix.core.series.ISeries;
import com.kescom.matrix.core.series.ISeriesExtents;
import com.kescom.matrix.core.time.CalendarIncrement;
import com.kescom.matrix.core.time.RelaxedDateFormat;
import com.kescom.matrix.core.time.TimeZoneUtils;
import com.kescom.matrix.core.user.IReportingEntity;
import com.kescom.matrix.core.user.ISeriesList;
import com.kescom.matrix.core.user.IUser;
import com.kescom.matrix.core.user.SeriesList;
import com.kescom.matrix.core.user.UserUtils;

@SuppressWarnings("serial")
public class GraphCtrlTag extends TagSupport {

    private String series = null;

    private String entity = null;

    private String list = null;

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            ISeries series = null;
            IReportingEntity entity = null;
            ISeriesList list = null;
            IOwned owned = null;
            if (this.series != null) owned = series = MatrixContext.getInstance().getEnvironment().getSerieses().find(this.series); else if (this.entity != null) owned = entity = MatrixContext.getInstance().getEnvironment().getEntities().find(this.entity); else if (this.list != null) owned = list = (ISeriesList) MatrixContext.getSession().load(SeriesList.class, this.list);
            IUser user = (IUser) pageContext.getAttribute("user");
            TimeZone timeZone = TimeZoneUtils.getUserTimeZone(user);
            AccessGate gate = new AccessGate(user);
            if (owned != null) {
                ChartDisplayOptions opts = (ChartDisplayOptions) OptionsUtils.loadRequestOptions(owned, "Chart.Overview", "Chart.Overview", ChartDisplayOptions.class, pageContext.getRequest(), gate);
                series = ChartServlet.buildResultSeries(series, entity, list, (HttpServletRequest) pageContext.getRequest(), null);
                long firstTime = series.getFirstTime();
                long lastTime = series.getLastTime();
                long fromExtent = firstTime;
                long toExtent = lastTime;
                series = ChartServlet.filterResultSeries(series, opts, (HttpServletRequest) pageContext.getRequest(), timeZone);
                if (series instanceof ISeriesExtents) {
                    ISeriesExtents extents = (ISeriesExtents) series;
                    fromExtent = extents.getFromExtent();
                    toExtent = extents.getToExtent();
                }
                CalendarIncrement period = CalendarIncrement.createBasedOnPeriod(fromExtent, toExtent);
                long zoomDelta = (toExtent - fromExtent) / 3;
                CalendarIncrement zoomPeriod = CalendarIncrement.createBasedOnPeriod(fromExtent + zoomDelta, toExtent - zoomDelta);
                dumpTime("firstTime", firstTime);
                dumpTime("lastTime", lastTime);
                dumpTime("fromExtent", fromExtent);
                dumpTime("toExtent", toExtent);
                RelaxedDateFormat rdf = (RelaxedDateFormat) MatrixContext.getInstance().getBeanFactory().getBean("RelaxedDateFormat", DateFormat.class);
                Calendar prevFromExtentCal = TimeZoneUtils.getCalendar(fromExtent);
                period.subFrom(prevFromExtentCal);
                String prevPeriod = rdf.format(prevFromExtentCal.getTime()) + " -> " + rdf.format(new Date(fromExtent - 1));
                Calendar nextToExtentCal = TimeZoneUtils.getCalendar(toExtent);
                period.addTo(nextToExtentCal);
                String nextPeriod = rdf.format(new Date(toExtent)) + " -> " + rdf.format(new Date(nextToExtentCal.getTimeInMillis() - 1));
                I18nWrapper i18nWrapper = (I18nWrapper) pageContext.getAttribute("i18nWrapper");
                boolean rtl = "rtl".equals(i18nWrapper.get("html.dir"));
                Calendar zoomInFromCal = TimeZoneUtils.getCalendar(fromExtent);
                zoomPeriod.addTo(zoomInFromCal);
                Calendar zoomInToCal = TimeZoneUtils.getCalendar(toExtent);
                zoomPeriod.addTo(zoomInToCal);
                String zoomInPeriod = rdf.format(zoomInFromCal.getTime()) + " -> " + rdf.format(new Date(zoomInToCal.getTimeInMillis() - 1));
                Calendar zoomOutFromCal = TimeZoneUtils.getCalendar(fromExtent);
                period.subFrom(zoomOutFromCal);
                Calendar zoomOutToCal = TimeZoneUtils.getCalendar(toExtent);
                period.addTo(zoomOutToCal);
                String zoomOutPeriod = rdf.format(zoomOutFromCal.getTime()) + " -> " + rdf.format(new Date(zoomOutToCal.getTimeInMillis() - 1));
                List<String> ctrls = new LinkedList<String>();
                ctrls.add(buildControl("window", "FirstMonth", "first_month", "gctrl/first.png"));
                ctrls.add(buildControl("window", prevPeriod, "prev_period", "gctrl/prev.png"));
                ctrls.add(buildControl("window", "all", "show_all", "gctrl/all.png"));
                ctrls.add(buildControl("window", nextPeriod, "next_period", "gctrl/next.png"));
                ctrls.add(buildControl("window", "LastMonth", "last_month", "gctrl/last.png"));
                if (rtl) Collections.reverse(ctrls);
                for (String ctrl : ctrls) out.println(ctrl);
                out.println(buildControl("window", "Today", "today", "gctrl/today.png"));
                out.println(buildControl("window", "ThisMonth", "this_month", "gctrl/month.png"));
                if (gate.isMaster()) {
                    out.println(buildControl("window", zoomInPeriod, "zoom_in", "gctrl/zoomin.png"));
                    out.println(buildControl("window", zoomOutPeriod, "zoom_out", "gctrl/zoomout.png"));
                }
            }
        } catch (Exception e) {
            throw new Error(e);
        }
        return SKIP_BODY;
    }

    private void dumpTime(String name, long value) {
        System.out.print(name + ": " + value);
        if (value >= 0) System.out.print(" (" + new Date(value) + ")");
        System.out.println("");
    }

    private String buildControl(String field, String value, String title, String icon) {
        String ownedParam = "";
        if (series != null) ownedParam = "series:'" + series + "'"; else if (entity != null) ownedParam = "entity:'" + entity + "'";
        if (list != null) ownedParam = "list:'" + list + "'";
        if (icon == null) icon = "entity.icon128.png";
        title = ((I18nWrapper) pageContext.getAttribute("i18nWrapper")).get("a.view.gctrl." + title);
        String html = ("<a href=\"javascript:update_main_panel('graph/graph_panel.jsp', {$1, '$2':'$3'})\" title=\"$4\")\">\n" + "<img src=\"../images/$5\" width=\"16\" height=\"16\" border=\"0\"/>\n" + "</a>\n").replace("$1", ownedParam).replace("$2", field).replace("$3", value).replace("$4", title).replace("$5", icon);
        return html;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }
}
