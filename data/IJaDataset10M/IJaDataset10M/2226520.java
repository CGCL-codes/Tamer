package com.testrefactoring.ui;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import com.testrefactoring.util.NameUseageASTVisitor;

public class PromoteToSetUpProposal implements IJavaCompletionProposal {

    private final TypeDeclaration typeDef;

    private final List nodes;

    private MethodDeclaration setUpMethod;

    public static IJavaCompletionProposal get(List nodes, TypeDeclaration typeDef) {
        if (nodes.size() > 1) return new PromoteToSetUpProposal(nodes, typeDef);
        ASTNode node = (ASTNode) nodes.get(0);
        Statement statement = getStatement(node);
        if (statement != null) return new PromoteToSetUpProposal(Collections.singletonList(statement), typeDef);
        return null;
    }

    private PromoteToSetUpProposal(List nodes, TypeDeclaration typeDef) {
        this.nodes = nodes;
        this.typeDef = typeDef;
    }

    /**
	 * Inserts the proposed completion into the given document.
	 *
	 * @param document the document into which to insert the proposed completion
	 */
    public void apply(IDocument document) {
        ASTRewrite rewriter = ASTRewrite.create(typeDef.getAST());
        removeFromTestMethods(nodes, rewriter);
        refactorStatements(rewriter);
        TextEdit changes = rewriter.rewriteAST(document, null);
        try {
            changes.apply(document);
        } catch (final MalformedTreeException e) {
            e.printStackTrace();
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void refactorStatements(final ASTRewrite rewriter) {
        Statement statement = (Statement) nodes.get(0);
        List originalList = ((Block) statement.getParent()).statements();
        List postStatemnts = getListAfterIndex(originalList.indexOf(statement) + (nodes.size() - 1), originalList);
        Set usedNames = new NameUseageASTVisitor(postStatemnts).getNames();
        AST ast = typeDef.getAST();
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            final ASTNode node = (ASTNode) i.next();
            if (node instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement varDef2 = (VariableDeclarationStatement) ASTNode.copySubtree(ast, node);
                List modifiers = varDef2.modifiers();
                modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PRIVATE_KEYWORD));
                Iterator j = varDef2.fragments().iterator();
                while (j.hasNext()) {
                    VariableDeclarationFragment fragment = (VariableDeclarationFragment) j.next();
                    refactorFragment(j, rewriter, ast, fragment, varDef2, usedNames);
                }
                if (varDef2.fragments().size() > 0) {
                    ListRewrite lrw = rewriter.getListRewrite(typeDef, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
                    lrw.insertFirst(varDef2, null);
                }
            } else {
                ListRewrite lrw = rewriter.getListRewrite(getSetUpMethod(rewriter, typeDef).getBody(), Block.STATEMENTS_PROPERTY);
                lrw.insertLast(ASTNode.copySubtree(ast, node), null);
            }
        }
    }

    private List getListAfterIndex(int afterIndex, List originalList) {
        boolean indexIsLastElement = originalList.size() - 1 == afterIndex;
        if (indexIsLastElement) {
            return Collections.EMPTY_LIST;
        }
        return originalList.subList(afterIndex + 1, originalList.size());
    }

    public int getRelevance() {
        return 5;
    }

