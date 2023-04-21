package muse.external.solver.parallel.backsync;

import static java.lang.Math.abs;
import muse.external.ExternalException;
import muse.external.solver.parallel.SynchronizedProcessor;

/**
 * The algorithm in which synchronization may be done only in segment [time - syncTau; time + syncTau].
 * Compromise between super classes realization (After* and Before*).
 * 
 * <p>waiter          : *******|<-syncTau->||<-syncTau->|</p>
 * <p>currentProcessor: *********************************</p>
 * 
 * @version 1.2
 * @author Korchak Anton
 */
public class BothNonDivisibleStepAlgorithm extends BeforeNonDivisibleStepAlgorithm {

    /**
	 * Compromise between super classes realization (After* and Before*).
	 * @see muse.external.solver.parallel.backsync.BeforeNonDivisibleStepAlgorithm#processInitState(muse.external.solver.parallel.SynchronizedProcessor, muse.external.solver.parallel.SynchronizedProcessor)
	 */
    protected boolean processInitState(SynchronizedProcessor currentProcessor, SynchronizedProcessor waiter) {
        double dif = currentProcessor.getCurrentTime() - waiter.getCurrentTime();
        if (abs(dif) <= getSynchronizationTimeStep()) {
            if (DEBUG) System.out.println("Transmition data form " + waiter.getName() + " to " + currentProcessor.getName());
            return waiter.backSynchronizeDataWithProcessor(currentProcessor);
        } else if (dif > getSynchronizationTimeStep()) throw new ExternalException("\"syncTau\" was set incorrect. Processors \"" + waiter.getName() + "\" and \"" + currentProcessor.getName() + "\" couldn't do synchronization during \"sychTau\".");
        return false;
    }

    /**
	 * Compromise between super classes realization (After* and Before*).
	 * @see muse.external.solver.parallel.backsync.BeforeNonDivisibleStepAlgorithm#processRunningState(muse.external.solver.parallel.SynchronizedProcessor, muse.external.solver.parallel.SynchronizedProcessor)
	 */
    protected boolean processRunningState(SynchronizedProcessor currentProcessor, SynchronizedProcessor waiter) {
        double dif = currentProcessor.getCurrentTime() - waiter.getEsitmatedTime();
        if (abs(dif) <= getSynchronizationTimeStep()) {
            if (!currentProcessor.getSynchronizationList().contains(waiter)) currentProcessor.getSynchronizationList().add(waiter);
            return true;
        } else if (dif > getSynchronizationTimeStep()) throw new ExternalException("\"syncTau\" was set incorrect. Processors \"" + waiter.getName() + "\" and \"" + currentProcessor.getName() + "\" couldn't do synchronization during \"sychTau\".");
        return false;
    }
}
