package vademecum.preparation.nonLinearTransformer.transformModels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.data.Column;
import vademecum.data.DataGrid;
import vademecum.data.DataRow;
import vademecum.data.IColumn;
import vademecum.data.IDataGrid;
import vademecum.data.IDataRow;
import vademecum.data.TypeMismatchException;
import vademecum.math.density.pareto.ParetoDensity;
import vademecum.math.statistics.Univariate;
import vademecum.preparation.nonLinearTransformer.INonLinearTransformModel;
import vademecum.preparation.nonLinearTransformer.NonLinearTransformerPreference;

public class PowerTransformModel implements INonLinearTransformModel {

    Vector<NonLinearTransformerPreference> preferences;

    Vector<NonLinearTransformerPreference> defaultPreferences;

    JPanel staticPanel = null;

    JPanel dynamicPanel = null;

    PowerTransformerPlot plot;

    ActionListener parentDialogActionListener = null;

    protected static final int MOD_SKEWNESS = 0;

    protected static final int MOD_QUANTILE = 1;

    protected static final int MOD_MIDSUMMARY = 2;

    protected static final int MOD_NIDM = 3;

    protected static final int MOD_SHANON_ENTROPY = 4;

    protected static final int USE_MOD = PowerTransformModel.MOD_MIDSUMMARY;

    Vector<Object> dataRow;

    double paretoRadius;

    private static Log log = LogFactory.getLog(PowerTransformModel.class);

    public PowerTransformModel() {
        defaultPreferences = new Vector<NonLinearTransformerPreference>();
        defaultPreferences.add(new NonLinearTransformerPreference("Power p: y=f(x)^p", (double) 0));
        defaultPreferences.add(new NonLinearTransformerPreference("Min-bound for pre-transformation p < 0", (double) 1));
        defaultPreferences.add(new NonLinearTransformerPreference("Max-bound for pre-transformation p < 0", (double) 10));
        defaultPreferences.add(new NonLinearTransformerPreference("Skewness", (double) 0, true));
        defaultPreferences.add(new NonLinearTransformerPreference("p-quantile", (double) 0, true));
        defaultPreferences.add(new NonLinearTransformerPreference("Gradient of Midsummarys", (double) 0, true));
        defaultPreferences.add(new NonLinearTransformerPreference("NIDM", (double) 0, true));
        defaultPreferences.add(new NonLinearTransformerPreference("Shannon Entropy", (double) 0, true));
    }

    public String toString() {
        return new String("Power Transformation");
    }

    @Override
    public String getDescription() {
        return new String("Power Transformation y = f(x) ^ p");
    }

    @Override
    public Double newMax(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return Univariate.getMax(this.transformReverse(this.calculate(this.transform(column)), min, max));
    }

    @Override
    public Double newMin(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return Univariate.getMin(this.transformReverse(this.calculate(this.transform(column)), min, max));
    }

    @Override
    public Double newStdDev(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return Math.sqrt(Univariate.getVariance(this.transformReverse(this.calculate(this.transform(column)), min, max)));
    }

    @Override
    public Double newMean(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return Univariate.getMean(this.transformReverse(this.calculate(this.transform(column)), min, max));
    }

    @Override
    public Double newSkewness(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return Univariate.getRelativeSkewness(this.transformReverse(this.calculate(this.transform(column)), min, max));
    }

    @Override
    public Vector<Object> performChanges(Vector<Object> column) {
        double max = Univariate.getMax(column);
        double min = Univariate.getMin(column);
        return this.transformReverse(this.calculate(this.transform(column)), min, max);
    }

    private Vector<Object> calculate(Vector<Object> input) {
        int i;
        Vector<Object> output = new Vector<Object>(input.size());
        for (i = 0; i < input.size(); i++) {
            output.add(this.calculate((Double) input.get(i)));
        }
        return output;
    }

    private Double calculate(double value) {
        double value_new;
        if (preferences.get(0).value == 0) {
            value_new = Math.log(value);
        } else {
            value_new = Math.pow(value, preferences.get(0).value);
        }
        return value_new;
    }

