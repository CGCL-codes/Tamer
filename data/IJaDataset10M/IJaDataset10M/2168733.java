package org.maven.ide.eclipse.ext.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.commons.util.DriverClassSupport;
import org.nexopenframework.ide.eclipse.commons.util.DriverClassSupport.Dependency;
import org.nexopenframework.ide.eclipse.commons.xml.ContentHandlerCallback;
import org.nexopenframework.ide.eclipse.commons.xml.ContentHandlerTemplate;
import org.nexopenframework.ide.eclipse.commons.xml.XMLUtils;
import org.nexopenframework.ide.eclipse.velocity.VelocityEngineHolder;
import org.nexopenframework.ide.eclipse.velocity.VelocityEngineUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <p></p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0-m2
 */
public abstract class Jetty6ConfigBuilder {

    /**
	 * <p>Avoid object construction</p>
	 */
    private Jetty6ConfigBuilder() {
        super();
    }

    /**
	 * <p></p>
	 * 
	 * @param prj
	 * @param conf
	 * @throws VelocityException 
	 * @throws CoreException 
	 */
    public static void createJetty6Config(final IProject prj, final IJetty6Configuration conf) throws VelocityException, CoreException {
        try {
            final VelocityEngine ve = VelocityEngineHolder.getEngine();
            final Map<String, String> model = new HashMap<String, String>();
            {
                model.put("project", prj.getName());
                final String driverClass = DriverClassSupport.getDriverClassName(conf.getDriver());
                model.put("driver", driverClass);
                model.put("url", conf.getUrl());
                model.put("username", conf.getUser());
                model.put("password", conf.getPassword());
            }
            final String jettyEnv = VelocityEngineUtils.mergeTemplateIntoString(ve, "jetty-env.vm", model);
            IFile file = prj.getFile("web/src/main/webapp/WEB-INF/jetty-env.xml");
            InputStream is = null;
            try {
                is = new ByteArrayInputStream(jettyEnv.getBytes());
                file.create(is, false, null);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (final IOException e) {
                    }
                }
            }
            final IFile pom = prj.getFile("pom.xml");
            ContentHandlerTemplate.handle(pom, new ContentHandlerCallback() {

                public void processHandle(final Document doc) {
                    final Element root = doc.getDocumentElement();
                    final Element build = XMLUtils.getChildElementByTagName(root, "build");
                    final Element plugins = XMLUtils.getChildElementByTagName(build, "plugins");
                    final Element plugin = createJetty6Plugin(doc, conf);
                    plugins.appendChild(plugin);
                }
            });
        } catch (final VelocityException e) {
            Logger.logException(e);
            Logger.log(Logger.ERROR, "Impossible to create jetty-env.xml [VelocityException] in project " + prj.getName());
            Logger.log(Logger.ERROR, "VelocityException :: " + e.getMessage());
            throw e;
        }
    }

    /**
	 * @param doc
	 * @param conf
	 * @return
	 */
    private static Element createJetty6Plugin(final Document doc, final IJetty6Configuration conf) {
        final Element plugin = doc.createElement("plugin");
        final Element groupId = doc.createElement("groupId");
        groupId.setTextContent("org.mortbay.jetty");
        final Element artifactId = doc.createElement("artifactId");
        artifactId.setTextContent("maven-jetty-plugin");
        final Element configuration = doc.createElement("configuration");
        createConfiguration(doc, configuration, conf);
        final Element dependencies = doc.createElement("dependencies");
        createDependencies(doc, dependencies, conf);
        plugin.appendChild(groupId);
        plugin.appendChild(artifactId);
        plugin.appendChild(configuration);
        plugin.appendChild(dependencies);
        return plugin;
    }

    /**
	 * @param doc
	 * @param dependencies
	 * @param conf
	 */
    private static void createDependencies(final Document doc, final Element dependencies, final IJetty6Configuration conf) {
        dependencies.appendChild(createDependency(doc, "c3p0", "c3p0", "0.9.1.2"));
        dependencies.appendChild(createDependency(doc, "commons-logging", "commons-logging", "1.0.4"));
        final Dependency dependency = DriverClassSupport.getDependency(conf.getDriver());
        dependencies.appendChild(createDependency(doc, dependency.groupId, dependency.artifactId, dependency.version));
    }

    private static Element createDependency(final Document doc, final String groupId, final String artifactId, final String version) {
        final Element dependency = doc.createElement("dependency");
        final Element e_groupId = doc.createElement("groupId");
        e_groupId.setTextContent(groupId);
        final Element e_artifactId = doc.createElement("artifactId");
        e_artifactId.setTextContent(artifactId);
        final Element e_version = doc.createElement("version");
        e_version.setTextContent(version);
        dependency.appendChild(e_groupId);
        dependency.appendChild(e_artifactId);
        dependency.appendChild(e_version);
        return dependency;
    }

    private static void createConfiguration(final Document doc, final Element configuration, final IJetty6Configuration conf) {
        final Element webApp = doc.createElement("webApp");
        webApp.setTextContent("${basedir}/web/src/main/webapp");
        final Element jettyEnvXml = doc.createElement("jettyEnvXml");
        jettyEnvXml.setTextContent("${basedir}/web/src/main/webapp/WEB-INF/jetty-env.xml");
        final Element scanIntervalSeconds = doc.createElement("scanIntervalSeconds");
        scanIntervalSeconds.setTextContent(conf.getScanInterval());
        final Element connectors = doc.createElement("connectors");
        final Element connector = doc.createElement("connector");
        connector.setAttribute("implementation", "org.mortbay.jetty.nio.SelectChannelConnector");
        final Element port = doc.createElement("port");
        port.setTextContent(conf.getHttpPort());
        final Element maxIdleTime = doc.createElement("maxIdleTime");
        maxIdleTime.setTextContent("60000");
        connector.appendChild(port);
        connector.appendChild(maxIdleTime);
        connectors.appendChild(connector);
        final Element stopPort = doc.createElement("stopPort");
        stopPort.setTextContent(conf.getStopPort());
        final Element stopKey = doc.createElement("stopKey");
        stopKey.setTextContent("nexopen");
        configuration.appendChild(webApp);
        configuration.appendChild(jettyEnvXml);
        configuration.appendChild(scanIntervalSeconds);
        configuration.appendChild(connectors);
        configuration.appendChild(stopPort);
        configuration.appendChild(stopKey);
    }
}
