package org.eclipse.dltk.freemarker.internal.ui.jdt.contentassist;

import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.fieldassist.ContentAssistCommandAdapter;

/**
 * TypeFieldAssistDisposer
 *
 */
public class TypeFieldAssistDisposer {

    private ContentAssistCommandAdapter fAdapter;

    private TypeContentProposalListener fListener;

    /**
	 * 
	 */
    public TypeFieldAssistDisposer(ContentAssistCommandAdapter adapter, TypeContentProposalListener listener) {
        fAdapter = adapter;
        fListener = listener;
    }

    /**
	 * 
	 */
    public void dispose() {
        if (fAdapter == null) {
            return;
        }
        ILabelProvider labelProvider = fAdapter.getLabelProvider();
        if ((labelProvider != null)) {
            fAdapter.setLabelProvider(null);
            labelProvider.dispose();
        }
        if (fListener != null) {
            fAdapter.removeContentProposalListener((IContentProposalListener) fListener);
            fAdapter.removeContentProposalListener((IContentProposalListener2) fListener);
        }
    }
}
