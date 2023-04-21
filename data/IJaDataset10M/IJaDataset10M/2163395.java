package com.wangyu001.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.sysolar.sun.mvc.core.ActionForward;
import org.sysolar.sun.mvc.core.TransAction;
import org.sysolar.sun.mvc.support.Pager;
import org.sysolar.sun.mvc.support.RequestWrapper;
import com.wangyu001.constant.C;
import com.wangyu001.constant.Logic;
import com.wangyu001.entity.Domain;
import com.wangyu001.entity.UserUrl;
import com.wangyu001.entity.UserUrlCat;
import com.wangyu001.entity.WangyuUser;
import com.wangyu001.logic.UserTopUrlLogic;
import com.wangyu001.support.UserData;

public class UserTopUrlAction extends TransAction {

    UserTopUrlLogic userTopUrlLogic = Logic.userTopUrlLogic;

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper, String dispatch) throws Exception {
        String resp = null;
        if ("update".equals(dispatch)) {
            resp = this.update(request, response, session, wrapper);
        } else if ("remove".equals(dispatch)) {
            resp = this.remove(request, response, session, wrapper);
        } else if ("fetchUserTopUrl".equals(dispatch)) {
            resp = this.fetchUserTopUrl(request, response, session, wrapper);
        } else if ("getUserUrlCat".equals(dispatch)) {
            resp = this.getUserUrlCat(request, response, session, wrapper);
        } else if ("fetchFriendTopUrl".equals(dispatch)) {
            resp = this.fetchFriendTopUrl(request, response, session, wrapper);
        }
        return new ActionForward(resp, ActionForward.RESP_TYPE_PAGE);
    }

    private String remove(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        return null;
    }

    private String update(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        return null;
    }

    private String getUserUrlCat(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = userData.getWangyuUser().getUserId();
        List<List<UserUrlCat>> catList = userTopUrlLogic.fetchUserUrlCatList(userId, userData, 1);
        request.setAttribute("userUrlCatListList", catList);
        return C.Page.USER_URL_CAT_LIST;
    }

    private String fetchUserTopUrl(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Long catId = wrapper.getLong("catId");
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = userData.getWangyuUser().getUserId();
        String isNeedNav = wrapper.getString("isNeedNav");
        int showLevel = getShowLevel(userId, userData);
        int domainStatus = showLevel == 1 ? Domain.DOMAIN_STATUS_BLACK : Domain.DOMAIN_STATUS_WHITE;
        Integer pageNum = wrapper.getPageNum();
        Pager<UserUrl> pager = null;
        if (showLevel == 3) {
            pager = userTopUrlLogic.fetchTopListForPeople(pageNum, userId, catId, showLevel, domainStatus);
        } else {
            pager = userTopUrlLogic.fetchTopListForSelf(pageNum, userId, catId);
        }
        request.setAttribute("pager", pager);
        if (userData.getWangyuUser().getUserId().longValue() == userId) {
            request.setAttribute("atHome", 1);
        }
        if ("true".equals(isNeedNav)) {
            List<List<UserUrlCat>> catList = userTopUrlLogic.fetchUserUrlCatList(userId, userData, showLevel);
            request.setAttribute("userUrlCatListList", catList);
            return C.Page.INDEX_USER_TOP_URL;
        }
        return C.Page.USER_TOP_URL_LIST;
    }

    private String fetchFriendTopUrl(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Long userId = wrapper.getLong("userId");
        Long catId = wrapper.getLong("catId");
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        String isNeedNav = wrapper.getString("isNeedNav");
        int showLevel = getShowLevel(userId, userData);
        int domainStatus = showLevel == 1 ? Domain.DOMAIN_STATUS_BLACK : Domain.DOMAIN_STATUS_WHITE;
        Integer pageNum = wrapper.getPageNum();
        request.setAttribute("pager", userTopUrlLogic.fetchTopListForPeople(pageNum, userId, catId, showLevel, domainStatus));
        if ("true".equals(isNeedNav)) {
            List<List<UserUrlCat>> catList = userTopUrlLogic.fetchUserUrlCatList(userId, userData, showLevel);
            request.setAttribute("userUrlCatListList", catList);
            return C.Page.INDEX_FRIEND_TOP_URL;
        }
        return C.Page.FRIEND_TOP_URL_LIST;
    }

    private int getShowLevel(Long userIdFromParam, UserData userData) {
        int showLevel = 0;
        WangyuUser wangyuUser = userData.getWangyuUser();
        if (wangyuUser == null) {
            return 3;
        }
        if (userIdFromParam.equals(wangyuUser.getUserId())) {
            if (UserData.USER_STATUS_IN != userData.getUserStatus()) {
                showLevel = 3;
            } else {
                showLevel = 1;
            }
        } else {
            if (false) {
                showLevel = 2;
            } else {
                showLevel = 3;
            }
        }
        return showLevel;
    }
}
