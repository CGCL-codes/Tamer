package org.plog4u.wiki.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

public class WikiSourceViewerConfiguration extends SourceViewerConfiguration {

    /**
	 * A no-op implementation of <code>IAnnotationHover</code> that will
	 * trigger the text editor to set up annotation hover support.
	 */
    private static final class NullHover implements IAnnotationHover {

        public String getHoverInfo(ISourceViewer sourceViewer, int lineNumber) {
            return null;
        }
    }

    private final ISharedTextColors fColors;

    private WikiEditor fEditor;

    public WikiSourceViewerConfiguration(WikiEditor editor, ISharedTextColors colors) {
        fEditor = editor;
        fColors = colors;
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getWikipediaScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    private ITokenScanner getWikipediaScanner() {
        RuleBasedScanner scanner = new RuleBasedScanner();
        IRule[] rules = new IRule[14];
        rules[0] = createHeader6Rule();
        rules[1] = createHeader5Rule();
        rules[2] = createHeader4Rule();
        rules[3] = createHeader3Rule();
        rules[4] = createHeader2Rule();
        rules[5] = createHeader1Rule();
        rules[6] = createHRRule();
        rules[7] = createLinkRule();
        rules[8] = createExternalHTTPRule();
        rules[9] = createListRule();
        rules[10] = createNumberedListRule();
        rules[11] = createBoldItalicRule();
        rules[12] = createBoldRule();
        rules[13] = createItalicRule();
        scanner.setRules(rules);
        return scanner;
    }

    private IRule createBoldRule() {
        IToken boldToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 0)), null, SWT.BOLD));
        SingleLineRule singleLineRule = new SingleLineRule("'''", "'''", boldToken);
        return singleLineRule;
    }

    private IRule createItalicRule() {
        IToken italicToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 0)), null, SWT.ITALIC));
        SingleLineRule singleLineRule = new SingleLineRule("''", "''", italicToken);
        return singleLineRule;
    }

    private IRule createBoldItalicRule() {
        IToken boldToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 0)), null, SWT.BOLD | SWT.ITALIC));
        SingleLineRule singleLineRule = new SingleLineRule("'''''", "'''''", boldToken);
        return singleLineRule;
    }

    private IRule createHRRule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.NONE));
        SingleLineRule singleLineRule = new SingleLineRule("----", "\n", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader1Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("=", "=", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader2Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("==", "==", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader3Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("===", "===", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader4Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("====", "====", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader5Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("=====", "=====", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createHeader6Rule() {
        IToken quantityToken = new Token(new TextAttribute(fColors.getColor(new RGB(0, 0, 140)), null, SWT.BOLD, null));
        SingleLineRule singleLineRule = new SingleLineRule("======", "======", quantityToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createListRule() {
        IToken dashToken = new Token(new TextAttribute(fColors.getColor(new RGB(63, 127, 95)), null, SWT.NONE));
        SingleLineRule singleLineRule = new SingleLineRule("*", "\n", dashToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createNumberedListRule() {
        IToken dashToken = new Token(new TextAttribute(fColors.getColor(new RGB(63, 127, 95)), null, SWT.NONE));
        SingleLineRule singleLineRule = new SingleLineRule("#", "\n", dashToken);
        singleLineRule.setColumnConstraint(0);
        return singleLineRule;
    }

    private IRule createLinkRule() {
        IToken stepToken = new Token(new TextAttribute(fColors.getColor(new RGB(200, 100, 100)), null, SWT.NONE));
        SingleLineRule stepRule = new SingleLineRule("[[", "]]", stepToken);
        return stepRule;
    }

    private IRule createExternalHTTPRule() {
        IToken stepToken = new Token(new TextAttribute(fColors.getColor(new RGB(200, 100, 100)), null, SWT.NONE));
        SingleLineRule stepRule = new SingleLineRule("[http", "]", stepToken);
        return stepRule;
    }

    public IReconciler getReconciler(ISourceViewer sourceViewer) {
        WikiReconcilingStrategy strategy = new WikiReconcilingStrategy(fEditor);
        MonoReconciler reconciler = new MonoReconciler(strategy, false);
        reconciler.setProgressMonitor(new NullProgressMonitor());
        reconciler.setDelay(500);
        return reconciler;
    }

    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        ContentAssistant assistant = new ContentAssistant();
        IContentAssistProcessor processor = new WikiCompletionProcessor(fEditor);
        assistant.setContentAssistProcessor(processor, IDocument.DEFAULT_CONTENT_TYPE);
        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
        assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
        assistant.enableAutoInsert(true);
        return assistant;
    }

    public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
        return new NullHover();
    }
}
