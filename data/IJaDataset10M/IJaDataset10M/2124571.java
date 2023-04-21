package com.phloc.html.hc.html;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.EHTMLElement;
import com.phloc.html.annotations.DeprecatedInXHTML1;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.impl.AbstractHCElementWithChildren;

/**
 * Represents an HTML &lt;blockquote&gt; element
 * 
 * @author philip
 */
@DeprecatedInXHTML1
public final class HCBlockQuote extends AbstractHCElementWithChildren<HCBlockQuote> {

    public HCBlockQuote() {
        super(EHTMLElement.BLOCKQUOTE);
    }

    public HCBlockQuote(@Nonnull final IPredefinedLocaleTextProvider aChild) {
        this(aChild.getText());
    }

    public HCBlockQuote(@Nullable final String sChild) {
        super(EHTMLElement.BLOCKQUOTE, sChild);
    }

    public HCBlockQuote(@Nullable final String... aChildren) {
        super(EHTMLElement.BLOCKQUOTE, aChildren);
    }

    public HCBlockQuote(@Nullable final IHCNode aChild) {
        super(EHTMLElement.BLOCKQUOTE, aChild);
    }

    public HCBlockQuote(@Nullable final IHCNode... aChildren) {
        super(EHTMLElement.BLOCKQUOTE, aChildren);
    }

    public HCBlockQuote(@Nullable final Iterable<? extends IHCNode> aChildren) {
        super(EHTMLElement.BLOCKQUOTE, aChildren);
    }
}
