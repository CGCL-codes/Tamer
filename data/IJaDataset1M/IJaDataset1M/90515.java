package beans.validation;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import logger.Log;

/**
 *
 * @author marcy
 */
public class CellValidator implements Validator {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("PortaleVisitePU");

    EntityManager em = emf.createEntityManager();

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String cellulare = (String) value;
        Object obj = null;
        if (!cellulare.matches("[0-9]*")) {
            throw new ValidatorException(new FacesMessage("Ivalid Characters"));
        }
        try {
            obj = em.createNativeQuery("Select r.cellulare from Referenze r where r.cellulare = ?").setParameter(1, cellulare).getSingleResult();
        } catch (NoResultException ex) {
            Log.getLogger().debug("No resul exception");
        } finally {
            if (obj != null) {
                throw new ValidatorException(new FacesMessage());
            }
        }
    }
}
