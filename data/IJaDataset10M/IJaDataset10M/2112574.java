package org.liris.schemerger.chronicle.heuristics;

import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.ITypeDec;

/**
 * Provides a template for implementing strategies of chronicle selection in the
 * complete chronicle miner {@link org.liris.schemerger.chronicle.CompleteSimChrMiner}
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
public interface HeuristicsManager<E extends ISimEvent, T extends ITypeDec> {

    /**
	 * Checks if c already belongs to the internal collection of waiting
	 * chronicles and add c to this collection in an optimized way for the
	 * implementing selection strategy.
	 * 
	 * @param c
	 *            the chronicle to add to the internal collection of the
	 *            chronicles waiting for selection
	 */
    public void addToOpens(IChronicle<T> c);

    /**
	 * @return true if the size of the internal collection is 0, that is no more
	 *         chronicle is waiting for selection.
	 */
    public boolean isEmpty();

    /**
	 * Selects and removes from the internal collection the next chronicle to
	 * proceed in the next discovery iteration of method
	 * {@link org.liris.schemerger.chronicle.CompleteSimChrMiner#mine(org.liris.schemerger.core.IRequest)}
	 * .
	 * 
	 * <p>
	 * The next chronicle is selected with respect to the strategy of the
	 * implementing class.
	 * </p>
	 * 
	 * @return the next chronicle to be handled by the chronicle miner
	 */
    public IChronicle<T> next();

    public void removeInvalidRoots(IChronicle<T> rootChronicle);

    public void clear();
}
