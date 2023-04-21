package com.volantis.mcs.runtime.project;

import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.cache.group.Group;
import java.util.Map;

/**
 * Responsible for building a project.
 *
 * <p>This has been extracted from the ProjectManager to enable it to be more
 * easily unit tested.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface RuntimeProjectConfigurator {

    /**
     * Create the global project.
     *
     * @param policySourceFactory The reader used by the policy source of the global
     *                     project.
     * @return The global project.
     */
    RuntimeProject createGlobalProject(RuntimePolicySourceFactory policySourceFactory);

    /**
     * Build the project from the configuration.
     *
     * @param configuration       The configuration from which the project
     *                            should be built.
     * @param baseProject         The base project, may be null.
     * @param policySourceFactory The factory for creating policy sources.
     * @return The built project.
     */
    RuntimeProject buildProject(RuntimeProjectConfiguration configuration, RuntimeProject baseProject, RuntimePolicySourceFactory policySourceFactory);

    PredefinedProjects createPredefinedProjects(ProjectsConfiguration projects, ConfigContext configContext, RuntimePolicySourceFactory policySourceFactory) throws ConfigurationException;
}
