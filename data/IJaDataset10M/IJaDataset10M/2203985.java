package org.parallelj.internal.kernel.join;

import org.parallelj.internal.kernel.KCall;
import org.parallelj.internal.kernel.KCondition;
import org.parallelj.internal.kernel.KOutputLink;
import org.parallelj.internal.kernel.KProcedure;
import org.parallelj.internal.kernel.KProcess;

/**
 * TODO javadoc
 * 
 * @author Atos Worldline
 */
public class KOrJoin extends KAbstractJoin {

    /**
	 * Create a new OR join
	 * 
	 * @param procedure
	 *            the procedure containing this join
	 */
    public KOrJoin(KProcedure procedure) {
        super(procedure);
    }

    /**
	 * Return <code>true</code> if there is at least one non empty input
	 * condition and empty ones cannot be filled by their incoming procedures.
	 */
    @Override
    public boolean isEnabled(KProcess process) {
        boolean exists = false;
        for (KCondition condition : this.conditions) {
            if (condition.contains(process)) {
                exists = true;
                continue;
            }
            for (KOutputLink link : condition.getOutputLinks()) {
                KProcedure procedure = link.getProcedure();
                if (procedure.isEnabled(process) || procedure.isBusy(process)) {
                    return false;
                }
            }
        }
        return exists;
    }

    /**
	 * Consume tokens in all non empty conditions.
	 */
    @Override
    public void join(KCall call) {
        for (KCondition condition : this.conditions) {
            if (condition.contains(call.getProcess())) {
                condition.consume(call.getProcess());
            }
        }
    }
}
