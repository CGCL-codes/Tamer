package org.rubypeople.rdt.refactoring.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.jruby.ast.Colon3Node;
import org.jruby.ast.IterNode;
import org.jruby.ast.MethodDefNode;
import org.jruby.ast.Node;
import org.jruby.lexer.yacc.IDESourcePosition;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.parser.StaticScope;

public class NodeUtil {

    public static boolean hasScope(Node node) {
        Method[] methods = node.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("getScope") || methods[i].equals("getStaticScope")) {
                return true;
            }
        }
        return false;
    }

    public static Node getBody(Node node) {
        try {
            Method method = node.getClass().getMethod("getBodyNode", new Class[] {});
            return (Node) method.invoke(node, new Object[] {});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StaticScope getScope(Node node) {
        String methodName = "getStaticScope";
        if (node instanceof MethodDefNode || node instanceof IterNode) {
            methodName = "getScope";
        }
        try {
            Method method = node.getClass().getMethod(methodName, new Class[] {});
            return (StaticScope) method.invoke(node, new Object[] {});
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean nodeAssignableFrom(Node n, Class... klasses) {
        if (n == null) {
            return false;
        }
        for (Class<?> klass : klasses) {
            if (klass.isAssignableFrom(n.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static ISourcePosition subPositionUnion(Node node) {
        ISourcePosition enclosingPosition = node.getPositionIncludingComments();
        List<Node> childList = node.childNodes();
        for (Node currentChild : childList) {
            enclosingPosition = posUnion(enclosingPosition, subPositionUnion(currentChild));
        }
        return enclosingPosition;
    }

    private static ISourcePosition posUnion(ISourcePosition firstPos, ISourcePosition secondPos) {
        String fileName = firstPos.getFile();
        int startOffset = firstPos.getStartOffset();
        int endOffset = firstPos.getEndOffset();
        int startLine = firstPos.getStartLine();
        int endLine = firstPos.getEndLine();
        if (startOffset > secondPos.getStartOffset()) {
            startOffset = secondPos.getStartOffset();
            startLine = secondPos.getStartLine();
        }
        if (endOffset < secondPos.getEndOffset()) {
            endOffset = secondPos.getEndOffset();
            endLine = secondPos.getEndLine();
        }
        return new IDESourcePosition(fileName, startLine, endLine, startOffset, endOffset);
    }

    public static boolean positionIsInNode(int offset, Colon3Node path) {
        return offset >= path.getPosition().getStartOffset() && offset <= path.getPosition().getEndOffset();
    }
}
