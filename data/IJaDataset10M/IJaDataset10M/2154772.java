package org.codehaus.mojo.retrotranslator.ext;

import java.io.File;
import net.sf.retrotranslator.transformer.Retrotranslator;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.retrotranslator.RetrotranslateMojoSupport;
import org.codehaus.plexus.archiver.ear.EarArchiver;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * Retrotranslates the classes in the ear, as well as all jars at EAR root. 
 * Creates a new ear with the specified classifier with these retrotranslations.
 *
 * @goal translate-ear
 * @phase package
 */
public class TranslateEarMojo extends RetrotranslateMojoSupport {

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * @component
     * @required
     * @readonly
     */
    protected MavenProjectHelper projectHelper;

    /**
     * Where to put the translated artifact.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    protected File outputDirectory;

    /**
     * The base-name of the generated artifact.
     *
     * @parameter expression="${project.build.finalName}"
     * @required
     */
    protected String baseName;

    /**
     * Flag to enable/disable attaching retrotranslated artifacts.
     *
     * @parameter expression="${attach}" default-value="true"
     */
    protected boolean attach;

    /**
     * The classifier used when attaching the retrotranslated project artifact.
     *
     * @parameter expression="${classifier}" default-value="jdk14"
     */
    protected String classifier;

    /**
	 * A set of jar files to include in the translation. 
	 * Note: any basedir will be ignored and reset to EAR root
	 *
	 * @parameter
	 */
    protected DirectoryScanner jarfileset;

    protected File transformedEarDir;

    /**
     * The File where .
     *
     * @parameter default-value="${basedir}/../web/target"
     */
    protected File transformedWarDir;

    /**
     * The classifier used when attaching the retrotranslated project artifact.
     *
     * @parameter expression="${warname}"
     * @required
     */
    protected String warname;

    protected File transformedWarFile;

    /**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#ear}"
	 * @required
	 * @readonly
	 */
    protected EarArchiver earArchiver;

    /**
	 * <p></p>
	 * 
	 * @see org.codehaus.mojo.retrotranslator.RetrotranslateMojoSupport#doExecute()
	 */
    protected void doExecute() throws Exception {
        log.info("Project :: " + project.getArtifactId());
        if (!"ear".equals(project.getPackaging())) {
            log.info("Not executing on non-EAR project");
            return;
        }
        final File earDir = new File(outputDirectory, project.getBuild().getFinalName());
        if (!earDir.exists() || !earDir.isDirectory()) {
            throw new MojoExecutionException("Invalid Ear build directory: " + earDir);
        }
        transformedEarDir = new File(outputDirectory, baseName + "-" + classifier);
        FileUtils.copyDirectoryStructure(earDir, transformedEarDir);
        transformedWarFile = new File(transformedWarDir, warname + "-" + project.getVersion() + "-" + classifier + ".war");
        FileUtils.copyFileToDirectory(transformedWarFile, transformedEarDir);
        FileUtils.forceDelete(new File(transformedEarDir, warname + "-" + project.getVersion() + ".war"));
        super.doExecute();
        final File outEar = new File(outputDirectory, baseName + "-" + classifier + ".ear");
        final MavenArchiveConfiguration archive = new MavenArchiveConfiguration();
        archive.setAddMavenDescriptor(true);
        final MavenArchiver archiver = new MavenArchiver();
        archiver.setArchiver(earArchiver);
        archiver.setOutputFile(outEar);
        earArchiver.addDirectory(transformedEarDir);
        earArchiver.setAppxml(new File(transformedEarDir, "META-INF/application.xml"));
        archiver.createArchive(project, archive);
        if (attach) {
            projectHelper.attachArtifact(project, "ear", classifier, outEar);
        }
    }

    /**
	 * <p></p>
	 * 
	 * @see org.codehaus.mojo.retrotranslator.RetrotranslateMojoSupport#configureRetrotranslator(net.sf.retrotranslator.transformer.Retrotranslator)
	 */
    protected void configureRetrotranslator(final Retrotranslator retrotranslator) {
        retrotranslator.addSrcdir(new File(outputDirectory, "classes"));
        if (jarfileset == null) {
            jarfileset = new DirectoryScanner();
            jarfileset.setIncludes(new String[] { "*.jar" });
        }
        jarfileset.setBasedir(transformedEarDir);
        jarfileset.scan();
        final String[] jarFiles = jarfileset.getIncludedFiles();
        for (int i = 0; i < jarFiles.length; i++) {
            retrotranslator.addSrcjar(new File(jarfileset.getBasedir(), jarFiles[i]));
        }
    }
}
