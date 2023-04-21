package entityDAOs;

import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.hibernate.Query;
import static org.hibernate.criterion.Example.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import entities.Language;

/**
 * A data access object (DAO) providing persistence and search support for
 * Language entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see entities.Language
 * @author MyEclipse Persistence Tools
 */
public class LanguageDAO extends BaseHibernateDAO<Language, java.lang.String> {

    private static final Logger log = LoggerFactory.getLogger(LanguageDAO.class);

    public static final String LANGUAGE = "language";

    public List<Language> findByLanguage(Object language) {
        return findByProperty(LANGUAGE, language);
    }
}
