package gumbo.journal.ops;

import gumbo.core.util.Checker;
import gumbo.journal.JournalDataset;
import gumbo.journal.data.JournalElement;
import gumbo.journal.data.JournalSample;
import java.io.Serializable;
import java.util.Set;

/**
 * Stateful JournalOperator use by a client to find journal data for building
 * JournalKeyframes.
 * @author jonb
 * @param <T> Base type of the state element identity object.
 */
public interface JournalKeyframer<T extends Serializable> extends JournalOperator<T> {

    /**
	 * Finds the closest journal time at or after a floor time at which the
	 * accumulated journal state is complete, as defined by the state domain.
	 * Returns the accumulated state data, which consists of all state elements
	 * in the state domain, and any state elements approved by the state
	 * checker. Typically used to find and collect state data for use in a
	 * forward keyframe, consisting of continuous domain state plus any discrete
	 * state.
	 * @param floorTime Floor time, in UTC milliseconds relative (+/-) to the
	 * epoch (January 1, 1970 00:00:00.000 GMT).
	 * @param stateDomain Temp input group of state elements. Never empty.
	 * @param stateChecker Temp input state checker. None if null (i.e. the
	 * result will only contain domain samples).
	 * @return Constant journal data. None if null (i.e. reached the end of the
	 * journal before completing the domain).
	 * @throws UnsupportedOperationException if the journal does not support
	 * forward access.
	 */
    public JournalDataset<T, JournalSample<T>> findForwardKeyData(long floorTime, Set<? extends JournalElement<T>> stateDomain, Checker<JournalSample<T>> stateChecker);

    /**
	 * Finds the closest journal time at or before a ceiling time at which the
	 * accumulated journal state is complete, as defined by the state domain.
	 * Returns the accumulated state data, which consists of all state elements
	 * in the state domain, and any state elements approved by the state
	 * checker. Typically used to find and collect state data for use in a
	 * forward keyframe, consisting of continuous domain state plus any discrete
	 * state.
	 * @param floorTime Floor time, in UTC milliseconds relative (+/-) to the
	 * epoch (January 1, 1970 00:00:00.000 GMT).
	 * @param stateDomain Temp input group of opaque state objects (value is
	 * ignored). Never empty.
	 * @param stateChecker Temp input state checker. None if null (i.e. the
	 * result will only contain domain state objects).
	 * @return Constant journal data. None if null (i.e. reached the end of the
	 * journal before completing the domain).
	 * @throws UnsupportedOperationException if the journal does not support
	 * forward access.
	 */
    public JournalDataset<T, JournalSample<T>> findReverseKeyData(long ceilTime, Set<? extends JournalElement<T>> stateDomain, Checker<JournalSample<T>> stateChecker);
}
