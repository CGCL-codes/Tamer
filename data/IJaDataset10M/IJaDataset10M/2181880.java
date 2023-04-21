package com.ixora.common.ui.jobs;

import java.awt.Window;
import com.ixora.common.ProgressProvider;

/**
 * A job that provides progress feedback from an external provider.
 * @author Daniel Moraru
 */
public abstract class UIWorkerJobCancelableWithExternalProgress extends UIWorkerJobDefaultCancelable {

    /** Progress provider */
    protected ProgressProvider fProgress;

    /**
	 * Constructor.
	 * @param owner window
	 * @param cursor the cursor type
	 * @param description the job description
	 * @param progressProvider the prorgess provider to use
	 */
    protected UIWorkerJobCancelableWithExternalProgress(Window owner, int cursor, String description, ProgressProvider progressProvider) {
        super(owner, cursor, description);
        init(owner, cursor, description, progressProvider);
    }

    /**
	 * @see com.ixora.common.ui.jobs.UIWorkerJob#getProgressProvider()
	 */
    public ProgressProvider getProgressProvider() {
        return this.fProgress;
    }

    /**
	 * Init this instance.
	 * @param owner
	 * @param cursor
	 * @param description
	 * @param progressProvider
	 */
    private void init(Window owner, int cursor, String description, ProgressProvider progressProvider) {
        if (owner == null) {
            throw new IllegalArgumentException("null owner component");
        }
        this.fOwner = owner;
        this.fCursor = cursor;
        this.fDescription = description;
        this.fProgress = progressProvider;
    }
}
