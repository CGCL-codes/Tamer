package org.ops4j.pax.construct.lifecycle;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.IOUtil;
import org.ops4j.pax.construct.util.PomUtils;
import org.ops4j.pax.construct.util.StreamFactory;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Provision all local and imported bundles onto the selected OSGi framework
 *
 * <code><pre>
 *   mvn pax:provision [-Dframework=felix|equinox|kf|concierge] [-Dprofiles=log,war,spring,...]
 * </pre></code>
 *
 * If you don't have Pax-Runner in your local Maven repository this command will
 * automatically attempt to download the latest release. It will then continue
 * to use this locally installed version of Pax-Runner unless you add
 * <code>-U</code> to force it to check online for a later release, or
 * <code>-Drunner=version</code> to temporarily use a different version.
 *
 * @goal provision-obr
 * @aggregator true
 *
 * @requiresProject false
 * @requiresDependencyResolution test
 */
public class ProvisionObrMojo extends AbstractMojo {

    /**
     * Maven groupId for the new Pax-Runner
     */
    private static final String PAX_RUNNER_GROUP = "org.ops4j.pax.runner";

    /**
     * Maven artifactId for the new Pax-Runner
     */
    private static final String PAX_RUNNER_ARTIFACT = "pax-runner";

    /**
     * Main entry-point for the new Pax-Runner
     */
    private static final String PAX_RUNNER_METHOD = "org.ops4j.pax.runner.Run";

    /**
     * Accumulated set of bundles to be deployed
     */
    private static List m_bundleIds;

    /**
     * Component for resolving Maven metadata
     *
     * @component
     */
    private ArtifactMetadataSource m_source;

    /**
     * Component factory for Maven artifacts
     *
     * @component
     */
    private ArtifactFactory m_factory;

    /**
     * Component for resolving Maven artifacts
     *
     * @component
     */
    private ArtifactResolver m_resolver;

    /**
     * Component for installing Maven artifacts
     *
     * @component
     */
    private ArtifactInstaller m_installer;

    /**
     * The local Maven settings.
     *
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    private Settings m_settings;

    /**
     * List of remote Maven repositories for the containing project.
     *
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @required
     * @readonly
     */
    private List m_remoteRepos;

    /**
     * The local Maven repository for the containing project.
     *
     * @parameter expression="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository m_localRepo;

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject m_project;

    /**
     * Name of the OSGi framework to deploy onto.
     *
     * @parameter expression="${framework}"
     */
    private String framework;

    /**
     * When true, start the OSGi framework and deploy the provisioned bundles.
     *
     * @parameter expression="${deploy}" default-value="true"
     */
    private boolean deploy;

    /**
     * Comma separated list of additional Pax-Runner profiles to deploy.
     *
     * @parameter expression="${profiles}"
     */
    private String profiles;

    /**
     * URL of file containing additional Pax-Runner arguments.
     *
     * @parameter expression="${args}"
     */
    private String args;

    /**
     * Comma separated list of additional bundle URLs to deploy.
     *
     * @parameter expression="${deployURLs}"
     */
    private String deployURLs;

    /**
     * The version of Pax-Runner to use for provisioning.
     *
     * @parameter expression="${runner}" default-value="RELEASE"
     */
    private String runner;

    /**
     * A set of provision commands for Pax-Runner.
     *
     * @parameter expression="${provision}"
     */
    private String[] provision;

    /**
     * Component factory for Maven repositories.
     *
     * @component
     */
    private ArtifactRepositoryFactory m_repoFactory;

    /**
     * @component roleHint="default"
     */
    private ArtifactRepositoryLayout m_defaultLayout;

    /**
     * Component for calculating mirror details.
     *
     * @component
     */
    private WagonManager m_wagonManager;

    /**
     * Runtime helper available on Maven 2.0.9 and above.
     */
    private Method m_getMirrorRepository;

