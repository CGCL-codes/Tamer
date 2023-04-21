package net.taylor.event.entity.editor;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Iterator;
import javax.persistence.EntityManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

public class DayChart {

    private EntityManager entityManager;

    private TimeSeriesCollection dataset;

    public DayChart(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
        dataset = new TimeSeriesCollection();
        Iterator results = this.entityManager.createQuery("select e.timestamp, e.source, e.name, count(e) from Event e group by e.timestamp, e.source, e.name").getResultList().iterator();
        while (results.hasNext()) {
            Data row = new Data(results.next());
            TimeSeries ts = dataset.getSeries(row.getName());
            if (ts == null) {
                ts = new TimeSeries(row.getName(), Day.class);
                dataset.addSeries(ts);
            }
            TimeSeriesDataItem item = ts.getDataItem(row.getDay());
            if (item == null) {
                ts.add(row.getDay(), row.getNumber());
            } else {
                long before = item.getValue().longValue();
                item.setValue(item.getValue().longValue() + row.getNumber().longValue());
            }
        }
    }

    public TimeSeriesCollection getDataset() {
        return dataset;
    }

    public BufferedImage getImage(int width, int height) {
        JFreeChart chart = ChartFactory.createXYBarChart("", "", true, "", getDataset(), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);
        chart.setBorderVisible(false);
        return chart.createBufferedImage(width, height);
    }

    public byte[] getData(Long width, Long height) {
        try {
            return ChartUtilities.encodeAsPNG(getImage(width.intValue(), height.intValue()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public class Data {

        private Day day;

        private String src;

        private String name;

        private Number number;

        public Data(Object o) {
            Object[] row = (Object[]) o;
            this.day = new Day((Date) row[0]);
            this.src = (String) row[1];
            this.name = (String) row[2];
            this.number = (Number) row[3];
        }

        public String getName() {
            return src + ":" + name;
        }

        public Day getDay() {
            return day;
        }

        public Number getNumber() {
            return number;
        }
    }
}
