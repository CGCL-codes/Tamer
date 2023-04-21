package net.sf.orcc.backends.transformations.threeAddressCodeTransformation;

import net.sf.orcc.ir.ExprVar;
import net.sf.orcc.ir.InstAssign;
import net.sf.orcc.ir.Procedure;
import net.sf.orcc.ir.Use;
import net.sf.orcc.ir.Var;
import net.sf.orcc.ir.util.AbstractActorVisitor;
import net.sf.orcc.ir.util.EcoreHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Replace occurrences with direct assignments to their corresponding values. A
 * direct assignment is an assign instruction of form x = y, which simply
 * assigns the value of y to x.
 * 
 * @author Jerome GORIN
 * 
 */
public class CopyPropagator extends AbstractActorVisitor<Object> {

    protected boolean changed;

    @Override
    public Object caseProcedure(Procedure procedure) {
        do {
            changed = false;
            super.caseProcedure(procedure);
        } while (changed);
        return null;
    }

    @Override
    public Object caseInstAssign(InstAssign assign) {
        if (assign.getValue().isVarExpr()) {
            Var source = ((ExprVar) assign.getValue()).getUse().getVariable();
            Var target = assign.getTarget().getVariable();
            EList<Use> targetUses = target.getUses();
            if (!targetUses.isEmpty()) {
                changed = true;
                while (!targetUses.isEmpty()) {
                    targetUses.get(0).setVariable(source);
                }
                EcoreUtil.remove(target);
                EcoreHelper.delete(assign);
                indexInst--;
            }
        }
        return null;
    }
}
