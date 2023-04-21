package net.sf.refactorit.query.usage.filters;

import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.query.usage.InvocationData;

public final class SimpleFilter extends SearchFilter {

    public SimpleFilter(final boolean showDuplicates, final boolean goToSingleUsage, final boolean runWithDefaultSettings) {
        super(showDuplicates, goToSingleUsage, runWithDefaultSettings);
    }

    protected final boolean passesFilter(final InvocationData invocationData, final Project project) {
        return true;
    }
}
