package org.opencms.workplace.galleries;

import org.opencms.file.CmsResource;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.CmsRuntimeException;
import org.opencms.main.OpenCms;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWorkplaceSettings;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;

/**
 * Provides methods for open gallery dialog.<p> 
 * 
 * The following files use this class:
 * <ul>
 * <li>/commons/opengallery.jsp
 * </ul>
 * <p>
 *
 * @author Armen Markarian 
 * 
 * @version $Revision: 1.13 $ 
 * 
 * @since 6.0.0 
 */
public class CmsOpenGallery extends CmsDialog {

    /** The dialog type. */
    public static final String DIALOG_TYPE = "opengallery";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsOpenGallery.class);

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsOpenGallery(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsOpenGallery(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Generates a javascript window open for the requested gallery type.<p>
     * 
     * @return a javascript window open for the requested gallery type
     */
    public String openGallery() {
        StringBuffer jsOpener = new StringBuffer(32);
        String galleryType = null;
        try {
            CmsResource res = getCms().readResource(getParamResource());
            if (res != null) {
                String galleryPath = getParamResource();
                if (!galleryPath.endsWith("/")) {
                    galleryPath += "/";
                }
                galleryType = OpenCms.getResourceManager().getResourceType(res.getTypeId()).getTypeName();
                String galleryUri = A_CmsGallery.PATH_GALLERIES + A_CmsGallery.OPEN_URI_SUFFIX + "?" + A_CmsGallery.PARAM_GALLERY_TYPENAME + "=" + galleryType;
                jsOpener.append("window.open('");
                jsOpener.append(getJsp().link(galleryUri));
                jsOpener.append("&");
                jsOpener.append(A_CmsGallery.PARAM_DIALOGMODE);
                jsOpener.append("=");
                jsOpener.append(A_CmsGallery.MODE_VIEW);
                jsOpener.append("&");
                jsOpener.append(A_CmsGallery.PARAM_GALLERYPATH);
                jsOpener.append("=");
                jsOpener.append(galleryPath);
                jsOpener.append("', '");
                jsOpener.append(galleryType);
                jsOpener.append("','width=650, height=700, resizable=yes, top=100, left=270, status=yes');");
            }
        } catch (CmsException e) {
            CmsMessageContainer message = Messages.get().container(Messages.ERR_OPEN_GALLERY_1, galleryType);
            LOG.error(message.key(), e);
            throw new CmsRuntimeException(message, e);
        }
        return jsOpener.toString();
    }

    /**
     * @see org.opencms.workplace.CmsWorkplace#initWorkplaceRequestValues(org.opencms.workplace.CmsWorkplaceSettings, javax.servlet.http.HttpServletRequest)
     */
    protected void initWorkplaceRequestValues(CmsWorkplaceSettings settings, HttpServletRequest request) {
        fillParamValues(request);
        setParamDialogtype(DIALOG_TYPE);
    }
}
