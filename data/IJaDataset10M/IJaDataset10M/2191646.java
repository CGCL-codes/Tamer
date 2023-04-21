package com.coltrane.web.customerorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.coltrane.domain.CustomerOrderStatus;
import com.coltrane.domain.Permission;
import com.coltrane.service.CustomerOrderService;
import com.coltrane.service.LoginService;
import com.coltrane.utils.Sidebar;

@Controller
@RequestMapping("customerOrderStatusEdit.htm")
public class CustomerOrderStatusUpdateController {

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

    @ModelAttribute("CustomerOrderStatus")
    public CustomerOrderStatus getCustomerOrderStatus() {
        return new CustomerOrderStatus();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showCustomerOrderStatusEditPage(ModelMap model, @ModelAttribute("CustomerOrderStatus") CustomerOrderStatus customerOrderStatus, @RequestParam(value = "customerOrderStatusId", required = true) long customerOrderStatusId) {
        if (!loginService.checkSignedIn()) {
            return "redirect:login.htm";
        }
        Permission permission = loginService.getAccountPermission();
        if (!permission.isCustomerOrderManage()) {
            return "redirect:permissionError.htm";
        }
        model.addAttribute("customerOrderStatus", customerOrderService.getCustomerOrderStatusById(customerOrderStatusId));
        model.put("sidebar", new Sidebar(permission).toString());
        return "customerOrderStatusEdit";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute("CustomerOrderStatus") CustomerOrderStatus customerOrderStatus) {
        customerOrderService.updateCustomerOrderStatus(customerOrderStatus);
        return "redirect:customerOrderStatusEdit.htm?customerOrderStatusId=" + customerOrderStatus.getId();
    }
}
