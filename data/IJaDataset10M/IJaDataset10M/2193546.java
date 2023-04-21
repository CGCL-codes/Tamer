package vqwiki.persistence;

import vqwiki.Environment;
import vqwiki.search.SearchEngine;
import vqwiki.svc.ChangeLog;
import vqwiki.svc.Notify;
import vqwiki.svc.VersionManager;
import vqwiki.svc.WikiMembers;

public abstract class AbstractPersistenceFactory {

    protected Environment environment;

    protected PersistenceHandler persistenceHandler;

    public AbstractPersistenceFactory(Environment environment) throws Exception {
        this.environment = environment;
        this.persistenceHandler = createPersistenceHandler();
    }

    public abstract PersistenceHandler createPersistenceHandler() throws Exception;

    public PersistenceHandler getPersistenceHandler() {
        return persistenceHandler;
    }

    public abstract ChangeLog createChangeLog();

    public abstract Notify createNotify(String virtualWiki, String topic) throws Exception;

    public abstract SearchEngine createSearchEngine() throws Exception;

    public abstract VersionManager createVersionManager();

    public abstract WikiMembers createWikiMembers(String virtualWiki) throws Exception;
}
