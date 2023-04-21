package org.dinopolis.timmon.frontend.treetable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

/**
 * This class represents a transferable for a single object.
 *
 * @author Dieter Freismuth
 * @version $Revision: 1.2 $
 */
public class ObjectTransferable implements Transferable {

    /** the supported flavors */
    public static final DataFlavor[] FLAVORS = { new DataFlavor(ObjectTransferable.class, "Object") };

    /** the object to transfer */
    protected Object object_;

    /**
   * Constructor taking the object that is to be transferred as its
   * argument. 
   *
   * @param object the object that is to be transferred.
   */
    public ObjectTransferable(Object object) {
        object_ = object;
    }

    /**
   * Returns the array of flavors in which it can provide the data.
   *
   * @return the array of flavors in which it can provide the data.
   */
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return (FLAVORS);
    }

    /**
   * Returns whether the requested flavor is supported by this
   * object.
   *
   * @param flavor the requested flavor for the data.
   */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return (flavor.equals(FLAVORS[0]));
    }

    /**
   * Returns the data corresponding to the given DataFlavor
   * <code>flavor</code>. 
   *
   * @param flavor the requested flavor for the data.
   * @exception UnsupportedFlavorException if the requested data flavor is
   * not supported.
   */
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (flavor.equals(FLAVORS[0])) return (object_);
        throw (new UnsupportedFlavorException(flavor));
    }
}
