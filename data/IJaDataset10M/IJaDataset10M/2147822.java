package org.iptc.nar.core.model;

import java.util.LinkedList;
import java.util.List;
import org.iptc.nar.core.datatype.BlockType;
import org.iptc.nar.core.datatype.FlexPropType;

public class RightsInformationType {

    /**
	 * Identity of this entity.
	 */
    private Long m_identity;

    /**
	 * An individual accountable for the content in legal terms.
	 */
    private FlexPropType m_accountablePerson;

    /**
	 * The person or organisation claiming the intellectual property for the
	 * content.
	 */
    private FlexPropType m_copyrightHolder;

    /**
	 * A natural-language statement about the usage terms pertaining to the
	 * content.
	 */
    private List<BlockType> m_usageTerms = new LinkedList<BlockType>();

    /**
	 * Any necessary copyright notice for claiming the intellectual property for
	 * the content.
	 */
    private List<BlockType> m_copyrightNotices = new LinkedList<BlockType>();

    public Long getIdentity() {
        return m_identity;
    }

    public void setIdentity(Long identity) {
        m_identity = identity;
    }

    public FlexPropType getAccountablePerson() {
        return m_accountablePerson;
    }

    public void setAccountablePerson(FlexPropType accountable) {
        m_accountablePerson = accountable;
    }

    public FlexPropType getCopyrightHolder() {
        return m_copyrightHolder;
    }

    public void setCopyrightHolder(FlexPropType copyrightHolder) {
        m_copyrightHolder = copyrightHolder;
    }

    public List<BlockType> getUsageTerms() {
        return m_usageTerms;
    }

    public void setUsageTerms(List<BlockType> usageTerms) {
        m_usageTerms = usageTerms;
    }

    public void addUsageTerm(BlockType usage) {
        m_usageTerms.add(usage);
    }

    public void removeUsageTerm(BlockType usage) {
        m_usageTerms.remove(usage);
    }

    public List<BlockType> getCopyrightNotices() {
        return m_copyrightNotices;
    }

    public void setCopyrightNotices(List<BlockType> copyrightNotices) {
        m_copyrightNotices = copyrightNotices;
    }

    public void addCopyrightNotice(BlockType notice) {
        m_copyrightNotices.add(notice);
    }

    public void removeCopyrightNotice(BlockType notice) {
        m_copyrightNotices.remove(notice);
    }
}
