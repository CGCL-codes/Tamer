package com.jguild.devportal.infrastructure.continuousintegration;

import com.jguild.devportal.infrastructure.InfrastructureApplication;
import com.jguild.devportal.infrastructure.InfrastructureConfiguration;
import com.jguild.devportal.project.Module;
import org.hibernate.annotations.Entity;

@Entity
public class ContinuousIntegrationConfiguration extends InfrastructureConfiguration {

    public ContinuousIntegrationConfiguration() {
    }

    public ContinuousIntegrationConfiguration(final Module module, final InfrastructureApplication application) {
        super(module, application);
    }
}
