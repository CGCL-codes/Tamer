package org.eclipse.jdt.internal.compiler.lookup;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public class SourceTypeCollisionException extends RuntimeException {

    private static final long serialVersionUID = 4798247636899127380L;

    public ICompilationUnit[] newAnnotationProcessorUnits;
}
