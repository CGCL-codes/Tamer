package org.torweg.pulse.component.core.site.search;

import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.site.map.SitemapSectionTag;

/**
 * result with all available {@code SitemapSectionTag} for the advanced
 * search.
 * 
 * @author Thomas Weber
 * @version $Revision: 1822 $
 */
@XmlRootElement(name = "advanced-search-result")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdvancedSearchResult {

    /**
	 * the section tags available for the advanced search.
	 */
    @XmlElement(name = "section-tag")
    private Collection<SitemapSectionTag> sectionTags;

    /**
	 * used by JAXB.
	 */
    @Deprecated
    protected AdvancedSearchResult() {
        super();
    }

    /**
	 * creates a new result.
	 * 
	 * @param tags
	 *            the section tags
	 */
    public AdvancedSearchResult(final Collection<SitemapSectionTag> tags) {
        super();
        this.sectionTags = tags;
    }

    /**
	 * returns the section tags available for the advanced search.
	 * 
	 * @return the section tags
	 */
    public final Collection<SitemapSectionTag> getSectionTags() {
        return this.sectionTags;
    }
}
