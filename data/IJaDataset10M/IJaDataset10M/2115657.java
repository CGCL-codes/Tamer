package org.xmatthew.spy2servers.component.spy.jmx;

import java.util.Date;
import org.xmatthew.spy2servers.component.util.StorageUnitUtils;

/**
 * @author Matthew Xie
 *
 */
public class MemorySpy {

    public static final int MEMORY_GOOD = 0;

    public static final int MEMORY_WILL_OUT_OF_MAX = 1;

    public static final int MEMORY_LOWER_TO_GOOD = 2;

    private long memoryUsedToAlert;

    private float memoryUsedPercentToAlert;

    private long alertAfterKeepTimeLive;

    private Date keepLiveDate;

    private int status;

    /**
	 * @return the alertAfterKeepTimeLive
	 */
    public long getAlertAfterKeepTimeLive() {
        return alertAfterKeepTimeLive;
    }

    /**
	 * @param alertAfterKeepTimeLive the alertAfterKeepTimeLive to set
	 */
    public void setAlertAfterKeepTimeLive(long alertAfterKeepTimeLive) {
        this.alertAfterKeepTimeLive = alertAfterKeepTimeLive;
    }

    /**
	 * @return the memoryUsed
	 */
    public long getMemoryUsedToAlert() {
        return memoryUsedToAlert;
    }

    /**
	 * @param memoryUsed the memoryUsed to set
	 */
    public void setMemoryUsedToAlert(long memoryUsed) {
        this.memoryUsedToAlert = memoryUsed;
    }

    public MemorySpy() {
        super();
        this.memoryUsedToAlert = -1;
        this.alertAfterKeepTimeLive = -1;
        this.memoryUsedPercentToAlert = 0;
        init();
    }

    public void init() {
        this.keepLiveDate = null;
        this.memoryUsedToAlert = MEMORY_GOOD;
    }

    /**
	 * @param memoryUsed
	 * @return
	 */
    public int spyMemory(long memoryUsed, long maxMemorySize) {
        if ((memoryUsedToAlert == -1 && memoryUsedPercentToAlert <= 0) || alertAfterKeepTimeLive == -1) {
            return MEMORY_GOOD;
        }
        boolean isSpyed = false;
        if (memoryUsedPercentToAlert > 0) {
            if (memoryUsedPercentToAlert <= (memoryUsed / Double.valueOf(maxMemorySize) * 100)) {
                isSpyed = true;
            }
        } else {
            if (StorageUnitUtils.asMbyteFromByte(memoryUsed) >= this.memoryUsedToAlert) {
                isSpyed = true;
            }
        }
        if (isSpyed) {
            if (keepLiveDate == null) {
                keepLiveDate = new Date();
                return MEMORY_GOOD;
            }
            if (status != MEMORY_WILL_OUT_OF_MAX) {
                long timeKeepLived = (System.currentTimeMillis() - keepLiveDate.getTime()) / 1000;
                if (timeKeepLived >= alertAfterKeepTimeLive) {
                    status = MEMORY_WILL_OUT_OF_MAX;
                    return MEMORY_WILL_OUT_OF_MAX;
                }
            }
        } else {
            if (status == MEMORY_WILL_OUT_OF_MAX) {
                status = MEMORY_GOOD;
                return MEMORY_LOWER_TO_GOOD;
            }
        }
        return MEMORY_GOOD;
    }

    /**
	 * @return the memoryUsedPercent
	 */
    public float getMemoryUsedPercentToAlert() {
        return memoryUsedPercentToAlert;
    }

    /**
	 * @param memoryUsedPercent the memoryUsedPercent to set
	 */
    public void setMemoryUsedPercentToAlert(float memoryUsedPercent) {
        this.memoryUsedPercentToAlert = memoryUsedPercent;
    }
}
