package net.sf.refactorit.refactorings;

import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.statements.BinReturnStatement;
import net.sf.refactorit.classmodel.statements.BinStatementList;
import net.sf.refactorit.query.BinItemVisitor;
import java.util.ArrayList;
import java.util.List;

public final class RefactoringUtil {

    private RefactoringUtil() {
    }

    /**
   * @param method BinMethod to find return statements for
   * @return List of all BinReturnStatements for given method
   */
    public static List findReturnStatementsForMethod(BinMethod method) {
        class ReturnStatementsFinder extends BinItemVisitor {

            public ReturnStatementsFinder() {
            }

            public List returnStatementList = new ArrayList();

            public void visit(BinReturnStatement x) {
                returnStatementList.add(x);
                super.visit(x);
            }

            public List getReturnStatementList() {
                return this.returnStatementList;
            }
        }
        ;
        ReturnStatementsFinder visitor = null;
        visitor = new ReturnStatementsFinder();
        BinStatementList body = method.getBody();
        if (body != null) {
            body.accept(visitor);
        }
        return visitor.getReturnStatementList();
    }
}
