package com.greentea.relaxation.jnmf.gui.components.project.network.learning.visualization;

import com.greentea.relaxation.algorithms.LearningAlgorithm;
import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.gui.components.AbstractComponent;
import com.greentea.relaxation.jnmf.gui.utils.GuiUtils;
import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.model.events.listeners.INotGuessedSamplesPersentCalculatedListener;
import com.greentea.relaxation.jnmf.util.data.ColumnInfo;
import com.greentea.relaxation.jnmf.util.data.Metadata;
import com.greentea.relaxation.jnmf.parameters.IDynamicParameter;
import com.greentea.relaxation.jnmf.parameters.annotations.Parameters;
import com.greentea.relaxation.jnmf.parameters.annotations.Configurable;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYDataItem;
import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 27.03.2009 Time: 18:15:50 To change this template
 * use File | Settings | File Templates.
 */
@Configurable
public class ProgressByClassesComponent extends AbstractComponent {

    private class XYSeriesVisibilityController {

        private boolean visible;

        private List<XYDataItem> dataItems = new ArrayList<XYDataItem>();

        private XYSeries series;

        private XYSeriesVisibilityController(XYSeries series, boolean visible) {
            this.series = series;
            setVisible(visible);
        }

        public synchronized void add(double x, double y) {
            if (isVisible()) {
                series.add(x, y);
            } else {
                dataItems.add(new XYDataItem(x, y));
            }
        }

        public boolean isVisible() {
            return visible;
        }

        public synchronized void setVisible(boolean visible) {
            if (visible) {
                for (XYDataItem item : dataItems) {
                    series.add(item);
                }
                dataItems.clear();
            } else {
                dataItems.addAll(series.getItems());
                series.clear();
            }
            this.visible = visible;
        }
    }

    private class NotGuessedSamplesPersentCalculatedListener implements INotGuessedSamplesPersentCalculatedListener {

        private String dataIdentifier;

        private int epochesCount = 0;

        private Map<String, XYSeriesVisibilityController> visibilityControllers = new HashMap<String, XYSeriesVisibilityController>();

        private XYSeriesCollection dataSeriesCollection;

        private Metadata metadata;

        NotGuessedSamplesPersentCalculatedListener(String dataIdentifier) {
            this.dataIdentifier = dataIdentifier;
        }

        public void signalNewNotGuessedPersentCalclulated(double notGuessedPersent, DoubleList persentByClasses) {
            epochesCount++;
            for (int i = 0; i < persentByClasses.size(); ++i) {
                String key = getSeriesKey(i, metadata);
                inCurrentMomentCategoryDataset.setValue((Number) persentByClasses.get(i), dataIdentifier, key);
                XYSeries series;
                XYSeriesVisibilityController visibilityController;
                try {
                    dataSeriesCollection.getSeries(key);
                    visibilityController = visibilityControllers.get(key);
                } catch (UnknownKeyException ignored) {
                    series = new XYSeries(key);
                    dataSeriesCollection.addSeries(series);
                    visibilityController = new XYSeriesVisibilityController(series, true);
                    visibilityControllers.put(key, visibilityController);
                }
                visibilityController.add(epochesCount, persentByClasses.get(i));
            }
        }

        public boolean isVisible(String seriesKey) {
            XYSeriesVisibilityController visibilityController = visibilityControllers.get(seriesKey);
            return visibilityController != null ? visibilityController.isVisible() : true;
        }

        public void setVisible(String seriesKey, boolean visible) {
            XYSeriesVisibilityController visibilityController = visibilityControllers.get(seriesKey);
            if (visibilityController != null) {
                visibilityController.setVisible(visible);
            }
        }

        public void reset() {
            dataSeriesCollection.removeAllSeries();
            epochesCount = 0;
        }

        public XYSeriesCollection getDataSeriesCollection() {
            return dataSeriesCollection;
        }

