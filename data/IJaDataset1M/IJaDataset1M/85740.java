package com.intrigueit.myc2i.role.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APPLICATION_PAGES")
public class ApplicationPages implements Serializable {

    /**
   * 
   */
    private static final long serialVersionUID = 5196784922719881536L;

    @Id
    @Column(name = "PAGE_ID")
    private Long pageId;

    @Column(name = "PAGE_URL")
    private String pageUrl;

    /**
	 * @return the pageId
	 */
    public Long getPageId() {
        return pageId;
    }

    /**
	 * @param pageId
	 *            the pageId to set
	 */
    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    /**
	 * @return the pageUrl
	 */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
	 * @param pageUrl
	 *            the pageUrl to set
	 */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
