package unclej.framework;

import unclej.filepath.ClassPath;
import unclej.filepath.Filelike;
import unclej.filepath.PathSpec;
import unclej.javatype.ClassName;
import unclej.log.ULog;
import unclej.utasks.compile.CompileJavaUTask;
import unclej.utasks.compile.JavacUTask;
import unclej.validate.ValidationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Loads projects as Java classes.  If the <tt>.java</tt> file is newer than the <tt>.class</tt> file or the
 * <tt>.class</tt> does not exist, it will be compiled before loading.
 * @author scottv
 */
public class DefaultUProjectLoader implements UProjectLoader {

    public DefaultUProjectLoader() {
    }

    public DefaultUProjectLoader(ClassPath classPath, ULog log) {
        this.classPath = classPath;
        this.log = log;
    }

    public UProject getProject(ClassName name, File baseDir) throws ValidationException {
        ClassPath combinedClassPath = (ClassPath) ClassPath.getCurrent().append(classPath).append(defaultBuildPath);
        compileProjectClass(name, combinedClassPath, log);
        UProject project = combinedClassPath.constructObject(name, UProject.class);
        if (baseDir == null) {
            Filelike location = combinedClassPath.findFirst(name);
            baseDir = location.getBase();
            String baseName = baseDir.getName();
            if ("classes".equals(baseName) || "uncle".equals(baseName)) {
                baseDir = baseDir.getParentFile();
            }
        }
        project.setBaseDir(baseDir);
        return project;
    }

    public UProject getDefaultProject() throws ValidationException {
        File baseDir = new File(".");
        Map<ClassName, Filelike> candidateClasses = defaultBuildPath.listClasses("*");
        List<UProject> projects = new ArrayList<UProject>();
        for (ClassName name : candidateClasses.keySet()) {
            try {
                projects.add(getProject(name, baseDir));
            } catch (ValidationException x) {
                log.log(Level.FINER, x, "this is probably not supposed to be a project, my bad");
            } catch (NoClassDefFoundError x) {
                log.log(Level.FINER, x, "probably guessed the wrong package name here");
            }
        }
        if (projects.isEmpty()) {
            throw new ValidationException("no projects found on path " + defaultBuildPath);
        } else if (projects.size() == 1) {
            return projects.get(0);
        } else {
            CompositeUProject composite = new CompositeUProject();
            composite.setBaseDir(baseDir);
            for (UProject project : projects) {
                composite.addProject(project);
            }
            return composite;
        }
    }

    protected void compileProjectClass(ClassName name, ClassPath combinedClassPath, ULog log) throws ValidationException {
        Filelike classFile = combinedClassPath.findFirst(name);
        Filelike sourceFile = combinedClassPath.findFirst(name.getSourceFileName());
        if (sourceFile != null && sourceFile.toFile() != null && (classFile == null || classFile.getModTime() < sourceFile.getModTime())) {
            JavacUTask javac = new JavacUTask();
            javac.setClassPath(combinedClassPath);
            javac.setOutputDir((classFile == null ? sourceFile : classFile).getBase());
            javac.addSourceFiles(new PathSpec(sourceFile.toFile()));
            javac.setSourceVersion(CompileJavaUTask.VERSION_1_5);
            javac.describe(log, false);
            try {
                javac.execute(log);
            } catch (ExitException x) {
                throw x;
            } catch (Exception x) {
                throw new ValidationException("exception occurred while trying to compile " + name, x);
            }
        }
    }

    public void setClassPath(ClassPath classPath) {
        this.classPath = classPath;
    }

    public void setLog(ULog log) {
        this.log = log;
    }

    protected void setDefaultBuildPath(ClassPath defaultBuildPath) {
        this.defaultBuildPath = defaultBuildPath;
    }

    private ClassPath classPath;

    private ULog log;

    private ClassPath defaultBuildPath = new ClassPath(".", "build", "uncle");
}
