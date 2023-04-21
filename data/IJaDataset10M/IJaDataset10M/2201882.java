package com.googlecode.intelliguard.runner;

import com.googlecode.intelliguard.ant.YProject;
import com.googlecode.intelliguard.ant.YProjectHelper;
import com.googlecode.intelliguard.facet.GuardFacet;
import com.googlecode.intelliguard.generator.YGuardGenerator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ronnie
 * Date: 2009-nov-02
 * Time: 18:37:23
 */
public class ObfuscateTask implements Runnable {

    private RunProgress runProgress;

    private GuardFacet guardFacet;

    public ObfuscateTask(@NotNull final RunProgress runProgress, @NotNull final GuardFacet guardFacet) {
        this.runProgress = runProgress;
        this.guardFacet = guardFacet;
    }

    public void run() {
        final String buildXml = YGuardGenerator.generateBuildXml(guardFacet);
        final Project project = new YProject(runProgress);
        final ProjectHelper projectHelper = new YProjectHelper();
        projectHelper.parse(project, new ByteArrayInputStream(buildXml.getBytes()));
        project.init();
        try {
            project.executeTarget(YGuardGenerator.YGUARD_TARGET_NAME);
        } catch (BuildException e) {
            runProgress.markError(e.getMessage());
        }
    }
}
