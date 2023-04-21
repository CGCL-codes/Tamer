package org.apache.tools.ant.taskdefs.optional.dependencies;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: rgold
 * Date: May 8, 2006
 * Time: 2:20:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class Maven2Repository extends MavenRepositoryBase {

    static final String DEFAULT_REMOTE_REPOSITORY = "http://www.ibiblio.org/maven2/";

    public Maven2Repository() throws MalformedURLException {
        super(new URL(DEFAULT_REMOTE_REPOSITORY));
    }

    public Maven2Repository(URL repositoryPath) {
        super(repositoryPath);
    }

    protected String getRelativeDependencyPath(String group, String type, String artifact, String version) {
        return group + '/' + artifact + '/' + version + "/" + getDependencyName(artifact, version, type);
    }

    public String getLatestVersion(Dependencies task, String group, String type, String artifact) {
        try {
            URL directory = new URL(getUrl(), group + '/' + artifact + '/');
            InputStream is = task.read(directory);
            DirectoryListing listing = DirectoryListing.createDirectoryListing(is);
            String[] matches = listing.getMatches(".*");
            return latestVersion(matches);
        } catch (IOException e) {
            throw new BuildException("Unable to read directory for " + group + '/' + type + "s/");
        }
    }

    private String latestVersion(String[] artifacts) {
        String latest = artifacts[0];
        for (int i = 0; i < artifacts.length; i++) {
            if (artifacts[i].compareTo(latest) > 0) latest = artifacts[i];
        }
        return latest;
    }
}
