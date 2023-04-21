package com.jacum.cms.rcp.ui.editors.item.stylededitor;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * Class is responsible for configuration editor viewer.
 * In our implementation return TextHover object.
 * 
 * @author rich
 */
public class RichTextEditorConfiguration extends SourceViewerConfiguration {

    /**
	 * Constructs new RichTextEditorConfiguration
	 */
    public RichTextEditorConfiguration() {
        super();
    }

    @Override
    public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
        return new TextHover();
    }
}
