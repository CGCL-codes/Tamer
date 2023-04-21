package com.hk.api.action.cmp;

import java.util.List;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.hk.api.action.BaseApiAction;
import com.hk.bean.CmpModule;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.TemplateService;

public class CmpModuleAction extends BaseApiAction {

    @Autowired
    private TemplateService templateService;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        List<CmpModule> list = this.templateService.getCmpModuleList(companyId);
        VelocityContext context = new VelocityContext();
        context.put("list", list);
        this.write(resp, "vm/e/cmpmodulelist.vm", context);
        return null;
    }

    public String ignorehide(HkRequest req, HkResponse resp) throws Exception {
        long companyId = req.getLong("companyId");
        List<CmpModule> list = this.templateService.getCmpModuleListIgnoreHide(companyId);
        VelocityContext context = new VelocityContext();
        context.put("list", list);
        this.write(resp, "vm/e/cmpmodulelist.vm", context);
        return null;
    }
}
