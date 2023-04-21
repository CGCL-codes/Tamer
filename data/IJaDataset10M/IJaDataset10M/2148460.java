package com.hk.web.hk4.admin;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.CmpActor;
import com.hk.bean.CmpActorPink;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpActorService;
import com.hk.svr.processor.CmpActorProcessor;
import com.hk.web.pub.action.BaseAction;

@Component("/h4/admin/actor")
public class ActorAction extends BaseAction {

    @Autowired
    private CmpActorService cmpActorService;

    @Autowired
    private CmpActorProcessor cmpActorProcessor;

    public String execute(HkRequest req, HkResponse resp) {
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 *         2010-8-23
	 */
    public String pinklist(HkRequest req, HkResponse resp) {
        SimplePage page = req.getSimplePage(20);
        List<CmpActorPink> list = cmpActorProcessor.getCmpActorPinkList(true, true, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, list);
        req.setAttribute("list", list);
        return this.getWeb4Jsp("admin/cmpactor/pinklist.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-8-22
	 */
    public String createcmpactorpink(HkRequest req, HkResponse resp) {
        long actorId = req.getLong("actorId");
        CmpActor cmpActor = this.cmpActorService.getCmpActor(actorId);
        if (cmpActor == null) {
            return null;
        }
        CmpActorPink cmpActorPink = new CmpActorPink();
        cmpActorPink.setActorId(actorId);
        this.cmpActorService.createCmpActorPink(cmpActorPink);
        this.setOpFuncSuccessMsg(req);
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-8-22
	 */
    public String delcmpactorpink(HkRequest req, HkResponse resp) {
        long oid = req.getLong("oid");
        this.cmpActorService.deleteCmpActorPink(oid);
        this.setOpFuncSuccessMsg(req);
        return null;
    }
}
