package org.cleartk.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * Check that all dependency versions in a multi-module project are consistent.
 * 
 * @goal check-version-consistency
 * @phase validate
 * @aggregator true
 * @requiresDependencyCollection compile
 */
public class ConsistentVersionsMojo extends AbstractMojo {

    /**
   * The Maven Project.
   * 
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException, MojoFailureException {
        Map<String, Map<String, Set<String>>> dependencyVersionProjects;
        dependencyVersionProjects = new HashMap<String, Map<String, Set<String>>>();
        for (Object projectObject : this.mavenProject.getCollectedProjects()) {
            MavenProject project = (MavenProject) projectObject;
            for (Object artifactObject : project.getArtifacts()) {
                Artifact artifact = (Artifact) artifactObject;
                List<String> trail = artifact.getDependencyTrail();
                this.add(dependencyVersionProjects, trailToString(trail.subList(0, trail.size() - 1)), artifact.getDependencyConflictId(), artifact.getVersion());
            }
            DependencyManagement dependencyManagement = project.getDependencyManagement();
            if (dependencyManagement != null) {
                for (Dependency dependency : dependencyManagement.getDependencies()) {
                    this.add(dependencyVersionProjects, project.getArtifactId(), dependency.getManagementKey(), dependency.getVersion());
                }
            }
        }
        StringBuilder error = new StringBuilder();
        for (String dependency : dependencyVersionProjects.keySet()) {
            Map<String, Set<String>> versionProjects = dependencyVersionProjects.get(dependency);
            if (versionProjects.size() > 1) {
                error.append('\n');
                error.append(String.format("Found multiple versions of %s:\n", dependency));
                for (String version : versionProjects.keySet()) {
                    for (String project : versionProjects.get(version)) {
                        error.append(String.format("  %s in %s\n", version, project));
                    }
                }
            }
        }
        if (error.length() > 0) {
            throw new MojoFailureException(error.toString());
        }
    }

    private void add(Map<String, Map<String, Set<String>>> dependencyVersionProjects, String source, String key, String version) {
        String keyWithVersion = String.format("%s:%s", key, version);
        if (!dependencyVersionProjects.containsKey(key)) {
            dependencyVersionProjects.put(key, new HashMap<String, Set<String>>());
        }
        if (!dependencyVersionProjects.get(key).containsKey(keyWithVersion)) {
            dependencyVersionProjects.get(key).put(keyWithVersion, new HashSet<String>());
        }
        dependencyVersionProjects.get(key).get(keyWithVersion).add(source);
    }

    private String trailToString(List<String> trail) {
        List<String> reversedTrail = new ArrayList<String>(trail);
        Collections.reverse(reversedTrail);
        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = reversedTrail.iterator();
        if (iter.hasNext()) {
            builder.append(iter.next());
        }
        while (iter.hasNext()) {
            builder.append(" -> ");
            builder.append(iter.next());
        }
        return builder.toString();
    }
}
