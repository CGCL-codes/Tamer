package com.jandan.web.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;
import com.jandan.logic.JWordzFacade;
import com.jandan.ui.client.util.ClientUtil;
import com.jandan.ui.model.Account;
import com.jandan.ui.model.Lesson;
import com.jandan.ui.model.Word;
import com.jandan.util.EncryptUtil;
import edu.emory.mathcs.backport.java.util.Arrays;

public class AccountManageController extends MultiActionController {

    private JWordzFacade jwordz;

    public void setJwordz(JWordzFacade jwordz) {
        this.jwordz = jwordz;
    }

    public ModelAndView displayAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageIndex = request.getParameter("pageIndex");
        int pi = ClientUtil.FIRST_PAGE_INDEX;
        int limit = ClientUtil.ACCOUNT_PAGE_LIMIT;
        if (pageIndex != null) {
            pi = Integer.parseInt(pageIndex);
        }
        int totalAccountCount = jwordz.getTotalAccountCount();
        List<Account> accountList = jwordz.getAllAccountList(pi * limit, limit);
        if (accountList == null) {
            accountList = new ArrayList<Account>();
        }
        Map map = new HashMap();
        map.put("totalAccountCount", totalAccountCount);
        map.put("accountList", accountList);
        map.put("authories", ClientUtil.autorities);
        return new ModelAndView("admin_account", map);
    }

    public ModelAndView displayOne(HttpServletRequest request, HttpServletResponse reponse) {
        String userID = request.getParameter("userID");
        ModelAndView model = this.getModelWithAccount("admin_accountinfo", userID);
        long id = Long.parseLong(userID);
        List<Lesson> lessonList = jwordz.getLessonListByUserID(id);
        List<Word> wordList = jwordz.getStrangeWordListByUserID(id);
        model.addObject("lessonList", lessonList);
        model.addObject("wordList", wordList);
        model.addObject("authories", ClientUtil.autorities);
        return model;
    }

    public ModelAndView displayForm(HttpServletRequest request, HttpServletResponse reponse) {
        String userID = request.getParameter("userID");
        ModelAndView model = this.getModelWithAccount("admin_editAccountForm", userID);
        List<String> authoriesList = Arrays.asList(ClientUtil.autorities);
        model.addObject("authories", authoriesList);
        return model;
    }

    public ModelAndView add(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        Account account = generateAccount(request);
        jwordz.insertAccount(account);
        return new ModelAndView(new RedirectView("account.htm"), "message", "新用户添加成功");
    }

    public ModelAndView edit(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
        Account account = generateAccount(request);
        jwordz.updateAccount(account);
        return new ModelAndView(new RedirectView("account.htm"), "message", "用户信息户修改成功");
    }

    public ModelAndView delete(HttpServletRequest request, HttpServletResponse reponse) {
        long userID = Long.parseLong(request.getParameter("userID"));
        jwordz.deleteAccount(userID);
        return new ModelAndView(new RedirectView("account.htm"), "message", "用户删除成功");
    }

    private ModelAndView getModelWithAccount(String viewName, String userID) {
        ModelAndView model = new ModelAndView(viewName);
        if (userID != null) {
            long id = Long.parseLong(userID);
            Account account = jwordz.getAccountByUserID(id);
            model.addObject("account", account);
        }
        return model;
    }

    private Account generateAccount(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String province = request.getParameter("province");
        String address = request.getParameter("address");
        int type = Integer.parseInt(request.getParameter("type"));
        String p = EncryptUtil.encryptPassword(password);
        Account account = new Account();
        String s = request.getParameter("userID");
        if (s != null) {
            account.setUserID(Long.parseLong(s));
        }
        account.setUserName(userName);
        account.setPassword(p);
        account.setEmail(email);
        account.setName(name);
        account.setPhone(phone);
        account.setProvince(province);
        account.setAddress(address);
        account.setType(type);
        return account;
    }
}
