package com.hk.web.user.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.HkbLog;
import com.hk.bean.ScoreLog;
import com.hk.bean.User;
import com.hk.bean.UserOtherInfo;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.UserService;
import com.hk.web.pub.action.BaseAction;

@Component("/user/log/log")
public class LogAction extends BaseAction {

    @Autowired
    private UserService userService;

    private int size = 20;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        return null;
    }

    /**
	 * 积分日志
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String scorelog(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        UserOtherInfo info = this.userService.getUserOtherInfo(loginUser.getUserId());
        SimplePage page = req.getSimplePage(size);
        List<ScoreLog> list = this.userService.getScoreLogList(loginUser.getUserId(), page.getBegin(), size);
        page.setListSize(list.size());
        List<ScoreLogVo> scorelogvolist = ScoreLogVo.createVoList(list);
        req.setAttribute("scorelogvolist", scorelogvolist);
        req.setAttribute("info", info);
        return "/WEB-INF/page/user/scorelog.jsp";
    }

    /**
	 * 酷币日志
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String hkblog(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        UserOtherInfo info = this.userService.getUserOtherInfo(loginUser.getUserId());
        SimplePage page = req.getSimplePage(size);
        List<HkbLog> list = this.userService.getHkbLogList(loginUser.getUserId(), page.getBegin(), size);
        page.setListSize(list.size());
        List<HkbLogVo> hkblogvolist = HkbLogVo.createVoList(list);
        req.setAttribute("hkblogvolist", hkblogvolist);
        req.setAttribute("info", info);
        return "/WEB-INF/page/user/hkblog.jsp";
    }
}
