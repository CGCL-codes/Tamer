package deesel.parser.parslet.builders;

import deesel.parser.ASTConstructorDeclaration;
import deesel.parser.ASTFormalParameters;
import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.DeclaredClass;
import deesel.parser.com.nodes.DeclaredConstructor;
import deesel.parser.com.nodes.DeeselClass;
import deesel.parser.parslet.Parslet;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class ConstructorBuilder extends AbstractCOMBuilder {

    private static Logger log = Logger.getLogger(ConstructorBuilder.class);

    public COMNode build(Parslet parslet, ASTNode node, COMNode parent) {
        log.debug("Building Constructor.");
        if (node.alreadyVisited() && parslet.lazyLoading()) {
            log.debug("Node already processed.");
            return null;
        }
        if (!parslet.lazyLoading()) {
            node.setVisited(true);
        }
        DeeselClass ownerClass = parent.getParentClass();
        DeclaredConstructor comNode = new DeclaredConstructor(node, ownerClass, ((ASTConstructorDeclaration) node).getModifiers());
        comNode.setParent(parent);
        addChild(parslet, node.getFirstChildOfType(ASTFormalParameters.class), comNode);
        if (!parslet.lazyLoading()) {
            addChildren(parslet, node, comNode);
            ((DeclaredClass) comNode.getParentClass()).removeDuplicateConstructor(comNode);
        }
        return comNode;
    }
}
