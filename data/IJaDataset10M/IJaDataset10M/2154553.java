package org.kablink.teaming.portlet.administration;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.HomePageConfig;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.portlet.SAbstractController;
import org.kablink.teaming.web.util.PortletRequestUtils;
import org.kablink.teaming.web.util.WebHelper;
import org.springframework.web.portlet.ModelAndView;

public class ConfigureHomePageController extends SAbstractController {

    public void handleActionRequestAfterValidation(ActionRequest request, ActionResponse response) throws Exception {
        Map formData = request.getParameterMap();
        if (formData.containsKey("okBtn") && WebHelper.isMethodPost(request)) {
            Long defaultHomePageId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_HOME_PAGE_ID);
            Long defaultGuestHomePageId = PortletRequestUtils.getLongParameter(request, WebKeys.URL_GUEST_HOME_PAGE_ID);
            Boolean deleteHomePageId = PortletRequestUtils.getBooleanParameter(request, "deleteHomePage", false);
            Boolean deleteGuestHomePageId = PortletRequestUtils.getBooleanParameter(request, "deleteGuestHomePage", false);
            if (deleteHomePageId) defaultHomePageId = null;
            if (deleteGuestHomePageId) defaultGuestHomePageId = null;
            HomePageConfig homePageConfig = new HomePageConfig();
            homePageConfig.setDefaultHomePageId(defaultHomePageId);
            homePageConfig.setDefaultGuestHomePageId(defaultGuestHomePageId);
            getAdminModule().setHomePageConfig(homePageConfig);
        } else response.setRenderParameters(formData);
    }

    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        Map model = new HashMap();
        HomePageConfig homePageConfig = getAdminModule().getHomePageConfig();
        model.put(WebKeys.HOMEPAGE_CONFIG, homePageConfig);
        Long defaultHomePageId = homePageConfig.getDefaultHomePageId();
        Long defaultGuestHomePageId = homePageConfig.getDefaultGuestHomePageId();
        if (defaultHomePageId != null) {
            try {
                Binder binder = getBinderModule().getBinder(defaultHomePageId);
                model.put(WebKeys.DEFAULT_HOMEPAGE_BINDER, binder);
            } catch (Exception e) {
            }
        }
        if (defaultGuestHomePageId != null) {
            try {
                Binder binder = getBinderModule().getBinder(defaultGuestHomePageId);
                model.put(WebKeys.DEFAULT_GUEST_HOMEPAGE_BINDER, binder);
            } catch (Exception e) {
            }
        }
        return new ModelAndView(WebKeys.VIEW_ADMIN_CONFIGURE_HOME_PAGE, model);
    }
}
