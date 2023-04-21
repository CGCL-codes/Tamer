package com.entelience.metrics.events;

import org.apache.xerces.parsers.XMLDocumentParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import com.entelience.util.XMLHelper;
import com.entelience.objects.metricsquery.Dim;
import com.entelience.objects.metricsquery.DrilldownView;
import com.entelience.objects.metricsquery.ForeignKeyParam;
import com.entelience.objects.metricsquery.HierarchyParam;
import com.entelience.objects.metricsquery.Metric;
import com.entelience.objects.metricsquery.OriginView;
import com.entelience.objects.metricsquery.PivotParams;
import com.entelience.objects.metricsquery.PivotView;
import com.entelience.objects.metricsquery.QueryBean;
import com.entelience.objects.metricsquery.QueryBeanItem;
import com.entelience.objects.metricsquery.QueryType;
import com.entelience.objects.metricsquery.RangeQuery;
import com.entelience.objects.metricsquery.SpotQuery;
import com.entelience.objects.metricsquery.StringParam;
import com.entelience.objects.metricsquery.Window;

/**
 * XML parser to configure query objects for metrics, aggregates etc.
 * <br/>
 * Parse a QueryBean object (soap- compatible query description) and
 * create a Query object from the configuration Bundle to satisfy the
 * QueryBean.
 * <br/>
 * Unit test-cases have an entry point into the results via this XML file
 * format.
 * <br/>
 * TODO: Allow this format to be sent to a jsp query handler.
 */
public class QueryXMLParser extends XMLDocumentParser implements XMLErrorHandler {

    private static org.apache.log4j.Logger _logger = com.entelience.util.Logs.getLogger();

    private QueryBean queryBean = null;

    private QueryBeanItem currentItem = null;

    private QueryType currentQuery = null;

    private PivotParams currentParams = null;

