package playground.tnicolai.matsim4opus.utils.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.matsim.core.utils.charts.XYLineChart;
import org.matsim.core.utils.io.IOUtils;
import playground.tnicolai.matsim4opus.constants.Constants;
import playground.tnicolai.matsim4opus.utils.io.HeaderParser;
import playground.tnicolai.matsim4opus.utils.io.filter.TabFilter;

/**
 * @author thomas
 *
 */
public class Convert4Gnuplot {

    private static String source1;

    private static File[] fileList1;

    private static String source2;

    private static File[] fileList2;

    private static int zone_id;

    private static int zone_id_tmp;

    private static boolean isSingleDataSet = true;

    private static HeaderObject ho1 = null;

    private static HeaderObject ho2 = null;

    /**
	 * starting point
	 * @param args
	 */
    public static void main(String args[]) {
        try {
            System.out.println("Starting Convert4Gnuplot");
            String[] args1 = new String[] { "/Users/thomas/Development/opus_home/vsp_configs/re-estimate_travel_data_related_models/create_plots/highway_low_cap_accessibility_indicators", "908" };
            String[] args2 = new String[] { "/Users/thomas/Development/opus_home/vsp_configs/re-estimate_travel_data_related_models/create_plots/highway_accessibility_indicators", "908" };
            String[] args3 = new String[] { "/Users/thomas/Development/opus_home/vsp_configs/re-estimate_travel_data_related_models/create_plots/ferry_accessibility_indicators", "908" };
            ArrayList<String[]> tasks = new ArrayList<String[]>();
            tasks.add(args1);
            tasks.add(args2);
            tasks.add(args3);
            for (int i = 0; i < tasks.size(); i++) {
                init(tasks.get(i));
                System.out.println("Starting queue process ...");
                if (isSingleDataSet) queueSingleDataSet(); else queueMultipleDataSets();
            }
            System.out.println("Finished!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * init variables
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private static void init(String args[]) throws FileNotFoundException, IOException {
        System.out.println("Init program ...");
        source1 = args[0];
        System.out.println("Set working directory to: " + source1);
        File folder = new File(source1);
        fileList1 = folder.listFiles(new TabFilter());
        if (args.length == 3) {
            System.out.println("Detected multiple data set");
            isSingleDataSet = false;
            source2 = args[1];
            System.out.println("Set second  working directory to: " + source2);
            folder = new File(source2);
            fileList2 = folder.listFiles(new TabFilter());
            if (!dataSetsAreOk()) System.exit(-1);
            zone_id = Integer.parseInt(args[2]);
        } else zone_id = Integer.parseInt(args[1]);
        zone_id_tmp = zone_id;
        System.out.println("... finished init.");
    }

    /**
	 * checks the correct number of data sets in each folder (number of data sets should be equal) and
	 * the data set names (every data set needs to be in both folders).
	 * 
	 * @return boolean
	 */
    private static boolean dataSetsAreOk() {
        if (fileList1 != null && fileList2 != null && (fileList1.length == fileList2.length)) {
            for (File file1 : fileList1) {
                String fileName1 = file1.getName();
                boolean foundRelatedFile = false;
                for (File file2 : fileList2) {
                    String fileName2 = file2.getName();
                    if (fileName1.equalsIgnoreCase(fileName2)) {
                        foundRelatedFile = true;
                        break;
                    }
                }
                if (!foundRelatedFile) {
                    System.err.println("Didn't found related file for : " + fileName1);
                    return false;
                }
            }
            return true;
        }
        System.err.println("Number of datasets not equal or fileList variables is null.");
        return false;
    }

    /**
	 * create JFreeChart
	 * @param filename
	 * @param title
	 * @param xAxisLabel
	 * @param yAxisLable
	 * @param series
	 * @param xAxis
	 * @param yAxis
	 */
    public static void writeChartSingleDataSet(String filename, String title, String xAxisLabel, String yAxisLable, String series, double[] xAxis, double[] yAxis) {
        System.out.println("Writing chart: " + filename);
        XYLineChart chart = new XYLineChart(title, xAxisLabel, yAxisLable);
        chart.addSeries(series, xAxis, yAxis);
        chart.saveAsPng(filename, 1920, 1080);
        System.out.println("... finished writing chart.");
    }

    public static void writeChartMultipleDataSet(String filename1, String filename2, String title, String xAxisLabel, String yAxisLable, String series1, String series2, double[] xAxis1, double[] xAxis2, double[] yAxis1, double[] yAxis2) {
        System.out.println("Writing chart to: ");
        System.out.println(filename1);
        System.out.println(filename2);
        XYLineChart chart = new XYLineChart(title, xAxisLabel, yAxisLable);
        chart.addSeries(series1, xAxis1, yAxis1);
        chart.addSeries(series2, xAxis2, yAxis2);
        chart.saveAsPng(filename1, 1920, 1080);
        chart.saveAsPng(filename2, 1920, 1080);
        System.out.println("... finished writing chart.");
    }

    /**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private static void queueSingleDataSet() throws FileNotFoundException, IOException {
        String line;
        int id;
        String[] parts = null;
        String title, xAxisLabel, yAxisLable, series;
        double xAxis[];
        double yAxis[];
        for (File file : fileList1) {
            String source = file.getCanonicalPath();
            String destinationPNG = source.replace(".tab", ".png");
            String destinationDAT = source.replace(".tab", ".dat");
            System.out.println("Processing : " + source);
            checkAndRestoreZoneID(source);
            BufferedReader br = IOUtils.getBufferedReader(source);
            BufferedWriter bw = IOUtils.getBufferedWriter(destinationDAT);
            StringBuffer content = new StringBuffer("");
            line = br.readLine();
            parts = line.split(Constants.TAB);
            ho1 = initHeaderObject(line);
            xAxisLabel = "years";
            yAxisLable = getYAxisLabel(parts);
            series = yAxisLable;
            title = yAxisLable + " in zone " + zone_id;
            xAxis = ho1.getSortedHeaderAsDouble();
            yAxis = new double[xAxis.length];
            while ((line = br.readLine()) != null) {
                parts = line.split("\t");
                id = Integer.parseInt(parts[ho1.getZoneId()]);
                if (id == zone_id) {
                    System.out.print("");
                    System.out.println("Found zone : " + id);
                    for (int i = 1; i < parts.length; i++) {
                        yAxis[i - 1] = Double.parseDouble(parts[ho1.getIndexOf(i)]);
                        content.append(((int) xAxis[i - 1]) + Constants.TAB + yAxis[i - 1] + Constants.NEW_LINE);
                    }
                    break;
                }
            }
            System.out.println("Writing gnuplot dat: " + destinationDAT);
            bw.write(content.toString());
            bw.flush();
            bw.close();
            System.out.println("Finished writing gnuplot dat");
        }
    }

    /**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    private static void queueMultipleDataSets() throws FileNotFoundException, IOException {
        String lineSource1, lineSource2;
        int id1, id2;
        String[] parts1, parts2;
        String title, xAxisLabel, yAxisLable, series1, series2;
        double xAxis1[];
        double xAxis2[];
        double yAxis1[];
        double yAxis2[];
        for (int i = 0; i < fileList1.length; i++) {
            String source1 = fileList1[i].getCanonicalPath();
            String source2 = fileList2[i].getCanonicalPath();
            String destination1PNG = source1.replace(".tab", ".png");
            String destination1DAT = source1.replace(".tab", ".dat");
            String destination2PNG = source2.replace(".tab", ".png");
            String destination2DAT = source2.replace(".tab", ".dat");
            System.out.println("Processing : " + source1 + " and " + source2);
            checkAndRestoreZoneID(source1);
            BufferedReader br1 = IOUtils.getBufferedReader(source1);
            BufferedReader br2 = IOUtils.getBufferedReader(source2);
            BufferedWriter bw1 = IOUtils.getBufferedWriter(destination1DAT);
            BufferedWriter bw2 = IOUtils.getBufferedWriter(destination2DAT);
            StringBuffer content1 = new StringBuffer("");
            StringBuffer content2 = new StringBuffer("");
            lineSource1 = br1.readLine();
            lineSource2 = br2.readLine();
            if (!lineSource1.equalsIgnoreCase(lineSource2)) System.err.println("Different headers in " + source1 + " and " + source2);
            parts1 = lineSource1.split(Constants.TAB);
            parts2 = lineSource2.split(Constants.TAB);
            ho1 = initHeaderObject(lineSource1);
            ho2 = initHeaderObject(lineSource2);
            xAxisLabel = "years";
            yAxisLable = getYAxisLabel(parts1);
            series1 = "Ferry Scenario";
            series2 = "Highway Scenario";
            title = yAxisLable + " in zone " + zone_id;
            xAxis1 = ho1.getSortedHeaderAsDouble();
            xAxis2 = ho2.getSortedHeaderAsDouble();
            yAxis1 = new double[xAxis1.length];
            yAxis2 = new double[xAxis2.length];
            boolean foundZoneID1 = false;
            boolean foundZoneID2 = false;
            while (!(foundZoneID1 && foundZoneID2)) {
                lineSource1 = br1.readLine();
                lineSource2 = br2.readLine();
                if (lineSource1 == null || lineSource2 == null) {
                    System.err.println("Zone " + zone_id + " not found in " + fileList1[i].getName());
                    break;
                }
                parts1 = lineSource1.split(Constants.TAB);
                parts2 = lineSource2.split(Constants.TAB);
                if (parts1.length != parts2.length) System.err.println("Number of columns differ in " + source1 + " and " + source2 + ".");
                id1 = Integer.parseInt(parts1[ho1.getZoneId()]);
                id2 = Integer.parseInt(parts2[ho2.getZoneId()]);
                if ((id1 == zone_id)) {
                    System.out.println("Found zone in source dir 1 : " + id1);
                    for (int j = 1; j < parts1.length; j++) {
                        yAxis1[j - 1] = Double.parseDouble(parts1[ho1.getIndexOf(j)]);
                        content1.append(((int) xAxis1[j - 1]) + Constants.TAB + yAxis1[j - 1] + Constants.NEW_LINE);
                    }
                    foundZoneID1 = true;
                }
                if (id2 == zone_id) {
                    System.out.println("Found zone in source dir 2 : " + id1);
                    for (int j = 1; j < parts2.length; j++) {
                        yAxis2[j - 1] = Double.parseDouble(parts2[ho2.getIndexOf(j)]);
                        content2.append(((int) xAxis2[j - 1]) + Constants.TAB + yAxis2[j - 1] + Constants.NEW_LINE);
                    }
                    foundZoneID2 = true;
                }
            }
            if (foundZoneID1 && foundZoneID2) {
                System.out.println("Writing gnuplot dat: " + destination1DAT);
                bw1.write(content1.toString());
                bw1.flush();
                System.out.println("Writing gnuplot dat: " + destination2DAT);
                bw2.write(content2.toString());
                bw2.flush();
                System.out.println("Finished writing gnuplot dat");
            } else System.err.println("Didn't found zone id " + zone_id + " in " + fileList1[i].getName());
            bw1.close();
            bw2.close();
        }
    }

    private static void checkAndRestoreZoneID(String source) {
        if (zone_id != zone_id_tmp) {
            zone_id = zone_id_tmp;
            System.out.println("Restoring zone_id (id = " + zone_id + ")...");
        }
        if ((source != null) && (source.endsWith("total_units__total_units.tab") || source.endsWith("total_office_units__total_office_units.tab"))) {
            System.out.println("Switching zone id from " + zone_id + " to 1 for table " + source);
            zone_id = 1;
        }
    }

    /**
	 * returns x-axis values for JFreeChart
	 * @param header
	 * @return
	 * @throws NumberFormatException
	 */
    @SuppressWarnings("all")
    private static double[] getXAxis(String[] header) throws NumberFormatException {
        String[] tmp;
        String tmpName;
        double xAxis[] = new double[header.length - 1];
        for (int i = 1; i < header.length; i++) {
            tmpName = header[i];
            tmp = tmpName.split(":");
            tmpName = tmp[0];
            tmp = tmpName.split("_");
            tmpName = tmp[tmp.length - 1];
            xAxis[i - 1] = Integer.parseInt(tmpName);
        }
        return xAxis;
    }

    /**
	 * return y-axis label for JFreeChart
	 * @param header
	 * @return
	 */
    private static String getYAxisLabel(String[] header) {
        String[] tmp;
        String tmpName;
        String columnName = header[1];
        tmp = columnName.split(":");
        columnName = tmp[0];
        tmp = columnName.split("_");
        tmpName = "";
        for (int i = 0; i < (tmp.length - 1); i++) tmpName = tmpName + tmp[i] + " ";
        columnName = tmpName.trim();
        return columnName;
    }

    /**
	 * creates an header object. A header object retuns the indices of columns
	 * in ascending order.
	 * 
	 * @param line
	 */
    private static HeaderObject initHeaderObject(String line) {
        String parts[] = line.split(Constants.TAB);
        int unsortedHeaderArray[] = new int[parts.length];
        int sortedHeaderArray[] = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            int index = parts[i].indexOf(":");
            parts[i] = parts[i].substring(0, index);
            String key[] = parts[i].split("_");
            try {
                unsortedHeaderArray[i] = Integer.parseInt(key[key.length - 1]);
            } catch (NumberFormatException nfe) {
                unsortedHeaderArray[i] = -1;
            }
            sortedHeaderArray[i] = unsortedHeaderArray[i];
        }
        Map<Integer, Integer> idxFromKey = HeaderParser.createIdxFromKey(unsortedHeaderArray);
        ArrayQuicksort(sortedHeaderArray);
        return new HeaderObject(idxFromKey, sortedHeaderArray);
    }

    /**
	 * sorts a given array
	 * 
	 * @param array
	 * 
	 * @author thomas
	 */
    public static int[] ArrayQuicksort(int array[]) {
        int i;
        System.out.println("Values Before the sort:\n");
        for (i = 0; i < array.length; i++) System.out.print(array[i] + "  ");
        System.out.println();
        quick_srt(array, 0, array.length - 1);
        System.out.print("Values after the sort:\n");
        for (i = 0; i < array.length; i++) System.out.print(array[i] + "  ");
        return array;
    }

    private static void quick_srt(int array[], int low, int n) {
        int lo = low;
        int hi = n;
        if (lo >= n) {
            return;
        }
        int mid = array[(lo + hi) / 2];
        while (lo < hi) {
            while (lo < hi && array[lo] < mid) {
                lo++;
            }
            while (lo < hi && array[hi] > mid) {
                hi--;
            }
            if (lo < hi) {
                int T = array[lo];
                array[lo] = array[hi];
                array[hi] = T;
            }
        }
        if (hi < lo) {
            int T = hi;
            hi = lo;
            lo = T;
        }
        quick_srt(array, low, lo);
        quick_srt(array, lo == low ? lo + 1 : lo, n);
    }
}

class HeaderObject {

    private Map<Integer, Integer> idxFromKey = null;

    private int sortedHeaderArray[] = null;

    public HeaderObject(Map<Integer, Integer> idxFromKey, int sortedHeaderArray[]) {
        this.idxFromKey = idxFromKey;
        this.sortedHeaderArray = sortedHeaderArray;
    }

    /**
	 * returns the index for zone_id
	 * @return index
	 */
    public int getZoneId() {
        int index = idxFromKey.get(-1);
        return index;
    }

    public int getIndexOf(int i) {
        int year = sortedHeaderArray[i];
        int index = idxFromKey.get(year);
        return index;
    }

    public double[] getSortedHeaderAsDouble() {
        double tmp[] = new double[sortedHeaderArray.length - 1];
        for (int i = 1; i < sortedHeaderArray.length; i++) tmp[i - 1] = (double) sortedHeaderArray[i];
        return tmp;
    }
}