    private Vector<Object> transform(Vector<Object> input) {
        Vector<Object> output = new Vector<Object>(input.size());
        double max = Univariate.getMax(input);
        double min = Univariate.getMin(input);
        for (int i = 0; i < input.size(); i++) {
            output.add(this.transform((Double) (input.get(i)), min, max));
        }
        return output;
    }

    private Double transform(double value, double min, double max) {
        double transformed;
        if (this.preferences.get(0).value > 0) {
            transformed = (value - min) / (max - min);
        } else {
            transformed = ((this.preferences.get(2).value * (value - min)) / (max - min)) + this.preferences.get(1).value;
        }
        return transformed;
    }

    private Vector<Object> transformReverse(Vector<Object> input, double min, double max) {
        Vector<Object> output = new Vector<Object>(input.size());
        for (int i = 0; i < input.size(); i++) {
            output.add(this.transformReverse((Double) (input.get(i)), min, max));
        }
        return output;
    }

    private Double transformReverse(double value, double min, double max) {
        double transformed;
        if (this.preferences.get(0).value > 0) {
            transformed = ((value * (max - min)) + min);
        } else {
            transformed = (((value - this.preferences.get(1).value) * (max - min)) / this.preferences.get(2).value) + min;
        }
        return transformed;
    }

    @Override
    public void setPreferences(Vector<NonLinearTransformerPreference> preferences) {
        this.preferences = preferences;
        if (this.dataRow != null) {
            if (this.preferences.size() > 3) this.preferences.get(3).value = this.getTestValue(PowerTransformModel.MOD_SKEWNESS); else {
                this.preferences.add(new NonLinearTransformerPreference("Skewness", (double) this.getTestValue(PowerTransformModel.MOD_SKEWNESS), true));
            }
            if (this.preferences.size() > 4) this.preferences.get(4).value = this.getTestValue(PowerTransformModel.MOD_QUANTILE); else {
                this.preferences.add(new NonLinearTransformerPreference("p-quantile", (double) this.getTestValue(PowerTransformModel.MOD_QUANTILE), true));
            }
            if (this.preferences.size() > 5) this.preferences.get(5).value = this.getTestValue(PowerTransformModel.MOD_MIDSUMMARY); else {
                this.preferences.add(new NonLinearTransformerPreference("Gradient of Midsummarys", (double) this.getTestValue(PowerTransformModel.MOD_MIDSUMMARY), true));
            }
            if (this.preferences.size() > 6) this.preferences.get(6).value = this.getTestValue(PowerTransformModel.MOD_NIDM); else {
                this.preferences.add(new NonLinearTransformerPreference("NDIM", (double) this.getTestValue(PowerTransformModel.MOD_NIDM), true));
            }
            if (this.preferences.size() > 7) this.preferences.get(7).value = this.getTestValue(PowerTransformModel.MOD_SHANON_ENTROPY); else {
                this.preferences.add(new NonLinearTransformerPreference("Shanon Entropy", (double) this.getTestValue(PowerTransformModel.MOD_SHANON_ENTROPY), true));
            }
        }
    }

    @Override
    public void updatePreferences(Vector<NonLinearTransformerPreference> preferences) {
        if (preferences != null) this.preferences = preferences;
        this.preferences.get(3).value = this.getTestValue(PowerTransformModel.MOD_SKEWNESS);
        this.preferences.get(4).value = this.getTestValue(PowerTransformModel.MOD_QUANTILE);
        this.preferences.get(5).value = this.getTestValue(PowerTransformModel.MOD_MIDSUMMARY);
        this.preferences.get(6).value = this.getTestValue(PowerTransformModel.MOD_NIDM);
        this.preferences.get(7).value = this.getTestValue(PowerTransformModel.MOD_SHANON_ENTROPY);
    }

    @Override
    public Vector<NonLinearTransformerPreference> getPreferences() {
        return this.preferences;
    }

