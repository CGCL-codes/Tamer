package com.wangyu001.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.sysolar.sun.mvc.core.ActionForward;
import org.sysolar.sun.mvc.core.TransAction;
import org.sysolar.sun.mvc.support.Json;
import org.sysolar.sun.mvc.support.RequestWrapper;
import com.wangyu001.constant.C;
import com.wangyu001.constant.Logic;
import com.wangyu001.entity.HotWebCat;
import com.wangyu001.logic.HotWebCatLogic;
import com.wangyu001.logic.HotWebLogic;
import com.wangyu001.support.UserData;

public class HotWebCatAction extends TransAction {

    HotWebCatLogic hotWebCatLogic = Logic.hotWebCatLogic;

    HotWebLogic hotWebLogic = Logic.hotWebLogic;

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper, String dispatch) throws Exception {
        String resp = null;
        int type = ActionForward.RESP_TYPE_PAGE;
        if ("create".equals(dispatch)) {
            resp = this.create(request, response, session, wrapper);
            type = ActionForward.RESP_TYPE_STRING;
        } else if ("update".equals(dispatch)) {
            resp = this.update(request, response, session, wrapper);
            type = ActionForward.RESP_TYPE_STRING;
        } else if ("remove".equals(dispatch)) {
            resp = this.remove(request, response, session, wrapper);
            type = ActionForward.RESP_TYPE_STRING;
        } else if ("fetchHotWebCatList".equals(dispatch)) {
            resp = this.fetchHotWebCatList(request, response, session, wrapper);
            type = ActionForward.RESP_TYPE_STRING;
        }
        return new ActionForward(resp, type);
    }

    /**
     * 热门分享-创建分类
     * @param request
     * @param response
     * @param session
     * @param wrapper
     * @return
     * @throws Exception
     */
    private String create(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Long userId = ((UserData) session.getAttribute(UserData.KEY_IN_SESSION)).getWangyuUser().getUserId();
        HotWebCat hotWebCat = new HotWebCat().fill(wrapper);
        hotWebCat.setAdminId(userId);
        hotWebCatLogic.create(hotWebCat);
        return Json.toJs("result", C.Ajax.RESULT_OK);
    }

    private String remove(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        HotWebCat hotWebCat = new HotWebCat().fill(wrapper);
        hotWebCatLogic.remove(hotWebCat);
        return Json.toJs("result", C.Ajax.RESULT_OK);
    }

    /**
     * 热门分享-修改分类
     * @param request
     * @param response
     * @param session
     * @param wrapper
     * @return
     * @throws Exception
     */
    private String update(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Long userId = ((UserData) session.getAttribute(UserData.KEY_IN_SESSION)).getWangyuUser().getUserId();
        HotWebCat hotWebCat = new HotWebCat().fill(wrapper);
        hotWebCat.setAdminId(userId);
        hotWebCatLogic.update(hotWebCat);
        Integer isUpdateHotWeb = wrapper.getInt("isUpdateHotWeb");
        if (isUpdateHotWeb != 0) {
            hotWebLogic.updateTopCatIdByCatId(hotWebCat);
        }
        request.setAttribute(C.Ajax.KEY_INT_VALUE, C.Ajax.RESULT_OK);
        return Json.toJs("result", C.Ajax.RESULT_OK);
    }

    private String fetchHotWebCatList(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        List<HotWebCat> catList = hotWebCatLogic.fetchHotWebCatList();
        return Json.toJs("hotWebCatList", catList);
    }
}
