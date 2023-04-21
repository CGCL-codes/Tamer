package protoj.build;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.aspectj.tools.ant.taskdefs.AjcTask;

public class MakeCommand {

    private AntTarget target;

    private boolean success;

    public MakeCommand(DirConfig jarProject, String maxMemory) {
        target = new AntTarget("protoj-make");
        jarProject.getClassesDir().mkdirs();
        Copy copy = new Copy();
        target.addTask(copy);
        copy.setTaskName("copy");
        copy.setTodir(jarProject.getClassesDir());
        FileSet resourceFiles = new FileSet();
        resourceFiles.setDir(jarProject.getResourcesDir());
        copy.addFileset(resourceFiles);
        AjcTask ajc = new AjcTask();
        target.addTask(ajc);
        ajc.setTaskName("ajc");
        ajc.setFailonerror(true);
        Path sourceRoots = ajc.createSourceRoots();
        DirSet dirSet = new DirSet();
        dirSet.setDir(jarProject.getJavaDir().getParentFile());
        dirSet.setIncludes(jarProject.getJavaDir().getName());
        sourceRoots.addDirset(dirSet);
        ajc.setDestdir(jarProject.getClassesDir());
        ajc.setSource("1.5");
        Path classpath = new Path(target.getProject());
        jarProject.initClasspath(classpath);
        ajc.setClasspath(classpath);
    }

    public void initLogging(File logFile) {
        target.initLogging(logFile, Project.MSG_INFO);
    }

    public void execute() {
        try {
            target.execute();
            success = true;
        } catch (BuildException e) {
            success = false;
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
