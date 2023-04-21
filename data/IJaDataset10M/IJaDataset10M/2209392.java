package org.broadleafcommerce.cms.structure.domain;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * Implementations hold the values for a rule used to determine if a <code>StructuredContent</code>
 * item should be displayed.
 * <br>
 * The rule is represented as a valid MVEL string.    The Content Management System by default
 * is able to process rules based on the current customer, product,
 * {@link org.broadleafcommerce.common.TimeDTO time}, or {@link org.broadleafcommerce.common.RequestDTO request}
 *
 * @see org.broadleafcommerce.cms.web.structure.DisplayContentTag
 * @see org.broadleafcommerce.cms.structure.service.StructuredContentServiceImpl#evaluateAndPriortizeContent(java.util.List, int, java.util.Map)
 * @author jfischer
 * @author bpolster
 *
 */
public interface StructuredContentRule extends Serializable {

    /**
     * Gets the primary key.
     *
     * @return the primary key
     */
    @Nullable
    public Long getId();

    /**
     * Sets the primary key.
     *
     * @param id the new primary key
     */
    public void setId(@Nullable Long id);

    /**
     *
     * @return the rule as an MVEL string
     */
    @Nonnull
    public String getMatchRule();

    /**
     * Sets the match rule used to test this item.
     *
     * @param matchRule
     */
    public void setMatchRule(@Nonnull String matchRule);

    /**
     * Builds a copy of this content rule.   Used by the content management system when an
     * item is edited.
     *
     * @return a copy of this rule
     */
    @Nonnull
    public StructuredContentRule cloneEntity();
}
