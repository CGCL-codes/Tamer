package org.easybi.chart.impl.fusioncharts.spring;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easybi.chart.core.ChartContext;
import org.easybi.chart.core.ChartSupport;
import org.easybi.data.structure.TabularData;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

/**
 * @author yunjian.liu
 * 
 * Jul 28, 2008 10:49:58 AM
 */
public abstract class FusionFormat implements ChartSupport {

    protected Log logger = LogFactory.getLog(FusionFormat.class);

    Document doc = null;

    ChartContext chartContext = new FusionChartContext();

    protected Map<String, String> props = null;

    protected Map<String, Map<String, String>> trendLines = new HashMap<String, Map<String, String>>();

    protected Map<String, Map<String, String>> definitions = new HashMap<String, Map<String, String>>();

    protected Map<String, String> applications = new HashMap<String, String>();

    private Map<String, Map<String, Object>> datasets = new HashMap<String, Map<String, Object>>();

    private static final String indexPrefix = "set";

    private int setIndex = 0;

    public Map<String, Map<String, Object>> getDatasets() {
        return datasets;
    }

    public void setDatasets(Map<String, Map<String, Object>> datasets) {
        this.datasets = datasets;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public Map<String, Map<String, String>> getTrendLines() {
        return trendLines;
    }

    public void setTrendLines(Map<String, Map<String, String>> trendLines) {
        this.trendLines = trendLines;
    }

    public Map<String, Map<String, String>> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Map<String, Map<String, String>> definitions) {
        this.definitions = definitions;
    }

    public Map<String, String> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, String> applications) {
        this.applications = applications;
    }

    public void buildChart() {
        Element root = new Element("chart");
        doc = new Document(root);
        buildChartProps(root);
        TabularData data = this.getChartCx().getData();
        buildChartSet(root, data);
        buildChartTrendLines(root);
        buildChartSytle(root);
    }

    public ChartContext getChartCx() {
        return this.chartContext;
    }

    public String runChart(String chartFullpath) {
        try {
            XMLOutputter xmlOut = new XMLOutputter();
            FileOutputStream out = new FileOutputStream(chartFullpath);
            xmlOut.output(doc, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * ��Բ�ͬ��ͼ����װ��ͬ����ݣ���Ҫ�Ƿ�Ϊdataset��set
	 * @param root
	 * @param tabularData
	 */
    protected abstract void buildChartSet(Element root, TabularData tabularData);

    protected void buildsets(Element root, TabularData tabularData) {
        Entry entry1 = null;
        String seriesName = null;
        Map setMap = null;
        for (Iterator iterator = this.getDatasets().entrySet().iterator(); iterator.hasNext(); ) {
            entry1 = (Entry) iterator.next();
            seriesName = (String) entry1.getKey();
            setMap = (Map) entry1.getValue();
        }
        Map<String, TabularData> rowMap1 = tabularData.getRow(seriesName);
        if (rowMap1 == null || rowMap1.size() <= 0) {
            logger.error("rowMap's size is 0");
            return;
        }
        Entry entry = null;
        Element set = null;
        for (Iterator iterator = rowMap1.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Entry) iterator.next();
            String key = (String) entry.getKey();
            TabularData td = (TabularData) entry.getValue();
            String tData = String.valueOf(td.getValue());
            set = new Element("set");
            set.setAttribute("label", key);
            set.setAttribute("value", tData);
            buildDatasetProps(set, key, setMap);
            root.addContent(set);
            root.addContent("\n\t");
        }
    }

    private void buildDatasetProps(Element el, String serieskey, Map setMap) {
        Map<String, String> dataProps;
        if (setMap.containsKey(serieskey)) {
            setIndex++;
            dataProps = (Map<String, String>) setMap.get(serieskey);
        } else if (setMap.containsKey(indexPrefix + setIndex)) {
            dataProps = (Map<String, String>) setMap.get(indexPrefix + setIndex);
            setIndex++;
        } else {
            return;
        }
        if (dataProps != null && dataProps.size() > 0) {
            Entry entry = null;
            for (Iterator iterator = dataProps.entrySet().iterator(); iterator.hasNext(); ) {
                entry = (Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                el.setAttribute(key, value);
            }
        }
    }

    /**
	 * ����chart���������ԣ����뵽chart Element��
	 * @param root
	 */
    private void buildChartProps(Element root) {
        Entry entry = null;
        for (Iterator iterator = props.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            root.setAttribute(key, value);
        }
        root.addContent("\n\t");
    }

    private void buildChartTrendLines(Element root) {
        Element el = new Element("trendlines");
        el.addContent("\n\t");
        if (trendLines != null && trendLines.size() > 0) {
            Entry entry = null;
            for (Iterator iterator = trendLines.entrySet().iterator(); iterator.hasNext(); ) {
                entry = (Entry) iterator.next();
                Map propsMap = (Map) entry.getValue();
                Element sytle = new Element("line");
                for (Iterator it = propsMap.entrySet().iterator(); it.hasNext(); ) {
                    Entry entry2 = (Entry) it.next();
                    sytle.setAttribute(String.valueOf(entry2.getKey()), String.valueOf(entry2.getValue()));
                }
                el.addContent(sytle);
                el.addContent("\n\t");
            }
        }
        root.addContent(el);
        root.addContent("\n\t");
    }

    /**
	 * ����chart����ʽ����ʽ��Ϊ���������
	 * @param root
	 */
    private void buildChartSytle(Element root) {
        Element el = new Element("styles");
        el.addContent("\n\t");
        Element definition = new Element("definition");
        definition.addContent("\n\t");
        if (definitions != null && definitions.size() > 0) {
            Entry entry = null;
            for (Iterator iterator = definitions.entrySet().iterator(); iterator.hasNext(); ) {
                entry = (Entry) iterator.next();
                Map propsMap = (Map) entry.getValue();
                Element sytle = new Element("style");
                for (Iterator it = propsMap.entrySet().iterator(); it.hasNext(); ) {
                    Entry entry2 = (Entry) it.next();
                    sytle.setAttribute(String.valueOf(entry2.getKey()), String.valueOf(entry2.getValue()));
                }
                definition.addContent(sytle);
                definition.addContent("\n\t");
            }
        }
        Element application = new Element("application");
        application.addContent("\n\t");
        if (applications != null && applications.size() > 0) {
            Entry entry = null;
            for (Iterator iterator = applications.entrySet().iterator(); iterator.hasNext(); ) {
                entry = (Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                Element apply = new Element("apply");
                ;
                apply.setAttribute("toObject", key);
                apply.setAttribute("styles", value);
                application.addContent(apply);
                application.addContent("\n\t");
            }
        }
        el.addContent(definition);
        el.addContent("\n\t");
        el.addContent(application);
        el.addContent("\n\t");
        root.addContent(el);
        root.addContent("\n\t");
    }
}
