package org.fcrepo.server.security;

public class MethodRoleConfig extends AbstractRoleConfig {

    private final String m_role;

    public MethodRoleConfig(ServiceDeploymentRoleConfig sDepConfig, String methodName) {
        super(sDepConfig);
        m_role = sDepConfig.getRole() + "/" + methodName;
    }

    @Override
    public String getRole() {
        return m_role;
    }
}
