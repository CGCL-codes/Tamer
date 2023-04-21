package net.kano.joustsim.oscar.oscar.service.ssi;

public interface ServerStoredSettingsListener {

    void handleMobileDeviceShownChanged(ServerStoredSettings settings, boolean mobileDeviceShown);

    void handleIdleTimeShownChanged(ServerStoredSettings settings, boolean idleTimeShown);

    void handleTypingShownChanged(ServerStoredSettings settings, boolean typingShown);

    void handleRecentBuddiesUsedChanged(ServerStoredSettings settings, boolean recentBuddiesUsed);
}
