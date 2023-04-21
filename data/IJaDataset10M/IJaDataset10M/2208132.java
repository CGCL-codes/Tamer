package net.sourceforge.omov.logic.model;

import net.sourceforge.omov.core.imodel.IDataVersionDao;
import net.sourceforge.omov.logic.AbstractTestCase;
import net.sourceforge.omov.logic.model.db4o.Db4oConnection;
import net.sourceforge.omov.logic.model.db4o.Db4oDataVersionDao;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oDataVersionDaoTest extends AbstractTestCase {

    private static final IDataVersionDao DAO = new Db4oDataVersionDao(new Db4oConnection("Db4oDataVersionDaoTest.yap"));

    public void testInitialStore() {
        final int expectedMovieVersion = 1;
        final int expectedSmartFolderVersion = 1;
        DAO.storeDataVersions(expectedMovieVersion, expectedSmartFolderVersion);
        assertEquals(expectedMovieVersion, DAO.getMovieDataVersion());
        assertEquals(expectedSmartFolderVersion, DAO.getSmartfolderDataVersion());
    }

    public void testMultipleStoring() {
        final int expectedVersion = 3;
        DAO.storeDataVersions(expectedVersion, expectedVersion);
        DAO.storeDataVersions(expectedVersion, expectedVersion);
        DAO.storeDataVersions(expectedVersion, expectedVersion);
        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
    }
}
