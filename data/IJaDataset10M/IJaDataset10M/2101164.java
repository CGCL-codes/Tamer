package openschool.domain.dao.hibernate;

import java.util.List;
import openschool.domain.dao.EleveDAO;
import openschool.domain.model.Eleve;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import openschool.domain.model.Session;

@Repository
public class EleveDAOHibernate implements EleveDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public void addEleve(Eleve eleve) {
        sessionFactory.getCurrentSession().save(eleve);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Eleve> listEleves() {
        return sessionFactory.getCurrentSession().createQuery("from Eleve").list();
    }

    @Override
    public Eleve getEleve(Long id) {
        Eleve eleve = (Eleve) sessionFactory.getCurrentSession().get(Eleve.class, id);
        return eleve;
    }

    @Override
    public void updateEleve(Eleve eleve) {
        sessionFactory.getCurrentSession().update(eleve);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Session> findSessionsAvailableForEleve(Long id) {
        return hibernateTemplate.findByNamedQueryAndNamedParam("Session.getSessionAvailableForEleve", "idUser", id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Session> findSessionByEleve(Long idUser) {
        return sessionFactory.getCurrentSession().createFilter(hibernateTemplate.get(Eleve.class, idUser).getSessions(), "order by this.dateDebut desc").list();
    }
}
