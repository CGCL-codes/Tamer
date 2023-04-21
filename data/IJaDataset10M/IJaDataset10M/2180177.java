package com.google.code.sagetvaddons.sre3.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author dbattams
 *
 */
public final class Settings implements IsSerializable {

    private boolean enabled;

    private boolean endEarly;

    private int maxExtensionHours;

    private int defaultPadMins;

    private boolean rmManualFlag;

    private boolean ignoreBackToBack;

    private boolean sendSysMsgs;

    private int postGamePadMins;

    private String goEmail;

    @SuppressWarnings("unused")
    private Settings() {
    }

    /**
	 * @param enabled
	 * @param endEarly
	 * @param maxExtensionHours
	 * @param defaultPadMins
	 * @param rmManualFlag
	 * @param ignoreBackToBack
	 * @param sendSysMsgs
	 * @param postGamePadMins
	 */
    public Settings(boolean enabled, boolean endEarly, int maxExtensionHours, int defaultPadMins, boolean rmManualFlag, boolean ignoreBackToBack, boolean sendSysMsgs, int postGamePadMins, String goEmail) {
        this.enabled = enabled;
        this.endEarly = endEarly;
        this.maxExtensionHours = maxExtensionHours;
        this.defaultPadMins = defaultPadMins;
        this.rmManualFlag = rmManualFlag;
        this.ignoreBackToBack = ignoreBackToBack;
        this.sendSysMsgs = sendSysMsgs;
        this.postGamePadMins = postGamePadMins;
        this.goEmail = goEmail;
    }

    /**
	 * 
	 * @return the postGamePadMins
	 */
    public int getPostGamePadMins() {
        return postGamePadMins;
    }

    /**
	 * @return the enabled
	 */
    public boolean isEnabled() {
        return enabled;
    }

    /**
	 * @return the endEarly
	 */
    public boolean isEndEarly() {
        return endEarly;
    }

    /**
	 * @return the maxExtensionHours
	 */
    public int getMaxExtensionHours() {
        return maxExtensionHours;
    }

    /**
	 * @return the defaultPadMins
	 */
    public int getDefaultPadMins() {
        return defaultPadMins;
    }

    /**
	 * @return the rmManualFlag
	 */
    public boolean isRmManualFlag() {
        return rmManualFlag;
    }

    /**
	 * @return the ignoreBackToBack
	 */
    public boolean isIgnoreBackToBack() {
        return ignoreBackToBack;
    }

    /**
	 * @return the sendSysMsgs
	 */
    public boolean isSendSysMsgs() {
        return sendSysMsgs;
    }

    public String getGoEmail() {
        return goEmail;
    }
}
