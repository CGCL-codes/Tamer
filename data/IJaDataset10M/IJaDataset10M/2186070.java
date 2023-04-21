package geneview2.pref;

class DisplayFeatureTableModel extends FeatureTableModel {

    private static final long serialVersionUID = 6887339534155642405L;

    DisplayFeatureTableModel(String headingForLeftColumn) {
        super(headingForLeftColumn);
        for (int i = 0; i < data[0].length; i++) data[0][i] = Boolean.TRUE;
        copyDataToBuffer();
    }

    public boolean isCellEditable(int row, int col) {
        return (col == 0) ? true : false;
    }

    public void setValueAt(Object value, int row, int col) {
        data[col][row] = value.equals(Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE;
        fireTableCellUpdated(row, col);
    }

    boolean isFeatureToBeDisplayed(int index) {
        return (data[0][index].equals(Boolean.TRUE)) ? true : false;
    }

    boolean[] getFeaturesToBeDisplayed() {
        final boolean[] list = new boolean[data[0].length];
        for (int i = 0; i < data[0].length; i++) list[i] = data[0][i].equals(Boolean.TRUE) ? true : false;
        return list;
    }
}
