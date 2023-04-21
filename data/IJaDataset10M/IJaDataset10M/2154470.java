package com.beardediris.ajaqs.taglib;

import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.log4j.Logger;

/**
 * <p>This tag is used to set the active <code>Project</code>, and
 * associate it with some variable.  The syntax for this tag is:
 * <pre>
 *   &lt;ajq:setProject project="${param.project}" userid="user"
 *     scope="page" var="project"/&gt;
 * </pre>
 * The attribute <tt>userid</tt> identifies a <code>FaqUser</code>
 * that should exist in the given scope.  If a <code>Project</code> with
 * the primary key of <tt>project</tt> (here, specified in a URL parameter
 * as <tt>param.project</tt>) exists in the project, then we associate
 * that <code>Project</code> with the given <tt>var</tt>, in the named
 * scope.</p>
 * <p>See the file <tt>ajaqs.tld</tt> for configuration details.</p>
 */
public class SetProjectTag extends GetOrSetTag {

    private static final String TAGNAME = "setProject";

    private static final Logger logger = Logger.getLogger(SetProjectTag.class.getName());

    private String m_project;

    private String m_userid;

    private String m_emptyOk;

    private void init() {
        m_project = null;
        m_userid = null;
        m_emptyOk = null;
    }

    public SetProjectTag() {
        super();
        init();
    }

    public int doStartTag() throws JspException {
        evalExpressions(TAGNAME);
        Integer projId = (Integer) TagHelper.eval("project", m_project, Integer.class, this, pageContext);
        logger.info("projId=" + projId);
        if (null == projId) {
            throw new NullAttributeException("project", TAGNAME);
        }
        String userid = (String) TagHelper.eval("userid", m_userid, String.class, this, pageContext);
        logger.info("userid=" + userid);
        if (null == userid || userid.length() <= 0) {
            throw new NullAttributeException("userid", TAGNAME);
        }
        boolean emptyOk = false;
        if (null != m_emptyOk) {
            Boolean b = (Boolean) TagHelper.eval("emptyOk", m_emptyOk, Boolean.class, this, pageContext);
            emptyOk = b.booleanValue();
        }
        FaqUser fuser = (FaqUser) pageContext.getAttribute(userid, scope);
        if (null == fuser) {
            throw (JspTagException) new JspTagException("User with id \"" + userid + "\" not found");
        }
        Project project = null;
        try {
            project = fuser.getProject(projId.intValue());
            logger.info("found project=" + project.getName());
        } catch (ProjectNotFoundException pnfe) {
            if (!emptyOk) {
                throw (JspTagException) new JspTagException("Could not find project \"" + projId + "\"").initCause(pnfe);
            }
            project = null;
        }
        if (null == project) {
            if (emptyOk) {
                pageContext.setAttribute(var, "", scope);
            } else {
                throw new JspTagException("Could not find project \"" + projId + "\"");
            }
        } else {
            pageContext.setAttribute(var, project, scope);
        }
        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public String getProject() {
        return m_project;
    }

    public void setProject(String project) {
        m_project = project;
    }

    public String getUserid() {
        return m_userid;
    }

    public void setUserid(String userid) {
        m_userid = userid;
    }

    public String getEmptyOk() {
        return m_emptyOk;
    }

    public void setEmptyOk(String emptyOk) {
        m_emptyOk = emptyOk;
    }
}
