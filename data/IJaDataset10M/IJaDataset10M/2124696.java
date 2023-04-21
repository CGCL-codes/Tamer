package com.jeecms.core.entity;

import org.apache.commons.lang.StringUtils;
import com.jeecms.core.entity.base.BaseCmsConfig;

public class CmsConfig extends BaseCmsConfig {

    private static final long serialVersionUID = 1L;

    public void blankToNull() {
        if (StringUtils.isBlank(getProcessUrl())) {
            setProcessUrl(null);
        }
        if (StringUtils.isBlank(getContextPath())) {
            setContextPath(null);
        }
        if (StringUtils.isBlank(getServletPoint())) {
            setServletPoint(null);
        }
    }

    public CmsConfig() {
        super();
    }

    /**
	 * Constructor for primary key
	 */
    public CmsConfig(java.lang.Integer id) {
        super(id);
    }

    /**
	 * Constructor for required fields
	 */
    public CmsConfig(java.lang.Integer id, java.lang.String dbFileUri, java.lang.Boolean uploadToDb, java.lang.String defImg, java.lang.String loginUrl, java.util.Date countClearTime, java.util.Date countCopyTime, java.lang.String downloadCode, java.lang.Integer downloadTime) {
        super(id, dbFileUri, uploadToDb, defImg, loginUrl, countClearTime, countCopyTime, downloadCode, downloadTime);
    }
}
