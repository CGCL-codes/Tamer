package ru.sql.java.idef;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author a.grasoff
 */
public class JavaFoldingBuilder extends FoldingBuilderEx {

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        if (node instanceof PsiComment) {
            String text = node.getText();
            return text != null && text.startsWith("//<editor-fold") && text.contains("default=\"collapsed\"");
        }
        return false;
    }

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        MyFoldingVisitor visitor = new MyFoldingVisitor();
        root.accept(visitor);
        return visitor.foldingData.toArray(new FoldingDescriptor[visitor.foldingData.size()]);
    }

    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        if (node instanceof PsiComment) {
            String text = node.getText();
            if (text.startsWith("//<editor-fold")) {
                return text;
            }
        }
        return "...";
    }

    private class MyFoldingVisitor extends JavaRecursiveElementWalkingVisitor {

        private PsiComment lastOpened;

        private final List<FoldingDescriptor> foldingData = new ArrayList<FoldingDescriptor>();

        @Override
        public void visitComment(PsiComment comment) {
            String text = comment.getText();
            if (text == null) {
                return;
            }
            if (text.startsWith("//<editor-fold")) {
                foldingDataStart(comment);
            } else if (text.startsWith("//</editor-fold>")) {
                foldingDataEnd(comment);
            }
        }

        private void foldingDataEnd(PsiComment comment) {
            if (lastOpened == null) {
                return;
            }
            synchronized (foldingData) {
                TextRange range = new TextRange(lastOpened.getTextRange().getStartOffset(), comment.getTextRange().getEndOffset());
                FoldingDescriptor descriptor = new FoldingDescriptor(lastOpened, range);
                foldingData.add(descriptor);
            }
            lastOpened = null;
        }

        private void foldingDataStart(PsiComment comment) {
            lastOpened = comment;
        }
    }
}
