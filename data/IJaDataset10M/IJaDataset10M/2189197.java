package com.coyousoft.wangyu.action;

import static com.coyousoft.wangyu.constant.Logic.wangyuSkinLogic;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.sysolar.sun.mvc.core.ActionForward;
import org.sysolar.sun.mvc.core.BaseAction;
import org.sysolar.sun.mvc.support.Json;
import org.sysolar.sun.mvc.support.RequestWrapper;
import com.coyousoft.wangyu.entity.WangyuSkin;

public class WangyuSkinAction extends BaseAction {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper, String dispatch) throws Exception {
        if ("create".equals(dispatch)) {
            return this.create(request, response, session, wrapper);
        }
        if ("jsonCreate".equals(dispatch)) {
            return this.jsonCreate(request, response, session, wrapper);
        }
        if ("remove".equals(dispatch)) {
            return this.remove(request, response, session, wrapper);
        }
        if ("update".equals(dispatch)) {
            return this.update(request, response, session, wrapper);
        }
        if ("fetch".equals(dispatch)) {
            return this.fetch(request, response, session, wrapper);
        }
        if ("export".equals(dispatch)) {
            return this.export(request, response, session, wrapper);
        }
        if ("fetchList".equals(dispatch)) {
            return this.fetchList(request, response, session, wrapper);
        }
        return null;
    }

    private ActionForward fetchList(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        String resp = "";
        String key = wrapper.getString("key");
        List<WangyuSkin> skinCatList = wangyuSkinLogic.fetchList();
        resp = Json.toJs(key, skinCatList);
        return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
    }

    private ActionForward create(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        wangyuSkinLogic.create(new WangyuSkin().fill(wrapper));
        return new ActionForward(1, ActionForward.RESP_TYPE_STRING);
    }

    @SuppressWarnings("unchecked")
    private ActionForward jsonCreate(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Object jsonData = Json.toJavaObject(wrapper.getString("jsonData"));
        if (jsonData instanceof Map) {
            wangyuSkinLogic.create(new WangyuSkin().fillJson((Map<String, Object>) jsonData));
        } else {
            List<Object> list = (List<Object>) jsonData;
            List<WangyuSkin> wangyuSkinList = new ArrayList<WangyuSkin>(list.size());
            for (Object obj : list) {
                wangyuSkinList.add(new WangyuSkin().fillJson((Map<String, Object>) obj));
            }
            wangyuSkinLogic.create(wangyuSkinList);
        }
        return new ActionForward(1, ActionForward.RESP_TYPE_STRING);
    }

    private ActionForward remove(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        int resp = wangyuSkinLogic.remove(wrapper.getInteger("skinId"));
        return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
    }

    private ActionForward update(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        int resp = wangyuSkinLogic.update(new WangyuSkin().fill(wrapper));
        return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
    }

    private ActionForward fetch(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        WangyuSkin wangyuSkin = wangyuSkinLogic.fetch(wrapper.getInteger("skinId"));
        String resp = Json.toJs("skinId", wangyuSkin);
        return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
    }

    private ActionForward export(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        List<WangyuSkin> wangyuSkinList = wangyuSkinLogic.export(wrapper.getInteger("offset"), wrapper.getInteger("limit"));
        String resp = Json.toJs("wangyuSkinList", wangyuSkinList);
        return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
    }
}
