package playground.yu.utils.charts.txt2chart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.matsim.core.utils.charts.XYLineChart;
import playground.yu.utils.container.CollectionMath;
import playground.yu.utils.io.SimpleReader;
import playground.yu.utils.io.SimpleWriter;

/**
 * @author yu
 *
 */
public class ModalSplitLogExtractor {

    private final List<Double> carFracs = new ArrayList<Double>();

    private final List<Double> ptFracs = new ArrayList<Double>();

    private final List<Double> wlkFracs = new ArrayList<Double>();

    private final List<Double> bikeFracs = new ArrayList<Double>();

    private final List<Double> undefinedFracs = new ArrayList<Double>();

    private final int maxIter;

    public int getMaxIter() {
        return maxIter;
    }

    public ModalSplitLogExtractor(final int n) {
        maxIter = n;
        for (int i = 0; i < n; i++) {
            carFracs.add(i, Double.valueOf(0));
            ptFracs.add(i, Double.valueOf(0));
            wlkFracs.add(i, Double.valueOf(0));
            bikeFracs.add(i, Double.valueOf(0));
            undefinedFracs.add(i, Double.valueOf(0));
        }
    }

    public void addCar(final int idx, final String carFrac) {
        carFracs.set(idx, Double.valueOf(carFrac));
    }

    public void addPt(final int idx, final String ptFrac) {
        ptFracs.set(idx, Double.valueOf(ptFrac));
    }

    public void addWalk(final int idx, final String walkFrac) {
        wlkFracs.set(idx, Double.valueOf(walkFrac));
    }

    public void addBike(final int idx, final String bikeFrac) {
        bikeFracs.set(idx, Double.valueOf(bikeFrac));
    }

    public void addUndefined(final int idx, final String undefinedFrac) {
        undefinedFracs.set(idx, Double.valueOf(undefinedFrac));
    }

    private static String extractFrac(final String line) {
        String[] words = line.split("\t");
        String frac = words[words.length - 1];
        return frac.substring(0, frac.length() - 1);
    }

    /**
	 * @param line
	 *            , which must contains "ITERATION " and " BEGINS"
	 * @return ITERATION-No.
	 */
    private static int getCount(final String line) {
        String[] words = line.split(" ");
        return Integer.parseInt(words[words.length - 2]);
    }

    private static void readLog(final String logFilename, final ModalSplitLogExtractor msle) {
        if (!new File(logFilename).exists()) {
            throw new RuntimeException(logFilename + "\tdoes NOT exist!!!");
        }
        SimpleReader sr = new SimpleReader(logFilename);
        String line = sr.readLine();
        int count = -1;
        while (line != null) {
            line = sr.readLine();
            if (line != null) {
                if (line.contains("ITERATION ") && line.contains(" BEGINS")) {
                    count = getCount(line);
                } else if (line.contains("car legs")) {
                    if (count > -1) {
                        msle.addCar(count, extractFrac(line));
                    }
                } else if (line.contains("pt legs")) {
                    if (count > -1) {
                        msle.addPt(count, extractFrac(line));
                    }
                } else if (line.contains("walk legs")) {
                    if (count > -1) {
                        msle.addWalk(count, extractFrac(line));
                    }
                } else if (line.contains("bike legs")) {
                    if (count > -1) {
                        msle.addBike(count, extractFrac(line));
                    }
                } else if (line.contains("undefined legs")) {
                    if (count > -1) {
                        msle.addUndefined(count, extractFrac(line));
                    }
                } else if (line.contains("ITERATION ") && line.contains(" ENDS")) {
                    count = -1;
                }
            }
        }
        sr.close();
    }

    private static void writeMode(final String chartFilename, final String outputFilename, final ModalSplitLogExtractor msle) {
        int maxIter = msle.getMaxIter();
        double xs[] = new double[maxIter];
        double carFracs[] = new double[maxIter];
        double ptFracs[] = new double[maxIter];
        double wlkFracs[] = new double[maxIter];
        double bikeFracs[] = new double[maxIter];
        double undefinedFracs[] = new double[maxIter];
        for (int i = 0; i < maxIter; i++) {
            xs[i] = i;
            Double carFrac = i < msle.carFracs.size() ? msle.carFracs.get(i) : null;
            if (carFrac != null) {
                carFracs[i] = carFrac;
            }
            Double ptFrac = i < msle.ptFracs.size() ? msle.ptFracs.get(i) : null;
            if (ptFrac != null) {
                ptFracs[i] = ptFrac;
            }
            Double wlkFrac = i < msle.wlkFracs.size() ? msle.wlkFracs.get(i) : null;
            if (wlkFrac != null) {
                wlkFracs[i] = wlkFrac;
            }
            Double bikeFrac = i < msle.bikeFracs.size() ? msle.bikeFracs.get(i) : null;
            if (bikeFrac != null) {
                bikeFracs[i] = bikeFrac;
            }
            Double undefinedFrac = i < msle.undefinedFracs.size() ? msle.undefinedFracs.get(i) : null;
            if (undefinedFrac != null) {
                undefinedFracs[i] = undefinedFrac;
            }
        }
        XYLineChart chart = new XYLineChart("Mode Choice", "iteration", "leg mode fraction [%]");
        if (CollectionMath.getSum(carFracs) > 0) {
            chart.addSeries("car", xs, carFracs);
        }
        if (CollectionMath.getSum(ptFracs) > 0) {
            chart.addSeries("pt", xs, ptFracs);
        }
        if (CollectionMath.getSum(wlkFracs) > 0) {
            chart.addSeries("walk", xs, wlkFracs);
        }
        if (CollectionMath.getSum(bikeFracs) > 0) {
            chart.addSeries("bike", xs, bikeFracs);
        }
        if (CollectionMath.getSum(undefinedFracs) > 0) {
            chart.addSeries("others", xs, undefinedFracs);
        }
        chart.saveAsPng(chartFilename, 800, 600);
        SimpleWriter sw = new SimpleWriter(outputFilename);
        sw.writeln("iteration\tcar [%]\tpt [%]\twalk [%]\tbike [%]\tundefined [%]");
        System.out.println("n=" + maxIter);
        for (int i = 0; i < maxIter; i++) {
            sw.writeln(i + "\t" + carFracs[i] + "\t" + ptFracs[i] + "\t" + wlkFracs[i] + "\t" + bikeFracs[i] + "\t" + undefinedFracs[i]);
            sw.flush();
        }
        sw.close();
    }

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        String filenameBase = "../../runs-svn/run1536/1536.";
        String logFilename = filenameBase + "logfile.log.gz";
        String chartFilename = filenameBase + "legModeChart.png";
        String outputFilename = filenameBase + "legMode.txt";
        int maxIter = 1001;
        ModalSplitLogExtractor msle = new ModalSplitLogExtractor(maxIter);
        readLog(logFilename, msle);
        writeMode(chartFilename, outputFilename, msle);
    }
}
