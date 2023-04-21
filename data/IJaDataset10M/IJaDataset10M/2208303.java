package net.sourceforge.squirrel_sql.client.plugin;

import net.sourceforge.squirrel_sql.fw.datasetviewer.ColumnDisplayDefinition;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetDefinition;
import net.sourceforge.squirrel_sql.fw.datasetviewer.DataSetException;
import net.sourceforge.squirrel_sql.fw.datasetviewer.IDataSet;
import net.sourceforge.squirrel_sql.fw.util.IMessageHandler;
import net.sourceforge.squirrel_sql.client.plugin.IPlugin;
import net.sourceforge.squirrel_sql.client.plugin.PluginInfo;

public class PluginInfoArrayDataSet implements IDataSet {

    private interface i18n {

        String NAME = "Name";

        String AUTHOR = "Author";

        String LOADED = "Loaded";

        String VERSION = "Version";

        String UNKNOWN = "<Unknown>";

        String TRUE = "true";

        String FALSE = "false";
    }

    private PluginInfo[] _src;

    private DataSetDefinition _dsDef;

    private static final String[] s_hdgs = new String[] { i18n.NAME, i18n.LOADED, i18n.VERSION, i18n.AUTHOR };

    private PluginInfo _curRow;

    private int _curIndex = -1;

    public PluginInfoArrayDataSet(PluginInfo[] src) throws DataSetException, IllegalArgumentException {
        super();
        if (src == null) {
            throw new IllegalArgumentException("Null PluginInfo[][] passed");
        }
        _src = src;
        _dsDef = new DataSetDefinition(createColumnDefinitions());
    }

    public final int getColumnCount() {
        return s_hdgs.length;
    }

    public DataSetDefinition getDataSetDefinition() {
        return _dsDef;
    }

    public synchronized boolean next(IMessageHandler msgHandler) {
        _curRow = null;
        if (_src.length > (_curIndex + 1)) {
            _curRow = _src[++_curIndex];
            return true;
        }
        return false;
    }

    public synchronized Object get(int columnIndex) {
        if (_curRow == null) {
            throw new IllegalStateException("PluginInfoArrayDataSet.get() called but all rows read");
        }
        IPlugin plugin = _curRow.getPlugin();
        switch(columnIndex) {
            case 0:
                return plugin != null ? plugin.getDescriptiveName() : i18n.UNKNOWN;
            case 1:
                return _curRow.isLoaded() ? i18n.TRUE : i18n.FALSE;
            case 2:
                return plugin != null ? plugin.getVersion() : i18n.UNKNOWN;
            case 3:
                return plugin != null ? plugin.getAuthor() : i18n.UNKNOWN;
            default:
                throw new IndexOutOfBoundsException("" + columnIndex);
        }
    }

    private ColumnDisplayDefinition[] createColumnDefinitions() {
        final int columnCount = getColumnCount();
        ColumnDisplayDefinition[] columnDefs = new ColumnDisplayDefinition[columnCount];
        for (int i = 0; i < columnCount; ++i) {
            columnDefs[i] = new ColumnDisplayDefinition(50, s_hdgs[i]);
        }
        return columnDefs;
    }
}
