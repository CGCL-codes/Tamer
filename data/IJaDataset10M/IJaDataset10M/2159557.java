package org.jata.write.util;

import java.io.File;
import java.util.Arrays;
import org.apache.maven.embedder.Configuration;
import org.apache.maven.embedder.ConfigurationValidationResult;
import org.apache.maven.embedder.DefaultConfiguration;
import org.apache.maven.embedder.MavenEmbedder;
import org.apache.maven.embedder.MavenEmbedderException;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionResult;
import org.apache.maven.project.MavenProject;

public class MavenBuildUtil {

    public static void buildGeneratedJataProject(String dirName) throws BuildFailedException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = MavenBuildUtil.class.getClassLoader();
        }
        Configuration configuration = new DefaultConfiguration();
        if (MavenEmbedder.DEFAULT_USER_SETTINGS_FILE.exists()) {
            configuration.setUserSettingsFile(MavenEmbedder.DEFAULT_USER_SETTINGS_FILE);
        }
        if (MavenEmbedder.DEFAULT_GLOBAL_SETTINGS_FILE.exists()) {
            configuration.setGlobalSettingsFile(MavenEmbedder.DEFAULT_GLOBAL_SETTINGS_FILE);
        }
        configuration.setClassLoader(classLoader);
        ConfigurationValidationResult validationResult = MavenEmbedder.validateConfiguration(configuration);
        File projectDirectory = new File(dirName);
        if (validationResult.isValid()) {
            MavenEmbedder embedder;
            try {
                embedder = new MavenEmbedder(configuration);
            } catch (MavenEmbedderException e) {
                throw new BuildFailedException(e);
            }
            MavenExecutionRequest request = new DefaultMavenExecutionRequest().setBaseDirectory(projectDirectory).setGoals(Arrays.asList(new String[] { "package" }));
            MavenExecutionResult result = embedder.execute(request);
            if (result.hasExceptions()) {
                throw new BuildFailedException("Build failed", result.getExceptions());
            }
            MavenProject project = result.getProject();
            String groupId = project.getGroupId();
            String artifactId = project.getArtifactId();
            String version = project.getVersion();
            String name = project.getName();
            String environment = project.getProperties().getProperty("environment");
        }
    }
}
