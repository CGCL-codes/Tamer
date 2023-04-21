package com.nexirius.framework.dataviewer;

import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelCommand;
import com.nexirius.framework.datamodel.DataModelCommandVector;
import javax.swing.*;

/**
 * This class creates a popup menu. It creates menu items for all methods which are associated with a
 * certain data model.
 *
 * @author Marcel Baumann
 */
public class DefaultTreePopupMenu implements TreePopupMenu {

    /**
     * Creates a simple popup menu which is based on the given data model (diaplays all the method from
     * the data model command list)
     *
     * @return null (if the model has no associated commands) or a JPopupMenu
     */
    public JPopupMenu createPopupMenu(ViewerFactory factory, DataModel model) {
        JPopupMenu ret = null;
        if (model.hasMethods()) {
            DataModelCommandVector v = model.getMethods();
            ret = new JPopupMenu();
            for (DataModelCommand m = v.firstItem(); m != null; m = v.nextItem()) {
                try {
                    CommandViewer viewer = (CommandViewer) factory.createDefaultViewer(m);
                    ret.add(viewer.createJMenuItem());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return ret;
    }
}
