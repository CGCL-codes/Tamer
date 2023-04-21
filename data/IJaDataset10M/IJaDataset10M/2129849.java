package com.phloc.html.hc.html;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.css.CCSS;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.impl.HCEntityNode;

/**
 * Represents an HTML &lt;table&gt; element with open semantics.
 * 
 * @author philip
 * @param <THISTYPE>
 *        The implementing type
 */
public abstract class AbstractHCTable<THISTYPE extends AbstractHCTable<THISTYPE>> extends AbstractHCBaseTable<THISTYPE> {

    public static final ICSSClassProvider CSS_FORCE_COLSPAN = DefaultCSSClassProvider.create("force_colspan");

    private static final Logger s_aLogger = LoggerFactory.getLogger(AbstractHCTable.class);

    public AbstractHCTable() {
        super(EHTMLElement.TABLE);
    }

    public AbstractHCTable(@Nullable final HCCol aCol) {
        this();
        addColumn(aCol);
    }

    public AbstractHCTable(@Nullable final HCCol... aCols) {
        this();
        addColumns(aCols);
    }

    public AbstractHCTable(@Nullable final Iterable<HCCol> aCols) {
        this();
        addColumns(aCols);
    }

    @OverrideOnDemand
    protected void applyHeaderRow(final IMicroElement aTHead, final HCRow aRow, @Nonnull final HCConversionSettings aConversionSettings) {
        aTHead.appendChild(aRow.getAsNode(aConversionSettings));
    }

    @OverrideOnDemand
    protected void applyFooterRow(final IMicroElement aTFoot, final HCRow aRow, @Nonnull final HCConversionSettings aConversionSettings) {
        aTFoot.appendChild(aRow.getAsNode(aConversionSettings));
    }

    @OverrideOnDemand
    protected void applyBodyRow(final IMicroElement aTBody, final HCRow aRow, @Nonnull final HCConversionSettings aConversionSettings) {
        aTBody.appendChild(aRow.getAsNode(aConversionSettings));
    }

    @Override
    protected boolean canConvertToNode(@Nonnull final HCConversionSettings aConversionSettings) {
        return m_aHeaderRow != null || !m_aBodyRows.isEmpty() || m_sBodyID != null || m_aFooterRow != null;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void applyProperties(@Nonnull final IMicroElement aElement, @Nonnull final HCConversionSettings aConversionSettings) {
        super.applyProperties(aElement, aConversionSettings);
        if (aConversionSettings.getHTMLVersion().isPriorToHTML5()) {
            if (m_nCellSpacing >= 0) aElement.setAttribute(CHTMLAttributes.CELLSPACING, Integer.toString(m_nCellSpacing));
            if (m_nCellPadding >= 0) aElement.setAttribute(CHTMLAttributes.CELLPADDING, Integer.toString(m_nCellPadding));
        }
        if (m_aColGroup != null && m_aColGroup.hasColumns()) aElement.appendChild(m_aColGroup.getAsNode(aConversionSettings));
        if (m_aHeaderRow != null) {
            final IMicroElement aTHead = aElement.appendElement(EHTMLElement.THEAD.getElementName());
            if (StringHelper.hasText(m_sHeaderID)) aTHead.setAttribute(CHTMLAttributes.ID, m_sHeaderID);
            applyHeaderRow(aTHead, m_aHeaderRow, aConversionSettings);
        }
        if (m_aFooterRow != null) {
            final IMicroElement aTFoot = aElement.appendElement(EHTMLElement.TFOOT.getElementName());
            if (StringHelper.hasText(m_sFooterID)) aTFoot.setAttribute(CHTMLAttributes.ID, m_sFooterID);
            applyFooterRow(aTFoot, m_aFooterRow, aConversionSettings);
        }
        if (m_aBodyRows.isEmpty() && aConversionSettings.getHTMLVersion().isXHTML11()) s_aLogger.warn("Tables without body rows are prohibited by XHTML 1.1!");
        final IMicroElement aTBody = aElement.appendElement(EHTMLElement.TBODY.getElementName());
        if (StringHelper.hasText(m_sBodyID)) aTBody.setAttribute(CHTMLAttributes.ID, m_sBodyID);
        if (m_aColGroup != null && !m_aBodyRows.isEmpty()) {
            final HCRow aFirstRow = m_aBodyRows.get(0);
            boolean bFirstRowUsesColSpan = false;
            for (final AbstractHCCell aCell : aFirstRow.getChildren()) if (aCell.getColspan() > 1) {
                bFirstRowUsesColSpan = true;
                break;
            }
            if (bFirstRowUsesColSpan) {
                final HCRow aRow = new HCRow(false);
                aRow.addClass(CSS_FORCE_COLSPAN);
                for (final HCCol aCol : m_aColGroup.getColumns()) {
                    final AbstractHCCell aCell = aRow.addAndReturnCell(HCEntityNode.newNBSP());
                    final int nWidth = StringHelper.parseInt(aCol.getWidth(), -1);
                    if (nWidth >= 0) aCell.addStyle(CCSSProperties.WIDTH.newValue(CCSS.px(nWidth)));
                }
                applyBodyRow(aTBody, aRow, aConversionSettings);
            }
        }
        for (final HCRow aRow : m_aBodyRows) applyBodyRow(aTBody, aRow, aConversionSettings);
    }
}
