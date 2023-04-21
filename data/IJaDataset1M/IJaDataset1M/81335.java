package managedbeans;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

public class outocomplete {

    public List<String> autoComplete(Object o) {
        String name = (String) o;
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        Query query = null;
        try {
            query = session.createQuery("select ing.ingredientName from  Ingredients ing where " + "ing.ingredientName LIKE '" + name + "%'");
            System.out.println(query.list().size() + "--------------------------------------------------------------------------------------------------------------------------------------------------------------");
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
