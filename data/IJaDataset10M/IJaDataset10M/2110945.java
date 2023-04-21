package org.slasoi.monitoring.fbk.impl;

import org.slasoi.slamodel.core.EventExpr;
import org.slasoi.slamodel.core.FunctionalExpr;
import org.slasoi.slamodel.primitives.CONST;
import org.slasoi.slamodel.primitives.Expr;
import org.slasoi.slamodel.primitives.ValueExpr;
import org.slasoi.slamodel.sla.VariableDeclr;

/**
 * Utility to interpret the sum formula.
 *
 * @since 0.1
 */
public class SumUtility {

    /** The reference to the variable declaration. */
    private VariableDeclr varRef = null;

    /** The aspect. */
    private String aspect = null;

    /** The periodic. */
    private String periodic = null;

    /**
     * Constructor.
     *
     * @param varRefIn the variable reference
     */
    public SumUtility(final VariableDeclr varRefIn) {
        this.varRef = varRefIn;
    }

    /**
     * Get aspect.
     *
     * @return String the aspect
     */
    public final String getAspect() {
        return aspect;
    }

    /**
     * Set aspect.
     *
     * @param aspectIn the aspect
     */
    public final void setAspect(final String aspectIn) {
        this.aspect = aspectIn;
    }

    /**
     * Get periodic.
     *
     * @return String
     */
    public final String getPeriodic() {
        return periodic;
    }

    /**
     * Set periodic.
     *
     * @param periodicIn the periodic
     */
    public final void setPeriodic(final String periodicIn) {
        this.periodic = periodicIn;
    }

    /**
     * Is the formula the mean.
     *
     * @return boolean the value of truth
     */
    public final boolean isSum() {
        FunctionalExpr fe = (FunctionalExpr) this.varRef.getExpr();
        if (!(fe.getOperator().toString().equals("sum"))) {
            return false;
        }
        if (fe.getParameters().length < 1) {
            return false;
        }
        ValueExpr par0l = fe.getParameters()[0];
        FunctionalExpr fe0l = (FunctionalExpr) par0l;
        if (!(fe0l.getOperator().toString().equals("series"))) {
            return false;
        } else {
            if (fe0l.getParameters().length < 1) {
                return false;
            }
        }
        return true;
    }

    /** Parse the formula. */
    public final void parse() {
        FunctionalExpr fe = (FunctionalExpr) this.varRef.getExpr();
        ValueExpr par0l = fe.getParameters()[0];
        FunctionalExpr fe0l = (FunctionalExpr) par0l;
        ValueExpr par1l = fe0l.getParameters()[0];
        EventExpr ev1r = (EventExpr) fe0l.getParameters()[1];
        Expr e2 = ev1r.getParameters()[0];
        CONST cons = (CONST) e2;
        String datatype = cons.getDatatype().toString();
        String consValue = cons.getValue();
        this.setAspect(par1l.toString());
        this.setPeriodic(consValue + " " + datatype);
    }
}
