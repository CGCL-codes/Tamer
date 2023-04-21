package org.eclipse.help.internal.search.federated;

import org.eclipse.core.runtime.*;
import org.eclipse.core.runtime.jobs.Job;

/**
 * A federated search job.
 */
public class FederatedSearchJob extends Job {

    public static final String FAMILY = "org.eclipse.help.base.searchEngine";

    private String expression;

    private FederatedSearchEntry entry;

    /**
	 * @param name
	 */
    public FederatedSearchJob(String expression, FederatedSearchEntry entry) {
        super(entry.getEngineName());
        this.expression = expression;
        this.entry = entry;
    }

    protected IStatus run(IProgressMonitor monitor) {
        try {
            entry.getEngine().run(expression, entry.getScope(), entry.getResultCollector(), monitor);
            return Status.OK_STATUS;
        } catch (CoreException e) {
            return e.getStatus();
        }
    }

    public boolean belongsTo(Object family) {
        return family.equals(FAMILY);
    }
}
