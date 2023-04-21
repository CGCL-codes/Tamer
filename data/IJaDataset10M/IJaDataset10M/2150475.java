package org.liris.schemerger.chronicle.cstdb;

import org.liris.schemerger.chronicle.IChrMinerRequest;
import org.liris.schemerger.chronicle.LoggedProcess;
import org.liris.schemerger.core.AbortException;
import org.liris.schemerger.core.dataset.SimSequence;
import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.utils.Factory;

/**
 * An template class for the building of a constraint database in compliance
 * with {@link #request}. The resulting constraint database is meant for the
 * input of any chronicle mining algorithm like
 * {@link org.liris.schemerger.chronicle.CompleteSimChrMiner#mineFromCstDB(IChrMinerRequest, CstDB)}
 * or
 * {@link org.liris.schemerger.chronicle.DuongChrMiner#mineFromCstDB(IChrMinerRequest, CstDB)}
 * .
 * 
 * @author Damien Cram
 * 
 * @param <E>
 *            The type of event in the sequence to mine. Must be a subclass of
 *            {@link ISimEvent}.
 * @param <T>
 *            The type of event declaration of chronicle episodes.
 */
public abstract class CstDBBuilder<E extends ISimEvent, T extends ITypeDec> {

    protected Class<T> decClass;

    protected Factory factory = Factory.getInstance();

    protected SimSequence<E> sequence;

    protected IChrMinerRequest<E, T> request;

    protected LoggedProcess loggedProcess;

    public void setLoggedProcess(LoggedProcess loggedProcess) {
        this.loggedProcess = loggedProcess;
    }

    public LoggedProcess getLoggedProcess() {
        return loggedProcess;
    }

    public CstDBBuilder(SimSequence<E> sequence, IChrMinerRequest<E, T> request, Class<T> clazz) {
        super();
        this.sequence = sequence;
        this.request = request;
        this.decClass = clazz;
    }

    /**
	 * Builds the constraint-database with respect to the parameters contained
	 * in {@link #request}.
	 * 
	 * @return the constraint database
	 */
    public abstract CstDB<T> build() throws AbortException;
}
