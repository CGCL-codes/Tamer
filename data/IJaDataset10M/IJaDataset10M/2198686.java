package org.bitz.dao;

import org.bitz.base.BaseCountryDAO;
import org.hibernate.Session;

public class CountryDAO extends BaseCountryDAO implements org.bitz.dao.iface.CountryDAO {

    public CountryDAO() {
        super.initialize();
    }

    public CountryDAO(Session session) {
        super(session);
    }
}
