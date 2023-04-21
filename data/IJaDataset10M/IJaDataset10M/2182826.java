package vqwiki.plugin.chart;

import com.keypoint.PngEncoder;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultCategoryDataset;
import vqwiki.Environment;
import vqwiki.lex.ExternalLex;
import vqwiki.utils.SystemTime;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;

/**
 * Simple plugin for building charts (graphs).
 * <p/>
 * The expected text format is:
 *
 * <pre>
 * chart-type,title,domain-label,range-label;
 * category1,category2,category3;
 * series1,1,2,3;
 * series2,1,2,3;
 * </pre>
 * <p/>
 * Where chart type is one of "bar". the category names are the second line.
 * Each series is preceded with the series name.
 *
 * @author Gareth Cronin (garethc)
 * @version $Revision: 1215 $ $Date: 2010-05-23 19:59:54 -0400 (Sun, 23 May 2010) $
 * @since 6/06/2004 10:08:05
 */
public class ChartLex implements ExternalLex {

    /**
     * Logger
     */
    public static final Logger logger = Logger.getLogger(ChartLex.class.getName());

    /**
     * Bar chart type
     */
    private static final String TYPE_BAR = "bar";

    /**
     * Pie chart type
     */
    private static final String TYPE_PIE = "pie";

    /**
     * Line chart type
     */
    private static final String TYPE_LINE = "line";

    /**
     * Build a chart and display it from the given text. Generates a png and
     * stores it in the root of the webapp context.
     *
     * TODO A nicer way would be to supply the text to a servlet as a parameter
     * and use the output stream to render the image. This could be done by
     * adding an action to the plugin that does just that and supply a link to
     * the action with the text as a parameter.
     *
     * @param text
     *            text in required format
     * @return link to generated chart
     */
    public synchronized String process(String text) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        StringTokenizer seriesTokenizer = new StringTokenizer(text, ";");
        String type = null;
        String title = null;
        String domainLabel = null;
        String rangeLabel = null;
        String expecting = null;
        int seriesCount = 0;
        try {
            expecting = "header string with type, title, domain label and range label";
            String chartString = seriesTokenizer.nextToken().trim();
            StringTokenizer chartTokenizer = new StringTokenizer(chartString, ",");
            expecting = "chart type";
            type = chartTokenizer.nextToken();
            expecting = "chart title";
            title = chartTokenizer.nextToken();
            expecting = "domain label";
            domainLabel = chartTokenizer.nextToken();
            expecting = "range label";
            rangeLabel = chartTokenizer.nextToken();
            expecting = "category names";
            String categoriesString = seriesTokenizer.nextToken().trim();
            seriesCount = 0;
            while (seriesTokenizer.hasMoreTokens()) {
                seriesCount++;
                expecting = "series";
                String seriesString = seriesTokenizer.nextToken();
                StringTokenizer dataTokenizer = new StringTokenizer(seriesString, ",");
                expecting = "series name";
                String seriesName = dataTokenizer.nextToken().trim();
                StringTokenizer categoryNameTokenizer = new StringTokenizer(categoriesString, ",");
                while (dataTokenizer.hasMoreTokens()) {
                    expecting = "data item";
                    String data = dataTokenizer.nextToken().trim();
                    String categoryName = null;
                    if (categoryNameTokenizer.hasMoreTokens()) {
                        categoryName = categoryNameTokenizer.nextToken().trim();
                    } else {
                        categoryName = "?";
                    }
                    dataset.addValue(new Float(data), seriesName, categoryName);
                }
            }
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return "??? chart data error, expecting:" + expecting + " ???";
        }
        JFreeChart chart;
        if (TYPE_BAR.equals(type)) {
            chart = ChartFactory.createVerticalBarChart(title, domainLabel, rangeLabel, dataset, seriesCount > 1, true, false);
        } else if (TYPE_PIE.equals(type)) {
            chart = ChartFactory.createPieChart(title, dataset, 0, true, false, false);
        } else if (TYPE_LINE.equals(type)) {
            chart = ChartFactory.createLineChart(title, domainLabel, rangeLabel, dataset, seriesCount > 1, true, false);
        } else {
            return "Unknown chart type: " + type;
        }
        File base = new File(Environment.getInstance().getRealPath());
        String filename = "chart" + String.valueOf(SystemTime.asMillis()) + ".png";
        File imageFile = new File(base, filename);
        PngEncoder pngEncoder = new PngEncoder(chart.createBufferedImage(400, 400));
        byte[] bytes = pngEncoder.pngEncode();
        FileOutputStream fileOut = null;
        BufferedOutputStream out = null;
        try {
            fileOut = new FileOutputStream(imageFile);
            out = new BufferedOutputStream(fileOut);
            out.write(bytes);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "error closing streams", e);
            }
        }
        return "<img src='../" + filename + "'/>";
    }
}
