package domain.simplex.control;

import domain.model.Variable;
import domain.simplex.ColIterator;
import domain.simplex.OpMax;
import domain.simplex.OpMin;
import domain.simplex.OptimizerCriteriaVisitor;
import domain.model.numbers.Number;
import domain.model.numbers.NumberModel;
import domain.simplex.RowIterator;
import domain.simplex.VariableRepository;
import java.util.Iterator;
import java.util.Vector;

public class SensitivityAnalizator implements OptimizerCriteriaVisitor {

    static final SensitivityAnalizator instance = new SensitivityAnalizator();

    private ResolverControl rc;

    private SensitivityAnalizys sa;

    public static synchronized SensitivityAnalizys getSentivityReport(ResolverControl rc) {
        instance.rc = rc;
        instance.sa = new SensitivityAnalizys(rc.getVariableRepository().getRealCount());
        rc.getFunctional().getOp().accept(instance);
        return instance.sa;
    }

    private SensitivityAnalizator() {
        this.rc = null;
    }

    public void visit(OpMax opMax) {
        ColIterator colVars = rc.colVarsIterator();
        VarInformation vi;
        Iterator<VarRelation> iter;
        while (colVars.hasNext()) {
            Variable v = colVars.next();
            Number functionalCoef = rc.getVarFunctionalCoef(v);
            if (rc.getVariableRepository().isReal(v)) {
                if (rc.isVarInBase(v)) {
                    Vector<Number> hvalues = new Vector<Number>();
                    Vector<Number> lvalues = new Vector<Number>();
                    vi = rc.getBaseVarInformation(v);
                    iter = vi.getRelations().iterator();
                    while (iter.hasNext()) {
                        VarRelation vr = iter.next();
                        if (!vr.var.equals(v) && !vr.getCofactor().equals(NumberModel.getZero())) {
                            Number num = calcIncompleteZjCi(v, vr.var);
                            num = num.negate().divide(vr.getCofactor());
                            if (vr.getCofactor().higher(NumberModel.getZero())) lvalues.add(num); else hvalues.add(num);
                        }
                    }
                    sa.addVarSentitivy(v, functionalCoef, getMin(hvalues), getMax(lvalues));
                } else {
                    vi = rc.getColVarInformation(v);
                    Number n = vi.zjci.add(functionalCoef);
                    if (vi.zjci.higherEquals(NumberModel.getZero())) sa.addVarSentitivy(v, functionalCoef, n, NumberModel.getMin()); else sa.addVarSentitivy(v, functionalCoef, NumberModel.getMax(), n);
                }
            }
        }
    }

    public void visit(OpMin opMin) {
        ColIterator colVars = rc.colVarsIterator();
        VarInformation vi;
        Iterator<VarRelation> iter;
        VariableRepository repo = rc.getVariableRepository();
        while (colVars.hasNext()) {
            Variable v = colVars.next();
            Number functionalCoef = rc.getVarFunctionalCoef(v);
            if (repo.isReal(v)) {
                if (rc.isVarInBase(v)) {
                    Vector<Number> hvalues = new Vector<Number>();
                    Vector<Number> lvalues = new Vector<Number>();
                    vi = rc.getBaseVarInformation(v);
                    iter = vi.getRelations().iterator();
                    while (iter.hasNext()) {
                        VarRelation vr = iter.next();
                        if (!vr.var.equals(v) && !vr.getCofactor().equals(NumberModel.getZero())) {
                            Number num = calcIncompleteZjCi(v, vr.var);
                            num = num.negate().divide(vr.getCofactor());
                            if (vr.getCofactor().higher(NumberModel.getZero())) hvalues.add(num); else lvalues.add(num);
                        }
                    }
                    sa.addVarSentitivy(v, functionalCoef, getMin(hvalues), getMax(lvalues));
                } else {
                    vi = rc.getColVarInformation(v);
                    Number n = vi.zjci.add(functionalCoef);
                    sa.addVarSentitivy(v, functionalCoef, NumberModel.getMax(), n);
                }
            }
        }
    }

    private Number calcIncompleteZjCi(Variable vb, Variable varCol) {
        Number zj = NumberModel.getZero();
        RowIterator iter = rc.rowVarsIterator();
        zj = NumberModel.getZero();
        while (iter.hasNext()) {
            Variable varBase = iter.next();
            if (!varBase.equals(vb)) {
                Number cofactor = rc.getCofactor(varBase, varCol);
                zj = zj.add(cofactor.multiply(rc.getVarFunctionalCoef(varBase)));
            }
        }
        zj = zj.subtract(rc.getVarFunctionalCoef(varCol));
        return zj;
    }

    private Number getMin(Vector<Number> values) {
        Number res = NumberModel.getMax();
        for (Number value : values) {
            if (value.lower(res)) res = value;
        }
        return res;
    }

    private Number getMax(Vector<Number> values) {
        Number res = NumberModel.getMin();
        for (Number value : values) {
            if (value.higher(res)) res = value;
        }
        return res;
    }
}
