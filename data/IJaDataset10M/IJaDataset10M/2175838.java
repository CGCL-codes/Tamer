package com.liferay.taglib.ui;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.PortalIncludeUtil;
import com.liferay.util.Html;
import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <a href="TableIteratorTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TableIteratorTag extends TagSupport {

    public int doStartTag() throws JspException {
        try {
            if (_list.size() > 0) {
                ServletRequest req = pageContext.getRequest();
                req.setAttribute("liferay-ui:table-iterator:list", _list);
                req.setAttribute("liferay-ui:table-iterator:rowLength", String.valueOf(_rowLength));
                req.setAttribute("liferay-ui:table-iterator:rowPadding", _rowPadding);
                req.setAttribute("liferay-ui:table-iterator:rowValign", _rowValign);
                req.setAttribute("liferay-ui:table-iterator:rowBreak", _rowBreak);
                PortalIncludeUtil.include(pageContext, getStartPage());
                pageContext.setAttribute("tableIteratorObj", _list.get(_listPos));
                pageContext.setAttribute("tableIteratorPos", new Integer(_listPos));
                return EVAL_BODY_INCLUDE;
            } else {
                return SKIP_BODY;
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public int doAfterBody() throws JspException {
        try {
            ServletRequest req = pageContext.getRequest();
            req.setAttribute("liferay-ui:table-iterator:listPos", String.valueOf(_listPos));
            PortalIncludeUtil.include(pageContext, getBodyPage());
            _listPos++;
            if (_listPos < _list.size()) {
                pageContext.setAttribute("tableIteratorObj", _list.get(_listPos));
                pageContext.setAttribute("tableIteratorPos", new Integer(_listPos));
                return EVAL_BODY_AGAIN;
            } else {
                return SKIP_BODY;
            }
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public int doEndTag() throws JspException {
        try {
            if (_list.size() > 0) {
                PortalIncludeUtil.include(pageContext, getEndPage());
            }
            return EVAL_PAGE;
        } catch (Exception e) {
            throw new JspException(e);
        } finally {
            _startPage = null;
            _bodyPage = null;
            _endPage = null;
            _list = null;
            _listPos = 0;
            _rowLength = 0;
            _rowPadding = "0";
            _rowValign = "middle";
            _rowBreak = null;
        }
    }

    public String getStartPage() {
        if (Validator.isNull(_startPage)) {
            return _START_PAGE;
        } else {
            return _startPage;
        }
    }

    public void setStartPage(String startPage) {
        _startPage = startPage;
    }

    public String getBodyPage() {
        if (Validator.isNull(_bodyPage)) {
            return _BODY_PAGE;
        } else {
            return _bodyPage;
        }
    }

    public void setBodyPage(String bodyPage) {
        _bodyPage = bodyPage;
    }

    public String getEndPage() {
        if (Validator.isNull(_endPage)) {
            return _END_PAGE;
        } else {
            return _endPage;
        }
    }

    public void setEndPage(String endPage) {
        _endPage = endPage;
    }

    public void setList(List list) {
        _list = list;
    }

    public void setListType(String listType) {
    }

    public void setRowLength(String rowLength) {
        _rowLength = GetterUtil.getInteger(rowLength);
    }

    public void setRowPadding(String rowPadding) {
        _rowPadding = rowPadding;
    }

    public void setRowValign(String rowValign) {
        _rowValign = rowValign;
    }

    public void setRowBreak(String rowBreak) {
        _rowBreak = Html.formatFrom(rowBreak);
    }

    private static final String _START_PAGE = "/html/taglib/ui/table_iterator/start.jsp";

    private static final String _BODY_PAGE = "/html/taglib/ui/table_iterator/body.jsp";

    private static final String _END_PAGE = "/html/taglib/ui/table_iterator/end.jsp";

    private String _startPage;

    private String _bodyPage;

    private String _endPage;

    private List _list;

    private int _listPos;

    private int _rowLength;

    private String _rowPadding = "0";

    private String _rowValign = "middle";

    private String _rowBreak = "<br />";
}
