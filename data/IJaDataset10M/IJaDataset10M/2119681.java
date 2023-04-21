package dao;

import javax.ejb.Local;
import javax.ejb.Stateless;
import dao.test.DaoFactoryTest;

@Stateless
@Local
public class FactoryAbstractDaoImpl implements FactoryAbstractDao {

    public static DaoFactory getFactory(String cle) {
        if (cle.equals("TEST")) return new DaoFactoryTest(); else return new DaoFactoryJpa();
    }
}
