package net.sourceforge.jsfannonition.example.service;

import java.util.List;
import net.sourceforge.jsfannonition.example.dao.KlantenDAO;
import net.sourceforge.jsfannonition.example.model.Adres;
import net.sourceforge.jsfannonition.example.model.Klant;
import net.sourceforge.jsfannonition.example.model.SelectieCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class KlantenServiceImpl implements KlantenService {

    private KlantenDAO klantenDAO;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Klant> findKlanten(SelectieCriteria selectieCriteria) {
        List<Klant> returnValue = null;
        if (selectieCriteria == null) {
            returnValue = this.klantenDAO.findAll();
        } else {
            Klant exampleKlant = new Klant();
            if (!StringUtils.isEmpty(selectieCriteria.getAchternaam())) {
                exampleKlant.setAchternaam(selectieCriteria.getAchternaam());
                if ("bug".equals(selectieCriteria.getAchternaam())) {
                    throw new IllegalArgumentException("Debug bug is geen geldig zoekcriterium.");
                }
            }
            if (!StringUtils.isEmpty(selectieCriteria.getVoornaam())) {
                exampleKlant.setVoornaam(selectieCriteria.getVoornaam());
            }
            Adres adres = new Adres();
            if (!StringUtils.isEmpty(selectieCriteria.getPlaats())) {
                adres.setPlaats(selectieCriteria.getPlaats());
            }
            exampleKlant.setAdres(adres);
            returnValue = this.klantenDAO.findByExample(exampleKlant, "bestellingen", "id", "tussenvoegsel", "versie", "adres.huisnummer", "adres.postcode", "adres.straatnaam", "adres.toevoeging");
        }
        return returnValue;
    }

    /**
	 * @param klantenDAO the klantenDAO to set
	 */
    public void setKlantenDAO(KlantenDAO klantenDAO) {
        this.klantenDAO = klantenDAO;
    }
}
