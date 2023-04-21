package com.phloc.html.hc.html;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.EHTMLElement;
import com.phloc.html.annotations.DeprecatedInHTML32;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.impl.AbstractHCElementWithChildren;

/**
 * Represents an HTML &lt;listing&gt; element
 * 
 * @author philip
 */
@DeprecatedInHTML32
public final class HCListing extends AbstractHCElementWithChildren<HCListing> {

    public HCListing() {
        super(EHTMLElement.LISTING);
    }

    public HCListing(@Nonnull final IPredefinedLocaleTextProvider aChild) {
        this(aChild.getText());
    }

    public HCListing(@Nullable final String sChild) {
        super(EHTMLElement.LISTING, sChild);
    }

    public HCListing(@Nullable final String... aChildren) {
        super(EHTMLElement.LISTING, aChildren);
    }

    public HCListing(@Nullable final IHCNode aChild) {
        super(EHTMLElement.LISTING, aChild);
    }

    public HCListing(@Nullable final IHCNode... aChildren) {
        super(EHTMLElement.LISTING, aChildren);
    }

    public HCListing(@Nullable final Iterable<? extends IHCNode> aChildren) {
        super(EHTMLElement.LISTING, aChildren);
    }
}
