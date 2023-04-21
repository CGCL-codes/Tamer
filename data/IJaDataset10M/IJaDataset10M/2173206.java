package org.impalaframework.build.ant;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Checksum;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Echo;
import org.apache.tools.ant.taskdefs.LoadFile;

/**
 * Copies created artifacts from dist directory to Maven publish directory. Also
 * generates simple minimal POMs for each artifact.
 * 
 * @author Phil Zoio
 */
public class MavenPublishTask extends Task {

    private File sourceDir;

    private File destDir;

    private String artifacts;

    private String organisation;

    private File sharedPomFragment;

    private Copy copy;

    @Override
    public void execute() throws BuildException {
        checkArgs();
        final File[] files = getFiles();
        ArtifactOutput[] ads = getArtifactOutput(files);
        copyArtifacts(ads);
    }

    private void copyArtifacts(ArtifactOutput[] ads) {
        copy = new Copy();
        copy.setProject(getProject());
        copy.setOwningTarget(this.getOwningTarget());
        copy.setPreserveLastModified(true);
        final File organisationDirectory = getOrganisationDirectory();
        for (ArtifactOutput artifactOutput : ads) {
            publishArtifacts(organisationDirectory, artifactOutput);
        }
    }

    void publishArtifacts(final File organisationDirectory, ArtifactOutput artifactOutput) {
        File targetFile = getTargetFile(organisationDirectory, artifactOutput);
        File srcFile = artifactOutput.getSrcFile();
        copy(copy, srcFile, targetFile);
        if (artifactOutput.getSourceSrcFile() != null) {
            File targetSourceFile = getTargetSourceFile(organisationDirectory, artifactOutput);
            File sourceSrcFile = artifactOutput.getSourceSrcFile();
            copy(copy, sourceSrcFile, targetSourceFile);
        }
        if (artifactOutput.getJavadocSrcFile() != null) {
            File targetJavadocFile = getTargetJavadocFile(organisationDirectory, artifactOutput);
            File javadocSrcFile = artifactOutput.getJavadocSrcFile();
            copy(copy, javadocSrcFile, targetJavadocFile);
        }
        String pomFragment = getSharedPomFragment();
        String pomText = "<project xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n" + "<modelVersion>4.0.0</modelVersion>\n" + "<groupId>" + artifactOutput.getOrganisation() + "</groupId>\n" + "<artifactId>" + artifactOutput.getArtifact() + "</artifactId>\n" + "<version>" + artifactOutput.getVersion() + "</version>\n" + "<packaging>jar</packaging>\n" + "<name>" + artifactOutput.getArtifact() + "</name>\n" + "<description>" + artifactOutput.getArtifact() + "</description>\n" + pomFragment + "</project>";
        File pomFile = getPomFile(organisationDirectory, artifactOutput);
        if (pomFile.exists()) {
            pomFile.delete();
        }
        getProject().log(this, "Writing POM: " + pomText, Project.MSG_DEBUG);
        writePom(pomText, pomFile);
        writeChecksum(pomFile, pomFile);
        postProcessArtifacts(organisationDirectory, artifactOutput);
    }

    protected File getTargetFile(final File organisationDirectory, ArtifactOutput artifactOutput) {
        return artifactOutput.getOutputLocation(organisationDirectory, null, ".jar");
    }

    protected File getTargetSourceFile(final File organisationDirectory, ArtifactOutput artifactOutput) {
        return artifactOutput.getOutputLocation(organisationDirectory, "sources", ".jar");
    }

    protected File getTargetJavadocFile(final File organisationDirectory, ArtifactOutput artifactOutput) {
        return artifactOutput.getOutputLocation(organisationDirectory, "javadoc", ".jar");
    }

    protected File getPomFile(File organisationDirectory, ArtifactOutput artifactOutput) {
        return artifactOutput.getOutputLocation(organisationDirectory, null, ".pom");
    }

    /**
     * Hook for subclasses to post-process output
     */
    protected void postProcessArtifacts(File organisationDirectory, ArtifactOutput artifactOutput) {
    }

    String getSharedPomFragment() {
        if (sharedPomFragment == null) {
            return "";
        }
        LoadFile loadFile = new LoadFile();
        loadFile.setProject(getProject());
        final String sharedPom = "shared.pom." + System.currentTimeMillis();
        loadFile.setProperty(sharedPom);
        loadFile.setSrcFile(sharedPomFragment);
        loadFile.init();
        loadFile.execute();
        return getProject().getProperty(sharedPom);
    }

    private void copy(Copy copy, File srcFile, File targetFile) {
        copy.setFile(srcFile);
        copy.setTofile(targetFile);
        copy.execute();
        copy.init();
        writeChecksum(srcFile, targetFile);
        getProject().log(this, "Copied file " + targetFile, Project.MSG_INFO);
    }

