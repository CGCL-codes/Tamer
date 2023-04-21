package com.liferay.taglib.ui;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <a href="SearchContainerResultsTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class SearchContainerResultsTag extends TagSupport {

    public static final String DEFAULT_RESULTS_VAR = "results";

    public static final String DEFAULT_TOTAL_VAR = "total";

    public int doEndTag() throws JspException {
        try {
            if (_results == null) {
                _results = (List) pageContext.getAttribute(_resultsVar);
                _total = (Integer) pageContext.getAttribute(_totalVar);
            }
            SearchContainerTag parentTag = (SearchContainerTag) findAncestorWithClass(this, SearchContainerTag.class);
            SearchContainer searchContainer = parentTag.getSearchContainer();
            searchContainer.setResults(_results);
            searchContainer.setTotal(_total);
            parentTag.setHasResults(true);
            pageContext.setAttribute(_resultsVar, _results);
            pageContext.setAttribute(_totalVar, _total);
            return EVAL_PAGE;
        } catch (Exception e) {
            throw new JspException(e);
        } finally {
            _results = null;
            _resultsVar = DEFAULT_RESULTS_VAR;
            _total = 0;
            _totalVar = DEFAULT_TOTAL_VAR;
        }
    }

    public int doStartTag() throws JspException {
        SearchContainerTag parentTag = (SearchContainerTag) findAncestorWithClass(this, SearchContainerTag.class);
        if (parentTag == null) {
            throw new JspTagException("Requires liferay-ui:search-container");
        }
        if (_results == null) {
            pageContext.setAttribute(_resultsVar, new ArrayList());
            pageContext.setAttribute(_totalVar, 0);
        }
        return EVAL_BODY_INCLUDE;
    }

    public List getResults() {
        return _results;
    }

    public String getResultsVar() {
        return _resultsVar;
    }

    public int getTotal() {
        return _total;
    }

    public String getTotalVar() {
        return _totalVar;
    }

    public void setResults(List results) {
        _results = results;
    }

    public void setResultsVar(String resultsVar) {
        _resultsVar = resultsVar;
    }

    public void setTotal(int total) {
        _total = total;
    }

    public void setTotalVar(String totalVar) {
        _totalVar = totalVar;
    }

    private List _results;

    private String _resultsVar = DEFAULT_RESULTS_VAR;

    private int _total;

    private String _totalVar = DEFAULT_TOTAL_VAR;
}
