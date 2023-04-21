package com.hk.web.user.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.IpCityRange;
import com.hk.bean.IpCityUser;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.IpCityService;
import com.hk.svr.UserService;
import com.hk.web.pub.action.BaseAction;
import com.hk.web.user.UserVo;
import com.hk.web.user.UserVoBuilder;

@Component("/user/hkuser")
public class HkUserAction extends BaseAction {

    @Autowired
    private UserService userService;

    @Autowired
    private IpCityService ipCityService;

    private int size = 20;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        String ip = req.getRemoteAddr();
        String uip = req.getString("uip");
        if (uip != null) {
            ip = uip;
        }
        IpCityRange range = this.ipCityService.getIpCityRange(ip);
        SimplePage page = req.getSimplePage(size);
        if (range != null) {
            List<IpCityUser> list = this.userService.getIpCityUserListSortUserRecentUpdate(range.getCityId(), page.getBegin(), size);
            page.setListSize(list.size());
            List<UserVo> uservolist = new ArrayList<UserVo>();
            for (IpCityUser ipu : list) {
                UserVo o = new UserVo();
                o.setUser(ipu.getUser());
                uservolist.add(o);
            }
            UserVoBuilder builder = new UserVoBuilder();
            builder.setVolist(uservolist);
            builder.setLabaParserCfg(this.getLabaParserCfg(req));
            builder.setForceFriend(false);
            builder.setNeedFriend(true);
            builder.setLoginUser(this.getLoginUser(req));
            builder.setNeedLaba(true);
            UserVo.buildUserVoInfo(builder);
            req.setAttribute("uservobuilder", builder);
            req.setAttribute("uservolist", uservolist);
        }
        return "/WEB-INF/page/user/hkuser.jsp";
    }
}
