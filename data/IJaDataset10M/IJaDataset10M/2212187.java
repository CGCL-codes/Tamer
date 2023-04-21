package deesel.parser.translation.translators;

import deesel.parser.com.COMNode;
import deesel.parser.com.nodes.*;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.exceptions.GeneralParserFailureException;
import deesel.parser.translation.TranslationVisitor;
import deesel.util.logging.Logger;
import java.io.PrintStream;

/**
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 */
public class PrimaryExpressionTranslator extends AbstractTranslator {

    private static Logger log = Logger.getLogger(PrimaryExpressionTranslator.class);

    public void translate(COMNode node, VisitorContext context, PrintStream out, TranslationVisitor translationVisitor) {
        log.debug("Translating PrimaryExpression.");
        PrimaryExpression expression = (PrimaryExpression) node;
        PrimaryExpressionComponent[] components = expression.getComponents();
        Operator[] operators = expression.getOperators();
        if (log.isDebugEnabled()) log.debug("operators.length= " + operators.length);
        int end;
        if (node.getParent() instanceof AssignmentExpression && expression.getLastNode() instanceof DynamicMemberReference) {
            end = components.length - 2;
        } else {
            end = components.length - 1;
        }
        for (int i = end; i >= 0; i--) {
            if (components[i] instanceof DynamicMemberReference) {
                out.print("deesel.lang.runtime.Invoker.getInstance().get(");
            } else if (components[i] instanceof DynamicMethodCall) {
                out.print("deesel.lang.runtime.Invoker.getInstance().invoke(");
            } else if (components[i] instanceof Subscript && ((Subscript) components[i]).getSubscriptExpressions().length > 1) {
                out.print("deesel.lang.runtime.Operators.multipleSubscript(");
            }
            if (components[i] instanceof MethodCall && ((MethodCall) components[i]).isUsesExtension()) {
                out.print(((MethodCall) components[i]).getMethodUsed().getDeclaringClass().getFullName() + ".getInstance()." + ((MethodCall) components[i]).getMethodUsed().getName() + "(");
                if (i == 0) {
                    out.print("this");
                }
            }
        }
        for (int i = 0; i <= end; i++) {
            PrimaryExpressionComponent component = components[i];
            if (component instanceof DynamicMethodCall || component instanceof DynamicMemberReference) {
                out.print(", ");
                component.accept(translationVisitor, context);
                out.print(")");
            } else if (components[i] instanceof MethodCall && ((MethodCall) components[i]).isUsesExtension()) {
                if (((MethodCall) components[i]).getMethodUsed().getParameterTypes().length > 1) {
                    out.print(", ");
                }
                component.accept(translationVisitor, context);
            } else if (components[i] instanceof MethodCall) {
                if (i > 0) {
                    out.print(".");
                }
                out.print(((MethodCall) components[i]).getMethodUsed().getName());
                out.print("(");
                component.accept(translationVisitor, context);
            } else if (component instanceof Subscript) {
                DeeselClass type = components[i - 1].getType();
                if (((Subscript) component).getSubscriptExpressions().length == 1) {
                    if (type.isArray()) {
                        out.print("[");
                        ((Subscript) component).getSubscriptExpressions()[0].accept(translationVisitor, context);
                        out.print("]");
                    } else if (type.isInstance(ClassDefFactory.LIST)) {
                        out.print(".get(");
                        ((Subscript) component).accept(translationVisitor, context);
                        out.print(")");
                    } else {
                        try {
                            type.getMethod("get", new DeeselClass[] { ClassDefFactory.OBJECT });
                            out.print(".get(");
                            ((Subscript) component).accept(translationVisitor, context);
                            out.print(")");
                        } catch (NoSuchMethodException e) {
                            throw new GeneralParserFailureException(e);
                        }
                    }
                } else {
                    out.print(",");
                    component.accept(translationVisitor, context);
                    out.print(")");
                }
            } else {
                if (i > 0) {
                    out.print(".");
                }
                component.accept(translationVisitor, context);
            }
        }
    }
}
