package com.hk.web.cmpunion.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.pub.util.CmpUnionModuleUtil;
import web.pub.util.CmpUnionSite;
import web.pub.util.CmpUnionSiteOrder;
import com.hk.bean.CmpUnion;
import com.hk.bean.CmpUnionKind;
import com.hk.frame.util.DataUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpUnionService;

@Component("/union/union")
public class CmpUnionAction extends CmpUnionBaseAction {

    @Autowired
    private CmpUnionService cmpUnionService;

    public String setmobile(HkRequest req, HkResponse resp) {
        CmpUnionUtil.setBrowseTypeCookie(resp, false);
        return "r:/union/union.do?uid=" + req.getLong("uid");
    }

    public String setpc(HkRequest req, HkResponse resp) {
        CmpUnionUtil.setBrowseTypeCookie(resp, true);
        return "r:/union/union.do?uid=" + req.getLong("uid");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String webname(HkRequest req, HkResponse resp) {
        CmpUnion cmpUnion = this.getCmpUnion(req);
        long uid = cmpUnion.getUid();
        return "/union/union.do?uid=" + uid;
    }

    public String execute(HkRequest req, HkResponse resp) {
        long uid = req.getLongAndSetAttr("uid");
        return this.process(req, resp, uid);
    }

    public String process(HkRequest req, HkResponse resp, long uid) {
        CmpUnion cmpUnion = this.getCmpUnion(req);
        if (cmpUnion == null) {
            return this.getNotFoundForward(resp);
        }
        if (DataUtil.isEmpty(cmpUnion.getData())) {
            cmpUnion.setData(CmpUnionModuleUtil.getDefaultOrder());
            this.cmpUnionService.updateCmpUnionData(uid, cmpUnion.getData());
        }
        CmpUnionSite cmpUnionSite = new CmpUnionSite(cmpUnion.getData());
        req.setAttribute("cmpUnionSite", cmpUnionSite);
        List<Integer> anlist = new ArrayList<Integer>();
        for (CmpUnionSiteOrder order : cmpUnionSite.getCmpUnionSiteOrderList()) {
            if (!order.isHide()) {
                req.setAttribute("loadmod_" + order.getModule(), true);
                anlist.add(order.getModule());
            }
        }
        if (anlist.size() > 0) {
            anlist.remove(0);
        }
        int i = 0;
        for (CmpUnionSiteOrder order : cmpUnionSite.getCmpUnionSiteOrderList()) {
            if (!order.isHide()) {
                if (i < anlist.size()) {
                    order.setNextModule(anlist.get(i++));
                }
                req.setAttribute("mod_" + order.getModule(), order);
            }
        }
        req.setAttribute("cmpUnion", cmpUnion);
        List<CmpUnionKind> kindlist = this.cmpUnionService.getCmpUnionKindListByUid(uid, 0, 0, 10);
        req.setAttribute("kindlist", kindlist);
        return this.getUnionWapJsp("index/index.jsp");
    }
}
