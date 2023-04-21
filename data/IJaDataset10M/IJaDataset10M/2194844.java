package web.epp.mgr.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.pub.action.EppBaseAction;
import com.hk.bean.CmpFrLink;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpFrLinkService;
import com.hk.svr.pub.Err;

/**
 * 友情链接
 * 
 * @author akwei
 */
@Component("/epp/web/op/webadmin/cmpfrlink")
public class CmpFrLinkAction extends EppBaseAction {

    @Autowired
    private CmpFrLinkService cmpFrLinkService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        req.setAttribute("active_22", 1);
        long companyId = req.getLong("companyId");
        List<CmpFrLink> list = this.cmpFrLinkService.getCmpFrLinkListByCompanyId(companyId);
        req.setAttribute("list", list);
        return this.getWebPath("admin/cmpfrlink/list.jsp");
    }

    /**
	 * 创建
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-27
	 */
    public String create(HkRequest req, HkResponse resp) throws Exception {
        if (req.getInt("ch") == 0) {
            return this.getWebPath("admin/cmpfrlink/create.jsp");
        }
        long companyId = req.getLong("companyId");
        CmpFrLink cmpFrLink = new CmpFrLink();
        cmpFrLink.setCompanyId(companyId);
        cmpFrLink.setName(req.getHtmlRow("name"));
        cmpFrLink.setUrl(req.getHtmlRow("url"));
        int code = cmpFrLink.validate();
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "createerror", null);
        }
        if (cmpFrLink.getUrl().toLowerCase().startsWith("http://")) {
            cmpFrLink.setUrl(cmpFrLink.getUrl().substring(7));
        }
        this.cmpFrLinkService.createCmpFrLink(cmpFrLink);
        this.setOpFuncSuccessMsg(req);
        return this.onSuccess2(req, "createok", null);
    }

    /**
	 * 修改
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-27
	 */
    public String update(HkRequest req, HkResponse resp) throws Exception {
        long linkId = req.getLongAndSetAttr("linkId");
        CmpFrLink cmpFrLink = this.cmpFrLinkService.getCmpFrLink(linkId);
        if (cmpFrLink == null) {
            return null;
        }
        req.setAttribute("cmpFrLink", cmpFrLink);
        if (req.getInt("ch") == 0) {
            return this.getWebPath("admin/cmpfrlink/update.jsp");
        }
        cmpFrLink.setName(req.getHtmlRow("name"));
        cmpFrLink.setUrl(req.getHtmlRow("url"));
        int code = cmpFrLink.validate();
        if (code != Err.SUCCESS) {
            return this.onError(req, code, "updateerror", null);
        }
        if (cmpFrLink.getUrl().toLowerCase().startsWith("http://")) {
            cmpFrLink.setUrl(cmpFrLink.getUrl().substring(7));
        }
        this.cmpFrLinkService.updateCmpFrLink(cmpFrLink);
        this.setOpFuncSuccessMsg(req);
        return this.onSuccess2(req, "updateok", null);
    }

    /**
	 * 删除
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-27
	 */
    public String del(HkRequest req, HkResponse resp) throws Exception {
        long linkId = req.getLongAndSetAttr("linkId");
        long companyId = req.getLong("companyId");
        CmpFrLink cmpFrLink = this.cmpFrLinkService.getCmpFrLink(linkId);
        if (cmpFrLink == null) {
            return null;
        }
        if (cmpFrLink.getCompanyId() == companyId) {
            this.cmpFrLinkService.deleteCmpFrLink(linkId);
            this.setDelSuccessMsg(req);
        }
        return null;
    }
}
