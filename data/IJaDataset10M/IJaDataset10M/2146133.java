package com.jedox.etl.components.extract;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.palo.api.Cell;
import org.palo.api.Connection;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ExportContext;
import org.palo.api.ExportDataset;
import com.jedox.etl.components.config.extract.CubeExtractConfigurator;
import com.jedox.etl.components.config.extract.DimensionFilterDefinition;
import com.jedox.etl.components.config.extract.CubeExtractConfigurator.CellType;
import com.jedox.etl.components.config.extract.DimensionFilterDefinition.FilterModes;
import com.jedox.etl.core.component.InitializationException;
import com.jedox.etl.core.component.RuntimeException;
import com.jedox.etl.core.connection.IConnection;
import com.jedox.etl.core.connection.IOLAPConnection;
import com.jedox.etl.core.extract.IExtract;
import com.jedox.etl.core.node.Row;
import com.jedox.etl.core.source.TableSource;
import com.jedox.etl.core.source.processor.IProcessor;
import com.jedox.etl.core.source.processor.Processor;
import com.jedox.etl.core.util.PersistenceUtil;
import com.jedox.etl.core.aliases.AliasMapElement;

/**
 *
 * @author Christian Schwarzinger. Mail: christian.schwarzinger@proclos.com
 *
 */
public class CubeExtract extends TableSource implements IExtract {

    private class CubeProcessor extends Processor {

        private CubeFilter filter;

        private ExportDataset export;

        private Row row;

        private int count;

        private ExportContext context;

        private int extractBulkSize;

        public CubeProcessor(CubeFilter filter) throws RuntimeException {
            try {
                this.filter = filter;
                setName(CubeExtract.this.getName());
                setInfo(true, "extract");
                int bulkSize = 10000;
                getParameter();
                extractBulkSize = Integer.parseInt(getParameter("bulkSize", String.valueOf(bulkSize)));
                row = PersistenceUtil.getColumnDefinition(getAliasMap(), getColumnsAsString(getColumns(filter.getCube())));
                init();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get cells from cube " + getCubeName() + ": " + e.getMessage());
            }
        }

        private void init() {
            count = 0;
            context = filter.getCube().getExportContext();
            context.reset();
            context.setBlocksize(extractBulkSize);
            context.setIgnoreEmptyCells(ignoreEmptyCells);
            context.setBaseCellsOnly(baseCellsOnly);
            context.setUseRules(useRules);
            context.setType(celltype.ordinal());
            log.debug("Parameters for cube extract " + getName() + ": bulkSize=" + context.getBlocksize() + ";" + " isBaseCellsOnly=" + context.isBaseCellsOnly() + "; ignoreEmptyCells=" + context.ignoreEmptyCells());
            if (filter.isFiltered()) {
                Element[][] basis = filter.getBasisElements(FilterModes.onlyNodes);
                if (basis == null) {
                    return;
                }
                context.setCellsArea(basis);
            }
            export = filter.getCube().getDataExport(context);
        }

        protected boolean fillRow(Row row) throws Exception {
            if (export != null && export.hasNextCell()) {
                count++;
                Cell cell = export.getNextCell();
                if (cell != null) {
                    try {
                        Element[] path = cell.getPath();
                        if (path != null) {
                            for (int j = 0; j < path.length; j++) {
                                row.getColumn(j).setValue(path[j].getName());
                            }
                            row.getColumn(row.size() - 1).setValue(getStringValue(cell.getValue()));
                        }
                    } catch (Exception e) {
                        log.error("Error in export cell: " + count + " " + e.getMessage());
                    }
                }
                return true;
            } else {
                context.reset();
                return false;
            }
        }

        protected Row getRow() {
            return row;
        }
    }

    private String cubeName;

    private String valueName;

    private boolean useRules;

    private boolean ignoreEmptyCells;

    private boolean baseCellsOnly;

    private CellType celltype;

    private List<DimensionFilterDefinition> definitions;

    private static final Log log = LogFactory.getLog(CubeExtract.class);

    public CubeExtract() {
        setConfigurator(new CubeExtractConfigurator());
    }

    public CubeExtractConfigurator getConfigurator() {
        return (CubeExtractConfigurator) super.getConfigurator();
    }

    private String getColumnsAsString(String[] coordinates) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < coordinates.length; i++) {
            sb.append(coordinates[i] + ", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String[] getColumns(Cube c) {
        Dimension[] dimensions = c.getDimensions();
        String[] names = new String[dimensions.length + 1];
        for (int i = 0; i < dimensions.length; i++) {
            names[i] = dimensions[i].getName();
            getAliasMap().map(new AliasMapElement(names[i], i + 1));
        }
        names[dimensions.length] = valueName;
        getAliasMap().map(new AliasMapElement(names[dimensions.length], dimensions.length + 1));
        return names;
    }

    private void setCubeName(String cubeName) {
        this.cubeName = cubeName;
    }

    private String getCubeName() {
        return cubeName;
    }

    public IOLAPConnection getConnection() throws RuntimeException {
        IConnection connection = super.getConnection();
        if ((connection != null) && (connection instanceof IOLAPConnection)) return (IOLAPConnection) connection;
        if (connection != null) throw new RuntimeException("OLAP connection is needed for extract " + getName() + ".");
        return null;
    }

    private CubeFilter getCubeFilter() throws RuntimeException {
        Connection con = getConnection().open();
        String database = getConnection().getDatabase();
        Database d = con.getDatabaseByName(database);
        if (d == null) {
            throw new RuntimeException("Database " + database + " not found in connection " + getConnection().getName());
        }
        Cube cube = d.getCubeByName(cubeName);
        if (cube == null) {
            throw new RuntimeException("Cube " + cubeName + " not found in database " + database);
        }
        CubeFilter filter = new CubeFilter(cube);
        filter.configure(definitions);
        return filter;
    }

    private String getStringValue(Object value) {
        if (value == null) return null;
        return value.toString();
    }

    protected IProcessor getSourceProcessor(int size) throws RuntimeException {
        IProcessor result = new CubeProcessor(getCubeFilter());
        result.setLastRow(size);
        return result;
    }

    public void init() throws InitializationException {
        try {
            super.init();
            setCubeName(getConfigurator().getCubeName());
            valueName = getConfigurator().getValueName();
            useRules = getConfigurator().getUseRules();
            definitions = getConfigurator().getFilterDefinitions();
            ignoreEmptyCells = getConfigurator().getIgnoreEmptyCells();
            baseCellsOnly = getConfigurator().isBaseCellsOnly();
            celltype = getConfigurator().getCellType();
        } catch (Exception e) {
            invalidate();
            throw new InitializationException(e);
        }
    }
}
