package playground.droeder.analysis2;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.jfree.chart.JFreeChart;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.counts.CountSimComparison;
import org.matsim.counts.CountSimComparisonImpl;
import org.matsim.counts.algorithms.graphs.CountsSimRealPerHourGraph;
import playground.droeder.charts.DaChartWriter;

/**
 * @author droeder
 *
 */
public class DrCountsCompareReader {

    private static final Logger log = Logger.getLogger(DrCountsCompareReader.class);

    private String countsCompareFile;

    private List<CountSimComparison> comps;

    public static void main(String[] args) {
        DrCountsCompareReader reader = new DrCountsCompareReader("D:/VSP/projects/sketchPlanning/Berlin/output/ba16_17_storkower/ITERS/it.80/80.countscompare.txt");
        reader.read();
        reader.createSim2CountVolCharts("D:/VSP/projects/sketchPlanning/Berlin/output/ba16_17_storkower/ITERS/it.80/count2SimVol/", 80);
    }

    public DrCountsCompareReader(String countsCompareFile) {
        this.countsCompareFile = countsCompareFile;
        this.comps = new ArrayList<CountSimComparison>();
    }

    public void read() {
        CountSimComparison comp;
        String line;
        String[] entries;
        BufferedReader reader = IOUtils.getBufferedReader(this.countsCompareFile);
        try {
            log.info("start reading countsCompareData...");
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                entries = line.split("\t");
                comp = new CountSimComparisonImpl(new IdImpl(entries[0].trim()), Integer.valueOf(entries[1].trim()), Double.valueOf(entries[3].trim().replace(",", "")), Double.valueOf(entries[2].trim().replace(",", "")));
                this.comps.add(comp);
            }
            log.info("finished...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createSim2CountVolCharts(String outDir, int iter) {
        if (!new File(outDir).exists()) {
            new File(outDir).mkdirs();
            log.info("Output-Directory " + outDir + " not found! Created: " + outDir + " ...");
        }
        CountsSimRealPerHourGraph graph = new CountsSimRealPerHourGraph(this.comps, iter, outDir);
        JFreeChart chart;
        String title;
        for (int i = 1; i < 25; i++) {
            title = String.valueOf(i) + "_compare";
            chart = graph.createChart(i);
            DaChartWriter.saveAsPng(outDir + title + ".png", 800, 600, chart);
        }
        log.info("created charts...");
    }
}
