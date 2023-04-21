package com.griddynamics.convergence.demo.dar.service;

import com.griddynamics.convergence.demo.dar.control.JobRunner;

public interface DemoUi {

    public void setServers(String[] servers);

    public void setServerColors(String[] colors);

    public void setStrategies(ExperimentSetup[] strategies);

    public void setJobRunner(JobRunner runner);

    public void setTaskSet(int[] taskSet);

    public void setStatus(String status);
}
