package com.liferay.taglib.journal;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <a href="StructureSearchTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Prakash Reddy
 *
 */
public class StructureSearchTag extends TagSupport {

    public int doStartTag() throws JspException {
        try {
            List<JournalStructure> structures = JournalStructureLocalServiceUtil.search(_companyId, _groupId, _structureId, _name, _description, _andOperator, _start, _end, _obc);
            pageContext.setAttribute(_var, structures);
            return SKIP_BODY;
        } catch (Exception e) {
            throw new JspException(e);
        }
    }

    public void setAndOperator(boolean andOperator) {
        _andOperator = andOperator;
    }

    public void setCompanyId(long companyId) {
        _companyId = companyId;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public void setEnd(int end) {
        _end = end;
    }

    public void setGroupId(long groupId) {
        _groupId = groupId;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setObc(OrderByComparator obc) {
        _obc = obc;
    }

    public void setStart(int start) {
        _start = start;
    }

    public void setStructureId(String structureId) {
        _structureId = structureId;
    }

    public void setVar(String var) {
        _var = var;
    }

    private boolean _andOperator;

    private long _companyId;

    private String _description;

    private int _end;

    private long _groupId;

    private String _name;

    private OrderByComparator _obc;

    private int _start;

    private String _structureId;

    private String _var;
}
