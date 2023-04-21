package net.sourceforge.squirrel_sql.fw.gui.action.exportData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;
import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.CellComponentFactory;
import net.sourceforge.squirrel_sql.fw.datasetviewer.cellcomponent.DataTypeGeneral;
import net.sourceforge.squirrel_sql.fw.dialects.DialectType;
import net.sourceforge.squirrel_sql.fw.util.StringManager;
import net.sourceforge.squirrel_sql.fw.util.StringManagerFactory;
import net.sourceforge.squirrel_sql.fw.util.log.ILogger;
import net.sourceforge.squirrel_sql.fw.util.log.LoggerController;

/**
 * The implementation of {@link IExportData} for exporting data of a {@link ResultSet}
 * This class encapsulate the access to the {@link ResultSet}.
 * The implementation does not read the whole result set at once. It reads only a single row at a time.
 * So the memory footprint should be very small, even for huge result sets.
 * @author Stefan Willinger
 * 
 */
public class ResultSetExportData implements IExportData {

    /** Logger for this class. */
    private static final ILogger log = LoggerController.createLogger(ResultSetExportData.class);

    /** Internationalized strings for this class */
    private static final StringManager s_stringMgr = StringManagerFactory.getStringManager(ResultSetExportData.class);

    static interface i18n {

        String ERROR_READING_RESULTSET = s_stringMgr.getString("ResultSetExportData.errorReadingResultSet");
    }

    /**
	 * The result set to work on.
	 */
    private ResultSet resultSet;

    /**
	 * The {@link ColumnDisplayDefinition} for each column.
	 */
    private List<ColumnDisplayDefinition> colDispDef;

    private DialectType dialect;

    /**
	 * The current row index
	 */
    private int rowIndex = 0;

    /**
	 * Constructs the data on a given {@link ResultSet} and a specific dialect.
	 * @param resultSet The result set to use.
	 * @param dialect The dialect to use.
	 * @throws SQLException if the meta data of the result set could not be read.
	 */
    public ResultSetExportData(ResultSet resultSet, DialectType dialect) throws SQLException {
        this.resultSet = resultSet;
        this.colDispDef = new ArrayList<ColumnDisplayDefinition>();
        this.dialect = dialect;
        int columnCount = this.resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            colDispDef.add(new ColumnDisplayDefinition(resultSet, i, this.dialect, true));
        }
    }

    /**
	 * Gets the headers for the data.
	 * This depends on the extracted meta data of the result set.
	 * @see net.sourceforge.squirrel_sql.fw.gui.action.exportData.IExportData#getHeaders()
	 */
    @Override
    public Iterator<String> getHeaders() {
        List<String> headers = new ArrayList<String>();
        for (ColumnDisplayDefinition col : this.colDispDef) {
            String headerValue = col.getColumnHeading();
            headers.add(headerValue);
        }
        return headers.iterator();
    }

    /**
	 * Iterates over the result set.
	 * The result set will not be read into the memory. It creates a iterator, which reads one row at a time. 
	 * The amount of the used heap depends on the number of rows, that the JDBC-driver reads at once.
	 * @see net.sourceforge.squirrel_sql.fw.gui.action.exportData.IExportData#getRows()
	 */
    @Override
    public Iterator<IExportDataRow> getRows() {
        return new Iterator<IExportDataRow>() {

            @Override
            public void remove() {
                throw new IllegalStateException("not supported");
            }

            /**
			 * Reads the next row from the result set.
			 * @return A new IExportDataRow, created from the current row of the result set.
			 * @see java.util.Iterator#next()
			 */
            @Override
            public IExportDataRow next() {
                try {
                    List<IExportDataCell> cells = new ArrayList<IExportDataCell>();
                    for (int i = 1; i <= colDispDef.size(); i++) {
                        ColumnDisplayDefinition colDef = colDispDef.get(i - 1);
                        Object object = CellComponentFactory.readResultSet(colDef, resultSet, i, false);
                        IExportDataCell cell = new ExportDataColumn(colDef, object, rowIndex, i - 1);
                        cells.add(cell);
                    }
                    ExportDataRow data = new ExportDataRow(cells, rowIndex);
                    rowIndex++;
                    return data;
                } catch (SQLException e) {
                    log.error(i18n.ERROR_READING_RESULTSET, e);
                    throw new RuntimeException(i18n.ERROR_READING_RESULTSET, e);
                }
            }

            /**
			 * @see java.util.Iterator#hasNext()
			 * @return true, if the result set has more data.
			 */
            @Override
            public boolean hasNext() {
                try {
                    boolean next = resultSet.next();
                    return next;
                } catch (SQLException e) {
                    log.error(i18n.ERROR_READING_RESULTSET, e);
                    throw new RuntimeException(i18n.ERROR_READING_RESULTSET, e);
                }
            }
        };
    }
}
