package net.sourceforge.greenvine.testmodel.data.daos.impl.springhibernate;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import net.sourceforge.greenvine.testmodel.data.daos.UmbrellaDao;
import net.sourceforge.greenvine.testmodel.data.daos.DaoException;
import net.sourceforge.greenvine.testmodel.data.entities.Umbrella;

@Repository
public class UmbrellaDaoImpl extends HibernateDaoSupport implements UmbrellaDao {

    @Override
    public Umbrella loadUmbrella(java.lang.Integer umbrellaId) {
        Umbrella obj = (Umbrella) getHibernateTemplate().get(Umbrella.class, umbrellaId);
        if (obj == null) throw new DaoException("Umbrella with identifier " + umbrellaId + " not found in database.");
        return obj;
    }

    @Override
    public Umbrella findUmbrella(java.lang.Integer umbrellaId) {
        return (Umbrella) getHibernateTemplate().get(Umbrella.class, umbrellaId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Umbrella> findAllUmbrellas() {
        return (List<Umbrella>) getHibernateTemplate().find("from Umbrella as o order by o.id");
    }

    @Override
    public void updateUmbrella(Umbrella umbrella) {
        getHibernateTemplate().saveOrUpdate(umbrella);
    }

    @Override
    public void createUmbrella(Umbrella umbrella) {
        getHibernateTemplate().saveOrUpdate(umbrella);
    }

    @Override
    public void removeUmbrella(java.lang.Integer umbrellaId) {
        Object umbrella = getHibernateTemplate().load(Umbrella.class, umbrellaId);
        getHibernateTemplate().delete(umbrella);
    }
}
