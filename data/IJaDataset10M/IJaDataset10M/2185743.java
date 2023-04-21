package dnb.model;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import dnb.data.RepositoryObject;

public class RepositoryObjectTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final List<RepositoryObject> repositoryObjects;

    private final String columnName;

    public RepositoryObjectTableModel(List<RepositoryObject> repositoryObjects, String columnName) {
        this.repositoryObjects = repositoryObjects;
        this.columnName = columnName;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public int getRowCount() {
        return repositoryObjects.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return repositoryObjects.get(rowIndex).getName();
    }

    @Override
    public String getColumnName(int column) {
        return columnName;
    }
}
