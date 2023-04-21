package net.sourceforge.javautil.enterprise.server.jboss;

import net.sourceforge.javautil.common.xml.annotation.XmlTag;

/**
 * A local tx data source.
 *
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface ILocalTxDataSource {

    String getJndiName();

    void setJndiName(String jndiName);

    String getConnectionURL();

    void setConnectionURL(String connectionURL);

    String getDriverClass();

    void setDriverClass(String driverClass);

    String getUserName();

    void setUserName(String username);

    String getPassword();

    void setPassword(String password);

    int getMinSize();

    void setMinSize(int minPoolSize);

    int getMaxSize();

    void setMaxSize(int maxPoolSize);

    long getIdleTimeout();

    void setIdleTimeout(long idleTimeout);

    int getBlockingTimeout();

    void setBlockingTimeout(int idleTimeout);
}
