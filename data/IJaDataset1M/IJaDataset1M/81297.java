package gumbo.journal.data.impl;

import gumbo.core.struct.ListSet;
import gumbo.journal.data.JournalKeyframe;
import gumbo.journal.data.JournalSample;
import java.io.Serializable;
import java.util.Collection;

/**
 * Default implementation for JournalKeyframe.
 * @author jonb
 * @param <T> Base type of the state element identity object.
 */
public class JournalKeyframeImpl<T extends Serializable> extends JournalDomainImpl<T> implements JournalKeyframe<T> {

    private static final long serialVersionUID = 1L;

    /**
	 * Creates an instance. The client is responsible for assuring that the
	 * state set is complete.
	 * @param samples Temp input group of constant state samples. Never empty.
	 */
    JournalKeyframeImpl(Collection<? extends JournalSample<T>> samples) {
        super(samples);
    }

    @Override
    public ListSet<JournalSample<T>> getElements() {
        @SuppressWarnings("unchecked") ListSet<JournalSample<T>> samples = (ListSet<JournalSample<T>>) super.getElements();
        return samples;
    }
}
