package com.coltrane.web.factoryorder;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.coltrane.domain.FactoryOrderEntry;
import com.coltrane.domain.Permission;
import com.coltrane.service.FactoryOrderService;
import com.coltrane.service.LoginService;
import com.coltrane.service.MaterialService;
import com.coltrane.utils.AmountEditor;
import com.coltrane.utils.FormDateFormat;
import com.coltrane.utils.FormTimeFormat;
import com.coltrane.utils.Sidebar;

@Controller
@RequestMapping("orderAdd.htm")
public class FactoryOrderEntryAddController {

    private LoginService loginService;

    private MaterialService materialService;

    private FactoryOrderService orderService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setMaterialService(MaterialService materialService) {
        this.materialService = materialService;
    }

    @Autowired
    public void setFactoryOrderService(FactoryOrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("FactoryOrderEntry")
    public FactoryOrderEntry getFactoryOrderEntry() {
        return new FactoryOrderEntry();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new FormDateFormat(), true));
        binder.registerCustomEditor(Time.class, new CustomDateEditor(new FormTimeFormat(), true));
        binder.registerCustomEditor(BigDecimal.class, new AmountEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showAddOrderPage(ModelMap model, @RequestParam(value = "orderNumber", required = false, defaultValue = "") String orderNumber) {
        if (!loginService.checkSignedIn()) {
            return "redirect:login.htm";
        }
        Permission permission = loginService.getAccountPermission();
        if (!permission.isFactoryOrderView() || !permission.isFactoryOrderAdd()) {
            return "redirect:permissionError.htm";
        }
        loginService.getUserSession().setGoBackToOrder(orderNumber);
        model.addAttribute("goBackToOrder", orderNumber);
        model.addAttribute("enteredBy", loginService.getUserSession().getCode());
        model.addAttribute("material", materialService.getMaterialOptions());
        model.put("sidebar", new Sidebar(permission).toString());
        return "orderAdd";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute("FactoryOrderEntry") FactoryOrderEntry entry) {
        orderService.addFactoryOrderEntry(entry);
        String goBackToOrder = loginService.getUserSession().getGoBackToOrder();
        if (!goBackToOrder.equals("")) {
            loginService.getUserSession().setGoBackToOrder("");
            return "redirect:orderView.htm?orderNumber=" + goBackToOrder;
        }
        return "redirect:ordersList.htm";
    }
}
