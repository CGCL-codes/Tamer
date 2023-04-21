package ideah.psi.api.util;

import com.intellij.lang.ASTNode;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import ideah.HaskellFileType;
import ideah.lexer.HaskellTokenTypes;
import ideah.parser.HaskellFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HaskellPsiElementFactoryImpl extends HaskellPsiElementFactory {

    private final Project myProject;

    public HaskellPsiElementFactoryImpl(Project project) {
        myProject = project;
    }

    private static final String DUMMY = "DUMMY.";

    @Nullable
    public ASTNode createIdentNodeFromText(@NotNull String newName) {
        HaskellFile dummyFile = createHaskellFileFromText(newName);
        PsiElement firstChild = dummyFile.getFirstChild();
        if (firstChild != null) return firstChild.getNode();
        return null;
    }

    private HaskellFile createHaskellFileFromText(String text) {
        return (HaskellFile) PsiFileFactory.getInstance(myProject).createFileFromText(DUMMY + HaskellFileType.INSTANCE.getDefaultExtension(), text);
    }
}
