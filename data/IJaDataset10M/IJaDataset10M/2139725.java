package sk.sigp.tetras.frontend.web.company;

import java.util.List;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import sk.sigp.tetras.entity.Firma;
import sk.sigp.tetras.entity.Vystup;
import sk.sigp.tetras.frontend.web.auth.AuthBean;
import sk.sigp.tetras.utils.ServiceLocator;

public class DetailBean {

    private static Logger LOG = Logger.getLogger(DetailBean.class);

    private List<Vystup> vystupy = null;

    private Firma firma;

    private Long id;

    public Firma getFirma() {
        if (firma == null) {
            firma = ServiceLocator.getInstance().getFirmaDao().get(getId());
        }
        return firma;
    }

    /**
	 * will return vystupy by criteria made by certain pars of bean
	 * @return
	 */
    public List<Vystup> getVystupy() {
        if (vystupy == null) {
            vystupy = ServiceLocator.getInstance().getVystupDao().findByFirma(getFirma());
        }
        return vystupy;
    }

    public String back() {
        return "ok";
    }

    public void preview() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        AuthBean.infoLog("Redirecting to preview of fax offer " + id, LOG);
        AuthBean.redirect("preview/?id=" + id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
