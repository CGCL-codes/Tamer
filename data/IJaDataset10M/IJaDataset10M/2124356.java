package org.opencms.workplace.galleries;

import org.opencms.file.CmsProperty;
import org.opencms.file.CmsPropertyDefinition;
import org.opencms.file.CmsResource;
import org.opencms.file.types.CmsResourceTypeImage;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.loader.CmsImageLoader;
import org.opencms.loader.CmsImageScaler;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsStringUtil;
import java.awt.Color;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 * Generates the image gallery popup window which can be used in editors or as a dialog widget.<p>
 * 
 * @author Andreas Zahner 
 * @author Armen Markarian 
 * 
 * @version $Revision: 1.18 $ 
 * 
 * @since 6.0.0 
 */
public class CmsImageGallery extends A_CmsGallery {

    /** URI of the image gallery popup dialog. */
    public static final String URI_GALLERY = PATH_GALLERIES + "img_fs.jsp";

    /** The order value of the gallery for sorting the galleries. */
    private static final Integer ORDER_GALLERY = new Integer(10);

    /** The default image scaling parameters for the gallery preview. */
    private CmsImageScaler m_defaultScaleParams;

    /**
     * Public empty constructor, required for {@link A_CmsGallery#createInstance(String, CmsJspActionElement)}.<p>
     */
    public CmsImageGallery() {
    }

    /**
     * Public constructor with JSP action element.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsImageGallery(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsImageGallery(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#applyButton()
     */
    public String applyButton() {
        String width = null;
        String height = null;
        if (MODE_VIEW.equals(getParamDialogMode())) {
            return button(null, null, "apply_in.png", Messages.GUI_BUTTON_PASTE_0, 0);
        } else {
            String uri = getParamResourcePath();
            if (CmsStringUtil.isEmpty(getParamDialogMode())) {
                uri = getJsp().link(uri);
                if (CmsImageLoader.isEnabled()) {
                    try {
                        CmsProperty imageSize = getJsp().getCmsObject().readPropertyObject(getParamResourcePath(), CmsPropertyDefinition.PROPERTY_IMAGE_SIZE, false);
                        if (!imageSize.isNullProperty()) {
                            CmsImageScaler scaler = new CmsImageScaler(imageSize.getValue());
                            if (scaler.getWidth() > 0) {
                                width = String.valueOf(scaler.getWidth());
                            }
                            if (scaler.getHeight() > 0) {
                                height = String.valueOf(scaler.getHeight());
                            }
                        }
                    } catch (CmsException e) {
                    }
                }
            }
            return button("javascript:pasteImage('" + uri + "',document.form.title.value, document.form.title.value," + width + "," + height + ");", null, "apply.png", Messages.GUI_BUTTON_PASTE_0, 0);
        }
    }

    /**
     * Builds the html String for the preview frame.<p>
     * 
     * @return the html String for the preview frame
     */
    public String buildGalleryItemPreview() {
        StringBuffer html = new StringBuffer(16);
        try {
            if (CmsStringUtil.isNotEmpty(getParamResourcePath())) {
                CmsResource res = getCms().readResource(getParamResourcePath());
                if (res != null) {
                    html.append("<img alt=\"\" src=\"");
                    html.append(getJsp().link(getParamResourcePath()));
                    html.append("\" border=\"0\">");
                }
            }
        } catch (CmsException e) {
            CmsLog.getLog(CmsImageGallery.class).error(e);
        }
        return html.toString();
    }

