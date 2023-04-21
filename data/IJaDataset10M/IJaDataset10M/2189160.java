package org.remus.infomngmnt.common.ui.jface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ActiveShellExpression;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

public class AnnotatingQuickFixTextBox implements ModifyListener, TraverseListener, FocusListener, Observer {

    private final StyledText fTextField;

    private final String fMessage;

    public static final String OK_REQUESTED = "OkRequested";

    public static final String COMMENT_MODIFIED = "CommentModified";

    private String fText;

    private final List<IPropertyChangeListener> listeners;

    private final Composite cc;

    public AnnotatingQuickFixTextBox(final Composite composite, final String message, final String initialText) {
        this.listeners = new ArrayList<IPropertyChangeListener>();
        this.fMessage = message;
        this.fText = initialText;
        AnnotationModel annotationModel = new AnnotationModel();
        IAnnotationAccess annotationAccess = new DefaultMarkerAnnotationAccess();
        this.cc = new Composite(composite, SWT.BORDER);
        this.cc.setLayout(new FillLayout());
        this.cc.setLayoutData(new GridData(GridData.FILL_BOTH));
        final SourceViewer sourceViewer = new SourceViewer(this.cc, null, null, true, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        this.fTextField = sourceViewer.getTextWidget();
        this.fTextField.setIndent(2);
        final SourceViewerDecorationSupport support = new SourceViewerDecorationSupport(sourceViewer, null, annotationAccess, EditorsUI.getSharedTextColors());
        Iterator e = new MarkerAnnotationPreferences().getAnnotationPreferences().iterator();
        while (e.hasNext()) {
            support.setAnnotationPreference((AnnotationPreference) e.next());
        }
        support.install(EditorsUI.getPreferenceStore());
        final IHandlerService handlerService = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
        final IHandlerActivation handlerActivation = installQuickFixActionHandler(handlerService, sourceViewer);
        final TextViewerAction cutAction = new TextViewerAction(sourceViewer, ITextOperationTarget.CUT);
        cutAction.setText("Cut");
        cutAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.CUT);
        final TextViewerAction copyAction = new TextViewerAction(sourceViewer, ITextOperationTarget.COPY);
        copyAction.setText("Copy");
        copyAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.COPY);
        final TextViewerAction pasteAction = new TextViewerAction(sourceViewer, ITextOperationTarget.PASTE);
        pasteAction.setText("Paste");
        pasteAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.PASTE);
        final TextViewerAction selectAllAction = new TextViewerAction(sourceViewer, ITextOperationTarget.SELECT_ALL);
        selectAllAction.setText("Select all");
        selectAllAction.setActionDefinitionId(IWorkbenchActionDefinitionIds.SELECT_ALL);
        MenuManager contextMenu = new MenuManager();
        contextMenu.add(cutAction);
        contextMenu.add(copyAction);
        contextMenu.add(pasteAction);
        contextMenu.add(selectAllAction);
        contextMenu.add(new Separator());
        final SubMenuManager quickFixMenu = new SubMenuManager(contextMenu);
        quickFixMenu.setVisible(true);
        quickFixMenu.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(final IMenuManager manager) {
                quickFixMenu.removeAll();
                IAnnotationModel annotationModel = sourceViewer.getAnnotationModel();
                Iterator annotationIterator = annotationModel.getAnnotationIterator();
                while (annotationIterator.hasNext()) {
                    org.eclipse.jface.text.source.Annotation annotation = (org.eclipse.jface.text.source.Annotation) annotationIterator.next();
                    if (!annotation.isMarkedDeleted() && includes(annotationModel.getPosition(annotation), sourceViewer.getTextWidget().getCaretOffset()) && sourceViewer.getQuickAssistAssistant().canFix(annotation)) {
                        ICompletionProposal[] computeQuickAssistProposals = sourceViewer.getQuickAssistAssistant().getQuickAssistProcessor().computeQuickAssistProposals(sourceViewer.getQuickAssistInvocationContext());
                        for (int i = 0; i < computeQuickAssistProposals.length; i++) {
                            final ICompletionProposal proposal = computeQuickAssistProposals[i];
                            quickFixMenu.add(new Action(proposal.getDisplayString()) {

                                @Override
                                public void run() {
                                    proposal.apply(sourceViewer.getDocument());
                                }

                                @Override
                                public ImageDescriptor getImageDescriptor() {
                                    if (proposal.getImage() != null) {
                                        return ImageDescriptor.createFromImage(proposal.getImage());
                                    }
                                    return null;
                                }
                            });
                        }
                    }
                }
            }
        });
        this.fTextField.addFocusListener(new FocusListener() {

            private IHandlerActivation cutHandlerActivation;

            private IHandlerActivation copyHandlerActivation;

            private IHandlerActivation pasteHandlerActivation;

            private IHandlerActivation selectAllHandlerActivation;

            public void focusGained(final FocusEvent e) {
                cutAction.update();
                copyAction.update();
                IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
                this.cutHandlerActivation = service.activateHandler(IWorkbenchActionDefinitionIds.CUT, new ActionHandler(cutAction), new ActiveShellExpression(composite.getShell()));
                this.copyHandlerActivation = service.activateHandler(IWorkbenchActionDefinitionIds.COPY, new ActionHandler(copyAction), new ActiveShellExpression(composite.getShell()));
                this.pasteHandlerActivation = service.activateHandler(IWorkbenchActionDefinitionIds.PASTE, new ActionHandler(pasteAction), new ActiveShellExpression(composite.getShell()));
                this.selectAllHandlerActivation = service.activateHandler(IWorkbenchActionDefinitionIds.SELECT_ALL, new ActionHandler(selectAllAction), new ActiveShellExpression(composite.getShell()));
            }

            public void focusLost(final FocusEvent e) {
                IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
                if (this.cutHandlerActivation != null) {
                    service.deactivateHandler(this.cutHandlerActivation);
                }
                if (this.copyHandlerActivation != null) {
                    service.deactivateHandler(this.copyHandlerActivation);
                }
                if (this.pasteHandlerActivation != null) {
                    service.deactivateHandler(this.pasteHandlerActivation);
                }
                if (this.selectAllHandlerActivation != null) {
                    service.deactivateHandler(this.selectAllHandlerActivation);
                }
            }
        });
        sourceViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                cutAction.update();
                copyAction.update();
            }
        });
        sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {

            public void widgetDisposed(final DisposeEvent e) {
                support.uninstall();
                handlerService.deactivateHandler(handlerActivation);
            }
        });
        Document document = new Document(initialText);
        sourceViewer.configure(new TextSourceViewerConfiguration(EditorsUI.getPreferenceStore()));
        sourceViewer.setDocument(document, annotationModel);
        this.fTextField.addTraverseListener(this);
        this.fTextField.addModifyListener(this);
        this.fTextField.addFocusListener(this);
        this.fTextField.setMenu(contextMenu.createContextMenu(this.fTextField));
        this.fTextField.selectAll();
    }

    protected boolean includes(final Position position, final int caretOffset) {
        return position.includes(caretOffset) || (position.offset + position.length) == caretOffset;
    }

    /**
	 * Installs the quick fix action handler and returns the handler activation.
	 * 
	 * @param handlerService
	 *            the handler service
	 * @param sourceViewer
	 *            the source viewer
	 * @return the handler activation
	 * @since 3.4
	 */
    private IHandlerActivation installQuickFixActionHandler(final IHandlerService handlerService, final SourceViewer sourceViewer) {
        return handlerService.activateHandler(ITextEditorActionDefinitionIds.QUICK_ASSIST, createQuickFixActionHandler(sourceViewer), new ActiveShellExpression(sourceViewer.getTextWidget().getShell()));
    }

    /**
	 * Creates and returns a quick fix action handler.
	 * 
	 * @param textOperationTarget
	 *            the target for text operations
	 * @since 3.4
	 */
    private ActionHandler createQuickFixActionHandler(final ITextOperationTarget textOperationTarget) {
        Action quickFixAction = new Action() {

            @Override
            public void run() {
                textOperationTarget.doOperation(ISourceViewer.QUICK_ASSIST);
            }
        };
        quickFixAction.setActionDefinitionId(ITextEditorActionDefinitionIds.QUICK_ASSIST);
        return new ActionHandler(quickFixAction);
    }

    public void modifyText(final ModifyEvent e) {
        final String old = this.fText;
        this.fText = this.fTextField.getText();
        firePropertyChangeChange(COMMENT_MODIFIED, old, this.fText);
    }

    public void keyTraversed(final TraverseEvent e) {
        if (e.detail == SWT.TRAVERSE_RETURN && (e.stateMask & SWT.CTRL) != 0) {
            e.doit = false;
            firePropertyChangeChange(OK_REQUESTED, null, null);
        }
    }

    public void focusGained(final FocusEvent e) {
        if (this.fText.length() > 0) {
            return;
        }
        this.fTextField.removeModifyListener(this);
        try {
            this.fTextField.setText(this.fText);
        } finally {
            this.fTextField.addModifyListener(this);
        }
    }

    public void focusLost(final FocusEvent e) {
        if (this.fText.length() > 0) {
            return;
        }
        this.fTextField.removeModifyListener(this);
        try {
            this.fTextField.setText(this.fMessage);
            this.fTextField.selectAll();
        } finally {
            this.fTextField.addModifyListener(this);
        }
    }

    public void setEnabled(final boolean enabled) {
        this.fTextField.setEnabled(enabled);
    }

    public void update(final Observable o, final Object arg) {
        if (arg instanceof String) {
            setText((String) arg);
        }
    }

    public String getText() {
        return this.fText;
    }

    private void setText(final String text) {
        if (text.length() == 0) {
            this.fTextField.setText(this.fMessage);
            this.fTextField.selectAll();
        } else {
            this.fTextField.setText(text);
        }
    }

    public void setFocus() {
        this.fTextField.setFocus();
    }

    /**
	 * @return the fTextField
	 */
    public StyledText getFTextField() {
        return this.fTextField;
    }

    public void addPropertyChangeListener(final IPropertyChangeListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void setLayoutData(final Object data) {
        this.cc.setLayoutData(data);
    }

    /**
	 * Remove the provided listener from the receiver.
	 * 
	 * @param listener
	 */
    public void removePropertyChangeListener(final IPropertyChangeListener listener) {
        this.listeners.remove(listener);
    }

    protected void firePropertyChangeChange(final String property, final Object oldValue, final Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, property, oldValue, newValue);
        for (Iterator<IPropertyChangeListener> iter = this.listeners.iterator(); iter.hasNext(); ) {
            IPropertyChangeListener listener = iter.next();
            listener.propertyChange(event);
        }
    }
}
