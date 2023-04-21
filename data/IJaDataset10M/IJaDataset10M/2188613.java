package net.sf.poormans.server.lifecycle;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.poormans.Constants;
import net.sf.poormans.configuration.InitializationManager;
import net.sf.poormans.model.domain.IPersistentPojo;
import net.sf.poormans.model.domain.IRenderable;
import net.sf.poormans.model.domain.pojo.Gallery;
import net.sf.poormans.model.domain.pojo.Image;
import net.sf.poormans.model.domain.pojo.Macro;
import net.sf.poormans.model.domain.pojo.Page;
import net.sf.poormans.model.domain.pojo.SiteResource;
import net.sf.poormans.model.domain.pojo.Template;
import net.sf.poormans.model.persistence.dao.IMaintenanceDAO;
import net.sf.poormans.tool.PathTool;
import net.sf.poormans.view.ViewMode;
import org.apache.commons.lang.StringUtils;

/**
 * Provide/generate all data, which is relevant for a servlet lifecycle.
 * 
 * @version $Id:Context.java 566 2006-11-03 17:32:25Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class Context {

    private static final Pattern actionPattern = Pattern.compile("([a-zA-Z]+)\\.([a-zA-Z]+)");

    private HttpServletRequest servletRequest;

    private HttpServletResponse servletResponse;

    private String requestedResource;

    private String fullRequestedResource;

    private String requestedPath;

    private String extention;

    private String actionObjectName;

    private String actionMethodName;

    private boolean isExportView = false;

    private static IMaintenanceDAO dao = (IMaintenanceDAO) InitializationManager.getBean("mainDAO");

    protected Context(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
        this.fullRequestedResource = this.servletRequest.getPathInfo();
        if (this.fullRequestedResource.endsWith("/")) {
            this.requestedPath = this.fullRequestedResource;
        } else {
            this.requestedResource = this.fullRequestedResource.substring(this.fullRequestedResource.lastIndexOf("/") + 1);
            this.requestedPath = this.fullRequestedResource.substring(0, this.fullRequestedResource.lastIndexOf("/") + 1);
            this.extention = this.requestedResource.substring(this.requestedResource.lastIndexOf(".") + 1);
        }
        if (StringUtils.isNotBlank(this.servletRequest.getParameter(Constants.LINK_ACTION_DESCRIPTOR))) {
            Matcher matcher = Context.actionPattern.matcher(this.servletRequest.getParameter(Constants.LINK_ACTION_DESCRIPTOR));
            if (matcher.matches()) {
                this.actionObjectName = matcher.group(1);
                this.actionMethodName = matcher.group(2);
            }
        }
    }

    /**
	 * @return The servletRequest.
	 */
    public HttpServletRequest getServletRequest() {
        return this.servletRequest;
    }

    /**
	 * @return The servletResponse.
	 */
    public HttpServletResponse getServletResponse() {
        return this.servletResponse;
    }

    /**
	 * @return Requested requestedResource with full relative requestedPath.
	 */
    public String getFullRequestedResource() {
        return this.fullRequestedResource;
    }

    /**
	 * @return The map of request parameters.
	 */
    @SuppressWarnings("unchecked")
    public Map<String, String> getParameters() {
        return this.servletRequest.getParameterMap();
    }

    /**
	 * @param key
	 * @return Parameter value to the given key, if key doesn't exist, <code>null</code> will be returned.
	 */
    public String getParameter(String key) {
        if (StringUtils.isBlank(this.servletRequest.getParameter(key))) return null;
        return this.servletRequest.getParameter(key);
    }

    /**
	 * @return The name of the action method.
	 */
    public String getActionMethodName() {
        return this.actionMethodName;
    }

    /**
	 * @return The name of the action object (class name).
	 */
    public String getActionObjectName() {
        return this.actionObjectName;
    }

    /**
	 * @return True, if requested requestedResource is from a sites area.
	 */
    public boolean isSitesRequest() {
        return this.servletRequest.getPathInfo().startsWith(PathTool.getURLFromFile(InitializationManager.getSitesPath()));
    }

    /**
	 * @return RequestedResource, without requestedPath.
	 */
    public String getRequestedResource() {
        return this.requestedResource;
    }

    /**
	 * @return RequestedPath of the requestedResource.
	 */
    public String getRequestedPath() {
        return this.requestedPath;
    }

    /**
	 * @return The extention of the requestedResource.
	 */
    public String getRequestedResourceExtention() {
        return this.extention;
    }

    /**
	 * @return The referer.
	 */
    public String getReferer() {
        String referer = this.servletRequest.getHeader("referer");
        if (referer == null) referer = ""; else {
            try {
                referer = new URL(referer).getFile();
                if (referer.indexOf("?") > 0) referer = referer.substring(0, referer.indexOf("?"));
            } catch (MalformedURLException e) {
            }
        }
        return referer;
    }

    /**
	 * @return True, if the requested object is to render in the edit mode.
	 */
    public boolean isEditView() {
        return this.fullRequestedResource.endsWith(Constants.LINK_IDENTICATOR_EDIT);
    }

    /**
	 * @return True, if the requested object is to export.
	 */
    public boolean isExportView() {
        return this.isExportView;
    }

    public void setExportView(boolean isExport) {
        this.isExportView = isExport;
    }

    /**
	 * @return True, if request is for previewing a user page.
	 */
    public boolean isPreview() {
        return this.fullRequestedResource.substring(1).startsWith(Constants.LINK_IDENTICATOR_PREVIEW) || this.fullRequestedResource.substring(1).startsWith(Constants.LINK_IDENTICATOR_PREVIEW_AFTERSAVE);
    }

    /**
	 * @return True, if request is for previewing a user page after saving an editor.
	 */
    public boolean isPreviewAfterEdit() {
        return this.fullRequestedResource.substring(1).startsWith(Constants.LINK_IDENTICATOR_PREVIEW_AFTERSAVE);
    }

    public ViewMode getViewMode() {
        if (isExportView()) return ViewMode.EXPORT;
        if (isEditView()) return ViewMode.EDIT;
        if (isPreview() || isPreviewAfterEdit()) return ViewMode.PREVIEW;
        return null;
    }

    /**
	 * @return A list of editable field names of a form, or null.
	 */
    public List<String> getEditableFields() {
        String[] paramFields = StringUtils.split(getParameter(Constants.LINK_EDITFIELDS_DESCRIPTOR), ',');
        if (paramFields == null || paramFields.length < 1) return null;
        return Arrays.asList(paramFields);
    }

    /**
	 * @return the renderable
	 */
    public IRenderable getRenderable() {
        IPersistentPojo persistentPojo = getPersistentPojo();
        return (!(persistentPojo instanceof SiteResource)) ? (IRenderable) persistentPojo : null;
    }

    public IPersistentPojo getPersistentPojo() {
        String pojoType = this.servletRequest.getParameter(Constants.LINK_TYPE_DESCRIPTOR);
        String idString = this.servletRequest.getParameter("id");
        if (StringUtils.isNotBlank(pojoType) && StringUtils.isNotBlank(idString) && StringUtils.isNumeric(idString)) {
            Long id = Long.valueOf(idString);
            Class<?> cls = null;
            if (pojoType.equals(Constants.LINK_TYPE_PAGE)) cls = Page.class; else if (pojoType.equals(Constants.LINK_TYPE_GALLERY)) cls = Gallery.class; else if (pojoType.equals(Constants.LINK_TYPE_IMAGE)) cls = Image.class; else if (pojoType.equals(Constants.LINK_TYPE_MACRO)) cls = Macro.class; else if (pojoType.equals(Constants.LINK_TYPE_TEMPLATE)) cls = Template.class; else throw new IllegalArgumentException("Unknown pojo TYPE in link: " + pojoType);
            return dao.getById(cls, id);
        }
        return null;
    }

    public String getTypeDescriptor() {
        return getParameter(Constants.LINK_TYPE_DESCRIPTOR);
    }
}
