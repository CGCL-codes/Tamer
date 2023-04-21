package com.ivis.xprocess.artifact_substituter;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import com.ivis.xprocess.ui.artifact_substituter.IXprocessArtifactSubstituteProvider;

public class ArtifactSubstituter implements IXprocessArtifactSubstituteProvider {

    private static final Logger logger = Logger.getLogger(ArtifactSubstituter.class.getName());

    public void substitute(Set<String> artifactPaths, Map<String, String> parameterMap) {
        if (!parameterMap.isEmpty()) {
            for (String artifactPath : artifactPaths) {
                IXprocessArtifactSubstituteProvider provider = getProvider(artifactPath);
                if (provider != null) {
                    provider.substitute(artifactPath, parameterMap);
                }
            }
        }
    }

    public void substitute(String artifactPath, Map<String, String> parameterMap) {
        logger.log(Level.INFO, "Artifact substitution requested for " + artifactPath);
        String extension = FileUtils.getExtension(artifactPath);
        if (extension.length() > 0) {
            if (extension.toUpperCase().equals(ExtensionType.DOC.name())) {
                MSWordSubstituter.substitute(artifactPath, parameterMap);
                return;
            }
        }
        TextSubstituter.substitute(artifactPath, parameterMap);
    }

    public boolean canSubstitute(String artifactPath) {
        if (Platform.getExtensionRegistry().getExtensionPoint("XprocessArtifactSubstituter") != null) {
            IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("XprocessArtifactSubstituter").getExtensions();
            for (int i = 0; i < extensions.length; i++) {
                IConfigurationElement[] configurationElements = extensions[i].getConfigurationElements();
                for (int j = 0; j < configurationElements.length; j++) {
                    try {
                        Object obj = configurationElements[j].createExecutableExtension("class");
                        if (obj instanceof IXprocessArtifactSubstituteProvider) {
                            final IXprocessArtifactSubstituteProvider possibleProvider = (IXprocessArtifactSubstituteProvider) obj;
                            if (possibleProvider.canSubstitute(artifactPath)) {
                                return true;
                            }
                        }
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private IXprocessArtifactSubstituteProvider getProvider(String artifactPath) {
        if (Platform.getExtensionRegistry().getExtensionPoint("XprocessArtifactSubstituter") != null) {
            IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("XprocessArtifactSubstituter").getExtensions();
            for (int i = 0; i < extensions.length; i++) {
                IConfigurationElement[] configurationElements = extensions[i].getConfigurationElements();
                for (int j = 0; j < configurationElements.length; j++) {
                    try {
                        Object obj = configurationElements[j].createExecutableExtension("class");
                        if (obj instanceof IXprocessArtifactSubstituteProvider) {
                            final IXprocessArtifactSubstituteProvider possibleProvider = (IXprocessArtifactSubstituteProvider) obj;
                            if (possibleProvider.canSubstitute(artifactPath)) {
                                return possibleProvider;
                            }
                        }
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return this;
    }

    enum ExtensionType {

        DOC, TXT, XML, RTF
    }
}
