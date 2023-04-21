package org.opencms.workplace.explorer;

import org.opencms.file.CmsResource;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.lock.CmsLock;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.workplace.CmsWorkplaceSettings;
import org.opencms.workplace.commons.CmsPropertyAdvanced;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;

/**
 * The new resource sibling dialog handles the creation of a new sibling in the VFS.<p>
 * 
 * The following files use this class:
 * <ul>
 * <li>/commons/newresource_sibling.html
 * </ul>
 * <p>
 * 
 * @author Andreas Zahner 
 * 
 * @version $Revision: 1.19 $ 
 * 
 * @since 6.0.0 
 */
public class CmsNewResourceSibling extends CmsNewResourcePointer {

    /** Request parameter name for the keep properties flag. */
    public static final String PARAM_KEEPPROPERTIES = "keepproperties";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsNewResourceSibling.class);

    private String m_paramKeepProperties;

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsNewResourceSibling(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsNewResourceSibling(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Creates the new sibling of a resource.<p>
     * 
     * @throws JspException if inclusion of error dialog fails
     */
    public void actionCreateResource() throws JspException {
        try {
            String fullResourceName = computeFullResourceName();
            String newResourceParam = fullResourceName;
            String targetName = getParamLinkTarget();
            if (targetName == null) {
                targetName = "";
            }
            String storedSiteRoot = null;
            try {
                if (OpenCms.getSiteManager().getSiteRoot(targetName) != null) {
                    String siteRootFolder = getCms().getRequestContext().getSiteRoot();
                    if (siteRootFolder.endsWith("/")) {
                        siteRootFolder = siteRootFolder.substring(0, siteRootFolder.length() - 1);
                    }
                    fullResourceName = siteRootFolder + fullResourceName;
                    storedSiteRoot = getCms().getRequestContext().getSiteRoot();
                    getCms().getRequestContext().setSiteRoot("/");
                }
                boolean isFolder = false;
                CmsResource targetRes = getCms().readResource(targetName);
                isFolder = targetRes.isFolder();
                if (isFolder) {
                    if (targetName.endsWith("/")) {
                        targetName = targetName.substring(0, targetName.length() - 1);
                    }
                    getCms().copyResource(targetName, fullResourceName, CmsResource.COPY_AS_SIBLING);
                } else {
                    List targetProperties = null;
                    boolean keepProperties = Boolean.valueOf(getParamKeepProperties()).booleanValue();
                    if (keepProperties) {
                        try {
                            targetProperties = getCms().readPropertyObjects(targetName, false);
                        } catch (Exception e) {
                            LOG.error(e.getLocalizedMessage(), e);
                        }
                    }
                    getCms().createSibling(targetName, fullResourceName, targetProperties);
                }
            } finally {
                if (storedSiteRoot != null) {
                    getCms().getRequestContext().setSiteRoot(storedSiteRoot);
                }
            }
            setParamResource(newResourceParam);
            setResourceCreated(true);
        } catch (Throwable e) {
            setParamMessage(Messages.get().getBundle(getLocale()).key(Messages.ERR_CREATE_LINK_0));
            includeErrorpage(this, e);
        }
    }

    /**
     * Forwards to the property dialog if the resourceeditprops parameter is true.<p>
     * 
     * If the parameter is not true, the dialog will be closed.<p>
     * If the sibling of the new resource is locked, the paramter will be ignored as properties
     * cannot be created in this case.<p>
     * 
     * @throws IOException if forwarding to the property dialog fails
     * @throws ServletException if forwarding to the property dialog fails
     * @throws JspException if an inclusion fails
     */
    public void actionEditProperties() throws IOException, JspException, ServletException {
        boolean editProps = Boolean.valueOf(getParamNewResourceEditProps()).booleanValue();
        String newRes = getParamResource();
        try {
            CmsLock lock = getCms().getLock(newRes);
            if (!lock.isExclusive()) {
                editProps = false;
            }
        } catch (CmsException e) {
            throw new JspException(e);
        }
        if (editProps) {
            Map params = new HashMap();
            params.put(PARAM_RESOURCE, getParamResource());
            params.put(CmsPropertyAdvanced.PARAM_DIALOGMODE, CmsPropertyAdvanced.MODE_WIZARD);
            sendForward(CmsPropertyAdvanced.URI_PROPERTY_DIALOG_HANDLER, params);
        } else {
            actionCloseDialog();
        }
    }

    /**
     * Returns the current explorer path for use in Javascript of new sibling dialog.<p>
     * 
     * @return the current explorer path
     */
    public String getCurrentPath() {
        String path = getSettings().getExplorerResource();
        if (path == null) {
            path = "/";
        }
        return CmsResource.getFolderPath(path);
    }

    /**
     * Returns the keep properties request parameter value.<p>
     * 
     * @return the keep properties request parameter value
     */
    public String getParamKeepProperties() {
        return m_paramKeepProperties;
    }

    /**
     * Sets the keep properties request parameter value.<p>
     * 
     * @param keepProperties the keep properties request parameter value
     */
    public void setParamKeepProperties(String keepProperties) {
        m_paramKeepProperties = keepProperties;
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {
        fillParamValues(request);
        setParamDialogtype(DIALOG_TYPE);
        if (DIALOG_OK.equals(getParamAction())) {
            setAction(ACTION_OK);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            setAction(ACTION_CANCEL);
        } else {
            setAction(ACTION_DEFAULT);
            setParamTitle(key(Messages.GUI_NEWRESOURCE_SIBLING_0));
        }
    }
}
