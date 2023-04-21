package barde.lipo;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import barde.log.view.LogView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author cbonar@free.fr
 *
 */
public class LogTableListener extends MouseAdapter implements TableModelListener, ActionListener {

    private LogTableModel model;

    public LogTableListener(LogTableModel model) {
        this.model = model;
    }

    /**
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
    public void tableChanged(TableModelEvent e) {
    }

    /**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JTable table = (JTable) e.getComponent();
            JPopupMenu popup = new JPopupMenu();
            popup.setLightWeightPopupEnabled(false);
            int[] rows = table.getSelectedRows();
            if (rows.length >= 1) {
                String avatar = (String) table.getValueAt(rows[0], LogTableModel.COLUMN_AVATAR);
                JMenuItem retainAvatar = new JMenuItem(new ActionRetainAvatar(this.model, avatar));
                retainAvatar.addActionListener(this);
                popup.add(retainAvatar);
                JMenuItem removeAvatar = new JMenuItem(new ActionRemoveAvatar(this.model, avatar));
                removeAvatar.addActionListener(this);
                popup.add(removeAvatar);
            }
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
    }
}
