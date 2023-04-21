package com.ilog.translator.java2cs.translation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ltk.core.refactoring.Change;

public interface ITransformer {

    public abstract String getName();

    public abstract boolean transform(IProgressMonitor pm, ASTNode cunit);

    public abstract boolean applyChange(IProgressMonitor pm) throws CoreException;

    public abstract Change createChange(IProgressMonitor pm, Object param) throws CoreException;

    public abstract boolean runAgain(CompilationUnit unit);

    public abstract boolean runOnce();

    public abstract boolean canRun();

    public abstract boolean needValidation();

    public abstract void setCompilationUnit(ICompilationUnit icunit);

    public abstract void reset();

    public abstract boolean isAbridged();

    public abstract void setSimulation(boolean sim);

    public abstract boolean needRecovery();

    public abstract boolean needCompilable();

    public abstract void postAction(ICompilationUnit icunit, CompilationUnit unit);

    public abstract void setTriggerConditionName(String optionName);

    public abstract void postActionOnAST(ICompilationUnit icunit, CompilationUnit unit);

    public abstract boolean hasPostActionOnAST();
}
