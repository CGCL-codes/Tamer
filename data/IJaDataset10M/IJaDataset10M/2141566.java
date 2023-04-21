package com.greentea.relaxation.jnmf.gui.components.project.network.using;

import com.greentea.relaxation.jnmf.gui.components.project.data.DatasetControlPanel;
import com.greentea.relaxation.jnmf.gui.style.Colors;
import com.greentea.relaxation.jnmf.gui.utils.ColoredTableCellRenderer;
import com.greentea.relaxation.jnmf.gui.utils.IColorProvider;
import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.util.JNMFMathUtils;
import com.greentea.relaxation.jnmf.util.data.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 02.05.2009 Time: 23:33:08 To change this template
 * use File | Settings | File Templates.
 */
public class ForecastReport extends JFrame {

    private static final int WindowWidth = 800;

    private static final int WindowHeight = 600;

    private DatasetControlPanel dataTableControlPanel;

    private Dataset forecast;

    private JPanel mainPanel;

    private Map<ColumnInfo, DoubleList> confidenceTable = new HashMap<ColumnInfo, DoubleList>();

    private RowSorter rowSorter;

    public ForecastReport() {
        super();
        $$$setupUI$$$();
        initGUI();
    }

    private void initGUI() {
        Calendar now = Calendar.getInstance();
        Formatter f = new Formatter();
        this.setTitle(Localizer.getString(StringId.NETWORK_FORECAST) + "  [" + f.format("%tT", now) + "]");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(WindowWidth, WindowHeight));
        this.setSize(WindowWidth, WindowHeight);
        dataTableControlPanel = new DatasetControlPanel(null);
        ColoredTableCellRenderer coloredTableRenderer = new ColoredTableCellRenderer(new IColorProvider() {

            public Color getColor(int row, int column) {
                return ForecastReport.this.getColor(row, column);
            }
        });
        dataTableControlPanel.setTableCellRenderer(coloredTableRenderer);
        mainPanel.add(dataTableControlPanel.getPanel(), new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, true));
        rowSorter = dataTableControlPanel.getDataTable().getRowSorter();
        getContentPane().add(mainPanel);
    }

    private Color getColor(int row, int column) {
        row = rowSorter.convertRowIndexToModel(row);
        Color color = null;
        ColumnInfo columnInfo = forecast.getMetadata().getColumn(column);
        if (columnInfo.getVariableType() == VariableType.ID) {
            color = Colors.get(Colors.NOT_ACTIVE_CELL_COLOR);
        } else if (columnInfo.getVariableType() == VariableType.IN) {
            color = Color.WHITE;
        } else {
            double persent = confidenceTable.get(columnInfo).get(row);
            color = new Color(255, (int) (255 - 128 * persent), (int) (255 - 196 * persent));
        }
        return color;
    }

    public void showNetworkForecast(TrainingDataset networkForecast) {
        this.forecast = networkForecast.createDataset();
        updateConfidenceTable(networkForecast, forecast);
        dataTableControlPanel.updateDataTable(forecast);
    }

    void updateConfidenceTable(TrainingDataset networkKorecast, Dataset forecast) {
        Metadata metadata = forecast.getMetadata();
        Map<ColumnInfo, Double> minValues = new HashMap<ColumnInfo, Double>();
        Map<ColumnInfo, Double> maxValues = new HashMap<ColumnInfo, Double>();
        for (ColumnInfo columnInfo : metadata.getColumns()) {
            minValues.put(columnInfo, (double) Integer.MAX_VALUE);
            maxValues.put(columnInfo, (double) Integer.MIN_VALUE);
        }
        final List<ColumnInfo> outputColumns = metadata.resolveColumns(VariableType.OUT);
        for (int i = 0; i < forecast.size(); ++i) {
            DoubleList output = networkKorecast.get(i).getOutput();
            for (ColumnInfo columnInfo : outputColumns) {
                Double currentMin = minValues.get(columnInfo);
                Double currentMax = maxValues.get(columnInfo);
                int firstIndex = metadata.getFirstIndexInTrainingDataset(columnInfo);
                int endIndex = metadata.getEndIndexInTrainingDataset(columnInfo);
                double value = 0;
                if (columnInfo.getDataType() == DataType.Number) {
                    value = output.get(firstIndex);
                }
                if (columnInfo.getDataType() == DataType.Boolean) {
                    value = output.get(firstIndex);
                }
                if (columnInfo.getDataType() == DataType.Category) {
                    value = calcSuperiority(output.subList(firstIndex, endIndex));
                }
                if (value < currentMin) {
                    minValues.put(columnInfo, value);
                }
                if (value > currentMax) {
                    maxValues.put(columnInfo, value);
                }
            }
        }
        for (ColumnInfo columnInfo : outputColumns) {
            Double currentMin = minValues.get(columnInfo);
            Double currentMax = maxValues.get(columnInfo);
            int firstIndex = metadata.getFirstIndexInTrainingDataset(columnInfo);
            int endIndex = metadata.getEndIndexInTrainingDataset(columnInfo);
            DoubleList confidence = new ArrayDoubleList();
            for (int i = 0; i < forecast.size(); ++i) {
                DoubleList output = networkKorecast.get(i).getOutput();
                double value = 0;
                if (columnInfo.getDataType() == DataType.Number) {
                    value = output.get(firstIndex);
                }
                if (columnInfo.getDataType() == DataType.Boolean) {
                    value = output.get(firstIndex);
                }
                if (columnInfo.getDataType() == DataType.Category) {
                    value = calcSuperiority(output.subList(firstIndex, endIndex));
                }
                confidence.add(JNMFMathUtils.reflectToInterval(value, currentMin, currentMax, 0, 1));
            }
            confidenceTable.put(columnInfo, confidence);
        }
    }

    private double calcSuperiority(DoubleList values) {
        double maxValue = Integer.MIN_VALUE;
        double secondMaxValue = Integer.MIN_VALUE;
        for (int i = 0; i < values.size(); ++i) {
            final double value = values.get(i);
            if (value > maxValue) {
                secondMaxValue = maxValue;
                maxValue = value;
            } else if (value > secondMaxValue) {
                secondMaxValue = value;
            }
        }
        return maxValue - secondMaxValue;
    }

    /**
    * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
    * call it in your code!
    *
    * @noinspection ALL
    */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }

    /**
    * @noinspection ALL
    */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
