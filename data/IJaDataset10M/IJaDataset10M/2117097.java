package com.sf.plctest.testmodel.statementcoverage;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import com.sf.plctest.testmodel.coveragepackage.Instruction;
import com.sf.plctest.testmodel.statementcoverage.ILContentOutlinePage.NetworkData;

public class ILEditor extends AbstractDecoratedTextEditor {

    private ISharedTextColors colors = getSharedColors();

    OccurrencesUpdater fOccurrencesUpdater;

    protected ILContentOutlinePage contentOutlinePage;

    ISourceViewer viewer = null;

    protected IPartListener partListener = new IPartListener() {

        public void partActivated(IWorkbenchPart p) {
        }

        public void partBroughtToTop(IWorkbenchPart p) {
        }

        public void partClosed(IWorkbenchPart p) {
        }

        public void partDeactivated(IWorkbenchPart p) {
        }

        public void partOpened(IWorkbenchPart p) {
        }
    };

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        site.getPage().addPartListener(partListener);
        super.init(site, input);
    }

    public ILEditor() {
        setSourceViewerConfiguration(new SourceViewerConfiguration() {

            @Override
            public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
                viewer = sourceViewer;
                PresentationReconciler reconciler = new PresentationReconciler();
                DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
                reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
                reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
                return reconciler;
            }
        });
    }

    public void setInstrList(String blockName, EList<Instruction> blockInstrList, EList<Instruction> executedInstrList) {
        if (executedInstrList != null && blockInstrList != null && blockName != null) {
            if (fOccurrencesUpdater != null) fOccurrencesUpdater.update(getSourceViewer(), blockInstrList, executedInstrList, blockName);
            if (contentOutlinePage != null) contentOutlinePage.update(viewer, blockInstrList, executedInstrList, blockName);
        }
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        fOccurrencesUpdater = new OccurrencesUpdater();
    }

    private ITokenScanner getScanner() {
        RuleBasedScanner scanner = new RuleBasedScanner();
        IRule[] rules = new IRule[1];
        rules[0] = createCommentRule();
        scanner.setRules(rules);
        return scanner;
    }

    private IRule createCommentRule() {
        IToken stepToken = new Token((Object) (new TextAttribute(colors.getColor(new RGB(63, 127, 95)), colors.getColor(new RGB(255, 255, 255)), SWT.NORMAL)));
        EndOfLineRule rule = new EndOfLineRule("//", stepToken);
        rule.setColumnConstraint(-1);
        return rule;
    }

    public Object getAdapter(Class key) {
        if (key.equals(IContentOutlinePage.class)) {
            return getContentOutlinePage();
        } else {
            return super.getAdapter(key);
        }
    }

    public IContentOutlinePage getContentOutlinePage() {
        if (contentOutlinePage == null) {
            contentOutlinePage = new ILContentOutlinePage(viewer.getDocument());
        }
        contentOutlinePage.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                TreeSelection ts = (TreeSelection) event.getSelection();
                Object o = ts.getFirstElement();
                if (o instanceof NetworkData) {
                    NetworkData nd = (NetworkData) o;
                    setHighlightRange(nd.position, nd.networkName.length(), true);
                }
            }
        });
        return contentOutlinePage;
    }
}
