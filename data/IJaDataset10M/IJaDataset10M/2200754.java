package windu.sms.tblModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import windu.sms.entity.Inbox;

/**
 *
 * @author Windu Purnomo
 */
public class InboxTableModel extends AbstractTableModel {

    List<Inbox> list = new ArrayList<Inbox>();

    public InboxTableModel() {
    }

    public InboxTableModel(List<Inbox> list) {
        this.list = list;
    }

    public void addField(Inbox field) {
        list.add(field);
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    public void deleteField(int baris) {
        list.remove(baris);
        fireTableRowsDeleted(baris, baris);
    }

    public void clear() {
        list.clear();
        fireTableRowsDeleted(0, list.size());
    }

    public int getRowCount() {
        return list.size();
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return list.get(rowIndex).getSenderName();
            case 1:
                return list.get(rowIndex).getTextDecoded();
            case 2:
                return list.get(rowIndex).getReceivingDateTime();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Pengirim";
            case 1:
                return "Pesan";
            case 2:
                return "Waktu";
            default:
                return null;
        }
    }
}
