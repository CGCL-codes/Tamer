package com.bayareasoftware.chartengine.chart.jfree;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.data.general.Dataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.time.Week;
import org.jfree.data.time.Year;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.OHLCDataItem;
import com.bayareasoftware.chartengine.ds.DataStream;
import com.bayareasoftware.chartengine.model.ChartInfo;
import com.bayareasoftware.chartengine.model.SeriesDescriptor;
import com.bayareasoftware.chartengine.model.TimeConstants;

public class TimeProducer extends Producer implements TimeConstants {

    private Log log = LogFactory.getLog(TimeProducer.class);

    private int chartPeriodType = -1;

    private boolean bubbleRender = false;

    private List<double[]> bubbleVals = null;

    private boolean ohlcRender = false;

    private List<OHLCDataItem> ohlcVals = null;

    private boolean isStacked = false;

    /**
     * keep a mapping of SeriesDescriptor *or* seriesName to XYSeries
     * so we are not relying solely on series names to find the XYSeries
     * this allows the case of multiple series with the same name.  Chart looks goofy but it's better than
     * losing the same named series altogether
     */
    private Map<Object, TimeSeries> stMap;

    private Map<String, Integer> dynamicSeries = new HashMap();

    /**
     * @param periodType - a TimeConstants values  (e.g. TIME_YEAR)
     */
    public TimeProducer(int periodType) {
        super();
        this.chartPeriodType = periodType;
    }

    public Dataset createDataset(ChartInfo ci, SeriesDescriptor sd) {
        String rtype = sd.getRenderer();
        if (rtype == null) {
            rtype = ci.getRenderType();
        }
        if ("Bubble".equals(rtype)) {
            return new DefaultXYZDataset();
        } else if ("Candlestick".equals(rtype)) {
            return new DefaultOHLCDataset(sd.getName(), new OHLCDataItem[0]);
        } else if (rtype != null && rtype.toLowerCase().indexOf("stacked") != -1) {
            TimeTableXYDataset ret = new TimeTableXYDataset();
            ret.setXPosition(TimePeriodAnchor.START);
            return ret;
        } else {
            TimeSeriesCollection tsc = new TimeSeriesCollection();
            tsc.setXPosition(TimePeriodAnchor.MIDDLE);
            return tsc;
        }
    }

    public void beginSeries(Dataset d, SeriesDescriptor sd, DataStream r) {
        isStacked = bubbleRender = ohlcRender = false;
        if (d == null) {
        } else if (d instanceof DefaultXYZDataset) {
            bubbleRender = true;
            bubbleVals = new ArrayList<double[]>();
        } else if (d instanceof DefaultOHLCDataset) {
            ohlcRender = true;
            ohlcVals = new ArrayList<OHLCDataItem>();
        } else if (d instanceof TimeTableXYDataset) {
            isStacked = true;
        }
    }

    private boolean populateSingleOHLC(Dataset ds, SeriesDescriptor sd, DataStream rs) throws Exception {
        boolean res = false;
        Date d = rs.getDate(1);
        Double o = rs.getDouble(2);
        Double h = rs.getDouble(3);
        Double l = rs.getDouble(4);
        Double c = rs.getDouble(5);
        Double v = rs.getDouble(6);
        if (d != null && o != null && h != null && l != null && c != null && v != null) {
            ohlcVals.add(new OHLCDataItem(d, o, h, l, c, v));
            if (sd.getLinkExpression() != null) {
                String url = ChartContext.translateLinkExpression(rs, rs.getMetadata(), sd.getName(), sd.getLinkExpression());
                recordImgMapUrl(ds, sd.getName(), (double) d.getTime(), o, url);
            }
            res = true;
        } else {
        }
        return res;
    }

