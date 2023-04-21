package com.ohua.clustering.checkpoint.online;

import java.util.concurrent.atomic.AtomicBoolean;

public class DistributedOnlineCheckpointInitiator implements Runnable {

    private AtomicBoolean _stop = new AtomicBoolean(false);

    private long _checkpointInterval = 200;

    private int _cpsToInject = 0;

    private int _cpsInjected = 0;

    private SimpleCPMasterDaemon _master = null;

    public DistributedOnlineCheckpointInitiator(SimpleCPMasterDaemon master) {
        _master = master;
    }

    public void run() {
        try {
            Thread.sleep(_checkpointInterval);
            while (!_stop.get() && _cpsToInject > _cpsInjected) {
                _master.checkpointRequest();
                _cpsInjected++;
                Thread.sleep(_checkpointInterval);
            }
        } catch (InterruptedException e) {
        }
    }

    public void stop() {
        _stop.set(true);
    }

    public long getCheckpointInterval() {
        return _checkpointInterval;
    }

    public void setCheckpointInterval(long interval) {
        _checkpointInterval = interval;
    }

    public int getCpsToInject() {
        return _cpsToInject;
    }

    public void setCpsToInject(int toInject) {
        _cpsToInject = toInject;
    }

    public int getCpsInjected() {
        return _cpsInjected;
    }

    public void set_cpsInjected(int injected) {
        _cpsInjected = injected;
    }
}
