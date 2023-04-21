package be.vds.jtbdive.view.tablemodels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import be.vds.jtbdive.model.Diver;

public class DiverTableModel extends TranslatableTableModel {

    public static final String FIRSTNAME = "header.firstname";

    public static final String LASTNAME = "header.lastname";

    public static final String[] HEADERS_1 = { FIRSTNAME, LASTNAME };

    private Collection<Diver> divers;

    public DiverTableModel(String[] currentHeaders) {
        super(currentHeaders);
    }

    public int getRowCount() {
        if (null == divers) {
            return 0;
        }
        return divers.size();
    }

    public Diver getDiverAt(int row) {
        int i = 0;
        for (Diver diver : divers) {
            if (i == row) {
                return diver;
            }
            i++;
        }
        return null;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > divers.size() || columnIndex > columnIndex) {
            return null;
        }
        Diver diver = getDiverAt(rowIndex);
        if (columnIndex == 0) {
            String s = diver.getFirstName();
            return s != null ? s : "";
        } else if (columnIndex == 1) {
            String s = diver.getLastName();
            return s != null ? s : "";
        } else {
            return null;
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setDivers(Collection<Diver> divers) {
        this.divers = divers;
        fireTableDataChanged();
    }

    public void addDiver(Diver diver) {
        if (null == divers) {
            divers = new ArrayList<Diver>();
            divers.add(diver);
            setDivers(divers);
        } else {
            if (divers.contains(diver)) {
                divers.remove(diver);
            }
            divers.add(diver);
            fireTableDataChanged();
        }
    }

    public void removeDiver(Diver diver) {
        this.divers.remove(diver);
        fireTableDataChanged();
    }

    public void removeDivers(List<Diver> divers) {
        for (Diver diver : divers) {
            this.divers.remove(diver);
        }
        fireTableDataChanged();
    }
}
