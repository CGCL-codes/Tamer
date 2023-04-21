package bean.user;

import hibernate.DAO;
import java.util.List;
import java.util.Set;
import bean.person.employee.Employee;
import bean.ossolicitation.OSSolicitation;
import bean.user.User;
import bean.user.AccessLevel;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author w4m
 */
public class UserDAO extends DAO<User> {

    public UserDAO() {
        super(User.class);
    }

    public User load(Session session, String login, String pass) {
        Criteria criteria = session.createCriteria(persistentClass);
        Criterion loginCriterion = Restrictions.like("login", login, MatchMode.EXACT);
        Criterion passCriterion = Restrictions.like("pass", pass, MatchMode.EXACT);
        criteria.add(Restrictions.and(loginCriterion, passCriterion));
        criteria.add(getActiveCriterion());
        return (User) criteria.uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> load(Session session) throws HibernateException {
        Criteria criteria = session.createCriteria(persistentClass);
        criteria.add(getActiveCriterion());
        criteria.add(Restrictions.ne("accessLevel", AccessLevel.ROOT));
        return criteria.list();
    }

    @Override
    public void delete(Session session, User user) {
        processDelete(session, user);
    }

    private void processDelete(Session session, User user) {
        session.update(user);
        Set<OSSolicitation> solicitations = user.getOSSolicitations();
        if (solicitations != null && solicitations.size() > 0) {
            user.setActive(0);
            String login = user.getLogin();
            user.setLogin(login + " (REMOVIDO)");
        } else {
            session.delete(user);
        }
    }
}
