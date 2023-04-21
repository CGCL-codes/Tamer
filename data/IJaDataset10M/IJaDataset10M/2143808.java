package org.fao.geonet.services.statistics;

import java.awt.Color;
import java.io.File;
import java.util.List;
import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Log;
import org.fao.geonet.constants.Geonet;
import org.jdom.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Service to get the db-stored requests information group by popularity
 * todo: factorize chart code into a factory...
 * todo I18N all strings
 * @author nicolas Ribot
 *
 */
public class GroupsPopularity implements Service {

    /** the SQL query to get results */
    private String query;

    /** should we generate and send tooltips to client (caution, can slow down the process if
	 * dataset is big)
	 */
    private boolean createTooltips;

    /** the imagemap for this chart, allowing to display tooltips */
    private String imageMap;

    /** should we generate and send legend to client (caution, can slow down the process if
	 * dataset is big)
	 */
    private boolean createLegend;

    /** chart width, service parameter, can be overloaded by request */
    private int chartWidth;

    /** chart width, can be overloaded by request */
    private int chartHeight;

    public void init(String appPath, ServiceConfig params) throws Exception {
        this.createLegend = Boolean.parseBoolean(params.getValue("createLegend"));
        this.createTooltips = Boolean.parseBoolean(params.getValue("createTooltips"));
        this.chartWidth = Integer.parseInt(params.getValue("chartWidth"));
        this.chartHeight = Integer.parseInt(params.getValue("chartHeight"));
        this.query = params.getValue("query");
    }

    public Element exec(Element params, ServiceContext context) throws Exception {
        String message = "";
        Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        List l = dbms.select("select sum(popularity) as sumpop from metadata").getChildren();
        if (l.size() != 1) {
            message = "cannot get popularity count";
            return null;
        }
        int cnt = Integer.parseInt(((Element) l.get(0)).getChildText("sumpop"));
        Log.debug(Geonet.SEARCH_LOGGER, "query to get popularity by group:\n" + query);
        dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);
        DefaultPieDataset dataset = new DefaultPieDataset();
        List resultSet = dbms.select(query).getChildren();
        for (int i = 0; i < resultSet.size(); i++) {
            Element record = (Element) resultSet.get(i);
            String popularity = (record).getChildText("popularity");
            Double d = 0.0;
            if (popularity.length() > 0) {
                d = (Double.parseDouble(popularity) / cnt) * 100;
            }
            dataset.setValue(record.getChildText("groupname"), d);
        }
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, true, true, false);
        chart.setBackgroundPaint(Color.decode("#E7EDF5"));
        String chartFilename = "popubygroup_" + System.currentTimeMillis() + ".png";
        File statFolder = new File(context.getAppPath() + File.separator + "images" + File.separator + "statTmp");
        if (!statFolder.exists()) {
            statFolder.mkdirs();
        }
        File f = new File(statFolder, chartFilename);
        this.imageMap = org.fao.geonet.services.statistics.ChartFactory.writeChartImage(chart, f, this.chartWidth, this.chartHeight, this.createTooltips, "graphPopuByGroupImageMap");
        Element elResp = new Element(Jeeves.Elem.RESPONSE);
        Element elchartUrl = new Element("popuByGroupUrl").setText(context.getBaseUrl() + "/images/statTmp/" + chartFilename);
        Element elTooltipImageMap = new Element("tooltipImageMap").addContent(this.createTooltips ? this.imageMap : "");
        Element elMessage = new Element("message").setText(message);
        Element elChartWidth = new Element("chartWidth").setText("" + this.chartWidth);
        Element elChartHeight = new Element("chartHeight").setText("" + this.chartHeight);
        elResp.addContent(elchartUrl);
        elResp.addContent(elTooltipImageMap);
        elResp.addContent(elMessage);
        elResp.addContent(elChartWidth);
        elResp.addContent(elChartHeight);
        return elResp;
    }
}
