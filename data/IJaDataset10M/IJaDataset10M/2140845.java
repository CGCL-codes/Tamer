package org.opencms.workplace.commons;

import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsPropertyDefinition;
import org.opencms.file.CmsResource;
import org.opencms.i18n.CmsEncoder;
import org.opencms.i18n.CmsMessages;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.jsp.CmsJspNavBuilder;
import org.opencms.jsp.CmsJspNavElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.security.CmsPermissionSet;
import org.opencms.util.CmsStringUtil;
import org.opencms.workplace.CmsDialog;
import org.opencms.workplace.CmsWorkplace;
import org.opencms.workplace.CmsWorkplaceSettings;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;

/**
 * Provides methods for the change navigation dialog.<p> 
 * 
 * The following files use this class:
 * <ul>
 * <li>/commons/chnav.jsp
 * </ul>
 * <p>
 *
 * @author  Andreas Zahner 
 * 
 * @version $Revision: 1.28 $ 
 * 
 * @since 6.0.0 
 */
public class CmsChnav extends CmsDialog {

    /** Value for the action: change the navigation. */
    public static final int ACTION_CHNAV = 100;

    /** The dialog type. */
    public static final String DIALOG_TYPE = "chnav";

    /** Request parameter name for the navigation position. */
    public static final String PARAM_NAVPOS = "navpos";

    /** Request parameter name for the navigation text. */
    public static final String PARAM_NAVTEXT = "navtext";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(CmsChnav.class);

    private String m_paramNavpos;

    private String m_paramNavtext;

    /**
     * Public constructor.<p>
     * 
     * @param jsp an initialized JSP action element
     */
    public CmsChnav(CmsJspActionElement jsp) {
        super(jsp);
    }

    /**
     * Public constructor with JSP variables.<p>
     * 
     * @param context the JSP page context
     * @param req the JSP request
     * @param res the JSP response
     */
    public CmsChnav(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        this(new CmsJspActionElement(context, req, res));
    }

