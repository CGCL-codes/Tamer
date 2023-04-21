package ru.adv.repository;

import ru.adv.repository.channel.io.RemoteEventHandler;
import ru.adv.repository.channel.io.UserInfo;
import ru.adv.repository.channel.io.ChannelException;
import ru.adv.event.QuestionEvent;
import ru.adv.event.Response;
import ru.adv.event.Interactive;
import ru.adv.event.QuestionListener;
import ru.adv.logger.TLogger;

/**
 * User: roma
 * Date: 11.02.2005
 * Time: 16:14:10
 */
public class SQLHandler implements RemoteEventHandler, QuestionListener {

    private TLogger logger = new TLogger(SQLHandler.class);

    private RepositoryAdmin _r;

    private String _db;

    private String _sql;

    private UserInfo _remoteUser;

    private QuestionEvent _event;

    private RestoreProcess _restoreProcess;

    private Thread _restoreThread;

    private boolean _isClosed;

    private Exception _raiseException;

    private Response _response;

    public SQLHandler(RepositoryAdmin r, String db, String sql, UserInfo remoteUser) {
        _r = r;
        _db = db;
        _sql = sql;
        _remoteUser = remoteUser;
    }

    public void destroy() {
    }

    public synchronized QuestionEvent getEvent() throws ChannelException {
        QuestionEvent tmpEvent = _event;
        if (_event != null && !_event.isQuestion()) {
            setResponse(new Response(true));
        }
        return tmpEvent;
    }

    /**
     * Начинает процесс синхронизации и сообщает есть ли чего для {@link #getEvent}
     *
     * @return
     * @throws ChannelException
     */
    public synchronized boolean hasEvent() throws ChannelException {
        boolean has = true;
        if (_restoreProcess == null) {
            _restoreProcess = new RestoreProcess(this);
            _restoreThread = new Thread(_restoreProcess, this.getClass().getName());
            _restoreThread.start();
        }
        if (!_restoreThread.isAlive() || _isClosed) {
            has = false;
        } else {
            if (_event == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.warning(e);
                }
            }
            if (_raiseException != null) {
                throw new ChannelException(_raiseException.getMessage());
            }
            if (!_restoreThread.isAlive() || _isClosed) {
                has = false;
            }
        }
        return has;
    }

    public synchronized void setResponse(Response response) throws ChannelException {
        _response = response;
        _event = null;
        notify();
    }

    /**
     * Больше сообщений не будет
     */
    public synchronized void conversationClosed() {
        _isClosed = true;
        notify();
    }

    /**
     * Больше сообщений не будет, так ка произошла критическая ошибка вызванная
     * переданным <code>Exception</code>
     */
    public synchronized void conversationClosed(Exception e) {
        _raiseException = e;
        conversationClosed();
    }

    /**
     * обработка поступившего сообщения
     */
    public synchronized Response questionPerformed(QuestionEvent e) {
        _event = e;
        notify();
        try {
            wait();
        } catch (InterruptedException eeee) {
            logger.warning("InterruptedException=" + eeee);
        }
        return _response;
    }

    /**
     *
     */
    private class RestoreProcess implements Runnable {

        private SQLHandler _handler;

        public RestoreProcess(SQLHandler handler) {
            _handler = handler;
        }

        public void run() {
            Interactive inter = new Interactive();
            inter.setQuestionListener(_handler);
            try {
                _r.executeSQL(_db, _sql, inter);
                conversationClosed();
            } catch (Exception e) {
                conversationClosed(e);
            }
        }
    }
}
