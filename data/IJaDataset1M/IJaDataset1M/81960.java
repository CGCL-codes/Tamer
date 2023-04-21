package br.com.sysmap.crux.tools.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Commandline.Argument;

/**
 * Ant Task to invoke ModuleExporter Tool
 * @author Thiago da Rosa de Bustamante
 */
public class ModuleExporterTask extends Task {

    private List<Path> classpath = new ArrayList<Path>();

    private List<Argument> args = new ArrayList<Argument>();

    private List<Argument> jvmArgs = new ArrayList<Argument>();

    private boolean failOnError = true;

    private File outputDir;

    private File sourceDir;

    private String moduleName;

    private String outputModuleName;

    private String includes;

    private String excludes;

    private String javaSource;

    private String javaTarget;

    private boolean exportCruxCompilation = true;

    private String scanAllowedPackages;

    private String scanIgnoredPackages;

    private String pagesOutputCharset;

    private String pageFileExtension;

    private boolean unpackaged = false;

    private String inputCharset = "UTF-8";

    public void addClasspath(Path classpath) {
        this.classpath.add(classpath);
    }

    public void addArg(Argument arg) {
        this.args.add(arg);
    }

    public void addJvmarg(Argument jvmarg) {
        this.jvmArgs.add(jvmarg);
    }

    public void setFailOnError(Boolean failOnError) {
        this.failOnError = failOnError;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setOutputModuleName(String outputModuleName) {
        this.outputModuleName = outputModuleName;
    }

    public void setIncludes(String includes) {
        this.includes = includes;
    }

    public void setExcludes(String excludes) {
        this.excludes = excludes;
    }

    public void setJavaSource(String javaSource) {
        this.javaSource = javaSource;
    }

    public void setJavaTarget(String javaTarget) {
        this.javaTarget = javaTarget;
    }

    public void setExportCruxCompilation(boolean exportCruxCompilation) {
        this.exportCruxCompilation = exportCruxCompilation;
    }

    public void setScanAllowedPackages(String scanAllowedPackages) {
        this.scanAllowedPackages = scanAllowedPackages;
    }

    public void setScanIgnoredPackages(String scanIgnoredPackages) {
        this.scanIgnoredPackages = scanIgnoredPackages;
    }

    public void setPagesOutputCharset(String pagesOutputCharset) {
        this.pagesOutputCharset = pagesOutputCharset;
    }

    public void setPageFileExtension(String pageFileExtension) {
        this.pageFileExtension = pageFileExtension;
    }

    public void setUnpackaged(boolean unpackaged) {
        this.unpackaged = unpackaged;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    /**
	 * @see org.apache.tools.ant.Task#execute()
	 */
    public void execute() throws BuildException {
        try {
            exportModule();
        } catch (Throwable e) {
            log(e.getMessage(), e, Project.MSG_ERR);
            new BuildException(e.getMessage(), e);
        }
    }

    /**
	 *Exports the module using ModuleExporter
	 * @throws Exception
	 */
    protected void exportModule() throws Exception {
        log("Exporting...");
        Java javatask = (Java) getProject().createTask("java");
        javatask.setClassname(getProgramClassName());
        javatask.setFork(true);
        javatask.setFailonerror(true);
        javatask.createJvmarg().setValue("-Dfile.encoding=" + this.inputCharset);
        for (Argument arg : jvmArgs) {
            if (arg != null) {
                javatask.createJvmarg().setValue(arg.getParts()[0]);
            }
        }
        addExporterParameters(javatask);
        for (Argument arg : args) {
            if (arg != null) {
                javatask.createArg().setValue(arg.getParts()[0]);
            }
        }
        for (Path path : this.classpath) {
            javatask.setClasspath(path);
        }
        int resultCode = javatask.executeJava();
        if (resultCode != 0) {
            if (this.failOnError) {
                throw new ModuleExporterException("Crux ModuleExporter returned errors.");
            } else {
                log("Crux ModuleExporter returned errors.", Project.MSG_ERR);
            }
        }
    }

    /**
	 * @return
	 */
    protected String getProgramClassName() {
        return ModuleExporter.class.getCanonicalName();
    }

    /**
	 * @param javatask
	 * @throws IOException
	 */
    protected void addExporterParameters(Java javatask) throws Exception {
        if (this.outputDir != null) {
            javatask.createArg().setValue("outputDir");
            javatask.createArg().setValue(this.outputDir.getCanonicalPath());
        }
        if (this.sourceDir != null) {
            javatask.createArg().setValue("sourceDir");
            javatask.createArg().setValue(this.sourceDir.getCanonicalPath());
        }
        if (this.moduleName != null) {
            javatask.createArg().setValue("moduleName");
            javatask.createArg().setValue(this.moduleName);
        }
        if (this.outputModuleName != null) {
            javatask.createArg().setValue("outputModuleName");
            javatask.createArg().setValue(this.outputModuleName);
        }
        if (this.includes != null) {
            javatask.createArg().setValue("includes");
            javatask.createArg().setValue(this.includes);
        }
        if (this.excludes != null) {
            javatask.createArg().setValue("excludes");
            javatask.createArg().setValue(this.excludes);
        }
        if (this.javaSource != null) {
            javatask.createArg().setValue("javaSource");
            javatask.createArg().setValue(this.javaSource);
        }
        if (this.javaTarget != null) {
            javatask.createArg().setValue("javaTarget");
            javatask.createArg().setValue(this.javaTarget);
        }
        if (this.scanAllowedPackages != null) {
            javatask.createArg().setValue("scanAllowedPackages");
            javatask.createArg().setValue(this.scanAllowedPackages);
        }
        if (this.scanIgnoredPackages != null) {
            javatask.createArg().setValue("scanIgnoredPackages");
            javatask.createArg().setValue(this.scanIgnoredPackages);
        }
        if (this.pageFileExtension != null) {
            javatask.createArg().setValue("pageFileExtension");
            javatask.createArg().setValue(this.pageFileExtension);
        }
        if (this.pagesOutputCharset != null) {
            javatask.createArg().setValue("pagesOutputCharset");
            javatask.createArg().setValue(this.pagesOutputCharset);
        }
        if (!exportCruxCompilation) {
            javatask.createArg().setValue("-doNotExportCruxCompilation");
        }
        if (unpackaged) {
            javatask.createArg().setValue("-unpackaged");
        }
    }
}
