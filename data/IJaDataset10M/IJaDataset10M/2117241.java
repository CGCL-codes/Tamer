package vqwiki.persistence.file;

import vqwiki.Environment;
import vqwiki.persistence.AbstractPersistenceFactory;
import vqwiki.persistence.PersistenceHandler;
import vqwiki.search.SearchEngine;
import vqwiki.svc.ChangeLog;
import vqwiki.svc.Notify;
import vqwiki.svc.VersionManager;
import vqwiki.svc.WikiMembers;

public class FilePersistenceFactory extends AbstractPersistenceFactory {

    public FilePersistenceFactory(Environment environment) throws Exception {
        super(environment);
    }

    public PersistenceHandler createPersistenceHandler() {
        return new FileHandler();
    }

    public ChangeLog createChangeLog() {
        return new FileChangeLog();
    }

    public Notify createNotify(String virtualWiki, String topic) throws Exception {
        return new FileNotify(virtualWiki, topic);
    }

    public SearchEngine createSearchEngine() throws Exception {
        return new FileSearchEngine((FileHandler) super.persistenceHandler);
    }

    public VersionManager createVersionManager() {
        return new FileVersionManager();
    }

    public WikiMembers createWikiMembers(String virtualWiki) throws Exception {
        return new FileWikiMembers(environment, virtualWiki);
    }
}
