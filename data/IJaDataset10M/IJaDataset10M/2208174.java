package org.jdeluxe.views;

import java.util.Vector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;

/**
 * The Class XsdContentProvider.
 */
public class XsdContentProvider implements ITreeContentProvider {

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public Object[] getElements(Object inputElement) {
        return new Object[] { inputElement };
    }

    public Object[] getChildren(Object parentElement) {
        Vector<Object> out = new Vector<Object>();
        out.add("a");
        if (parentElement instanceof XSDElementDeclaration) {
            XSDElementDeclaration dec = (XSDElementDeclaration) parentElement;
            out.add(dec.getAnnotation());
        }
        if (parentElement instanceof XSDTypeDefinition) {
            XSDTypeDefinition type = (XSDTypeDefinition) parentElement;
            EList<XSDAnnotation> anos = type.getAnnotations();
            for (XSDAnnotation a : anos) {
                out.add(a);
            }
            XSDSimpleTypeDefinition st = type.getSimpleType();
            XSDParticle ct = type.getComplexType();
            if (st != null) {
                out.add(st);
                out.add(st.getMinFacet());
                out.add(st.getMaxFacet());
            } else if (ct != null) {
                out.add(ct);
                out.add(ct.getMinOccurs());
                out.add(ct.getMaxOccurs());
                out.add(ct.getElement().getAttributes());
            }
        }
        if (out.size() > 0) {
            return out.toArray();
        }
        return null;
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return false;
    }
}
