package net.sourceforge.mezzo.plg.outline;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.common.editor.outline.ContentOutlineNode;
import org.eclipse.xtext.ui.common.editor.outline.transformer.AbstractDeclarativeSemanticModelTransformer;

/**
 * customization of the default outline structure
 * 
 */
public class ManuscriptTransformer extends AbstractDeclarativeSemanticModelTransformer {

    public ContentOutlineNode createNode(net.sourceforge.mezzo.plg.manuscript.Node semanticNode, ContentOutlineNode parentNode) {
        return parentNode;
    }

    public List<EObject> getChildren(net.sourceforge.mezzo.plg.manuscript.Method method) {
        return NO_CHILDREN;
    }
}
