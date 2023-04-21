package net.sourceforge.olduvai.lrac.dataservice;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.sourceforge.jglchartutil.AbstractDataSet;
import net.sourceforge.jglchartutil.AxisMarker;
import net.sourceforge.jglchartutil.DataSeries2D;
import net.sourceforge.jglchartutil.DefaultDataSet;
import net.sourceforge.jglchartutil.GLChart;
import net.sourceforge.jglchartutil.TimeDataSet;
import net.sourceforge.olduvai.lrac.dataservice.queries.AbstractQuery;
import net.sourceforge.olduvai.lrac.dataservice.queries.CellSwatchQuery;
import net.sourceforge.olduvai.lrac.dataservice.queries.ChartDataAggQuery;
import net.sourceforge.olduvai.lrac.dataservice.queries.ChartDataQuery;
import net.sourceforge.olduvai.lrac.dataservice.queries.SourceGroupQuery;
import net.sourceforge.olduvai.lrac.dataservice.queries.SourceQuery;
import net.sourceforge.olduvai.lrac.dataservice.records.AlarmRecord;
import net.sourceforge.olduvai.lrac.dataservice.records.CellSwatchRecord;
import net.sourceforge.olduvai.lrac.dataservice.records.ChannelValueSet;
import net.sourceforge.olduvai.lrac.dataservice.records.SourceAlarmDataResult;
import net.sourceforge.olduvai.lrac.dataservice.records.SourceChartDataResultSet;
import net.sourceforge.olduvai.lrac.dataservice.records.SourceGroup;
import net.sourceforge.olduvai.lrac.drawer.Source;
import net.sourceforge.olduvai.lrac.drawer.strips.DataAggregator;
import net.sourceforge.olduvai.lrac.drawer.strips.Strip;
import net.sourceforge.olduvai.lrac.util.Util;

public class DataServiceThread extends Thread implements DataInterface {

    static final int SLEEPLENGTH = 300;

    /**
	 * If set to true this thread stops running during its next run cycle
	 */
    private boolean halt = false;

    /**
	 * The reader handles the actual nitty gritty interactions with the server.
	 */
    LRACSwiftReader reader = null;

    /**
	 * The result interface is the visualization interface that accepts data that has
	 * been returned from the server.
	 */
    ResultInterface ri = null;

    /**
	 * This is a thread safe structure for making alarm requests.  These are 
	 * then serviced by the run() method of the thread, which adds the appropriate 
	 * info to the LRAC datastructures
	 * 
	 */
    List<AbstractQuery> requestQueue = Collections.synchronizedList(new ArrayList<AbstractQuery>());

    /**
	 * A tail of two queues: requests are processed from requestQueue, aggregated, and added to the 
	 * aggQueue.  Note: aggQueue is NOT thread safe, it depends on thread safe operations on requestQueue
	 * it should only be accessed from within the dataservicethread.  
	 * 
	 */
    List<AbstractQuery> aggQueue = new ArrayList<AbstractQuery>();

    public DataServiceThread(ResultInterface ri) {
        super("LiveRAC data service thread");
        this.ri = ri;
        reader = new LRACSwiftReader(ri);
    }

    /** 
	 * Sets a flag and interupts to temporarily stop queries from being serviced
	 * Note: flushes all queued queries, and disallows new queries
	 * @see #go()
	 */
    public void halt() {
        halt = true;
        requestQueue.clear();
    }

    /**
	 * Sets a flag and resumes query execution
	 *
	 */
    public void go() {
        requestQueue.clear();
        halt = false;
    }

