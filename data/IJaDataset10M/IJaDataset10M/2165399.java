package org.eclipse.mylyn.internal.bugs.java;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author Mik Kersten
 */
public abstract class AbstractHyperlinkDetector implements IHyperlinkDetector {

    private ITextEditor fEditor;

    public abstract IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks);

    public ITextEditor getEditor() {
        return fEditor;
    }

    public void setEditor(ITextEditor editor) {
        this.fEditor = editor;
    }
}
