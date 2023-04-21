package sk.sigp.tetras.dao.impl;

import java.util.List;
import sk.sigp.tetras.dao.UzivatelDao;
import sk.sigp.tetras.dao.general.GenericHibernateDao;
import sk.sigp.tetras.entity.Uzivatel;
import sk.sigp.tetras.entity.enums.Rola;

@SuppressWarnings("unchecked")
public class UzivatelHibernateDao extends GenericHibernateDao<Uzivatel, Long> implements UzivatelDao {

    public Uzivatel findUserByNameAndPassword(String username, String passwordHash) {
        List users = getHibernateTemplate().findByNamedQueryAndNamedParam("findUserByNameAndPwd", new String[] { "name", "hash" }, new Object[] { username, passwordHash });
        if (users.size() < 1) return null;
        return (Uzivatel) users.get(0);
    }

    public List<Uzivatel> getAll() {
        List l = getHibernateTemplate().findByNamedQuery("findAllUzivatel");
        return l;
    }

    public List<Uzivatel> getAllByName() {
        List l = getHibernateTemplate().findByNamedQuery("findAllUzivatelByName");
        return l;
    }

    public List<Uzivatel> findByRole(Rola role) {
        List l = getHibernateTemplate().findByNamedQueryAndNamedParam("findUzivatelByRole", new String[] { "role" }, new Object[] { role });
        return l;
    }
}
