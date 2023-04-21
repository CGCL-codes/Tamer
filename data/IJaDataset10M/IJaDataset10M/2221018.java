package org.nightlabs.jfire.base.ui.overview;

/**
 * Abstract base for {@link EntryViewer}s holding the entry that created it.
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 * @param <O> the type of objects shown in the 
 */
public abstract class AbstractEntryViewer implements EntryViewer {

    private Entry entry;

    /**
	 * 
	 */
    public AbstractEntryViewer(Entry entry) {
        this.entry = entry;
    }

    /**
	 * {@inheritDoc}
	 * @see org.nightlabs.jfire.base.ui.overview.EntryViewer#getEntry()
	 */
    public Entry getEntry() {
        return entry;
    }
}
