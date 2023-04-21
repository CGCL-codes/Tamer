package org.semtinel.core.rsview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.semtinel.core.data.api.Annotation;

/**
 *
 * @author kai
 */
public class AnnotationsTableModel extends AbstractTableModel {

    private List<Annotation> annotations = new ArrayList<Annotation>();

    private String title;

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations.clear();
        this.annotations.addAll(annotations);
        Collections.sort(this.annotations, new Comparator<Annotation>() {

            public int compare(Annotation o1, Annotation o2) {
                if (o1.getRank() > o2.getRank()) {
                    return -1;
                }
                if (o1.getRank() == o2.getRank()) {
                    return 0;
                }
                return 1;
            }
        });
        fireTableDataChanged();
    }

    public AnnotationsTableModel(String title) {
        this.title = title;
    }

    public int getRowCount() {
        return annotations.size();
    }

    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(int column) {
        return title;
    }

    public Annotation getAnnotation(int row) {
        if (row == -1) return null;
        return annotations.get(row);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return annotations.get(rowIndex);
    }
}
