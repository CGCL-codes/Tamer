package org.clarent.ivyidea.resolve.dependency;

import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.clarent.ivyidea.intellij.compatibility.IntellijCompatibilityService;
import java.io.File;

/**
 * @author Guy Mahieu
 */
public class ExternalJavaDocDependency extends ExternalDependency {

    public ExternalJavaDocDependency(Artifact artifact, File externalArtifact, final String configurationName) {
        super(artifact, externalArtifact, configurationName);
    }

    protected String getTypeName() {
        return "javadoc";
    }

    public OrderRootType getType() {
        return IntellijCompatibilityService.getCompatibilityMethods().getJavadocOrderRootType();
    }
}
