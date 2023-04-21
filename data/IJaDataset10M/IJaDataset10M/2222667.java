package db.dao;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import util.CriteriaHelper;
import util.HiberSession;
import util.MD5generator;
import db.Klient;
import db.base.BaseKlientDAO;

public class KlientDAO extends BaseKlientDAO {

    /**
	 * Default constructor.  Can be used in place of getInstance()
	 */
    public KlientDAO() {
    }

    public Klient findKlientByLoginAndPassword(String login, String password) {
        Session s = HiberSession.getInstance().getSession();
        Criteria c = s.createCriteria(Klient.class);
        CriteriaHelper.eq(c, Klient.PROP_LOGIN, login);
        if (password != null) {
            try {
                password = MD5generator.MD5(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CriteriaHelper.eq(c, Klient.PROP_HASLO, password);
        }
        if (c.list().size() == 1) return (Klient) c.uniqueResult(); else return null;
    }

    public Klient findKlientByLogin(String login) {
        Session s = HiberSession.getInstance().getSession();
        Criteria c = s.createCriteria(Klient.class);
        CriteriaHelper.eq(c, Klient.PROP_LOGIN, login);
        if (c.list().size() == 1) return (Klient) c.uniqueResult(); else return null;
    }

    public Klient findKlientById(Integer id) {
        Session s = HiberSession.getInstance().getSession();
        Criteria c = s.createCriteria(Klient.class);
        CriteriaHelper.eq(c, Klient.PROP_ID, id);
        if (c.list().size() == 1) return (Klient) c.uniqueResult(); else return null;
    }
}
