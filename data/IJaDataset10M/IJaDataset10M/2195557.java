package web.epp.action;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.epp.mgr.action.CmpNavUtil;
import web.pub.action.EppBaseAction;
import com.hk.bean.CmpNav;
import com.hk.frame.util.DataUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpNavService;

@Component("/epp/web/cmpnav")
public class CmpNavAction extends EppBaseAction {

    @Autowired
    private CmpNavService cmpNavService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        long navId = req.getLong("navId");
        CmpNav cmpNav = this.cmpNavService.getCmpNav(navId);
        if (cmpNav == null) {
            return null;
        }
        if (cmpNav.isUrlLink()) {
            return "r:http://" + cmpNav.getUrl();
        }
        if (cmpNav.isHomeNav()) {
            return "r:http://" + req.getServerName();
        }
        List<CmpNav> list = this.cmpNavService.getCmpNavListByCompanyIdAndParentId(companyId, cmpNav.getOid());
        CmpNav o = null;
        if (list.size() > 0) {
            o = list.get(0);
        } else {
            o = cmpNav;
        }
        String url = CmpNavUtil.getWebFuncUrl(o);
        if (url != null) {
            return "r:" + url;
        }
        return null;
    }

    /**
	 * wap访问的导航
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-21
	 */
    public String wap(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        long navId = req.getLongAndSetAttr("navId");
        CmpNav cmpNav = this.cmpNavService.getCmpNav(navId);
        if (cmpNav == null) {
            return null;
        }
        if (cmpNav.isUrlLink()) {
            if (this.isWap(req)) {
                if (this.isForwardPage(req)) {
                    String url = req.getHeader("referer");
                    if (DataUtil.isEmpty(url)) {
                        url = "/m";
                    }
                    req.setReturnUrl(url);
                    return this.getWapPath("cmpnav/cfmurl.jsp");
                }
                if (req.getString("ok") != null) {
                    return "r:http://" + cmpNav.getUrl();
                }
                return "r:" + req.getReturnUrl();
            }
            return "r:http://" + cmpNav.getUrl();
        }
        if (cmpNav.isHomeNav()) {
            return "r:/m";
        }
        List<CmpNav> list = this.cmpNavService.getCmpNavListByCompanyIdAndParentId(companyId, cmpNav.getOid());
        if (cmpNav.isDirectory()) {
            req.setAttribute("list", list);
            return this.getWapPath("cmpnav/index.jsp");
        }
        CmpNav o = null;
        if (list.size() > 0) {
            o = list.get(0);
        } else {
            o = cmpNav;
        }
        String url = CmpNavUtil.getWapFuncUrl(o);
        if (url != null) {
            return "r:" + url;
        }
        return null;
    }

    /**
	 * 待建设页面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-18
	 */
    public String view(HkRequest req, HkResponse resp) throws Exception {
        return this.getWebPath("cmpnav/view.jsp");
    }

    /**
	 * 待建设页面
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 *             2010-5-18
	 */
    public String wapview(HkRequest req, HkResponse resp) throws Exception {
        return this.getWapPath("cmpnav/wapview.jsp");
    }
}