    /**
     * Builds the HTML for the select box of the navigation position.<p>
     * 
     * @param cms the CmsObject
     * @param filename the current file
     * @param attributes optional attributes for the &lt;select&gt; tag, do not add the "name" atribute!
     * @param messages the localized workplace messages
     * 
     * @return the HTML for a navigation position select box
     */
    public static String buildNavPosSelector(CmsObject cms, String filename, String attributes, CmsMessages messages) {
        CmsJspNavElement curNav = CmsJspNavBuilder.getNavigationForResource(cms, filename);
        filename = CmsResource.getParentFolder(filename);
        List navList = CmsJspNavBuilder.getNavigationForFolder(cms, filename);
        float maxValue = 0;
        float nextPos = 0;
        float firstValue = 1;
        if (navList.size() > 0) {
            try {
                CmsJspNavElement ne = (CmsJspNavElement) navList.get(0);
                maxValue = ne.getNavPosition();
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage());
            }
        }
        if (maxValue != 0) {
            firstValue = maxValue / 2;
        }
        List options = new ArrayList(navList.size() + 1);
        List values = new ArrayList(navList.size() + 1);
        options.add(messages.key(Messages.GUI_CHNAV_POS_FIRST_0));
        values.add(firstValue + "");
        for (int i = 0; i < navList.size(); i++) {
            CmsJspNavElement ne = (CmsJspNavElement) navList.get(i);
            String navText = ne.getNavText();
            float navPos = ne.getNavPosition();
            nextPos = navPos + 2;
            if ((i + 1) < navList.size()) {
                nextPos = ((CmsJspNavElement) navList.get(i + 1)).getNavPosition();
            }
            float newPos;
            if ((nextPos - navPos) > 1) {
                newPos = navPos + 1;
            } else {
                newPos = (navPos + nextPos) / 2;
            }
            if (navPos > maxValue) {
                maxValue = navPos;
            }
            if (curNav.getNavText().equals(navText) && (curNav.getNavPosition() == navPos)) {
                options.add(CmsEncoder.escapeHtml(messages.key(Messages.GUI_CHNAV_POS_CURRENT_1, new Object[] { ne.getFileName() })));
                values.add("-1");
            } else {
                options.add(CmsEncoder.escapeHtml(navText + " [" + ne.getFileName() + "]"));
                values.add(newPos + "");
            }
        }
        options.add(messages.key(Messages.GUI_CHNAV_POS_LAST_0));
        values.add((maxValue + 1) + "");
        options.add(messages.key(Messages.GUI_CHNAV_NO_CHANGE_0));
        if (curNav.getNavPosition() == Float.MAX_VALUE) {
            values.add((maxValue + 1) + "");
        } else {
            values.add("-1");
        }
        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(attributes)) {
            attributes = " " + attributes;
        } else {
            attributes = "";
        }
        return CmsWorkplace.buildSelect("name=\"" + PARAM_NAVPOS + "\"" + attributes, options, values, values.size() - 1, true);
    }

    /**
     * Performs the navigation change.<p>
     * 
     * @throws JspException if including a JSP subelement is not successful
     */
    public void actionChangeNav() throws JspException {
        getJsp().getRequest().setAttribute(SESSION_WORKPLACE_CLASS, this);
        String filename = getParamResource();
        String newText = getJsp().getRequest().getParameter(PARAM_NAVTEXT);
        String selectedPosString = getParamNavpos();
        try {
            checkLock(getParamResource());
            if (newText != null) {
                CmsProperty newNavText = new CmsProperty();
                newNavText.setName(CmsPropertyDefinition.PROPERTY_NAVTEXT);
                CmsProperty oldNavText = getCms().readPropertyObject(filename, CmsPropertyDefinition.PROPERTY_NAVTEXT, false);
                if (oldNavText.isNullProperty()) {
                    if (OpenCms.getWorkplaceManager().isDefaultPropertiesOnStructure()) {
                        newNavText.setStructureValue(newText);
                    } else {
                        newNavText.setResourceValue(newText);
                    }
                } else {
                    if (oldNavText.getStructureValue() != null) {
                        newNavText.setStructureValue(newText);
                        newNavText.setResourceValue(oldNavText.getResourceValue());
                    } else {
                        newNavText.setResourceValue(newText);
                    }
                }
                String oldStructureValue = oldNavText.getStructureValue();
                String newStructureValue = newNavText.getStructureValue();
                if (CmsStringUtil.isEmpty(oldStructureValue)) {
                    oldStructureValue = CmsProperty.DELETE_VALUE;
                }
                if (CmsStringUtil.isEmpty(newStructureValue)) {
                    newStructureValue = CmsProperty.DELETE_VALUE;
                }
                String oldResourceValue = oldNavText.getResourceValue();
                String newResourceValue = newNavText.getResourceValue();
                if (CmsStringUtil.isEmpty(oldResourceValue)) {
                    oldResourceValue = CmsProperty.DELETE_VALUE;
                }
                if (CmsStringUtil.isEmpty(newResourceValue)) {
                    newResourceValue = CmsProperty.DELETE_VALUE;
                }
                if (!oldResourceValue.equals(newResourceValue) || !oldStructureValue.equals(newStructureValue)) {
                    getCms().writePropertyObject(getParamResource(), newNavText);
                }
            }
            float selectedPos = -1;
            try {
                selectedPos = Float.parseFloat(selectedPosString);
            } catch (Exception e) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(e.getLocalizedMessage());
                }
            }
            if (selectedPos != -1) {
                CmsProperty newNavPos = new CmsProperty();
                newNavPos.setName(CmsPropertyDefinition.PROPERTY_NAVPOS);
                CmsProperty oldNavPos = getCms().readPropertyObject(filename, CmsPropertyDefinition.PROPERTY_NAVPOS, false);
                if (oldNavPos.isNullProperty()) {
                    if (OpenCms.getWorkplaceManager().isDefaultPropertiesOnStructure()) {
                        newNavPos.setStructureValue(selectedPosString);
                    } else {
                        newNavPos.setResourceValue(selectedPosString);
                    }
                } else {
                    if (oldNavPos.getStructureValue() != null) {
                        newNavPos.setStructureValue(selectedPosString);
                        newNavPos.setResourceValue(oldNavPos.getResourceValue());
                    } else {
                        newNavPos.setResourceValue(selectedPosString);
                    }
                }
                getCms().writePropertyObject(filename, newNavPos);
            }
        } catch (Throwable e) {
            includeErrorpage(this, e);
        }
        actionCloseDialog();
    }

    /**
     * Builds the HTML for the select box of the navigation position.<p>
     * 
     * @return the HTML for a navigation position select box
     */
    public String buildNavPosSelector() {
        return buildNavPosSelector(getCms(), getParamResource(), null, getMessages());
    }

    /**
     * Returns the escaped NavText property value of the current resource.<p>
     * 
     * @return the NavText property value of the current resource
     */
    public String getCurrentNavText() {
        try {
            String navText = getCms().readPropertyObject(getParamResource(), CmsPropertyDefinition.PROPERTY_NAVTEXT, false).getValue();
            if (navText == null) {
                navText = "";
            }
            return CmsEncoder.escapeXml(navText);
        } catch (CmsException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(e.getLocalizedMessage());
            }
            return "";
        }
    }

    /**
     * Returns the value of the navigation position parameter, 
     * or null if this parameter was not provided.<p>
     * 
     * The navigation position parameter defines the new value for 
     * the NavPos property.<p>
     * 
     * @return the value of the target parameter
     */
    public String getParamNavpos() {
        return m_paramNavpos;
    }

    /**
     * Returns the value of the navigation text parameter, 
     * or null if this parameter was not provided.<p>
     * 
     * The navigation text parameter defines the new value for 
     * the NavText property.<p>
     * 
     * @return the value of the target parameter
     */
    public String getParamNavtext() {
        return m_paramNavtext;
    }

    /**
     * Sets the value of the navigation position parameter.<p>
     * 
     * @param value the value to set
     */
    public void setParamNavpos(String value) {
        m_paramNavpos = value;
    }

    /**
     * Sets the value of the navigation text parameter.<p>
     * 
     * @param value the value to set
     */
    public void setParamNavtext(String value) {
        m_paramNavtext = value;
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
        if (DIALOG_TYPE.equals(getParamAction())) {
            setAction(ACTION_CHNAV);
        } else if (DIALOG_LOCKS_CONFIRMED.equals(getParamAction())) {
            setAction(ACTION_LOCKS_CONFIRMED);
        } else if (DIALOG_CANCEL.equals(getParamAction())) {
            setAction(ACTION_CANCEL);
        } else {
            setAction(ACTION_DEFAULT);
            setParamTitle(key(Messages.GUI_CHNAV_1, new Object[] { CmsResource.getName(getParamResource()) }));
        }
    }
}
