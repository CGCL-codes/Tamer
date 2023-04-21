package deesel.parser.parslet.builders;

import deesel.parser.ASTNode;
import deesel.parser.ASTType;
import deesel.parser.Token;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.DeeselPackage;
import deesel.parser.com.nodes.Type;
import deesel.parser.parslet.Parslet;
import deesel.util.logging.Logger;
import java.util.ArrayList;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class TypeBuilder extends AbstractCOMBuilder {

    private static Logger log = Logger.getLogger(TypeBuilder.class);

    public COMNode build(Parslet parslet, ASTNode node, COMNode parent) {
        log.debug("Building Type.");
        Type type;
        if (node instanceof ASTType) {
            type = new Type((ASTType) node);
        } else {
            ArrayList packages = new ArrayList();
            for (Token t = node.getFirstToken(); t != node.getLastToken().next; t = t.next) {
                if (!t.image.equals(".")) {
                    packages.add(t.image);
                }
            }
            String className = (String) packages.remove(packages.size() - 1);
            DeeselPackage deeselPackage = new DeeselPackage(node, packages);
            type = new Type(deeselPackage, className);
        }
        type.setParent(parent);
        if (!parslet.lazyLoading()) {
            node.setVisited(true);
        }
        return type;
    }
}
