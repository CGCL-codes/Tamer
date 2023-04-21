package de.searchworkorange.lib.database;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public interface IDatabaseConfiguration {

    public String getDbName();

    public String getDbPass();

    public int getDbPort();

    public String getDbServer();

    public String getDbType();

    public String getDbUser();

    public StringBuffer toStringBuffer();

    @Override
    public String toString();
}
