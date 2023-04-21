package com.ixora.rms.logging;

import java.util.LinkedList;
import java.util.List;
import com.ixora.common.logging.AppLogger;
import com.ixora.common.logging.AppLoggerFactory;
import com.ixora.rms.EntityDescriptor;
import com.ixora.rms.HostId;
import com.ixora.rms.agents.AgentDescriptor;
import com.ixora.rms.agents.AgentId;
import com.ixora.rms.logging.exception.DataLogException;
import com.ixora.rms.logging.exception.NoLogWasLoaded;
import com.ixora.rms.services.DataLogScanningService;

/**
 * @author Daniel Moraru
 */
public final class DataLogScanning implements DataLogScanningService {

    /** Logger */
    private static final AppLogger logger = AppLoggerFactory.getLogger(DataLogScanning.class);

    /** Data log reader */
    private DataLogReader fReader;

    /** Scanning flag */
    private boolean fScanning;

    /** Listeners */
    private List<ScanListener> fScanListeners;

    /** Event handler */
    private EventHandler fEventHandler;

    /** Log */
    private LogRepositoryInfo fLog;

    /**
	 * Event handler.
	 */
    private final class EventHandler implements DataLogReader.ScanCallback {

        /**
		 * @see com.ixora.rms.logging.DataLogReader.ScanCallback#handleEntity(com.ixora.rms.logging.DataLogReader, com.ixora.rms.HostId, com.ixora.rms.agents.AgentId, com.ixora.rms.EntityDescriptor)
		 */
        public void handleEntity(DataLogReader source, HostId host, AgentId aid, EntityDescriptor ed) {
            DataLogScanning.this.fireNewEntity(fLog, host, aid, ed);
        }

        /**
         * @see com.ixora.rms.logging.DataLogReader.ScanCallback#handleAgent(com.ixora.rms.logging.DataLogReader, com.ixora.rms.HostId, com.ixora.rms.agents.AgentDescriptor)
         */
        public void handleAgent(DataLogReader source, HostId host, AgentDescriptor ad) {
            DataLogScanning.this.fireNewAgent(fLog, host, ad);
        }

        /**
		 * @see com.ixora.rms.logging.DataLogReader.ScanCallback#finishedScanning(com.ixora.rms.logging.DataLogReader, com.ixora.rms.logging.BoundedTimeInterval)
		 */
        public void finishedScanning(DataLogReader source, BoundedTimeInterval ti) {
            DataLogScanning.this.handleFinishedScanning(fLog, ti);
        }

        /**
		 * @see com.ixora.rms.logging.DataLogReader.ScanCallback#handleScanFatalError(com.ixora.rms.logging.DataLogReader, java.lang.Exception)
		 */
        public void handleScanFatalError(DataLogReader source, Exception e) {
            DataLogScanning.this.fireFatalScanError(fLog, e);
        }
    }

    public DataLogScanning() {
        super();
        this.fEventHandler = new EventHandler();
        this.fScanListeners = new LinkedList<ScanListener>();
    }

    /**
	 * @see com.ixora.rms.services.DataLogReplayService#stopScanning()
	 */
    public synchronized void stopScanning() throws DataLogException {
        if (!fScanning) {
            return;
        }
        try {
            if (this.fReader == null) {
                throw new NoLogWasLoaded();
            }
            closeReader();
        } finally {
            fScanning = false;
        }
    }

    /**
	 * @throws DataLogException
	 */
    private void closeReader() throws DataLogException {
        if (fReader != null) {
            fReader.close();
        }
    }

    /**
	 * @see com.ixora.common.Service#shutdown()
	 */
    public synchronized void shutdown() {
        try {
            closeReader();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Fires end of log event.
	 * @param logRepositoryInfo 
     * @param beginTimestamp
     * @param endTimestamp
     */
    private void fireFinishedScanning(LogRepositoryInfo logRepositoryInfo, BoundedTimeInterval ti) {
        synchronized (fScanListeners) {
            for (ScanListener listener : fScanListeners) {
                try {
                    listener.finishedScanningLog(logRepositoryInfo, ti);
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
    }

    /**
     * Fires fatal error.
	 * @param logRepositoryInfo 
     * @param e
     */
    private void fireFatalScanError(LogRepositoryInfo logRepositoryInfo, Exception e) {
        synchronized (this) {
            fScanning = false;
        }
        synchronized (fScanListeners) {
            for (ScanListener listener : fScanListeners) {
                try {
                    listener.fatalScanError(logRepositoryInfo, e);
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }

    /**
     * Fires new entity.
	 * @param logRepositoryInfo 
     * @param e
     */
    private void fireNewEntity(LogRepositoryInfo logRepositoryInfo, HostId hid, AgentId aid, EntityDescriptor entity) {
        synchronized (fScanListeners) {
            for (ScanListener listener : fScanListeners) {
                try {
                    listener.newEntity(logRepositoryInfo, hid, aid, entity);
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }

    /**
     * @param logRepositoryInfo 
     * @param host
     * @param ad
     */
    private void fireNewAgent(LogRepositoryInfo logRepositoryInfo, HostId host, AgentDescriptor ad) {
        synchronized (fScanListeners) {
            for (ScanListener listener : fScanListeners) {
                try {
                    listener.newAgent(logRepositoryInfo, host, ad);
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }

    /**
	 * @see com.ixora.rms.services.DataLogReplayService#addScanListener(com.ixora.rms.services.DataLogReplayService.ScanListener)
	 */
    public void addScanListener(ScanListener l) {
        synchronized (fScanListeners) {
            if (!fScanListeners.contains(l)) {
                fScanListeners.add(l);
            }
        }
    }

    /**
	 * @see com.ixora.rms.services.DataLogReplayService#removeScanListener(com.ixora.rms.services.DataLogReplayService.ScanListener)
	 */
    public void removeScanListener(ScanListener l) {
        synchronized (fScanListeners) {
            fScanListeners.remove(l);
        }
    }

    /**
	 * @param logRepositoryInfo 
	 * @param ti
	 */
    private void handleFinishedScanning(LogRepositoryInfo logRepositoryInfo, BoundedTimeInterval ti) {
        synchronized (this) {
            fScanning = false;
        }
        fireFinishedScanning(logRepositoryInfo, ti);
    }

    /**
	 * @see com.ixora.rms.services.DataLogScanningService#startScanning(com.ixora.rms.logging.LogRepositoryInfo)
	 */
    public synchronized void startScanning(LogRepositoryInfo log) throws DataLogException {
        if (fScanning) {
            return;
        }
        fScanning = true;
        fLog = log;
        fReader = DataLogUtils.createReader(log);
        fReader.scan(fEventHandler);
    }
}
