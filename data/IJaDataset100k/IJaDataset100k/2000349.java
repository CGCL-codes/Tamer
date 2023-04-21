package org.fcrepo.server.security;

import java.util.SortedMap;
import java.util.TreeMap;

public class ServiceDeploymentRoleConfig extends AbstractRoleConfig {

    private final String m_role;

    private final SortedMap<String, MethodRoleConfig> m_methodConfigs;

    public ServiceDeploymentRoleConfig(DefaultRoleConfig defaultConfig, String pid) {
        super(defaultConfig);
        m_role = pid;
        m_methodConfigs = new TreeMap<String, MethodRoleConfig>();
    }

    @Override
    public String getRole() {
        return m_role;
    }

    public SortedMap<String, MethodRoleConfig> getMethodConfigs() {
        return m_methodConfigs;
    }
}
