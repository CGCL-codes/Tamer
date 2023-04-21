package com.safi.workshop;

import java.util.List;
import com.safi.core.saflet.Saflet;
import com.safi.db.server.config.TelephonySubsystem;

public interface TelephonyModulePlugin {

    public abstract List<TelephonySubsystem> getAvailableTelephonySubsystems();

    public abstract String getPlatformId();

    public abstract Saflet createInitialSaflet();

    public abstract void preProcessSaflet(Saflet saflet);
}
