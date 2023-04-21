package edu.psu.its.lionshare.gui.library;

import edu.psu.its.lionshare.share.DataObjectGroup;
import edu.psu.its.lionshare.database.PeerserverHost;
import edu.psu.its.lionshare.gui.library.DataObjectTableModel;
import javax.swing.table.TableModel;
import javax.swing.JComponent;
import javax.swing.JTable;

/**
 *
 * This interface should be implemented by all the library view mediators.
 * (e.g. list/icon/image).
 *
 * @author Lorin Metzger    
 * LionShareP2P.
 *
 */
public interface LibraryViewInterface {

    public void setDataObjectGroup(DataObjectGroup group);

    public DataObjectGroup getDataObjectGroup();

    public JComponent getComponent();

    public JTable getTable();

    public DataObjectTableModel getTableModel();

    public LibraryTableControlPanel getControlPanel();
}
