package com.frameworkset.common.tag.pager.db;

import javax.servlet.jsp.JspException;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.BaseTag;

/**
 * 
 * <p>Title: BatchTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-13
 * @author biaoping.yin
 * @version 1.0
 */
public class BatchTag extends BaseTag implements SQLParamsContext {

    private StatementTag statementTag;

    public SQLExecutor getSQLExecutor() {
        return statementTag.getSQLExecutor();
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -717053305505663705L;

    @Override
    public int doEndTag() throws JspException {
        getSQLExecutor().addPreparedBatch();
        statementTag = null;
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() throws JspException {
        statementTag = (StatementTag) findAncestorWithClass(this, StatementTag.class);
        statementTag.setHasbag(true);
        return EVAL_BODY_INCLUDE;
    }
}
