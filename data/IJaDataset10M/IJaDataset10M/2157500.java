package com.jme3.gde.codecheck.hints;

import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
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

public class UpdateHint extends AbstractHint {

    private static final List<Fix> NO_FIXES = Collections.<Fix>emptyList();

    private static final Set<Tree.Kind> TREE_KINDS = EnumSet.<Tree.Kind>of(Tree.Kind.METHOD_INVOCATION);

    public UpdateHint() {
        super(true, true, AbstractHint.HintSeverity.WARNING);
    }

    @Override
    public Set<Kind> getTreeKinds() {
        return TREE_KINDS;
    }

    @Override
    public List<ErrorDescription> run(CompilationInfo info, TreePath treePath) {
        Tree t = treePath.getLeaf();
        Element el = info.getTrees().getElement(treePath);
        String name = el.getSimpleName().toString();
        if (name.equals("updateGeometricState") || name.equals("updateLogicalState") || name.equals("updateModelBound")) {
            JTextComponent editor = EditorRegistry.lastFocusedComponent();
            Document doc = editor.getDocument();
            SourcePositions sp = info.getTrees().getSourcePositions();
            int start = (int) sp.getStartPosition(info.getCompilationUnit(), t);
            int end = (int) sp.getEndPosition(info.getCompilationUnit(), t);
            String bodyText = info.getText().substring(start, end);
            List<Fix> fixes = new ArrayList<Fix>();
            fixes.add(new MessagesFix(doc, start, bodyText));
            return Collections.<ErrorDescription>singletonList(ErrorDescriptionFactory.createErrorDescription(getSeverity().toEditorSeverity(), getDisplayName(), fixes, info.getFileObject(), (int) info.getTrees().getSourcePositions().getStartPosition(info.getCompilationUnit(), t), (int) info.getTrees().getSourcePositions().getEndPosition(info.getCompilationUnit(), t)));
        }
        return null;
    }

    @Override
    public void cancel() {
    }

    @Override
    public String getDisplayName() {
        return "Updating is not needed in jME3, check your update order if you need to call this.";
    }

    @Override
    public String getId() {
        return "Update States / Bound";
    }

    @Override
    public String getDescription() {
        return "Checks for calls to updateGeometricState(), updateLogicalState() and updateModelBound().";
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
