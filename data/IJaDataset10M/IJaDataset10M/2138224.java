package deesel.parser.translation.translators;

import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.Instanceof;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.translation.TranslationVisitor;
import deesel.util.logging.Logger;
import java.io.PrintStream;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class InstanceofTranslator extends AbstractTranslator {

    private static Logger log = Logger.getLogger(InstanceofTranslator.class);

    public void translate(COMNode node, VisitorContext context, PrintStream out, TranslationVisitor translationVisitor) {
        Instanceof instanceofNode = (Instanceof) node;
        out.print(" ( ");
        instanceofNode.getExpression().accept(translationVisitor, context);
        out.print(" instanceof ");
        out.print(instanceofNode.getClassType().getFullName());
        out.print(") ");
    }
}
