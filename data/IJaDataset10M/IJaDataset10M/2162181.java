package de.l3s.ppt.rcp.peertrust;

import org.eclipse.ui.texteditor.AbstractTextEditor;
import de.l3s.ppt.rcp.DocumentProvider;

public class PeerTrustEditor extends AbstractTextEditor {

    private PeerTrustColorManager colorManager;

    public PeerTrustEditor() {
        super();
        setKeyBindingScopes(new String[] { "org.eclipse.ui.textEditorScope" });
        internal_init();
    }

    /**
	 * Initializes the document provider and source viewer configuration.
	 * Called by the constructor. Subclasses may replace this method.
	 */
    protected void internal_init() {
        configureInsertMode(SMART_INSERT, false);
        colorManager = new PeerTrustColorManager();
        setSourceViewerConfiguration(new PeerTrustConfiguration(colorManager));
        setDocumentProvider(new DocumentProvider());
    }
}
