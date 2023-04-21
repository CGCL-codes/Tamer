package com.google.gdt.eclipse.designer.model.widgets.panels;

import org.eclipse.wb.core.eval.EvaluationContext;
import org.eclipse.wb.core.eval.ExecutionFlowUtils.ExecutionFlowFrameVisitor;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.broadcast.JavaInfosetObjectBefore;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.utils.ast.AstNodeUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * {@link CreationSupport} for <code>RootLayoutPanel.get()</code>.
 * 
 * @author scheglov_ke
 * @coverage gwt.model
 */
public final class RootLayoutPanelCreationSupport extends CreationSupport {

    private final MethodInvocation m_invocation;

    public RootLayoutPanelCreationSupport(MethodInvocation invocation) {
        m_invocation = invocation;
    }

    @Override
    public String toString() {
        return "RootLayoutPanel.get()";
    }

    @Override
    public ASTNode getNode() {
        return m_invocation;
    }

    @Override
    public boolean isJavaInfo(ASTNode node) {
        return AstNodeUtils.isMethodInvocation(node, "com.google.gwt.user.client.ui.RootLayoutPanel", "get()");
    }

    @Override
    public void setJavaInfo(JavaInfo javaInfo) throws Exception {
        super.setJavaInfo(javaInfo);
        m_javaInfo.addBroadcastListener(new JavaInfosetObjectBefore() {

            public void invoke(JavaInfo target, Object[] objectRef) throws Exception {
                if (target == m_javaInfo) {
                    objectRef[0] = evaluate();
                }
            }
        });
    }

    @Override
    public boolean canBeEvaluated() {
        return false;
    }

    @Override
    public Object create(EvaluationContext context, ExecutionFlowFrameVisitor visitor) throws Exception {
        return evaluate();
    }

    private Object evaluate() throws Exception {
        return ((RootLayoutPanelInfo) m_javaInfo).getUIObjectUtils().getRootLayoutPanel();
    }
}
