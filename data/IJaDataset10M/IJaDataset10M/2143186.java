package org.kablink.teaming.gwt.client.profile;

import org.kablink.teaming.gwt.client.rpc.shared.VibeRpcResponseData;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DiskUsageInfo implements IsSerializable, VibeRpcResponseData {

    private boolean isEnabled = false;

    private boolean isHighWaterMarkExceeded = false;

    private boolean isExceeded = false;

    private String quotaMessage = null;

    private String maxQuota;

    private String usedQuota;

    /**
	 * Constructor method.
	 * 
	 * No parameters as per GWT serialization requirements.
	 */
    public DiskUsageInfo() {
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isHighWaterMarkExceeded() {
        return isHighWaterMarkExceeded;
    }

    public void setHighWaterMarkExceeded(boolean isHighWaterMarkExceeded) {
        this.isHighWaterMarkExceeded = isHighWaterMarkExceeded;
    }

    public boolean isExceeded() {
        return isExceeded;
    }

    public void setExceeded(boolean isExceeded) {
        this.isExceeded = isExceeded;
    }

    public String getMaxQuota() {
        return maxQuota;
    }

    public void setMaxQuota(String maxQuota) {
        this.maxQuota = maxQuota;
    }

    public String getUsedQuota() {
        return usedQuota;
    }

    public void setUsedQuota(String usedQuota) {
        this.usedQuota = usedQuota;
    }

    public void setQuotaMessage(String quotaMessage) {
        this.quotaMessage = quotaMessage;
    }

    public String getQuotaMessage() {
        return quotaMessage;
    }
}
