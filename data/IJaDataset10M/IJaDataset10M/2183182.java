package sequime.DotPlot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.data.DataType;
import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.base.node.viz.plotter.DataProvider;
import org.knime.base.node.util.DataArray;
import org.knime.base.node.viz.plotter.node.DefaultVisualizationNodeModel;
import org.knime.core.data.container.*;
import org.knime.core.data.DataRow;
import org.knime.base.node.preproc.transpose.TransposeTableNodeFactory;

/**
 * Rearranges incoming table in a matter the dotplot super classes expect their arguments.
 *
 * @author micha
 */
public class DotPlotNodeModel extends DefaultVisualizationNodeModel {

    static final String CFGKEY_COLUMN = "column";

    static final String DEFAULT_COLUMN = "Sequence";

    private final SettingsModelString m_column = new SettingsModelString(CFGKEY_COLUMN, DEFAULT_COLUMN);

    protected static final String CFGKEY_MAXLENGTH = "MaxSequenceLength";

    protected static final String CFGKEY_WORDSIZE = "WordSize";

    protected SettingsModelIntegerBounded maxSequenceLength = new SettingsModelIntegerBounded(DotPlotNodeModel.CFGKEY_MAXLENGTH, 1200, 1, Integer.MAX_VALUE);

    protected SettingsModelIntegerBounded wordSize = new SettingsModelIntegerBounded(DotPlotNodeModel.CFGKEY_WORDSIZE, 3, 1, Integer.MAX_VALUE);

    public DotPlotNodeModel() {
        super();
    }

    /**
	 * Rearranges columns in a matter our dotplot superclass expects arguments.
	 * throws IllegalArgumentException if sequence length exceeds maxSequenceLength.
	 * 
	 * Visualization is done inside the view.
	 */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
        DataTableSpec dts = inData[0].getDataTableSpec();
        int size = inData[0].getRowCount();
        DataColumnSpec[] allColSpecs = new DataColumnSpec[size];
        DataRow row;
        int i = 0;
        for (CloseableRowIterator iter = inData[0].iterator(); iter.hasNext(); i++) {
            row = iter.next();
            allColSpecs[i] = new DataColumnSpecCreator(row.getCell(dts.findColumnIndex("Identifier")).toString(), StringCell.TYPE).createSpec();
        }
        DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        i = 0;
        DataCell[] cells = new DataCell[size];
        for (CloseableRowIterator iter = inData[0].iterator(); iter.hasNext(); i++) {
            row = iter.next();
            String sequence = row.getCell(dts.findColumnIndex(this.m_column.getStringValue())).toString();
            if (sequence.length() > this.maxSequenceLength.getIntValue()) throw new IllegalArgumentException("Sequence exceeds maximum sequence length.");
            cells[i] = new StringCell(sequence);
        }
        DataRow newRow = new DefaultRow(new RowKey("PlotterLine"), cells);
        container.addRowToTable(newRow);
        container.close();
        return super.execute(new BufferedDataTable[] { container.getTable() }, exec);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        this.m_column.saveSettingsTo(settings);
        this.maxSequenceLength.saveSettingsTo(settings);
        this.wordSize.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        this.m_column.loadSettingsFrom(settings);
        this.maxSequenceLength.loadSettingsFrom(settings);
        this.wordSize.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        this.m_column.validateSettings(settings);
        this.maxSequenceLength.validateSettings(settings);
        this.wordSize.validateSettings(settings);
    }
}
