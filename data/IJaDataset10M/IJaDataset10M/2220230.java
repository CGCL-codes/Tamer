package org.iptc.nar.core.model;

import org.iptc.nar.core.datatype.LabelType;

public class UsageTerms {

    public UsageTerms() {
        super();
    }

    /**
	 * A natural-language statement about the usage terms pertaining to the
	 * content.
	 */
    private LabelType m_usageTerm;

    public LabelType getUsageTerm() {
        return m_usageTerm;
    }

    public void setUsageTerm(LabelType usageTerm) {
        m_usageTerm = usageTerm;
    }
}
