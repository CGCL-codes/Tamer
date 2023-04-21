package net.sf.refactorit.audit.rules.j2se5;

import net.sf.refactorit.audit.AwkwardSourceConstruct;
import net.sf.refactorit.classmodel.BinVariable;
import net.sf.refactorit.classmodel.expressions.BinExpression;
import net.sf.refactorit.classmodel.expressions.BinMethodInvocationExpression;
import net.sf.refactorit.classmodel.expressions.BinVariableUseExpression;
import net.sf.refactorit.classmodel.statements.BinVariableDeclaration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juri Reinsalu
 */
public class ForinWhileIteratorViolation extends AwkwardSourceConstruct {

    private BinVariable iteratorVariable;

    private BinExpression iterableExpression;

    private BinMethodInvocationExpression nextCallExpression;

    private BinVariableDeclaration iteratorDeclStatement;

    private BinVariableUseExpression[] itemVarUsages;

    ForinWhileIteratorViolation(ForinWhileIteratorTraversalCandidateChecker checker) {
        super(checker.getWhileStatement(), "J2SE 5.0 construct for/in candidate", "refact.audit.forin");
        this.iteratorVariable = checker.getIteratorVariable();
        this.iterableExpression = checker.getIterableExpression();
        this.nextCallExpression = checker.getNextCallExpression();
        this.iteratorDeclStatement = checker.getIteratorDeclStatement();
        this.itemVarUsages = checker.getItemVarUsages();
    }

    public List getCorrectiveActions() {
        List list = new ArrayList(1);
        list.add(ForinWhileIteratorCorrectiveAction.getInstance());
        return list;
    }

    public BinExpression getIterableExpression() {
        return this.iterableExpression;
    }

    public BinMethodInvocationExpression getNextCallExpression() {
        return this.nextCallExpression;
    }

    public BinVariableDeclaration getIteratorDeclStatement() {
        return this.iteratorDeclStatement;
    }

    public BinVariableUseExpression[] getItemVarUsages() {
        return this.itemVarUsages;
    }
}
