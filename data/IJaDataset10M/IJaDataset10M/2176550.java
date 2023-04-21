package com.m4f.web.controller.users;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.logging.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.m4f.business.domain.Course;
import com.m4f.business.domain.MediationService;
import com.m4f.business.domain.Provider;
import com.m4f.business.domain.School;
import com.m4f.business.domain.extended.ExtendedCourse;
import com.m4f.business.domain.extended.ExtendedSchool;
import com.m4f.utils.PageManager;
import com.m4f.utils.StackTraceUtil;
import com.m4f.web.bind.form.FilterForm;
import com.m4f.web.controller.BaseController;

@Controller
@RequestMapping("/dashboard/mediator")
public class MediatorController extends BaseController {

    private static final Logger LOGGER = Logger.getLogger(MediatorController.class.getName());

    @Secured({ "ROLE_MANUAL_MEDIATOR", "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(method = RequestMethod.GET)
    public String getView(Principal currentUser, Model model, @RequestParam(required = false, defaultValue = "en") String lang) throws IOException, ClassNotFoundException {
        return "mediator.home";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR", "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String changeMediatorInfo(Model model, Principal principal, Locale locale) {
        return "mediator.profile";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR", "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(value = "/catalog", method = RequestMethod.GET)
    public String getIndex(Principal currentUser) {
        return "mediator.catalog.index";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR", "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(value = "/tutorials", method = RequestMethod.GET)
    public String getTutorials(Principal currentUser, Model model, Locale locale) {
        return "tutorials";
    }

    @Secured({ "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(value = "/catalog/schools", method = RequestMethod.GET)
    public String getSchools(Principal currentUser, Model model, Locale locale, @RequestParam(defaultValue = "1", required = false) Integer page, @RequestParam(defaultValue = "", required = false) String order) {
        try {
            String ordering = order != null && !("").equals(order) ? order : "name";
            Provider provider = this.serviceLocator.getTransversalService().getProviderByUserName(currentUser.getName(), locale);
            model.addAttribute("provider", provider);
            PageManager<School> paginator = new PageManager<School>();
            paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/mediator/catalog/schools");
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setSize(this.serviceLocator.getSchoolService().countSchoolsByProvider(provider.getId()));
            paginator.setCollection(this.serviceLocator.getSchoolService().getSchoolsByProvider(provider.getId(), ordering, locale, paginator.getStart(), paginator.getEnd()));
            model.addAttribute("paginator", paginator);
            model.addAttribute("order", ordering);
        } catch (Exception e) {
            LOGGER.severe(StackTraceUtil.getStackTrace(e));
            model.addAttribute("paginator", new PageManager<School>());
        }
        return "mediator.catalog.schools";
    }

    @Secured({ "ROLE_AUTOMATIC_MEDIATOR" })
    @RequestMapping(value = "/catalog/courses", method = RequestMethod.GET)
    public String getCourses(Principal currentUser, Model model, Locale locale, @RequestParam(defaultValue = "1", required = false) Integer page, @RequestParam(defaultValue = "", required = false) String order) {
        try {
            String ordering = order != null && !("").equals(order) ? order : "title";
            Provider provider = this.serviceLocator.getTransversalService().getProviderByUserName(currentUser.getName(), locale);
            model.addAttribute("provider", provider);
            PageManager<Course> paginator = new PageManager<Course>();
            paginator.setOffset(this.getPageSize());
            paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/mediator/catalog/courses");
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setSize(this.serviceLocator.getCourseService().countCoursesByProvider(provider.getId()));
            paginator.setCollection(this.serviceLocator.getCourseService().getCoursesByProvider(provider.getId(), ordering, locale, paginator.getStart(), paginator.getEnd()));
            model.addAttribute("paginator", paginator);
            model.addAttribute("order", ordering);
        } catch (Exception e) {
            LOGGER.severe(StackTraceUtil.getStackTrace(e));
            model.addAttribute("paginator", new PageManager<Course>());
        }
        return "mediator.catalog.courses";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR" })
    @RequestMapping(value = "/catalog/extended/schools", method = RequestMethod.GET)
    public String getExtendedSchools(Principal currentUser, Model model, Locale locale, @RequestParam(defaultValue = "1", required = false) Integer page, @RequestParam(defaultValue = "", required = false) String order, @RequestParam(required = false) Long provinceId, @RequestParam(required = false) Long regionId, @RequestParam(required = false) Long townId) {
        if (provinceId != null || regionId != null || townId != null) {
            FilterForm filterForm = new FilterForm();
            filterForm.setProvinceId(provinceId);
            filterForm.setRegionId(regionId);
            filterForm.setTownId(townId);
            return this.listFilter(filterForm, model, page, order, locale);
        }
        try {
            model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
            String ordering = order != null && !("").equals(order) ? order : "name";
            PageManager<ExtendedSchool> paginator = new PageManager<ExtendedSchool>();
            paginator.setOffset(this.getPageSize());
            paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/mediator/catalog/extended/schools");
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setSize(this.serviceLocator.getExtendedSchoolService().countSchools());
            paginator.setCollection(this.serviceLocator.getExtendedSchoolService().getSchools(ordering, paginator.getStart(), paginator.getEnd(), locale));
            model.addAttribute("paginator", paginator);
            model.addAttribute("order", ordering);
        } catch (Exception e) {
            LOGGER.severe(StackTraceUtil.getStackTrace(e));
            model.addAttribute("paginator", new PageManager<ExtendedSchool>());
        }
        return "admin.catalog.extended.schools";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR" })
    @RequestMapping(value = "/catalog/extended/schools", method = RequestMethod.POST)
    public String listFilter(@ModelAttribute("filterForm") FilterForm filterForm, Model model, @RequestParam(defaultValue = "1", required = false) Integer page, @RequestParam(defaultValue = "", required = false) String order, Locale locale) {
        try {
            String ordering = order != null && !("").equals(order) ? order : "name";
            model.addAttribute("filterForm", filterForm);
            model.addAttribute("provinces", this.serviceLocator.getTerritorialService().getAllProvinces(locale));
            PageManager<ExtendedSchool> paginator = new PageManager<ExtendedSchool>();
            paginator.setOffset(this.getPageSize());
            paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/mediator/catalog/extended/schools");
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setSize(this.serviceLocator.getExtendedSchoolService().countSchoolsByTerritorial(filterForm.getProvinceId(), filterForm.getRegionId(), filterForm.getTownId()));
            paginator.setCollection(this.serviceLocator.getExtendedSchoolService().getSchoolsByTerritorial(filterForm.getProvinceId(), filterForm.getRegionId(), filterForm.getTownId(), ordering, paginator.getStart(), paginator.getEnd(), locale));
            model.addAttribute("paginator", paginator);
            model.addAttribute("order", ordering);
        } catch (Exception e) {
            LOGGER.severe(StackTraceUtil.getStackTrace(e));
            model.addAttribute("paginator", new PageManager<ExtendedSchool>());
            return "extended.school.list";
        }
        return "extended.school.list";
    }

    @Secured({ "ROLE_MANUAL_MEDIATOR" })
    @RequestMapping(value = "/catalog/extended/courses", method = RequestMethod.GET)
    public String list(Principal currentUser, Model model, Locale locale, @RequestParam(defaultValue = "1", required = false) Integer page, @RequestParam(defaultValue = "", required = false) String order) {
        try {
            String ordering = order != null && !("").equals(order) ? order : "title";
            MediationService mediationService = this.serviceLocator.getTransversalService().getMediationServiceByUser(currentUser.getName());
            PageManager<ExtendedCourse> paginator = new PageManager<ExtendedCourse>();
            paginator.setOffset(this.getPageSize());
            paginator.setUrlBase("/" + locale.getLanguage() + "/dashboard/mediator/catalog/extended/courses");
            paginator.setStart((page - 1) * paginator.getOffset());
            paginator.setSize(this.serviceLocator.getExtendedCourseService().countCoursesByOwner(mediationService.getId()));
            paginator.setCollection(this.serviceLocator.getExtendedCourseService().getCoursesByOwner(mediationService.getId(), ordering, locale, paginator.getStart(), paginator.getEnd()));
            model.addAttribute("paginator", paginator);
            model.addAttribute("order", ordering);
        } catch (Exception e) {
            LOGGER.severe(StackTraceUtil.getStackTrace(e));
            model.addAttribute("paginator", new PageManager<ExtendedCourse>());
        }
        return "mediator.catalog.extended.courses";
    }

    @ModelAttribute("filterForm")
    public FilterForm getFilterForm() {
        return new FilterForm();
    }
}
