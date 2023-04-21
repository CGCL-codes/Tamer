package deesel.parser.parslet.builders;

import deesel.parser.ASTDoStatement;
import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.DoWhile;
import deesel.parser.parslet.Parslet;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class DoWhileBuilder extends AbstractCOMBuilder {

    private static Logger log = Logger.getLogger(DoWhileBuilder.class);

    public COMNode build(Parslet parslet, ASTNode node, COMNode context) {
        log.debug("Building DoWhile.");
        DoWhile comNode = new DoWhile((ASTDoStatement) node);
        comNode.setParent(context);
        addChildren(parslet, node, comNode);
        node.setVisited(true);
        return comNode;
    }
}
