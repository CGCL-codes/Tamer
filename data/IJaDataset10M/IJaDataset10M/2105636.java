package org.opencms.workplace.commons;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsResource;
import org.opencms.i18n.CmsEncoder;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.security.CmsPermissionSet;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWorkplaceSettings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * The edit pointer dialog changes the link target of a pointer resource.<p>
 * 
 * The following files use this class:
 * <ul>
 * <li>/commons/editpointer.jsp
 * </ul>
 * <p>
 * 
 * @author Andreas Zahner 
 * 
 * @version $Revision: 1.16 $ 
 * 
 * @since 6.0.0 
 */
public class CmsEditPointer extends CmsDialog {

    /** The dialog type.<p> */
    public static final String DIALOG_TYPE = "newlink";

    /** Request parameter name for the link target.<p> */
    public static final String PARAM_LINKTARGET = "linktarget";

    /** Stores the value of the link target.<p> */
    private String m_paramLinkTarget;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsEditPointer(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsEditPointer(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Changes the link target of the pointer.<p>
     * 
     * @throws JspException if inclusion of error dialog fails
     */
    public void actionChangeLinkTarget() throws JspException {
        try {
            checkLock(getParamResource());
            CmsFile editFile = getCms().readFile(getParamResource());
            editFile.setContents(getParamLinkTarget().getBytes());
            getCms().writeFile(editFile);
            actionCloseDialog();
        } catch (Throwable e) {
            setParamMessage(Messages.get().getBundle(getLocale()).key(Messages.ERR_CHANGE_LINK_TARGET_0));
            includeErrorpage(this, e);
        }
    }

    /**
     * Returns the old link target value of the pointer resource to edit.<p>
     * 
     * @return the old link target value
     * @throws JspException if problems including sub-elements occur 
     * 
     */
    public String getOldTargetValue() throws JspException {
        String linkTarget = "";
        if (CmsStringUtil.isEmpty(getParamLinkTarget())) {
            try {
                CmsFile file = getCms().readFile(getParamResource());
                linkTarget = new String(file.getContents());
            } catch (Throwable e1) {
                setParamMessage(Messages.get().getBundle(getLocale()).key(Messages.ERR_GET_LINK_TARGET_1, getParamResource()));
                includeErrorpage(this, e1);
            }
        }
        return CmsEncoder.escapeXml(linkTarget);
    }

    /**
     * Returns the link target request parameter value.<p>
     * 
     * @return the link target request parameter value
     */
    public String getParamLinkTarget() {
        return m_paramLinkTarget;
    }

    /**
     * Sets the link target request parameter value.<p>
     * 
     * @param linkTarget the link target request parameter value
     */
    public void setParamLinkTarget(String linkTarget) {
        m_paramLinkTarget = linkTarget;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {
        fillParamValues(request);
        if (!checkResourcePermissions(CmsPermissionSet.ACCESS_WRITE, false)) {
            setParamAction(DIALOG_CANCEL);
        }
        setParamDialogtype(DIALOG_TYPE);
        if (DIALOG_OK.equals(getParamAction())) {
            setAction(ACTION_OK);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            setAction(ACTION_CANCEL);
        } else {
            setAction(ACTION_DEFAULT);
            setParamTitle(key(Messages.GUI_CHLINK_1, new Object[] { CmsResource.getName(getParamResource()) }));
        }
    }
}
