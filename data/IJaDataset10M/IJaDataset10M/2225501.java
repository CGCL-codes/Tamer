package org.fao.fenix.web.eh.upload;

import javax.servlet.http.HttpServletRequest;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

@RemoteProxy(creator = SpringCreator.class, creatorParams = @Param(name = "beanName", value = "uploadMonitor"), name = "UploadMonitor")
public class UploadMonitor {

    public UploadInfo getUploadInfo() {
        HttpServletRequest req = WebContextFactory.get().getHttpServletRequest();
        if (req.getSession().getAttribute("uploadInfo") != null) return (UploadInfo) req.getSession().getAttribute("uploadInfo"); else return new UploadInfo();
    }
}
