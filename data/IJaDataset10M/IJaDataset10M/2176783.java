package com.aptana.ide.editors.unified;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITextViewerExtension2;
import org.eclipse.jface.text.ITextViewerExtension5;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.editors.text.NonExistingFileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.core.ui.CoreUIUtils;
import com.aptana.ide.core.ui.editors.ISaveAsEvent;
import com.aptana.ide.core.ui.editors.ISaveEvent;
import com.aptana.ide.core.ui.views.IRefreshableView;
import com.aptana.ide.editors.UnifiedEditorsPlugin;
import com.aptana.ide.editors.actions.ShowWhitespace;
import com.aptana.ide.editors.managers.FileContextManager;
import com.aptana.ide.editors.preferences.IPreferenceConstants;
import com.aptana.ide.editors.unified.actions.FoldingActionGroup;
import com.aptana.ide.editors.unified.context.ContextItem;
import com.aptana.ide.editors.unified.context.IContextAwareness;
import com.aptana.ide.editors.unified.errors.ExternalFileErrorListener;
import com.aptana.ide.editors.unified.errors.FileErrorListener;
import com.aptana.ide.editors.unified.errors.ProjectFileErrorListener;
import com.aptana.ide.editors.unified.help.LexemeUIHelp;
import com.aptana.ide.editors.unified.messaging.UnifiedMessages;
import com.aptana.ide.editors.unified.painting.WhitespacePainter;
import com.aptana.ide.editors.untitled.BaseTextEditor;
import com.aptana.ide.editors.views.outline.UnifiedOutlinePage;
import com.aptana.ide.lexer.Lexeme;
import com.aptana.ide.lexer.LexemeList;
import com.aptana.ide.lexer.TokenCategories;
import com.aptana.ide.parsing.IParseState;

/**
 * UnifiedEditor
 */
public abstract class UnifiedEditor extends BaseTextEditor implements IUnifiedEditor, IPropertyChangeListener, ISelectionChangedListener {

    /**
	 * ctrlDown
	 */
    public static boolean ctrlDown;

    /**
	 * outlinePage
	 */
    protected UnifiedOutlinePage outlinePage;

    private UnifiedColorizer _colorizer;

    private IUnifiedEditorContributor baseContributor;

    private FileErrorListener errorListener;

    private WhitespacePainter fWhitespacePainter;

    private EditorFileContext fileContextWrapper;

    private ArrayList saveAsListeners;

    private ArrayList saveListeners;

    private IRefreshableView fileExplorerView;

    private IPreferenceStore prefStore;

    private IPartListener _partListener;

    private ProjectionSupport projectionSupport;

    private ProjectionAnnotationModel annotationModel;

    private Listener _keyUpListener;

    private VerifyKeyListener _verifyKeyListener;

    private UnifiedViewer viewer;

    private IContextAwareness _contextAwareness;

    private boolean isDisposing = false;

    private boolean hasKeyBeenPressed = false;

    private LineStyleListener _lineStyleListener;

    private TextChangeListener _textChangeListener;

    private PairMatcher _pairMatcher;

    private int _maxColorizingColumns;

    private boolean _extendedStylesEnabled;

    private boolean _autoActivateCodeAssist = true;

