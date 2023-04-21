package com.sun.jini.outrigger;

import java.util.Set;

/**
 * Holds a collection of <code>TemplateHandle</code>s who's templates are all of
 * exactly the same class. Unless otherwise noted all methods are thread safe.
 * This method provides the linkage between <code>TemplateHandle</code>s and
 * <code>TransitionWatchers</code> and for the most part is not visible to the
 * clients of either.
 */
class WatchersForTemplateClass {

    /** All the templates we know about */
    private final FastList contents = new FastList();

    /** The object we are inside of */
    private final TransitionWatchers owner;

    /**
	 * Create a new <code>WatchersForTemplateClass</code> object associated with
	 * the specified <code>TransitionWatchers</code> object.
	 * 
	 * @param owner
	 *            The <code>TransitionWatchers</code> that this object will be a
	 *            part of.
	 * @throws NullPointerException
	 *             if <code>owner</code> is <code>null</code>.
	 */
    WatchersForTemplateClass(TransitionWatchers owner) {
        if (owner == null) throw new NullPointerException("owner must be non-null");
        this.owner = owner;
    }

    /**
	 * Add a <code>TransitionWatcher</code> to the list of watchers looking for
	 * visibility transitions in entries that match the specified template.
	 * Associates a <code>TemplateHandle</code> using
	 * <code>TransitionWatcher.setTemplateHandle</code> method.
	 * 
	 * @param watcher
	 *            The <code>TransitionWatcher</code> being added.
	 * @param template
	 *            The <code>EntryRep</code> that represents the template of
	 *            interest.
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>.
	 */
    void add(TransitionWatcher watcher, EntryRep template) {
        TemplateHandle handle = (TemplateHandle) contents.head();
        for (; handle != null; handle = (TemplateHandle) handle.next()) {
            if (template.equals(handle.rep())) {
                synchronized (handle) {
                    if (!handle.removed()) {
                        if (watcher.addTemplateHandle(handle)) {
                            handle.addTransitionWatcher(watcher);
                        }
                        return;
                    }
                }
            }
        }
        handle = new TemplateHandle(template, this);
        synchronized (handle) {
            contents.add(handle);
            if (watcher.addTemplateHandle(handle)) {
                handle.addTransitionWatcher(watcher);
            } else {
                contents.remove(handle);
            }
        }
    }

    /**
	 * Iterate over the watchers associated with this object calling
	 * <code>isInterested</code> on each and if it returns <code>true</code>
	 * adding the watcher to the passed set.
	 * 
	 * @param set
	 *            The set to accumulate interested watchers into.
	 * @param transition
	 *            The transition being processed.
	 * @param ordinal
	 *            The ordinal associated with <code>transition</code>.
	 * @throws NullPointerException
	 *             if either argument is <code>null</code>.
	 */
    void collectInterested(Set set, EntryTransition transition, long ordinal) {
        final EntryHandle entryHandle = transition.getHandle();
        final EntryRep rep = entryHandle.rep();
        final long entryHash = entryHandle.hash();
        final int repNumFields = rep.numFields();
        for (TemplateHandle handle = (TemplateHandle) contents.head(); handle != null; handle = (TemplateHandle) handle.next()) {
            EntryHandleTmplDesc desc = handle.descFor(repNumFields);
            if ((entryHash & desc.mask) != desc.hash) continue;
            if (handle.matches(rep)) {
                if (handle.removed()) continue;
                handle.collectInterested(set, transition, ordinal);
            }
        }
    }

    /**
	 * Return the <code>OutriggerServerImpl</code> this handle is part of.
	 * 
	 * @return The <code>OutriggerServerImpl</code> this handle is part of.
	 */
    OutriggerServerImpl getServer() {
        return owner.getServer();
    }

    /**
	 * Visit each <code>TransitionWatcher</code> and check to see if it has
	 * expired, removing it if it has. Also reaps the <code>FastList</code>
	 * associated with this object.
	 * 
	 * @param now
	 *            an estimate of the current time expressed as milliseconds
	 *            since the beginning of the epoch.
	 */
    void reap(long now) {
        for (TemplateHandle handle = (TemplateHandle) contents.head(); handle != null; handle = (TemplateHandle) handle.next()) {
            handle.reap(now);
            synchronized (handle) {
                if (handle.isEmpty()) {
                    contents.remove(handle);
                    continue;
                }
            }
        }
        contents.reap();
    }
}
