package ru.adv.xml.newt;

import ru.adv.repository.Repository;
import ru.adv.repository.RepositoryException;
import ru.adv.repository.channel.io.UserInfo;
import ru.adv.util.StringParser;
import ru.adv.util.BadBooleanException;
import ru.adv.util.ErrorCodeException;
import ru.adv.http.Session;
import ru.adv.http.InvalidatedSessionException;
import ru.adv.logger.TLogger;
import java.io.Serializable;

/**
 * @version $Revision: 1.13 $
 */
public class Sync extends Newt {

    public static final String LOG_ATTR = "sync-log";

    public static final String EXCEPTION_ATTR = "sync-exception";

    public static final String ACTIVE_FLAG = "sync-active-flag";

    public static final String ACTION = "action";

    public static final String ID = "db";

    public static final String FULL = "full";

    public static final String USER = "user";

    public static final String PASSWORD = "password";

    private TLogger logger = new TLogger(Sync.class);

    public Sync() {
    }

    /**
     * get desctiption for this Processor
     */
    public final String getNewtName() {
        return "newt-sync";
    }

    public void onStartTag() throws NewtException {
        String action = getAction();
        if ("diff".equals(action)) {
            diff();
        } else if ("sync".equals(action)) {
            sync();
        } else if ("create".equals(action)) {
            createdb();
        }
    }

    private void diff() throws NewtException {
        Repository repository = getRepository();
        try {
            repository.prepare(getDatabaseId(), getUser(), false, null);
            appendNode(repository.getDiff(getDatabaseId(), getUser()).toXML(getOwnerDocument()));
        } catch (RepositoryException e) {
            appendXMLObject(e);
        }
    }

    private void sync() throws NewtException {
        try {
            if (getContext().getSession().getAttribute(ACTIVE_FLAG) != null) {
                appendElement("warn", "Another sync session is active");
                return;
            }
            SyncWorker worker = new SyncWorker(getDatabaseId(), getUser(), getRepository(), getContext().getSession(), isFullSync());
            worker.start();
        } catch (InvalidatedSessionException e) {
            throw new NewtException(this, e);
        }
    }

    private void createdb() throws NewtException {
        Repository repository = getRepository();
        try {
            repository.createDB(getDatabaseId(), getUser(), null);
        } catch (RepositoryException e) {
            appendXMLObject(e);
        }
    }

    private boolean isSuccessfulyPrepared(SyncLog log) {
        return log.getPrepareConfigException() == null || ErrorCodeException.OK.equals(log.getPrepareConfigException());
    }

    private UserInfo getUser() {
        return new UserInfo(getQuery().getFirst(USER), getQuery().getFirst(PASSWORD));
    }

    private boolean isFullSync() {
        boolean result = false;
        String s = getQuery().getFirst(FULL);
        if (s != null) {
            try {
                result = StringParser.toBoolean(s);
            } catch (BadBooleanException e) {
                result = false;
            }
        }
        return result;
    }

    private String getAction() {
        String result = getQuery().getFirst(ACTION);
        result = result == null ? "" : result;
        return result;
    }

    private String getDatabaseId() {
        String result = getQuery().getFirst(ID);
        result = result == null ? "" : result;
        return result;
    }

    private Repository getRepository() throws NewtException {
        Repository result = getContext().getRepository();
        if (result == null) {
            throw new NewtException(this, NewtException.REPOZITORY_NOT_INITIALIZED, "Repository not initialized");
        }
        return result;
    }

    private class SyncWorker extends Thread {

        private Repository _repository;

        private Session _session;

        private String _db;

        private boolean _fullSync;

        private UserInfo _user;

        public SyncWorker(String db, UserInfo user, Repository repository, Session session, boolean fullSync) {
            super(SyncWorker.class.getName());
            _repository = repository;
            _session = session;
            _db = db;
            _fullSync = fullSync;
            _user = user;
        }

        public void run() {
            doSync();
        }

        private void doSync() {
            try {
                _session.setAttribute(ACTIVE_FLAG, new Serializable() {
                });
                SyncLog log = createLog();
                _session.removeAttribute(EXCEPTION_ATTR);
                try {
                    _repository.prepare(_db, _user, false, log);
                    if (isSuccessfulyPrepared(log)) {
                        _repository.synchronize(_db, _user, _fullSync, log, null);
                    }
                } catch (RepositoryException e) {
                    log.setFinished(true);
                    _session.setAttribute(EXCEPTION_ATTR, e);
                } finally {
                    log.setFinished(true);
                    _session.removeAttribute(ACTIVE_FLAG);
                }
            } catch (InvalidatedSessionException e) {
                logger.error("Sync session has already invalidated");
            }
        }

        private SyncLog createLog() throws InvalidatedSessionException {
            SyncLog log = new SyncLog();
            _session.setAttribute(LOG_ATTR, log);
            return log;
        }
    }
}
