package org.fao.waicent.kids.giews.communication.providermodule.uploadmodule;

import java.util.Vector;
import org.fao.waicent.kids.giews.communication.utility.DownloadInfo;
import org.fao.waicent.kids.giews.communication.utility.GIEWSException;
import org.fao.waicent.kids.giews.communication.utility.MyDebug;

/**
 * <p>Title: PoooledUpload</p>
 *
 *
 * @author A. Tamburo
 * @version 1
 * @since 1
*/
public class PooledUpload {

    private Vector pool;

    private UploadThread[] dThread;

    private int MAX_UPLOAD = 2;

    private int poolLength = 0;

    private int bandwidth;

    public static byte RUNNABLE = 0;

    public static byte WAITING = 1;

    public int speed[];

    public int bandwidthResidue;

    private int pool_length = 30;

    private MyDebug debug;

    /**
       * PooledDownload 
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
       */
    public PooledUpload(int maxDownload, int uploadQueueLength, int bandwidth, MyDebug debug) {
        int s;
        if (bandwidth > 0) this.bandwidth = (int) ((bandwidth * 1024) / 8); else this.bandwidth = (10 * 1024) / 8;
        if (maxDownload <= 0) {
            s = MAX_UPLOAD;
        } else s = maxDownload;
        dThread = new UploadThread[s];
        this.speed = new int[s];
        if (uploadQueueLength > 0) pool_length = uploadQueueLength;
        this.bandwidthResidue = bandwidth;
        pool = new Vector();
        for (int i = 0; i < s; i++) {
            dThread[i] = new UploadThread(pool, i, this, debug);
            dThread[i].start();
        }
        this.debug = debug;
    }

    /**
       * addUpload
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
      */
    public boolean addUpload(DownloadInfo newdi) {
        synchronized (pool) {
            if (pool.size() < pool_length) {
                pool.add(newdi);
                pool.notifyAll();
            } else {
                this.debug.println("UploadQueue: impossible add upload, queue is full");
                return false;
            }
        }
        return true;
    }

    /**
       * getUploadQueue
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
      */
    public Vector getUploadQueue() {
        Vector newVect = new Vector();
        synchronized (pool) {
            DownloadInfo di;
            try {
                for (int i = 0; i < pool.size(); i++) {
                    di = new DownloadInfo((DownloadInfo) pool.get(i));
                    newVect.add(di);
                }
            } catch (GIEWSException e) {
                return null;
            }
        }
        return newVect;
    }

    /**
       * stopUpload
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
      */
    public boolean stopUpload(byte[] ID) {
        for (int i = 0; i < dThread.length; i++) {
            if (dThread[i].getStateThread() == RUNNABLE) {
                if (dThread[i].matchDownload(ID)) {
                    try {
                        dThread[i].stopThread();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
       * stopAllUpload
       *
       * @version 1, last modified by A. Tamburo, 17/08/06
      */
    public boolean stopAllUpload() {
        debug.println("Upload: stop all download process");
        for (int i = 0; i < dThread.length; i++) {
            dThread[i].stopThread();
        }
        return false;
    }

    /**
       * clearAllThread
       *
       * @version 1, last modified by A. Tamburo, 11/09/06
       */
    public void clearAllThread() {
        this.debug.println("PooledUpload: Clear all process");
        for (int i = 0; i < dThread.length; i++) {
            dThread[i].clearThread();
        }
        synchronized (pool) {
            pool.notifyAll();
        }
    }

    /**
       * deleteFromQueue
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
      */
    public boolean deleteFromQueue(byte[] ID) {
        synchronized (pool) {
            DownloadInfo di;
            for (int i = 0; i < pool.size(); i++) {
                di = (DownloadInfo) pool.get(i);
                if (DownloadInfo.matching(ID, di.getID())) {
                    pool.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
       * getUploadActive
       *
       * @version 1, last modified by A. Tamburo, 19/01/06
      */
    public Vector getUploadActive() {
        Vector v = new Vector();
        for (int i = 0; i < dThread.length; i++) {
            if (dThread[i].getStateThread() == RUNNABLE) {
                v.add(dThread[i].getUploadInfo());
            }
        }
        return v;
    }

    /**
       * getCountUpload
       *
       * @version 1, last modified by A. Tamburo, 24/01/06
      */
    public int getCountUpload() {
        int count = 0;
        for (int i = 0; i < dThread.length; i++) {
            if (dThread[i].getStateThread() == RUNNABLE) {
                count++;
            }
        }
        return count;
    }

    /**
       * get
       *
       * @version 1, last modified by A. Tamburo, 24/01/06
      */
    public int getBandwith() {
        return this.bandwidth;
    }

    /**
       * decrementSpeed
       *
       * @version 1, last modified by A. Tamburo, 24/01/06
      */
    public void decrementSpeed(int id, int delta) {
        synchronized (speed) {
            speed[id] -= delta;
            int count = 0;
            for (int i = 0; i < dThread.length; i++) {
                if (dThread[i].getStateThread() == RUNNABLE) {
                    count += speed[i];
                } else {
                    speed[i] = 0;
                }
            }
            this.bandwidthResidue = this.bandwidth - count;
        }
    }

    /**
       * incrementSpeed
       *
       * @version 1, last modified by A. Tamburo, 24/01/06
      */
    public void incrementSpeed(int id) {
        if (speed[id] >= this.bandwidth) return;
        synchronized (speed) {
            if (this.bandwidthResidue > 0) {
                if (this.bandwidthResidue > 100) this.speed[id] += (this.bandwidthResidue / this.dThread.length); else this.speed[id] += this.bandwidthResidue;
            } else {
                int max = 0, index = -1;
                for (int i = 0; i < dThread.length; i++) {
                    if (speed[i] > max) {
                        index = i;
                        max = speed[i];
                    }
                }
                if (index != id && index != -1) {
                    int middle = (speed[index] + speed[id]) / 2;
                    speed[id] = middle;
                    speed[index] = middle;
                }
            }
            int count = 0;
            for (int i = 0; i < dThread.length; i++) {
                if (dThread[i].getStateThread() == RUNNABLE) {
                    count += speed[i];
                } else {
                    speed[i] = 0;
                }
            }
            this.bandwidthResidue = this.bandwidth - count;
        }
    }

    /**
        * isUploadInQueue
        *
        * @param  byte[] IDUpload  6/02/06
      */
    public boolean isUploadInQueue(byte[] IDUpload) {
        synchronized (pool) {
            DownloadInfo di;
            for (int i = 0; i < pool.size(); i++) {
                di = (DownloadInfo) pool.get(i);
                if (DownloadInfo.matching(di.getID(), IDUpload)) return true;
            }
        }
        return false;
    }

    /**
       * getSpeed
       *
       * @version 1, last modified by A. Tamburo, 24/01/06
      */
    public int getSpeed(int id) {
        return speed[id];
    }
}
