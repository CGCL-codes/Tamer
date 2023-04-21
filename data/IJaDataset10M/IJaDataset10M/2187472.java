package pl.edu.agh.ssm.persistence.dao.impl;

import java.sql.SQLException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import pl.edu.agh.ssm.beans.User;
import pl.edu.agh.ssm.persistence.IUser;
import pl.edu.agh.ssm.persistence.dao.UserAccessDAO;

public class UserAccessDAOImpl extends GenericHibernateDao<IUser> implements UserAccessDAO {

    public void create(String userName) {
        IUser user = getUser();
        user.setLogin(userName);
        create(user);
    }

    public IUser getUser() {
        return new User();
    }

    public IUser getUser(final String userLogin) {
        return (IUser) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(final Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(getEntityClass());
                criteria.add(Restrictions.eq("login", userLogin));
                System.out.println(">>>>>>" + criteria.uniqueResult());
                return criteria.uniqueResult();
            }
        });
    }

    public boolean checkUser(String userName, String password) {
        IUser user = getUser(userName);
        if (user == null) return false; else return user.getPassword().equals(password);
    }

    public IUser create(String nick, String login, String name, String password, String surname, String phone, String email, Integer gg, String skype) {
        IUser user = getUser();
        user.setNick(nick);
        user.setLogin(login);
        user.setName(surname);
        user.setPassword(password);
        user.setSurname(surname);
        user.setPhone(phone);
        user.setEmail(email);
        user.setGg(gg);
        user.setSkype(skype);
        user.setRole(0);
        create(user);
        return user;
    }
}
