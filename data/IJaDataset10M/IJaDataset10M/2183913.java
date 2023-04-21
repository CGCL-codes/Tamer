package org.cesar.flip.flipex.ajdt.extractors;

import java.util.LinkedList;
import java.util.List;
import org.cesar.flip.flipex.JavaRefactoringInfo;
import org.cesar.flip.flipex.ajdt.util.Advices;
import org.cesar.flip.flipex.ajdt.util.AspectsUtil;
import org.cesar.flip.flipex.ajdt.util.JoinPoints;
import org.cesar.flip.flipex.ajdt.validators.visitors.AttributeSetCollectorVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;

/**
 * Implements extraction of block of code from a method in a java class to an
 * before set advice.
 * 
 * @author Andrea Menezes (andrea.menezes@cesar.org.br)
 * 
 * 
 */
public class BeforeSetExtractor extends AJDTExtractor {

    @Override
    public void extract() throws CoreException {
        super.applyChanges();
    }

    @Override
    protected void writeAspect() throws CoreException {
        JavaRefactoringInfo info = (JavaRefactoringInfo) this.refactoringInfo;
        LinkedList<ASTNode> selectedNodes = info.getSelectedNodes();
        IVariableBinding fieldBinding = null;
        ASTNode firstSelectedNode = selectedNodes.getFirst();
        ASTNode parentOfSelectedNodes = firstSelectedNode.getParent();
        List<Statement> statement = null;
        if (parentOfSelectedNodes instanceof Block) {
            Block block = (Block) parentOfSelectedNodes;
            statement = block.statements();
        } else {
            SwitchStatement switchStatement = (SwitchStatement) parentOfSelectedNodes;
            statement = switchStatement.statements();
        }
        Statement statementAfterSelection = null;
        for (Object currentStatement : statement) {
            if (currentStatement == firstSelectedNode) {
                int index = statement.indexOf(currentStatement) + 1;
                int size = statement.size();
                if (index < size) {
                    statementAfterSelection = (Statement) statement.get(index);
                }
                break;
            }
        }
        AttributeSetCollectorVisitor visitor = new AttributeSetCollectorVisitor();
        statementAfterSelection.accept(visitor);
        fieldBinding = visitor.getFieldBinding();
        ASTNode node = firstSelectedNode.getParent();
        do {
            node = node.getParent();
        } while ((node != null) && !(node instanceof MethodDeclaration));
        MethodDeclaration methodDeclaration = (MethodDeclaration) node;
        String afterCallCode = AspectsUtil.generateAdviceString(Advices.BEFORE, JoinPoints.SET, super.getSelectedClass(), this.selectedNodes, methodDeclaration, fieldBinding);
        String aspectString = AspectsUtil.getAspectString(this.destination);
        String newAspectCode = AspectsUtil.updateAspectString(aspectString, afterCallCode);
        AspectsUtil.updateAspect(this.destination, newAspectCode);
        this.addReturnAndParametersTypesToNodesForImport(methodDeclaration);
    }

    /**
	 * Adds the return type and the parameter types to the nodes for import
	 * list.
	 * 
	 * @param methodDeclaration
	 *            the method declaration from which the return type and
	 *            parameter types will be acquired
	 */
    private void addReturnAndParametersTypesToNodesForImport(MethodDeclaration methodDeclaration) {
        super.addNodesForImport(methodDeclaration.getReturnType2());
        for (Object parameter : methodDeclaration.parameters()) {
            SingleVariableDeclaration parameterDeclaration = (SingleVariableDeclaration) parameter;
            super.addNodesForImport(parameterDeclaration.getType());
        }
    }
}
