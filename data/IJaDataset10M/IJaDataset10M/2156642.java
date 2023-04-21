package org.apache.maven.plugin.assembly;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.AndArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.resolver.filter.IncludesArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Unpack project dependencies.  Currently supports dependencies of type jar and zip.
 *
 * @version $Id$
 * @goal unpack
 * @requiresDependencyResolution test
 */
public class UnpackMojo extends AbstractUnpackingMojo {

    /**
     * The set of dependecies to include when unpacking
     * 
     * 
     * @parameter
     */
    private Set dependencyIncludes = new HashSet();

    ;

    /**
     * The set of dependecies to exclude when unpacking
     * 
     * @parameter
     */
    private Set dependencyExcludes = new HashSet();

    ;

    /**
     * Unpacks the archive file.
     *
     * @throws MojoExecutionException
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        AndArtifactFilter filter = new AndArtifactFilter();
        filter.add(new ScopeArtifactFilter(Artifact.SCOPE_RUNTIME));
        if (!dependencyIncludes.isEmpty()) {
            List includes = new ArrayList();
            includes.addAll(dependencyIncludes);
            filter.add(new IncludesArtifactFilter(includes));
        }
        if (!dependencyExcludes.isEmpty()) {
            List excludes = new ArrayList();
            excludes.addAll(dependencyExcludes);
            filter.add(new ExcludesArtifactFilter(excludes));
        }
        for (Iterator j = getDependencies().iterator(); j.hasNext(); ) {
            Artifact artifact = (Artifact) j.next();
            if (filter.include(artifact)) {
                String name = artifact.getFile().getName();
                File tempLocation = new File(workDirectory, name.substring(0, name.length() - 4));
                boolean process = false;
                if (!tempLocation.exists()) {
                    tempLocation.mkdirs();
                    process = true;
                } else if (artifact.getFile().lastModified() > tempLocation.lastModified()) {
                    process = true;
                }
                if (process) {
                    File file = artifact.getFile();
                    try {
                        unpack(file, tempLocation);
                    } catch (NoSuchArchiverException e) {
                        this.getLog().info("Skip unpacking dependency file with unknown extension: " + file.getPath());
                    }
                }
            } else {
                this.getLog().debug("Skip unpacking dependency: " + artifact);
            }
        }
    }
}
