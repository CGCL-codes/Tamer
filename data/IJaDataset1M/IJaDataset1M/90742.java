package org.clarent.ivyidea.resolve.dependency;

import com.intellij.openapi.project.Project;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.clarent.ivyidea.config.IvyIdeaConfigHelper;
import org.clarent.ivyidea.config.model.ArtifactTypeSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;

/**
 * @author Guy Mahieu
 */
public class ExternalDependencyFactory {

    private static ExternalDependencyFactory instance = new ExternalDependencyFactory();

    public static ExternalDependencyFactory getInstance() {
        return instance;
    }

    @Nullable
    public ExternalDependency createExternalDependency(@NotNull Artifact artifact, @Nullable File file, @NotNull Project project, @NotNull final String configurationName) {
        final ArtifactTypeSettings.DependencyCategory category = determineCategory(project, artifact);
        if (category != null) {
            switch(category) {
                case Classes:
                    return new ExternalJarDependency(artifact, file, configurationName);
                case Sources:
                    return new ExternalSourceDependency(artifact, file, configurationName);
                case Javadoc:
                    return new ExternalJavaDocDependency(artifact, file, configurationName);
            }
        }
        return null;
    }

    private static ArtifactTypeSettings.DependencyCategory determineCategory(@NotNull Project project, @NotNull Artifact artifact) {
        final ArtifactTypeSettings typeSettings = IvyIdeaConfigHelper.getArtifactTypeSettings(project);
        if (typeSettings == null) {
            return null;
        }
        return typeSettings.getCategoryForType(artifact.getType());
    }
}