    /**
	 * UnifiedEditor
	 */
    public UnifiedEditor() {
        super();
        addPluginToPreferenceStore(UnifiedEditorsPlugin.getDefault());
        saveListeners = new ArrayList();
        saveAsListeners = new ArrayList();
        baseContributor = createLocalContributor();
        _colorizer = UnifiedColorizer.getInstance();
        setSourceViewerConfiguration(new UnifiedConfiguration(this, getPreferenceStore()));
        getFileServiceFactory();
        prefStore = UnifiedEditorsPlugin.getDefault().getPreferenceStore();
        prefStore = (prefStore == null) ? null : prefStore;
        setDocumentProvider(createDocumentProvider());
        fileContextWrapper = new EditorFileContext();
        baseContributor.setFileContext(fileContextWrapper);
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        setEditorContextMenuId(getSite().getId() + "#UnifiedEditorContext");
        setRulerContextMenuId(getSite().getId() + "#UnifiedRulerContext");
        IPreferenceStore localPreferenceStore = this.getPreferenceStore();
        if (localPreferenceStore != null) {
            _autoActivateCodeAssist = localPreferenceStore.getBoolean(IPreferenceConstants.CODE_ASSIST_AUTO_ACTIVATION);
        }
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#editorContextMenuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
    protected void editorContextMenuAboutToShow(IMenuManager menu) {
        menu.add(new Separator("debug"));
        super.editorContextMenuAboutToShow(menu);
    }

    /**
	 * Adds the specified plugin to the list of plugin stores to check when searching for preference
	 * 
	 * @param plugin
	 *            The plugin to add
	 */
    protected void addPluginToPreferenceStore(AbstractUIPlugin plugin) {
        setPreferenceStore(new ChainedPreferenceStore(new IPreferenceStore[] { getPreferenceStore(), plugin.getPreferenceStore() }));
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        DropTarget target = new DropTarget(parent, DND.DROP_TARGET_MOVE | DND.DROP_MOVE | DND.DROP_DEFAULT | DND.DROP_LINK | DND.DROP_COPY);
        Transfer[] transfers = new Transfer[] { TextTransfer.getInstance(), FileTransfer.getInstance() };
        target.setTransfer(transfers);
        target.addDropListener(new UnifiedDropTargetListener(this));
        boolean wordWrap = getPreferenceStore().getBoolean(IPreferenceConstants.ENABLE_WORD_WRAP);
        this.getViewer().getTextWidget().setWordWrap(wordWrap);
        ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();
        projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
        projectionSupport.install();
        if (viewer.canDoOperation(ProjectionViewer.TOGGLE)) {
            viewer.doOperation(ProjectionViewer.TOGGLE);
        }
        annotationModel = viewer.getProjectionAnnotationModel();
        showWhitespace(ShowWhitespace.isInstanceChecked());
        boolean colorizeFlag = getPreferenceStore().getBoolean(IPreferenceConstants.COLORIZE_EDITOR);
        if (colorizeFlag) {
            _maxColorizingColumns = getPreferenceStore().getInt(IPreferenceConstants.COLORIZER_MAXCOLUMNS);
            linkColorer();
        }
        boolean linkPairFlag = getPreferenceStore().getBoolean(IPreferenceConstants.PAIRMATCH_EDITOR);
        if (linkPairFlag) {
            linkPairMatcher();
        }
        _extendedStylesEnabled = getPreferenceStore().getBoolean(IPreferenceConstants.COLORIZER_TEXT_HIGHLIGHT_ENABLED);
        if (_extendedStylesEnabled) {
            installTextOccurrenceHighlightSupport();
        }
        LexemeUIHelp.setHelp(this, this.getViewer().getTextWidget(), getFileContext());
    }

    private void installTextOccurrenceHighlightSupport() {
        ISelectionProvider selectionProvider = getSelectionProvider();
        if (selectionProvider instanceof IPostSelectionProvider) {
            IPostSelectionProvider provider = (IPostSelectionProvider) selectionProvider;
            provider.addPostSelectionChangedListener(this);
        } else {
            selectionProvider.addSelectionChangedListener(this);
        }
        _extendedStylesEnabled = true;
    }

    private void uninstallTextOccurrenceHighlightSupport() {
        ISelectionProvider selectionProvider = getSelectionProvider();
        if (selectionProvider != null) {
            if (selectionProvider instanceof IPostSelectionProvider) {
                IPostSelectionProvider provider = (IPostSelectionProvider) selectionProvider;
                provider.removePostSelectionChangedListener(this);
            } else {
                selectionProvider.removeSelectionChangedListener(this);
            }
        }
        removeMarkedOccurrences();
        _extendedStylesEnabled = false;
    }

    private void linkColorer() {
        if (_lineStyleListener == null) {
            _lineStyleListener = new LineStyleListener() {

                /**
				 * @see org.eclipse.swt.custom.LineStyleListener#lineGetStyle(org.eclipse.swt.custom.LineStyleEvent)
				 */
                public void lineGetStyle(LineStyleEvent e) {
                    EditorFileContext fileContext = getFileContext();
                    if (fileContext == null || fileContext.getFileContext() == null) {
                        return;
                    }
                    IParseState parseState = fileContext.getParseState();
                    if (parseState == null) {
                        return;
                    }
                    LexemeList lexemeList = parseState.getLexemeList();
                    if (lexemeList == null) {
                        IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedEditor_LexemeListIsNull);
                        return;
                    }
                    int orgOffset = e.lineOffset;
                    int offset = orgOffset;
                    int extra = 0;
                    int lineLength = e.lineText.length();
                    if (viewer instanceof ITextViewerExtension5) {
                        ITextViewerExtension5 v5 = (ITextViewerExtension5) viewer;
                        offset = v5.widgetOffset2ModelOffset(e.lineOffset);
                        extra = offset - e.lineOffset;
                    }
                    int maxLineLength = lineLength > _maxColorizingColumns ? _maxColorizingColumns : lineLength;
                    Lexeme[] lexemes = null;
                    synchronized (lexemeList) {
                        int startingIndex = lexemeList.getLexemeCeilingIndex(offset);
                        int endingIndex = lexemeList.getLexemeFloorIndex(offset + maxLineLength);
                        if (startingIndex == -1 && endingIndex != -1) {
                            startingIndex = endingIndex;
                        }
                        if (endingIndex == -1 && startingIndex != -1) {
                            endingIndex = startingIndex;
                        }
                        if (startingIndex != -1 && endingIndex != -1) {
                            lexemes = lexemeList.copyRange(startingIndex, endingIndex);
                        }
                    }
                    if (lexemes != null) {
                        Vector styles = new Vector();
                        _colorizer.createStyles(styles, lexemes, _extendedStylesEnabled);
                        StyleRange[] styleResults = (StyleRange[]) styles.toArray(new StyleRange[] {});
                        if (extra > 0) {
                            for (int i = 0; i < styleResults.length; i++) {
                                StyleRange range = styleResults[i];
                                range.start -= extra;
                            }
                        }
                        e.styles = styleResults;
                    }
                }
            };
        }
        if (_textChangeListener == null) {
            _textChangeListener = new TextChangeListener() {

                public void textChanging(TextChangingEvent event) {
                }

                public void textChanged(TextChangedEvent event) {
                    StyledText text = getViewer().getTextWidget();
                    redrawFrom(text, text.getLineAtOffset(text.getCaretOffset()));
                }

                public void textSet(TextChangedEvent event) {
                    StyledText text = getViewer().getTextWidget();
                    redrawFrom(text, 0);
                }

                private void redrawFrom(StyledText text, int lno) {
                    if (lno < 0 || lno >= text.getLineCount()) {
                        return;
                    }
                    int y = text.getLocationAtOffset(text.getOffsetAtLine(lno)).y;
                    int height = text.getClientArea().height - y;
                    int width = text.getClientArea().width + text.getHorizontalPixel();
                    text.redraw(0, y, width, height, false);
                }
            };
        }
        getViewer().getTextWidget().addLineStyleListener(_lineStyleListener);
        getViewer().getTextWidget().getContent().addTextChangeListener(_textChangeListener);
    }

