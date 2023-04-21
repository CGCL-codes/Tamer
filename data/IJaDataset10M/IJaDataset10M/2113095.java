package net.sf.buildbox.maven.installer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.net.URL;
import net.sf.buildbox.installer.bean.InstallerBean;
import net.sf.buildbox.installer.bean.InstallerDocument;
import net.sf.buildbox.installer.bean.LauncherBean;
import net.sf.buildbox.installer.bean.PathItemBean;
import net.sf.buildbox.strictlogging.api.*;
import net.sf.buildbox.strictlogging.maven.MavenLoggingFactory;
import net.sf.buildbox.util.BbxClassLoaderUtils;
import net.sf.buildbox.maven.MojoUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.xmlbeans.XmlOptions;

/**
 * <p>Packages an installable bundle.</p>
 *
 * @author <a href="mailto:pkozelka@gmail.com">Petr Kozelka</a>
 * @phase package
 * @goal make
 * @requiresProject true
 * @requiresDependencyResolution runtime
 */
public class MakeMojo extends AbstractMojo {

    private static final Catalog CAT = StrictCatalogFactory.getCatalog(Catalog.class);

    private final StrictLogger logger = MavenLoggingFactory.mojoLogger(this);

    /**
     * Tool launcher definitions.
     *
     * @parameter
     */
    LauncherDef[] launchers;

    /**
     * Classpath definitions.
     *
     * @parameter default-value="${project.build.directory}/installer/launchers.xml"
     */
    File outputFile;

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * Used to resolve artifacts of aggregated modules
     *
     * @component
     */
    ArtifactMetadataSource artifactMetadataSource;

    /**
     * Used for resolving artifacts
     *
     * @component
     */
    ArtifactResolver resolver;

    /**
     * The local repository where the artifacts are located
     *
     * @parameter expression="${localRepository}"
     */
    ArtifactRepository localRepository;

    /**
     * The remote repositories where artifacts are located
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     */
    List<ArtifactRepository> remoteRepositories;

    /**
     * Dependency artifacts of this plugin.
     *
     * @parameter expression="${plugin.artifacts}"
     * @readonly
     */
    List<Artifact> pluginArtifacts;

