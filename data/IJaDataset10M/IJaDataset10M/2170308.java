package preprocessing.methods.Import.databasedata.gui.controllers.dragndrop;

import preprocessing.methods.Import.databasedata.gui.components.paintarea.PaintSQLStructure;
import preprocessing.methods.Import.databasedata.gui.controllers.actions.AddTableAction;
import java.awt.Point;
import javax.swing.TransferHandler;

/**
 * This class represents paint SQL panel transfer handler.
 *
 * @author Jiri Petnik
 */
public class PaintSQLTransferHandler extends TransferHandler {

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!info.isDataFlavorSupported(TableTransferableData.tableFlavor)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }
        if (!canImport(info)) {
            return false;
        }
        DropLocation dl = (DropLocation) info.getDropLocation();
        Point poi = dl.getDropPoint();
        PaintSQLStructure pan = (PaintSQLStructure) info.getComponent();
        pan.setDragDropPoint(poi);
        AddTableAction.instance.actionPerformed(null);
        return true;
    }
}
