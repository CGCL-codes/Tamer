package com.aptana.ide.editor.scriptdoc;

import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import com.aptana.ide.editor.scriptdoc.contentassist.ScriptDocContentAssistProcessor;
import com.aptana.ide.editor.scriptdoc.formatting.ScriptDocAutoIndentStrategy;
import com.aptana.ide.editor.scriptdoc.parsing.ScriptDocMimeType;
import com.aptana.ide.editors.unified.BaseContributor;
import com.aptana.ide.editors.unified.EditorFileContext;
import com.aptana.ide.editors.unified.UnifiedReconcilingStrategy;

/**
 * @author Robin Debreuil
 */
public class ScriptDocContributor extends BaseContributor {

    private ScriptDocReconcilingStrategy _reconcilingStrategy;

    private IAutoEditStrategy[] _autoEditStrategy;

    private IContentAssistProcessor _contentAssistProcessor;

    private boolean isDisposing = false;

    private ScriptDocColorizer _colorizer;

    /**
	 * ScriptDocContributor
	 */
    public ScriptDocContributor() {
        this._colorizer = new ScriptDocColorizer();
        this._colorizer = (this._colorizer == null) ? null : this._colorizer;
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditorContributor#getLocalContentType()
	 */
    public String getLocalContentType() {
        return ScriptDocMimeType.MimeType;
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditorContributor#getReconcilingStrategy()
	 */
    public UnifiedReconcilingStrategy getReconcilingStrategy() {
        _reconcilingStrategy = new ScriptDocReconcilingStrategy();
        return _reconcilingStrategy;
    }

    /**
	 * @see com.aptana.ide.editors.unified.BaseContributor#getLocalAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer,
	 *      java.lang.String)
	 */
    public IAutoEditStrategy[] getLocalAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
        if (contentType.equals(ScriptDocMimeType.MimeType)) {
            _autoEditStrategy = new IAutoEditStrategy[] { new ScriptDocAutoIndentStrategy(this.getFileContext(), this.getParentConfiguration(), sourceViewer) };
            return _autoEditStrategy;
        }
        return null;
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditorContributor#getLocalContentAssistProcessor(org.eclipse.jface.text.source.ISourceViewer,
	 *      java.lang.String)
	 */
    public IContentAssistProcessor getLocalContentAssistProcessor(ISourceViewer sourceViewer, String contentType) {
        if (contentType.equals(ScriptDocMimeType.MimeType)) {
            EditorFileContext context = getFileContext();
            if (context != null) {
                _contentAssistProcessor = new ScriptDocContentAssistProcessor(context, (SourceViewer) sourceViewer);
                return _contentAssistProcessor;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditorContributor#dispose()
	 */
    public void dispose() {
        if (isDisposing) {
            return;
        }
        isDisposing = true;
        if (_colorizer != null) {
            _colorizer.dispose();
            _colorizer = null;
        }
        _autoEditStrategy = null;
        _reconcilingStrategy = null;
        _contentAssistProcessor = null;
        super.dispose();
    }
}
