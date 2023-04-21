package org.osmorc.manifest.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Robert F. Beeger (robert@beeger.net)
 */
public class ManifestHeader extends ManifestElementBase {

    public ManifestHeader(@NotNull ASTNode node) {
        super(node);
    }

    public String getName() {
        ManifestHeaderName headerName = PsiTreeUtil.getChildOfType(this, ManifestHeaderName.class);
        return headerName != null ? headerName.getName() : null;
    }
}