    private boolean populateSingleXYZ(Dataset d, SeriesDescriptor currentSD, DataStream rs) throws Exception {
        boolean res = false;
        int indx = currentSD.getXColumn();
        int indy = currentSD.getYColumn();
        int indz = currentSD.getZColumn();
        Date date = rs.getDate(indx);
        Double yval = rs.getDouble(indy);
        Double zval = rs.getDouble(indz);
        if (date != null && yval != null && zval != null) {
            double[] vals = new double[3];
            vals[0] = date.getTime();
            vals[1] = yval;
            vals[2] = zval;
            bubbleVals.add(vals);
            if (currentSD.getLinkExpression() != null) {
                String url = ChartContext.translateLinkExpression(rs, rs.getMetadata(), currentSD.getName(), currentSD.getLinkExpression());
                recordImgMapUrl(d, currentSD.getName(), (double) date.getTime(), yval, url);
            }
            res = true;
        }
        return res;
    }

    public Dataset endSeries(Dataset d, SeriesDescriptor sd) {
        if (bubbleRender) {
            DefaultXYZDataset dset = (DefaultXYZDataset) d;
            double[][] vals = new double[3][bubbleVals.size()];
            for (int i = 0; i < bubbleVals.size(); i++) {
                double[] row = bubbleVals.get(i);
                vals[0][i] = row[0];
                vals[1][i] = row[1];
                vals[2][i] = row[2];
            }
            dset.addSeries(sd.getName(), vals);
            return dset;
        } else if (ohlcRender) {
            DefaultOHLCDataset dset = (DefaultOHLCDataset) d;
            OHLCDataItem[] items = new OHLCDataItem[ohlcVals.size()];
            ohlcVals.toArray(items);
            dset = new DefaultOHLCDataset(sd.getName(), items);
            return dset;
        } else if (isStacked) {
        } else {
            TimeSeries ts = getTimeSeries(sd);
            if (ts != null && ts.getItemCount() == 0) {
                TimeSeriesCollection tsc = (TimeSeriesCollection) d;
                tsc.removeSeries(ts);
            }
        }
        this.stMap = null;
        return d;
    }

    private static final Date NOW = new Date();

    /**
     * get the TimeSeries that matches the current SeriesDescriptor;
     * @return
     */
    private TimeSeries getTimeSeries(SeriesDescriptor currentSD) {
        TimeSeries ret = null;
        if (stMap != null) {
            ret = stMap.get(currentSD);
        }
        return ret;
    }

    private void addTimeSeries(TimeSeries t, SeriesDescriptor currentSD) {
        if (stMap == null) {
            stMap = new HashMap<Object, TimeSeries>();
        }
        stMap.put(currentSD, t);
    }

    /**
     * Get an existing time series based on name - for use only with dynamic time series
     * @param seriesName
     * @return
     */
    private TimeSeries getTimeSeries(String seriesName) {
        return stMap != null ? stMap.get(seriesName) : null;
    }

    private void addTimeSeries(String seriesName, TimeSeries t) {
        if (stMap == null) {
            stMap = new HashMap<Object, TimeSeries>();
        }
        stMap.put(seriesName, t);
    }

