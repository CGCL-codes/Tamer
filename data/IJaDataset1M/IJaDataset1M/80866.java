package it.kion.util.ui.ulog2.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.*;
import org.eclipse.jface.text.presentation.*;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.*;
import it.kion.util.ui.ulog2.editor.contentassist.*;
import it.kion.util.ui.ulog2.editor.scanners.*;

public class PropertiesConfiguration extends SourceViewerConfiguration {

    private final TokenManager tokenManager;

    public PropertiesConfiguration(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return PropertiesPartitionScanner.getLegalContentTypes();
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr;
        dr = new DefaultDamagerRepairer(new DefaultScanner(tokenManager));
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        dr = new DefaultDamagerRepairer(new CommentScanner(tokenManager));
        reconciler.setDamager(dr, PropertiesPartitionScanner.LOG4J_COMMENT);
        reconciler.setRepairer(dr, PropertiesPartitionScanner.LOG4J_COMMENT);
        dr = new DefaultDamagerRepairer(new ValueScanner(tokenManager));
        reconciler.setDamager(dr, PropertiesPartitionScanner.LOG4J_VALUE);
        reconciler.setRepairer(dr, PropertiesPartitionScanner.LOG4J_VALUE);
        return reconciler;
    }

    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
        assistant.setContentAssistProcessor(new PropertiesAssistant(), IDocument.DEFAULT_CONTENT_TYPE);
        assistant.enableAutoActivation(true);
        assistant.enableAutoInsert(true);
        return assistant;
    }
}
