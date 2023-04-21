package com.liferay.taglib.ui;

import com.liferay.portal.kernel.dao.search.ResultRow;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

/**
 * <a href="SearchContainerColumnScoreTag.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Raymond Augé
 *
 */
public class SearchContainerColumnScoreTag extends SearchContainerColumnTag {

    private static final String DEFAULT_NAME = "score";

    public int doEndTag() {
        try {
            SearchContainerRowTag parentTag = (SearchContainerRowTag) findAncestorWithClass(this, SearchContainerRowTag.class);
            ResultRow row = parentTag.getRow();
            if (index <= -1) {
                index = row.getEntries().size();
            }
            row.addScore(index, getScore());
            return EVAL_PAGE;
        } finally {
            index = -1;
            _name = DEFAULT_NAME;
            _score = 0;
        }
    }

    public int doStartTag() throws JspException {
        SearchContainerRowTag parentRowTag = (SearchContainerRowTag) findAncestorWithClass(this, SearchContainerRowTag.class);
        if (parentRowTag == null) {
            throw new JspTagException("Requires liferay-ui:search-container-row");
        }
        if (!parentRowTag.isHeaderNamesAssigned()) {
            List<String> headerNames = parentRowTag.getHeaderNames();
            headerNames.add(_name);
        }
        return EVAL_BODY_INCLUDE;
    }

    public float getScore() {
        return _score;
    }

    public void setScore(float score) {
        _score = score;
    }

    private String _name = DEFAULT_NAME;

    private float _score;
}
