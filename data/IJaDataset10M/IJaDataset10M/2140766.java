package deesel.parser.translation.translators;

import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.Expression;
import deesel.parser.com.nodes.DeeselClass;
import deesel.parser.com.nodes.MethodCall;
import deesel.parser.com.nodes.NullClass;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.translation.TranslationVisitor;
import deesel.util.logging.Logger;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class MethodCallTranslator extends AbstractTranslator {

    private static Logger log = Logger.getLogger(MethodCallTranslator.class);

    public void translate(COMNode node, VisitorContext context, PrintStream out, TranslationVisitor translationVisitor) {
        MethodCall call = (MethodCall) node;
        Expression[] params = call.getParameters();
        List destParamTypes = new ArrayList(Arrays.asList(call.getMethodUsed().getParameterTypes()));
        if (call.isUsesExtension()) {
            destParamTypes.remove(0);
        }
        DeeselClass[] paramTypes = (DeeselClass[]) destParamTypes.toArray(new DeeselClass[destParamTypes.size()]);
        for (int i = 0; i < params.length; i++) {
            DeeselClass parameterType = params[i].getType();
            if (!parameterType.isPrimitive() && paramTypes[i].isPrimitive()) {
                translateChild(params[i], translationVisitor, context);
                out.print(".");
                out.print(paramTypes[i]);
                out.print("Value()");
            } else {
                if (!paramTypes[i].isPrimitive() && parameterType.isPrimitive()) {
                    out.print("(new ");
                    out.print(params[i].getType().getAutoboxType().getFullName());
                    out.print("(");
                    translateChild(params[i], translationVisitor, context);
                    out.print("))");
                } else {
                    if (!(parameterType instanceof NullClass)) {
                        out.print("(");
                        out.print(paramTypes[i].getFullName());
                        out.print(")");
                    }
                    translateChild(params[i], translationVisitor, context);
                }
            }
            if (i < params.length - 1) {
                out.print(", ");
            }
        }
        out.print(")");
    }
}
