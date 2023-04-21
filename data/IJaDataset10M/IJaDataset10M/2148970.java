package com.phloc.html.hc.html;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.impl.AbstractHCElementWithChildren;

/**
 * Represents an HTML &lt;code&gt; element
 * 
 * @author philip
 */
public final class HCCode extends AbstractHCElementWithChildren<HCCode> {

    public HCCode() {
        super(EHTMLElement.CODE);
    }

    public HCCode(@Nonnull final IPredefinedLocaleTextProvider aChild) {
        this(aChild.getText());
    }

    public HCCode(@Nullable final String sChild) {
        super(EHTMLElement.CODE, sChild);
    }

    public HCCode(@Nullable final String... aChildren) {
        super(EHTMLElement.CODE, aChildren);
    }

    public HCCode(@Nullable final IHCNode aChild) {
        super(EHTMLElement.CODE, aChild);
    }

    public HCCode(@Nullable final IHCNode... aChildren) {
        super(EHTMLElement.CODE, aChildren);
    }

    public HCCode(@Nullable final Iterable<? extends IHCNode> aChildren) {
        super(EHTMLElement.CODE, aChildren);
    }
}
