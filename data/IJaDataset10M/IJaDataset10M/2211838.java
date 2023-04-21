package net.pms.external;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import Spotify.AudioFormat;
import Spotify.Session;

public class SpotifySession extends Session implements Runnable {

    private SpotifyInputStream m_listener;

    private Thread m_workerThread;

    private boolean m_isShuttingDown = false;

    private Lock m_lock = new ReentrantLock();

    private Condition m_taskComplete = m_lock.newCondition();

    private volatile WorkerTask m_task;

    private Object m_executeLock = new Object();

    private Object m_notifyMainThreadLock = new Object();

    private volatile boolean m_isUpdateRequired = false;

    private volatile boolean m_isConnected = false;

    public void run() {
        while (!m_isShuttingDown) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isUpdateRequired = false;
            synchronized (m_notifyMainThreadLock) {
                if (m_isUpdateRequired) {
                    isUpdateRequired = true;
                    m_isUpdateRequired = false;
                }
            }
            if (isUpdateRequired) {
                Update();
            }
            m_lock.lock();
            if (m_task != null) {
                m_task.Run();
                m_taskComplete.signalAll();
            }
            m_lock.unlock();
        }
    }

    public void Execute(String name, WorkerTask task) {
        if (m_workerThread == Thread.currentThread()) {
            task.Run();
        } else {
            synchronized (m_executeLock) {
                m_lock.lock();
                m_task = task;
                try {
                    m_taskComplete.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                m_task = null;
                m_lock.unlock();
            }
        }
    }

    public void Initialise() {
        m_workerThread = new Thread(this);
        m_workerThread.start();
    }

    public void Shutdown() {
        m_isShuttingDown = true;
        try {
            m_workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        m_workerThread = null;
    }

    public void SetListener(SpotifyInputStream listener) {
        if ((m_listener != null) && (m_listener != listener)) {
            m_listener.OnPlaybackEndOfTrack();
            m_listener = null;
        }
        m_listener = listener;
    }

    public void ClearListener(SpotifyInputStream listener) {
        if (m_listener == listener) {
            m_listener = null;
        }
    }

    public int OnMusicDelivery(AudioFormat format, byte[] frames, int numFrames) {
        if (m_listener != null) {
            return m_listener.OnPlaybackMusicDelivery(format, frames, numFrames);
        } else {
            return numFrames;
        }
    }

    public void OnPlayTokenLost() {
        if (m_listener != null) {
            m_listener.OnPlaybackError();
        }
    }

    public void OnLogMessage(String message) {
    }

    public void OnEndOfTrack() {
        if (m_listener != null) {
            m_listener.OnPlaybackEndOfTrack();
        }
    }

    public void OnStreamingError(int error) {
        if (m_listener != null) {
            m_listener.OnPlaybackError();
        }
    }

    public void OnNotifyMainThread() {
        synchronized (m_notifyMainThreadLock) {
            m_isUpdateRequired = true;
        }
    }

    public SpotifyInputStream GetListener() {
        return m_listener;
    }

    public boolean IsConnected() {
        return m_isConnected;
    }

    public void OnLoggedIn(int error) {
        m_isConnected = true;
    }

    public void OnLoggedOut() {
        m_isConnected = false;
    }
}
