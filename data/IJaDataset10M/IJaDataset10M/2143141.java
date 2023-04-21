package rbsla.gui.wizard.treeeditor.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import rbsla.gui.elgenerator.rbsla.RBSLAElement;
import rbsla.gui.kernel.container.XMLNode;
import rbsla.gui.util.infoareas.Description;
import rbsla.gui.wizard.treeeditor.XMLTreeEditor;
import rbsla.gui.wizard.wizardutil.ElementStructure;

public class PopupTriggerEditor extends MouseAdapter {

    private JTree tree;

    private XMLTreeEditor owner;

    private int y, x;

    TreePath locatedPath;

    private Hashtable<String, ElementStructure> elStructureContainer;

    public PopupTriggerEditor(XMLTreeEditor owner, Hashtable<String, ElementStructure> elStructureContainer, JTree tree) {
        this.owner = owner;
        this.tree = tree;
        this.elStructureContainer = elStructureContainer;
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            x = e.getX();
            y = e.getY();
            locatedPath = tree.getPathForLocation(x, y);
            if (locatedPath != null) {
                if (locatedPath.getLastPathComponent() instanceof XMLNode) {
                    XMLNode selected_value = (XMLNode) locatedPath.getLastPathComponent();
                    if (!"text".equals(selected_value.toString()) && !selected_value.toString().contains("arg") && elStructureContainer.containsKey(selected_value.toString())) {
                        Description d = new Description(owner, new RBSLAElement(elStructureContainer.get(selected_value.toString()).getStructure()));
                        Point tLoc = tree.getLocationOnScreen();
                        d.setLocation(tLoc.x + x, tLoc.y + y);
                        d.setVisible(true);
                    }
                }
            }
        }
    }
}
