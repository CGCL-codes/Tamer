package com.ilog.translator.java2cs.translation.astrewriter.astchange;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ltk.core.refactoring.Change;
import com.ilog.translator.java2cs.translation.ITranslationContext;
import com.ilog.translator.java2cs.translation.astrewriter.TranslationVisitor;

public abstract class ASTChangeVisitor extends TranslationVisitor {

    protected Change change;

    public ASTChangeVisitor(ITranslationContext context) {
        super(context);
        change = null;
    }

    @Override
    public boolean needValidation() {
        return true;
    }

    public void reset() {
        change = null;
        fCu = null;
    }

    public boolean needRecovery() {
        return false;
    }

    @Override
    public Change createChange(IProgressMonitor pm, Object param) {
        if (context.getConfiguration().getOptions().isSimulation()) {
            return null;
        }
        return change;
    }

    @Override
    public abstract boolean runOnce();

    public boolean needCompilable() {
        return true;
    }
}