    private void writeChecksum(File srcFile, File targetFile) {
        Checksum checksum = new Checksum();
        checksum.setProject(getProject());
        checksum.setAlgorithm("SHA1");
        checksum.setFile(srcFile);
        String property = MavenPublishTask.class.getName() + ".checksum.property." + srcFile.getAbsolutePath();
        checksum.setProperty(property);
        checksum.execute();
        Echo echo = new Echo();
        echo.setProject(getProject());
        File checksumFile = new File(targetFile.getParentFile(), targetFile.getName() + ".sha1");
        echo.setFile(checksumFile);
        String checksumValue = getProject().getProperty(property);
        echo.addText(checksumValue);
        echo.execute();
    }

    private void writePom(String pomText, File pomFile) {
        Echo echo = new Echo();
        echo.setProject(getProject());
        echo.setFile(pomFile);
        echo.addText(pomText);
        echo.execute();
        getProject().log(this, "Written pom " + pomFile, Project.MSG_INFO);
    }

    File getOrganisationDirectory() {
        String organisationSplit = organisation.replace(".", "/");
        File organisationDirectory = new File(destDir, organisationSplit);
        return organisationDirectory;
    }

    ArtifactOutput[] getArtifactOutput(File[] files) {
        ArtifactOutput[] ads = new ArtifactOutput[files.length];
        for (int i = 0; i < files.length; i++) {
            final ArtifactOutput artifactDescription = new ArtifactOutput();
            artifactDescription.setSrcFile(files[i]);
            String fileName = files[i].getName();
            String fileNameWithoutJar = fileName.substring(0, fileName.indexOf(".jar"));
            int lastDashIndex = firstDigitIndex(fileNameWithoutJar);
            String version = fileNameWithoutJar.substring(lastDashIndex + 1);
            String artifact = fileNameWithoutJar.substring(0, lastDashIndex);
            artifactDescription.setArtifact(artifact);
            artifactDescription.setOrganisation(organisation);
            artifactDescription.setVersion(version);
            File parent = files[i].getParentFile();
            String sourceFileName = fileName.replace(".jar", "-sources.jar");
            File sourceFile = new File(parent, sourceFileName);
            if (sourceFile.exists()) {
                artifactDescription.setHasSource(true);
                artifactDescription.setSourceSrcFile(sourceFile);
            }
            String javadocFileName = fileName.replace(".jar", "-javadoc.jar");
            File javadocFile = new File(parent, javadocFileName);
            if (javadocFile.exists()) {
                artifactDescription.setHasJavaDoc(true);
                artifactDescription.setJavadocSrcFile(javadocFile);
            } else {
                artifactDescription.setHasJavaDoc(false);
            }
            ads[i] = artifactDescription;
        }
        return ads;
    }

    int firstDigitIndex(String fileNameWithoutJar) {
        int lastDashIndex = fileNameWithoutJar.lastIndexOf("-");
        while (lastDashIndex >= 0) {
            if (fileNameWithoutJar.length() > lastDashIndex + 1) {
                char c = fileNameWithoutJar.charAt(lastDashIndex + 1);
                if (Character.isDigit(c)) {
                    return lastDashIndex;
                }
            }
            fileNameWithoutJar = fileNameWithoutJar.substring(0, lastDashIndex);
            lastDashIndex = fileNameWithoutJar.lastIndexOf("-");
        }
        return lastDashIndex;
    }

    File[] getFiles() {
        final String[] artifactList = artifacts.split(",");
        final File[] files = sourceDir.listFiles(new FileFilter() {

            public boolean accept(File file) {
                final String fileName = file.getName();
                if (fileName.contains("sources.jar")) {
                    return false;
                }
                if (fileName.contains("javadoc.jar")) {
                    return false;
                }
                if (file.isDirectory()) {
                    return false;
                }
                if (!fileName.endsWith(".jar")) {
                    return false;
                }
                for (String artifact : artifactList) {
                    if (fileName.startsWith(artifact.trim())) {
                        return true;
                    }
                }
                return false;
            }
        });
        final String message = "Processing " + files.length + " files from source directory " + sourceDir + " corresponding to artefacts: " + Arrays.toString(artifactList);
        getProject().log(this, message, Project.MSG_INFO);
        return files;
    }

    void checkArgs() {
        if (sourceDir == null) {
            throw new BuildException("'sourceDir' cannot be null", getLocation());
        }
        if (artifacts == null) {
            throw new BuildException("'artifacts' cannot be null", getLocation());
        }
        if (organisation == null) {
            throw new BuildException("'organisation' cannot be null", getLocation());
        }
        if (destDir == null) {
            throw new BuildException("'destDir' cannot be null", getLocation());
        }
        if (!sourceDir.exists()) {
            throw new BuildException("The source directory '" + sourceDir + "' does not exist", getLocation());
        }
        if (!sourceDir.exists()) {
            throw new BuildException("The source directory '" + sourceDir + "' does not exist", getLocation());
        }
        if (!sourceDir.isDirectory()) {
            throw new BuildException("'sourceDir' is not a directory", getLocation());
        }
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setArtifacts(String artifacts) {
        this.artifacts = artifacts;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setSharedPomFragment(File sharedPomFragment) {
        this.sharedPomFragment = sharedPomFragment;
    }
}
