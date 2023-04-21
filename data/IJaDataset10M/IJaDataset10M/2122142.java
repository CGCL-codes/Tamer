package org.nakedobjects.nos.store.sql.auto;

import org.nakedobjects.nos.store.sql.ObjectMapper;
import org.nakedobjects.nos.store.sql.ObjectMapperFactory;
import org.nakedobjects.nos.store.sql.SqlObjectStoreException;

public class AutoMapperFactory implements ObjectMapperFactory {

    public ObjectMapper createMapper(final String className, final String propertiesBase) throws SqlObjectStoreException {
        return new AutoMapper(className, propertiesBase);
    }
}