        public void setDataSeriesCollection(XYSeriesCollection dataSeriesCollection) {
            this.dataSeriesCollection = dataSeriesCollection;
        }

        public int getEpochesCount() {
            return epochesCount;
        }

        public void setEpochesCount(int epochesCount) {
            this.epochesCount = epochesCount;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }
    }

    private class ShowClassParameter implements IDynamicParameter {

        private List<NotGuessedSamplesPersentCalculatedListener> parents;

        private String className;

        public ShowClassParameter(List<NotGuessedSamplesPersentCalculatedListener> parents, String className) {
            this.parents = parents;
            this.className = className;
        }

        public String getName() {
            return Localizer.getString(StringId.SHOW_DIAGRAM_FOR_CLASS) + ": " + className;
        }

        public String getDescription() {
            return StringUtils.EMPTY;
        }

        public Object getMinValue() {
            return null;
        }

        public Object getMaxValue() {
            return null;
        }

        public Object getValue() {
            return parents.get(0).isVisible(className);
        }

        public void setValue(Object value) {
            for (NotGuessedSamplesPersentCalculatedListener parent : parents) {
                parent.setVisible(className, (Boolean) value);
            }
        }
    }

    private ChartPanel inCurrentMomentChartPanel;

    private JPanel controlPanel;

    private JTabbedPane mainTabbedPane;

    private ChartPanel learningDataChartPanel;

    private ChartPanel testDataChartPanel;

    private DefaultCategoryDataset inCurrentMomentCategoryDataset;

    private NotGuessedSamplesPersentCalculatedListener learningDataListener = new NotGuessedSamplesPersentCalculatedListener(Localizer.getString(StringId.ON_LEARNING_DATA));

    private NotGuessedSamplesPersentCalculatedListener testDataListener = new NotGuessedSamplesPersentCalculatedListener(Localizer.getString(StringId.ON_TEST_DATA));

    @Parameters
    private List<ShowClassParameter> showClassParameters;

    private Map<Integer, String> seriesKeysMap = new HashMap<Integer, String>();

    public ProgressByClassesComponent() {
        super(Localizer.getString(StringId.PROGRESS_BY_CLASSES));
        inCurrentMomentCategoryDataset = new DefaultCategoryDataset();
        $$$setupUI$$$();
        setControlPanel(controlPanel);
        setIcon(new ImageIcon(MainFrame.ICONS_DIR + "check.gif"));
    }

    private void createUIComponents() {
        final String yAxisLabel = Localizer.getString(StringId.PERCENT_NOT_GUESSED_CLASSES);
        JFreeChart errorsByClassesChart = ChartFactory.createBarChart(Localizer.getString(StringId.PROGRESS_BY_CLASSES), Localizer.getString(StringId.CLASSES), yAxisLabel, inCurrentMomentCategoryDataset, PlotOrientation.VERTICAL, true, true, true);
        XYSeriesCollection learningDataSeriesCollection = new XYSeriesCollection();
        XYSeriesCollection testDataSeriesCollection = new XYSeriesCollection();
        JFreeChart learningDataDiagramsChart = GuiUtils.createXYLineChart(Localizer.getString(StringId.PROGRESS_BY_CLASSES_ON_LEARNING_DATA), Localizer.getString(StringId.EPOCHES), yAxisLabel, learningDataSeriesCollection, PlotOrientation.VERTICAL, true, true, false, 1.8f, true);
        JFreeChart testDataDiagramsChart = GuiUtils.createXYLineChart(Localizer.getString(StringId.PROGRESS_BY_CLASSES_ON_TEST_DATA), Localizer.getString(StringId.EPOCHES), yAxisLabel, testDataSeriesCollection, PlotOrientation.VERTICAL, true, true, false, 1.8f, true);
        inCurrentMomentChartPanel = new ChartPanel(errorsByClassesChart);
        learningDataChartPanel = new ChartPanel(learningDataDiagramsChart, true);
        testDataChartPanel = new ChartPanel(testDataDiagramsChart, true);
        learningDataListener.setDataSeriesCollection(learningDataSeriesCollection);
        testDataListener.setDataSeriesCollection(testDataSeriesCollection);
    }

