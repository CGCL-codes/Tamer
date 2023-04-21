package org.osmorc.manifest.lang;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.ManifestApplicationComponent;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public class ManifestElementType extends IElementType {

    public ManifestElementType(@NotNull @NonNls String debugName) {
        super(debugName, ManifestApplicationComponent.MANIFEST.getLanguage());
    }

    public String toString() {
        return "Manifest: " + super.toString();
    }
}
