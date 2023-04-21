package com.googlecode.bdoc.mojo;

import static org.apache.commons.beanutils.PropertyUtils.getSimpleProperty;
import java.io.File;
import java.util.ArrayList;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import com.googlecode.bdoc.help.StoryRefCodeGenerator;
import com.googlecode.bdoc.help.StoryRefCodeGenerator.Result;

/**
 * @goal init
 * @author Per Otto Bergum Christensen
 */
public class BDocInitMojo extends AbstractMojo {

    public static final String NL = System.getProperty("line.separator");

    /**
	 * @parameter default-value="${project.testCompileSourceRoots}"
	 * @required
	 */
    private ArrayList<String> testCompileSourceRoots;

    /**
	 * @parameter default-value="${project}"
	 * @required
	 * @readonly
	 */
    private MavenProject project;

    /**
	 * @parameter expression="${javaPackage}"
	 */
    private String javaPackage;

    public void execute() throws MojoExecutionException {
        if (testCompileSourceRoots.isEmpty()) {
            getLog().error("No testCompileSourceRoots for project");
            return;
        }
        if (null == javaPackage) {
            javaPackage = RootPackageGenerator.calculateRootPackage(project);
            getLog().info("No package is set with option -DjavaPackage=<package>, will use: " + javaPackage);
        }
        try {
            executeInternal();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeInternal() throws Exception {
        StoryRefCodeGenerator storyRefCodeGenerator = new StoryRefCodeGenerator();
        storyRefCodeGenerator.setJavaPackage(javaPackage);
        storyRefCodeGenerator.setTestDirectory(new File(testCompileSourceRoots.get(0)));
        Result result = storyRefCodeGenerator.execute();
        getLog().info("------------------------------------------------------------------------");
        getLog().info("Result: " + result + NL + String.valueOf(getSimpleProperty(storyRefCodeGenerator, "resultLog")));
    }
}
