package com.bocoon.entity.cms.assist;

import com.bocoon.entity.cms.assist.base.BaseCmsGuestbookCtg;

public class CmsGuestbookCtg extends BaseCmsGuestbookCtg {

    private static final long serialVersionUID = 1L;

    public CmsGuestbookCtg() {
        super();
    }

    /**
	 * Constructor for primary key
	 */
    public CmsGuestbookCtg(java.lang.Integer id) {
        super(id);
    }

    /**
	 * Constructor for required fields
	 */
    public CmsGuestbookCtg(java.lang.Integer id, com.bocoon.entity.cms.main.CmsSite site, java.lang.String name, java.lang.Integer priority) {
        super(id, site, name, priority);
    }
}
