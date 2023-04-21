package org.hoydaa.codesnippet.core.filter.docfrag;

import org.w3c.dom.DocumentFragment;

/**
 * 
 * @author Umut Utkan
 */
public abstract class DocumentFragmentFilter {

    private DocumentFragmentFilter filter;

    public final void addFilter(DocumentFragmentFilter filter) {
        if (this.filter != null) {
            this.filter.addFilter(filter);
        } else {
            this.filter = filter;
        }
    }

    public final void filter(DocumentFragment docFrag, final int tokenKind, final String tokenImage) {
        run(docFrag, tokenKind, tokenImage);
        if (filter != null) {
            filter.filter(docFrag, tokenKind, tokenImage);
        }
    }

    protected abstract void run(DocumentFragment docFrag, final int tokenKind, final String tokenImage);
}
