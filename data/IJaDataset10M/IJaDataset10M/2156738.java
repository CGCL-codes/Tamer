package org.databene.jdbacl.identity;

import java.sql.Connection;

/**
 * Parent for classes that map between primary key and natural keys 
 * of table rows in different tables in one or more source databases and one target database.<br/><br/>
 * Created: 23.08.2010 16:48:21
 * @since 0.6.4
 * @author Volker Bergmann
 */
public abstract class KeyMapper {

    IdentityProvider identityProvider;

    public KeyMapper(IdentityProvider identityProvider) {
        this.identityProvider = identityProvider;
    }

    public IdentityProvider getIdentityProvider() {
        return identityProvider;
    }

    public abstract void registerSource(String dbId, Connection connection);

    public abstract void store(String sourceDbId, IdentityModel identity, String naturalKey, Object sourcePK, Object targetPK);

    public abstract Object getTargetPK(String sourceDbId, IdentityModel identity, Object sourcePK);

    public abstract Object getTargetPK(IdentityModel identity, String naturalKey);

    public abstract String getNaturalKey(String dbId, IdentityModel identity, Object sourcePK);
}
