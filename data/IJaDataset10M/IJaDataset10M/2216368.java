package com.cafe.serve.services.usermanagement;

import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import com.cafe.serve.services.GenericBusinessServiceImpl;
import com.cafe.serve.services.usermanagement.intefaces.ZipcodeBusinessService;
import com.spring.hibernate.cashbox.model.dao.cashbox.ZipcodesDao;
import com.spring.hibernate.cashbox.model.obj.cashbox.Zipcodes;

public class ZipcodeBusinessServiceImpl extends GenericBusinessServiceImpl<Zipcodes, Integer, ZipcodesDao> implements ZipcodeBusinessService {

    public ZipcodeBusinessServiceImpl(final ZipcodesDao dao) {
        super(dao);
    }

    public Zipcodes createZipcode(final String zipcode, final String city, final String country) {
        final int id = getSystemTimeIdGenerator().getNextId();
        final Zipcodes zipcodeEntity = new Zipcodes();
        zipcodeEntity.setId(id);
        zipcodeEntity.setZipcode(zipcode);
        zipcodeEntity.setCity(city);
        zipcodeEntity.setCountry(country);
        return zipcodeEntity;
    }

    public void deleteAllZipcodes() {
        final List<Zipcodes> zipcodes = this.findAll();
        for (final Iterator iterator = zipcodes.iterator(); iterator.hasNext(); ) {
            final Zipcodes zipcodes2 = (Zipcodes) iterator.next();
            delete(zipcodes2);
        }
    }

    public boolean existsZipcode(final String zipcode) {
        final Criterion un = Expression.eq("zipcode", zipcode);
        final List<Zipcodes> zipcodes = getDao().findByCriteria(un);
        if (zipcodes != null && !zipcodes.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Zipcodes> findZipcodes(final String zipcode) {
        final String hqlString = "select zc from Zipcodes zc where zc.zipcode=:zipcode";
        final Query query = getQuery(hqlString);
        query.setParameter("zipcode", zipcode);
        final List<Zipcodes> zipcodes = query.list();
        return zipcodes;
    }
}
