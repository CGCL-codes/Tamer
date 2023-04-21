package net.rptools.maptool.client;

import net.rptools.maptool.model.Zone;

/**
 */
public interface ZoneActivityListener {

    public void zoneAdded(Zone zone);

    public void zoneActivated(Zone zone);
}
