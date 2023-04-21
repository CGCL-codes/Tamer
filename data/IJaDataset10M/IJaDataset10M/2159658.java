package net.sourceforge.deco;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.internal.runners.CompositeRunner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class AntUnitRunner extends CompositeRunner {

    private final Project antProject;

    private final String target;

    /**
	 * The <code>Projects</code> annotation specifies the ant-unit script to run when a class
	 * annotated with <code>@RunWith(AntUnitRunner.class)</code> is run.
	 */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Scripts {

        public String[] value();
    }

    public AntUnitRunner(Class<?> testedClass) {
        super(testedClass.getName());
        antProject = null;
        this.target = null;
        Scripts annotation = testedClass.getAnnotation(Scripts.class);
        if (annotation == null) {
            throw new IllegalArgumentException(testedClass.getName() + " doesn't have @Project configured");
        }
        String[] scripts = annotation.value();
        ProjectHelper prjHelper = ProjectHelper.getProjectHelper();
        for (String script : scripts) {
            Project prj = new Project();
            URL scriptUrl = testedClass.getClassLoader().getResource(script);
            if (scriptUrl == null) {
                throw new IllegalArgumentException(testedClass.getName() + " doesn't found the script " + script + ".  Scripts are searched in the classloader");
            }
            DefaultLogger logger = new DefaultLogger();
            logger.setMessageOutputLevel(Project.MSG_INFO);
            logger.setErrorPrintStream(System.err);
            logger.setOutputPrintStream(System.out);
            prj.addBuildListener(logger);
            prj.addReference(ProjectHelper.PROJECTHELPER_REFERENCE, prjHelper);
            prj.setBasedir(".");
            prj.init();
            prjHelper.parse(prj, scriptUrl);
            add(new AntUnitRunner(script, prj));
        }
    }

    protected AntUnitRunner(String script, Project prj) {
        super(script);
        this.antProject = prj;
        this.target = null;
        Map<String, ?> targets = (Map<String, ?>) prj.getTargets();
        for (String target : targets.keySet()) {
            if (target.startsWith("test") && !target.equals("test")) {
                add(new AntUnitRunner(prj, target));
            }
        }
    }

    protected AntUnitRunner(Project prj, String target) {
        super(target);
        this.antProject = prj;
        this.target = target;
    }

    @Override
    public void run(RunNotifier notifier) {
        if (target == null) {
            super.run(notifier);
        } else {
            notifier.fireTestStarted(getDescription());
            try {
                antProject.executeTarget(target);
                notifier.fireTestFinished(getDescription());
            } catch (BuildException ex) {
                notifier.fireTestFailure(new Failure(getDescription(), ex));
            }
        }
    }
}