    protected MethodDeclaration createSetUpMethod(final ASTRewrite rewriter, AST ast) {
        MethodDeclaration setUpMethod = ast.newMethodDeclaration();
        setUpMethod.setName(ast.newSimpleName("setUp"));
        setUpMethod.thrownExceptions().add(ast.newSimpleName("Exception"));
        setUpMethod.setBody(ast.newBlock());
        List methodModifiers = setUpMethod.modifiers();
        methodModifiers.add(ast.newModifier(Modifier.ModifierKeyword.PROTECTED_KEYWORD));
        SuperMethodInvocation superMethodInvocation = ast.newSuperMethodInvocation();
        superMethodInvocation.setName(ast.newSimpleName("setUp"));
        setUpMethod.getBody().statements().add(ast.newExpressionStatement(superMethodInvocation));
        ListRewrite lrw = rewriter.getListRewrite(typeDef, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        lrw.insertFirst(setUpMethod, null);
        return setUpMethod;
    }

    protected void removeFromTestMethods(List nodes, ASTRewrite rewriter) {
        MethodDeclaration[] methodDecs = typeDef.getMethods();
        for (int j = 0; j < methodDecs.length; j++) {
            MethodDeclaration method = methodDecs[j];
            if (TestRefactoringQuickFixProvider.isTestMethod(method)) {
                Block body = method.getBody();
                if (body != null) {
                    removeFromMethod(rewriter, body, nodes);
                }
            }
        }
    }

    private void removeFromMethod(ASTRewrite rewriter, Block body, List nodes) {
        ListRewrite lrw = null;
        for (int i = body.statements().size() - 1; i >= 0; i--) {
            Statement s = (Statement) body.statements().get(i);
            Iterator j = nodes.iterator();
            while (j.hasNext()) {
                ASTNode node = (ASTNode) j.next();
                if (node.subtreeMatch(new ASTMatcher(), s)) {
                    if (lrw == null) {
                        lrw = rewriter.getListRewrite(body, Block.STATEMENTS_PROPERTY);
                    }
                    lrw.remove(s, null);
                    break;
                }
            }
        }
    }

    protected MethodDeclaration getSetUpMethod(ASTRewrite rewriter, TypeDeclaration typeDef) {
        if (setUpMethod == null) {
            MethodDeclaration[] methodDecs = typeDef.getMethods();
            for (int i = 0; i < methodDecs.length; i++) {
                MethodDeclaration method = methodDecs[i];
                if ("setUp".equals("" + method.getName()) && "void".equals("" + method.getReturnType2()) && method.parameters().size() == 0) {
                    setUpMethod = method;
                    break;
                }
            }
            if (setUpMethod == null) {
                setUpMethod = createSetUpMethod(rewriter, typeDef.getAST());
            }
        }
        return setUpMethod;
    }

    /**
	 * Returns the new selection after the proposal has been applied to
	 * the given document in absolute document coordinates. If it returns
	 * <code>null</code>, no new selection is set.
	 *
	 * A document change can trigger other document changes, which have
	 * to be taken into account when calculating the new selection. Typically,
	 * this would be done by installing a document listener or by using a
	 * document position during {@link #apply(IDocument)}.
	 *
	 * @param document the document into which the proposed completion has been inserted
	 * @return the new selection in absolute document coordinates
	 */
    public Point getSelection(IDocument document) {
        return null;
    }

    /**
	 * Returns optional additional information about the proposal.
	 * The additional information will be presented to assist the user
	 * in deciding if the selected proposal is the desired choice.
	 *
	 * @return the additional information or <code>null</code>
	 */
    public String getAdditionalProposalInfo() {
        return null;
    }

    /**
	 * Returns the string to be displayed in the list of completion proposals.
	 *
	 * @return the string to be displayed
	 */
    public String getDisplayString() {
        return "Promote to SetUp()";
    }

    /**
	 * Returns the image to be displayed in the list of completion proposals.
	 * The image would typically be shown to the left of the display string.
	 *
	 * @return the image to be shown or <code>null</code> if no image is desired
	 */
    public Image getImage() {
        return null;
    }

    /**
	 * Returns optional context information associated with this proposal.
	 * The context information will automatically be shown if the proposal
	 * has been applied.
	 *
	 * @return the context information for this proposal or <code>null</code>
	 */
    public IContextInformation getContextInformation() {
        return null;
    }

    private static Statement getStatement(ASTNode node) {
        if (node instanceof Statement) return (Statement) node;
        ASTNode parent = node.getParent();
        if (parent == null) return null;
        return getStatement(parent);
    }

    private void refactorFragment(Iterator fragItter, final ASTRewrite rewriter, AST ast, VariableDeclarationFragment fragment, VariableDeclarationStatement varStatment, Set usedNames) {
        boolean hasInitialiser = fragment.getInitializer() != null;
        SimpleName copiedName = (SimpleName) ASTNode.copySubtree(ast, fragment.getName());
        Expression copiedInitialiser = hasInitialiser ? (Expression) ASTNode.copySubtree(ast, fragment.getInitializer()) : null;
        if (usedNames.contains(fragment.getName().getIdentifier())) {
            if (hasInitialiser) {
                ListRewrite lrw = rewriter.getListRewrite(getSetUpMethod(rewriter, typeDef).getBody(), Block.STATEMENTS_PROPERTY);
                Assignment assignmentExp = ast.newAssignment();
                assignmentExp.setLeftHandSide(copiedName);
                assignmentExp.setOperator(Assignment.Operator.ASSIGN);
                if (copiedInitialiser instanceof ArrayInitializer) {
                    Type componentType = varStatment.getType();
                    ArrayCreation arrayCreation = ast.newArrayCreation();
                    arrayCreation.setType(ast.newArrayType((Type) ASTNode.copySubtree(ast, componentType)));
                    arrayCreation.setInitializer((ArrayInitializer) copiedInitialiser);
                    assignmentExp.setRightHandSide(arrayCreation);
                } else {
                    assignmentExp.setRightHandSide(copiedInitialiser);
                }
                lrw.insertLast(ast.newExpressionStatement(assignmentExp), null);
                fragment.setInitializer(null);
            }
        } else {
            VariableDeclarationFragment frag2 = (VariableDeclarationFragment) ASTNode.copySubtree(fragment.getAST(), fragment);
            ListRewrite lrw = rewriter.getListRewrite(getSetUpMethod(rewriter, typeDef).getBody(), Block.STATEMENTS_PROPERTY);
            VariableDeclarationStatement varDeclarationStatement = ast.newVariableDeclarationStatement(frag2);
            varDeclarationStatement.setType((Type) ASTNode.copySubtree(fragment.getAST(), ((VariableDeclarationStatement) fragment.getParent()).getType()));
            lrw.insertLast(varDeclarationStatement, null);
            fragItter.remove();
        }
    }
}
