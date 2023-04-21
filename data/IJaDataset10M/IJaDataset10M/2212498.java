package com.dotmarketing.menubuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.factories.IdentifierFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.util.WebKeys;

public class CrumbTrailListBuilder implements ViewTool {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public List getCrumbTrail(Host host) {
        if (request.getAttribute(WebKeys.CRUMB_TRAIL) != null) {
            return (List) request.getAttribute(WebKeys.CRUMB_TRAIL);
        }
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("title", "Home");
        map.put("url", "/");
        list.add(map);
        Identifier id = (Identifier) InodeFactory.getInode((String) request.getAttribute("idInode"), Identifier.class);
        HTMLPage htmlPage = (HTMLPage) IdentifierFactory.getWorkingChildOfClass(id, HTMLPage.class);
        Folder folder = (Folder) InodeFactory.getParentOfClass(htmlPage, Folder.class);
        if (folder.getInode() == 0 || htmlPage.getInode() == 0) {
            map = new HashMap();
            map.put("title", "Page Not Found");
            map.put("url", "");
            map.put("theEnd", "true");
            list.add(map);
            return list;
        }
        map = new HashMap();
        map.put("title", htmlPage.getTitle());
        map.put("url", folder.getPath() + htmlPage.getPageUrl());
        map.put("theEnd", "true");
        list.add(map);
        if (htmlPage.getPageUrl().startsWith("index") || folder.getPath().startsWith("/global")) {
            folder = (Folder) InodeFactory.getParentOfClass(folder, Folder.class);
        }
        while (folder.getInode() != 0) {
            if (folder.getInode() == host.getInode() || folder.getPath().startsWith("/home")) {
                break;
            }
            map = new HashMap();
            map.put("title", folder.getTitle());
            map.put("url", folder.getPath());
            if (folder.isShowOnMenu()) {
                list.add(1, map);
            }
            folder = (Folder) InodeFactory.getParentOfClass(folder, Folder.class);
        }
        return list;
    }

    /**
     * Initializes this instance for the current request.
     * 
     * @param obj
     *            the ViewContext of the current request
     */
    public void init(Object obj) {
        ViewContext context = (ViewContext) obj;
        this.request = context.getRequest();
        this.response = context.getResponse();
    }
}
