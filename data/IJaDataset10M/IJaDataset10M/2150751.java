package gumbo.journal.data;

import java.io.Serializable;
import gumbo.core.struct.ListSet;

/**
 * A JournalDomain whose element set contains JournalSamples, thereby defining
 * both a state domain and the value of that domain. As a JournalDomain,
 * equality is based on type JournalDomain and that of its domain (identities,
 * not values). A keyframe is considered "complete" within some state domain
 * (the complete system state, or a subset thereof). In general, transient state
 * elements should not be included in keyframes since completeness cannot be
 * ascertained (i.e. such events may never occur).
 * <p>
 * Keyframes facilitate seeking to a particular time in a journal for recording
 * or playback of journal entries. A keyframe's state set specifies its state
 * domain (state object identities) and the initial state of the domain (state
 * object values).
 * <p>
 * Typically, once a keyframe is extracted the client inserts it into the
 * journal for future use. In the case of a "full head" keyframe, the client
 * typically replaces the journal entries from the head time to the keyframe
 * time with the keyframe.
 * @author jonb
 * @param <T> Base type of the state element identity object.
 */
public interface JournalKeyframe<T extends Serializable> extends JournalDomain<T> {

    /**
	 * Gets the state samples specifying the state keyframe.
	 * @return Constant group of constant state samples. Never null.
	 */
    @Override
    public ListSet<? extends JournalSample<T>> getElements();
}
