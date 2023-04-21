package jcash.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import javax.swing.JPanel;
import jcash.app.view.PlanListPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;
import org.jfree.date.*;
import org.jfree.data.*;
import org.jfree.data.category.*;
import org.jfree.data.general.*;
import org.jfree.chart.renderer.category.*;

public class PlanChart extends JPanel {

    Log log = LogFactory.getLog(this.getClass().getName());

    TransDataset dataSource = null;

    ChartPanel chartPnl = null;

    JFreeChart chart = null;

    public PlanChart(PlanListPanel lp) {
        super(new BorderLayout());
        dataSource = new TransDataset(lp);
        chart = ChartFactory.createBarChart("", "", "", dataSource, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseToolTipGenerator(new ChartTipGenerator());
        plot.setDrawingSupplier(new CustDrawingSupplier());
        log.debug(plot.getDrawingSupplier().getClass().getName());
        chartPnl = new ChartPanel(chart);
        add(chartPnl, BorderLayout.CENTER);
        setPreferredSize(new Dimension(300, 200));
    }

    public void updateChart() {
        dataSource.loadData();
    }

    class TransDataset extends AbstractDataset implements CategoryDataset {

        PlanListPanel list = null;

        List data = new ArrayList(100);

        List categories = new ArrayList(100);

        SimpleDateFormat dateFmt = new SimpleDateFormat("M/d");

        TransDataset(PlanListPanel lp) {
            list = lp;
            dateFmt.setTimeZone(TimeZone.getDefault());
        }

        public Comparable getColumnKey(int i) {
            return new Integer(0);
        }

        public int getColumnIndex(Comparable cmp) {
            log.debug("TransDataset.getColumnIndex " + cmp.getClass().getName());
            return 0;
        }

        public java.util.List getColumnKeys() {
            return categories;
        }

        public String getSeriesName(int i) {
            return "Balance";
        }

        public java.util.List getCategories() {
            return categories;
        }

        public int getColumnCount() {
            return 1;
        }

        public int getRowCount() {
            return data.size();
        }

        public int getCategoryCount() {
            return this.categories.size();
        }

        public Number getValue(Comparable l, Comparable r) {
            log.debug("l=" + l + "  r=" + r);
            return new Double(0.0);
        }

        public Number getValue(int seriesIndex, Object category) {
            return (Number) data.get(categories.indexOf(category));
        }

        public Comparable getRowKey(int row) {
            return (Comparable) categories.get(row);
        }

        public int getRowIndex(Comparable c) {
            return categories.indexOf(c);
        }

        public java.util.List getRowKeys() {
            return categories;
        }

        public Number getValue(int row, int col) {
            return (Number) data.get(row);
        }

        public int getSeriesCount() {
            return 1;
        }

        public void loadData() {
            SerialDate sDate = SerialDate.createInstance(list.getStart());
            SerialDate endDate = SerialDate.createInstance(list.getEnd());
            categories.clear();
            data.clear();
            BigDecimal dec = new BigDecimal("0.00");
            while (sDate.isOnOrBefore(endDate)) {
                log.debug("add date " + sDate);
                categories.add(dateFmt.format(sDate.toDate()));
                data.add(dec);
                sDate = SerialDate.addDays(1, sDate);
            }
            int rowCnt = list.getEntryCount();
            int index = 0;
            for (int i = 0; i < rowCnt; i++) {
                index = categories.indexOf(dateFmt.format(list.getEntryDate(i)));
                if (index >= 0 && index < categories.size()) {
                    dec = (BigDecimal) data.get(index);
                    data.set(index, dec.add(list.getEntryAmount(i)));
                } else {
                    log.trace("" + index + " " + dateFmt.format(list.getEntryDate(i)));
                }
            }
            dec = list.getBeginningBalance();
            for (int i = 0; i < data.size(); i++) {
                dec = dec.add((BigDecimal) data.get(i));
                data.set(i, dec);
            }
            fireDatasetChanged();
        }
    }

    class ChartTipGenerator extends StandardCategoryToolTipGenerator {

        java.text.NumberFormat localNumberFmt = new java.text.DecimalFormat("0.00");

        ChartTipGenerator() {
            super("", new java.text.DecimalFormat("0.00"));
        }

        public String generateToolTip(CategoryDataset data, int series, int category) {
            String result = null;
            Number value = data.getValue(series, category);
            if (value != null) {
                Object seriesName = data.getRowKey(series);
                if (seriesName != null) {
                    result = seriesName.toString() + " ";
                }
                String valueString = null;
                valueString = localNumberFmt.format(value);
                if (result != null) {
                    result += " = " + valueString;
                } else {
                    result = " = " + valueString;
                }
            }
            return result;
        }
    }

    class CustDrawingSupplier extends DefaultDrawingSupplier {

        CustDrawingSupplier() {
            super(new java.awt.Paint[] { java.awt.Color.BLUE }, DEFAULT_OUTLINE_PAINT_SEQUENCE, DEFAULT_STROKE_SEQUENCE, DEFAULT_OUTLINE_STROKE_SEQUENCE, DEFAULT_SHAPE_SEQUENCE);
        }
    }
}
