package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.html.HCA;

/**
 * A bootstrap button that has a target URL if you click it.
 * 
 * @author philip
 */
public class BootstrapLinkButton extends HCA {

    private EBootstrapButtonType m_eType = EBootstrapButtonType.DEFAULT;

    private EBootstrapButtonSize m_eSize;

    private void _init() {
        addClass(CBootstrapCSS.BTN);
    }

    public BootstrapLinkButton() {
        super();
        _init();
    }

    public BootstrapLinkButton(@Nonnull final ISimpleURL aHref) {
        super(aHref);
        _init();
    }

    @Nonnull
    public BootstrapLinkButton setType(@Nonnull final EBootstrapButtonType eType) {
        if (eType == null) throw new NullPointerException("type");
        m_eType = eType;
        return this;
    }

    @Nonnull
    public BootstrapLinkButton setSize(@Nullable final EBootstrapButtonSize eSize) {
        m_eSize = eSize;
        return this;
    }

    @Override
    protected void prepareBeforeCreateElement(@Nonnull final HCConversionSettings aConversionSettings) {
        super.prepareBeforeCreateElement(aConversionSettings);
        addClasses(m_eType, m_eSize);
    }
}
