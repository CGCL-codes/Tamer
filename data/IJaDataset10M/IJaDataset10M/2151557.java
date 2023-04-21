package com.entelience.objects.metricsquery;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.entelience.util.DateHelper;

/**
 * BEAN : Query a range of values of an aggregate between specific dates.
 * <br/>
 * Before is inclusive, after is not inclusive.
 * <br/>
 * If one of both of the dates are null then the query is unbounded on that side
 * or returns the whole range of values.
 */
public final class RangeQuery implements QueryType, java.io.Serializable {

    private transient java.util.Date before = null, after = null;

    private transient List<Metric> metrics = null;

    private transient List<Window> windows = null;

    private transient List<View> views = null;

    private transient PivotParams params = null;

    public RangeQuery() {
        this.after = null;
        this.before = null;
        this.metrics = null;
        this.windows = null;
        this.views = null;
        this.params = null;
    }

    public void setAfter(java.util.Date date) {
        this.after = date;
    }

    public java.util.Date getAfter() {
        return this.after;
    }

    public void setBefore(java.util.Date date) {
        this.before = date;
    }

    public java.util.Date getBefore() {
        return this.before;
    }

    public PivotParams getParams() {
        return this.params;
    }

    public void setParams(PivotParams params) {
        this.params = params;
    }

    public Metric[] getMetrics() {
        if (this.metrics == null) {
            return null;
        } else {
            Metric[] r = new Metric[this.metrics.size()];
            r = (Metric[]) this.metrics.toArray(r);
            return r;
        }
    }

    public void setMetrics(Metric metrics[]) {
        if (metrics == null) {
            this.metrics = null;
        } else {
            this.metrics = new ArrayList<Metric>(Arrays.asList(metrics));
        }
    }

    public void addMetric(Metric metric) {
        if (this.metrics == null) {
            this.metrics = new ArrayList<Metric>();
        }
        this.metrics.add(metric);
    }

    public Iterator<Metric> metricsIterator() {
        if (this.metrics == null) {
            return null;
        }
        return this.metrics.iterator();
    }

    public Window[] getWindows() {
        if (this.windows == null) {
            return null;
        } else {
            Window[] r = new Window[this.windows.size()];
            r = (Window[]) this.windows.toArray(r);
            return r;
        }
    }

    public void setWindows(Window windows[]) {
        if (windows == null) {
            this.windows = null;
        } else {
            this.windows = new ArrayList<Window>(Arrays.asList(windows));
        }
    }

    public void addWindow(Window window) {
        if (this.windows == null) {
            this.windows = new ArrayList<Window>();
        }
        this.windows.add(window);
    }

    public Iterator<Window> windowsIterator() {
        if (this.windows == null) {
            return null;
        }
        return this.windows.iterator();
    }

    public View[] getViews() {
        if (this.views == null) {
            return null;
        } else {
            View[] r = new View[this.views.size()];
            r = (View[]) this.views.toArray(r);
            return r;
        }
    }

    public void setViews(View views[]) {
        if (views == null) {
            this.views = null;
        } else {
            this.views = new ArrayList<View>(Arrays.asList(views));
        }
    }

    public void addView(View view) {
        if (this.views == null) {
            this.views = new ArrayList<View>();
        }
        this.views.add(view);
    }

    public Iterator<View> viewsIterator() {
        if (this.views == null) {
            return null;
        }
        return this.views.iterator();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
        sb.append('(');
        try {
            sb.append(" after=").append(DateHelper.isoStringOrNull(after));
            sb.append(" before=").append(DateHelper.isoStringOrNull(before));
        } catch (Exception e) {
            RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
        if (metrics == null) {
            sb.append(" metrics=null");
        } else {
            Iterator<Metric> i = metrics.iterator();
            int n = 0;
            while (i.hasNext()) {
                Metric m = i.next();
                sb.append(" metrics[").append(n++).append("]=").append(m.toString());
            }
        }
        if (windows == null) {
            sb.append(" windows=null");
        } else {
            Iterator<Window> i = windows.iterator();
            int n = 0;
            while (i.hasNext()) {
                Window w = i.next();
                sb.append(" windows[").append(n++).append("]=").append(w.toString());
            }
        }
        if (views == null) {
            sb.append(" views=null");
        } else {
            Iterator<View> i = views.iterator();
            int n = 0;
            while (i.hasNext()) {
                View v = i.next();
                sb.append(" views[").append(n++).append("]=").append(v.toString());
            }
        }
        if (params == null) {
            sb.append(" params=null");
        } else {
            sb.append(" params=").append(params);
        }
        sb.append(')');
        return sb.toString();
    }
}
