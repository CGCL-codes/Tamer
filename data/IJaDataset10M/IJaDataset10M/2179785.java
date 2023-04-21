package rgzm.gui.bfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import rgzm.bean.Fahrplan;
import rgzm.bean.TRAIN_STATE;
import rgzm.bean.Zug;
import rgzm.gui.View;
import base.util.ModelTime;

public class BfoTableModel extends AbstractTableModel implements View {

    public void notifyChanged(final TRAIN_STATE n, final TRAIN_STATE o) {
        fireTableDataChanged();
    }

    private List<Zug> oFpl;

    public int getColumnCount() {
        return 9;
    }

    public int getRowCount() {
        final List<Zug> zuege = getZuege();
        return zuege != null ? zuege.size() : 0;
    }

    public Object getValueAt(final int rowIndex, final int columnIndex) {
        String s = "---";
        final Zug z = getZug(rowIndex);
        switch(columnIndex) {
            case 0:
                s = "";
                break;
            case 1:
                ModelTime t1 = z.getAn();
                s = t1 != null ? t1.getHourMinString() : "";
                break;
            case 2:
                ModelTime t2 = z.getAb();
                s = t2 != null ? t2.getHourMinString() : "";
                break;
            case 3:
                s = z.getGleis();
                break;
            case 4:
                s = z.getZugNr();
                break;
            case 5:
                s = z.getVon();
                break;
            case 6:
                s = z.getNach();
                break;
            case 7:
                s = z.getPlusAnMin() + "";
                break;
            case 8:
                s = z.getPlusAbMin() + "";
                break;
        }
        return s;
    }

    public Zug getZug(final int i) {
        Zug z = getZuege().get(i);
        z.setView(this);
        return z;
    }

    private Fahrplan getFahrplan() {
        return Fahrplan.FAHRPLAN;
    }

    public void reset() {
        oFpl = null;
        fireTableDataChanged();
    }

    private List<Zug> getZuege() {
        if (oFpl == null) {
            oFpl = new ArrayList<Zug>();
            for (Zug z : getFahrplan()) {
                oFpl.add(z);
            }
            Collections.sort(oFpl);
        }
        return oFpl;
    }
}
