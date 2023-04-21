package guineu.modules.visualization.intensityplot;

import guineu.data.PeakListRow;
import guineu.main.GuineuCore;
import guineu.parameters.ParameterSet;
import guineu.util.CollectionUtils;
import guineu.util.MathUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.statistics.StatisticalCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;

/**
 * This class implements 2 kinds of data sets - CategoryDataset and XYDataset
 * CategoryDataset is used if X axis is a raw data file or string parameter
 * XYDataset is used if X axis is a number parameter
 */
class IntensityPlotDataset extends AbstractDataset implements StatisticalCategoryDataset, IntervalXYDataset {

    private Object xAxisValueSource;

    private Comparable xValues[];

    private String selectedFiles[];

    private PeakListRow selectedRows[];

    IntensityPlotDataset(ParameterSet parameters) {
        this.xAxisValueSource = parameters.getParameter(IntensityPlotParameters.xAxisValueSource).getValue();
        this.selectedFiles = parameters.getParameter(IntensityPlotParameters.dataFiles).getValue();
        this.selectedRows = parameters.getParameter(IntensityPlotParameters.selectedRows).getValue();
        if (!xAxisValueSource.equals("Sample")) {
            String xAxisParameter = (String) xAxisValueSource;
            LinkedHashSet<Comparable> parameterValues = new LinkedHashSet<Comparable>();
            for (String file : selectedFiles) {
                Object value = GuineuCore.getDesktop().getSelectedDataFiles()[0].getParametersValue(file, (String) xAxisParameter);
                parameterValues.add((Comparable) value);
            }
            xValues = parameterValues.toArray(new Comparable[0]);
        }
        if (xAxisValueSource == IntensityPlotParameters.rawDataFilesOption) {
            xValues = new String[selectedFiles.length];
            System.arraycopy(selectedFiles, 0, xValues, 0, selectedFiles.length);
        }
    }

    Double[] getPeaks(int row, int column) {
        return getPeaks(xValues[column], selectedRows[row]);
    }

    Double[] getPeaks(Comparable xValue, PeakListRow row) {
        String files[] = getFiles(xValue);
        Double[] peaks = new Double[files.length];
        for (int i = 0; i < files.length; i++) {
            peaks[i] = (Double) row.getPeak(files[i]);
        }
        return peaks;
    }

    String[] getFiles(int column) {
        return getFiles(xValues[column]);
    }

    String[] getFiles(Comparable xValue) {
        if (xAxisValueSource.equals("Sample")) {
            String columnFile = selectedFiles[getColumnIndex(xValue)];
            return new String[] { columnFile };
        } else {
            HashSet<String> files = new HashSet<String>();
            String xAxisParameter = (String) xAxisValueSource;
            for (String file : selectedFiles) {
                Object fileValue = GuineuCore.getDesktop().getSelectedDataFiles()[0].getParametersValue(file, (String) xAxisParameter);
                if (fileValue == null) {
                    continue;
                }
                if (fileValue.equals(xValue)) {
                    files.add(file);
                }
            }
            return files.toArray(new String[0]);
        }
    }

    public Number getMeanValue(int row, int column) {
        Double[] peaks = getPeaks(xValues[column], selectedRows[row]);
        HashSet<Double> values = new HashSet<Double>();
        for (int i = 0; i < peaks.length; i++) {
            if (peaks[i] == null) {
                continue;
            }
            values.add(peaks[i]);
        }
        double doubleValues[] = CollectionUtils.toDoubleArray(values);
        if (doubleValues.length == 0) {
            return 0;
        }
        double mean = MathUtils.calcAvg(doubleValues);
        return mean;
    }

    public Number getMeanValue(Comparable rowKey, Comparable columnKey) {
        throw (new UnsupportedOperationException("Unsupported"));
    }

    public Number getStdDevValue(int row, int column) {
        Double[] peaks = getPeaks(xValues[column], selectedRows[row]);
        if (peaks.length == 1) {
            return 0;
        }
        HashSet<Double> values = new HashSet<Double>();
        for (int i = 0; i < peaks.length; i++) {
            if (peaks[i] == null) {
                continue;
            }
            values.add(peaks[i]);
        }
        double doubleValues[] = CollectionUtils.toDoubleArray(values);
        double std = MathUtils.calcStd(doubleValues);
        return std;
    }

    public Number getStdDevValue(Comparable rowKey, Comparable columnKey) {
        throw (new UnsupportedOperationException("Unsupported"));
    }

    public int getColumnIndex(Comparable column) {
        for (int i = 0; i < selectedFiles.length; i++) {
            if (selectedFiles[i].toString().equals(column)) {
                return i;
            }
        }
        return -1;
    }

    public Comparable getColumnKey(int column) {
        return xValues[column];
    }

    public List getColumnKeys() {
        return Arrays.asList(xValues);
    }

    public int getRowIndex(Comparable row) {
        for (int i = 0; i < selectedRows.length; i++) {
            if (selectedRows[i].toString().equals(row)) {
                return i;
            }
        }
        return -1;
    }

    public Comparable getRowKey(int row) {
        return selectedRows[row].toString();
    }

    public List getRowKeys() {
        return Arrays.asList(selectedRows);
    }

    public Number getValue(Comparable rowKey, Comparable columnKey) {
        return getMeanValue(rowKey, columnKey);
    }

    public int getColumnCount() {
        return xValues.length;
    }

    public int getRowCount() {
        return selectedRows.length;
    }

    public Number getValue(int row, int column) {
        return getMeanValue(row, column);
    }

    public Number getEndX(int row, int column) {
        return getEndXValue(row, column);
    }

    public double getEndXValue(int row, int column) {
        try {
            return ((Number) xValues[column]).doubleValue();
        } catch (ClassCastException e) {
            return column;
        }
    }

    public Number getEndY(int row, int column) {
        return getEndYValue(row, column);
    }

    public double getEndYValue(int row, int column) {
        return getMeanValue(row, column).doubleValue() + getStdDevValue(row, column).doubleValue();
    }

    public Number getStartX(int row, int column) {
        return getEndXValue(row, column);
    }

    public double getStartXValue(int row, int column) {
        return getEndXValue(row, column);
    }

    public Number getStartY(int row, int column) {
        return getStartYValue(row, column);
    }

    public double getStartYValue(int row, int column) {
        return getMeanValue(row, column).doubleValue() - getStdDevValue(row, column).doubleValue();
    }

    public DomainOrder getDomainOrder() {
        return DomainOrder.ASCENDING;
    }

    public int getItemCount(int series) {
        return xValues.length;
    }

    public Number getX(int series, int item) {
        return getStartX(series, item);
    }

    public double getXValue(int series, int item) {
        return getStartX(series, item).doubleValue();
    }

    public Number getY(int series, int item) {
        return getMeanValue(series, item);
    }

    public double getYValue(int series, int item) {
        return getMeanValue(series, item).doubleValue();
    }

    public int getSeriesCount() {
        return selectedRows.length;
    }

    public Comparable getSeriesKey(int series) {
        return getRowKey(series);
    }

    public int indexOf(Comparable value) {
        return getRowIndex(value);
    }
}
