package mipt.gui.data.choice;

import mipt.data.Data;

/**
 * Object for choosing single Data object from choice component
 */
public interface DataChooser extends mipt.data.choice.DataChooser {

    /**
 * See java.awt.List.makeVisible or swing.JTree.makeVisible to do the same
 * @param dataToView mipt.data.Data
 */
    void makeVisible(Data dataToView);
}
