package mainview;

import java.awt.datatransfer.*;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import javax.activation.DataHandler;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * {@link TransferHandler} that allows drag and drop support for a 
 * <code>JTable</code>.
 * <p>
 * Used in {@link MainviewView} to allow dragging from the <code>JXTable</code> 
 * to the 
 * <code>JXTree</code>.
 * <p>
 * This {@link TransferHandler} creates a collection of keys of all the selected 
 * <code>Artifact</code> (which are stored in the first (and hidden) column of the 
 * JXTable). This transfer object is then available to be dropped into the JXTree.
 * If this action is successful the <code>exportDone</code> method is called to 
 * fire an event saying the table has been updated.
 *
 * @author cbride
 */
public class MainviewTableTransferHandler extends TransferHandler {

    /**
	 * Flavor of the data being transferred
	 */
    public static DataFlavor dataFlavor;

    /**
	 * String describing the data being transferred
	 */
    private String dataFlavorString = DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.ArrayList";

    /**
	 * Constructor
	 */
    public MainviewTableTransferHandler() {
        super();
        try {
            dataFlavor = new DataFlavor(dataFlavorString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
	* Creates a <code>Transferable</code> to use as the source for
	* a data transfer. Returns the representation of the data to
	* be transferred. 
	 * <p>
	 * The Transferable is a collection of keys describing
	 * the Artifacts that are selected in the <code>JComponent</code>.
	 * 
	 * @param jComponent the JTable which is the source of the data transfer
	 * @return the representation of the data to be transferred
	 */
    public Transferable createTransferable(JComponent jComponent) {
        JTable table = (JTable) jComponent;
        ArrayList<String> objects = new ArrayList<String>();
        for (int i : table.getSelectedRows()) {
            int rowInModel = table.convertRowIndexToModel(i);
            objects.add(table.getModel().getValueAt(rowInModel, 0).toString());
        }
        return new DataHandler(objects, dataFlavorString);
    }

    /**
	 * Invoked after data has been exported. Tells the <code>source</code> 
	 * component, which is a JTable, that the data has changed and it needs to 
	 * update.
	 * 
	 * @param source the JComponent that was the source of the data
	 * @param data The data that was transferred
	 * @param action the actual action that was performed  
	 */
    public void exportDone(JComponent source, Transferable data, int action) {
        if (action != NONE) {
            JTable table = (JTable) source;
            ((RmtTableModel) table.getModel()).fireTableDataChanged();
        }
    }

    /**
	 * Returns the type of transfer actions supported by the source. This 
	 * transfer handler is used to {@code COPY} or {@code MOVE} the data
	 * 
	 * @param source the JComponent that was the source of the data
	 * @return the action type, which is  {@code COPY_OR_MOVE}
	 */
    public int getSourceActions(JComponent source) {
        return COPY_OR_MOVE;
    }
}
