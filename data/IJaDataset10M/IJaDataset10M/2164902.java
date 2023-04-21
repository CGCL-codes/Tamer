package be.vds.jtb.jtbtaskplanner.persistence.xml.dao;

import be.vds.jtbtaskplanner.persistence.core.dao.interfaces.GlossaryDAO;

public class XMLGlossaryDAO implements GlossaryDAO {

    private static XMLGlossaryDAO instance;

    public static XMLGlossaryDAO getInstance() {
        if (instance == null) {
            instance = new XMLGlossaryDAO();
        }
        return instance;
    }

    public void initialize(String basePath) {
    }
}
