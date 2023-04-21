package uqdsd.infosec.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import uqdsd.infosec.InfoSec;
import uqdsd.infosec.model.HierarchyNode;

/**
 * @author InfoSec Project (c) 2008 UQ
 * 
 * Menu action to rename a component. Delegates to the application.
 */
public class SortMatrixAction extends AbstractAction {

    static final long serialVersionUID = 0;

    InfoSec application;

    public SortMatrixAction(InfoSec application) {
        super("Sort matrix");
        this.application = application;
    }

    public void actionPerformed(ActionEvent e) {
        String key = e.getActionCommand();
        if (key != null) {
            HierarchyNode hnode = application.lookupHierarchyNode(key);
            if (hnode != null) {
                hnode.getComponentModel().sortMatrix();
            }
        }
    }
}
