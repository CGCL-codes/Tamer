package org.argouml.uml.diagram.static_structure.ui;

import java.util.Vector;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

public class GoClassToCompositeClass implements TreeModel {

    public String toString() {
        return "Class->Composite Class";
    }

    public Object getRoot() {
        throw new UnsupportedOperationException("getRoot should never be called");
    }

    public void setRoot(Object r) {
    }

    public Object getChild(Object parent, int index) {
        if (parent instanceof MClass) {
            Vector children = getChildren((MClass) parent);
            return (children == null) ? null : children.elementAt(index);
        }
        throw new UnsupportedOperationException("getChild should never get here");
    }

    public int getChildCount(Object parent) {
        if (parent instanceof MClass) {
            Vector children = getChildren((MClass) parent);
            return (children == null) ? 0 : children.size();
        }
        return 0;
    }

    public int getIndexOfChild(Object parent, Object child) {
        int res = 0;
        if (parent instanceof MClass) {
            Vector children = getChildren((MClass) parent);
            if (children == null) return -1;
            if (children.contains(child)) return children.indexOf(child);
        }
        return -1;
    }

    public Vector getChildren(MClass parentClass) {
        Vector res = new Vector();
        Vector ends = new Vector(parentClass.getAssociationEnds());
        if (ends == null) return res;
        java.util.Enumeration enu = ends.elements();
        while (enu.hasMoreElements()) {
            MAssociationEnd ae = (MAssociationEnd) enu.nextElement();
            if (!ae.getAggregation().equals(MAggregationKind.COMPOSITE)) continue;
            MAssociation asc = ae.getAssociation();
            Vector allEnds = new Vector(asc.getConnections());
            MAssociationEnd otherEnd = null;
            if (ae == allEnds.elementAt(0)) otherEnd = (MAssociationEnd) allEnds.elementAt(1);
            if (ae == allEnds.elementAt(1)) otherEnd = (MAssociationEnd) allEnds.elementAt(0);
            MClassifier assocClass = otherEnd.getType();
            if (assocClass != null && !res.contains(assocClass)) res.addElement(assocClass);
        }
        return res;
    }

    public boolean isLeaf(Object node) {
        return !(node instanceof MClass && getChildCount(node) > 0);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }
}
