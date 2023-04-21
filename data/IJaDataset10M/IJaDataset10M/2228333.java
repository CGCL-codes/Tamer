package org.apache.maven.plugins.grafo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactCollector;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ResolutionListener;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

class DependencyExplorer {

    private static final String MARKER = "############################################################\n";

    private static final boolean VERBOSE = false;

    private DependencyHandler dependencyHandler;

    private Log log;

    private List reactorProjects;

    private MavenProjectBuilder mavenProjectBuilder;

    private ArtifactRepository localRepository;

    private ArtifactMetadataSource artifactMetadataSource;

    private ArtifactCollector artifactCollector;

    private static final boolean DEBUG = true;

    public void close() throws IOException {
        dependencyHandler.close();
    }

    public void explore(final MavenProject project, final List reactorProjects) throws MojoExecutionException {
        if (dependencyHandler == null) {
            throw new IllegalStateException("Attribute '" + dependencyHandler + "' must be set");
        }
        this.reactorProjects = reactorProjects;
        log.debug("\n" + MARKER + "Project " + project.getId() + "\n" + MARKER);
        debug(project);
        handleParent(project);
        final Set idsOfDirectDependencies = collectDirectDependencyIDs(project);
        handleDirectDependencies(project, idsOfDirectDependencies);
        handleModules(project);
        log.debug("\n\n=== finished===\n");
    }

    private void handleParent(final MavenProject project) {
        final MavenProject parent = project.getParent();
        if (parent != null) {
            log.debug("\n\nParent: \n" + parent.getId());
            dependencyHandler.addGeneralization(project, parent);
        }
    }

    private Set collectDirectDependencyIDs(final MavenProject project) {
        final Set idsOfDirectDependencies = new HashSet();
        final Model model = project.getOriginalModel();
        for (Iterator it = model.getDependencies().iterator(); it.hasNext(); ) {
            final Dependency dependency = (Dependency) it.next();
            log.debug("Direct dependency: " + dependency);
            idsOfDirectDependencies.add(dependency.getManagementKey());
        }
        return idsOfDirectDependencies;
    }

    private void handleDirectDependencies(final MavenProject project, final Set idsOfDirectDependencies) {
        for (Iterator it = project.getDependencies().iterator(); it.hasNext(); ) {
            final Dependency dependency = (Dependency) it.next();
            if (idsOfDirectDependencies.contains(dependency.getManagementKey())) {
                log.debug("Using Direct Dependency: " + dependency);
                dependencyHandler.addDependency(project, dependency);
                Artifact artifact = retrieveArtifactForDependency(project, dependency);
                log.debug("dependency.artifact = '" + artifact + "'");
                handleDependencyArtifact(project, artifact);
            }
        }
    }

    private void handleModules(final MavenProject project) throws MojoExecutionException {
        log.debug("\n\n#Modules = " + project.getModules().size() + "\n");
        for (Iterator it = project.getModules().iterator(); it.hasNext(); ) {
            final String modulePath = (String) it.next();
            log.debug("Module " + modulePath);
            final MavenProject moduleProject = readMavenProjectForModule(project.getBasedir(), modulePath);
            dependencyHandler.addModule(project, moduleProject);
        }
    }

    private void debug(final MavenProject project) {
        if (VERBOSE) {
            log.debug("\n\n");
            Model model = project.getOriginalModel();
            for (Iterator it = model.getDependencies().iterator(); it.hasNext(); ) {
                log.debug("Direct dependency: " + it.next());
            }
            log.debug("\n\n");
            for (Iterator it = project.getArtifacts().iterator(); it.hasNext(); ) {
                log.debug("Artifact: " + it.next());
            }
            log.debug("\n\n");
            for (Iterator it = project.getAttachedArtifacts().iterator(); it.hasNext(); ) {
                log.debug("Attached Artifact: " + it.next());
            }
            log.debug("\n\n");
            for (Iterator it = project.getDependencies().iterator(); it.hasNext(); ) {
                log.debug("Dependency: " + it.next());
            }
            log.debug("\n\n");
            for (Iterator it = project.getArtifactMap().keySet().iterator(); it.hasNext(); ) {
                Object key = it.next();
                log.debug("Artifact map entry: '" + key + "' --> '" + project.getArtifactMap().get(key) + "'");
            }
        }
    }

    private Artifact retrieveArtifactForDependency(MavenProject project, Dependency dependency) {
        final String id = dependency.getManagementKey();
        log.debug("retrieveArtifactForDependency: looking for key '" + id + "'");
        for (Iterator it = project.getArtifacts().iterator(); it.hasNext(); ) {
            final Artifact artifact = (Artifact) it.next();
            final String artifactId = artifact.getDependencyConflictId();
            log.debug("artifactId '" + artifactId + "'");
            if (id.equals(artifactId)) {
                return artifact;
            }
        }
        return null;
    }

    MavenProject readMavenProjectForModule(final File basedir, final String module) throws MojoExecutionException {
        final File f = new File(basedir, module + "/pom.xml");
        try {
            Model model = mavenProjectBuilder.build(f, localRepository, null).getModel();
            return new MavenProject(model);
        } catch (ProjectBuildingException e) {
            throw new MojoExecutionException("Unable to read/parse POM of module '" + f + "'", e);
        }
    }

    public DependencyHandler getDependencyHandler() {
        return dependencyHandler;
    }

    public void setDependencyHandler(DependencyHandler dependencyHandler) {
        this.dependencyHandler = dependencyHandler;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public void setMavenProjectBuilder(MavenProjectBuilder mavenProjectBuilder) {
        this.mavenProjectBuilder = mavenProjectBuilder;
    }

    public void setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    private void handleDependencyArtifact(final MavenProject project, final Artifact rootArtifact) {
        Map managedVersions = null;
        ResolutionListener storingListener = new ResolutionListener() {

            private Artifact currentArtifact = rootArtifact;

            public void endProcessChildren(Artifact artifact) {
                this.currentArtifact = null;
            }

            public void includeArtifact(Artifact artifact) {
                if (this.currentArtifact != artifact) {
                    dependencyHandler.addDependency(this.currentArtifact, artifact);
                }
            }

            public void manageArtifact(Artifact arg0, Artifact arg1) {
            }

            public void omitForCycle(Artifact arg0) {
            }

            public void omitForNearer(Artifact arg0, Artifact arg1) {
            }

            public void restrictRange(Artifact arg0, Artifact arg1, VersionRange arg2) {
            }

            public void selectVersionFromRange(Artifact arg0) {
            }

            public void startProcessChildren(Artifact artifact) {
                this.currentArtifact = artifact;
            }

            public void testArtifact(Artifact arg0) {
            }

            public void updateScope(Artifact arg0, String arg1) {
            }

            public void updateScopeCurrentPom(Artifact arg0, String arg1) {
            }
        };
        final List<ResolutionListener> listeners;
        if (!DEBUG) {
            listeners = Collections.<ResolutionListener>singletonList(storingListener);
        } else {
            listeners = Collections.<ResolutionListener>singletonList(new DebugResolutionListener(log));
        }
        try {
            artifactCollector.collect(Collections.singleton(rootArtifact), rootArtifact, localRepository, project.getRemoteArtifactRepositories(), artifactMetadataSource, null, listeners);
        } catch (ArtifactResolutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void setArtifactMetadataSource(ArtifactMetadataSource artifactMetadataSource) {
        this.artifactMetadataSource = artifactMetadataSource;
    }

    public void setArtifactCollector(ArtifactCollector artifactCollector) {
        this.artifactCollector = artifactCollector;
    }
}
