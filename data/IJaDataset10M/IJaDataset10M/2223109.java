package dbsync4j.core.concrete;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.mythosis.beandiff.DiffField;
import com.mythosis.beandiff.Diffable;
import dbsync4j.core.behavior.Column;
import dbsync4j.core.behavior.DatabaseObject;
import dbsync4j.core.behavior.Index;
import dbsync4j.core.types.IndexType;
import dbsync4j.core.types.SortSequence;

/**
 * Abstrai os metadados estruturais dos �ndices do banco de dados.
 * 
 * @author Rafael
 *
 */
@Diffable
public class ConcreteIndex extends AbstractDependantObject implements Index {

    @DiffField
    private int cardinality;

    private Map<String, Column> columns = new HashMap<String, Column>();

    @DiffField
    private int pages;

    private SortSequence sortSequence;

    private IndexType indexType;

    @DiffField
    private boolean unique;

    @SuppressWarnings("unused")
    @DiffField
    private String indexTypeName;

    public ConcreteIndex(DatabaseObject object, String name, String remarks) {
        super(object, name, remarks);
    }

    @Override
    public int getCardinality() {
        return cardinality;
    }

    @Override
    public Collection<Column> getColumns() {
        return columns.values();
    }

    @Override
    public int getPages() {
        return pages;
    }

    @Override
    public SortSequence getSortSequence() {
        return sortSequence;
    }

    @Override
    public IndexType getType() {
        return indexType;
    }

    @Override
    public boolean isUnique() {
        return unique;
    }

    /**
	 * @param cardinality the cardinality to set
	 */
    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    /**
	 * @param pages the pages to set
	 */
    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
	 * @param sortSequence the sortSequence to set
	 */
    public void setSortSequence(SortSequence sortSequence) {
        this.sortSequence = sortSequence;
    }

    /**
	 * @param indexType the indexType to set
	 */
    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
        indexTypeName = indexType.name();
    }

    /**
	 * @param unique the unique to set
	 */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public void addColumn(Column column) {
        columns.put(column.getName(), column);
    }
}
