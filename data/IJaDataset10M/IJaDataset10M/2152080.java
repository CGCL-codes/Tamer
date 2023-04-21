package org.jmlspecs.eclipse.refactor.dom;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.jmlspecs.annotation.Pure;

public class JmlPostcondition extends JmlTypedNode<JmlSpecCase, IJavaElement> implements JmlStatement {

    /**
     * Construct a new JmlPostcondition.
     * 
     * @param node
     */
    public JmlPostcondition(JmlSpecCase parent, MethodInvocation node) {
        super(parent, null, node.getStartPosition(), node.getLength());
        isPostcondition(node);
        name = AstHelper.parentStatement(node).toString().trim();
    }

    @Override
    @Pure
    public String getName() {
        return name;
    }

    @Override
    protected void accept0(JmlAstVisitor visitor) {
        visitor.visit(this);
    }

    private final String name;

    /**
     * Is the MethodInvocation a JC.Ensures specification.
     * 
     * @param node
     *            the MethodInvocation to test for a postcondition.
     * @return true if the MethodInvocation a JC.Ensures specification.
     */
    public static boolean isPostcondition(MethodInvocation node) {
        return JcType.valueOfNode(node) == JcType.ENSURES;
    }
}
