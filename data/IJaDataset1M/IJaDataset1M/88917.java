package deesel.parser.parslet.builders;

import deesel.parser.ASTName;
import deesel.parser.ASTNode;
import deesel.parser.Token;
import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.DeeselPackage;
import deesel.parser.parslet.Parslet;
import deesel.util.logging.Logger;
import java.util.ArrayList;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class DeeselPackageBuilder extends AbstractCOMBuilder {

    private static Logger log = Logger.getLogger(DeeselPackageBuilder.class);

    public COMNode build(Parslet parslet, ASTNode node, COMNode context) {
        log.debug("Building DeeselPackage.");
        ASTName astName = (ASTName) node.getFirstChild();
        ArrayList packages = new ArrayList();
        for (Token t = astName.getFirstToken(); t != astName.getLastToken().next; t = t.next) {
            final String name = t.image;
            if (t.image.equals(".")) {
            } else {
                packages.add(name);
                if (log.isDebugEnabled()) log.debug("Building import from ASTImportDeclaration - package added is: " + name);
            }
        }
        DeeselPackage comNode = new DeeselPackage(node, packages);
        comNode.setParent(context);
        node.setVisited(true);
        return comNode;
    }
}
