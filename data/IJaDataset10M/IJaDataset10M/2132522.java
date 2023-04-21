package org.chon.web.api.res;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.chon.web.api.Application;
import org.chon.web.api.Request;
import org.chon.web.api.Resource;
import org.chon.web.api.Response;
import org.chon.web.api.ServerInfo;
import org.chon.web.api.res.action.Processor;
import org.chon.web.api.res.action.impl.AjaxProcessor;
import org.chon.web.api.res.action.impl.DoProcessor;
import org.chon.web.api.res.action.impl.UploadProcessor;
import org.chon.web.mpac.InitStatusErrorHandler;
import org.chon.web.mpac.InitStatusInfo;
import org.chon.web.mpac.Initializer;
import org.chon.web.mpac.ModulePackage;

public class ActionResource implements Resource {

    private ModulePackage modulePackage;

    private String action;

    private String ext;

    private ServerInfo serverInfo;

    public static Resource create(String action, String ext, ModulePackage mp) {
        ActionResource res = new ActionResource();
        res.action = action;
        res.ext = ext;
        res.modulePackage = mp;
        return res;
    }

    public static Resource create(String action, ModulePackage mp) {
        return create(action, "do", mp);
    }

    @Override
    public void process(ServerInfo si) {
        this.serverInfo = si;
        Application app = si.getApplication();
        Request req = si.getReq();
        req.setAction(action);
        req.setExtension(ext);
        Response resp = si.getResp();
        Properties props = app.getAppProperties();
        if (props != null) {
            Iterator<Object> it = props.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                resp.getTemplateContext().put(key, props.get(key));
            }
        }
        si.getResp().getTemplateContext().put("app", app);
        si.getResp().getTemplateContext().put("user", req.getUser());
        Initializer initializer = modulePackage.getInitializer();
        InitStatusInfo initStatusInfo = InitStatusInfo.DEFAULT;
        if (initializer != null) {
            initStatusInfo = initializer.process(app, req, resp);
        }
        if (initStatusInfo == InitStatusInfo.REDIRECT) {
            return;
        }
        if (initStatusInfo.getStatus() < 0) {
            InitStatusErrorHandler eh = modulePackage.getInitStatusErrorHandler();
            if (eh != null) {
                eh.handleStatusError(initStatusInfo, app, req, resp);
            } else {
                resp.getOut().write("NO HANDLER FOR STATUS ERROR. Error was: " + initStatusInfo.getInfo());
            }
        } else {
            Processor processor = getProcessor(ext);
            if (processor == null) {
                throw new RuntimeException("Unknown extension: ." + ext + " Path = " + req.getPath());
            }
            processor.process(this);
        }
    }

    public ModulePackage getModulePackage() {
        return modulePackage;
    }

    public String getAction() {
        return action;
    }

    public String getExt() {
        return ext;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    private static Map<String, Processor> processors = new HashMap<String, Processor>();

    static {
        processors.put("do", new DoProcessor());
        processors.put("ajax", new AjaxProcessor());
        processors.put("upload", new UploadProcessor());
    }

    protected Processor getProcessor(String ext) {
        return processors.get(ext);
    }
}
