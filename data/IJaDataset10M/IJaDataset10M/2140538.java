package uturismu.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import uturismu.bean.AccountBean;
import uturismu.bean.BookerBean;
import uturismu.bean.BookerUpdate;
import uturismu.bean.CityBean;
import uturismu.bean.util.BeanMapping;
import uturismu.bean.util.DateGenerator;
import uturismu.dto.Account;
import uturismu.dto.Booker;
import uturismu.service.AdministratorService;
import uturismu.service.BookerService;
import uturismu.service.UserService;
import static uturismu.controller.util.SessionCheck.isActiveSession;
import static uturismu.controller.util.SessionCheck.isBooker;

@Controller
@SessionAttributes("account")
public class BookerController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdministratorService adminService;

    @Autowired
    private BookerService bookerService;

    @RequestMapping(value = "bo/home", method = RequestMethod.POST)
    public String showHomePage(HttpSession webSession) {
        AccountBean account = (AccountBean) webSession.getAttribute("account");
        if (account == null) {
            return "redirect:/";
        }
        return "home";
    }

    @RequestMapping(value = "/updateBo", method = RequestMethod.GET)
    public String prepareUpdate(HttpSession session, Model model) {
        if (!isBooker(session)) {
            return "redirect:/";
        }
        BookerBean beanAccount = (BookerBean) session.getAttribute("account");
        Account account = userService.getAccountByEmail(beanAccount.getEmail());
        Booker booker = account.getBooker();
        BookerUpdate bean = BeanMapping.encodeBookerUpdate(account, booker);
        model.addAttribute("updateData", bean);
        List<CityBean> cities = BeanMapping.encode(adminService.getCities());
        model.addAttribute("days", DateGenerator.getDays());
        model.addAttribute("months", DateGenerator.getMonths());
        model.addAttribute("years", DateGenerator.getYears());
        return "booker/update";
    }
}
