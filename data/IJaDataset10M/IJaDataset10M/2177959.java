package org.maestroframework.db.interfaces;

import java.sql.Connection;
import java.util.HashMap;
import org.maestroframework.db.ClassMapper;
import org.maestroframework.db.DatabaseTypeMapper;
import org.maestroframework.db.MaestroTransaction;

public abstract class DBDialect {

    private HashMap<ClassMapper, Boolean> daoCache = new HashMap<ClassMapper, Boolean>();

    public abstract Connection beginTransaction() throws Exception;

    public abstract DatabaseTypeMapper getTypeMapper();

    public abstract DBDialectSupport getDialectSupport();

    public abstract <T> MaestroDAO<T> instantiateDAO(ClassMapper<T> classMapper, MaestroTransaction transaction);

    public final <T> MaestroDAO<T> getDAO(ClassMapper<T> classMapper, MaestroTransaction transaction) {
        MaestroDAO<T> dao = this.instantiateDAO(classMapper, transaction);
        if (daoCache.containsKey(classMapper)) {
            if (!classMapper.isReadOnly() && classMapper.isCreateTable()) {
                DBDialectSupport dialectSupport = this.getDialectSupport();
                if (dialectSupport != null && dialectSupport.isCreateTableSupported()) {
                    dialectSupport.createTable(classMapper);
                }
            }
            daoCache.put(classMapper, true);
        }
        return dao;
    }
}
