package no.ugland.utransprod.dao.hibernate.pkg;

import java.sql.Connection;
import java.sql.SQLException;
import no.ugland.utransprod.ProTransRuntimeException;
import no.ugland.utransprod.dao.pkg.DatabasePkgDAO;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Implementsjon av databasepakke
 * 
 * @author atle.brekka
 * 
 */
public class DatabasePkgDAOHibernate extends JdbcDaoSupport implements DatabasePkgDAO {

    /**
	 * @see no.ugland.utransprod.dao.pkg.DatabasePkgDAO#getDbConnection()
	 */
    public Connection getDbConnection() {
        try {
            return this.getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error("Feil i getDbConnection", e);
            throw new ProTransRuntimeException(e.getMessage());
        }
    }
}
