package ru.adv.repository;

import java.io.File;
import ru.adv.event.Interactive;
import ru.adv.event.QuestionEvent;
import ru.adv.event.QuestionListener;
import ru.adv.event.Response;
import ru.adv.logger.TLogger;
import ru.adv.repository.channel.client.RepositoryClient;
import ru.adv.repository.channel.io.ChannelException;
import ru.adv.repository.channel.io.RemoteEventHandler;
import ru.adv.repository.channel.io.UserInfo;

/**
 * User: roma Date: 11.02.2005 Time: 16:14:10
 */
public class RemoteSyncHandler implements RemoteEventHandler, QuestionListener {

    private UserInfo _remoteUser;

    private QuestionEvent _event;

    private DBCreateProcess _process;

    private Thread _restoreThread;

    private boolean _isClosed;

    private Exception _raiseException;

    private Response _response;

    private ReplicaConfig _config;

    private boolean _full;

    private File _tmpDir;

    private File _preparedConfig;

    private TLogger logger = new TLogger(RemoteSyncHandler.class);

    public RemoteSyncHandler(ReplicaConfig config, Boolean full, UserInfo remoteUser, File preparedConfig, File tmpDir) {
        _config = config;
        _remoteUser = remoteUser;
        _full = full == null ? false : full.booleanValue();
        _tmpDir = tmpDir;
        _preparedConfig = preparedConfig;
    }

    public void destroy() {
    }

    public synchronized QuestionEvent getEvent() throws ChannelException {
        QuestionEvent tmpEvent = _event;
        if (_event != null && !_event.isQuestion()) {
            setResponse(new Response(true));
        }
        logger.debug("RemoteSyncHandler.getEvent(): tmpEvent=" + tmpEvent);
        return tmpEvent;
    }

    /**
     * Начинает процесс синхронизации и сообщает есть ли чего для
     * {@link #getEvent}
     * 
     * @return
     * @throws ChannelException
     */
    public synchronized boolean hasEvent() throws ChannelException {
        boolean result = true;
        if (_process == null) {
            _process = new DBCreateProcess(this);
            _restoreThread = new Thread(_process, this.getClass().getName());
            _restoreThread.start();
        }
        if (!_restoreThread.isAlive() || _isClosed) {
            result = false;
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
                result = false;
            }
        }
        logger.debug("RemoteSyncHandler.hasEvent(): result=" + result);
        return result;
    }

    public synchronized void setResponse(Response response) throws ChannelException {
        _response = response;
        logger.debug("RemoteSyncHandler.setResponse(): _response=" + _response);
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

    protected RepositoryClient openChannel() throws ChannelException {
        return new RepositoryClient(_config.getHost(), _config.getPort(), _config.getRemoteDBName(), _remoteUser);
    }

    /**
     * обработка поступившего сообщения
     */
    public synchronized Response questionPerformed(QuestionEvent e) {
        _event = e;
        logger.debug("RemoteSyncHandler.questionPerformed(): _event=" + _event);
        notify();
        try {
            wait();
        } catch (InterruptedException eeee) {
            logger.warning("InterruptedException=" + eeee);
        }
        logger.debug("RemoteSyncHandler.questionPerformed(): _response=" + _response);
        return _response;
    }

    /**
     * 
     */
    private class DBCreateProcess implements Runnable {

        private RemoteSyncHandler _handler;

        public DBCreateProcess(RemoteSyncHandler handler) {
            _handler = handler;
        }

        public void run() {
            Interactive inter = new Interactive();
            inter.setQuestionListener(_handler);
            try {
                RepositoryClient channel = openChannel();
                try {
                    String fileId = channel.uploadFile(_preparedConfig, _tmpDir, inter);
                    logger.debug("fileId=" + fileId);
                    channel.copyToBasePrepared(fileId);
                    channel.synchronizeDBSchema(_config.getRemoteDBName(), _remoteUser, _full);
                    conversationClosed();
                } finally {
                    channel.close();
                }
            } catch (Exception e) {
                conversationClosed(e);
            }
        }
    }
}
