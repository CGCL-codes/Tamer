package be.vds.jtb.jtbdivelogbook.persistence.xml.dao;

import java.util.List;
import be.vds.jtbdive.core.core.Country;
import be.vds.jtbdive.core.exceptions.DataStoreException;
import be.vds.jtbdive.persistence.core.dao.interfaces.GlossaryDAO;

public class XMLGlossaryDAO implements GlossaryDAO {

    private static XMLGlossaryDAO instance;

    public static XMLGlossaryDAO getInstance() {
        if (instance == null) {
            instance = new XMLGlossaryDAO();
        }
        return instance;
    }

    public List<Country> getCountries() throws DataStoreException {
        return XMLCountryDAO.getInstance().getCountries();
    }

    public void initialize(String basePath) {
    }
}