    private void linkPairMatcher() {
        if (_pairMatcher == null) {
            _pairMatcher = new PairMatcher();
            StyledText text = getViewer().getTextWidget();
            text.addPaintListener(_pairMatcher);
            text.addKeyListener(_pairMatcher);
            text.addTraverseListener(_pairMatcher);
            text.addMouseListener(_pairMatcher);
            text.addSelectionListener(_pairMatcher);
            ScrollBar sb = text.getVerticalBar();
            if (sb != null) {
                sb.addSelectionListener(_pairMatcher);
            }
        }
    }

    private void unlinkPairMatcher() {
        if (_pairMatcher != null) {
            if (getViewer() == null) {
                return;
            }
            StyledText text = getViewer().getTextWidget();
            if (text == null || text.isDisposed() == true) {
                return;
            }
            text.removePaintListener(_pairMatcher);
            text.removeKeyListener(_pairMatcher);
            text.removeTraverseListener(_pairMatcher);
            text.removeMouseListener(_pairMatcher);
            text.removeSelectionListener(_pairMatcher);
            ScrollBar sb = text.getVerticalBar();
            if (sb != null) {
                sb.removeSelectionListener(_pairMatcher);
            }
        }
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#getPairMatch(int)
	 */
    public PairMatch getPairMatch(int offset) {
        return null;
    }

    /**
	 * findBalancingLexeme
	 * 
	 * @param startIndex
	 * @param language
	 * @param startType
	 * @param endType
	 * @param direction
	 * @return Lexeme
	 */
    protected Lexeme findBalancingLexeme(int startIndex, String language, int startType, int endType, int direction) {
        LexemeList lexemeList = this.getFileContext().getParseState().getLexemeList();
        Lexeme result = null;
        int count = 0;
        while (0 <= startIndex && startIndex < lexemeList.size()) {
            result = lexemeList.get(startIndex);
            if (result.getLanguage().equals(language)) {
                if (result.typeIndex == endType) {
                    count--;
                    if (count == 0) {
                        break;
                    }
                } else if (result.typeIndex == startType) {
                    count++;
                }
            }
            startIndex += direction;
        }
        if (count != 0) {
            result = null;
        }
        return result;
    }

    /**
	 * @author Paul Colton
	 */
    class PairMatcher implements SelectionListener, MouseListener, KeyListener, PaintListener, TraverseListener {

        private Color _color;

        private PairMatch _currentPair;

        /**
		 * PairMatcher
		 */
        public PairMatcher() {
            _color = new Color(getSite().getShell().getDisplay(), 150, 150, 150);
        }

        /**
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
		 */
        public void mouseDoubleClick(MouseEvent e) {
        }

        /**
		 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
        public void mouseDown(MouseEvent e) {
            stateChanged();
        }

        /**
		 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
		 */
        public void mouseUp(MouseEvent e) {
        }

        /**
		 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
		 */
        public void keyPressed(KeyEvent e) {
            stateChanged();
        }

        /**
		 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
		 */
        public void keyReleased(KeyEvent e) {
        }

        /**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
        public void paintControl(PaintEvent e) {
            stateChanged();
            pairsDraw(e.gc, _currentPair);
        }

        /**
		 * @see org.eclipse.swt.events.TraverseListener#keyTraversed(org.eclipse.swt.events.TraverseEvent)
		 */
        public void keyTraversed(TraverseEvent e) {
            stateChanged();
        }

        /**
		 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
        public void widgetSelected(SelectionEvent e) {
            stateChanged();
        }

        /**
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
        public void widgetDefaultSelected(SelectionEvent e) {
        }

        void stateChanged() {
            StyledText text = getViewer().getTextWidget();
            if (text != null) {
                PairMatch newmatch = getPairMatch(text.getCaretOffset());
                if ((newmatch == null && _currentPair != null) || (newmatch != null && !newmatch.equals(_currentPair))) {
                    pairsDraw(null, _currentPair);
                    pairsDraw(null, newmatch);
                }
                _currentPair = newmatch;
            }
        }

        void pairsDraw(GC gc, PairMatch pm) {
            if (pm == null) {
                return;
            }
            pairDraw(gc, pm.beginStart, pm.beginEnd);
            pairDraw(gc, pm.endStart, pm.endEnd);
        }

        void pairDraw(GC gc, int start, int end) {
            StyledText text = getViewer().getTextWidget();
            if (start > text.getCharCount() || end > text.getCharCount()) {
                return;
            }
            if (gc != null) {
                try {
                    Point left = text.getLocationAtOffset(start);
                    Point right = text.getLocationAtOffset(end);
                    gc.setForeground(_color);
                    gc.setLineWidth(1);
                    gc.drawRectangle(left.x + 1, left.y + 1, right.x - left.x - 2, gc.getFontMetrics().getHeight() - 2);
                } catch (Exception e) {
                    IdeLog.logError(UnifiedEditorsPlugin.getDefault(), StringUtils.format(Messages.UnifiedEditor_PairDraw, e.getMessage()));
                }
            } else {
                text.redrawRange(start, end - start, true);
            }
        }
    }

    /**
	 * getDefaultFileExtension
	 * 
	 * @return String
	 */
    public abstract String getDefaultFileExtension();

    /**
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
    public void propertyChange(PropertyChangeEvent e) {
    }

    /**
	 * Are we currently the active editor?
	 * 
	 * @return boolean
	 */
    public boolean isActiveEditor() {
        IEditorPart part = getEditorSite().getPage().getActiveEditor();
        if (part == null) {
            return false;
        } else if (part instanceof IUnifiedEditor) {
            IUnifiedEditor editor = (IUnifiedEditor) part;
            return editor.getEditor() == this;
        } else {
            return part == this;
        }
    }

    /**
	 * onKeyPressed
	 * 
	 * @param event
	 */
    private void onKeyPressed(VerifyEvent event) {
        char c = event.character;
        StyledText styledText = (StyledText) event.widget;
        int keyCode = event.keyCode;
        ITypedRegion reg = fileContextWrapper.getPartitionAtOffset(styledText.getCaretOffset());
        if (reg == null) {
            return;
        }
        final String contentType = reg.getType();
        IUnifiedEditorContributor contributor = baseContributor.findChildContributor(contentType);
        if (autoActivateCodeAssist() && contributor != null && contributor.isValidIdentifier(c, keyCode) && isLeftCharacterWhitespace(contributor, styledText, c, keyCode)) {
            showContentAssist();
        }
    }

    /**
	 * isLeftCharacterWhitespace
	 * 
	 * @param styledText
	 * @param c
	 * @param keyCode
	 * @return boolean
	 */
    private boolean isLeftCharacterWhitespace(IUnifiedEditorContributor contributor, StyledText styledText, char c, int keyCode) {
        int offset = styledText.getCaretOffset();
        if (offset == 0) {
            return true;
        }
        String line = styledText.getText(offset - 1, offset - 1);
        if (line.length() > 0) {
            return contributor.isValidActivationCharacter(line.charAt(0), keyCode);
        } else {
            return false;
        }
    }

    /**
	 * showContentAssist
	 */
    private void showContentAssist() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final Display display = workbench.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                try {
                    final SourceViewer sv = (SourceViewer) getSourceViewer();
                    if (sv == null) {
                        return;
                    }
                    Control c = display.getFocusControl();
                    if (c == null || c != sv.getTextWidget()) {
                        return;
                    }
                    if (sv.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS)) {
                        sv.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
                    }
                } catch (Exception e) {
                    IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedEditor_ErrorContentProposals, e);
                }
            }
        });
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#showWhitespace(boolean)
	 */
    public void showWhitespace(boolean state) {
        ISourceViewer fSourceViewer = this.getSourceViewer();
        if (state) {
            if (fWhitespacePainter == null) {
                if (fSourceViewer instanceof ITextViewerExtension2) {
                    fWhitespacePainter = new WhitespacePainter(fSourceViewer);
                    fWhitespacePainter.setColor(new Color(this.getSite().getShell().getDisplay(), 200, 200, 200));
                    ITextViewerExtension2 extension = (ITextViewerExtension2) fSourceViewer;
                    fWhitespacePainter.handleDrawRequest(null);
                    extension.addPainter(fWhitespacePainter);
                }
            }
        } else {
            if (fWhitespacePainter != null) {
                if (fSourceViewer instanceof ITextViewerExtension2) {
                    ITextViewerExtension2 extension = (ITextViewerExtension2) fSourceViewer;
                    extension.removePainter(fWhitespacePainter);
                    fWhitespacePainter.deactivate(true);
                    fWhitespacePainter.dispose();
                    fWhitespacePainter = null;
                }
            }
        }
    }

    private Annotation[] computeDifferences(ProjectionAnnotationModel model, Set additions) {
        List deletions = new ArrayList();
        for (Iterator iter = model.getAnnotationIterator(); iter.hasNext(); ) {
            Object annotation = iter.next();
            if (annotation instanceof ProjectionAnnotation) {
                Position position = model.getPosition((Annotation) annotation);
                if (additions.contains(position)) {
                    additions.remove(position);
                } else {
                    deletions.add(annotation);
                }
            }
        }
        return (Annotation[]) deletions.toArray(new Annotation[deletions.size()]);
    }

    /**
	 * updateFoldingStructure
	 * 
	 * @param positions
	 */
    public void updateFoldingStructure(Set positions) {
        HashMap additions = new HashMap();
        Annotation[] deletions = computeDifferences(annotationModel, positions);
        for (Iterator iter = positions.iterator(); iter.hasNext(); ) {
            Object position = iter.next();
            additions.put(new ProjectionAnnotation(), position);
        }
        if ((deletions.length != 0 || additions.size() != 0)) {
            annotationModel.modifyAnnotations(deletions, additions, new Annotation[0]);
        }
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#createSourceViewer(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.jface.text.source.IVerticalRuler, int)
	 */
    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
        viewer = new UnifiedViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
        getSourceViewerDecorationSupport(viewer);
        SourceViewer sv = viewer;
        Display d = this.getSite().getShell().getDisplay();
        _keyUpListener = new Listener() {

            public void handleEvent(Event e) {
                UnifiedEditor.ctrlDown = false;
            }
        };
        d.addFilter(SWT.KeyUp, _keyUpListener);
        _verifyKeyListener = new VerifyKeyListener() {

            public void verifyKey(VerifyEvent event) {
                hasKeyBeenPressed = true;
                onKeyPressed(event);
                if (event.keyCode == SWT.CTRL) {
                    UnifiedEditor.ctrlDown = true;
                } else {
                    UnifiedEditor.ctrlDown = false;
                }
            }
        };
        sv.prependVerifyKeyListener(_verifyKeyListener);
        return viewer;
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#doSetInput(org.eclipse.ui.IEditorInput)
	 */
    protected void doSetInput(IEditorInput input) throws CoreException {
        super.doSetInput(input);
        try {
            IDocumentProvider dp = (IDocumentProvider) getDocumentProvider();
            if (dp == null) {
                throw new Exception(Messages.UnifiedEditor_DocumentProviderNull);
            }
            IDocument document = dp.getDocument(input);
            DocumentSourceProvider provider = new DocumentSourceProvider(document, input);
            if (provider == null) {
                throw new Exception(Messages.UnifiedEditor_ProviderIsNull);
            }
            boolean isNewInput = isNewInput(input);
            updateFileInfo(input, provider, document);
            updatePartitioner(provider, document, isNewInput);
            fileContextWrapper.setFileContext(FileContextManager.get(provider.getSourceURI()));
            setErrorListeners(input);
        } catch (Exception e) {
            IdeLog.logError(UnifiedEditorsPlugin.getDefault(), e.getMessage());
        }
    }

    private void setErrorListeners(IEditorInput input) {
        if (input instanceof IFileEditorInput) {
            IFileEditorInput fileInput = (IFileEditorInput) input;
            IFile file = fileInput.getFile();
            errorListener = new ProjectFileErrorListener(file);
        } else if (input instanceof IPathEditorInput || input instanceof NonExistingFileEditorInput) {
            IDocument doc = this.getDocumentProvider().getDocument(input);
            IAnnotationModel ann = this.getDocumentProvider().getAnnotationModel(input);
            errorListener = new ExternalFileErrorListener(ann, doc);
        }
        if (errorListener != null) {
            getFileContext().addErrorListener(errorListener);
        }
    }

    /**
	 * updateFileInfo
	 *
	 * @param input
	 * @param provider
	 * @param document
	 */
    protected void updateFileInfo(IEditorInput input, DocumentSourceProvider provider, IDocument document) {
        if (isNewInput(input)) {
            IFileServiceFactory fileServiceFactory = this.getFileServiceFactory();
            if (fileServiceFactory != null) {
                FileService context = fileServiceFactory.createFileService(provider);
                FileContextManager.add(provider.getSourceURI(), context);
            }
            FileContextManager.connectSourceProvider(provider.getSourceURI(), provider);
        }
    }

    private void updatePartitioner(DocumentSourceProvider provider, IDocument document, boolean isNewInput) {
        if (isNewInput) {
            UnifiedDocumentPartitioner partitioner = new UnifiedDocumentPartitioner(provider.getSourceURI());
            partitioner.setLegalContentTypes(baseContributor.getContentTypes());
            partitioner.setPartitions();
            if (document instanceof IDocumentExtension3) {
                ((IDocumentExtension3) document).setDocumentPartitioner(UnifiedConfiguration.UNIFIED_PARTITIONING, partitioner);
            } else {
                throw new IllegalStateException(Messages.UnifiedEditor_DocumentMustBe);
            }
        }
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#getFileContext()
	 */
    public EditorFileContext getFileContext() {
        return fileContextWrapper;
    }

    /**
	 * createLocalContributor
	 * 
	 * @return IUnifiedEditorContributor
	 */
    protected abstract IUnifiedEditorContributor createLocalContributor();

    /**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
    public void dispose() {
        if (isDisposing) {
            return;
        }
        isDisposing = true;
        if (_lineStyleListener != null) {
            if (getViewer().getTextWidget() != null && getViewer().getTextWidget().isDisposed() == false) {
                getViewer().getTextWidget().removeLineStyleListener(_lineStyleListener);
            }
        }
        if (_textChangeListener != null) {
            if (getViewer().getTextWidget() != null && getViewer().getTextWidget().isDisposed() == false) {
                getViewer().getTextWidget().getContent().removeTextChangeListener(_textChangeListener);
            }
        }
        unlinkPairMatcher();
        if (errorListener != null) {
            getFileContext().removeErrorListener(errorListener);
        }
        if (_partListener != null) {
            this.getEditorSite().getPage().removePartListener(_partListener);
            _partListener = null;
        }
        if (_keyUpListener != null) {
            Display d = this.getSite().getShell().getDisplay();
            d.removeFilter(SWT.KeyUp, _keyUpListener);
            this._keyUpListener = null;
        }
        if (_verifyKeyListener != null) {
            ((SourceViewer) viewer).removeVerifyKeyListener(_verifyKeyListener);
            this._verifyKeyListener = null;
        }
        SourceViewerConfiguration svc = this.getConfiguration();
        if (svc instanceof UnifiedConfiguration) {
            ((UnifiedConfiguration) svc).dispose();
        }
        if (outlinePage != null) {
            outlinePage.dispose();
            outlinePage = null;
        }
        showWhitespace(false);
        if (fWhitespacePainter != null) {
            fWhitespacePainter.dispose();
        }
        saveListeners.clear();
        saveAsListeners.clear();
        _lineStyleListener = null;
        outlinePage = null;
        if (baseContributor != null) {
            baseContributor.setParentConfiguration(null);
            baseContributor = null;
        }
        errorListener = null;
        fWhitespacePainter = null;
        fileExplorerView = null;
        prefStore = null;
        this._contextAwareness = null;
        this.disposeDocumentProvider();
        if (fileContextWrapper != null) {
            fileContextWrapper.deactivateForEditing();
            fileContextWrapper.disconnectSourceProvider(null);
        }
        if (_extendedStylesEnabled) {
            uninstallTextOccurrenceHighlightSupport();
        }
        super.dispose();
    }

    /**
	 * getFileServiceFactory
	 * 
	 * @return IFileServiceFactory
	 */
    public abstract IFileServiceFactory getFileServiceFactory();

    /**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
    public Object getAdapter(Class adapter) {
        if (adapter == IContextProvider.class) {
            return LexemeUIHelp.getHelpContextProvider(this, getFileContext());
        }
        if (IContentOutlinePage.class.equals(adapter)) {
            return getOutlinePage();
        }
        return super.getAdapter(adapter);
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#getViewer()
	 */
    public ISourceViewer getViewer() {
        return this.getSourceViewer();
    }

    /**
	 * // exposing this as public for scripting access
	 * 
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#getConfiguration()
	 */
    public SourceViewerConfiguration getConfiguration() {
        return this.getSourceViewerConfiguration();
    }

    /**
	 * @see com.aptana.ide.editors.unified.IUnifiedEditor#getContextAwareness()
	 */
    public IContextAwareness getContextAwareness() {
        if (_contextAwareness == null) {
            _contextAwareness = new IContextAwareness() {

                public void update(IFileService fileService) {
                }

                public ContextItem getFileContext() {
                    return new ContextItem();
                }
            };
        }
        return _contextAwareness;
    }

    /**
	 * Returns the content outline page adapter
	 * 
	 * @return Returns the content outline page adapter.
	 */
    public UnifiedOutlinePage getOutlinePage() {
        if (this.outlinePage == null || (this.outlinePage.getControl() != null && this.outlinePage.getControl().isDisposed())) {
            this.outlinePage = new UnifiedOutlinePage(this);
        }
        return outlinePage;
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#handlePreferenceStoreChanged(org.eclipse.jface.util.PropertyChangeEvent)
	 */
    protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
        try {
            ISourceViewer sourceViewer = getSourceViewer();
            if (sourceViewer == null) {
                return;
            }
            String property = event.getProperty();
            if (IPreferenceConstants.COLORIZE_EDITOR.equals(property)) {
                boolean colorize = getPreferenceStore().getBoolean(IPreferenceConstants.COLORIZE_EDITOR);
                if (colorize) {
                    if (_lineStyleListener == null) {
                        linkColorer();
                    }
                } else if (_lineStyleListener != null) {
                    getViewer().getTextWidget().removeLineStyleListener(_lineStyleListener);
                    _lineStyleListener = null;
                }
            } else if (IPreferenceConstants.PAIRMATCH_EDITOR.equals(property)) {
                boolean pairMatch = getPreferenceStore().getBoolean(IPreferenceConstants.PAIRMATCH_EDITOR);
                if (pairMatch) {
                    if (_pairMatcher == null) {
                        linkPairMatcher();
                    }
                } else if (_pairMatcher != null) {
                    unlinkPairMatcher();
                    _pairMatcher = null;
                }
            } else if (IPreferenceConstants.COLORIZER_MAXCOLUMNS.equals(property)) {
                _maxColorizingColumns = getPreferenceStore().getInt(IPreferenceConstants.COLORIZER_MAXCOLUMNS);
            } else if (AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH.equals(property) || IPreferenceConstants.INSERT_SPACES_FOR_TABS.equals(property)) {
                IPreferenceStore store = getPreferenceStore();
                if (store == null) {
                    throw new Exception(Messages.UnifiedEditor_UnableToRetrievePreferenceStore);
                }
                int prefs = store.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH);
                SourceViewerConfiguration sv = getSourceViewerConfiguration();
                boolean tabsOrSpaces = store.getBoolean(IPreferenceConstants.INSERT_SPACES_FOR_TABS);
                if (sv != null && sv instanceof UnifiedConfiguration) {
                    UnifiedConfiguration uc = (UnifiedConfiguration) sv;
                    uc.setTabWidth(prefs, tabsOrSpaces, sourceViewer);
                } else {
                    IdeLog.logInfo(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedEditor_ErrorUpdateTabWidth);
                }
            } else if (IPreferenceConstants.COLORIZER_TEXT_HIGHLIGHT_ENABLED.equals(property)) {
                boolean textHighlightEnabled = getPreferenceStore().getBoolean(IPreferenceConstants.COLORIZER_TEXT_HIGHLIGHT_ENABLED);
                if (textHighlightEnabled) {
                    installTextOccurrenceHighlightSupport();
                } else {
                    uninstallTextOccurrenceHighlightSupport();
                }
            } else if (IPreferenceConstants.CODE_ASSIST_AUTO_ACTIVATION.equals(property)) {
                _autoActivateCodeAssist = getPreferenceStore().getBoolean(IPreferenceConstants.CODE_ASSIST_AUTO_ACTIVATION);
            }
            StyledText viewer = sourceViewer.getTextWidget();
            if (viewer != null) {
                sourceViewer.getTextWidget().redraw();
            }
        } catch (Exception ex) {
            IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedEditor_ErrorHandlingPreferenceChange, ex);
        } finally {
            super.handlePreferenceStoreChanged(event);
        }
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#handleCursorPositionChanged()
	 */
    protected void handleCursorPositionChanged() {
        StyledText styledText = getSourceViewer().getTextWidget();
        int offset = styledText.getCaretOffset();
        IFileService fs = this.getFileContext();
        if (fs != null) {
            LexemeList list = fs.getLexemeList();
            if (list == null) {
                return;
            }
            int index = list.getLexemeFloorIndex(offset);
            if (index == -1) {
                index = list.getLexemeCeilingIndex(offset);
                if (index == -1) {
                    return;
                }
            }
            Lexeme l = list.get(index);
            if (l != null) {
                IFileLanguageService ls = fs.getLanguageService(l.getLanguage());
                if (ls != null && ls.getOffsetMapper() != null) {
                    ls.getOffsetMapper().calculateCurrentLexeme(offset);
                }
            }
        }
        super.handleCursorPositionChanged();
    }

    /**
	 * Retrieves the current IEditorPart
	 * 
	 * @return The current EditorPart
	 */
    public IEditorPart getEditor() {
        return this;
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#initializeKeyBindingScopes()
	 */
    protected void initializeKeyBindingScopes() {
        setKeyBindingScopes(new String[] { "com.aptana.ide.editors.UnifiedEditorsScope" });
    }

    /**
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#configureSourceViewerDecorationSupport(org.eclipse.ui.texteditor.SourceViewerDecorationSupport)
	 */
    protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
        super.configureSourceViewerDecorationSupport(support);
    }

    /**
	 * 
	 */
    protected void onSaveComplete() {
        ISaveEvent[] listeners = null;
        synchronized (saveListeners) {
            listeners = (ISaveEvent[]) saveListeners.toArray(new ISaveEvent[0]);
        }
        for (int i = 0; i < listeners.length; i++) {
            ISaveEvent element = listeners[i];
            element.onSave(this.getEditor());
        }
    }

    /**
	 * @see com.aptana.ide.editors.untitled.BaseTextEditor#onSaveAsComplete(java.io.File,
	 *      java.io.File)
	 */
    protected void onSaveAsComplete(File oldFile, File newFile) {
        ISaveAsEvent[] listeners = null;
        synchronized (saveAsListeners) {
            listeners = (ISaveAsEvent[]) saveAsListeners.toArray(new ISaveAsEvent[0]);
        }
        for (int i = 0; i < listeners.length; i++) {
            ISaveAsEvent element = listeners[i];
            element.onSaveAs(this.getEditor(), oldFile, newFile);
        }
        updateFileExplorer();
    }

    private void updateFileExplorer() {
        if (this.fileExplorerView == null) {
            this.fileExplorerView = (IRefreshableView) CoreUIUtils.getViewInternal("com.aptana.ide.js.ui.views.FileExplorerView", null);
        }
        if (this.fileExplorerView == null) {
            return;
        }
        final IRefreshableView view = fileExplorerView;
        Display display = Display.getDefault();
        display.asyncExec(new Runnable() {

            public void run() {
                view.refresh();
            }
        });
    }

    /**
	 * @param listener
	 */
    public void addSaveListener(ISaveEvent listener) {
        this.saveListeners.add(listener);
    }

    /**
	 * @param listener
	 */
    public void removeSaveListener(ISaveEvent listener) {
        if (this.saveListeners.contains(listener)) {
            this.saveListeners.remove(listener);
        }
    }

    /**
	 * @param listener
	 */
    public void addSaveAsListener(ISaveAsEvent listener) {
        this.saveAsListeners.add(listener);
    }

    /**
	 * @param listener
	 */
    public void removeSaveAsListener(ISaveAsEvent listener) {
        if (this.saveAsListeners.contains(listener)) {
            this.saveAsListeners.remove(listener);
        }
    }

    void showPairError() {
        MessageDialog.openInformation(null, Messages.UnifiedEditor_MatchingPairError, Messages.UnifiedEditor_MatchingPairErrorMessage);
    }

    /**
	 * isHasKeyBeenPressed
	 * 
	 * @return boolean
	 */
    public boolean isHasKeyBeenPressed() {
        return hasKeyBeenPressed;
    }

    /**
	 * setHasKeyBeenPressed
	 * 
	 * @param hasKeyBeenPressed
	 */
    public void setHasKeyBeenPressed(boolean hasKeyBeenPressed) {
        this.hasKeyBeenPressed = hasKeyBeenPressed;
    }

    /**
	 * @see org.eclipse.ui.editors.text.TextEditor#createActions()
	 */
    protected void createActions() {
        super.createActions();
        new FoldingActionGroup(this, getViewer());
        Action action = new ContentAssistAction(UnifiedMessages.getResourceBundle(), "ContentAssistProposal.", this);
        String id = ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS;
        action.setActionDefinitionId(id);
        setAction("ContentAssistProposal", action);
        markAsStateDependentAction("ContentAssistProposal", true);
        Action actionContext = new TextOperationAction(UnifiedMessages.getResourceBundle(), "ContentAssistContextInformation.", this, ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION);
        actionContext.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_CONTEXT_INFORMATION);
        setAction("ContentAssistContextInformation", actionContext);
        markAsStateDependentAction("ContentAssistContextInformation", true);
    }

    /**
	 * @return Returns the baseContributor.
	 */
    public IUnifiedEditorContributor getBaseContributor() {
        return baseContributor;
    }

    /**
	 * isNewInput
	 * 
	 * @param input
	 * @return boolean
	 */
    private boolean isNewInput(IEditorInput input) {
        return true;
    }

    /**
	 * 
	 * @param event
	 */
    public void selectionChanged(SelectionChangedEvent event) {
        ISourceViewer sourceViewer = getSourceViewer();
        if (sourceViewer == null) {
            return;
        }
        StyledText styledText = sourceViewer.getTextWidget();
        if (styledText == null) {
            return;
        }
        int offset = -1;
        Point selectionRange = sourceViewer.getSelectedRange();
        if (selectionRange.x > -1 && selectionRange.y > 0) {
            offset = selectionRange.x;
        }
        if (offset < 0) {
            if (sourceViewer instanceof ITextViewerExtension5) {
                ITextViewerExtension5 extension = (ITextViewerExtension5) sourceViewer;
                offset = extension.widgetOffset2ModelOffset(styledText.getCaretOffset());
            } else {
                offset = sourceViewer.getVisibleRegion().getOffset();
                offset += styledText.getCaretOffset();
            }
        }
        if (offset < 0) {
            return;
        }
        LexemeList lexemeList = getLexemeList();
        if (lexemeList == null) {
            IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UnifiedEditor_LexemeListIsNull);
            return;
        }
        Lexeme selectedLexeme = lexemeList.getLexemeFromOffset(offset);
        if (selectedLexeme == null) {
            return;
        }
        if (!canMarkOccurrences(selectedLexeme)) {
            return;
        }
        String selectedText = selectedLexeme.getText();
        if (selectedText == null || selectedText.length() == 0) {
            return;
        }
        for (int i = 0; i < lexemeList.size(); i++) {
            Lexeme lexeme = lexemeList.get(i);
            if (lexeme != null) {
                if (lexeme.highlight) {
                    lexeme.highlight = false;
                }
                if (lexeme.length == selectedLexeme.length && selectedText.equals(lexeme.getText())) {
                    lexeme.highlight = true;
                }
            }
        }
        styledText.redraw();
    }

    /**
	 * 
	 * @return
	 */
    protected LexemeList getLexemeList() {
        EditorFileContext fileContext = getFileContext();
        if (fileContext == null || fileContext.getFileContext() == null) {
            return null;
        }
        IParseState parseState = fileContext.getParseState();
        if (parseState == null) {
            return null;
        }
        LexemeList lexemeList = parseState.getLexemeList();
        return lexemeList;
    }

    /**
	 * 
	 *
	 */
    public void removeMarkedOccurrences() {
        LexemeList lexemeList = getLexemeList();
        if (lexemeList == null) {
            return;
        }
        for (int i = 0; i < lexemeList.size(); i++) {
            Lexeme lexeme = lexemeList.get(i);
            if (lexeme != null && lexeme.highlight) {
                lexeme.highlight = false;
            }
        }
    }

    /**
	 * 
	 * @param lexeme
	 * @return
	 */
    public boolean canMarkOccurrences(Lexeme lexeme) {
        if (lexeme.getCategoryIndex() == TokenCategories.WHITESPACE) {
            return false;
        }
        if (lexeme.getCategoryIndex() == TokenCategories.PUNCTUATOR) {
            return false;
        }
        return true;
    }

    /**
	 * Creates the docment provider
	 * 
	 * @return IDocumentProvider
	 */
    public abstract IDocumentProvider createDocumentProvider();

    /**
	 * Is code assist available for auto-activation
	 * @return
	 */
    public boolean autoActivateCodeAssist() {
        return _autoActivateCodeAssist;
    }
}
