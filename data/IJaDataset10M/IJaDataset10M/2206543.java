package edu.ftn.ais.webapp.action;

import com.opensymphony.xwork2.Preparable;
import edu.ftn.ais.service.CasoviManager;
import edu.ftn.ais.service.OsobljeManager;
import edu.ftn.ais.service.PolaznikManager;
import edu.ftn.ais.service.VoziloManager;
import edu.ftn.ais.model.Casovi;
import edu.ftn.ais.webapp.action.BaseAction;
import java.util.List;

public class CasoviAction extends BaseAction implements Preparable {

    private CasoviManager casoviManager;

    private List casovies = null;

    private Casovi casovi;

    private Long idCas;

    private OsobljeManager osobljeManager;

    private VoziloManager voziloManager;

    private PolaznikManager polaznikManager;

    private List osobljes = null;

    private List vozilos = null;

    private List polazniks = null;

    public void setCasoviManager(CasoviManager casoviManager) {
        this.casoviManager = casoviManager;
    }

    public void setOsobljeManager(OsobljeManager osobljeManager) {
        this.osobljeManager = osobljeManager;
    }

    public void setVoziloManager(VoziloManager voziloManager) {
        this.voziloManager = voziloManager;
    }

    public void setPolaznikManager(PolaznikManager polaznikManager) {
        this.polaznikManager = polaznikManager;
    }

    public List getCasovies() {
        return casovies;
    }

    public List getOsobljes() {
        return osobljes;
    }

    public List getVozilos() {
        return vozilos;
    }

    public List getPolazniks() {
        return polazniks;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        this.osobljes = osobljeManager.getAll();
        this.vozilos = voziloManager.getAll();
        this.polazniks = polaznikManager.getAll();
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            String casoviId = getRequest().getParameter("casovi.idCas");
            if (casoviId != null && !casoviId.equals("")) {
                casovi = casoviManager.get(new Long(casoviId));
            }
        }
    }

    public String list() {
        casovies = casoviManager.getAll();
        return SUCCESS;
    }

    public void setIdCas(Long idCas) {
        this.idCas = idCas;
    }

    public Casovi getCasovi() {
        return casovi;
    }

    public void setCasovi(Casovi casovi) {
        this.casovi = casovi;
    }

    public String delete() {
        casoviManager.remove(casovi.getIdCas());
        saveMessage(getText("casovi.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (idCas != null) {
            casovi = casoviManager.get(idCas);
        } else {
            casovi = new Casovi();
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
        boolean isNew = (casovi.getIdCas() == null);
        casoviManager.save(casovi);
        String key = (isNew) ? "casovi.added" : "casovi.updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}
