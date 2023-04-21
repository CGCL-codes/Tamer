package org.jraptor.petclinic;

import java.io.IOException;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.jraptor.petclinic.model.Vet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class uses DBUnit to load a set of initial data to an empty database. It distinguish between different JPA
 * vendors and load different data sets accordingly.
 * 
 * @author <a href="mailto:goran.oberg@jraptor.org">Goran Oberg</a>
 * @version $Rev: 136 $ $Date: 2008-12-17 01:22:58 -0500 (Wed, 17 Dec 2008) $
 */
@Transactional
public class DataLoader {

    private static String hibernateDataSet = "/hibernate-data.xml";

    private static String topLinkDataSet = "/toplink-data.xml";

    private static String openJpaDataSet = "/openjpa-data.xml";

    private static String eclipseLinkDataSet = "/eclipselink-data.xml";

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected DataSource ds;

    @PostConstruct
    protected void init() throws IOException, SQLException, DatabaseUnitException {
        if (em.find(Vet.class, 1) == null) {
            IDatabaseConnection connection = new DatabaseConnection(ds.getConnection());
            DatabaseConfig databaseConfig = connection.getConfig();
            databaseConfig.setProperty(DatabaseConfig.PROPERTY_PRIMARY_KEY_FILTER, new PrimaryKeyFilter());
            IDataSet dataSet = null;
            String delegateName = em.getDelegate().getClass().getName();
            if (delegateName.equals("org.hibernate.impl.SessionImpl")) {
                dataSet = new FlatXmlDataSet(getClass().getResource(hibernateDataSet));
            } else if (delegateName.equals("oracle.toplink.essentials.internal.ejb.cmp3.EntityManagerImpl")) {
                dataSet = new FlatXmlDataSet(getClass().getResource(topLinkDataSet));
            } else if (delegateName.equals("org.apache.openjpa.persistence.EntityManagerImpl")) {
                dataSet = new FlatXmlDataSet(getClass().getResource(openJpaDataSet));
            } else if (delegateName.equals("org.eclipse.persistence.internal.jpa.EntityManagerImpl")) {
                dataSet = new FlatXmlDataSet(getClass().getResource(eclipseLinkDataSet));
            }
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        }
    }

    protected class PrimaryKeyFilter implements IColumnFilter {

        public boolean accept(String tableName, Column column) {
            return true;
        }
    }
}
