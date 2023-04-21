package org.xulbooster.eclipse.xb.ui.editors.xul.internal.correction;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.wst.xml.ui.internal.Logger;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;

public class RemoveUnknownElementQuickFixProposal implements ICompletionProposal, ICompletionProposalExtension2 {

    private Object fAdditionalFixInfo = null;

    private String fDisplayString;

    private Image fImage;

    private Point fSelection;

    public RemoveUnknownElementQuickFixProposal(Object additionalFixInfo, Image image, String displayString) {
        fAdditionalFixInfo = additionalFixInfo;
        fImage = image;
        fDisplayString = displayString;
    }

    public void apply(IDocument document) {
    }

    public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
        int startTagOffset = ((Integer) ((Object[]) fAdditionalFixInfo)[0]).intValue();
        int startTagLength = ((Integer) ((Object[]) fAdditionalFixInfo)[1]).intValue();
        int endTagOffset = ((Integer) ((Object[]) fAdditionalFixInfo)[2]).intValue();
        int endTagLength = ((Integer) ((Object[]) fAdditionalFixInfo)[3]).intValue();
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        if (endTagOffset != -1) {
            multiTextEdit.addChild(new DeleteEdit(endTagOffset, endTagLength));
            fSelection = new Point(endTagOffset, 0);
        }
        if (startTagOffset != -1) {
            multiTextEdit.addChild(new DeleteEdit(startTagOffset, startTagLength));
            fSelection = new Point(startTagOffset, 0);
        }
        try {
            multiTextEdit.apply(viewer.getDocument());
        } catch (MalformedTreeException e) {
            Logger.log(Logger.INFO, e.getMessage());
        } catch (BadLocationException e) {
            Logger.log(Logger.INFO, e.getMessage());
        }
    }

    public String getAdditionalProposalInfo() {
        return null;
    }

    public IContextInformation getContextInformation() {
        return null;
    }

    public String getDisplayString() {
        if (fDisplayString == null) {
            fDisplayString = XMLUIMessages.QuickFixProcessorXML_11;
        }
        return fDisplayString;
    }

    public Image getImage() {
        return fImage;
    }

    public Point getSelection(IDocument document) {
        return fSelection;
    }

    public void selected(ITextViewer viewer, boolean smartToggle) {
    }

    public void unselected(ITextViewer viewer) {
    }

    public boolean validate(IDocument document, int offset, DocumentEvent event) {
        return false;
    }
}
