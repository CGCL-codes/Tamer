package net.sourceforge.squirrel_sql.fw.datasetviewer;

public class DataSetDefinition {

    /**
     * The collection of <TT>ColumnDisplayDefinition</TT> objects
     * for this data set.
     */
    private ColumnDisplayDefinition[] _columnDefs;

    /**
     * Ctor.
     *
     * @param   columnDefs  The <TT>ColumnDisplayDefinition</TT>
     *                      objects that make up this data set.
     */
    public DataSetDefinition(ColumnDisplayDefinition[] columnDefs) {
        super();
        _columnDefs = columnDefs != null ? columnDefs : new ColumnDisplayDefinition[0];
    }

    /**
     * Return the <TT>ColumnDisplayDefinition</TT> objects
     * that make up this data set.
     *
     * @return  <TT>ColumnDisplayDefinition</TT> objects
     *          that make up this data set.
     */
    public ColumnDisplayDefinition[] getColumnDefinitions() {
        return _columnDefs;
    }
}
