package net.beeger.osmorc.manifest.lang.psi;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import net.beeger.osmorc.BundleManager;
import net.beeger.osmorc.manifest.BundleManifest;
import org.jetbrains.annotations.Nullable;

public class BundleReference extends PsiReferenceBase<ManifestHeaderValue> implements EmptyResolveMessageProvider {

    public BundleReference(ManifestHeaderValue element) {
        super(element);
        _bundleManager = ServiceManager.getService(element.getProject(), BundleManager.class);
    }

    @Nullable
    public PsiElement resolve() {
        BundleManifest bundleManifest = _bundleManager.getBundleManifest(getCanonicalText());
        return bundleManifest != null ? bundleManifest.getManifestFile() : null;
    }

    public Object[] getVariants() {
        return new Object[0];
    }

    public String getUnresolvedMessagePattern() {
        return "Cannot resolve bundle";
    }

    private final BundleManager _bundleManager;
}
