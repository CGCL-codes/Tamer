package playboy;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class OgnlExample extends ActionSupport {

    private HttpServletRequest request;

    private HttpSession session;

    private List<Material> materials;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    @Override
    public String execute() throws Exception {
        request = ServletActionContext.getRequest();
        session = request.getSession();
        request.setAttribute("requestName", "request-material from request");
        session.setAttribute("sessionName", "session-material from session");
        materials = new ArrayList<Material>();
        materials.add(new Material("First", 35, 1));
        materials.add(new Material("Second", 55, 3));
        materials.add(new Material("Third", 80, 5));
        return SUCCESS;
    }
}
