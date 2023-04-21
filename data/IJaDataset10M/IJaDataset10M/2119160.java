package com.aptana.ide.editors.unified;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.editors.UnifiedEditorsPlugin;
import com.aptana.ide.lexer.LexemeList;
import com.aptana.ide.parsing.IParseState;

/**
 * UnifiedReconcilingStrategy
 */
public class UnifiedReconcilingStrategy implements IReconcilingStrategy, IReconcilingStrategyExtension {

    private UnifiedEditor _editor;

    private IDocument _document;

    private boolean _isDisposing = false;

    /** holds the calculated positions */
    protected final HashSet positions = new HashSet();

    /**
	 * dispose
	 */
    public void dispose() {
        this._isDisposing = true;
        this._editor = null;
        this._document = null;
    }

    /**
	 * @return Returns the editor.
	 */
    public IUnifiedEditor getEditor() {
        return this._editor;
    }

    /**
	 * setEditor
	 * 
	 * @param editor
	 */
    public void setEditor(UnifiedEditor editor) {
        this._editor = editor;
    }

    /**
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#setDocument(org.eclipse.jface.text.IDocument)
	 */
    public void setDocument(IDocument document) {
        this._document = document;
    }

    /**
	 * getDocument
	 * 
	 * @return IDocument
	 */
    public IDocument getDocument() {
        return this._document;
    }

    /**
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.reconciler.DirtyRegion,
	 *      org.eclipse.jface.text.IRegion)
	 */
    public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
        calculatePositions();
    }

    /**
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategy#reconcile(org.eclipse.jface.text.IRegion)
	 */
    public void reconcile(IRegion partition) {
        calculatePositions();
    }

    /**
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#setProgressMonitor(org.eclipse.core.runtime.IProgressMonitor)
	 */
    public void setProgressMonitor(IProgressMonitor monitor) {
    }

    /**
	 * @see org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension#initialReconcile()
	 */
    public void initialReconcile() {
        if (this._isDisposing) {
            return;
        }
        calculatePositions();
    }

    /**
	 * calculatePositions
	 */
    protected void calculatePositions() {
        if (this._isDisposing) {
            return;
        }
        positions.clear();
        LexemeList ll = this._editor.getFileContext().getLexemeList();
        if (ll != null) {
            IParseState parseState = this._editor.getFileContext().getParseState();
            parseForRegions(parseState);
        }
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                if (!_isDisposing) {
                    _editor.updateFoldingStructure(positions);
                }
            }
        });
    }

    /**
	 * emitPosition
	 * 
	 * @param startOffset
	 * @param length
	 */
    public void emitPosition(int startOffset, int length) {
        if (this._isDisposing) {
            return;
        }
        try {
            IDocument doc = this._document;
            if (doc != null) {
                int startLine = doc.getLineOfOffset(startOffset);
                int endLine = doc.getLineOfOffset(startOffset + length);
                if (startLine < endLine) {
                    int start = doc.getLineOffset(startLine);
                    int end = doc.getLineOffset(endLine) + doc.getLineLength(endLine);
                    Position position = new Position(start, end - start);
                    positions.add(position);
                }
            }
        } catch (BadLocationException e) {
            IdeLog.logInfo(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedReconcilingStrategy_EmitPositionFailed, e);
        }
    }

    /**
	 * parseForRegions
	 * 
	 * @param parseState
	 */
    public void parseForRegions(IParseState parseState) {
    }

    /**
	 * @return Returns the positions.
	 */
    public Set getPositions() {
        return positions;
    }
}
