package com.frameworkset.common.poolman.management;

import java.io.Serializable;
import java.util.Map;

public interface PoolManDeployer extends Serializable {

    public void deployConfiguration(PoolManConfiguration config) throws Exception;

    public void deployConfiguration(PoolManConfiguration config, Map<String, String> values) throws Exception;

    public void deployConfiguration(PoolManConfiguration config, String dbname) throws Exception;
}
