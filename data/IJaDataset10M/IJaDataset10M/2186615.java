package ftn.edu.ais.webapp.action;

import com.opensymphony.xwork2.Preparable;
import ftn.edu.ais.service.KategorijaManager;
import ftn.edu.ais.model.Kategorija;
import java.util.List;

public class KategorijaAction extends BaseAction implements Preparable {

    private KategorijaManager kategorijaManager;

    private List kategorijas;

    private Kategorija kategorija;

    private Long idk;

    public void setKategorijaManager(KategorijaManager kategorijaManager) {
        this.kategorijaManager = kategorijaManager;
    }

    public List getKategorijas() {
        return kategorijas;
    }

    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            String kategorijaId = getRequest().getParameter("kategorija.idk");
            if (kategorijaId != null && !kategorijaId.equals("")) {
                kategorija = kategorijaManager.get(new Long(kategorijaId));
            }
        }
    }

    public String list() {
        kategorijas = kategorijaManager.getAll();
        return SUCCESS;
    }

    public void setIdk(Long idk) {
        this.idk = idk;
    }

    public Kategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(Kategorija kategorija) {
        this.kategorija = kategorija;
    }

    public String delete() {
        kategorijaManager.remove(kategorija.getIdk());
        saveMessage(getText("kategorija.deleted"));
        return SUCCESS;
    }

    public String edit() {
        if (idk != null) {
            kategorija = kategorijaManager.get(idk);
        } else {
            kategorija = new Kategorija();
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
        boolean isNew = (kategorija.getIdk() == null);
        kategorijaManager.save(kategorija);
        String key = (isNew) ? "kategorija.added" : "kategorija.updated";
        saveMessage(getText(key));
        if (!isNew) {
            return INPUT;
        } else {
            return SUCCESS;
        }
    }
}
