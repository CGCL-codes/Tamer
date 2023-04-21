package druid.util.gui;

import java.util.Vector;
import org.dlib.gui.TComboBox;
import org.dlib.gui.flextable.FlexTableConfirmator;
import druid.core.AttribList;
import druid.core.AttribSet;

public class DataEntryPanel extends AbstractDataEntryPanel implements FlexTableConfirmator {

    protected Vector vAttribs = new Vector();

    protected AttribList alData;

    public DataEntryPanel() {
        flexModel.setConfirmator(this);
    }

    public void addAttrib(String name, String label, int width) {
        flexModel.addColumn(label, width);
        vAttribs.addElement(name);
    }

    public void addAttrib(String name, String label, int width, TComboBox tcb) {
        flexModel.addColumn(label, width, tcb);
        vAttribs.addElement(name);
    }

    public void setAttribList(AttribList al) {
        alData = al;
        flexModel.clearData();
        for (int i = 0; i < al.size(); i++) flexModel.addRow(buildVector(al.get(i)));
        flexTable.setFlexModel(flexModel);
        btnDel.setEnabled(false);
        btnCut.setEnabled(false);
        btnCopy.setEnabled(false);
        btnPaste.setEnabled(clipData != null);
        btnUp.setEnabled(false);
        btnDown.setEnabled(false);
    }

    public int dataSize() {
        return alData.size();
    }

    protected void handleNew() {
        AttribSet as = alData.append();
        flexModel.addRow(buildVector(as));
        flexTable.updateTable();
        flexTable.selectLastRow();
    }

    protected void handleDel(boolean cut) {
        if (cut) clipData = alData.get(iSelectedRow);
        alData.remove(iSelectedRow);
        flexModel.removeRow(iSelectedRow);
        flexTable.updateTable();
        int rows = flexTable.getRowCount();
        if (rows == iSelectedRow) flexTable.clearSelection(); else if (rows - 1 == iSelectedRow) btnDown.setEnabled(false);
        btnPaste.setEnabled(clipData != null);
    }

    protected void handleCopy() {
        AttribSet as = alData.get(iSelectedRow).duplicate();
        as.remapId();
        clipData = as;
        btnPaste.setEnabled(true);
    }

    protected void handlePaste() {
        AttribSet as = (AttribSet) clipData;
        clipData = ((AttribSet) clipData).duplicate();
        ((AttribSet) clipData).remapId();
        if (iSelectedRow == -1 || iSelectedRow == flexTable.getRowCount() - 1) {
            alData.append(as);
            flexModel.addRow(buildVector(as));
            flexTable.updateTable();
            flexTable.selectLastRow();
        } else {
            alData.insert(iSelectedRow + 1, as);
            flexModel.insertRow(iSelectedRow + 1, buildVector(as));
            flexTable.updateTable();
            flexTable.selectRow(iSelectedRow + 1);
        }
    }

    protected void handleUp() {
        int i = iSelectedRow;
        alData.swap(i, i - 1);
        flexModel.swapRows(i, i - 1);
        flexTable.updateTable();
        flexTable.selectRow(i - 1);
    }

    protected void handleDown() {
        int i = iSelectedRow;
        alData.swap(i, i + 1);
        flexModel.swapRows(i, i + 1);
        flexTable.updateTable();
        flexTable.selectRow(i + 1);
    }

    public boolean confirmValueChanged(int row, int col, Object value) {
        if (value == null) return false;
        String name = (String) vAttribs.get(col);
        alData.get(row).setData(name, value);
        return true;
    }

    private Vector buildVector(AttribSet as) {
        Vector row = new Vector();
        for (int j = 0; j < vAttribs.size(); j++) {
            String name = (String) vAttribs.get(j);
            row.add(as.getData(name));
        }
        return row;
    }
}
