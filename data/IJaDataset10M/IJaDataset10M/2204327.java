package pt.igeo.snig.mig.editor.ui.dragAndDrop;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import pt.igeo.snig.mig.editor.record.contact.Contact;

/**
 * Drag and drop data flavors to allow contact drag and drop.
 * 
 * @author Jos� Pedro Dias
 * @version $Revision: 9654 $
 * @since 1.0
 */
public class TransferableTreeNode extends DefaultMutableTreeNode implements Transferable {

    /** required */
    private static final long serialVersionUID = -4061715596299746000L;

    /** the only supported flavor */
    public static final DataFlavor CONTACT_FLAVOR = new DataFlavor(Contact.class, "Contact Tree Node");

    /** supported flavors */
    static DataFlavor flavors[] = { CONTACT_FLAVOR };

    /** the data to drag around */
    private Contact contact;

    /**
	 * 
	 * @param data
	 */
    public TransferableTreeNode(Contact data) {
        this.contact = data;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(CONTACT_FLAVOR)) {
            return contact;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (DataFlavor df : flavors) {
            if (df.equals(flavor)) return true;
        }
        return false;
    }
}
