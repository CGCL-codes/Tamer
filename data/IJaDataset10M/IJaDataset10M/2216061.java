package net.sf.refactorit.audit.rules;

import net.sf.refactorit.audit.AuditRule;
import net.sf.refactorit.audit.AwkwardSourceConstruct;
import net.sf.refactorit.classmodel.BinItemVisitable;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinSourceConstruct;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.statements.BinAssertStatement;
import net.sf.refactorit.classmodel.statements.BinBreakStatement;
import net.sf.refactorit.classmodel.statements.BinCITypesDefStatement;
import net.sf.refactorit.classmodel.statements.BinEmptyStatement;
import net.sf.refactorit.classmodel.statements.BinExpressionStatement;
import net.sf.refactorit.classmodel.statements.BinForStatement;
import net.sf.refactorit.classmodel.statements.BinIfThenElseStatement;
import net.sf.refactorit.classmodel.statements.BinLabeledStatement;
import net.sf.refactorit.classmodel.statements.BinLocalVariableDeclaration;
import net.sf.refactorit.classmodel.statements.BinReturnStatement;
import net.sf.refactorit.classmodel.statements.BinStatement;
import net.sf.refactorit.classmodel.statements.BinStatementList;
import net.sf.refactorit.classmodel.statements.BinSwitchStatement;
import net.sf.refactorit.classmodel.statements.BinSynchronizedStatement;
import net.sf.refactorit.classmodel.statements.BinThrowStatement;
import net.sf.refactorit.classmodel.statements.BinTryStatement;
import net.sf.refactorit.classmodel.statements.BinWhileStatement;
import net.sf.refactorit.classmodel.statements.BinSwitchStatement.CaseGroup;
import net.sf.refactorit.query.BinItemVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Switch Case Fallthrough audit
 *
 * @author Villu Ruusmann
 * @author Daniel Wilken Damm (implementing corrective actions)
 */
public class SwitchCaseFallthroughRule extends AuditRule {

    public static final String NAME = "switch_case";

    public void visit(BinSwitchStatement statement) {
        BinSwitchStatement.CaseGroup[] groups = statement.getCaseGroupList();
        StatementFlowAnalyzer analyzer = new StatementFlowAnalyzer(statement);
        for (int i = 0; i < groups.length; i++) {
            groups[i].getStatementList().accept(analyzer);
            if (!analyzer.success) {
                addViolation(new SwitchCaseFallthrough(groups[i]));
            }
            analyzer.reset();
        }
        super.visit(statement);
    }

    /**
   * Ensures that every {@link BinStatementList} terminates abruptly.
   */
    static class StatementFlowAnalyzer extends BinItemVisitor {

        boolean success = true;

        private BinStatement root;

        StatementFlowAnalyzer(BinStatement root) {
            setRoot(root);
        }

        void reset() {
            this.success = true;
        }

        public void visit(BinLocalVariableDeclaration statement) {
            this.success = false;
        }

        public void visit(BinLabeledStatement statement) {
            this.success = false;
        }

        public void visit(BinAssertStatement statement) {
            this.success = false;
        }

        public void visit(BinBreakStatement statement) {
            BinStatement target = statement.getBreakTarget();
            if (getRoot().contains(target) && !getRoot().equals(target)) {
                this.success = false;
            }
        }

        public void visit(BinCITypesDefStatement statement) {
            this.success = false;
        }

        public void visit(BinEmptyStatement statement) {
            this.success = false;
        }

        public void visit(BinExpressionStatement statement) {
            this.success = false;
        }

        public void visit(BinForStatement statement) {
            this.success = false;
        }

        public void visit(BinIfThenElseStatement statement) {
            super.visit(statement);
        }

        public void visit(BinReturnStatement statement) {
        }

        public void visit(BinStatementList statement) {
            if (!this.success) {
                return;
            }
            BinStatement[] array = statement.getStatements();
            if ((array == null) || (array.length == 0)) {
                this.success = false;
                return;
            }
            array[array.length - 1].accept(this);
        }

        public void visit(BinSwitchStatement statement) {
            super.visit(statement);
        }

        public void visit(BinSynchronizedStatement statement) {
            super.visit(statement);
        }

        public void visit(BinThrowStatement statement) {
            BinTypeRef throwableRef = (statement.getExpression()).getReturnType();
            BinItemVisitable interim = statement;
            while (interim instanceof BinSourceConstruct && interim != getRoot()) {
                if (interim instanceof BinTryStatement) {
                    if (throwableIsCatched((BinTryStatement) interim, throwableRef)) {
                        this.success = false;
                        return;
                    }
                }
                interim = interim.getParent();
            }
        }

        public void visit(BinTryStatement statement) {
            super.visit(statement);
        }

        public void visit(BinWhileStatement statement) {
            this.success = false;
        }

        private BinStatement getRoot() {
            return this.root;
        }

        private void setRoot(BinStatement root) {
            this.root = root;
        }

        private static boolean throwableIsCatched(BinTryStatement statement, BinTypeRef throwableRef) {
            BinTryStatement.CatchClause[] catches = statement.getCatches();
            for (int i = 0; i < catches.length; i++) {
                BinTypeRef parameterRef = catches[i].getParameter().getTypeRef();
                if (throwableRef.equals(parameterRef)) {
                    return true;
                }
                if (throwableRef.isDerivedFrom(parameterRef)) {
                    return true;
                }
            }
            return false;
        }
    }
}

class SwitchCaseFallthrough extends AwkwardSourceConstruct {

    SwitchCaseFallthrough(CaseGroup group) {
        super(group.getCaseList()[0], "Case group falls through", "refact.audit.switch_fallthrough");
        setTargetItem(group.getStatementList());
    }

    public BinMember getSpecificOwnerMember() {
        return getStatementList().getParentMember();
    }

    BinStatementList getStatementList() {
        return (BinStatementList) getTargetItem();
    }

    public List getCorrectiveActions() {
        List result = new ArrayList(2);
        result.add(InsertBreakAction.instance);
        result.add(InsertFallthroughCommentAction.instance);
        return result;
    }
}

class InsertBreakAction extends InsertLineAction {

    static final InsertBreakAction instance = new InsertBreakAction();

    public InsertBreakAction() {
        super.textToInsert = "break;";
    }

    public String getKey() {
        return "refactorit.audit.action.insert.break";
    }

    public String getName() {
        return "Insert break";
    }

    public String getMultiTargetName() {
        return "Insert break(s)";
    }
}

class InsertFallthroughCommentAction extends InsertLineAction {

    static final InsertFallthroughCommentAction instance = new InsertFallthroughCommentAction();

    public InsertFallthroughCommentAction() {
        super.textToInsert = "/* CAUTION: Case group falls through! */";
    }

    public String getKey() {
        return "refactorit.audit.action.switch_case.insert_comment";
    }

    public String getName() {
        return "Mark with comment";
    }

    public String getMultiTargetName() {
        return "Mark with comment(s)";
    }
}
