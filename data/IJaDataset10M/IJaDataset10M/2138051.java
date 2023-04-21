package org.hironico.database.driver.factories;

/**
 * Classe de support pour MS SQL SERVER. utilise le driver JTDS version 1.2
 * @author hironico
 * @version $Rev: 1.2 $
 * @since 0.0.8
 */
public class MsSqlServerPooledConnectionFactory extends AbstractJTDSPooledConnectionFactory {

    public MsSqlServerPooledConnectionFactory() {
        super(SERVER_TYPE_SQLSERVER);
        connectionProperties.put("cacheMetaData", "true");
    }
}
