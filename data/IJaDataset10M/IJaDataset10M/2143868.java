package com.intrigueit.myc2i.zipcode.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intrigueit.myc2i.zipcode.dao.ZipCodeDao;
import com.intrigueit.myc2i.zipcode.domain.ZipCode;

@Service
public class ZipCodeServiceImpl implements ZipCodeService {

    private ZipCodeDao zipCodeDao;

    /**
	 * @param zipCodeDao
	 */
    @Autowired
    public ZipCodeServiceImpl(ZipCodeDao zipCodeDao) {
        this.zipCodeDao = zipCodeDao;
    }

    public List<ZipCode> findAll() {
        return this.zipCodeDao.loadAll();
    }

    public List<ZipCode> findByCity(String city) {
        String clause = " upper(t.city) = ?1 ";
        return zipCodeDao.loadByClause(clause, new Object[] { city });
    }

    public ZipCode findById(String id) {
        return this.zipCodeDao.loadById(id);
    }

    public List<ZipCode> findByState(String state) {
        String clause = " upper(t.state) = ?1 ";
        return zipCodeDao.loadByClause(clause, new Object[] { state });
    }
}
