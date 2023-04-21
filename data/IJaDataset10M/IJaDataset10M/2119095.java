package com.pentagaia.eclipse.sgs.build;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

/**
 * Quick fix processor
 * 
 * @author mepeisen
 */
public class SgsQuickFixProcessor implements IQuickFixProcessor {

    /**
     * {@inheritDoc}
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#getCorrections(org.eclipse.jdt.ui.text.java.IInvocationContext, org.eclipse.jdt.ui.text.java.IProblemLocation[])
     */
    @Override
    public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
        return null;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jdt.ui.text.java.IQuickFixProcessor#hasCorrections(org.eclipse.jdt.core.ICompilationUnit, int)
     */
    @Override
    public boolean hasCorrections(ICompilationUnit unit, int problemId) {
        return false;
    }
}
