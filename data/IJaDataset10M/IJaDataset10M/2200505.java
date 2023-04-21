package net.beeger.osmorc.manifest.lang.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class ManifestDirective extends AbstractBinaryManifestExpression {

    public ManifestDirective(@NotNull ASTNode node) {
        super(node);
    }
}
