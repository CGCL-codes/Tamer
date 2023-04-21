package net.sf.buildbox.maven.installer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.buildbox.installer.bean.InstallerBean;
import net.sf.buildbox.installer.bean.InstallerDocument;
import net.sf.buildbox.installer.bean.LauncherBean;
import net.sf.buildbox.installer.bean.PathItemBean;
import net.sf.buildbox.maven.MojoUtils;
import net.sf.buildbox.installer.platform.Linux;
import net.sf.buildbox.strictlogging.api.*;
import net.sf.buildbox.strictlogging.maven.MavenLoggingFactory;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.shared.runtime.MavenProjectProperties;
import org.apache.maven.shared.runtime.MavenRuntime;
import org.apache.maven.shared.runtime.MavenRuntimeException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * <p>Installs a prepackaged bundle.</p>
 *
 * @author <a href="mailto:pkozelka@gmail.com">Petr Kozelka</a>
 * @goal install2009
 * @requiresProject false
 */
public class InstallMojo extends AbstractInstallMojo {

    private static final Catalog CAT = StrictCatalogFactory.getCatalog(Catalog.class);

    private final StrictLogger logger = MavenLoggingFactory.mojoLogger(this);

    /**
     * Where to install
     *
     * @parameter expression="${where}"
     * @readonly
     */
    File where;

    /**
     * <p>What to install, using this notation:</p>
     * <p><code>groupId:artifactId:version</code></p>
     * <p>If not specified, installs current project (support for installation during "install" phase)</p>
     *
     * @parameter expression="${what}"
     * @readonly
     */
    String what;

    /**
     * <p>Whether to reuse Maven repository for classpath elements in runtime.</p>
     * <p>If false, a subdirectory "lib" will be created and receives copy of libraries needed for all tools.</p>
     *
     * @parameter expression="${shareLibs}" default-value="false"
     */
    boolean shareLibs;

    /**
     * @component
     */
    MavenRuntime runtime;

