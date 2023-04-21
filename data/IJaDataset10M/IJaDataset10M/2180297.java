package edu.cebanc.spring.biblioteca.dao;

import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import edu.cebanc.spring.biblioteca.domain.Language;

@Repository("languageDAO")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LanguagesDAOimpl implements LanguagesDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveLanguage(Language lang) {
        sessionFactory.getCurrentSession().saveOrUpdate(lang);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Language> listLanguages() {
        return (List<Language>) sessionFactory.getCurrentSession().createCriteria(Language.class).list();
    }

    @Override
    public Language getLanguage(int id) {
        return (Language) sessionFactory.getCurrentSession().load(Language.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delLanguage(Language lang) {
        sessionFactory.getCurrentSession().delete(lang);
    }
}
