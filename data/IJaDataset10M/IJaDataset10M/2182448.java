package org.exist.eclipse.xquery.ui.action.refactoring;

import java.io.BufferedReader;
import java.io.StringReader;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.ReplaceEdit;
import org.exist.eclipse.xquery.ui.XQueryUI;
import org.exist.eclipse.xquery.ui.internal.editor.XQueryEditor;

/**
 * Toggles comment of the currently active {@link XQueryEditor}, does nothing if
 * another editor is active.
 * 
 * @author Christian Oetterli
 * @version $Id: $
 */
public class ToggleCommentAction extends ARefactorAction {

    public static final String ID = ToggleCommentAction.class.getName();

    public ToggleCommentAction() {
        setActionDefinitionId(ID);
    }

    @Override
    protected void doRun(XQueryEditor editor) {
        try {
            IDocument document = getDocument(editor);
            final String startTag = "(:";
            final String endTag = ":)";
            ISourceViewer viewer = editor.getViewer();
            Point range = viewer.getSelectedRange();
            int len = range.y;
            int off = range.x;
            ReplaceEdit edit = null;
            int caretFix = 0;
            if (len > 0) {
                String sel = document.get(off, len);
                int ps = sel.indexOf(startTag);
                int pe = sel.lastIndexOf(endTag);
                String newText = null;
                if (ps == -1 && pe == -1) {
                    newText = startTag + sel + endTag;
                } else if (ps != -1 && pe != -1 && ps < pe) {
                    newText = sel.replace(startTag, "").replace(endTag, "");
                }
                if (newText != null) {
                    edit = new ReplaceEdit(off, len, newText);
                }
            } else {
                int line = document.getLineOfOffset(off);
                int lineLen = document.getLineLength(line);
                int lineOff = document.getLineOffset(line);
                String sel = document.get(lineOff, lineLen);
                int q = sel.length();
                try {
                    BufferedReader reader = new BufferedReader(new StringReader(sel));
                    sel = reader.readLine();
                    q -= sel.length();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                int ps = sel.indexOf(startTag);
                int pe = sel.lastIndexOf(endTag);
                String newText = null;
                if (ps == -1 && pe == -1) {
                    newText = startTag + sel + endTag;
                    edit = new ReplaceEdit(lineOff, newText.length() - (q * 2), newText);
                    caretFix = 2;
                } else if (ps != -1 && pe != -1 && ps < pe) {
                    newText = sel.replace(startTag, "").replace(endTag, "");
                    edit = new ReplaceEdit(lineOff, lineLen - q, newText);
                    caretFix = -2;
                }
            }
            if (edit != null) {
                int oldCaretOffset = viewer.getTextWidget().getCaretOffset();
                edit.apply(document);
                viewer.setSelectedRange(oldCaretOffset + caretFix, 0);
            }
        } catch (Exception e) {
            XQueryUI.getDefault().getLog().log(new Status(IStatus.ERROR, XQueryUI.PLUGIN_ID, "An error occured while toggle comments: " + e, e));
        }
    }
}
