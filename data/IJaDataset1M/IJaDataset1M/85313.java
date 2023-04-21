package org.xmi.repository.driver.perst;

import java.io.File;
import java.util.Properties;
import java.util.Random;
import org.xmi.repository.BasicRepositoryDataSource;
import org.xmi.repository.RepositoryDAO;
import org.xmi.repository.ModelRepositoryDAOImpl;
import org.xmi.repository.TestRepositoryDAO;
import org.xmi.repository.RepositoryDataSource;

public class PerstRepositoryDAOTest extends TestRepositoryDAO {

    private RepositoryDAO dao = null;

    protected void setUp() throws Exception {
        super.setUp();
        String reponame = "target/tmp/test-" + new Random().nextInt();
        new File(reponame).mkdirs();
        RepositoryDataSource source = new BasicRepositoryDataSource();
        source.setPath(reponame);
        source.setRepositoryType("PERST");
        source.setProperties(new Properties());
        dao = new ModelRepositoryDAOImpl(source);
    }

    public RepositoryDAO getRepositoryDAO() {
        return dao;
    }
}
