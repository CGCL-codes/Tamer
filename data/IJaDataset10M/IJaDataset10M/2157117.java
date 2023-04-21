package org.lambkin.maven.plugin;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.archiver.war.WarArchiver;
import org.lamb.utils.FileUtils;

/**
 * @goal package
 * @requiresDependencyResolution runtime
 */
public class PackageMojo extends AbstractLambkinMojo {

    /**
   * @parameter expression="${project.build.directory}/${project.artifactId}.war"
   */
    private File warFile;

    /**
   * @parameter expression="${project.build.directory}"
   */
    private File target;

    /**
   * The directory where the built is placed.
   *
   * @parameter expression="${project.build.directory}/${project.build.finalName}"
   * @required
   */
    private File buildDirectory;

    /**
   * Single directory for extra files to include in the WAR.
   *
   * @parameter expression="${basedir}/src/main/webapp"
   * @required
   */
    private File webappDirectory;

    /**
   * The directory containing generated classes.
   *
   * @parameter expression="${project.build.outputDirectory}"
   * @required
   * @readonly
   */
    private File classesDirectory;

    protected void doValidate() throws Exception {
        System.out.println("\n*************************\tBegin doValidate()\t*************************\n");
        getLog().info("warFile = " + warFile.getAbsolutePath());
        getLog().info("target = " + target.getAbsolutePath());
        getLog().info("buildDirectory = " + buildDirectory.getAbsolutePath());
        getLog().info("classesDirectory = " + classesDirectory.getAbsolutePath());
        getLog().info("webappDirectory = " + webappDirectory.getAbsolutePath());
        System.out.println("\n\n*************************\tEnd doValidate()\t*************************\n");
    }

    protected void doExecute() throws Exception {
        System.out.println("\n*************************\tBegin doExecute()\t*************************\n");
        File webINF = new File(webappDirectory, "WEB-INF");
        FileUtils.copyDirectory(classesDirectory, webINF);
        Set artifacts = project.getArtifacts();
        File libDir = new File(webINF, "lib");
        for (Iterator iter = artifacts.iterator(); iter.hasNext(); ) {
            Artifact artifact = (Artifact) iter.next();
            FileUtils.copyFileToDirectory(artifact.getFile(), libDir);
        }
        MavenArchiver archiver = new MavenArchiver();
        WarArchiver warArchiver = new WarArchiver();
        archiver.setArchiver(warArchiver);
        archiver.setOutputFile(warFile);
        warArchiver.addDirectory(webappDirectory);
        warArchiver.setWebxml(new File(webappDirectory, "WEB-INF/web.xml"));
        archiver.createArchive(project, new MavenArchiveConfiguration());
        project.getArtifact().setFile(warFile);
        System.out.println("\n\n*************************\tEnd doExecute()\t*************************\n");
    }
}
