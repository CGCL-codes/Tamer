package com.yilan.module;

import com.yilan.context.Runtime;

public interface DeployStrategy {

    public void deploy(Module module, Runtime runtime);

    public String getStrategyName();
}
