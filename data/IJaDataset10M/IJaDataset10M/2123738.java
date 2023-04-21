package com.knokode.ost.web.action;

import com.knokode.ost.content.admin.ContentAdmin;
import com.knokode.ost.content.admin.Node;
import com.knokode.ost.user.admin.UserAdminService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/{path}.html")
public class ViewActionBean extends BaseActionBean {

    String path;

    @DefaultHandler
    public Resolution view() {
        System.out.println("view");
        System.out.println(path);
        ContentAdmin ca = getBean(ContentAdmin.class);
        Node n = ca.get(path);
        System.out.println("Retrieved " + (n != null ? n.getContent() : "NULL"));
        return new ForwardResolution("/WEB-INF/jsp/home.jsp");
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public String getOsName() {
        return System.getProperty("os.name");
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
