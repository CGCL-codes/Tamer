package hibernate.service;

import hibernate.fileControler.MyException;
import hibernate.model.Bellekler;
import hibernate.model.Buzdolabi;
import hibernate.model.Klima;
import hibernate.model.Mp3ipod;
import hibernate.model.Resimler;
import hibernate.model.Telefon;
import hibernate.model.Tv;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("modelServices")
@Transactional
public class ModelServicesImpl implements EmmyService {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<?> findAll(Object object) {
        return sessionFactory.getCurrentSession().createQuery("from " + object.getClass().getSimpleName() + "").list();
    }

    @Override
    public Object findById(Object object, Long id) {
        return sessionFactory.getCurrentSession().get(object.getClass(), id);
    }

    @Override
    public boolean saveOrUpdate(Object object) throws MyException {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
            return true;
        } catch (Exception e) {
            System.out.println("Save error...\n");
            System.out.println("Excp : " + e);
            throw new MyException("save error" + e);
        }
    }

    @Override
    public boolean delete(Object object) {
        try {
            sessionFactory.getCurrentSession().delete(object);
        } catch (Exception e) {
            System.out.println("Delete error...\n");
            System.out.println("Excp : " + e);
            return false;
        }
        return true;
    }

    @Override
    public List<?> getQuery(String queryName, String propertyName, String Value) {
        return sessionFactory.getCurrentSession().getNamedQuery(queryName).setParameter(propertyName, Value).list();
    }

    @Override
    public List<?> getQuery(String queryName, String propertyName, Long Value) {
        return sessionFactory.getCurrentSession().getNamedQuery(queryName).setParameter(propertyName, Value).list();
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<?> getQuery(String query) {
        return sessionFactory.getCurrentSession().find(query);
    }

    @Override
    public ArrayList<String> getCurrentAygitResList(Object object) {
        ArrayList<String> resList = new ArrayList<String>();
        Set<Resimler> resimList = null;
        if (object.getClass().getSimpleName().equals("Bellekler")) {
            resimList = ((Bellekler) object).getAygit().getResimlers();
        } else if (object.getClass().getSimpleName().equals("Tv")) {
            resimList = ((Tv) object).getAygit().getResimlers();
        } else if (object.getClass().getSimpleName().equals("Buzdolabi")) {
            resimList = ((Buzdolabi) object).getAygit().getResimlers();
        } else if (object.getClass().getSimpleName().equals("Klima")) {
            resimList = ((Klima) object).getAygit().getResimlers();
        } else if (object.getClass().getSimpleName().equals("Mp3ipod")) {
            resimList = ((Mp3ipod) object).getAygit().getResimlers();
        } else if (object.getClass().getSimpleName().equals("Telefon")) {
            resimList = ((Telefon) object).getAygit().getResimlers();
        }
        for (Iterator<?> iter = resimList.iterator(); iter.hasNext(); ) {
            Resimler res = (Resimler) iter.next();
            resList.add(res.getResimKonum());
        }
        return resList;
    }

    @Override
    public Object getBean(Object object) {
        return FacesContext.getCurrentInstance().getELContext().getELResolver().getValue(FacesContext.getCurrentInstance().getELContext(), null, object.getClass().getSimpleName());
    }
}
