package mojo;

import index.IndexBuilder;
import index.context.Application;
import index.context.ApplicationLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * @goal createIndex
 */
public class IndexBuilderMojo extends ApplicationLoader {

    Log log = LogFactory.getLog(IndexBuilderMojo.class);

    public void execute() throws MojoExecutionException {
        Application application = this.loadApplication();
        IndexBuilder indexBuilder = application.<IndexBuilder>getInstance(IndexBuilder.class);
        indexBuilder.buildIndex();
    }
}