    public void startElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        if ("range-query".equals(element.localpart)) {
            if (currentQuery == null) {
                if (currentItem != null) throw new IllegalStateException("Error with parser - currentItem not null");
                String id = attrs.getValue("id");
                if (id == null) throw new IllegalArgumentException("Must provide attribute 'id'");
                currentQuery = new RangeQuery();
                currentItem = new QueryBeanItem();
                currentItem.setId(id);
                currentItem.setQuery(currentQuery);
                queryBean.addQueryItem(currentItem);
                String metric = attrs.getValue("metric");
                if (metric != null) {
                    Metric m = new Metric();
                    m.setMetric(metric);
                    currentQuery.addMetric(m);
                }
                String after = attrs.getValue("after");
                if (after != null) {
                    ((RangeQuery) currentQuery).setAfter(QueryHelper.parseDate(after));
                }
                String before = attrs.getValue("before");
                if (before != null) {
                    ((RangeQuery) currentQuery).setBefore(QueryHelper.parseDate(before));
                }
                String window = attrs.getValue("window");
                String slice = attrs.getValue("slice");
                if (window != null || slice != null) {
                    Window w = new Window();
                    if (window == null) {
                        w.setWindow(1);
                    } else {
                        w.setWindow(QueryHelper.parseWindow(window));
                    }
                    if (slice == null) {
                        w.setSlice(null);
                    } else {
                        w.setSlice(slice);
                    }
                    currentQuery.addWindow(w);
                }
            } else throw new IllegalStateException("Unable to nest <range-query/> tags");
        } else if ("spot-query".equals(element.localpart)) {
            if (currentQuery == null) {
                if (currentItem != null) throw new IllegalStateException("Error with parser - currentItem not null");
                String id = attrs.getValue("id");
                if (id == null) throw new IllegalArgumentException("Must provide attribute 'id'");
                currentQuery = new SpotQuery();
                currentItem = new QueryBeanItem();
                currentItem.setId(id);
                currentItem.setQuery(currentQuery);
                queryBean.addQueryItem(currentItem);
                String metric = attrs.getValue("metric");
                if (metric != null) {
                    Metric m = new Metric();
                    m.setMetric(metric);
                    currentQuery.addMetric(m);
                }
                String date = attrs.getValue("date");
                if (date == null) {
                    ((SpotQuery) currentQuery).setDate(null);
                    ((SpotQuery) currentQuery).setCheckDateLatest(false);
                } else {
                    if ("latest".equals(date)) {
                        ((SpotQuery) currentQuery).setDate(null);
                        ((SpotQuery) currentQuery).setCheckDateLatest(true);
                    } else {
                        ((SpotQuery) currentQuery).setDate(QueryHelper.parseDate(date));
                        ((SpotQuery) currentQuery).setCheckDateLatest(false);
                    }
                }
                String window = attrs.getValue("window");
                String slice = attrs.getValue("slice");
                if (window != null || slice != null) {
                    Window w = new Window();
                    if (window == null) {
                        w.setWindow(1);
                    } else {
                        w.setWindow(QueryHelper.parseWindow(window));
                    }
                    if (slice == null) {
                        w.setSlice(null);
                    } else {
                        w.setSlice(slice);
                    }
                    currentQuery.addWindow(w);
                }
            } else throw new IllegalStateException("Unable to nest <spot-query/> tags");
        } else if ("after".equals(element.localpart)) {
            String date = attrs.getValue("date");
            if (date == null) throw new IllegalArgumentException("Attribute date of tag <after/> must be specified.");
            if (currentQuery == null || !(currentQuery instanceof RangeQuery)) {
                throw new IllegalStateException("<after/> is only valid inside <range-query> ... </range-query>");
            } else {
                ((RangeQuery) currentQuery).setAfter(QueryHelper.parseDate(date));
            }
        } else if ("before".equals(element.localpart)) {
            String date = attrs.getValue("date");
            if (date == null) throw new IllegalArgumentException("Attribute date of tag <before/> must be specified.");
            if (currentQuery == null || !(currentQuery instanceof RangeQuery)) {
                throw new IllegalStateException("<before/> is only valid inside <range-query> ... </range-query>");
            } else {
                ((RangeQuery) currentQuery).setBefore(QueryHelper.parseDate(date));
            }
        } else if ("metric".equals(element.localpart)) {
            String metric = attrs.getValue("name");
            if (metric == null) throw new IllegalArgumentException("Attribute name of tag <metric/> must be specified.");
            if (currentQuery == null) {
                throw new IllegalStateException("<metric/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            } else {
                currentQuery.addMetric(new Metric(metric));
            }
        } else if ("date".equals(element.localpart)) {
            String date = attrs.getValue("date");
            if (date == null) throw new IllegalArgumentException("Attribute date of tag <date/> must be specified.");
            if (currentQuery == null || !(currentQuery instanceof SpotQuery)) {
                throw new IllegalStateException("<date/> is only valid inside <spot-query> ... </spot-query>");
            } else {
                if ("latest".equals(date)) {
                    ((SpotQuery) currentQuery).setDate(null);
                    ((SpotQuery) currentQuery).setCheckDateLatest(true);
                } else {
                    ((SpotQuery) currentQuery).setDate(QueryHelper.parseDate(date));
                    ((SpotQuery) currentQuery).setCheckDateLatest(false);
                }
            }
        } else if ("window".equals(element.localpart)) {
            String days = attrs.getValue("days");
            String slice = attrs.getValue("slice");
            if (days == null) throw new IllegalArgumentException("Attribute days of tag <window/> must be specified.");
            if (slice == null) slice = null;
            if (currentQuery == null) {
                throw new IllegalStateException("<window/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            } else {
                Window w = new Window();
                if (days == null) {
                    w.setWindow(1);
                } else {
                    w.setWindow(QueryHelper.parseWindow(days));
                }
                if (slice == null) {
                    w.setSlice(null);
                } else {
                    w.setSlice(slice);
                }
                currentQuery.addWindow(w);
            }
        } else if ("origin".equals(element.localpart)) {
            if (currentQuery == null) throw new IllegalStateException("<origin/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            OriginView view = new OriginView();
            currentQuery.addView(view);
        } else if ("pivot".equals(element.localpart)) {
            if (currentQuery == null) throw new IllegalStateException("<pivot/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            PivotView view = new PivotView();
            if (currentParams == null) {
                currentParams = new PivotParams();
                currentQuery.setParams(currentParams);
            }
            view.setPivot(currentParams);
            currentQuery.addView(view);
        } else if ("drilldown".equals(element.localpart)) {
            if (currentQuery == null) throw new IllegalStateException("<drilldown/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            String dimname = attrs.getValue("name");
            if (dimname == null) throw new IllegalArgumentException("Attribute 'name' of tag <drilldown/> must be specified.");
            DrilldownView view = new DrilldownView();
            if (currentParams == null) {
                currentParams = new PivotParams();
                currentQuery.setParams(currentParams);
            }
            view.setPivot(currentParams);
            view.setDrilldown(dimname);
            currentQuery.addView(view);
        } else if ("foreignkey-param".equals(element.localpart)) {
            String dimname = attrs.getValue("name");
            if (dimname == null) throw new IllegalArgumentException("Attribute 'name' of tag <foreignkey-param/> must be specified.");
            if (currentQuery == null) throw new IllegalStateException("<foreignkey-param/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            if (currentParams == null) {
                currentParams = new PivotParams();
                currentQuery.setParams(currentParams);
            }
            ForeignKeyParam param = new ForeignKeyParam();
            Dim dim = new Dim(dimname, 0);
            param.setDim(dim);
            String value_null = attrs.getValue("value_null");
            if (value_null == null) {
                String value = attrs.getValue("value");
                if (value == null) throw new IllegalArgumentException("Attribute 'value' (or 'value_null') of <foreignkey-param/> must be specified.");
                param.setValue(Integer.valueOf(value));
                param.setOrigin(false);
            } else {
                param.setValue(null);
                param.setOrigin(true);
            }
            currentParams.addParam(param);
        } else if ("hierarchy-param".equals(element.localpart)) {
            String dimname = attrs.getValue("name");
            if (dimname == null) throw new IllegalArgumentException("Attribute 'name' of tag <hierarchy-param/> must be specified.");
            if (currentQuery == null) throw new IllegalStateException("<hierarchy-param/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            if (currentParams == null) {
                currentParams = new PivotParams();
                currentQuery.setParams(currentParams);
            }
            HierarchyParam param = new HierarchyParam();
            String depth = attrs.getValue("depth");
            if (depth == null) throw new IllegalArgumentException("Attribute 'depth' of tag <hierarchy-param/> must be specified.");
            int n = Integer.parseInt(depth) - 1;
            if (n < -1) n = -1;
            Dim dim = new Dim(dimname, n);
            param.setDim(dim);
            if (n + 1 < 0) throw new IllegalArgumentException("Attribute 'depth' of tag <hierarchy-param/> must be an integer: 0 or larger.");
            final String value[] = new String[n + 1];
            for (int i = 0; i < (n + 1); ++i) {
                value[i] = attrs.getValue("value_" + i);
                if (value[i] == null) {
                    String value_null = attrs.getValue("value_" + i + "_null");
                    if (value_null == null) throw new IllegalArgumentException("Attribute 'value_" + i + "' (or 'value_" + i + "_null') of tag <hierarchy-param/> must be specified.");
                    value[i] = null;
                }
            }
            if (n >= 0) {
                param.setValue(value);
                param.setOrigin(false);
            } else {
                param.setValue(null);
                param.setOrigin(true);
            }
            currentParams.addParam(param);
        } else if ("string-param".equals(element.localpart)) {
            String dimname = attrs.getValue("name");
            if (dimname == null) throw new IllegalArgumentException("Attribute 'name' of tag <string-param/> must be specified.");
            if (currentQuery == null) throw new IllegalStateException("<string-param/> is only valid inside <range-query> ... </range-query> or <spot-query> ... </spot-query>");
            if (currentParams == null) {
                currentParams = new PivotParams();
                currentQuery.setParams(currentParams);
            }
            StringParam param = new StringParam();
            Dim dim = new Dim(dimname, 0);
            param.setDim(dim);
            String value = attrs.getValue("value");
            if (value == null) {
                if (attrs.getValue("value_null") == null) {
                    throw new IllegalArgumentException("Attribute 'value' (or 'value_null') of tag <string-param/> must be specified.");
                } else {
                    param.setValue(null);
                    param.setOrigin(true);
                }
            } else {
                param.setValue(value);
                param.setOrigin(false);
            }
            currentParams.addParam(param);
        }
    }

    public void endElement(QName element, Augmentations augs) throws XNIException {
        if ("spot-query".equals(element.localpart) || "range-query".equals(element.localpart)) {
            currentParams = null;
            currentQuery = null;
            currentItem = null;
        }
    }

    public QueryBean parse(String filename) throws Exception {
        queryBean = new QueryBean();
        parse(new XMLInputSource(null, filename, null));
        return queryBean;
    }

    /** Default constructor. */
    private QueryXMLParser() {
        super();
        fConfiguration.setErrorHandler(this);
        XMLHelper.setConfigFeatures(fConfiguration);
    }

    public static QueryXMLParser newParser() {
        return new QueryXMLParser();
    }

    /** Warning. */
    public void warning(String domain, String key, XMLParseException ex) throws XNIException {
        printError("Warning", ex);
    }

    /** Error. */
    public void error(String domain, String key, XMLParseException ex) throws XNIException {
        printError("Error", ex);
    }

    /** Fatal error. */
    public void fatalError(String domain, String key, XMLParseException ex) throws XNIException {
        printError("Fatal Error", ex);
        throw ex;
    }

    public void printError(String error, Throwable t) {
        _logger.error(error, t);
    }

    /** Prints the error message. */
    protected void printError(String type, XMLParseException ex) {
        StringBuffer msg = new StringBuffer();
        msg.append('[').append(type).append("] ");
        String systemId = ex.getExpandedSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1) systemId = systemId.substring(index + 1);
            msg.append(systemId);
        }
        msg.append(':').append(ex.getLineNumber()).append(':').append(ex.getColumnNumber()).append(": ").append(ex.getMessage());
        _logger.error(msg, ex);
    }
}
