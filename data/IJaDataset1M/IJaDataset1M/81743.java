package it.uniromadue.portaleuni.test.integration.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTestDao {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for it.uniromadue.portaleuni.dao.test");
        suite.addTestSuite(TestAreeDAO.class);
        suite.addTestSuite(TestAssistenzaDAO.class);
        suite.addTestSuite(TestEsamiDAO.class);
        suite.addTestSuite(TestAuleDAO.class);
        suite.addTestSuite(TestAvvisiDAO.class);
        suite.addTestSuite(TestInsegnamentiDAO.class);
        suite.addTestSuite(TestLezioniDAO.class);
        suite.addTestSuite(TestPeriodicitaCorsiDAO.class);
        suite.addTestSuite(TestDiscussioniDAO.class);
        suite.addTestSuite(TestPostDAO.class);
        suite.addTestSuite(TestPrenotazioneDAO.class);
        suite.addTestSuite(TestUtentiDAO.class);
        return suite;
    }
}
