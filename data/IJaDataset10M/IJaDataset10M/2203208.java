package org.argouml.uml.diagram.deployment;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.uml.ColumnDescriptor;
import org.argouml.uml.TableModelComposite;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MModelElement;

public class TableModelClass_in_DeplByProps extends TableModelComposite {

    public TableModelClass_in_DeplByProps() {
    }

    public void initColumns() {
        addColumn(ColumnDescriptor.Name);
        addColumn(ColumnDescriptor.ImplLocation);
        addColumn(ColumnDescriptor.ClassVisibility);
        addColumn(ColumnDescriptor.ClassKeyword);
        addColumn(ColumnDescriptor.Extends);
        addColumn(ColumnDescriptor.MStereotype);
    }

    public Vector rowObjectsFor(Object t) {
        if (!(t instanceof UMLDeploymentDiagram || t instanceof MComponent)) return new Vector();
        if (t instanceof UMLDeploymentDiagram) {
            UMLDeploymentDiagram d = (UMLDeploymentDiagram) t;
            Vector nodes = d.getNodes();
            Vector res = new Vector();
            int size = nodes.size();
            for (int i = 0; i < size; i++) {
                Object node = nodes.elementAt(i);
                if (node instanceof MClass) res.addElement(node);
            }
            return res;
        } else {
            MComponent d = (MComponent) t;
            Vector res = new Vector();
            Collection elementResidences = d.getResidentElements();
            Iterator it = elementResidences.iterator();
            while (it.hasNext()) {
                MElementResidence residence = (MElementResidence) it.next();
                MModelElement node = (MModelElement) residence.getResident();
                if (node instanceof MClass) res.addElement(node);
            }
            return res;
        }
    }

    public String toString() {
        return "Classes vs. Properties";
    }
}
