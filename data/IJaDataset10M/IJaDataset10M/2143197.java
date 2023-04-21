package com.archhuman.hart.model;

import java.util.List;
import com.archhuman.hart.RespawnTimerApp;
import com.archhuman.hart.event.TickListener;
import com.archhuman.hart.exception.ConfigurationException;

/**
 * @author Sean
 */
public interface AlertGenerator extends TickListener {

    public String getName();

    public void initialize(RespawnTimerApp app) throws ConfigurationException;

    public void shutdown() throws ConfigurationException;

    public void reset(List itemTimeTrackers);
}
