package org.eclipse.wst.xml.search.editor.internal.contentassist;

import org.eclipse.jdt.core.WorkingCopyOwner;

class CompilationUnitHelper {

    private CompilationProblemRequestor fProblemRequestor;

    private WorkingCopyOwner fWorkingCopyOwner;

    private static CompilationUnitHelper instance;

    private CompilationUnitHelper() {
        fProblemRequestor = null;
        fWorkingCopyOwner = null;
    }

    public static final synchronized CompilationUnitHelper getInstance() {
        if (instance == null) instance = new CompilationUnitHelper();
        return instance;
    }

    public CompilationProblemRequestor getProblemRequestor() {
        if (fProblemRequestor == null) fProblemRequestor = new CompilationProblemRequestor();
        return fProblemRequestor;
    }

    public WorkingCopyOwner getWorkingCopyOwner() {
        if (fWorkingCopyOwner == null) fWorkingCopyOwner = new WorkingCopyOwner() {

            public String toString() {
                return "WTP/XML Search Working copy owner";
            }

            final CompilationUnitHelper this$0;

            {
                this$0 = CompilationUnitHelper.this;
            }
        };
        return fWorkingCopyOwner;
    }
}
