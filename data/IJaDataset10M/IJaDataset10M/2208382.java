package org.formaria.swing.docking;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * A transfer handler for a dockable component. The object handles the 
 * <code>application/x-aria-xdockable</code> drop flavor. It is only intended
 * that the drag and drop operation occurs within a single application. This
 * transferrable object maintains a static reference to the dockable object and
 * therefore it is not suitable for use in other contexts or between JVM 
 * instances.
 * <p>Copyright: Formaria Ltd. (c) 2008<br>
 * License:      see license.txt</p>
 * $Revision: 1.2 $
 */
public class DockableTransferHandler extends TransferHandler {

    private static Dockable transferDockable;

    private Object target;

    private static DataFlavor dockableFlavor;

    /** 
   * Creates a new instance of DockableTransferHandler 
   */
    public DockableTransferHandler(Object target) {
        this.target = target;
        if (dockableFlavor == null) {
            try {
                dockableFlavor = new DataFlavor("application/x-aria-xdockable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Causes a transfer to a component from a clipboard or a 
   * DND drop operation.  The <code>Transferable</code> represents
   * the data to be imported into the component.  
   *
   * @param comp  the component to receive the transfer; this
   *  argument is provided to enable sharing of <code>TransferHandler</code>s 
   *  by multiple components
   * @param t     the data to import
   * @return  true if the data was inserted into the component, false otherwise
   */
    public boolean importData(JComponent c, Transferable t) {
        transferDockable.header.endDock();
        DockingPanel dp = null;
        if (c instanceof DockingPanel) dp = (DockingPanel) c; else if (c instanceof DockableHeader) dp = (DockingPanel) (((DockableHeader) c).getParent().getParent()); else if (c instanceof DockingPanel.DockableProxy) dp = ((DockingPanel.DockableProxy) c).getDockingPanel();
        if (dp != null) {
            transferDockable.dockedContainer.removeDockable(transferDockable, false);
            dp.addDockable(transferDockable, null, null);
            if (!dp.isVisible()) dp.restoreContent(transferDockable);
            transferDockable = null;
            return (t instanceof DockableTransferHandler);
        }
        return false;
    }

    /**
   * Creates a <code>Transferable</code> to use as the source for
   * a data transfer. Returns the representation of the data to
   * be transferred, or <code>null</code> if the component's
   * property is <code>null</code>
   *
   * @param c  the component holding the data to be transferred; this
   *  argument is provided to enable sharing of <code>TransferHandler</code>s
   *  by multiple components
   * @return  the representation of the data to be transferred, or
   *  <code>null</code> if the property associated with <code>c</code>
   *  is <code>null</code> 
   *  
   */
    protected Transferable createTransferable(JComponent c) {
        return new DockableTransferable(((DockableHeader) c).getDockable());
    }

    /**
   * Returns the type of transfer actions supported by the source. 
   * Some models are not mutable, so a transfer operation of <code>COPY</code>
   * only should be advertised in that case. In this case only the MOVE 
   * operation is supported.
   * 
   * @param c  the component holding the data to be transferred; this
   *  argument is provided to enable sharing of <code>TransferHandler</code>s
   *  by multiple components.
   * @return  <code>COPY</code> if the transfer property can be found,
   *  otherwise returns <code>NONE</code>; a return value of
   *  of <code>NONE</code> disables any transfers out of the component
   */
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    /**
   * Invoked after data has been exported.  This method should remove 
   * the data that was transferred if the action was <code>MOVE</code>.
   * <p>
   * This method is implemented to do nothing since <code>MOVE</code>
   * is not a supported action of this implementation
   * (<code>getSourceActions</code> does not include <code>MOVE</code>).
   *
   * @param source the component that was the source of the data
   * @param data   The data that was transferred or possibly null
   *               if the action is <code>NONE</code>.
   * @param action the actual action that was performed  
   */
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (transferDockable != null) transferDockable.header.endDock();
    }

    /**
   * Indicates whether a component would accept an import of the given
   * set of data flavors prior to actually attempting to import it. 
   *
   * @param comp  the component to receive the transfer; this
   *  argument is provided to enable sharing of <code>TransferHandlers</code>
   *  by multiple components
   * @param transferFlavors  the data formats available
   * @return  true if the data can be inserted into the component, false otherwise
   */
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (transferDockable == null) return false;
        if (c instanceof DockableHeader) {
            transferDockable.header.showDock(c, ((DockableHeader) c).getParent().getParent());
            return true;
        } else if (c instanceof DockingPanel) {
            transferDockable.header.showDock(c, c);
            return true;
        } else if (c instanceof DockingPanel.DockableProxy) {
            DockingPanel dp = ((DockingPanel.DockableProxy) c).getDockingPanel();
            transferDockable.header.showDock(c, dp);
            return true;
        }
        return false;
    }

    /**
   * An internal transfer object.
   */
    private class DockableTransferable implements Transferable {

        DockableTransferable(Dockable doc) {
            transferDockable = doc;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return transferDockable;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { dockableFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return dockableFlavor.equals(flavor);
        }
    }
}
