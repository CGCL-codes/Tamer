package org.osmorc.manifest.lang.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public class ManifestAttribute extends AbstractBinaryManifestExpression {

    public ManifestAttribute(@NotNull ASTNode node) {
        super(node);
    }
}
