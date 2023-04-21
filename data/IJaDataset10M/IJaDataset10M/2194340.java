package protoj.lang.internal;

import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Chmod;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.FileSet;
import protoj.core.ProjectLayout;
import protoj.core.internal.AntTarget;
import protoj.core.internal.InformationException;
import protoj.lang.ArchiveFeature;
import protoj.lang.ClassesArchive;
import protoj.lang.StandardProject;
import protoj.lang.ClassesArchive.ClassesEntry;

/**
 * Responsible for creating an instance of {@link StandardProject} representing
 * one of the test projects. When the
 * {@link #createTestProject(ProtoProject, String, String, String, String)}
 * method returns, the test project will have been created in the protoj project
 * target directory by using the source project directory and protoj lib
 * directory as the source files to copy. Any existing project will
 * automatically be deleted first.
 * <p>
 * As an example this instance can be used to create a target/helloworld project
 * based on the example/helloworld and lib/*.jar source files.
 * <p>
 * This is really TestProjectFeature, but junit then assumes it's a test and
 * fails it.
 * 
 * @author Ashley Williams
 * 
 */
public final class SubjectProjectFeature {

    private ProtoProject parent;

    private ProjectLayout parentLayout;

    private StandardProject test;

    private ProjectLayout testLayout;

    private String relativeSourceDir;

    private String includedJars;

    private String excludedJars;

    /**
	 * See {@link SubjectProjectFeature} class description.
	 * 
	 * @param parent
	 * @param relativeSourceDir
	 *            the name of the directory relative to the project root
	 *            containing the files that make up the test project: eg
	 *            "example/helloworld"
	 * @param scriptName
	 *            the name of the script used to start the test project: eg
	 *            "helloworld.sh"
	 * @param includedJars
	 *            the jars from the lib directory that should be included when
	 *            copying to the test project
	 * @param excludedJars
	 *            the jars from the lib directory that should be excluded when
	 *            copying to the test project
	 * @return
	 */
    public StandardProject createTestProject(ProtoProject parent, String relativeSourceDir, String scriptName, String includedJars, String excludedJars) {
        this.parent = parent;
        this.relativeSourceDir = relativeSourceDir;
        this.includedJars = includedJars;
        this.excludedJars = excludedJars;
        this.parentLayout = parent.getDelegate().getLayout();
        File sourceDir = new File(parentLayout.getRootDir(), relativeSourceDir);
        File testRootDir = new File(parentLayout.getTargetDir(), sourceDir.getName());
        this.test = new StandardProject(testRootDir, scriptName, null);
        this.testLayout = test.getLayout();
        AntTarget target = new AntTarget("create-test-project");
        target.initLogging(parentLayout.getLogFile(), Project.MSG_INFO);
        addDeleteOldProject(target);
        addCopyNewProject(target);
        addCopyArtifacts(target);
        addRelaxPermissions(target);
        target.execute();
        return test;
    }

    private void addRelaxPermissions(AntTarget target) {
        Chmod chmod = new Chmod();
        chmod.setTaskName("permissions");
        target.addTask(chmod);
        chmod.setDir(testLayout.getRootDir().getParentFile());
        chmod.setIncludes("**/*.*");
        chmod.setPerm("777");
    }

    /**
	 * Copies just the aspectj libraries and the protoj no dependencies library.
	 * 
	 * @param target
	 */
    private void addCopyArtifacts(AntTarget target) {
        Copy copyLibs = new Copy();
        copyLibs.setTaskName("copy");
        target.addTask(copyLibs);
        copyLibs.setTodir(testLayout.getLibDir());
        FileSet libs = new FileSet();
        if (includedJars != null) {
            libs.setIncludes(includedJars);
        }
        if (excludedJars != null) {
            libs.setExcludes(excludedJars);
        }
        libs.setDir(parentLayout.getLibDir());
        copyLibs.addFileset(libs);
        ArchiveFeature archive = parent.getDelegate().getArchiveFeature();
        ClassesArchive classes = archive.getClassesArchive();
        ClassesEntry entry = classes.getEntry(parent.getNoDepJarName());
        File noDepArtifact = entry.getArchiveEntry().getArtifact();
        if (!noDepArtifact.exists()) {
            String message = "can't create project: please ensure protoj artifact is created at " + noDepArtifact.getAbsolutePath();
            throw new InformationException(message);
        }
        Copy copyNoDep = new Copy();
        copyNoDep.setTaskName("test-copy-no-dep");
        target.addTask(copyNoDep);
        copyNoDep.setFile(noDepArtifact);
        copyNoDep.setTodir(testLayout.getLibDir());
    }

    private void addCopyNewProject(AntTarget target) {
        File sourceDir = new File(parentLayout.getRootDir(), relativeSourceDir);
        Copy copyProject = new Copy();
        copyProject.setTaskName("copy");
        target.addTask(copyProject);
        copyProject.setTodir(testLayout.getRootDir());
        FileSet projectResources = new FileSet();
        projectResources.setDir(sourceDir);
        copyProject.addFileset(projectResources);
    }

    private void addDeleteOldProject(AntTarget target) {
        Delete delete = new Delete();
        delete.setTaskName("delete");
        target.addTask(delete);
        delete.setDir(testLayout.getRootDir());
    }
}
