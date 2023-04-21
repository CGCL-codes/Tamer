package de.psychomatic.mp3db.core.dblayer.utils;

import javax.persistence.EntityManagerFactory;

public interface DBIf {

    public abstract void executeSQL(final String command) throws Exception;

    public abstract void init(final EntityManagerFactory emf);

    public abstract boolean isInit();

    public abstract boolean isInternal();

    public abstract void shutdown();
}
