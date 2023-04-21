package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCHR;
import com.phloc.html.hc.html5.HCFooter;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;

public class BootstrapContentLayout extends AbstractWrappedHCNode {

    private HCDiv m_aContainer;

    private IHCNode m_aContent;

    private IHCNode m_aFooter;

    public BootstrapContentLayout() {
    }

    @Nonnull
    public BootstrapContentLayout setContent(@Nullable final IHCNode aContent) {
        m_aContent = aContent;
        return this;
    }

    @Nonnull
    public BootstrapContentLayout setFooter(@Nullable final IHCNode aFooter) {
        m_aFooter = aFooter;
        return this;
    }

    @Override
    protected void prepareBeforeGetAsNode(@Nonnull final HCConversionSettings aConversionSettings) {
        m_aContainer = new HCDiv().addClass(CBootstrapCSS.CONTAINER_FLUID);
        if (m_aContent != null) m_aContainer.addChild(m_aContent);
        if (m_aFooter != null) m_aContainer.addChildren(new HCHR(), new HCFooter(m_aFooter));
    }

    @Override
    protected IHCNode getContainedHCNode() {
        return m_aContainer;
    }
}