    @Override
    public void setDefaultPreferences() {
        log.debug("Set the default Preference");
        this.preferences = this.defaultPreferences;
        this.initPreferenceData();
        this.preferences.get(3).value = this.getTestValue(PowerTransformModel.MOD_SKEWNESS);
        this.preferences.get(4).value = this.getTestValue(PowerTransformModel.MOD_QUANTILE);
        this.preferences.get(5).value = this.getTestValue(PowerTransformModel.MOD_MIDSUMMARY);
        this.preferences.get(6).value = this.getTestValue(PowerTransformModel.MOD_NIDM);
        this.preferences.get(7).value = this.getTestValue(PowerTransformModel.MOD_SHANON_ENTROPY);
    }

    @Override
    public void setDefaultPreferences(boolean noCalculate) {
        if (noCalculate) {
            this.preferences = this.defaultPreferences;
        } else this.setDefaultPreferences();
    }

    @Override
    public void initPreferenceData() {
        if (this.dataRow.size() > 3000) {
            this.getPMinimalIter();
        } else {
            this.getPMinimal();
        }
    }

    @Override
    public JPanel getStaticPanel() {
        this.staticPanel = new JPanel();
        JPanel sliderPanel = new JPanel();
        GridBagLayout dialoglayout = new GridBagLayout();
        GridLayout sliderLayout = new GridLayout(1, 2);
        GridBagConstraints c = new GridBagConstraints();
        this.staticPanel.setLayout(dialoglayout);
        this.staticPanel.setBorder(BorderFactory.createTitledBorder("Spezial Options"));
        sliderPanel.setLayout(sliderLayout);
        JSlider slider = new JSlider(-2000, 2000);
        slider.setMinorTickSpacing(20);
        slider.setMajorTickSpacing(200);
        slider.setPaintTicks(true);
        slider.setValue((int) (this.preferences.get(0).value * 200));
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                preferences.get(0).value = (double) slider.getValue() / 200;
                plot.updatePlotData(performChanges(getTestRow(dataRow)));
                parentDialogActionListener.actionPerformed(new ActionEvent(staticPanel, 0, "transformerpanelupdate"));
            }
        });
        sliderPanel.add(new JLabel("Adjust Power"));
        sliderPanel.add(slider);
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 0;
        c.gridx = 0;
        dialoglayout.setConstraints(sliderPanel, c);
        this.staticPanel.add(sliderPanel);
        return staticPanel;
    }

    @Override
    public JPanel getDynamicPanel() {
        this.dynamicPanel = new JPanel();
        GridBagLayout dialoglayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.dynamicPanel.setLayout(dialoglayout);
        this.dynamicPanel.setBorder(BorderFactory.createTitledBorder("Distribution"));
        plot = new PowerTransformerPlot(this.performChanges(this.getTestRow(this.dataRow)));
        c.gridheight = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridy = 0;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        dialoglayout.setConstraints(plot, c);
        this.dynamicPanel.add(plot);
        return this.dynamicPanel;
    }

    private Vector<Object> getTestRow(Vector<Object> dataRow) {
        int maxSize = 1000;
        Vector<Object> temp = null;
        Vector<Double> sorted = new Vector<Double>(dataRow.size());
        for (int i = 0; i < dataRow.size(); i++) {
            sorted.add(new Double((Double) dataRow.get(i)));
        }
        Collections.sort(sorted);
        if (dataRow.size() > maxSize) {
            double[] array = new double[sorted.size()];
            for (int i = 0; i < sorted.size(); i++) {
                array[i] = (Double) sorted.get(i);
            }
            temp = new Vector<Object>();
            for (int i = 1; i < maxSize; i++) {
                temp.add(Univariate.getQuantile(array, (double) (i * (double) 1 / maxSize)));
            }
        } else {
            temp = new Vector<Object>();
            for (int i = 1; i < sorted.size(); i++) {
                temp.add(sorted.get(i));
            }
        }
        return temp;
    }

    @Override
    public void setParentDialog(ActionListener parentDialogActionListener) {
        this.parentDialogActionListener = parentDialogActionListener;
    }

    @Override
    public void setDataRow(Vector<Object> dataRow) {
        this.dataRow = dataRow;
        double max = Univariate.getMax(dataRow);
        double min = Univariate.getMin(dataRow);
        Vector<Object> transTestData = new Vector<Object>(dataRow.size());
        for (int i = 0; i < dataRow.size(); i++) {
            transTestData.add(((Double) dataRow.get(i) - min) / (max - min));
        }
        int countElements = 100;
        Vector<Object> testData = new Vector<Object>(dataRow.size());
        double steps = transTestData.size() / countElements;
        for (int i = 0; i < countElements; i++) {
            testData.add(transTestData.get((int) Math.floor(i * steps)));
        }
        IDataGrid grid = new DataGrid();
        IColumn column = new Column(Double.class);
        grid.addColumn(column);
        for (int i = 0; i < testData.size(); i++) {
            IDataRow row = new DataRow(grid);
            try {
                row.setPoint(0, testData.get(i));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
            try {
                grid.addRow(row);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        IDataGrid centers = new DataGrid();
        column = new Column(Double.class);
        centers.addColumn(column);
        for (int i = 0; i < testData.size(); i++) {
            IDataRow row = new DataRow(grid);
            Double point = (double) i / testData.size();
            try {
                row.setPoint(0, point);
                centers.addRow(row);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        ParetoDensity pd = new ParetoDensity();
        pd.setDataGrid(grid);
        pd.setCenters(centers);
        this.paretoRadius = pd.getParetoRadius();
        log.debug("Pareto Radius for Shannon Entropy is: " + this.paretoRadius);
    }

    public double getTestValue(int mod) {
        double testvalue = 0;
        double max = Univariate.getMax(this.dataRow);
        double min = Univariate.getMin(this.dataRow);
        Vector<Object> temp = this.transformReverse(this.calculate(this.transform(this.dataRow)), min, max);
        Vector<Double> sorted = new Vector<Double>(temp.size());
        for (int i = 0; i < temp.size(); i++) {
            sorted.add(new Double((Double) temp.get(i)));
        }
        Collections.sort(sorted);
        double[] sortedArray = new double[temp.size()];
        for (int i = 0; i < sorted.size(); i++) {
            sortedArray[i] = (Double) sorted.get(i);
        }
        if (mod == PowerTransformModel.MOD_QUANTILE) {
            double quantile1 = Univariate.getQuantile(sortedArray, 0.1);
            double quantile5 = Univariate.getQuantile(sortedArray, 0.5);
            double quantile9 = Univariate.getQuantile(sortedArray, 0.9);
            testvalue = ((quantile9 - quantile5) - (quantile5 - quantile1)) / (quantile9 - quantile1);
        }
        if (mod == PowerTransformModel.MOD_SKEWNESS) {
            testvalue = this.newSkewness(this.dataRow);
        }
        if (mod == PowerTransformModel.MOD_MIDSUMMARY) {
            testvalue = this.getMidSummarys(sorted);
        }
        if (mod == PowerTransformModel.MOD_NIDM) {
            testvalue = this.getNIDM(sorted);
        }
        if (mod == PowerTransformModel.MOD_SHANON_ENTROPY) {
            testvalue = this.getShanonEntropy(temp);
        }
        return testvalue;
    }

    private double getShanonEntropy(Vector<Object> datas) {
        double max = Univariate.getMax(datas);
        double min = Univariate.getMin(datas);
        Vector<Object> transTestData = new Vector<Object>(datas.size());
        for (int i = 0; i < datas.size(); i++) {
            transTestData.add(((Double) datas.get(i) - min) / (max - min));
        }
        int elements = 100;
        Vector<Object> testData = new Vector<Object>(datas.size());
        double steps = transTestData.size() / elements;
        for (int i = 0; i < elements; i++) {
            testData.add(transTestData.get((int) Math.floor(i * steps)));
        }
        IDataGrid grid = new DataGrid();
        IColumn column = new Column(Double.class);
        grid.addColumn(column);
        for (int i = 0; i < testData.size(); i++) {
            IDataRow row = new DataRow(grid);
            try {
                row.setPoint(0, testData.get(i));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
            try {
                grid.addRow(row);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        IDataGrid centers = new DataGrid();
        column = new Column(Double.class);
        centers.addColumn(column);
        for (int i = 0; i < testData.size(); i++) {
            IDataRow row = new DataRow(grid);
            Double point = (double) i / (double) testData.size();
            try {
                row.setPoint(0, point);
                centers.addRow(row);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        ParetoDensity pd = new ParetoDensity();
        pd.setDataGrid(grid);
        pd.setCenters(centers);
        pd.calculateDensities(this.paretoRadius);
        double[] densities = pd.getDensities();
        double testvalue = 0;
        double p = 0;
        double sumDensities = 0;
        for (int i = 0; i < densities.length; i++) {
            sumDensities += densities[i];
        }
        for (int i = 0; i < densities.length; i++) {
            p = densities[i] / sumDensities;
            if (p != 0) testvalue += (p) * Math.log(p);
        }
        return (testvalue * -1);
    }

    private double getNIDM(Vector<Double> data) {
        double sum = 0;
        double max = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) > max) {
                max = data.get(i);
            }
            if (data.get(i) < min) {
                min = data.get(i);
            }
        }
        double value1 = 0;
        double value2 = 0;
        for (int i = 0; i < data.size() - 1; i++) {
            value1 = (data.get(i) - min) / (max - min);
            value2 = (data.get(i + 1) - min) / (max - min);
            sum += Math.sqrt((Math.abs(value1 - value2)));
        }
        return sum;
    }

    private double getMidSummarys(Vector<Double> data) {
        int size = data.size() - 1;
        double m;
        double next;
        Vector<Double> mids = new Vector<Double>();
        double temp;
        int i;
        m = (size + 1) / 2;
        if (m % 2 > 0) {
            mids.add(0.5 * (data.get((int) (m - 0.5)) + data.get((int) (m + 0.5))));
        } else {
            mids.add(data.get((int) m));
        }
        boolean go = false;
        next = m;
        while (!go) {
            next = (Math.floor(next) + 1) / 2;
            if (next % 2 == 0) {
                temp = (data.get((int) next) + data.get((int) (size - next + 1))) / 2;
                mids.add(temp);
                if (next == 1) go = true;
            } else {
                temp = (0.5 * (data.get((int) (next - 0.5)) + data.get((int) (next + 0.5)))) + (0.5 * (data.get((int) (size - next + 0.5)) + data.get((int) (size - next + 1.5)))) / 2;
                mids.add(temp);
                if (next == 1.5) go = true;
            }
        }
        double sum1 = 0;
        double xsquaresum = 0;
        double xsum = 0;
        double msize = mids.size();
        for (i = 0; i < msize; i++) {
            sum1 += (i + 1) * mids.get(i);
            xsquaresum += (i + 1) * (i + 1);
            xsum += (i + 1);
        }
        return (sum1 - msize * (xsum / msize) * Univariate.getMean2(mids)) / (xsquaresum - msize * ((xsum / msize) * (xsum / msize)));
    }

    private void getPMinimal() {
        double p = 0;
        double minValue = Double.MAX_VALUE;
        double newMinValue = 0;
        double step = 0.01;
        double testvalue = 0;
        int minIndex = 0;
        Vector<Object> testRow = new Vector<Object>();
        for (p = -10; p <= 10; p += step) {
            this.preferences.get(0).value = p;
            testvalue = Math.abs(this.getTestValue(PowerTransformModel.USE_MOD));
            testRow.add(testvalue);
        }
        for (int i = 0; i < testRow.size(); i++) {
            if ((newMinValue = (Double) testRow.get(i)) < minValue) {
                minValue = newMinValue;
                minIndex = i;
            }
        }
        p = step * minIndex - 10;
        log.debug("Get the p so testvalue is minimal");
        log.debug("p = " + p + " Testvalue Value = " + minValue);
        this.preferences.get(0).value = p;
    }

    private void getPMinimalIter() {
        double step = 1;
        double steps = 0;
        double p;
        double lower = 0;
        double upper = 0;
        double testvalue = 0;
        double oldvalue = 0;
        this.preferences.get(0).value = 1;
        p = this.preferences.get(0).value;
        testvalue = this.getTestValue(PowerTransformModel.USE_MOD);
        if (testvalue < 0) {
            lower = p;
            this.preferences.get(0).value = p + step;
            p = this.preferences.get(0).value;
            upper = p;
            steps++;
            while ((testvalue = this.getTestValue(PowerTransformModel.USE_MOD)) < 0 && steps < 9) {
                lower = p;
                this.preferences.get(0).value = p + step;
                p = this.preferences.get(0).value;
                upper = p;
                steps++;
            }
            if (steps < 9) {
                step = 0.01;
                this.preferences.get(0).value = lower + step;
                p = this.preferences.get(0).value;
                while ((testvalue = this.getTestValue(PowerTransformModel.USE_MOD)) < 0 && p < upper) {
                    this.preferences.get(0).value = p + step;
                    p = this.preferences.get(0).value;
                    oldvalue = testvalue;
                }
                if (Math.abs(oldvalue) < Math.abs(testvalue)) {
                    this.preferences.get(0).value = p - step;
                    p = this.preferences.get(0).value;
                }
            }
        } else {
            step = 0.1;
            upper = p;
            this.preferences.get(0).value = p - step;
            p = this.preferences.get(0).value;
            lower = p;
            steps++;
            while ((testvalue = this.getTestValue(PowerTransformModel.USE_MOD)) >= 0 && steps < 9) {
                upper = p;
                this.preferences.get(0).value = p - step;
                p = this.preferences.get(0).value;
                lower = p;
                steps++;
            }
            if (steps < 9) {
                step = 0.01;
                this.preferences.get(0).value = upper - step;
                p = this.preferences.get(0).value;
                while ((testvalue = this.getTestValue(PowerTransformModel.USE_MOD)) >= 0 && p > lower & p > 0.01) {
                    this.preferences.get(0).value = p - step;
                    p = this.preferences.get(0).value;
                    oldvalue = testvalue;
                }
                if (Math.abs(oldvalue) < Math.abs(testvalue)) {
                    this.preferences.get(0).value = p + step;
                    p = this.preferences.get(0).value;
                }
            }
        }
    }

    @Override
    public Vector<Object> getDataRow() {
        return this.dataRow;
    }
}

class PowerTransformerPlot extends JPanel {

    Vector<Object> dataRow;

    double min, max;

    private static Log log = LogFactory.getLog(PowerTransformerPlot.class);

    public PowerTransformerPlot(Vector<Object> dataRow) {
        updatePlotData(dataRow);
    }

    public void updatePlotData(Vector<Object> dataRow) {
        this.dataRow = dataRow;
        this.min = Univariate.getMin(this.dataRow);
        this.max = Univariate.getMax(this.dataRow);
    }

    @Override
    public Dimension getPreferredSize() {
        return this.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return this.getMaximumSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(400, 265);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int steps = this.getSize().width;
        IDataGrid grid = new DataGrid();
        IColumn column = new Column(Double.class);
        grid.addColumn(column);
        int i;
        for (i = 0; i < this.dataRow.size(); i++) {
            IDataRow row = new DataRow(grid);
            try {
                row.setPoint(0, this.dataRow.get(i));
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
            try {
                grid.addRow(row);
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        IDataGrid centers = new DataGrid();
        centers.addColumn(column);
        for (i = 0; i < steps; i++) {
            IDataRow row = new DataRow(grid);
            Double point = (double) i * ((this.max - this.min) / (steps - 1)) + this.min;
            try {
                row.setPoint(0, point);
                centers.addRow(row);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TypeMismatchException e) {
                e.printStackTrace();
            }
        }
        ParetoDensity pd = new ParetoDensity();
        pd.setDataGrid(grid);
        pd.setCenters(centers);
        pd.calculateDensities();
        double[] densities = pd.getDensities();
        double max = Univariate.getMax(densities);
        double min = Univariate.getMin(densities);
        int pointy1 = 0;
        int pointy2 = 0;
        for (i = 0; i < densities.length - 1; i++) {
            pointy1 = (int) ((200 * (densities[i] - min) / (max - min)));
            pointy2 = (int) ((200 * (densities[i + 1] - min) / (max - min)));
            g.drawLine(i, this.getMaximumSize().height - 50 - pointy1, i + 1, this.getMaximumSize().height - 50 - pointy2);
        }
    }
}
