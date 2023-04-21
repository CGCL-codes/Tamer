package com.tikal.maven.plugin.devtools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.DefaultLogger;

/**
 * @goal pack-site
 * @description create a site digest .
 * @author lior.kanfi inspired by callisto build
 */
public class PackSiteOptimizerMojo extends AbstractSiteOptimizerMojo {

    public PackSiteOptimizerMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        Project antProject = this.getAntProject();
        siteOptimize(antProject, "-jarProcessor -verbose -outputDir " + this.getUpdateSite() + " -pack " + this.getUpdateSite());
    }
}
