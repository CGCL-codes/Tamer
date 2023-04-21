package com.google.gwt.eclipse.platform.editors.java.contentassist;

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.viewers.StyledString;

/**
 * Overrides Eclipse 3.4+ functionality (styled display text) for
 * {@link com.google.gwt.eclipse.core.editors.java.contentassist.JsniCompletionProposal }
 */
public abstract class JsniCompletionProposal extends AbstractJsniCompletionProposal implements ICompletionProposalExtension6 {

    public JsniCompletionProposal(IJavaCompletionProposal jdtProposal, CompletionProposal wrappedProposal) {
        super(jdtProposal, wrappedProposal);
    }

    public StyledString getStyledDisplayString() {
        StyledString styledDisplayString = ((ICompletionProposalExtension6) jdtProposal).getStyledDisplayString();
        if (wrappedProposal.isConstructor()) {
            String displayString = styledDisplayString.getString();
            displayString = fixCtorDisplayString(displayString);
            int qualifierSeparatorPos = displayString.lastIndexOf('-');
            if (qualifierSeparatorPos > -1) {
                String ctorSignature = displayString.substring(0, qualifierSeparatorPos + 1);
                String className = displayString.substring(qualifierSeparatorPos + 1);
                styledDisplayString = new StyledString(ctorSignature);
                styledDisplayString.append(className, StyledString.QUALIFIER_STYLER);
            }
        }
        return styledDisplayString;
    }
}