    File installerFile;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            final MavenProjectProperties p = runtime.getProjectProperties(getClass());
            logger.log(CAT.splash(p.getGroupId(), p.getArtifactId(), p.getVersion()));
        } catch (MavenRuntimeException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        logger.log(CAT.repositories(localRepository, remoteRepositories));
        final File installRoot = new File(System.getProperty("user.home"), ".buildbox/opt");
        final Artifact artifact = getPlatformSpecificArtifact(what);
        logger.log(CAT.what(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()));
        if (where == null) {
            where = new File(installRoot, String.format("%s/%s/%s", artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion()));
        }
        try {
            resolve(artifact, false);
            logger.log(CAT.where(where));
            final File base = where.getParentFile();
            base.mkdirs();
            final File whereTmp = File.createTempFile(artifact.getVersion() + ".", ".tmp", base);
            whereTmp.delete();
            whereTmp.mkdirs();
            MojoUtils.unpackFile(logger.getSubLogger("unpack"), artifact.getFile(), whereTmp);
            installerFile = new File(whereTmp, "launchers.xml");
            if (installerFile.exists()) {
                final InstallerDocument doc = InstallerDocument.Factory.parse(installerFile);
                final File libPath = shareLibs ? null : new File(whereTmp, "lib");
                preloadArtifacts(doc, libPath);
                generateCp(whereTmp);
            }
            if (platform instanceof Linux) {
                final File rc = linux_generateRcScript(artifact, base, whereTmp, where);
                if (rc != null) {
                    logger.debug("Generated init script: %s", rc);
                }
            }
            if (where.exists()) {
                final File bakdir = new File(where.getAbsolutePath() + ".bak");
                if (bakdir.exists()) {
                    logger.log(CAT.deletingBak(bakdir));
                    FileUtils.deleteDirectory(bakdir);
                }
                logger.log(CAT.backup(where, bakdir));
                FileUtils.rename(where, bakdir);
            }
            if (where.exists()) {
                FileUtils.deleteDirectory(where);
            }
            logger.log(CAT.commit(whereTmp, where));
            if (!whereTmp.renameTo(where)) {
                FileUtils.copyDirectoryStructure(whereTmp, where);
                FileUtils.deleteDirectory(whereTmp);
            }
            final File currentVersionSymlinkFile = new File(base, "current");
            currentVersionSymlinkFile.delete();
            platform.symlink(currentVersionSymlinkFile, where);
            logger.log(platform.instructions(base));
        } catch (IOException e) {
            throw new MojoExecutionException("failed to resolve", e);
        } catch (ArchiverException e) {
            throw new MojoExecutionException("failed to unpack", e);
        } catch (TransformerException e) {
            throw new MojoExecutionException("failed to transform", e);
        } catch (org.apache.xmlbeans.XmlException e) {
            throw new MojoExecutionException("failed to parse " + installerFile, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public File linux_generateRcScript(Artifact artifact, File base, File whereTmp, File where) throws IOException {
        final String ga = artifact.getGroupId() + "_" + artifact.getArtifactId();
        final String toolkit = ga.toUpperCase().replace('.', '_');
        final File rc = new File(whereTmp, ".rc");
        final File appData = new File(base, "data");
        FileUtils.fileWrite(rc.getAbsolutePath(), String.format("%s_HOME=%s\n%1$s_DATA=%3$s\nsource ${%1$s_HOME}/bin/linux/.rc\n", toolkit, where, appData));
        return rc;
    }

    private void preloadArtifacts(InstallerDocument doc, File libPath) throws MojoExecutionException, IOException {
        final InstallerBean inst = doc.getInstaller();
        for (LauncherBean cp : inst.getLauncherArray()) {
            logger.log(CAT.preloading(cp.getId()));
            for (PathItemBean cpe : cp.getPathitemArray()) {
                if ("SYSTEM".equals(cpe.getVersion())) continue;
                final Artifact a = artifactFactory.createArtifactWithClassifier(cpe.getGroupId(), cpe.getArtifactId(), cpe.getVersion(), cpe.getType(), cpe.getClassifier());
                resolve(a, false);
                if (libPath != null) {
                    String classifierSuffix = a.getClassifier();
                    if (classifierSuffix != null && !classifierSuffix.equals("")) {
                        classifierSuffix = "-" + classifierSuffix;
                    }
                    final File libFile = new File(libPath, String.format("%s-%s-%s%s.%s", a.getGroupId(), a.getArtifactId(), a.getVersion(), classifierSuffix, a.getType()));
                    libFile.getParentFile().mkdirs();
                    logger.debug("Copying %s to %s", a.getFile(), libFile);
                    FileUtils.copyFile(a.getFile(), libFile);
                }
            }
        }
    }

    private void generateCp(File whereTmp) throws TransformerException, IOException {
        final String xslFile = "installer-" + platform.CLASSIFIER + ".xsl";
        final File of = new File(whereTmp, "VERSION.txt");
        logger.log(CAT.generating(installerFile, xslFile, of));
        final Transformer t = MojoUtils.getTransformer(new StreamSource(InstallMojo.class.getResourceAsStream(xslFile)));
        of.getParentFile().mkdirs();
        final OutputStream os = new FileOutputStream(of);
        try {
            t.setParameter("installedBy", MojoUtils.getInstallerId());
            t.setParameter("shareLibs", shareLibs);
            t.setParameter("tmpDir", whereTmp);
            t.setParameter("installDir", where);
            t.transform(new StreamSource(installerFile), new StreamResult(os));
        } finally {
            os.close();
        }
    }

    private boolean resolve(Artifact artifact, boolean optional) throws MojoExecutionException {
        try {
            logger.log(CAT.resolving(artifact));
            resolver.resolve(artifact, remoteRepositories, localRepository);
            logger.log(CAT.resolved(artifact, artifact.getFile()));
            return true;
        } catch (ArtifactResolutionException e) {
            if (optional) {
                logger.log(CAT.unresolved(artifact, e.getClass().getName(), e.getMessage()));
                return false;
            }
            throw new MojoExecutionException("Resolving failed: " + artifact, e);
        } catch (ArtifactNotFoundException e) {
            if (optional) {
                logger.log(CAT.unresolved(artifact, e.getClass().getName(), e.getMessage()));
                return false;
            }
            throw new MojoExecutionException("Artifact not found: " + artifact, e);
        }
    }

    private static interface Catalog extends StrictCatalog {

        @StrictCatalogEntry(severity = Severity.INFO, format = "%s:%s - Maven Shell Installer version %s by Petr Kozelka <pkozelka@gmail.com>")
        LogMessage splash(String groupId, String artifactId, String version);

        @StrictCatalogEntry(severity = Severity.INFO, format = "Installing %s:%s:%s")
        LogMessage what(String groupId, String artifactId, String version);

        @StrictCatalogEntry(severity = Severity.INFO, format = "Destination local directory: '%s'")
        LogMessage where(File where);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "local: %s  remote: %s")
        LogMessage repositories(ArtifactRepository what, List<ArtifactRepository> where);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "Resolving artifact: %s")
        LogMessage resolving(Artifact artifact);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "Resolved artifact: %s = %s")
        LogMessage resolved(Artifact artifact, File file);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "Failed to resolve optional artifact %s due to %s: %s")
        LogMessage unresolved(Artifact artifact, String exceptionClassName, String message);

        @StrictCatalogEntry(severity = Severity.DEBUG, format = "Deleting older backup directory '%s'")
        LogMessage deletingBak(File bakdir);

        @StrictCatalogEntry(severity = Severity.INFO, format = "storing backup copy of directory '%s' to '%s'")
        LogMessage backup(File where, File bakdir);

        @StrictCatalogEntry(severity = Severity.INFO, format = "moving successful installation from '%s' to '%s'")
        LogMessage commit(File tmpLocation, File where);

        @StrictCatalogEntry(severity = Severity.INFO, format = "preloading classpath elements for '%s'")
        LogMessage preloading(String id);

        @StrictCatalogEntry(severity = Severity.INFO, format = "%s::%s --> %s")
        LogMessage generating(File xmlInput, String xslFile, File outputFile);
    }
}
