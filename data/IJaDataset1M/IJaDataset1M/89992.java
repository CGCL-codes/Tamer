package com.pentagaia.eclipse.sgs.api;

import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;

/**
 * Properties used by the project facet
 * 
 * @author mepeisen
 */
public interface ISgsFacetInstallDataModelProperties extends IJ2EEModuleFacetInstallDataModelProperties {

    /** Property containing the main source folder used by this project */
    String JAVA_SOURCE_FOLDER = "ISgsFacetInstallDataModelProperties.javaSourceFolder";

    /** Property containing the application name */
    String SGS_APP_NAME = "ISgsFacetInstallDataModelProperties.sgsAppName";

    /** Property containing the default port */
    String SGS_DEFAULT_PORT = "ISgsFacetInstallDataModelProperties.sgsDefaultPort";

    /** Property containing the sgs authentificator */
    String SGS_AUTHENTIFICATOR = "ISgsFacetInstallDataModelProperties.sgsAuthentificator";

    /** Property containing the sgs app listener */
    String SGS_APP_LISTENER = "ISgsFacetInstallDataModelProperties.sgsAppListener";

    /** Property containing a list of services */
    String SGS_SERVICES = "ISgsFacetInstallDataModelProperties.sgsServices";

    /** Property containing a list of managers */
    String SGS_MANAGERS = "ISgsFacetInstallDataModelProperties.sgsManagers";

    /** Property containing configured project configuration */
    String PROJECT_CONFIGURATION = "ISgsFacetInstallDataModelProperties.projectConfiguration";

    /** Property containing maven group name */
    String MAVEN_GROUP = "ISgsFacetInstallDataModelProperties.mavenGroup";

    /** Property containing maven module name */
    String MAVEN_NAME = "ISgsFacetInstallDataModelProperties.mavenName";

    /** Property containing maven version */
    String MAVEN_VERSION = "ISgsFacetInstallDataModelProperties.mavenVersion";

    /** Property containing maven project name */
    String MAVEN_PROJECT_NAME = "ISgsFacetInstallDataModelProperties.mavenProjectName";
}
