package com.coltrane.web.customerorder;

import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.coltrane.domain.Permission;
import com.coltrane.domain.UserSession;
import com.coltrane.service.CustomerOrderService;
import com.coltrane.service.LoginService;
import com.coltrane.utils.Pagination;
import com.coltrane.utils.Sidebar;

@Controller
@RequestMapping("customerOrderStatusList.htm")
public class CustomerOrderStatusListController {

    private LoginService loginService;

    private CustomerOrderService customerOrderService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setCustomerOrderService(CustomerOrderService customerOrderService) {
        this.customerOrderService = customerOrderService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showCustomerOrderStatusPage(ModelMap model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "sort", required = false, defaultValue = "") String sort, @RequestParam(value = "header", required = false, defaultValue = "") String isHeader) {
        if (!loginService.checkSignedIn()) {
            return "redirect:login.htm";
        }
        Permission permission = loginService.getAccountPermission();
        if (!permission.isCustomerOrderManage()) {
            return "redirect:permissionError.htm";
        }
        UserSession session = loginService.getUserSession();
        if (page != null) {
            session.setCurrentCustomerOrderStatusPage(Integer.valueOf(page));
        }
        Pagination pagination = new Pagination("customerOrderStatusList.htm", "", false);
        pagination.setResultsPerPage(session.getRecordsPerPage());
        pagination.setPages(customerOrderService.countSearch(session.getCustomerOrderStatusSearch()));
        pagination.setCurrentPage(session.getCurrentCustomerOrderStatusPage());
        model.put("pagination", pagination);
        if (sort.equals("")) {
            sort = session.getCustomerOrderStatusSort();
        }
        if (sort == null || sort.equals("")) {
            sort = "Code";
            session.setCustomerOrderStatusSort(sort);
        }
        if (session.getCustomerOrderStatusSort() != null && session.getCustomerOrderStatusSort().equals(sort) && isHeader.equals("1")) {
            session.setCustomerOrderStatusDesc(!session.isCustomerOrderStatusDesc());
        }
        session.setCustomerOrderStatusSort(sort);
        model.put("headerLinks", prepareHeaderLinks("customerOrderStatusList.htm", session.getCustomerOrderStatusSort(), session.isCustomerOrderStatusDesc()));
        model.put("customerOrderStatus", customerOrderService.searchCustomerOrderStatus(session.getCustomerOrderStatusSearch()));
        model.put("permission", permission);
        model.put("sidebar", new Sidebar(permission).toString());
        return "customerOrderStatusList";
    }

    private HashMap<String, String> prepareHeaderLinks(String linkPage, String sort, boolean desc) {
        String[] headers = "Code,Name,Description".split(",");
        HashMap<String, String> headerLinks = new HashMap<String, String>();
        for (String header : headers) {
            String link = "";
            if (header.equals(sort)) {
                if (desc) {
                    link += "<img src=\"images/sort-desc.png\" />";
                } else {
                    link += "<img src=\"images/sort-asc.png\" />";
                }
            }
            link += "<a href=\"" + linkPage + "?sort=" + header + "&header=1\" class=\"header\">" + header + "</a>";
            headerLinks.put(header, link);
        }
        return headerLinks;
    }
}
