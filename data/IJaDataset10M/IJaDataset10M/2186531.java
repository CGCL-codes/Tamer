package net.sf.refactorit.query.usage;

import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinLocalVariable;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.expressions.BinVariableUseExpression;
import net.sf.refactorit.classmodel.statements.BinTryStatement;
import net.sf.refactorit.query.BinItemVisitor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Unused Variables are of types
 *  BinLocalVariable
 *   BinParameter
 *   BinCatchParameter
 *
 * @author  sander
 */
public final class UnusedVariablesIndexer extends BinItemVisitor {

    private final Set resultSet;

    private final BinMember member;

    /** Creates a new instance of UnusedVariablesIndexer */
    private UnusedVariablesIndexer(final Set resultSet, final BinMember member) {
        this.resultSet = resultSet;
        this.member = member;
    }

    public final void visit(final BinMethod aMethod) {
        super.visit(aMethod);
    }

    public final void visit(final BinConstructor aConstructor) {
        resultSet.addAll(Arrays.asList(aConstructor.getParameters()));
        super.visit(aConstructor);
    }

    public final void visit(final BinTryStatement.CatchClause catchClause) {
        resultSet.add(catchClause.getParameter());
        super.visit(catchClause);
    }

    public final void visit(final BinLocalVariable var) {
        if (var instanceof BinParameter) {
            if (("main".equals(member.getName()) && member.isStatic()) || member.isAbstract() || member.getOwner().getBinCIType().isInterface()) {
                return;
            }
        }
        resultSet.add(var);
        super.visit(var);
    }

    public final void visit(final BinVariableUseExpression varUse) {
        resultSet.remove(varUse.getVariable());
        super.visit(varUse);
    }

    public static BinLocalVariable[] getUnusedVariablesFor(final BinMember aMember) {
        final Set result = new HashSet();
        aMember.accept(new UnusedVariablesIndexer(result, aMember));
        return (BinLocalVariable[]) result.toArray(new BinLocalVariable[0]);
    }
}
