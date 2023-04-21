package com.aelitis.azureus.plugins.net.buddy.tracker;

public interface BuddyPluginTrackerListener {

    public void enabledStateChanged(BuddyPluginTracker tracker, boolean enabled);

    public void networkStatusChanged(BuddyPluginTracker tracker, int new_status);
}
