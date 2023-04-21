package com.volantis.mcs.runtime.repository.remote.xml;

import com.volantis.mcs.policies.PolicyBuildersResponse;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.remote.PolicyBuilders;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.shared.net.http.HttpGetMethod;
import com.volantis.shared.net.http.HttpMethodFactory;
import com.volantis.shared.net.http.HttpStatusCode;
import java.io.InputStream;

/**
 * Reads a {@link PolicyBuilders} from a remote source.
 */
public class RemotePolicyBuildersReader extends AbstractRemoteReader {

    /**
     * Initialise.
     *
     * @param methodFactory  Creates {@link HttpGetMethod} instances.
     * @param projectManager The project manager.
     * @param parser         The parser responsible for migrating and parsing
     *                       remote policies.
     */
    public RemotePolicyBuildersReader(HttpMethodFactory methodFactory, ProjectManager projectManager, RemotePolicyBuilderParser parser) {
        super(methodFactory, projectManager, parser);
    }

    /**
     * Get the {@link PolicyBuilders}.
     *
     * @param project The project to check.
     * @param name    The name of the resource containing the policies.
     * @return The response containing the {@link PolicyBuilders} and
     *         the owning project.
     * @throws RepositoryException If there was a problem accessing the remote
     *                             policy.
     */
    public PolicyBuildersResponse getPolicyBuilders(Project project, String name) throws RepositoryException {
        PolicyBuilders builders;
        HttpGetMethod method = methodFactory.createGetMethod(name);
        try {
            method.addRequestHeader(REQUEST_TYPE_HEADER_NAME, "policySet");
            HttpStatusCode code = method.execute();
            if (code == HttpStatusCode.OK) {
                project = updateProject(project, method);
                InputStream in = method.getResponseBodyAsStream();
                builders = parser.parsePolicyBuilders(in, name);
            } else {
                builders = null;
            }
        } catch (Exception e) {
            logger.error("remote-component-not-available", new Object[] { name }, e);
            builders = null;
        } finally {
            method.releaseConnection();
        }
        return new PolicyBuildersResponse(project, builders);
    }
}
