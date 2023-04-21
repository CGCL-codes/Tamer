package deesel.parser.translation.translators;

import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.ClassNameReference;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.translation.TranslationVisitor;
import deesel.util.logging.Logger;
import java.io.PrintStream;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class ClassNameReferenceTranslator extends AbstractTranslator {

    private static Logger log = Logger.getLogger(ClassNameReferenceTranslator.class);

    public void translate(COMNode node, VisitorContext context, PrintStream out, TranslationVisitor translationVisitor) {
        ClassNameReference theClassNameReference = (ClassNameReference) node;
        out.print(theClassNameReference.getType().getFullName());
    }
}
