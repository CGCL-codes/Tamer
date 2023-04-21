package com.google.gdata.client.codesearch;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.codesearch.CodeSearchFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Google Code Search data API.
 *
 * 
 */
public class CodeSearchService extends GoogleService {

    /**
   * The abbreviated name of Google Code Search recognized by Google.  The
   * service name is used when requesting an authentication token.
   */
    public static final String CODESEARCH_SERVICE = "codesearch";

    /**
   * The version ID of the service.
   */
    public static final String CODESEARCH_SERVICE_VERSION = "CodeSearch-Java/" + CodeSearchService.class.getPackage().getImplementationVersion();

    /**
   * GData versions supported by Google Code Search Service.
   */
    public static final class Versions {

        /** Version 1 of the Codesearch Data API. */
        public static final Version V1 = new Version(CodeSearchService.class, "1.0", Service.Versions.V1);

        /** Version 2 of the Codesearch Data API. */
        public static final Version V2 = new Version(CodeSearchService.class, "2.0", Service.Versions.V2);
    }

    /**
   * Default GData version used by the Google Code Search service.
   */
    public static final Version DEFAULT_VERSION = Service.initServiceVersion(CodeSearchService.class, Versions.V2);

    /**
   * Constructs an instance connecting to the Google Code Search service for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
    public CodeSearchService(String applicationName) {
        super(CODESEARCH_SERVICE, applicationName, "http", "www.google.com");
        declareExtensions();
    }

    /**
   * Constructs an instance connecting to the Google Code Search service for an
   * application with the name {@code applicationName} and the given {@code
   * GDataRequestFactory} and {@code AuthTokenFactory}. Use this constructor to
   * override the default factories.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param requestFactory the request factory that generates gdata request
   *     objects
   * @param authTokenFactory the factory that creates auth tokens
   */
    public CodeSearchService(String applicationName, Service.GDataRequestFactory requestFactory, AuthTokenFactory authTokenFactory) {
        super(applicationName, requestFactory, authTokenFactory);
        declareExtensions();
    }

    /**
   * Constructs an instance connecting to the Google Code Search service with
   * name {@code serviceName} for an application with the name {@code
   * applicationName}.  The service will authenticate at the provided {@code
   * domainName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *     ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
    public CodeSearchService(String applicationName, String protocol, String domainName) {
        super(CODESEARCH_SERVICE, applicationName, protocol, domainName);
        declareExtensions();
    }

    @Override
    public String getServiceVersion() {
        return CODESEARCH_SERVICE_VERSION + " " + super.getServiceVersion();
    }

    /**
   * Returns the current GData version used by the Google Code Search service.
   */
    public static Version getVersion() {
        return VersionRegistry.get().getVersion(CodeSearchService.class);
    }

    /**
   * Declare the extensions of the feeds for the Google Code Search service.
   */
    private void declareExtensions() {
        new CodeSearchFeed().declareExtensions(extProfile);
    }
}
