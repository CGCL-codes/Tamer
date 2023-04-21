package com.hk.web.cmpunion.action;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.CmpProduct;
import com.hk.bean.CmpUnionCmdKind;
import com.hk.bean.CmpUnionKind;
import com.hk.bean.Company;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpProductService;
import com.hk.svr.CmpUnionService;
import com.hk.svr.CompanyService;

@Component("/union/kind")
public class CmpUnionKindAction extends CmpUnionBaseAction {

    @Autowired
    private CmpUnionService cmpUnionService;

    @Autowired
    private CmpProductService cmpProductService;

    @Autowired
    private CompanyService companyService;

    public String execute(HkRequest req, HkResponse resp) {
        long kindId = req.getLongAndSetAttr("kindId");
        CmpUnionKind cmpUnionKind = this.cmpUnionService.getCmpUnionKind(kindId);
        if (cmpUnionKind == null) {
            return null;
        }
        req.setAttribute("cmpUnionKind", cmpUnionKind);
        List<CmpUnionKind> list2 = new ArrayList<CmpUnionKind>();
        this.loadCmpUnionKindList(list2, kindId);
        req.setAttribute("list2", list2);
        int p = req.getIntAndSetAttr("p");
        if (cmpUnionKind.isHasChild()) {
            return this.listKind(req, resp, cmpUnionKind);
        }
        if (p == 1) {
            return this.listProduct(req, resp, cmpUnionKind);
        }
        return this.listCmp(req, resp, cmpUnionKind);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String list(HkRequest req, HkResponse resp) {
        long uid = req.getLongAndSetAttr("uid");
        SimplePage page = req.getSimplePage(20);
        List<CmpUnionKind> kindlist = this.cmpUnionService.getCmpUnionKindListByUid(uid, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, kindlist);
        req.setAttribute("kindlist", kindlist);
        return this.getUnionWapJsp("kind/kindlist2.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String cmdlist(HkRequest req, HkResponse resp) {
        long uid = req.getLongAndSetAttr("uid");
        SimplePage page = req.getSimplePage(20);
        List<CmpUnionCmdKind> kindlist = this.cmpUnionService.getCmpUnionCmdKindListByUid(uid, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, kindlist);
        List<Long> idList = new ArrayList<Long>();
        for (CmpUnionCmdKind kind : kindlist) {
            idList.add(kind.getKindId());
        }
        List<CmpUnionKind> cmdkindlist = cmpUnionService.getCmpUnionKindListInId(uid, idList);
        req.setAttribute("cmdkindlist", cmdkindlist);
        return this.getUnionWapJsp("kind/cmdkindlist.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @param cmpUnionKind
	 * @return
	 */
    private String listKind(HkRequest req, HkResponse resp, CmpUnionKind cmpUnionKind) {
        SimplePage page = req.getSimplePage(20);
        List<CmpUnionKind> kindlist = this.cmpUnionService.getCmpUnionKindListByUid(cmpUnionKind.getUid(), cmpUnionKind.getKindId(), page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, kindlist);
        req.setAttribute("kindlist", kindlist);
        return this.getUnionWapJsp("kind/kindlist.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @param cmpUnionKind
	 * @return
	 */
    private String listProduct(HkRequest req, HkResponse resp, CmpUnionKind cmpUnionKind) {
        long kindId = req.getLongAndSetAttr("kindId");
        SimplePage page = req.getSimplePage(20);
        List<CmpProduct> productlist = this.cmpProductService.getCmpProductListByCmpUnionKindId(kindId, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, productlist);
        req.setAttribute("productlist", productlist);
        return this.getUnionWapJsp("product/productlist.jsp");
    }

    /**
	 * 获取联盟分类的足迹
	 * 
	 * @param req
	 * @param resp
	 * @param cmpUnionKind
	 * @return
	 */
    private String listCmp(HkRequest req, HkResponse resp, CmpUnionKind cmpUnionKind) {
        long kindId = req.getLongAndSetAttr("kindId");
        SimplePage page = req.getSimplePage(20);
        List<Company> companylist = this.companyService.getCompanyListByUnionKindId(kindId, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, companylist);
        req.setAttribute("companylist", companylist);
        return this.getUnionWapJsp("cmp/kindcmplist.jsp");
    }
}
