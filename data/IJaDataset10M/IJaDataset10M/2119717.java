package awilkins.eclipse.coding.cleaning.ast;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Category;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;

/**
 *
 */
public class DelegatingCatchBlockASTVisitor extends AbstractASTVisitor {

    private static final Category log = Category.getInstance(DelegatingCatchBlockASTVisitor.class);

    private AbstractCatchBlockASTVisitor delegate;

    public DelegatingCatchBlockASTVisitor(CompilationUnit anASTCompilationUnit) {
        super(anASTCompilationUnit);
    }

    public boolean visit(TryStatement node) {
        if (hasVisited(node)) {
            return false;
        }
        try {
            List catchClauses = node.catchClauses();
            Iterator catches = catchClauses.iterator();
            while (catches.hasNext()) {
                CatchClause catchClause = (CatchClause) catches.next();
                SingleVariableDeclaration exceptionVariableDeclaration = catchClause.getException();
                Block catchBlock = catchClause.getBody();
                AbstractCatchBlockASTVisitor visitor = getDelegate();
                visitor.setExceptionVariableDeclaration(exceptionVariableDeclaration);
                catchBlock.accept(visitor);
            }
        } catch (Exception e) {
            log.error("Unexpected Exception @ " + formattedPosition(node), e);
        }
        return true;
    }

    /**
   * @return Returns the delegate.
   */
    public AbstractCatchBlockASTVisitor getDelegate() {
        return delegate;
    }

    /**
   * @param delegate
   *          The delegate to set.
   */
    public void setDelegate(AbstractCatchBlockASTVisitor visitor) {
        this.delegate = visitor;
    }
}
