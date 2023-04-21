package sk.sigp.tetras.frontend.web.omitted;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import sk.sigp.tetras.entity.Country;
import sk.sigp.tetras.entity.NechcenaDestinacia;
import sk.sigp.tetras.frontend.web.auth.AuthBean;
import sk.sigp.tetras.utils.ServiceLocator;

public class EditBean {

    private static Logger LOG = Logger.getLogger(EditBean.class);

    private Long id;

    private NechcenaDestinacia destination;

    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String cancel() {
        return "ok";
    }

    public NechcenaDestinacia getDestination() {
        if (destination == null) {
            destination = ServiceLocator.getInstance().getNechcenaDestinaciaDao().get(getId());
            List<Country> c = ServiceLocator.getInstance().getCountryDao().findByShortId(destination.getCountry());
            if (c.size() > 0) country = c.get(0).getDescription();
        }
        return destination;
    }

    public List<SelectItem> getCountries() {
        List<Country> countries = ServiceLocator.getInstance().getCountryDao().findAll();
        List<SelectItem> res = new ArrayList<SelectItem>();
        for (Country c : countries) {
            SelectItem item = new SelectItem(c, c.getShortId());
            res.add(item);
        }
        return res;
    }

    /**
	 * will delete algorithm
	 * @return
	 */
    public String delete() {
        AuthBean.infoLog("Deleting ommited destination " + getDestination(), LOG);
        ServiceLocator.getInstance().getBasicEntityService().delete(getDestination());
        return "ok";
    }

    /**
	 * will edit algorithm
	 * @return
	 */
    public String edit() {
        Country c = null;
        for (SelectItem ctx : getCountries()) {
            if (ctx.getLabel().equals(getCountry())) {
                c = (Country) ctx.getValue();
                break;
            }
        }
        if (c != null) {
            getDestination().setCountry(c.getShortId());
        }
        AuthBean.infoLog("Modifying ommited destination " + getDestination(), LOG);
        ServiceLocator.getInstance().getNechcenaDestinaciaDao().modify(getId(), getDestination());
        return "ok";
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