    /**
     * @component
     */
    ArtifactFactory artifactFactory;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final Map<String, String> artifactVersions = new HashMap<String, String>();
        for (Object o : project.getArtifacts()) {
            final Artifact a = (Artifact) o;
            if (!Artifact.SCOPE_RUNTIME.equals(a.getScope())) continue;
            final String ref = a.getGroupId() + ":" + a.getArtifactId();
            artifactVersions.put(ref, a.getVersion());
        }
        for (Artifact a : pluginArtifacts) {
            final String ref = a.getGroupId() + ":" + a.getArtifactId();
            artifactVersions.put(ref, a.getVersion());
        }
        if (launchers != null) {
            try {
                outputFile.getParentFile().mkdirs();
                final InstallerDocument doc = InstallerDocument.Factory.newInstance();
                final InstallerBean installerBean = doc.addNewInstaller();
                final String tk = project.getGroupId() + "_" + project.getArtifactId();
                final String toolkit = tk.toUpperCase().replaceAll("[-\\.]", "_");
                installerBean.setToolkit(toolkit);
                installerBean.setBundledBy(MojoUtils.getInstallerId());
                for (LauncherDef launcherDef : launchers) {
                    logger.log(CAT.pathDef(launcherDef.getId(), launcherDef.isTransitive()));
                    final Set<Artifact> primaryArtifacts = new LinkedHashSet<Artifact>();
                    final Set<Artifact> resultArtifacts = new LinkedHashSet<Artifact>();
                    for (String ga : launcherDef.getClasspath()) {
                        logger.log(CAT.locating(ga));
                        final Artifact a = locateArtifact(artifactVersions, ga);
                        if (launcherDef.isTransitive()) {
                            primaryArtifacts.add(a);
                        } else {
                            if (!isMyself(a)) {
                                resolve(a, false);
                            }
                            resultArtifacts.add(a);
                        }
                    }
                    final Set<Artifact> primaryArtifactsReduced = new LinkedHashSet<Artifact>();
                    boolean usingSelf = false;
                    for (Artifact primaryArtifact : primaryArtifacts) {
                        if (isMyself(primaryArtifact)) {
                            usingSelf = true;
                            continue;
                        }
                        primaryArtifactsReduced.add(primaryArtifact);
                    }
                    if (launcherDef.isTransitive()) {
                        try {
                            logger.log(CAT.resolving(primaryArtifacts.size()));
                            final ArtifactResolutionResult rv = resolver.resolveTransitively(primaryArtifactsReduced, project.getArtifact(), remoteRepositories, localRepository, artifactMetadataSource);
                            @SuppressWarnings("unchecked") final Set<Artifact> result = rv.getArtifacts();
                            resultArtifacts.addAll(result);
                            resultArtifacts.addAll(primaryArtifactsReduced);
                            logger.log(CAT.resolved(primaryArtifacts.size(), result.size()));
                        } catch (ArtifactResolutionException e) {
                            throw new MojoExecutionException(launcherDef.getId() + ": transitive resolving failed", e);
                        } catch (ArtifactNotFoundException e) {
                            throw new MojoExecutionException(launcherDef.getId() + ": artifact not found", e);
                        }
                    }
                    final LauncherBean launcherBean = installerBean.addNewLauncher();
                    launcherBean.setId(launcherDef.getId());
                    String mainclass = launcherDef.getMainClass();
                    if (mainclass == null) {
                        final String f = resultArtifacts.iterator().next().getFile().getCanonicalPath();
                        final String extraSlash = f.startsWith("/") ? "" : "/";
                        final URL url = new URL("jar:file://" + extraSlash + f + "!/META-INF/MANIFEST.MF");
                        final InputStream is = url.openStream();
                        try {
                            final Manifest manifest = new Manifest(is);
                            final Object mco = manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS);
                            if (mco == null) {
                                throw new MojoFailureException(String.format("No mainclass specified for %s and none found in manifest of %s", launcherDef.getId(), url));
                            }
                            mainclass = mco.toString();
                            logger.log(CAT.mainclassAuto(launcherDef.getId(), mainclass, url));
                        } finally {
                            is.close();
                        }
                    }
                    launcherBean.setMainclass(mainclass);
                    if (usingSelf) {
                        final PathItemBean el = launcherBean.addNewPathitem();
                        final Artifact ra = project.getArtifact();
                        el.setGroupId(ra.getGroupId());
                        el.setArtifactId(ra.getArtifactId());
                        el.setVersion(ra.getVersion());
                        el.setType(ra.getType());
                    }
                    for (Artifact ra : resultArtifacts) {
                        final PathItemBean el = launcherBean.addNewPathitem();
                        el.setGroupId(ra.getGroupId());
                        el.setArtifactId(ra.getArtifactId());
                        if (Artifact.SCOPE_SYSTEM.equals(ra.getScope())) {
                            el.setVersion("SYSTEM");
                        } else {
                            el.setVersion(ra.getVersion());
                        }
                        final String classifier = ra.getClassifier();
                        if (classifier != null) {
                            el.setClassifier(classifier);
                        }
                        el.setType(ra.getType());
                    }
                }
                final XmlOptions options = new XmlOptions();
                options.setCharacterEncoding("UTF-8");
                options.setSavePrettyPrintIndent(2);
                options.setUseDefaultNamespace();
                options.setSavePrettyPrint();
                doc.save(outputFile, options);
                logger.log(CAT.output(outputFile));
            } catch (IOException e) {
                throw new MojoFailureException("writing to " + outputFile, e);
            }
        }
        final String installerVersion = BbxClassLoaderUtils.lookupManifestByGA("net.sf.buildbox.maven", "installer").getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        logger.log(CAT.shell(installerVersion, project.getGroupId(), project.getArtifactId(), project.getVersion()));
    }

    private boolean isMyself(Artifact artifact) {
        return artifact.getGroupId().equals(project.getGroupId()) && artifact.getArtifactId().equals(project.getArtifactId());
    }

    private Artifact locateArtifact(Map<String, String> validArtifacts, String ref) throws MojoFailureException, MojoExecutionException {
        final int n = ref.indexOf(':');
        if (n < 1) {
            throw new MojoFailureException("Element reference does not match '<groupId>:<artifactId>' notation: " + ref);
        }
        final String groupId = ref.substring(0, n);
        final String artifactId = ref.substring(n + 1);
        if (groupId.equals(project.getGroupId()) && artifactId.equals(project.getArtifactId())) {
            return project.getArtifact();
        }
        final String ga = groupId + ":" + artifactId;
        final String version = validArtifacts.get(ga);
        if (version == null) {
            throw new MojoFailureException("Unknown artifact: " + ga);
        }
        final String artifactType = "jar";
        final String artifactClassifier = null;
        return artifactFactory.createArtifactWithClassifier(groupId, artifactId, version, artifactType, artifactClassifier);
    }

    private boolean resolve(Artifact artifact, boolean optional) throws MojoExecutionException {
        try {
            resolver.resolve(artifact, remoteRepositories, localRepository);
            return true;
        } catch (ArtifactResolutionException e) {
            if (optional) {
                return false;
            }
            throw new MojoExecutionException("Resolving failed: " + artifact, e);
        } catch (ArtifactNotFoundException e) {
            if (optional) {
                return false;
            }
            throw new MojoExecutionException("Artifact not found: " + artifact, e);
        }
    }

    private static interface Catalog extends StrictCatalog {

        @StrictCatalogEntry(severity = Severity.INFO, format = "mvn net.sf.buildbox.maven:installer:%s:install -Dwhat=%s:%s:%s")
        LogMessage shell(String installerVersion, String groupId, String artifactId, String version);

        @StrictCatalogEntry(severity = Severity.INFO, format = "Processing pathdef %s (transitive: %s)")
        LogMessage pathDef(String id, boolean transitive);

        @StrictCatalogEntry(severity = Severity.INFO, format = "locating %s")
        LogMessage locating(String ga);

        @StrictCatalogEntry(severity = Severity.INFO, format = "computing transitive closure on %d primary artifacts")
        LogMessage resolving(int artifactCount);

        @StrictCatalogEntry(severity = Severity.INFO, format = "%d primary artifacts resolved to %d artifacts")
        LogMessage resolved(int primaryCount, int resolvedCount);

        @StrictCatalogEntry(severity = Severity.INFO, format = "Resolved path elements stored in file %s")
        LogMessage output(File outputFile);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "Autodetected mainclass for %s is '%s' - from %s")
        LogMessage mainclassAuto(String id, String mainclass, URL url);
    }
}
