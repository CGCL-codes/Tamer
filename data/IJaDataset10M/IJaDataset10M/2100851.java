package org.perlipse.internal.validators.ui.compiler;

import org.eclipse.core.resources.IMarker;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.text.IScriptCorrectionContext;
import org.eclipse.dltk.ui.text.IScriptCorrectionProcessor;

public class PerlCompilerCorrectionProcessor implements IScriptCorrectionProcessor {

    public boolean canFix(IScriptAnnotation annotation) {
        return false;
    }

    public boolean canFix(IMarker marker) {
        return false;
    }

    public void computeQuickAssistProposals(IScriptAnnotation annotation, IScriptCorrectionContext context) {
        System.out.println("foo");
    }

    public void computeQuickAssistProposals(IMarker marker, IScriptCorrectionContext context) {
        System.out.println("foo");
    }
}
