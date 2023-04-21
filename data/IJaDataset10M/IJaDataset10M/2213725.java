package at.voctrainee.service.facade;

import at.voctrainee.model.bo.WordBo;
import at.voctrainee.model.service.BusinessService;
import at.voctrainee.service.dao.BusinessDaoHibernate;
import at.voctrainee.service.db.Hibernate;
import at.voctrainee.service.dto.WordDto;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import org.hibernate.Session;

public class SessionFacadeBean implements SessionBean {

    private SessionContext context;

    public void ejbCreate() {
    }

    public void setSessionContext(SessionContext context) throws EJBException {
        this.context = context;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    /**
     * Legt ein Wort in der Datenbank an.
     * @param word das anzulegende Wort
     */
    public void createWord(WordDto word) throws Exception {
        final BusinessDaoHibernate businessDao = new BusinessDaoHibernate();
        final BusinessService service = new BusinessService(businessDao);
        System.out.println("Facade: Erstelle neues Wort: " + word.getName());
        service.createWord(new WordBo(word.getName()));
    }
}
