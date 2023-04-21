package blog;

import java.util.List;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/ManagerModify.action")
public class ManagerModifyAction extends Action {

    private String formermanager = "";

    private String currentmanager = "";

    private Post managerPW = null;

    public void setformerManager(String formermanager) {
        this.formermanager = formermanager;
    }

    public void setcurrentManager(String currentmanager) {
        this.currentmanager = currentmanager;
    }

    public Resolution execute() throws Exception {
        managerPW = postDao.manager();
        String PW = managerPW.getManager();
        if (formermanager.equals(PW)) {
            managerPW.setManager(currentmanager);
            return new RedirectResolution("/Login.jsp");
        } else {
            return new RedirectResolution("/Login.jsp");
        }
    }
}
