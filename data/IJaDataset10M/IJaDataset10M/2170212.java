package net.javacrumbs.mvnindex2.crawler;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.project.MavenProject;

public interface MavenProjectListener {

    static final MavenProjectListener LOGGING_PROJECT_LISTENER = new MavenProjectListener() {

        private final Log logger = LogFactory.getLog(getClass());

        public void projectParsed(MavenProject project, File source, Exception e) {
            logger.debug(project.getName() + " " + source.getAbsolutePath() + (e != null ? " " + e.getMessage() : ""));
        }
    };

    public void projectParsed(MavenProject project, File source, Exception e);
}
