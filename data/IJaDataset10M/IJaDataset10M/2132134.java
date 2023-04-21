package com.oasystem.web.controller.fund;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.oasystem.domain.PagingBean;
import com.oasystem.domain.fund.FundApplication;
import com.oasystem.domain.user.User;
import com.oasystem.service.fund.FundApplicationService;
import com.oasystem.service.system.SystemService;
import com.oasystem.utils.SystemConstants;
import com.oasystem.web.webbean.fund.FundApplicationWebBean;

@Controller
@RequestMapping(value = "/MainMenu/fund")
public class FundController {

    @Resource(name = "systemService")
    private SystemService systemService;

    @Resource(name = "fundApplicationService")
    private FundApplicationService fundApplicationService;

    @RequestMapping(value = "/fundApplicationPage")
    public String goodsPage(Model model) {
        model.addAttribute("mainMenu", "fund");
        model.addAttribute("subMenu", "application");
        model.addAttribute("fundApplicationWebBean", new FundApplicationWebBean());
        model.addAttribute("classificationList", systemService.fundClassifyList());
        model.addAttribute("approverList", systemService.leaderList());
        return "fund.application";
    }

    @RequestMapping(value = "/saveApplication")
    @ResponseBody
    public ModelMap saveAppication(Model model, FundApplicationWebBean fundApplicationWebBean) {
        ModelMap map = new ModelMap();
        try {
            fundApplicationService.insert(fundApplicationWebBean.buildFundApplication());
        } catch (Exception e) {
            map.addAttribute("error", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/fundApplicationHistory")
    public String fundApplicationHistory(Model model, String pageIndex, HttpSession session) {
        User user = (User) session.getAttribute(SystemConstants.USER);
        PagingBean<FundApplication> pagingBean = new PagingBean<FundApplication>();
        pagingBean.setCurrentPage(Integer.valueOf(pageIndex));
        fundApplicationService.fundApplicationHistory("1", pagingBean);
        model.addAttribute("pagingBean", pagingBean);
        return "fund.application.history";
    }

    @RequestMapping(value = "/fundApprovement")
    public String fundApprovement(Model model, String pageIndex, HttpSession session) {
        model.addAttribute("mainMenu", "fund");
        model.addAttribute("subMenu", "fundApprovement");
        return "fund.approvement";
    }

    @RequestMapping(value = "/fundApprovementList")
    public String fundApprovementList(Model model, String pageIndex, HttpSession session) {
        User user = (User) session.getAttribute(SystemConstants.USER);
        PagingBean<FundApplication> pagingBean = new PagingBean<FundApplication>();
        if (pageIndex == null || "".equals(pageIndex)) {
            pagingBean.setCurrentPage(1);
        } else {
            pagingBean.setCurrentPage(Integer.valueOf(pageIndex));
        }
        fundApplicationService.fundApplicationListForCheck("1", pagingBean);
        model.addAttribute("pagingBean", pagingBean);
        return "fund.approvement.list";
    }

    @RequestMapping(value = "/approveFundApplication")
    @ResponseBody
    public ModelMap approveFundApplication(Model model, String fundApplicationId, HttpSession session) {
        ModelMap map = new ModelMap();
        try {
            fundApplicationService.approveFundApplication(fundApplicationId);
        } catch (Exception e) {
            map.addAttribute("error", e.getMessage());
        }
        return map;
    }
}
