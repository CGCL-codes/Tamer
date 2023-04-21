package as.ide.ui.editors.contentAssistant;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;
import as.ide.core.dom.MethodDef;
import as.ide.core.dom.Param;
import as.ide.ui.editors.utils.ImageManager;

public class ASMethodCompletionProposal extends ASCompletionProposal implements ICompletionProposalExtension6 {

    private MethodDef fMethod;

    private int applyOffset = -1;

    public ASMethodCompletionProposal(ASContentAssistProcessor processor, String name, int offset, int len, int pos, String javaDoc, MethodDef mtd) {
        super(processor, name, offset, len, pos, ImageManager.getImage(mtd), mtd.toString(), null, javaDoc);
        this.fMethod = mtd;
    }

    @Override
    public Point getSelection(IDocument document) {
        Param[] params = fMethod.getParams();
        if (params == null || params.length == 0) return super.getSelection(document);
        int base = this.fReplacementOffset;
        int start = fReplacementString.indexOf("(");
        String pName = params[0].getName();
        int len = pName.length();
        int tmp = fReplacementString.indexOf(pName, start);
        return new Point(base + tmp, len);
    }

    @Override
    public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
        IDocument doc = viewer.getDocument();
        applyOffset = offset;
        super.apply(doc);
        Param[] params = fMethod.getParams();
        if (params == null || params.length == 0) return;
        try {
            int base = this.fReplacementOffset;
            int start = fReplacementString.indexOf("(");
            int end = fReplacementString.indexOf(")");
            LinkedModeModel model = new LinkedModeModel();
            for (Param pm : params) {
                String pName = pm.getName();
                int len = pName.length();
                int tmp = fReplacementString.indexOf(pName, start);
                LinkedPositionGroup group = new LinkedPositionGroup();
                group.addPosition(new LinkedPosition(doc, base + tmp, len));
                model.addGroup(group);
                start = tmp + len;
            }
            model.forceInstall();
            LinkedModeUI ui = new EditorLinkedModeUI(model, viewer);
            ui.setExitPosition(viewer, base + end + 1, 0, Integer.MAX_VALUE);
            ui.setExitPolicy(new ASEditorExitPolicy());
            ui.setDoContextInfo(true);
            ui.setCyclingMode(LinkedModeUI.CYCLE_WHEN_NO_PARENT);
            ui.enter();
        } catch (BadLocationException e) {
        }
    }

    @Override
    public boolean validate(IDocument document, int offset, DocumentEvent event) {
        return super.validate(document, offset, event);
    }

    @Override
    public StyledString getStyledDisplayString() {
        return null;
    }

    public boolean containsOffset(int offset) {
        return this.applyOffset >= 0;
    }
}
