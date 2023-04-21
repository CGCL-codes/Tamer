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
public class StringLiteral extends AbstractCOMNode implements COMNode {

    private static Logger log = Logger.getLogger(StringLiteral.class);

    private String string;

    public StringLiteral(ASTNode astNode, String s) {
        super(astNode);
        this.string = s;
    }

    public DeeselClass getType() {
        return ClassDefFactory.STRING;
    }

    public COMNode[] getAllChildren() {
        return new COMNode[0];
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public String getString() {
        return string;
    }
}
