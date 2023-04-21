package tjacobs.games.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import tjacobs.util.PrimaryKey.ComparablePrimaryKey;

@SuppressWarnings("unchecked")
public class HighScores<T extends ComparablePrimaryKey> extends ArrayList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String[] columnNames;

    private int[] columnDisplayOrder;

    private List<TableModelListener> mListeners;

    private int maxSize;

    public static final int DEFAULT_MAX_SIZE = 10;

    public HighScores(String[] columnNames) {
        this(columnNames, null, DEFAULT_MAX_SIZE);
    }

    public HighScores(String[] columnNames, int[] columnDisplayOrder, int maxSize) {
        this.maxSize = maxSize;
        this.columnNames = columnNames;
        if (columnDisplayOrder == null) {
            columnDisplayOrder = new int[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                columnDisplayOrder[i] = i;
            }
        }
        this.columnDisplayOrder = columnDisplayOrder;
    }

    public boolean isHighScore(T score) {
        if (score == null) return false;
        if (size() < maxSize) return true;
        return get(size() - 1).compareTo(score) < 0;
    }

    /**
	 * @deprecated this is not the proper way to use high score
	 * @see addHighScore
	 */
    public boolean add(T score) {
        return super.add(score);
    }

    /**
	 * @deprecated his is not the proper way to use high score
	 * @see addHighScore
	 */
    public void add(int idx, T score) {
        super.add(idx, score);
    }

    @SuppressWarnings("deprecation")
    public void addHighScore(T score) {
        if (size() == 0) {
            add(score);
            return;
        }
        for (int i = size(); i > 0; i--) {
            if (score.compareTo(get(i - 1)) < 0) {
                if (i >= maxSize) {
                    return;
                } else {
                    add(i, score);
                    while (size() > maxSize) {
                        remove(size() - 1);
                    }
                    return;
                }
            }
        }
        if (get(0).compareTo(score) < 0) {
            add(0, score);
        } else if (size() < maxSize) add(size(), score);
    }

    public void setTableModelListeners(List<TableModelListener> l) {
        mListeners = l;
    }

    public List<TableModelListener> removeTableModelListeners() {
        List<TableModelListener> l = mListeners;
        mListeners = null;
        return l;
    }

    public TableModel getTableModel() {
        int sz = size();
        if (sz == 0) return new DefaultTableModel();
        int wid = get(0).getPrimaryKey().length;
        DefaultTableModel dtm = new DefaultTableModel(size() + 1, get(0).getPrimaryKey().length);
        for (int i = 0; i < sz + 1; i++) {
            for (int j = 0; j < wid; j++) {
                dtm.setValueAt(this.getValueAt(i, j), i, j);
            }
        }
        return dtm;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnDisplayOrder[columnIndex]];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0) {
            return getColumnName(columnIndex);
        }
        Comparable[] vals = get(rowIndex - 1).getPrimaryKey();
        return vals[columnDisplayOrder[columnIndex]];
    }
}
