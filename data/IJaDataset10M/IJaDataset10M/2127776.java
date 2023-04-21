package org.osmorc.manifest;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;
import org.osmorc.valueobject.Version;
import java.util.List;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public interface BundleManifest {

    PsiFile getManifestFile();

    Version getBundleVersion();

    String getBundleSymbolicName();

    String getBundleActivator();

    List<String> getExportPackage();

    List<PsiPackage> getImportPackage();

    List<Module> getRequireBundle();

    boolean exportsPackage(@NotNull String aPackage);
}
