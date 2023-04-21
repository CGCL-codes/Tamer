package org.tolven.web;

import java.awt.Color;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.naming.NamingException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.tolven.app.CCHITLocal;
import org.tolven.app.entity.MenuData;
import org.tolven.core.TolvenRequest;
import org.tolven.growthchart.GrowthChartLocal;
import org.tolven.growthchart.entity.Lenageinf;
import org.tolven.growthchart.entity.Statage;
import org.tolven.growthchart.entity.Wtage;
import org.tolven.growthchart.entity.Wtageinf;
import org.tolven.logging.TolvenLogger;

/**
 * To manage Growth Chart
 * 
 * @author Suja
 * added on 01/20/2011
 */
public class ChartAction extends TolvenAction {

    private String graphURL = "";

    @EJB
    private GrowthChartLocal chartBean;

    @EJB
    private CCHITLocal cchitBean;

    protected GrowthChartLocal getChartBean() {
        if (chartBean == null) {
            try {
                chartBean = (GrowthChartLocal) getContext().lookup("tolven/GrowthChartBean/local");
            } catch (NamingException ex) {
                throw new RuntimeException("Could not lookup tolven/GrowthChartBean/local", ex);
            }
        }
        return chartBean;
    }

    protected CCHITLocal getCchitBean() {
        if (cchitBean == null) {
            try {
                cchitBean = (CCHITLocal) getContext().lookup("tolven/CCHITBean/local");
            } catch (NamingException ex) {
                throw new RuntimeException("Could not lookup tolven/CCHITBean/local", ex);
            }
        }
        return cchitBean;
    }

    /**
	 * This function will return height chart.
	 * 
	 * @author Suja
	 * added on 02/01/2011
	 * @return
	 * @throws IOException
	 */
    public String getGrowthChartHeight() throws IOException {
        JFreeChart chart = createChart(1);
        String filename = ServletUtilities.saveChartAsPNG(chart, 1000, 700, null);
        graphURL = "my.graph?filename=" + URLEncoder.encode(filename, "UTF-8");
        TolvenLogger.info("Graph URL: " + graphURL, MenuAction.class);
        return graphURL;
    }

    /**
	 * This function will return weight chart.
	 * 
	 * @author Suja
	 * added on 02/01/2011
	 * @return
	 * @throws IOException
	 */
    public String getGrowthChartWeight() throws IOException {
        JFreeChart chart = createChart(2);
        String filename = ServletUtilities.saveChartAsPNG(chart, 1000, 700, null);
        graphURL = "my.graph?filename=" + URLEncoder.encode(filename, "UTF-8");
        TolvenLogger.info("Graph URL: " + graphURL, MenuAction.class);
        return graphURL;
    }

