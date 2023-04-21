package org.eclipse.xtext.example.linker;

import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.linking.impl.DefaultLinkingService;
import org.eclipse.xtext.linking.impl.IllegalNodeException;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;

/**
 * @author bettini
 * 
 */
public class FJLinkingService extends DefaultLinkingService {

    @Override
    public List<EObject> getLinkedObjects(EObject context, EReference ref, INode node) throws IllegalNodeException {
        List<EObject> linkedObjects = super.getLinkedObjects(context, ref, node);
        if (ref.getName().equals("type") || ref.getName().equals("extends") || ref.getName().equals("classref")) {
            final String s = ((ILeafNode) node).getText();
            if (s.equals("Object")) return Collections.<EObject>singletonList(FJLinkingResource.getObjectClass(context));
        }
        return linkedObjects;
    }
}
