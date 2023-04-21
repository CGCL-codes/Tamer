package org.aspencloud.simple9.builder.editor;

import org.aspencloud.simple9.builder.editor.actions.DefineFoldingRegionAction;
import org.aspencloud.simple9.builder.outline.EspOutlinePage;
import org.aspencloud.simple9.builder.views.dom.Block;
import org.aspencloud.simple9.builder.views.dom.Element;
import org.aspencloud.simple9.builder.views.dom.EspCore;
import org.aspencloud.simple9.builder.views.dom.HtmlElement;
import org.aspencloud.simple9.builder.views.dom.HtmlLineBlock;
import org.aspencloud.simple9.builder.views.dom.MetaBlock;
import org.aspencloud.simple9.builder.views.dom.MetaElement;
import org.aspencloud.simple9.builder.views.dom.MultiLineBlock;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class EspEditor extends TextEditor {

    public static final String ID = EspEditor.class.getCanonicalName();

    private EspOutlinePage outlinePage;

    private ProjectionSupport projectionSupport;

    private IDocument document;

    /**
	 * Default constructor.
	 */
    public EspEditor() {
        super();
    }

    @Override
    protected void handleCursorPositionChanged() {
        super.handleCursorPositionChanged();
        if (outlinePage != null && document != null) {
            outlinePage.removeSelectionChangedListener(selListener);
            String[] sa = getCursorPosition().split(" *: *");
            int line = Integer.parseInt(sa[0]) - 1;
            int column = Integer.parseInt(sa[1]);
            Block block = EspCore.get(document).getBlock(line);
            if (block != null) {
                if (block instanceof HtmlLineBlock) {
                    HtmlElement element = ((HtmlLineBlock) block).getElement(column);
                    if (((HtmlLineBlock) block).getElementIndex(element) == 0) {
                        outlinePage.setSelection(new StructuredSelection(block));
                    } else {
                        outlinePage.setSelection(new StructuredSelection(element));
                    }
                } else if (block instanceof MetaBlock) {
                    MetaElement element = ((MetaBlock) block).getElement(line);
                    if (element == null) {
                        outlinePage.setSelection(new StructuredSelection(block));
                    } else {
                        outlinePage.setSelection(new StructuredSelection(element));
                    }
                } else {
                    outlinePage.setSelection(new StructuredSelection(block));
                }
            }
            outlinePage.addSelectionChangedListener(selListener);
        }
    }

    private ISelectionChangedListener selListener = new ISelectionChangedListener() {

        @Override
        public void selectionChanged(SelectionChangedEvent event) {
            ISelection selection = event.getSelection();
            if (selection.isEmpty()) {
                resetHighlightRange();
            } else if (document != null) {
                EspOutlinePage page = outlinePage;
                outlinePage = null;
                int offset = -1;
                int length = -1;
                Object sel = ((IStructuredSelection) selection).getFirstElement();
                try {
                    if (sel instanceof Block) {
                        Block block = (Block) sel;
                        offset = document.getLineOffset(block.getLine() - 1) + block.getLevel() + block.getStart();
                        if (sel instanceof MultiLineBlock) {
                            length = document.getLineOffset(block.getLine()) - offset - 1;
                        } else {
                            length = block.toString().length();
                        }
                    } else if (sel instanceof Element) {
                        Element element = (Element) sel;
                        offset = document.getLineOffset(element.getLine() - 1) + element.getLevel() + element.getStart();
                        length = element.toString().length();
                    }
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                if (offset > -1) {
                    try {
                        selectAndReveal(offset, length);
                    } catch (IllegalArgumentException x) {
                        resetHighlightRange();
                    }
                }
                outlinePage = page;
            }
        }
    };

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method extend the 
	 * actions to add those specific to the receiver
	 */
    protected void createActions() {
        super.createActions();
        IAction a = new TextOperationAction(EspEditorMessages.getResourceBundle(), "ContentAssistProposal.", this, ISourceViewer.CONTENTASSIST_PROPOSALS);
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssistProposal", a);
        a = new TextOperationAction(EspEditorMessages.getResourceBundle(), "ContentAssistTip.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);
        a.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
        setAction("ContentAssistTip", a);
        a = new DefineFoldingRegionAction(EspEditorMessages.getResourceBundle(), "DefineFoldingRegion.", this);
        setAction("DefineFoldingRegion", a);
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs any extra 
	 * disposal actions required by the java editor.
	 */
    public void dispose() {
        if (outlinePage != null) {
            outlinePage.setInput(null);
        }
        super.dispose();
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs any extra 
	 * revert behavior required by the java editor.
	 */
    public void doRevertToSaved() {
        super.doRevertToSaved();
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs any extra 
	 * save behavior required by the java editor.
	 * 
	 * @param monitor the progress monitor
	 */
    public void doSave(IProgressMonitor monitor) {
        super.doSave(monitor);
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs any extra 
	 * save as behavior required by the java editor.
	 */
    public void doSaveAs() {
        super.doSaveAs();
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs sets the 
	 * input of the outline page after AbstractTextEditor has set input.
	 * 
	 * @param input the editor input
	 * @throws CoreException in case the input can not be set
	 */
    public void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        if (input != null) {
            document = getDocumentProvider().getDocument(input);
        } else {
            document = null;
        }
        if (outlinePage != null) {
            outlinePage.setInput(document);
        }
    }

    protected void editorContextMenuAboutToShow(IMenuManager menu) {
        super.editorContextMenuAboutToShow(menu);
        addAction(menu, "ContentAssistProposal");
        addAction(menu, "ContentAssistTip");
        addAction(menu, "DefineFoldingRegion");
    }

    /** The <code>JavaEditor</code> implementation of this 
	 * <code>AbstractTextEditor</code> method performs gets
	 * the java content outline page if request is for a an 
	 * outline page.
	 * 
	 * @param required the required type
	 * @return an adapter for the required type or <code>null</code>
	 */
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class required) {
        if (IContentOutlinePage.class.equals(required)) {
            if (outlinePage == null) {
                outlinePage = new EspOutlinePage(getDocumentProvider(), this);
                if (document != null) {
                    outlinePage.setInput(document);
                    outlinePage.addSelectionChangedListener(selListener);
                }
            }
            return outlinePage;
        }
        if (projectionSupport != null) {
            Object adapter = projectionSupport.getAdapter(getSourceViewer(), required);
            if (adapter != null) {
                return adapter;
            }
        }
        return super.getAdapter(required);
    }

    protected void initializeEditor() {
        super.initializeEditor();
        setSourceViewerConfiguration(new EspSourceViewerConfiguration());
    }

    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        fAnnotationAccess = createAnnotationAccess();
        fOverviewRuler = createOverviewRuler(getSharedColors());
        ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        getSourceViewerDecorationSupport(viewer);
        return viewer;
    }

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
        projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        projectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error");
        projectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning");
        projectionSupport.install();
        viewer.doOperation(ProjectionViewer.TOGGLE);
        viewer.addTextInputListener(new ITextInputListener() {

            @Override
            public void inputDocumentAboutToBeChanged(IDocument oldInput, IDocument newInput) {
            }

            @Override
            public void inputDocumentChanged(IDocument oldInput, IDocument newInput) {
                if (oldInput != null) {
                    EspCore.remove(oldInput);
                }
            }
        });
        viewer.addTextListener(new ITextListener() {

            @Override
            public void textChanged(TextEvent event) {
                System.out.println("text changed event");
            }
        });
        viewer.getTextWidget().addVerifyListener(new VerifyListener() {

            @Override
            public void verifyText(VerifyEvent e) {
                if (e.stateMask == SWT.NONE && " ".equals(e.text)) {
                    e.doit = false;
                    StyledText txt = getSourceViewer().getTextWidget();
                    int offset = txt.getCaretOffset();
                    int line = txt.getLineAtOffset(offset);
                    int start = txt.getOffsetAtLine(line);
                    if (start < offset) {
                        char[] ca = txt.getText(start, offset - 1).toCharArray();
                        for (char c : ca) {
                            if (c != '\t' && c != '\n' && c != '\r') {
                                e.doit = true;
                                break;
                            }
                        }
                    }
                    if (e.doit == false) {
                        e.text = "\t";
                        e.doit = true;
                    }
                }
            }
        });
    }

    protected void adjustHighlightRange(int offset, int length) {
        ISourceViewer viewer = getSourceViewer();
        if (viewer instanceof ITextViewerExtension5) {
            ITextViewerExtension5 extension = (ITextViewerExtension5) viewer;
            extension.exposeModelRange(new Region(offset, length));
        }
    }
}
