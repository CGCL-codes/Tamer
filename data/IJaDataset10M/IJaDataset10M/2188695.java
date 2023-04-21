package com.dotmarketing.portlets.htmlpages.struts;

import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorForm;
import com.dotmarketing.beans.Host;
import com.dotmarketing.factories.HostFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.folders.model.Folder;
import com.liferay.portal.util.Constants;

/** @author Hibernate CodeGenerator */
public class HTMLPageForm extends ValidatorForm {

    private static final long serialVersionUID = 1L;

    /** identifier field */
    private long parent;

    /** nullable persistent field */
    private String selectedparent;

    /** nullable persistent field */
    private String selectedparentPath;

    /** nullable persistent field */
    private String metadata;

    /** nullable persistent field */
    private java.util.Date startDate;

    /** nullable persistent field */
    private java.util.Date endDate;

    /** nullable persistent field */
    private String webStartDate;

    /** nullable persistent field */
    private String webEndDate;

    /** nullable persistent field */
    private String pageUrl;

    /** nullable persistent field */
    private boolean httpsRequired;

    /** nullable persistent field */
    private String redirect;

    /** nullable persistent field */
    private long template;

    /** nullable persistent field */
    private String selectedtemplate;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String friendlyName;

    /** nullable persistent field */
    private boolean showOnMenu;

    /** nullable persistent field */
    private int sortOrder;

    /*** WEB ASSET FIELDS FOR THE FORM ***/
    public HTMLPageForm() {
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors ae = new ActionErrors();
        if (request.getParameter("cmd") != null && request.getParameter("cmd").equals(Constants.ADD)) {
            String inode = request.getParameter("parent");
            Folder parentFolder = (Folder) InodeFactory.getInode(inode, Folder.class);
            if (parentFolder.getInode() == 0) {
                Host host = (Host) HostFactory.getHost(inode);
                if (host.getInode() != 0) {
                    ae.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.folder.ishostfolder"));
                }
            }
        }
        return ae;
    }

    /**
	 * Returns the endDate.
	 * @return java.util.Date
	 */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /**
	 * Returns the httpsRequired.
	 * @return boolean
	 */
    public boolean isHttpsRequired() {
        return httpsRequired;
    }

    /**
	 * Returns the metadata.
	 * @return String
	 */
    public String getMetadata() {
        return metadata;
    }

    /**
	 * Returns the pageUrl.
	 * @return String
	 */
    public String getPageUrl() {
        return pageUrl;
    }

    /**
	 * Returns the parent.
	 * @return long
	 */
    public long getParent() {
        return parent;
    }

    /**
	 * Returns the redirect.
	 * @return String
	 */
    public String getRedirect() {
        return redirect;
    }

    /**
	 * Returns the startDate.
	 * @return java.util.Date
	 */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /**
	 * Sets the endDate.
	 * @param endDate The endDate to set
	 */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    /**
	 * Sets the httpsRequired.
	 * @param httpsRequired The httpsRequired to set
	 */
    public void setHttpsRequired(boolean httpsRequired) {
        this.httpsRequired = httpsRequired;
    }

    /**
	 * Sets the metadata.
	 * @param metadata The metadata to set
	 */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    /**
	 * Sets the pageUrl.
	 * @param pageUrl The pageUrl to set
	 */
    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
    public void setParent(long parent) {
        this.parent = parent;
    }

    /**
	 * Sets the redirect.
	 * @param redirect The redirect to set
	 */
    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    /**
	 * Sets the startDate.
	 * @param startDate The startDate to set
	 */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /**
	 * Returns the webEndDate.
	 * @return String
	 */
    public String getWebEndDate() {
        return webEndDate;
    }

    /**
	 * Returns the webStartDate.
	 * @return String
	 */
    public String getWebStartDate() {
        return webStartDate;
    }

    /**
	 * Sets the webEndDate.
	 * @param webEndDate The webEndDate to set
	 */
    public void setWebEndDate(String webEndDate) {
        this.webEndDate = webEndDate;
        try {
            this.endDate = new SimpleDateFormat("MM/dd/yyyy").parse(webEndDate);
        } catch (Exception ex) {
        }
    }

    /**
	 * Sets the webStartDate.
	 * @param webStartDate The webStartDate to set
	 */
    public void setWebStartDate(String webStartDate) {
        this.webStartDate = webStartDate;
        try {
            this.startDate = new SimpleDateFormat("MM/dd/yyyy").parse(webStartDate);
        } catch (Exception ex) {
        }
    }

    /**
	 * Returns the friendlyName.
	 * @return String
	 */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
	 * Returns the showOnMenu.
	 * @return boolean
	 */
    public boolean isShowOnMenu() {
        return showOnMenu;
    }

    /**
	 * Returns the sortOrder.
	 * @return int
	 */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
	 * Returns the title.
	 * @return String
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the friendlyName.
	 * @param friendlyName The friendlyName to set
	 */
    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    /**
	 * Sets the showOnMenu.
	 * @param showOnMenu The showOnMenu to set
	 */
    public void setShowOnMenu(boolean showOnMenu) {
        this.showOnMenu = showOnMenu;
    }

    /**
	 * Sets the sortOrder.
	 * @param sortOrder The sortOrder to set
	 */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
	 * Sets the title.
	 * @param title The title to set
	 */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * Returns the selectedtemplate.
	 * @return String
	 */
    public String getSelectedtemplate() {
        return selectedtemplate;
    }

    /**
	 * Returns the template.
	 * @return long
	 */
    public long getTemplate() {
        return template;
    }

    /**
	 * Sets the selectedtemplate.
	 * @param selectedtemplate The selectedtemplate to set
	 */
    public void setSelectedtemplate(String selectedtemplate) {
        this.selectedtemplate = selectedtemplate;
    }

    /**
	 * Sets the template.
	 * @param template The template to set
	 */
    public void setTemplate(long template) {
        this.template = template;
    }

    /**
	 * Returns the selectedparent.
	 * @return String
	 */
    public String getSelectedparent() {
        return selectedparent;
    }

    /**
	 * Sets the selectedparent.
	 * @param selectedparent The selectedparent to set
	 */
    public void setSelectedparent(String selectedparent) {
        this.selectedparent = selectedparent;
    }

    /**
	 * Returns the selectedparentPath.
	 * @return String
	 */
    public String getSelectedparentPath() {
        return selectedparentPath;
    }

    /**
	 * Sets the selectedparentPath.
	 * @param selectedparentPath The selectedparentPath to set
	 */
    public void setSelectedparentPath(String selectedparentPath) {
        this.selectedparentPath = selectedparentPath;
    }
}
