package com.nhncorp.cubridqa.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.ui.part.EditorPart;

/**
 * 
 * Configuration for editor.
 * @ClassName: Configuration
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class Configuration extends SourceViewerConfiguration {

    private EditorPart editor;

    public Configuration(EditorPart editor) {
        this.editor = editor;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant contentAssistant = new ContentAssistant();
        contentAssistant.setContentAssistProcessor(new ObjectContentAssistant(), IDocument.DEFAULT_CONTENT_TYPE);
        contentAssistant.enableAutoActivation(true);
        contentAssistant.setAutoActivationDelay(500);
        return contentAssistant;
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new CodeScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }
}
