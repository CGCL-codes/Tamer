package org.springframework.orm.hibernate.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.transaction.TransactionManager;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * Hibernate UserType implementation for Strings that get mapped to CLOBs.
 * Retrieves the LobHandler to use from LocalSessionFactoryBean at config time.
 *
 * <p>Particularly useful for storing Strings with more than 4000 characters in an
 * Oracle database (only possible via CLOBs), in combination with OracleLobHandler.
 *
 * <p>Can also be defined in generic Hibernate mappings, as DefaultLobCreator will
 * work with most JDBC-compliant database drivers. In this case, the field type
 * does not have to be BLOB: For databases like MySQL and MS SQL Server, any
 * large enough binary type will work.
 *
 * @author Juergen Hoeller
 * @since 12.01.2004
 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#setLobHandler
 */
public class ClobStringType extends AbstractLobType {

    /**
	 * Constructor used by Hibernate: fetches config-time LobHandler and
	 * config-time JTA TransactionManager from LocalSessionFactoryBean.
	 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#getConfigTimeLobHandler
	 * @see org.springframework.orm.hibernate.LocalSessionFactoryBean#getConfigTimeTransactionManager
	 */
    public ClobStringType() {
        super();
    }

    /**
	 * Constructor used for testing: takes an explicit LobHandler
	 * and an explicit JTA TransactionManager (can be <code>null</code>).
	 */
    protected ClobStringType(LobHandler lobHandler, TransactionManager jtaTransactionManager) {
        super(lobHandler, jtaTransactionManager);
    }

    public int[] sqlTypes() {
        return new int[] { Types.CLOB };
    }

    public Class returnedClass() {
        return String.class;
    }

    protected Object nullSafeGetInternal(ResultSet rs, int index, LobHandler lobHandler) throws SQLException {
        return lobHandler.getClobAsString(rs, index);
    }

    protected void nullSafeSetInternal(PreparedStatement ps, int index, Object value, LobCreator lobCreator) throws SQLException {
        lobCreator.setClobAsString(ps, index, (String) value);
    }
}