    private String getSeriesKey(int clazzIndex, Metadata metadata) {
        String key = seriesKeysMap.get(clazzIndex);
        if (key == null) {
            if (metadata == null) {
                key = Localizer.getString(StringId.CLASS) + " " + clazzIndex;
            } else {
                ColumnInfo outputColumn = metadata.getColumn(metadata.getFirstOutColumnIndex());
                return outputColumn.getCategoryValueByIndex(clazzIndex);
            }
            seriesKeysMap.put(clazzIndex, key);
        }
        return key;
    }

    public void onStartLearning(LearningAlgorithm learningAlgorithm) {
        inCurrentMomentCategoryDataset.clear();
        Metadata metadata = learningAlgorithm.getLearningData().getMetadata();
        learningDataListener.setMetadata(metadata);
        testDataListener.setMetadata(metadata);
        showClassParameters = createShowClassParameters(metadata, Arrays.asList(learningDataListener, testDataListener));
        learningAlgorithm.getNotGuessedLearningDataPercentListeners().add(learningDataListener);
        learningAlgorithm.getNotGuessedTestDataPercentListeners().add(testDataListener);
    }

    public List<ShowClassParameter> createShowClassParameters(Metadata metadata, List<NotGuessedSamplesPersentCalculatedListener> parents) {
        ColumnInfo outputColumn = metadata.getColumn(metadata.getFirstOutColumnIndex());
        int classesCount = outputColumn.getValuesOfCategory().size();
        List<ShowClassParameter> parameters = new ArrayList<ShowClassParameter>();
        for (int i = 0; i < classesCount; ++i) {
            String key = getSeriesKey(i, metadata);
            parameters.add(new ShowClassParameter(parents, key));
        }
        return parameters;
    }

    public void onCancelLearning(LearningAlgorithm learningAlgorithm) {
        seriesKeysMap.clear();
        learningDataListener.reset();
        testDataListener.reset();
    }

    @Override
    protected Serializable getSpecificSettings() {
        return learningDataListener.getEpochesCount();
    }

    @Override
    protected void setSpecificSettings(Serializable specificSettings) {
        int epochesCount = (Integer) specificSettings;
        learningDataListener.setEpochesCount(epochesCount);
        testDataListener.setEpochesCount(epochesCount);
    }

    public List<ShowClassParameter> getShowClassParameters() {
        return showClassParameters;
    }

    public void setShowClassParameters(List<ShowClassParameter> showClassParameters) {
        this.showClassParameters = showClassParameters;
    }

    @Override
    protected void loadLocalizedCaptions() {
        mainTabbedPane.setTitleAt(0, Localizer.getString(StringId.IN_CURRENT_MOMENT));
        mainTabbedPane.setTitleAt(1, Localizer.getString(StringId.DETAIL_FOR_LEARNING_DATA));
        mainTabbedPane.setTitleAt(2, Localizer.getString(StringId.DETAIL_FOR_TEST_DATA));
    }

    /**
    * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
    * call it in your code!
    *
    * @noinspection ALL
    */
    private void $$$setupUI$$$() {
        createUIComponents();
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainTabbedPane = new JTabbedPane();
        controlPanel.add(mainTabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainTabbedPane.addTab("� ������� ������", panel1);
        panel1.add(inCurrentMomentChartPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainTabbedPane.addTab("�������� ��� ��������� ������", panel2);
        panel2.add(learningDataChartPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainTabbedPane.addTab("�������� ��� �������� ������", panel3);
        panel3.add(testDataChartPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
    * @noinspection ALL
    */
    public JComponent $$$getRootComponent$$$() {
        return controlPanel;
    }
}
