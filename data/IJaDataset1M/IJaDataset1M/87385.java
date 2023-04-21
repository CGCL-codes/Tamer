package com.googlecode.gwt.test.plugin.editors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.DefaultHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.hyperlink.URLHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

public class GWTCsvConfiguration extends TextSourceViewerConfiguration {

    private GWTCsvScanner scanner;

    private ColorManager colorManager;

    private KeyWords keyWords;

    private IProject currentProject;

    public GWTCsvConfiguration(ColorManager colorManager, KeyWords keyWords, IProject currentProject) {
        this.colorManager = colorManager;
        this.keyWords = keyWords;
        this.currentProject = currentProject;
    }

    @Override
    public IHyperlinkPresenter getHyperlinkPresenter(ISourceViewer sourceViewer) {
        RGB linkColor = new RGB(0, 0, 200);
        return new DefaultHyperlinkPresenter(linkColor);
    }

    @Override
    public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
        IHyperlinkDetector[] detectors = new IHyperlinkDetector[2];
        detectors[0] = new URLHyperlinkDetector();
        detectors[1] = new GWTCsvHyperLinkDetector(currentProject, keyWords);
        return detectors;
    }

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return new String[] { IDocument.DEFAULT_CONTENT_TYPE };
    }

    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
        return super.getDoubleClickStrategy(sourceViewer, contentType);
    }

    public GWTCsvScanner getScanner() {
        if (scanner == null) {
            scanner = new GWTCsvScanner(colorManager, keyWords);
        }
        return scanner;
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
        assistant.setContentAssistProcessor(new GWTCsvContentAssistProcessor(this.keyWords, this.currentProject), IDocument.DEFAULT_CONTENT_TYPE);
        assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        return assistant;
    }
}
