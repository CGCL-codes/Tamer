package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.model.ColumnType;
import org.middleheaven.util.QualifiedName;

/**
 * The model for a row column.
 */
public interface DBColumnModel {

    public String getSimpleName();

    public DBColumnModel duplicate();

    public void setTableModel(DBTableModel beanTableModel);

    public boolean isNullable();

    public boolean isKey();

    public boolean isVersion();

    public boolean isUnique();

    public String getUniqueGroupName();

    public int getSize();

    public int getPrecision();

    public ColumnType getType();

    public DBTableModel getTableModel();

    public QualifiedName getName();

    /**
	 * @return
	 */
    public boolean isIndexed();
}
