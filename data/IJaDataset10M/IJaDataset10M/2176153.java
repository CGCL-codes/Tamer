package nl.openu.tiles.suggest;

import java.util.Date;
import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import nl.openu.commons.portlet.PortletUtils;
import nl.openu.tiles.dashboard.TilesPortlet;
import nl.openu.tiles.service.util.ExpandoUtil;
import org.hsqldb.lib.StringUtil;
import org.portletfaces.liferay.faces.context.LiferayFacesContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

public abstract class Suggestion {

    private static final Log LOG = LogFactoryUtil.getLog(SuggestionController.class);

    private static final LiferayFacesContext LIFERAY_FACES_CTX = LiferayFacesContext.getInstance();

    private long classNameId;

    private long assetCategoryId;

    private Group group;

    private String iconURL;

    private Date startDate;

    private String title;

    public Suggestion(final long classNameId, final long classPk, final long assetCategoryId) throws SystemException, PortalException {
        this.classNameId = ClassNameLocalServiceUtil.getClassName(Group.class.getName()).getClassNameId();
        this.assetCategoryId = assetCategoryId;
        if (this.classNameId != classNameId) {
            throw new NoGroupException();
        }
        group = GroupServiceUtil.getGroup(classPk);
    }

    public long getCategoryId() {
        return assetCategoryId;
    }

    public Group getGroup() {
        return group;
    }

    public String getIconURL() {
        if (iconURL == null) {
            if (group != null) {
                try {
                    iconURL = ExpandoUtil.getString(group.getCompanyId(), group.getClassNameId(), group.getClassPK(), ExpandoUtil.FIELD_ICON_URL);
                } catch (SystemException e) {
                    LIFERAY_FACES_CTX.addGlobalUnexpectedErrorMessage();
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e);
                    }
                } catch (PortalException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(e);
                    }
                }
            }
        }
        return StringUtil.isEmpty(iconURL) ? "/images/noimageavailable.jpg" : iconURL;
    }

    public abstract String getInfo();

    public Date getStartDate() {
        if (startDate == null) {
            if (group != null) {
                try {
                    startDate = ExpandoUtil.getDate(group.getCompanyId(), group.getClassNameId(), group.getClassPK(), ExpandoUtil.FIELD_START_DATE);
                } catch (SystemException e) {
                    LIFERAY_FACES_CTX.addGlobalUnexpectedErrorMessage();
                    if (LOG.isErrorEnabled()) {
                        LOG.error(e);
                    }
                } catch (PortalException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(e);
                    }
                }
            }
        }
        return startDate;
    }

    /**
	 * Very ugly code that deals with translations of community titles. It is a
	 * custom solution that works by convention.
	 * 
	 * @param request
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
    public String getTitle() {
        if (title == null) {
            if (group != null) {
                title = group.getName();
                if (title.contains("/")) {
                    String[] titles = title.split("[^\\\\]/");
                    title = LIFERAY_FACES_CTX.getThemeDisplay().getLanguageId() == "NL/nl" ? titles[0] : titles[1];
                    title = title.replace("\\/", "/");
                }
            } else {
                title = "";
            }
        }
        return title;
    }

    /**
	 * Returns an addTileAction actionURL for the TilesPortlet to process. T
	 * 
	 * @return URL of addTileAction
	 */
    public String getUpdateTileLayoutActionURL() {
        if (group != null) {
            ThemeDisplay td = LIFERAY_FACES_CTX.getThemeDisplay();
            LiferayPortletResponse lpr = PortalUtil.getLiferayPortletResponse(LIFERAY_FACES_CTX.getPortletResponse());
            LiferayPortletURL url = lpr.createLiferayPortletURL(td.getPlid(), TilesPortlet.portletName, PortletRequest.ACTION_PHASE);
            url.setParameter(ActionRequest.ACTION_NAME, "addTileAction");
            url.setParameter("classNameId", String.valueOf(group.getClassNameId()));
            url.setParameter("classPk", String.valueOf(group.getClassPK()));
            url.setParameter("redirect", getRenderUrl(td.getPlid()));
            return url.toString();
        }
        return "#";
    }

    private String getRenderUrl(final long plid) {
        LiferayPortletResponse lpr = PortalUtil.getLiferayPortletResponse(LIFERAY_FACES_CTX.getPortletResponse());
        LiferayPortletURL url = lpr.createLiferayPortletURL(plid, PortletUtils.getThemeDisplay().getPortletDisplay().getPortletName(), PortletRequest.RENDER_PHASE);
        return url.toString();
    }

    public String doAdd() {
        System.out.println("Add was called");
        return null;
    }
}
