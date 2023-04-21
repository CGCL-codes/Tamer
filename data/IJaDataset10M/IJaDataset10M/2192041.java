package bg.unisofia.fmi.kanban.gui.swing.dnd;

import bg.unisofia.fmi.kanban.swing.gui.PhasePanel;
import bg.unisofia.fmi.kanban.client.model.ITask;
import bg.unisofia.fmi.kanban.util.SwingUtil;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * Handles the Drag and Drop transfer between two instances of JList. The models of both instances of JList
 * must be instances of DefaultListModel and must containt nothing but TaskBean instances.
 *
 * @author nikolay.grozev
 */
public class InterListDropHandler extends TransferHandler {

    private static Logger LOGGER = Logger.getLogger(InterListDropHandler.class.getName());

    private DataFlavor mFlavour;

    private JList mDestinationList;

    /**
     * Creates a new instance with the provided falvour and destination list.
     * @param aFlavour - the falvor.  Must not be null
     * @param aDestinationList - the destination list. Must not be null.
     */
    public InterListDropHandler(DataFlavor aFlavour, JList aDestinationList) {
        this.mFlavour = aFlavour;
        this.mDestinationList = aDestinationList;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport anInfo) {
        if (!anInfo.isDataFlavorSupported(mFlavour)) {
            return false;
        }
        JList.DropLocation dropLocation = (JList.DropLocation) anInfo.getDropLocation();
        if (dropLocation.getIndex() == -1 || !dropLocation.isInsert()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport anInfo) {
        try {
            JList.DropLocation dropLocation = (JList.DropLocation) anInfo.getDropLocation();
            if (!anInfo.isDrop() || !anInfo.isDataFlavorSupported(mFlavour) || !dropLocation.isInsert()) {
                return false;
            }
            DefaultListModel listModel = (DefaultListModel) mDestinationList.getModel();
            int index = dropLocation.getIndex();
            Transferable t = anInfo.getTransferable();
            Object data = t.getTransferData(mFlavour);
            listModel.add(index, data);
            updateTasksCount(mDestinationList);
            return true;
        } catch (UnsupportedFlavorException ex) {
            LOGGER.log(Level.SEVERE, "Problems with the flavor in the Drag and Drop code", ex);
            return false;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Problem in the Drag and Drop code", ex);
            return false;
        }
    }

    @Override
    public int getSourceActions(JComponent aComp) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent aComp) {
        JList listCasted = (JList) aComp;
        return new TaskBeanTransferable((ITask) (listCasted.getSelectedValue()), mFlavour);
    }

    @Override
    protected void exportDone(JComponent aSourComp, Transferable aTransferable, int anActionCode) {
        if (anActionCode == TransferHandler.MOVE) {
            JList sourceList = (JList) aSourComp;
            DefaultListModel listModel = (DefaultListModel) sourceList.getModel();
            listModel.remove(sourceList.getSelectedIndex());
            updateTasksCount(sourceList);
        }
    }

    protected void updateTasksCount(JList component) {
        PhasePanel phasePanelParent = SwingUtil.getParentByClass(PhasePanel.class, component);
        phasePanelParent.updateTasksCount();
    }
}