    public void run() {
        while (true) {
            if (!halt) {
                if (requestQueue.size() != 0) {
                    ri.setLoadingState(true);
                    queueCleaner();
                    Iterator<AbstractQuery> it = aggQueue.iterator();
                    AbstractQuery req = null;
                    while (it.hasNext()) {
                        req = it.next();
                        it.remove();
                        try {
                            serverRequest(req);
                        } catch (IOException e) {
                            System.err.println("Unable to run requests: IOException");
                            e.printStackTrace();
                        }
                    }
                    ri.setLoadingState(false);
                }
            }
            try {
                sleep(SLEEPLENGTH);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
	 * cleans out duplicate entries and builds up 
	 * aggregate requests for charts.  These are added to aggQueue
	 * 
	 * @param rq
	 */
    private void queueCleaner() {
        boolean seenSwatchQuery = false;
        Date startDate = null;
        Date endDate = null;
        ChartDataAggQuery chartAggQuery = null;
        synchronized (requestQueue) {
            AbstractQuery query;
            for (int i = requestQueue.size() - 1; i >= 0; i--) {
                query = requestQueue.get(i);
                if (query instanceof CellSwatchQuery) {
                    if (seenSwatchQuery == false) {
                        aggQueue.add(query);
                        seenSwatchQuery = true;
                    }
                } else if (query instanceof ChartDataQuery) {
                    ChartDataQuery csq = (ChartDataQuery) query;
                    if (startDate == null) startDate = csq.getStartDate();
                    if (endDate == null) endDate = csq.getEndDate();
                    if (startDate == csq.getStartDate() && endDate == csq.getEndDate()) {
                        if (chartAggQuery == null) chartAggQuery = new ChartDataAggQuery(csq.getAggType());
                        chartAggQuery.addDataQuery(csq);
                    }
                }
                requestQueue.remove(i);
            }
        }
        if (chartAggQuery != null) aggQueue.add(chartAggQuery);
    }

    /**
	 * Queue a request for service from the server the next time the 
	 * data service thread runs.  
	 * 
	 * @param o Object An AlarmRequest object.
	 * @return Whether the request was successfully queue'd
	 */
    public boolean requestData(Object o) {
        AbstractQuery request = null;
        try {
            request = (AbstractQuery) o;
        } catch (Exception e) {
            System.out.println("DataServiceThread requestData() Invalid request.");
            return false;
        }
        return requestQueue.add(request);
    }

    /**
	 * Sends the request to the server, calls the LRAC DataStore method to 
	 * add the returned data to the visualization.  
	 * 
	 * @param req Request object
	 */
    private void serverRequest(AbstractQuery req) throws IOException {
        if (req instanceof CellSwatchQuery) {
            ArrayList<CellSwatchRecord> result = reader.getCellSwatchValues((CellSwatchQuery) req);
            if (result != null) ri.queueSwatchResult(result);
        } else if (req instanceof ChartDataAggQuery) {
            ChartDataAggQuery aggquery = (ChartDataAggQuery) req;
            if (aggquery.isAlarmQuery()) {
                Map<String, SourceAlarmDataResult> alarmresult = reader.getAlarmData(aggquery);
                if (alarmresult != null) {
                    HashMap<String, ProcessedChartDataResults> processedAlarmResults = processAlarmData(aggquery, alarmresult);
                    ri.queueChartDataCallBack(processedAlarmResults);
                    ri.queueAlarmDataCallBack(alarmresult);
                }
            }
            if (aggquery.isStatQuery()) {
                TreeMap<String, SourceChartDataResultSet> statresult = reader.getChartData(aggquery);
                if (statresult != null) {
                    HashMap<String, ProcessedChartDataResults> processedResults = processChartData(statresult);
                    ri.queueChartDataCallBack(processedResults);
                }
            }
        }
        return;
    }

    private HashMap<String, ProcessedChartDataResults> processAlarmData(ChartDataAggQuery aggquery, Map<String, SourceAlarmDataResult> alarmresult) {
        HashMap<String, ProcessedChartDataResults> results = new HashMap<String, ProcessedChartDataResults>();
        final Strip alarmStrip = ri.getAlarmStrip();
        final HashMap<String, Source> sourceTable = ri.getSourceTable();
        final double[] queryTimeRange = getQueryTimeStamps(aggquery);
        Iterator<SourceAlarmDataResult> alarmResultIt = alarmresult.values().iterator();
        while (alarmResultIt.hasNext()) {
            final SourceAlarmDataResult sadr = alarmResultIt.next();
            final String sourceName = sadr.getSourceName();
            final Source source = sourceTable.get(sourceName);
            if (source == null) {
                System.err.println("ProcessAlarmData() invalid source: " + sourceName);
                continue;
            }
            List<AlarmRecord> alarms = sadr.getAlarmRecords();
            HashMap<String, TimeValuePair> alarmTimeValues = getAlarmTimeValues(alarms);
            Iterator<String> alarmTimeIterator = alarmTimeValues.keySet().iterator();
            while (alarmTimeIterator.hasNext()) {
                final String channelName = alarmTimeIterator.next();
                final TimeValuePair tvp = alarmTimeValues.get(channelName);
                final double[] alarmTimes = Util.toPrimitiveDouble(tvp.timeStamps);
                final double[] alarmValues = Util.toPrimitiveDouble(tvp.values);
                ChannelValueSet processedCVS = new ChannelValueSet(channelName, "", channelName, ChannelValueSet.ALARMTYPE);
                processedCVS.setTimeStamps(alarmTimes);
                processedCVS.setDataValues(alarmValues);
                processedCVS = checkAggregators(alarmStrip.getDataAggregators(), processedCVS, queryTimeRange);
                ProcessedChartDataResults result = results.get(sourceName);
                if (result == null) {
                    final DefaultDataSet newDataSet = new DefaultDataSet();
                    result = new ProcessedChartDataResults(source, alarmStrip, newDataSet);
                    results.put(sourceName, result);
                }
                AbstractDataSet dataSet = result.getDataSet();
                if (dataSet.getLabelSet(GLChart.X) == null) {
                    dataSet.setLabelSet(GLChart.X, buildCategoricalLabelSet(processedCVS));
                }
                DataSeries2D series = new DataSeries2D(processedCVS.getTimeStamps(), processedCVS.getDataValues());
                series.setUniqueID(processedCVS.getChannelName());
                series.setLabel(processedCVS.getLabel());
                series.freshen();
                result.addSeries(series);
            }
        }
        return results;
    }

    /**
	 * @param data
	 * @return
	 */
    private static final String[] buildCategoricalLabelSet(ChannelValueSet data) {
        double[] timeStamps = data.getTimeStamps();
        String[] result = new String[timeStamps.length];
        SimpleDateFormat sdf = data.getDateFormat();
        for (int i = 0; i < result.length; i++) result[i] = sdf.format(new Date((long) timeStamps[i] * 1000));
        return result;
    }

    private double[] getQueryTimeStamps(ChartDataAggQuery aggquery) {
        double[] result = new double[2];
        result[0] = aggquery.getStartDate().getTime() / 1000;
        result[1] = aggquery.getEndDate().getTime() / 1000;
        return result;
    }

    private HashMap<String, TimeValuePair> getAlarmTimeValues(List<AlarmRecord> alarms) {
        HashMap<String, TimeValuePair> result = new HashMap<String, TimeValuePair>();
        Iterator<AlarmRecord> it = alarms.iterator();
        while (it.hasNext()) {
            final AlarmRecord r = it.next();
            final String channelname = r.getSeverityName();
            TimeValuePair tvp = result.get(channelname);
            if (tvp == null) tvp = new TimeValuePair();
            tvp.timeStamps.add(r.getTimeissued());
            tvp.values.add((double) r.getSeverity());
            result.put(channelname, tvp);
        }
        return result;
    }

    class TimeValuePair {

        ArrayList<Double> timeStamps = new ArrayList<Double>();

        ArrayList<Double> values = new ArrayList<Double>();
    }

    private HashMap<String, ProcessedChartDataResults> processChartData(TreeMap<String, SourceChartDataResultSet> statresult) {
        final HashMap<String, ProcessedChartDataResults> results = new HashMap<String, ProcessedChartDataResults>(statresult.size());
        final HashMap<String, Source> sourceTable = ri.getSourceTable();
        Iterator<SourceChartDataResultSet> sourceIt = statresult.values().iterator();
        while (sourceIt.hasNext()) {
            final SourceChartDataResultSet scdrs = sourceIt.next();
            final String sourceName = scdrs.sourceName;
            final Source source = sourceTable.get(sourceName);
            if (source == null) {
                System.err.println("processChartData: invalid source: " + sourceName);
                continue;
            }
            Iterator<ChannelValueSet> channelIt = scdrs.channels.values().iterator();
            while (channelIt.hasNext()) {
                final ChannelValueSet values = channelIt.next();
                final String channelName = values.getChannelName();
                final List<Strip> channelStripMap = ri.getChannelStripList(channelName);
                if (channelStripMap == null) {
                    System.err.println("No strips for channel: " + channelName);
                    continue;
                }
                Iterator<Strip> stripIt = channelStripMap.iterator();
                while (stripIt.hasNext()) {
                    final Strip strip = stripIt.next();
                    final String key = sourceName + "|" + strip.getStripTitle();
                    ProcessedChartDataResults newResult = results.get(key);
                    if (newResult == null) {
                        final TimeDataSet newDataSet = new TimeDataSet();
                        setAxisMarkers(strip, newDataSet);
                        newResult = new ProcessedChartDataResults(source, strip, newDataSet);
                        results.put(key, newResult);
                    }
                    ChannelValueSet processedCVS = checkAggregators(strip.getDataAggregators(), values);
                    DataSeries2D series = new DataSeries2D(processedCVS.getTimeStamps(), processedCVS.getDataValues());
                    if (processedCVS.getType() == ChannelValueSet.AVGTYPE) {
                        series.setDrawLegend(false);
                        series.setAveraged(true);
                    }
                    series.setUniqueID(processedCVS.getChannelName());
                    series.setUnitType(processedCVS.getUnit());
                    series.setLabel(processedCVS.getLabel() + " [" + processedCVS.getUnit() + "]");
                    series.setPriority(processedCVS.getChannelPriority());
                    series.freshen();
                    newResult.addSeries(series);
                }
            }
        }
        return results;
    }

    private void setAxisMarkers(Strip s, AbstractDataSet dataset) {
        ArrayList<Float> thresholds = s.getThresholds();
        AxisMarker[] markers = new AxisMarker[thresholds.size()];
        Iterator<Float> it = thresholds.iterator();
        int index = 0;
        while (it.hasNext()) {
            final Float f = it.next();
            final Color col = s.getThresholdColor(f);
            AxisMarker m = new AxisMarker(f.doubleValue(), col);
            markers[index] = m;
            index++;
        }
        dataset.setAxisMarkers(GLChart.Y, markers);
    }

    /**
	 * Wrapper for method below.  Extracts the time range straight from the channelvalueset. 
	 * 
	 * @see #checkAggregators(ArrayList, ChannelValueSet, double[])
	 * @param dataAggregators
	 * @param channelData
	 * @return
	 */
    private static final ChannelValueSet checkAggregators(ArrayList<DataAggregator> dataAggregators, ChannelValueSet channelData) {
        double[] channelTimeRange = channelData.getTimeStamps();
        return checkAggregators(dataAggregators, channelData, channelTimeRange);
    }

    /**
	 * 
	 * @param dataAggregators List of aggregators to try to match against the data time range
	 * @param channelData The object containing the actual data values to be aggregated/binned
	 * @param rangeTimeStamps Only the first and last value matter, this is used to determine which aggregator to apply
	 * @return
	 */
    private static final ChannelValueSet checkAggregators(ArrayList<DataAggregator> dataAggregators, ChannelValueSet channelData, double[] rangeTimeStamps) {
        Iterator<DataAggregator> it = dataAggregators.iterator();
        while (it.hasNext()) {
            final DataAggregator da = it.next();
            if (da.isValidForRange(rangeTimeStamps)) {
                double[][] newData = da.applyAggregator(rangeTimeStamps, channelData.getTimeStamps(), channelData.getDataValues());
                ChannelValueSet processedChannelData = new ChannelValueSet(channelData.channelName, channelData.unit, channelData.label, channelData.type);
                processedChannelData.setTimeStamps(newData[DataAggregator.TIMESTAMPS]);
                processedChannelData.setDataValues(newData[DataAggregator.DATAVALUES]);
                processedChannelData.setDateFormat(da.getDf());
                return processedChannelData;
            }
        }
        return channelData;
    }

    public ArrayList<Source> getSourceList(SourceQuery q) throws IOException {
        if (reader == null) return null;
        return reader.getSourceList(q);
    }

    /**
	 * Use the SWIFT reader to retrieve a list of groups.  Although this code is 
	 * on the DataServiceThread object, this is not called by the DataServiceThread
	 * right now, and execution is performed by the LiveRAC thread because synchronous 
	 * results are required for the GUI dialogs.  
	 *  
	 * @param q
	 * @return
	 */
    public ArrayList<SourceGroup> getAvailGroups(SourceGroupQuery q) throws IOException {
        if (reader == null) return null;
        return reader.getSourceGroups(q);
    }
}
