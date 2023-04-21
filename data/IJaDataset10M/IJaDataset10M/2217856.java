package web.epp.action;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.pub.action.EppBaseAction;
import com.hk.bean.CmpDownFile;
import com.hk.bean.CmpInfo;
import com.hk.bean.CmpNav;
import com.hk.bean.Company;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.DownloadFile;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.CmpDownFileService;
import com.hk.svr.pub.ImageConfig;

@Component("/epp/web/file")
public class FileAction extends EppBaseAction {

    @Autowired
    private CmpDownFileService cmpDownFileService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        Company o = (Company) req.getAttribute("o");
        if (o.getCmpflg() == 0) {
            return this.web0(req, resp);
        }
        if (o.getCmpflg() == 1) {
            return this.web1(req, resp);
        }
        return null;
    }

    private String web0(HkRequest req, HkResponse resp) {
        CmpInfo cmpInfo = (CmpInfo) req.getAttribute("cmpInfo");
        if (cmpInfo.getTmlflg() == 0) {
            return this.web00(req, resp);
        }
        return null;
    }

    private String web1(HkRequest req, HkResponse resp) {
        CmpInfo cmpInfo = (CmpInfo) req.getAttribute("cmpInfo");
        if (cmpInfo.getTmlflg() == 0) {
            return this.web10(req, resp);
        }
        return null;
    }

    /**
	 * 模板0/0的文件列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 *         2010-6-22
	 */
    private String web00(HkRequest req, HkResponse resp) {
        long navId = req.getLongAndSetAttr("navId");
        long companyId = req.getLong("companyId");
        this.setCmpNavInfo(req);
        CmpNav cmpNav = (CmpNav) req.getAttribute("cmpNav");
        if (cmpNav == null) {
            return null;
        }
        SimplePage page = req.getSimplePage(20);
        List<CmpDownFile> list = this.cmpDownFileService.getCmpDownFileListByCompanyIdAndCmpNavOid(companyId, navId, page.getBegin(), page.getSize() + 1);
        this.processListForPage(page, list);
        req.setAttribute("list", list);
        return this.getWebPath("cmpdownfile/list.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 *         2010-7-2
	 */
    private String web10(HkRequest req, HkResponse resp) {
        return null;
    }

    /**
	 * 文件下载
	 * 
	 * @param req
	 * @param resp
	 * @return
	 *         2010-6-22
	 */
    public String download(HkRequest req, HkResponse resp) {
        long oid = req.getLong("oid");
        CmpDownFile cmpDownFile = this.cmpDownFileService.getCmpDownFile(oid);
        if (cmpDownFile == null) {
            return null;
        }
        String filePath = ImageConfig.getCmpDownFileFilePath(cmpDownFile.getPath());
        File file = new File(filePath + ImageConfig.RARFILE);
        DownloadFile.download(resp, file, cmpDownFile.getName() + ".rar");
        this.cmpDownFileService.addCmpDownFileDcount(oid, 1);
        return null;
    }
}