    /**
     * Additional vmoptions to be appended.
     *
     * @parameter expression="${pax.vmoptions}"
     */
    private String paxVmOptions;

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        m_bundleIds = new ArrayList();
        setupRuntimeHelpers();
        StringBuilder addBundles = new StringBuilder();
        boolean firstBundle = true;
        Iterator i = m_project.getArtifacts().iterator();
        while (i.hasNext()) {
            Artifact art = (Artifact) i.next();
            try {
                if (!art.getScope().equals("test")) {
                    String jarUrl = "jar:file://" + art.getFile() + "!/";
                    URL url = new URL(jarUrl);
                    URLConnection uc = url.openConnection();
                    if (uc instanceof JarURLConnection) {
                        JarURLConnection jarurl = (JarURLConnection) uc;
                        JarFile jarfile = jarurl.getJarFile();
                        Attributes attr = jarfile.getManifest().getMainAttributes();
                        String bsymbol = attr.getValue("Bundle-SymbolicName");
                        if (bsymbol != null) {
                            if (!firstBundle) addBundles.append(','); else firstBundle = false;
                            int cleanupPos = bsymbol.indexOf(';');
                            if (cleanupPos > -1) {
                                bsymbol = bsymbol.substring(0, cleanupPos);
                            }
                            addBundles.append(makeBootstrapString(bsymbol, art.getVersion()));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!firstBundle) addBundles.append(','); else firstBundle = false;
        Artifact art = m_project.getArtifact();
        String symbolic = m_project.getProperties().getProperty("bundle.symbolicName");
        if (symbolic == null) System.out.println("Property \"bundle.symbolicName\" for this project is missing, ignoring this bundle!\n"); else addBundles.append(makeBootstrapString(symbolic, art.getVersion()));
        if (!firstBundle) addBundles.append(','); else firstBundle = false;
        String version;
        version = m_project.getProperties().getProperty("coos.version");
        if (version == null) version = m_project.getVersion();
        version = version.replace('-', '.');
        addBundles.append("(&(symbolicname=org.coosproject.messaging.impl)(version>=" + version + "))");
        deployBundles("file://" + m_localRepo.getBasedir() + "/repository.xml", addBundles.toString());
    }

    private String makeBootstrapString(String sn, String v) {
        return "(&(symbolicname=" + sn + ")(version>=" + v.replace('-', '.') + "))";
    }

    /**
     * Use reflection to find if certain runtime helper methods are available.
     */
    private void setupRuntimeHelpers() {
        try {
            m_getMirrorRepository = m_wagonManager.getClass().getMethod("getMirrorRepository", new Class[] { ArtifactRepository.class });
        } catch (RuntimeException e) {
            m_getMirrorRepository = null;
        } catch (NoSuchMethodException e) {
            m_getMirrorRepository = null;
        }
    }

    /**
     * Does this look like a provisioning POM? ie. artifactId of 'provision',
     * packaging type 'pom', with dependencies
     *
     * @param project
     *            a Maven project
     * @return true if this looks like a provisioning POM, otherwise false
     */
    public static boolean isProvisioningPom(MavenProject project) {
        if (!"provision".equals(project.getArtifactId())) {
            return false;
        }
        if (!"pom".equals(project.getPackaging())) {
            return false;
        }
        List dependencies = project.getDependencies();
        if ((dependencies == null) || (dependencies.size() == 0)) {
            return false;
        }
        return true;
    }

    /**
     * Create deployment POM and pass it onto Pax-Runner for provisioning
     *
     * @throws MojoExecutionException
     */
    private void deployBundles(String obrs, String resolves) throws MojoExecutionException {
        if (m_bundleIds.size() == 0) {
            getLog().info("~~~~~~~~~~~~~~~~~~~");
            getLog().info(" No bundles found! ");
            getLog().info("~~~~~~~~~~~~~~~~~~~");
        }
        List bundles = resolveProvisionedBundles();
        MavenProject deployProject = createDeploymentProject(bundles);
        installDeploymentPom(deployProject);
        if (!deploy) {
            getLog().info("Skipping OSGi deployment");
            return;
        }
        String delim = "";
        StringBuffer repoListBuilder = new StringBuffer();
        for (Iterator i = m_remoteRepos.iterator(); i.hasNext(); ) {
            repoListBuilder.append(delim);
            ArtifactRepository repo = (ArtifactRepository) i.next();
            repoListBuilder.append(getRepositoryURL(repo));
            if (repo.getSnapshots().isEnabled()) {
                repoListBuilder.append("@snapshots");
            }
            if (false == repo.getReleases().isEnabled()) {
                repoListBuilder.append("@noreleases");
            }
            delim = ",";
        }
        if (PomUtils.needReleaseVersion(runner)) {
            Artifact runnerProject = m_factory.createProjectArtifact(PAX_RUNNER_GROUP, PAX_RUNNER_ARTIFACT, runner);
            runner = PomUtils.getReleaseVersion(runnerProject, m_source, m_remoteRepos, m_localRepo, null);
        }
        Pattern classicVersion = Pattern.compile("0\\.[1-4]\\.\\d");
        if (classicVersion.matcher(runner).matches()) {
            Class clazz = loadRunnerClass("org.ops4j.pax", "runner", PAX_RUNNER_METHOD, false);
            deployRunnerClassic(clazz, deployProject, repoListBuilder.toString());
        } else {
            Class clazz = loadRunnerClass(PAX_RUNNER_GROUP, PAX_RUNNER_ARTIFACT, PAX_RUNNER_METHOD, true);
            deployRunnerNG(clazz, deployProject, repoListBuilder.toString(), obrs, resolves);
        }
    }

    /**
     * @param repo
     *            remote Maven repository
     * @return repository (or mirror) URL
     */
    private String getRepositoryURL(ArtifactRepository repo) {
        if (null != m_getMirrorRepository) {
            try {
                ArtifactRepository mirror = (ArtifactRepository) m_getMirrorRepository.invoke(m_wagonManager, new Object[] { repo });
                if (null != mirror) {
                    return mirror.getUrl();
                }
            } catch (RuntimeException e) {
                getLog().warn(e);
            } catch (IllegalAccessException e) {
                getLog().warn(e);
            } catch (InvocationTargetException e) {
                getLog().warn(e);
            }
        }
        Mirror mirror = m_settings.getMirrorOf(repo.getId());
        if (null != mirror) {
            return mirror.getUrl();
        }
        mirror = m_settings.getMirrorOf("*");
        if (null != mirror) {
            return mirror.getUrl();
        }
        return repo.getUrl();
    }

    /**
     * Attempt to resolve each provisioned bundle, and warn about any we can't
     * find
     * org.apache.felix/org.apache.felix.bundlerepository/1.4.2
     * @return list of bundles to be deployed (as Maven dependencies)
     */
    private List resolveProvisionedBundles() {
        List dependencies = new ArrayList();
        Dependency dep = new Dependency();
        dep.setGroupId("org.coosproject.build-tools");
        dep.setArtifactId("obrbootstrapper");
        String version;
        version = m_project.getProperties().getProperty("coos.version");
        if (version == null) version = m_project.getVersion();
        dep.setVersion(version);
        dep.setType("jar");
        dep.setScope(Artifact.SCOPE_PROVIDED);
        dependencies.add(dep);
        dep = new Dependency();
        dep.setGroupId("org.apache.felix");
        dep.setArtifactId("org.apache.felix.bundlerepository");
        dep.setVersion("1.4.2");
        dep.setType("jar");
        dep.setScope(Artifact.SCOPE_PROVIDED);
        dependencies.add(dep);
        return dependencies;
    }

    /**
     * Create new POM (based on the root POM) which lists the deployed bundles
     * as dependencies
     *
     * @param bundles
     *            list of bundles to be deployed
     * @return deployment project
     * @throws MojoExecutionException
     */
    private MavenProject createDeploymentProject(List bundles) throws MojoExecutionException {
        MavenProject deployProject;
        if (null == m_project.getFile()) {
            deployProject = new MavenProject();
            deployProject.setGroupId("examples");
            deployProject.setArtifactId("pax-provision");
            deployProject.setVersion("1.0-SNAPSHOT");
        } else {
            deployProject = new MavenProject(m_project);
        }
        String internalId = PomUtils.getCompoundId(deployProject.getGroupId(), deployProject.getArtifactId());
        deployProject.setGroupId(internalId + ".build");
        deployProject.setArtifactId("deployment");
        deployProject.setPackaging("pom");
        deployProject.getModel().setModules(null);
        deployProject.getModel().setDependencies(bundles);
        deployProject.getModel().setPluginRepositories(null);
        deployProject.getModel().setReporting(null);
        deployProject.setBuild(null);
        File deployFile = new File(deployProject.getBasedir(), "runner/deploy-pom.xml");
        deployFile.getParentFile().mkdirs();
        deployProject.setFile(deployFile);
        try {
            Writer writer = StreamFactory.newXmlWriter(deployFile);
            deployProject.writeModel(writer);
            IOUtil.close(writer);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to write deployment POM " + deployFile);
        }
        return deployProject;
    }

    /**
     * Install deployment POM in the local Maven repository
     *
     * @param project
     *            deployment project
     * @throws MojoExecutionException
     */
    private void installDeploymentPom(MavenProject project) throws MojoExecutionException {
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        String version = project.getVersion();
        Artifact pomArtifact = m_factory.createProjectArtifact(groupId, artifactId, version);
        try {
            m_installer.install(project.getFile(), pomArtifact, m_localRepo);
        } catch (ArtifactInstallationException e) {
            throw new MojoExecutionException("Unable to install deployment POM " + pomArtifact);
        }
    }

    /**
     * Dynamically resolve and load the Pax-Runner class
     *
     * @param groupId
     *            pax-runner group id
     * @param artifactId
     *            pax-runner artifact id
     * @param mainClass
     *            main pax-runner classname
     * @param needClassifier
     *            classify pax-runner artifact according to current JVM
     * @return main pax-runner class
     * @throws MojoExecutionException
     */
    private Class loadRunnerClass(String groupId, String artifactId, String mainClass, boolean needClassifier) throws MojoExecutionException {
        String jdk = null;
        if (needClassifier && (System.getProperty("java.class.version").compareTo("49.0") < 0)) {
            jdk = "jdk14";
        }
        Artifact jarArtifact = m_factory.createArtifactWithClassifier(groupId, artifactId, runner, "jar", jdk);
        if (!PomUtils.downloadFile(jarArtifact, m_resolver, m_remoteRepos, m_localRepo)) {
            throw new MojoExecutionException("Unable to find Pax-Runner " + jarArtifact);
        }
        URL[] urls = new URL[1];
        try {
            urls[0] = jarArtifact.getFile().toURI().toURL();
        } catch (MalformedURLException e) {
            throw new MojoExecutionException("Bad Jar location " + jarArtifact.getFile());
        }
        try {
            ClassLoader loader = new URLClassLoader(urls);
            Thread.currentThread().setContextClassLoader(loader);
            return Class.forName(mainClass, true, loader);
        } catch (ClassNotFoundException e) {
            throw new MojoExecutionException("Unable to find entry point " + mainClass + " in " + urls[0]);
        }
    }

    /**
     * Deploy bundles using the 'classic' Pax-Runner
     *
     * @param mainClass
     *            main Pax-Runner class
     * @param project
     *            deployment project
     * @param repositories
     *            comma separated list of Maven repositories
     * @throws MojoExecutionException
     */
    private void deployRunnerClassic(Class mainClass, MavenProject project, String repositories) throws MojoExecutionException {
        String workDir = project.getBasedir() + "/runner";
        String cachedPomName = project.getArtifactId() + '_' + project.getVersion() + ".pom";
        File cachedPomFile = new File(workDir + "/lib/" + cachedPomName);
        cachedPomFile.delete();
        if (PomUtils.isEmpty(framework)) {
            framework = "felix";
        }
        String[] deployAppCmds = { "--dir=" + workDir, "--no-md5", "--platform=" + framework, "--profile=default", "--repository=" + repositories, "--localRepository=" + m_localRepo.getBasedir(), project.getGroupId(), project.getArtifactId(), project.getVersion() };
        invokePaxRunner(mainClass, deployAppCmds);
    }

    /**
     * This method allows subclasses to specify additional commands for
     * provisioning. By default provide no initial deploy commands.
     *
     * @return A List of Strings that represent the deploy commands.
     */
    protected List getDeployCommands() {
        return new ArrayList();
    }

    /**
     * Deploy bundles using the new Pax-Runner codebase
     *
     * @param mainClass
     *            main Pax-Runner class
     * @param project
     *            deployment project
     * @param repositories
     *            comma separated list of Maven repositories
     * @param resolves
     * @param obrs
     * @throws MojoExecutionException
     */
    private void deployRunnerNG(Class mainClass, MavenProject project, String repositories, String obrs, String resolves) throws MojoExecutionException {
        List deployAppCmds = getDeployCommands();
        String vmOptions = "--vmOptions=-DobrRepos=" + obrs + " -Dresources=" + resolves;
        if (paxVmOptions != null) vmOptions += " " + paxVmOptions;
        deployAppCmds.add(vmOptions);
        if (PomUtils.isNotEmpty(framework)) {
            deployAppCmds.add("--platform=" + framework);
        }
        if (PomUtils.isNotEmpty(profiles)) {
            deployAppCmds.add("--profiles=" + profiles);
        }
        if (PomUtils.isNotEmpty(args)) {
            try {
                new URL(args);
            } catch (MalformedURLException e) {
                File argsFile = new File(args);
                args = argsFile.toURI().toString();
            }
            deployAppCmds.add("--args=" + args);
        }
        deployAppCmds.addAll(Arrays.asList(provision));
        deployAppCmds.add(project.getFile().getAbsolutePath());
        if (PomUtils.isNotEmpty(deployURLs)) {
            String[] urls = deployURLs.split(",");
            for (int i = 0; i < urls.length; i++) {
                deployAppCmds.add(urls[i].trim());
            }
        }
        deployAppCmds.add("--localRepository=" + m_localRepo.getBasedir());
        deployAppCmds.add("--repositories=" + repositories);
        deployAppCmds.add("--overwriteUserBundles");
        getLog().debug("Starting Pax-Runner " + runner + " with: " + deployAppCmds.toString());
        invokePaxRunner(mainClass, (String[]) deployAppCmds.toArray(new String[deployAppCmds.size()]));
    }

    /**
     * Invoke Pax-Runner in-process
     *
     * @param mainClass
     *            main Pax-Runner class
     * @param commands
     *            array of command-line options
     * @throws MojoExecutionException
     */
    private void invokePaxRunner(Class mainClass, String[] commands) throws MojoExecutionException {
        Class[] paramTypes = new Class[1];
        paramTypes[0] = String[].class;
        Object[] paramValues = new Object[1];
        paramValues[0] = commands;
        try {
            Method entryPoint = mainClass.getMethod("main", paramTypes);
            entryPoint.invoke(null, paramValues);
        } catch (NoSuchMethodException e) {
            throw new MojoExecutionException("Unable to find Pax-Runner entry point");
        } catch (IllegalAccessException e) {
            throw new MojoExecutionException("Unable to access Pax-Runner entry point");
        } catch (InvocationTargetException e) {
            throw new MojoExecutionException("Pax-Runner exception", e);
        }
    }

    /**
     * @return backup OPS4J remote repository
     */
    ArtifactRepository getOps4jRepository() {
        ArtifactRepositoryPolicy noSnapshots = new ArtifactRepositoryPolicy(false, ArtifactRepositoryPolicy.UPDATE_POLICY_DAILY, null);
        ArtifactRepositoryPolicy releases = new ArtifactRepositoryPolicy(true, ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER, null);
        return m_repoFactory.createArtifactRepository("ops4j.releases", "http://repository.ops4j.org/maven2/", m_defaultLayout, noSnapshots, releases);
    }
}
