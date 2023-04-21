package com.m4f.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.m4f.cityclient.api.exception.M4FCityClientException;
import com.m4f.cityclient.api.models.ApplicationCity;
import com.m4f.cityclient.api.models.OrganizationCity;
import com.m4f.cityclient.api.service.ifc.M4FApplicationApiService;
import com.m4f.cityclient.api.service.ifc.M4FOrganizationApiService;
import com.m4f.cityclient.api.service.ifc.M4FUserApiService;
import com.m4f.cityclient.common.model.M4FOAuthUser;
import com.m4f.gaeweb.web.utils.StackTraceUtil;

/**
 * @author David Basoko
 */
@Controller
@RequestMapping("/m4f")
public class M4fController {

    private static final Logger LOGGER = Logger.getLogger(M4fController.class.getName());

    @Autowired
    private M4FUserApiService m4fUserService;

    @Autowired
    private M4FOrganizationApiService m4fOrganizationService;

    @Autowired
    private M4FApplicationApiService m4fApplicationService;

    @RequestMapping("/info")
    public String getUserInfoApi(Principal princpal, Model model) throws M4FCityClientException {
        try {
            M4FOAuthUser user = getM4fUserService().getUserInfo();
            OrganizationCity organization = getM4fOrganizationService().getOrganizationInfo();
            List<ApplicationCity> applications = getM4fApplicationService().getApplicationsOfUser();
            ApplicationCity application = getM4fApplicationService().getApplicationInfo(applications.get(0).getId());
            model.addAttribute("user", user);
            model.addAttribute("organization", organization);
            model.addAttribute("applications", applications);
            model.addAttribute("application", application);
        } catch (M4FCityClientException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            model.addAttribute("error", StackTraceUtil.getStackTrace(e));
            return "common.error";
        }
        return "user.info";
    }

    public M4FUserApiService getM4fUserService() {
        return m4fUserService;
    }

    public void setM4fUserService(M4FUserApiService m4fUserService) {
        this.m4fUserService = m4fUserService;
    }

    public M4FOrganizationApiService getM4fOrganizationService() {
        return m4fOrganizationService;
    }

    public void setM4fOrganizationService(M4FOrganizationApiService m4fOrganizationService) {
        this.m4fOrganizationService = m4fOrganizationService;
    }

    public M4FApplicationApiService getM4fApplicationService() {
        return m4fApplicationService;
    }

    public void setM4fApplicationService(M4FApplicationApiService m4fApplicationService) {
        this.m4fApplicationService = m4fApplicationService;
    }
}
