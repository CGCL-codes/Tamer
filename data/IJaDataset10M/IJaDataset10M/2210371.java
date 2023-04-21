package com.hk.web.laba.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.AdminUser;
import com.hk.bean.Bomber;
import com.hk.bean.User;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.BombService;
import com.hk.svr.UserService;
import com.hk.web.pub.action.BaseAction;

@Component("/laba/op/magic")
public class MagicAction extends BaseAction {

    @Autowired
    private BombService bombService;

    @Autowired
    private UserService userService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        User user = this.getLoginUser(req);
        Bomber bomber = this.bombService.getBomber(user.getUserId());
        boolean canbomb = false;
        if (bomber == null) {
            AdminUser adminUser = this.userService.getAdminUser(user.getUserId());
            if (adminUser != null && adminUser.getAdminLevel() == AdminUser.LEVEL_SUPER) {
                canbomb = true;
            }
        } else {
            canbomb = true;
        }
        String queryString = req.getQueryString();
        req.setAttribute("queryString", queryString);
        req.reSetAttribute("labaId");
        req.setAttribute("canbomb", canbomb);
        return "/WEB-INF/page/laba/magic.jsp";
    }
}
