package com.griddynamics.convergence.demo.dar.service;

import java.io.Serializable;

public interface ExperimentSetup extends Serializable {

    public String getId();

    public String getName();

    public String getColor();

    public DataAccessStrategy getStrategy();

    public GridSchedulerProvider getGridProvider();

    public TradeStorageProvider getStorageProvider();
}