    /**
	 * Create dataset for the growth chart
	 * 
	 * @author Suja
	 * added on 02/01/2011
	 * @param type - 1: Height & 2: Weight
	 * @param gender
	 * @param age
	 * @param dob
	 * @return
	 */
    public XYDataset createDataset(int type, int gender, long age, Date dob) {
        try {
            XYSeriesCollection xySeries = new XYSeriesCollection();
            DateFormat dfy = new SimpleDateFormat("yyyy");
            DateFormat dfm = new SimpleDateFormat("MM");
            DateFormat dfcur = new SimpleDateFormat("MMM-dd-yyyy");
            if (type == 1) {
                List<Map<String, Object>> htList = getCchitBean().findAllMenuDataList("echr:patient:observations:active", getRequestParameter("element").toString(), "TestFilter=Height", TolvenRequest.getInstance().getAccountUser());
                if (htList != null) {
                    XYSeries htSeries = new XYSeries("Patient Height");
                    Map<String, Object> totheight = new HashMap<String, Object>();
                    Map<String, Object> cntheight = new HashMap<String, Object>();
                    for (Map<String, Object> ht : htList) {
                        double feet = Double.parseDouble(ht.get("Value").toString().split("'")[0]);
                        double inch = Double.parseDouble(ht.get("Value").toString().split("'")[1].split("\"")[0]);
                        String months = new Integer(months(Integer.parseInt(dfm.format(dfcur.parse(ht.get("Date").toString()))), Integer.parseInt(dfy.format(dfcur.parse(ht.get("Date").toString()))), Integer.parseInt(dfm.format(dob)), Integer.parseInt(dfy.format(dob)))).toString();
                        double cm = 2.54 * (12 * feet + inch);
                        double tot = 0;
                        double totCnt = 0;
                        if (Double.parseDouble(months) <= 240) {
                            if (totheight.containsKey(months)) {
                                tot = Double.parseDouble(totheight.get(months).toString());
                                totheight.remove(months);
                                totCnt = Double.parseDouble(cntheight.get(months).toString());
                                cntheight.remove(months);
                            }
                            totheight.put(months, tot + cm);
                            cntheight.put(months, totCnt + 1);
                        }
                    }
                    for (Map.Entry<String, Object> entry : totheight.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + (Double.parseDouble(entry.getValue().toString()) / Double.parseDouble(cntheight.get(entry.getKey().toString()).toString())));
                        htSeries.add(Double.parseDouble(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()) / Double.parseDouble(cntheight.get(entry.getKey().toString()).toString()));
                    }
                    xySeries.addSeries(htSeries);
                }
            } else {
                List<Map<String, Object>> wtList = getCchitBean().findAllMenuDataList("echr:patient:observations:active", getRequestParameter("element").toString(), "TestFilter=Weight", TolvenRequest.getInstance().getAccountUser());
                if (wtList != null) {
                    XYSeries htSeries = new XYSeries("Patient Weight");
                    Map<String, Object> totweight = new HashMap<String, Object>();
                    Map<String, Object> cntweight = new HashMap<String, Object>();
                    for (Map<String, Object> wt : wtList) {
                        if (wt.get("Value") != null && !wt.get("Value").equals("")) {
                            double kg = Double.parseDouble(wt.get("Value").toString());
                            if (wt.get("Units").toString().equals("lb")) kg = kg * 0.45;
                            String months = new Integer(months(Integer.parseInt(dfm.format(dfcur.parse(wt.get("Date").toString()))), Integer.parseInt(dfy.format(dfcur.parse(wt.get("Date").toString()))), Integer.parseInt(dfm.format(dob)), Integer.parseInt(dfy.format(dob)))).toString();
                            double tot = 0;
                            double totCnt = 0;
                            if (Double.parseDouble(months) <= 240) {
                                if (totweight.containsKey(months)) {
                                    tot = Double.parseDouble(totweight.get(months).toString());
                                    totweight.remove(months);
                                    totCnt = Double.parseDouble(cntweight.get(months).toString());
                                    cntweight.remove(months);
                                }
                                totweight.put(months, tot + kg);
                                cntweight.put(months, totCnt + 1);
                            }
                        }
                    }
                    for (Map.Entry<String, Object> entry : totweight.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + (Double.parseDouble(entry.getValue().toString()) / Double.parseDouble(cntweight.get(entry.getKey().toString()).toString())));
                        htSeries.add(Double.parseDouble(entry.getKey().toString()), Double.parseDouble(entry.getValue().toString()) / Double.parseDouble(cntweight.get(entry.getKey().toString()).toString()));
                    }
                    xySeries.addSeries(htSeries);
                }
            }
            List<Lenageinf> lenageinfs = new ArrayList<Lenageinf>();
            List<Statage> statages = new ArrayList<Statage>();
            List<Wtageinf> wtageInfs = new ArrayList<Wtageinf>();
            List<Wtage> wtages = new ArrayList<Wtage>();
            XYSeries p3 = new XYSeries("p3");
            XYSeries p5 = new XYSeries("p5");
            XYSeries p10 = new XYSeries("p10");
            XYSeries p25 = new XYSeries("p25");
            XYSeries p50 = new XYSeries("p50");
            XYSeries p75 = new XYSeries("p75");
            XYSeries p90 = new XYSeries("p90");
            XYSeries p95 = new XYSeries("p95");
            XYSeries p97 = new XYSeries("p97");
            if (type == 1 && age < 3) {
                lenageinfs = getChartBean().findLenageinfData(gender);
                if (lenageinfs != null) {
                    for (Lenageinf lenageinf : lenageinfs) {
                        p3.add(lenageinf.getAgemonth(), lenageinf.getP3());
                        p5.add(lenageinf.getAgemonth(), lenageinf.getP5());
                        p10.add(lenageinf.getAgemonth(), lenageinf.getP10());
                        p25.add(lenageinf.getAgemonth(), lenageinf.getP25());
                        p50.add(lenageinf.getAgemonth(), lenageinf.getP50());
                        p75.add(lenageinf.getAgemonth(), lenageinf.getP75());
                        p90.add(lenageinf.getAgemonth(), lenageinf.getP90());
                        p95.add(lenageinf.getAgemonth(), lenageinf.getP95());
                        p97.add(lenageinf.getAgemonth(), lenageinf.getP97());
                    }
                }
            } else if (type == 1 && age >= 3) {
                statages = getChartBean().findStatageData(gender);
                if (statages != null) {
                    for (Statage statage : statages) {
                        p3.add(statage.getAgemonth(), statage.getP3());
                        p5.add(statage.getAgemonth(), statage.getP5());
                        p10.add(statage.getAgemonth(), statage.getP10());
                        p25.add(statage.getAgemonth(), statage.getP25());
                        p50.add(statage.getAgemonth(), statage.getP50());
                        p75.add(statage.getAgemonth(), statage.getP75());
                        p90.add(statage.getAgemonth(), statage.getP90());
                        p95.add(statage.getAgemonth(), statage.getP95());
                        p97.add(statage.getAgemonth(), statage.getP97());
                    }
                }
            } else if (type == 2 && age < 3) {
                wtageInfs = getChartBean().findWtageinfData(gender);
                if (wtageInfs != null) {
                    for (Wtageinf wtageinf : wtageInfs) {
                        p3.add(wtageinf.getAgemonth(), wtageinf.getP3());
                        p5.add(wtageinf.getAgemonth(), wtageinf.getP5());
                        p10.add(wtageinf.getAgemonth(), wtageinf.getP10());
                        p25.add(wtageinf.getAgemonth(), wtageinf.getP25());
                        p50.add(wtageinf.getAgemonth(), wtageinf.getP50());
                        p75.add(wtageinf.getAgemonth(), wtageinf.getP75());
                        p90.add(wtageinf.getAgemonth(), wtageinf.getP90());
                        p95.add(wtageinf.getAgemonth(), wtageinf.getP95());
                        p97.add(wtageinf.getAgemonth(), wtageinf.getP97());
                    }
                }
            } else if (type == 2 && age >= 3) {
                wtages = getChartBean().findWtageData(gender);
                if (wtages != null) {
                    for (Wtage wtage : wtages) {
                        p3.add(wtage.getAgemonth(), wtage.getP3());
                        p5.add(wtage.getAgemonth(), wtage.getP5());
                        p10.add(wtage.getAgemonth(), wtage.getP10());
                        p25.add(wtage.getAgemonth(), wtage.getP25());
                        p50.add(wtage.getAgemonth(), wtage.getP50());
                        p75.add(wtage.getAgemonth(), wtage.getP75());
                        p90.add(wtage.getAgemonth(), wtage.getP90());
                        p95.add(wtage.getAgemonth(), wtage.getP95());
                        p97.add(wtage.getAgemonth(), wtage.getP97());
                    }
                }
            }
            xySeries.addSeries(p3);
            xySeries.addSeries(p5);
            xySeries.addSeries(p10);
            xySeries.addSeries(p25);
            xySeries.addSeries(p50);
            xySeries.addSeries(p75);
            xySeries.addSeries(p90);
            xySeries.addSeries(p95);
            xySeries.addSeries(p97);
            TolvenLogger.info("Done preparing Dataset", ChartAction.class);
            return xySeries;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Create Growth Chart
	 * 
	 * @author Suja
	 * added on 02/01/2011
	 * @param type - 1: Height & 2: Weight
	 * @return
	 */
    public JFreeChart createChart(int type) {
        long patientId = Long.parseLong(getRequestParameter("element").toString().split(":")[1].split("-")[1]);
        MenuData patMD = getMenuLocal().findMenuDataItem(patientId);
        int age = 0;
        int gender = 1;
        Date dob = null;
        if (patMD != null) {
            DateFormat df = new SimpleDateFormat("yyyy");
            dob = patMD.getDate01();
            Date cur = new Date();
            age = Integer.parseInt(df.format(cur)) - Integer.parseInt(df.format(dob));
            if (patMD.getString04().equals("Male")) gender = 1; else gender = 2;
        }
        XYDataset dataset = createDataset(type, gender, age, dob);
        String title = "";
        if (type == 1 && age < 3) title = "Height birth to 36 Months Old " + (gender == 1 ? "Male" : "Female"); else if (type == 1 && age >= 3) title = "Height 2-20 Year Old " + (gender == 1 ? "Male" : "Female"); else if (type == 2 && age < 3) title = "Weight birth to 36 Months Old " + (gender == 1 ? "Male" : "Female"); else if (type == 2 && age >= 3) title = "Weight 2-20 Year Old " + (gender == 1 ? "Male" : "Female");
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "X-Value", "Y-Value", dataset, true, true, false);
        chart.setBackgroundPaint(Color.lightGray);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(0.0, 0.0, 0.0, 0.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 1; i < 10; i++) {
            renderer.setSeriesLinesVisible(i, true);
            renderer.setSeriesShapesVisible(i, false);
        }
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);
        plot.setDomainAxis(new NumberAxis("Age (Months)"));
        plot.setRangeAxis(new NumberAxis((type == 1 ? "Height (Centimeters)" : "Weight (Kilograms)")));
        return chart;
    }

    /**
	 * This function calculates the month difference
	 * 
	 * @param month1
	 * @param year1
	 * @param month2
	 * @param year2
	 * @return
	 */
    public static int months(int month1, int year1, int month2, int year2) {
        if (year1 > year2) {
            return months(month2, year2, month1, year1);
        } else if (year1 == year2) {
            return Math.max(month1, month2) + 1;
        }
        return (12 - month1) + months(1, year1 + 1, month2, year2);
    }
}
