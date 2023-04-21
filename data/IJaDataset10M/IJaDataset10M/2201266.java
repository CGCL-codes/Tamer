package net.fckeditor.connector.impl;

import javax.servlet.ServletContext;
import net.fckeditor.handlers.PropertiesLoader;
import net.fckeditor.requestcycle.impl.ContextPathBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context filesystem backend connector. The file access will be restricted to
 * the real context root of the currently deployed webapp.
 * <p>
 * Though, this connector has some drawbacks:
 * <ul>
 * <li>Stored files and folders will be gone if the context is undeployed,
 * unless you have saved them before.</li>
 * <li>Some servers are unable to write to the real context root (like
 * WebSphere), see {@link #init(ServletContext)} for more details.</li>
 * <li>Some containers (like Jetty) unpack the war file to a different directory
 * at server start which means that files are gone in temp.</li>
 * </ul>
 * </p>
 * Use the {@link ContextPathBuilder} in conjunction with this class.
 * 
 * @version $Id: ContextConnector.java 4785 2009-12-21 20:10:28Z mosipov $
 */
public class ContextConnector extends AbstractLocalFileSystemConnector {

    private final Logger logger = LoggerFactory.getLogger(ContextConnector.class);

    /**
	 * {@inheritDoc}
	 * This method will prepare the connector for further usage, additionally it
	 * will check if the aforementioned drawback exists. It will try to resolve
	 * the default {@link PropertiesLoader#getUserFilesPath() UserFilesPath}
	 * with
	 * <code><a href="http://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/ServletContext.html#getRealPath(java.lang.String)"
	 * target="_blank">ServletContext.getRealPath(String)</a></code> against the
	 * local filesystem (real path). If it fails, it will emit helpful log
	 * messages and will throw an exception too.
	 */
    public void init(final ServletContext servletContext) throws Exception {
        this.servletContext = servletContext;
        String defaultAbsolutePath = getRealUserFilesAbsolutePath(PropertiesLoader.getUserFilesPath());
        if (defaultAbsolutePath == null) {
            logger.error("The context root cannot be resolved against the local filesystem");
            logger.info("Your servlet container/application server does not expand deployed war files");
            logger.debug("Use another Connector implementation (e.g. LocalConnector) and consult http://www.fckeditor.net/forums/viewtopic.php?f=6&t=11568");
            throw new NullPointerException("The real context root cannot be resolved against the local filesystem");
        }
    }

    /**
	 * Resolves the userfiles absolute path against the current context real
	 * path.
	 */
    @Override
    protected String getRealUserFilesAbsolutePath(String userFilesAbsolutePath) {
        return servletContext.getRealPath(userFilesAbsolutePath);
    }
}