    /**
     * Returns the default image scaling parameters for the gallery preview.<p>
     * 
     * @return the default image scaling parameters for the gallery preview
     */
    public CmsImageScaler getDefaultScaleParams() {
        return m_defaultScaleParams;
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#getGalleryItemsTypeId()
     */
    public int getGalleryItemsTypeId() {
        return CmsResourceTypeImage.getStaticTypeId();
    }

    /**
     * Returns the order of the implemented gallery, used to sort the gallery buttons in the editors.<p>
     * 
     * @return the order of the implemented gallery
     */
    public Integer getOrder() {
        return ORDER_GALLERY;
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#getPreviewBodyStyle()
     */
    public String getPreviewBodyStyle() {
        return "";
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#init()
     */
    public void init() {
        if (CmsImageLoader.isEnabled()) {
            m_defaultScaleParams = new CmsImageScaler(getGalleryTypeParams());
            if (!m_defaultScaleParams.isValid()) {
                m_defaultScaleParams.setType(0);
                m_defaultScaleParams.setPosition(0);
                m_defaultScaleParams.setWidth(120);
                m_defaultScaleParams.setHeight(90);
                m_defaultScaleParams.setColor(new Color(221, 221, 221));
            }
        } else {
            m_defaultScaleParams = null;
        }
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#previewButton()
     */
    public String previewButton() {
        return "";
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#targetSelectBox()
     */
    public String targetSelectBox() {
        return "";
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#buildGalleryItemListCustomEndCols(org.opencms.file.CmsResource, java.lang.String)
     */
    protected String buildGalleryItemListCustomEndCols(CmsResource res, String tdClass) {
        if (!CmsImageLoader.isEnabled()) {
            return super.buildGalleryItemListCustomEndCols(res, tdClass);
        }
        StringBuffer result = new StringBuffer(128);
        CmsImageScaler scaler = new CmsImageScaler(getCms(), res);
        result.append("\t<td class=\"");
        result.append(tdClass);
        result.append("\" style=\"text-align: right;\">");
        if (scaler.isValid()) {
            result.append(scaler.getWidth());
            result.append("*");
            result.append(scaler.getHeight());
            result.append(" ");
            result.append(key(Messages.GUI_LABEL_PIXELS_0));
            result.append(" / ");
        }
        result.append(res.getLength() / 1024);
        result.append(" ");
        result.append(key(Messages.GUI_LABEL_KILOBYTES_0));
        result.append("</td>\n");
        return result.toString();
    }

    /**
     * @see org.opencms.workplace.galleries.A_CmsGallery#buildGalleryItemListCustomStartCols(org.opencms.file.CmsResource, java.lang.String)
     */
    protected String buildGalleryItemListCustomStartCols(CmsResource res, String tdClass) {
        if (!CmsImageLoader.isEnabled()) {
            return super.buildGalleryItemListCustomStartCols(res, tdClass);
        }
        CmsProperty sizeProp = CmsProperty.getNullProperty();
        try {
            sizeProp = getCms().readPropertyObject(res, CmsPropertyDefinition.PROPERTY_IMAGE_SIZE, false);
        } catch (Exception e) {
        }
        if (sizeProp.isNullProperty()) {
            return super.buildGalleryItemListCustomStartCols(res, tdClass);
        }
        StringBuffer result = new StringBuffer(128);
        if ((m_defaultScaleParams != null) && m_defaultScaleParams.isValid()) {
            String resPath = getCms().getSitePath(res);
            result.append("\t<td class=\"");
            result.append(tdClass);
            result.append("\">");
            result.append("<a class=\"");
            result.append(tdClass);
            result.append("\" href=\"javascript: preview(\'");
            result.append(resPath);
            result.append("\');\" title=\"");
            result.append(key(Messages.GUI_BUTTON_PREVIEW_0));
            result.append("\">");
            result.append("<img src=\"");
            result.append(getJsp().link(resPath + m_defaultScaleParams.toRequestParam()));
            result.append("\" border=\"0\" width=\"");
            result.append(m_defaultScaleParams.getWidth());
            result.append("\" height=\"");
            result.append(m_defaultScaleParams.getHeight());
            result.append("\"></a></td>\n");
            result.append("</td>\n");
        } else {
            result.append(super.buildGalleryItemListCustomStartCols(res, tdClass));
        }
        return result.toString();
    }
}
