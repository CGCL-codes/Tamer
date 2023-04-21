package org.jnormalform.detectors;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.jnormalform.ASTUtil;
import org.jnormalform.Context;
import org.jnormalform.FlattenedStatement;
import org.jnormalform.MethodMatcher;
import org.jnormalform.plugin.preferences.Severity;
import org.jnormalform.refactoringextensionpoint.IRefactor;
import org.jnormalform.refactoringextensionpoint.InterestedInVisiting;
import org.jnormalform.refactorings.CallExistingMethod;
import org.jnormalform.refactorings.IntroduceMethod;
import org.jnormalform.visitor.ParamterFinderVisitor;

/**
 * This is the main refactoring point for removal of duplicate statements.
 * Creates the following IRefactor:
 * 		IntroduceMethod
 * 		IntroduceStaticMethod
 * 
 * Tested by IntroduceMethodFactoryTest
 * Tested by IntroduceMethodFactoryUnitTest
 * @author GilesCope
 */
public class IntroduceMethodDetector extends BaseRefactoringDetector {

    public IRefactor canDo(final Context ctx, final ASTNode astNode, boolean start) {
        final Block block1 = (Block) astNode;
        final List statements = block1.statements();
        int statementsSize = statements.size();
        if (statementsSize < 2 || !ctx.hasCurrentType()) {
            return null;
        }
        FlattenedStatement prevStmnt = new FlattenedStatement((Statement) statements.get(0));
        IRefactor result = findExistingMethod(ctx, prevStmnt, block1, 0);
        if (result != null) return result;
        for (int index = 1; index < statementsSize; index++) {
            final FlattenedStatement stmnt = new FlattenedStatement((Statement) statements.get(index));
            result = findExistingMethod(ctx, stmnt, block1, index);
            if (result != null) return result;
            final Statement nextStatementMatch = getBestMatch(ctx, stmnt.getStatement(), ctx.getSightings(prevStmnt, stmnt));
            if (nextStatementMatch != null && true) {
                final AbstractTypeDeclaration matchTypeDec = ASTUtil.getDeclaringClass(nextStatementMatch);
                final AbstractTypeDeclaration currentTypeDec = ctx.getCurrentType();
                final Block block2 = (Block) nextStatementMatch.getParent();
                return new IntroduceMethod(severity, ctx, block1, index - 1, block2, block2.statements().indexOf(nextStatementMatch) - 1, !currentTypeDec.equals(matchTypeDec));
            }
            prevStmnt = stmnt;
        }
        return null;
    }

    private IRefactor findExistingMethod(final Context ctx, FlattenedStatement prevStmnt, Block block, int pos) {
        final ASTNode parent = block.getParent();
        final List list = ctx.getPossibleMatchingMethodDeclarations(prevStmnt.getFlattenedRepresentation());
        Iterator i = list.iterator();
        while (i.hasNext()) {
            MethodDeclaration methodDec = (MethodDeclaration) i.next();
            if (parent == methodDec || !IntroduceMethod.isCallableFunction(methodDec, block)) continue;
            final ParamterFinderVisitor paramterFinderVisitor = new ParamterFinderVisitor(methodDec);
            paramterFinderVisitor.apply();
            final MethodMatcher methodMatcher = new MethodMatcher(paramterFinderVisitor);
            Statement statement = methodMatcher.isInstanceOfMethod(block, pos);
            if (shouldCallExistingMethod(methodDec, statement)) {
                return new CallExistingMethod(severity, block, pos, methodDec.getBody().statements().size(), statement);
            }
        }
        return null;
    }

    private boolean shouldCallExistingMethod(MethodDeclaration methodDec, Statement statement) {
        return statement != null && methodDec.getBody().toString().indexOf("super") < 0;
    }

    private Statement getBestMatch(final Context ctx, final Statement stmnt, final List sightings) {
        if (sightings == null) return null;
        final AbstractTypeDeclaration currentTypeDec = ctx.getCurrentType();
        final Iterator i = sightings.iterator();
        Statement lastAcceptable = null;
        while (i.hasNext()) {
            final Statement sighted = (Statement) i.next();
            if (sighted == stmnt) {
                continue;
            }
            final AbstractTypeDeclaration matchTypeDec = ASTUtil.getDeclaringClass(sighted);
            if (currentTypeDec.equals(matchTypeDec)) {
                return sighted;
            }
            lastAcceptable = sighted;
        }
        return lastAcceptable;
    }

    public List getASTNodeTypesToVisit() {
        return Collections.singletonList(new InterestedInVisiting(Block.class));
    }

    public String getExampleAfter() {
        return "class C { void M() { for(int ii = 0; ii < 2; ii++) m(); }\r\n\r\nprivate void m() {\r\n\tSystem.out.println(\"Hi\");  System.out.println(\"world\");\r\n} }";
    }

    public String getExampleBefore() {
        return "class C { void M() { System.out.println(\"Hi\"); System.out.println(\"world\"); System.out.println(\"Hi\");  System.out.println(\"world\"); } }";
    }

    public String getName() {
        return "introduceMethod";
    }

    public String getDefaultSeverity() {
        return Severity.IGNORE;
    }

    public String getDetail() {
        return "[WIP] Detect potential Extract Method refactorings";
    }
}
