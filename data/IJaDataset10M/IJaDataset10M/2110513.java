package deesel.parser.com.nodes;

import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.util.logging.Logger;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class ModuleUnit extends AbstractCOMNode {

    private static Logger log = Logger.getLogger(ModuleUnit.class);

    public ModuleUnit(ASTNode node) {
        super(node);
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public DeeselClass getType() {
        return ClassDefFactory.VOID;
    }

    public COMNode[] getAllChildren() {
        return new COMNode[0];
    }
}
