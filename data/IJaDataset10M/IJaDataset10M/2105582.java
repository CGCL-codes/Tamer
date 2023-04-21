package ftn.edu.ais.webapp.action;

import com.opensymphony.xwork2.Preparable;
import ftn.edu.ais.service.HraniteljskaPorodicaManager;
import ftn.edu.ais.model.HraniteljskaPorodica;
import java.util.List;

public class HraniteljskaPorodicaAction extends BaseAction implements Preparable {

    private HraniteljskaPorodicaManager hraniteljskaPorodicaManager;

    private List hraniteljskaPorodicas;

    private HraniteljskaPorodica hraniteljskaPorodica;

    private Long brlkh;

    public void setHraniteljskaPorodicaManager(HraniteljskaPorodicaManager hraniteljskaPorodicaManager) {
        this.hraniteljskaPorodicaManager = hraniteljskaPorodicaManager;
    }

    public List getHraniteljskaPorodicas() {
        return hraniteljskaPorodicas;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            String hraniteljskaPorodicaId = getRequest().getParameter("hraniteljskaPorodica.brlkh");
            if (hraniteljskaPorodicaId != null && !hraniteljskaPorodicaId.equals("")) {
                hraniteljskaPorodica = hraniteljskaPorodicaManager.get(new Long(hraniteljskaPorodicaId));
            }
        }
    }

    public String list() {
        hraniteljskaPorodicas = hraniteljskaPorodicaManager.getAll();
        return SUCCESS;
    }

    public void setBrlkh(Long brlkh) {
        this.brlkh = brlkh;
    }

    public HraniteljskaPorodica getHraniteljskaPorodica() {
        return hraniteljskaPorodica;
    }

    public void setHraniteljskaPorodica(HraniteljskaPorodica hraniteljskaPorodica) {
        this.hraniteljskaPorodica = hraniteljskaPorodica;
    }

    public String delete() {
        hraniteljskaPorodicaManager.remove(hraniteljskaPorodica.getBrlkh());
        saveMessage(getText("hraniteljskaPorodica.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (brlkh != null) {
            hraniteljskaPorodica = hraniteljskaPorodicaManager.get(brlkh);
        } else {
            hraniteljskaPorodica = new HraniteljskaPorodica();
        }
        return SUCCESS;
    }

    public String save() throws Exception {
        if (cancel != null) {
            return "cancel";
        }
        if (delete != null) {
            return delete();
        }
        boolean isNew = (hraniteljskaPorodica.getBrlkh() == null);
        hraniteljskaPorodicaManager.save(hraniteljskaPorodica);
        String key = (isNew) ? "hraniteljskaPorodica.added" : "hraniteljskaPorodica.updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}
