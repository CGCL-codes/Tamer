package com.jme3.gde.codecheck.hints;

import com.jme3.system.Annotations.ReadOnly;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.modules.java.hints.spi.AbstractHint;
import org.netbeans.spi.editor.hints.ChangeInfo;
import org.netbeans.spi.editor.hints.EnhancedFix;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.Fix;
import org.openide.awt.StatusDisplayer;

public class ReadOnlyPrimitiveHint extends AbstractHint {

    private static final List<Fix> NO_FIXES = Collections.<Fix>emptyList();

    private static final Set<Tree.Kind> TREE_KINDS = EnumSet.<Tree.Kind>of(Tree.Kind.METHOD_INVOCATION);

    public ReadOnlyPrimitiveHint() {
        super(true, true, AbstractHint.HintSeverity.WARNING);
    }

    @Override
    public Set<Kind> getTreeKinds() {
        return TREE_KINDS;
    }

    @Override
    public List<ErrorDescription> run(CompilationInfo info, TreePath treePath) {
        if (info.getTrees().getElement(treePath).getAnnotation(ReadOnly.class) != null) {
            Tree t = treePath.getLeaf();
            JTextComponent editor = EditorRegistry.lastFocusedComponent();
            SourcePositions sp = info.getTrees().getSourcePositions();
            int end = (int) sp.getEndPosition(info.getCompilationUnit(), t);
            boolean fail = false;
            if (info.getText().length() >= end + 4) {
                String dotText = info.getText().substring(end, end + 4);
                if (".set".equals(dotText)) {
                    fail = true;
                }
            }
            if (fail) {
                return Collections.<ErrorDescription>singletonList(ErrorDescriptionFactory.createErrorDescription(getSeverity().toEditorSeverity(), getDisplayName(), NO_FIXES, info.getFileObject(), (int) info.getTrees().getSourcePositions().getStartPosition(info.getCompilationUnit(), t), (int) info.getTrees().getSourcePositions().getEndPosition(info.getCompilationUnit(), t)));
            }
            return null;
        }
        return null;
    }

    @Override
    public void cancel() {
    }

    @Override
    public String getDisplayName() {
        return "This primitive is read only and should not be modified!";
    }

    @Override
    public String getId() {
        return "ReadOnly Primitives";
    }

    @Override
    public String getDescription() {
        return "Checks for modifications to readonly primitives. (getLocalTranslation().set())";
    }

    class MessagesFix implements EnhancedFix {

        Document doc = null;

        int start = 0;

        String bodyText = null;

        public MessagesFix(Document doc, int start, String bodyText) {
            this.doc = doc;
            this.start = start;
            this.bodyText = bodyText;
        }

        @Override
        public CharSequence getSortText() {
            return "charsequence";
        }

        @Override
        public String getText() {
            return "Remove this call";
        }

        @Override
        public ChangeInfo implement() throws Exception {
            doc.remove(start, bodyText.length() + 1);
            StatusDisplayer.getDefault().setStatusText("Removed: " + bodyText);
            return null;
        }
    }
}