    public String populateSingle(Dataset d, SeriesDescriptor currentSD, DataStream rs) throws Exception {
        String ret = null;
        boolean res = false;
        if (bubbleRender) {
            if (res = populateSingleXYZ(d, currentSD, rs)) {
                ret = currentSD.getName();
            }
        } else if (ohlcRender) {
            if (res = populateSingleOHLC(d, currentSD, rs)) {
                ret = currentSD.getName();
            }
        } else if (isStacked) {
            if (res = populateSingleStacked(d, currentSD, rs)) {
                ret = currentSD.getName();
            }
        } else {
            String seriesName = currentSD.getName();
            boolean dynamicSeriesName = false;
            if (currentSD.getSeriesNameFromData() > 0) {
                seriesName = rs.getString(currentSD.getSeriesNameFromData());
                dynamicSeriesName = true;
            }
            int ptype = currentSD.getTimePeriod();
            if (ptype == TIME_UNKNOWN) {
                ptype = chartPeriodType;
            }
            if (seriesName != null) {
                TimeSeriesCollection tsc = (TimeSeriesCollection) d;
                TimeSeries ts;
                boolean createDynamic = true;
                if (!dynamicSeriesName) {
                    ts = getTimeSeries(currentSD);
                } else if (seriesName != null) {
                    ts = getTimeSeries(seriesName);
                    if (ts == null) {
                        if (tsc.getSeriesCount() <= MAX_DYNAMIC_SERIES) {
                            createDynamic = true;
                        } else {
                            createDynamic = false;
                        }
                    }
                } else {
                    ts = null;
                    createDynamic = false;
                }
                if (ts == null && createDynamic) {
                    ts = new TimeSeries(seriesName, makePeriod(ptype, NOW).getClass());
                    tsc.addSeries(ts);
                    if (!dynamicSeriesName) {
                        addTimeSeries(ts, currentSD);
                    } else {
                        addTimeSeries(seriesName, ts);
                    }
                }
                if (ts != null) {
                    Date date = rs.getDate(currentSD.getXColumn());
                    Double y = rs.getDouble(currentSD.getYColumn());
                    if (date != null && y != null) {
                        RegularTimePeriod period = makePeriod(ptype, date);
                        if (period != null) {
                            ts.addOrUpdate(period, y);
                            res = true;
                            ret = seriesName;
                            if (currentSD.getLinkExpression() != null) {
                                String url = ChartContext.translateLinkExpression(rs, rs.getMetadata(), currentSD.getName(), currentSD.getLinkExpression());
                                double x = (double) period.getMiddleMillisecond();
                                recordImgMapUrl(tsc, currentSD.getName(), x, y, url);
                            }
                        } else {
                            log.warn("Failed to make time period for ptype: " + ptype + " date: " + date);
                        }
                    } else {
                    }
                }
            }
        }
        return ret;
    }

    private boolean populateSingleStacked(Dataset d, SeriesDescriptor currentSD, DataStream rs) throws Exception {
        boolean res = false;
        Date date = rs.getDate(currentSD.getXColumn());
        Double y = rs.getDouble(currentSD.getYColumn());
        if (date == null || y == null) {
        } else {
            TimeTableXYDataset ttd = (TimeTableXYDataset) d;
            int ptype = currentSD.getTimePeriod();
            if (ptype == TIME_UNKNOWN) {
                ptype = chartPeriodType;
            }
            RegularTimePeriod period = makePeriod(ptype, date);
            String seriesName = currentSD.getName();
            boolean addToSeries = true;
            if (currentSD.getSeriesNameFromData() > 0) {
                seriesName = rs.getString(currentSD.getSeriesNameFromData());
                if (this.dynamicSeries.get(seriesName) == null) {
                    if (ttd.getSeriesCount() >= MAX_DYNAMIC_SERIES) {
                        addToSeries = false;
                    } else {
                        dynamicSeries.put(seriesName, dynamicSeries.size());
                    }
                }
            }
            if (seriesName != null && addToSeries) {
                ttd.remove(period, seriesName);
                ttd.add(period, y, seriesName);
                String url = ChartContext.translateLinkExpression(rs, rs.getMetadata(), currentSD.getName(), currentSD.getLinkExpression());
                double x = (double) period.getFirstMillisecond();
                recordImgMapUrl(d, seriesName, x, y, url);
            }
            res = true;
        }
        return res;
    }

    private RegularTimePeriod makePeriod(int ptype, Date d) {
        RegularTimePeriod ret = null;
        try {
            switch(ptype) {
                case TIME_MILLI:
                    ret = new Millisecond(d);
                    break;
                case TIME_SECOND:
                    ret = new Second(d);
                    break;
                case TIME_MINUTE:
                    ret = new Minute(d);
                    break;
                case TIME_HOUR:
                    ret = new Hour(d);
                    break;
                case TIME_DAY:
                    ret = new Day(d);
                    break;
                case TIME_WEEK:
                    ret = new Week(d);
                    break;
                case TIME_MONTH:
                    ret = new Month(d);
                    break;
                case TIME_QUARTER:
                    ret = new Quarter(d);
                    break;
                case TIME_YEAR:
                    ret = new Year(d);
                    break;
                default:
                    throw new RuntimeException("periodType '" + ptype + "' invalid");
            }
        } catch (IllegalArgumentException e) {
            log.error("Failed to make periodType: " + ptype + " for date: " + d + " because of " + e);
        }
        return ret;
    }
}
