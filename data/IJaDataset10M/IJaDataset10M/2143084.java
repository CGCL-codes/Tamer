package mipt.io.table.indexed.factory;

import mipt.io.table.*;
import mipt.io.table.factory.AbstractDefaultTableFactory;
import mipt.io.table.indexed.DefaultIndexedIOTable;
import mipt.io.table.indexed.DefaultIndexedInputOutputTable;
import mipt.io.table.indexed.IndexedDefaultIOTable;
import mipt.io.table.indexed.IndexedDefaultInputOutputTable;
import mipt.io.table.indexed.IndexedFindTable;
import mipt.io.table.indexed.IndexedIOTable;
import mipt.io.table.indexed.IndexedInputOutputTable;
import mipt.io.table.indexed.IndexedInputTable;
import mipt.io.table.indexed.IndexedOutputTable;

/**
 */
public abstract class IndexedAbstractDefaultTableFactory extends AbstractDefaultTableFactory implements IndexedIOTableFactory, IndexedMultipathIOTableFactory {

    private AbstractIndexedDefaultTableFactory indexed;

    class IndexedDefaultTableFactoryAdapter extends AbstractIndexedDefaultTableFactory {

        public IndexedDefaultTableFactoryAdapter() {
            super();
        }

        protected IndexedFindTable createIndexedFindTable(String name, TablePath path) {
            return IndexedAbstractDefaultTableFactory.this.createIndexedFindTable(name, path);
        }

        protected IndexedInputTable createIndexedInputTable(String name, TablePath path) {
            return IndexedAbstractDefaultTableFactory.this.createIndexedInputTable(name, path);
        }

        protected IndexedOutputTable createIndexedOutputTable(String name, TablePath path) {
            return IndexedAbstractDefaultTableFactory.this.createIndexedOutputTable(name, path);
        }

        protected mipt.io.table.TableInfo createInfo(String name, TablePath path) {
            return IndexedAbstractDefaultTableFactory.this.createInfo(name, path);
        }

        public boolean supportsIndexedFind() {
            return IndexedAbstractDefaultTableFactory.this.supportsIndexedFind();
        }

        public char getTableNameSeparator() {
            return IndexedAbstractDefaultTableFactory.this.getTableNameSeparator();
        }

        public String getTableName(String fullName) {
            return IndexedAbstractDefaultTableFactory.this.getTableName(fullName);
        }

        public TablePath getTablePath(String fullName) {
            return IndexedAbstractDefaultTableFactory.this.getTablePath(fullName);
        }

        public java.util.HashMap getGroup(TablePath path) {
            return IndexedAbstractDefaultTableFactory.this.getGroup(path);
        }

        public boolean connect(String[] info) {
            return IndexedAbstractDefaultTableFactory.this.connect(info);
        }

        public void disconnect() {
            IndexedAbstractDefaultTableFactory.this.disconnect();
        }
    }

    public IndexedAbstractDefaultTableFactory() {
        super();
        this.indexed = new IndexedDefaultTableFactoryAdapter();
        indexed.setGroups(getGroups());
        indexed.setGroup(getGroup(null), null);
    }

    public IndexedAbstractDefaultTableFactory(TablePath path) {
        this();
        setPath(path);
    }

    /**
	 * Result must implement non-indexed FindTable interface
	 */
    protected abstract IndexedFindTable createIndexedFindTable(String name, TablePath path);

    /**
	 * Result must implement non-indexed InputTable interface
	 */
    protected abstract IndexedInputTable createIndexedInputTable(String name, TablePath path);

    /**
	 * Result must implement non-indexed OutputTable interface
	 */
    protected abstract IndexedOutputTable createIndexedOutputTable(String name, TablePath path);

    /**
	 * // Must be overriden if tables can't load info by themselves
	 */
    protected abstract TableInfo createInfo(String name, TablePath path);

    /**
	 * @see mipt.io.table.indexed.factory.IndexedFindTableFactory
	 */
    public final IndexedFindTable getIndexedFindTable(String name) {
        return indexed.getIndexedFindTable(name);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedFindTableFactory
	 */
    public final IndexedFindTable getIndexedFindTable(String name, TablePath path) {
        return indexed.getIndexedFindTable(name, path);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedInputTableFactory
	 */
    public final IndexedInputOutputTable getIndexedInputOutputTable(String name) {
        return indexed.getIndexedInputOutputTable(name);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedInputTableFactory
	 */
    public final IndexedInputOutputTable getIndexedInputOutputTable(String name, TablePath path) {
        return indexed.getIndexedInputOutputTable(name, path);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedInputTableFactory
	 */
    public final IndexedInputTable getIndexedInputTable(String name) {
        return indexed.getIndexedInputTable(name);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedInputTableFactory
	 */
    public final IndexedInputTable getIndexedInputTable(String name, TablePath path) {
        return indexed.getIndexedInputTable(name, path);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedOutputTableFactory
	 */
    public final IndexedOutputTable getIndexedOutputTable(String name) {
        return indexed.getIndexedOutputTable(name);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedOutputTableFactory
	 */
    public final IndexedOutputTable getIndexedOutputTable(String name, TablePath path) {
        return indexed.getIndexedOutputTable(name, path);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedIOTableFactory
	 */
    public final IndexedIOTable getIndexedTable(String name) {
        return indexed.getIndexedTable(name);
    }

    /**
	 * @see mipt.io.table.indexed.factory.IndexedIOTableFactory
	 */
    public final IndexedIOTable getIndexedTable(String name, TablePath path) {
        return indexed.getIndexedTable(name, path);
    }

    /**
	 * @see mipt.io.table.factory.AbstractDefaultTableFactory#createInputOutputTable(mipt.io.table.InputTable, mipt.io.table.OutputTable)
	 */
    protected DefaultInputOutputTable createInputOutputTable(InputTable input, OutputTable output, String name, TablePath path) {
        return new IndexedDefaultInputOutputTable(input, output, new DefaultIndexedInputOutputTable((IndexedInputTable) input, (IndexedOutputTable) output));
    }

    /**
	 * @see mipt.io.table.factory.AbstractDefaultTableFactory#createTable(mipt.io.table.FindTable, mipt.io.table.InputTable, mipt.io.table.OutputTable)
	 */
    protected DefaultIOTable createTable(FindTable find, InputTable input, OutputTable output, String name, TablePath path) {
        return new IndexedDefaultIOTable(find, input, output, new DefaultIndexedIOTable((IndexedFindTable) find, (IndexedInputTable) input, (IndexedOutputTable) output));
    }

    /**
	 * Default implementation
	 * If true, creates IO table, else creates InputOutputTable
	 * @return boolean
	 */
    public boolean supportsIndexedFind() {
        return supportsFind();
    }

    /**
	 */
    public void setPath(TablePath path) {
        super.setPath(path);
        indexed.setPath(path);
    }
}
