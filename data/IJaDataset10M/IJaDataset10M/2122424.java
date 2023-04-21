package com.jd.mysql.mgr.controller.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.jd.mysql.mgr.dto.MgrAccountDTO;
import com.jd.mysql.mgr.pojo.MgrAccount;
import com.jd.mysql.mgr.pojo.MgrAccountGroup;
import com.jd.mysql.mgr.service.IMgrAccountService;
import com.jd.mysql.mgr.util.GlobalStaticVariables;
import com.jd.mysql.mgr.util.MD5Util;
import com.jd.mysql.mgr.util.PageUtil;
import com.jd.mysql.mgr.util.StringUtil;

public class AddMgrAccountController extends SimpleFormController {

    private static Integer start = 0;

    private static Integer limit = GlobalStaticVariables.limit;

    public AddMgrAccountController() {
        this.setCommandClass(MgrAccount.class);
    }

    public IMgrAccountService mgrAccountService;

    public void setMgrAccountService(IMgrAccountService mgrAccountService) {
        this.mgrAccountService = mgrAccountService;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        MgrAccount mgraccount = (MgrAccount) command;
        String username = mgraccount.getUsername();
        String password = mgraccount.getPassword();
        String passwordAgain = request.getParameter("passwordAgain");
        String email = mgraccount.getEmail();
        if (!password.equals(passwordAgain)) {
            request.setAttribute("msg", "Different passwords!");
            return new ModelAndView("error");
        }
        if (StringUtil.isNull(username)) {
            request.setAttribute("msg", "Username is null,please input legal username");
            return new ModelAndView("error");
        }
        if (StringUtil.isNull(password)) {
            request.setAttribute("msg", "Password is null,please input legal password");
            return new ModelAndView("error");
        }
        if (StringUtil.isNull(passwordAgain)) {
            request.setAttribute("msg", "The second password is null,please input legal password again");
            return new ModelAndView("error");
        }
        if (StringUtil.isNull(email)) {
            request.setAttribute("msg", "The Email is null,please input legal Email");
            return new ModelAndView("error");
        }
        MgrAccount mgraccountOld = mgrAccountService.findMgrAccountByUserName(username);
        if (mgraccountOld != null) {
            request.setAttribute("msg", "Replicate username,change a special one just for you!");
            return new ModelAndView("error");
        }
        Integer groupId = Integer.parseInt(request.getParameter("group"));
        MgrAccountGroup group = mgrAccountService.findMgrAccountGroupById(groupId);
        String passwordMd5 = MD5Util.getMD5(password);
        mgraccount.setPassword(passwordMd5);
        mgraccount.setUsername(username);
        mgraccount.setEmail(email);
        mgraccount.setLastLoginIp(request.getRemoteAddr());
        mgraccount.setCreateDate(new Date());
        mgraccount.setLastLoginDate(null);
        mgraccount.setMgrAccountGroup(group);
        mgraccount.setYn(true);
        mgrAccountService.addAccount(mgraccount);
        String index = request.getParameter("index");
        Map<String, Object> map = new HashMap<String, Object>();
        Integer start = PageUtil.setPageParam(map, index, limit);
        Long totalcount = mgrAccountService.getAccountTotalCount(groupId, true);
        List<MgrAccount> list = mgrAccountService.getAccountList(start, limit, groupId);
        List<MgrAccountDTO> dtoList = new ArrayList<MgrAccountDTO>();
        for (MgrAccount mgra : list) {
            dtoList.add(mgra.toDTO());
        }
        map.put("totalCount", totalcount);
        map.put("dtoList", dtoList);
        return new ModelAndView("account/accountList", "map", map);
    }
}
