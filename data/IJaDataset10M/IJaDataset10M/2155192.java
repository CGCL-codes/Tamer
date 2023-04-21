package ar.edu.utn.frba.tadp.bttf.period;

import java.util.Iterator;
import ar.edu.utn.frba.tadp.bttf.Visitor;
import ar.edu.utn.frba.tadp.bttf.instant.Instant;

/**
 * Representa la union de periodos.
 * 
 * @author lgassman
 */
public class UnionPeriod extends CompositePeriod {

    public UnionPeriod(Period period, Period period2) {
        super(period, period2);
    }

    public boolean belongs(Instant instant) {
        for (Iterator iterator = this.getPeriods().iterator(); iterator.hasNext(); ) {
            if (!((Period) iterator.next()).belongs(instant)) {
                return false;
            }
        }
        return true;
    }

    public Period union(Period period) {
        this.addPeriod(period);
        return this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof UnionPeriod) {
            return this.getPeriods().equals(((UnionPeriod) obj).getPeriods());
        }
        return false;
    }

    protected String operation() {
        return this.getClass().getName();
    }

    public void accept(Visitor visitor) {
        ((PeriodVisitor) visitor).visitUnionPeriod(this);
    }
}
