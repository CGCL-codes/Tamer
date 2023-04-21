package org.isandlatech.plugins.rest.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TabsToSpacesConverter;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.ui.texteditor.spelling.ISpellingEngine;
import org.eclipse.ui.texteditor.spelling.SpellingEngineDescriptor;
import org.eclipse.ui.texteditor.spelling.SpellingService;
import org.isandlatech.plugins.rest.RestPlugin;
import org.isandlatech.plugins.rest.editor.formatters.DefaultTextFormattingStrategy;
import org.isandlatech.plugins.rest.editor.formatters.GridTableFormattingStrategy;
import org.isandlatech.plugins.rest.editor.formatters.SectionFormattingStrategy;
import org.isandlatech.plugins.rest.editor.linewrap.HardLineWrapAutoEdit;
import org.isandlatech.plugins.rest.editor.linewrap.LineWrapUtil;
import org.isandlatech.plugins.rest.editor.linewrap.LineWrapUtil.LineWrapMode;
import org.isandlatech.plugins.rest.editor.outline.OutlineUtil;
import org.isandlatech.plugins.rest.editor.outline.RestContentOutlinePage;
import org.isandlatech.plugins.rest.editor.providers.RuleProvider;
import org.isandlatech.plugins.rest.editor.providers.TokenProvider;
import org.isandlatech.plugins.rest.editor.scanners.RestLiteralBlockScanner;
import org.isandlatech.plugins.rest.editor.scanners.RestPartitionScanner;
import org.isandlatech.plugins.rest.editor.scanners.RestScanner;
import org.isandlatech.plugins.rest.editor.scanners.RestSectionBlockScanner;
import org.isandlatech.plugins.rest.editor.scanners.RestSourceBlockScanner;
import org.isandlatech.plugins.rest.editor.scanners.RestTableBlockScanner;
import org.isandlatech.plugins.rest.editor.ui.tooltip.RestInformationPresenter;
import org.isandlatech.plugins.rest.editor.userassist.BasicInternalLinkHandler;
import org.isandlatech.plugins.rest.editor.userassist.contentassist.DeclarativeProposalProcessor;
import org.isandlatech.plugins.rest.editor.userassist.contentassist.ProposalDispatcher;
import org.isandlatech.plugins.rest.editor.userassist.hover.RestTextHover;
import org.isandlatech.plugins.rest.prefs.IEditorPreferenceConstants;

/**
 * RestEditor configuration handler
 * 
 * @author Thomas Calmant
 */
public class RestViewerConfiguration extends TextSourceViewerConfiguration {

    /** Content pAssistant */
    private ContentAssistant pAssistant = null;

    /** Auto indent strategy */
    private DefaultIndentLineAutoEditStrategy pAutoEditIndent;

    /** Auto line wrapping strategy */
    private HardLineWrapAutoEdit pAutoEditLineWrap;

    /** Auto conversion from tabs to spaces */
    private TabsToSpacesConverter pAutoEditTabsToSpace;

    /** Document formatter */
    private ContentFormatter pDocFormatter = null;

    /** ReST document token scanner */
    private RestScanner pDocScanner = null;

    /** Parent editor */
    private final RestEditor pEditor;

    /** Preference store */
    private IPreferenceStore pPreferenceStore = null;

    /** Scanner rule provider */
    private RuleProvider pRuleProvider = null;

    /** Spell check text hover */
    private RestTextHover pSpellCheckHover = null;

    /** Token provider */
    private TokenProvider pTokenProvider = null;

    /**
	 * Prepares the configuration. Get a preference store reference.
	 * 
	 * @param Parent
	 *            ReST Editor instance
	 */
    public RestViewerConfiguration(final RestEditor aParentEditor) {
        super();
        pEditor = aParentEditor;
        pPreferenceStore = RestPlugin.getDefault().getPreferenceStore();
    }

    @Override
    public IAutoEditStrategy[] getAutoEditStrategies(final ISourceViewer aSourceViewer, final String aContentType) {
        if (pAutoEditIndent == null) {
            pAutoEditIndent = new DefaultIndentLineAutoEditStrategy();
        }
        if (pAutoEditLineWrap == null) {
            int maxLineLength = LineWrapUtil.get().getMaxLineLength();
            pAutoEditLineWrap = new HardLineWrapAutoEdit(RestPartitionScanner.PARTITIONING, maxLineLength);
        }
        List<IAutoEditStrategy> strategies = new ArrayList<IAutoEditStrategy>();
        strategies.add(pAutoEditIndent);
        if (LineWrapUtil.get().isWrappingEnabled() && IDocument.DEFAULT_CONTENT_TYPE.equals(aContentType)) {
            strategies.add(pAutoEditLineWrap);
        }
        if (pPreferenceStore.getBoolean(IEditorPreferenceConstants.EDITOR_TABS_TO_SPACES)) {
            if (pAutoEditTabsToSpace == null) {
                pAutoEditTabsToSpace = new TabsToSpacesConverter();
                pAutoEditTabsToSpace.setNumberOfSpacesPerTab(getTabWidth(aSourceViewer));
                pAutoEditTabsToSpace.setLineTracker(new DefaultLineTracker());
            }
            strategies.add(pAutoEditTabsToSpace);
        }
        return strategies.toArray(new IAutoEditStrategy[0]);
    }

    @Override
    public String[] getConfiguredContentTypes(final ISourceViewer sourceViewer) {
        return RestPartitionScanner.PARTITION_TYPES;
    }

    @Override
    public String getConfiguredDocumentPartitioning(final ISourceViewer sourceViewer) {
        return RestPartitionScanner.PARTITIONING;
    }

    @Override
    public IContentAssistant getContentAssistant(final ISourceViewer aSourceViewer) {
        if (pAssistant == null) {
            pAssistant = new ContentAssistant();
            pAssistant.setInformationControlCreator(RestInformationPresenter.getCreator(new BasicInternalLinkHandler()));
            pAssistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(aSourceViewer));
            ProposalDispatcher proposalDispatcher = new ProposalDispatcher();
            IContentAssistProcessor literalBlocksProcessor = new DeclarativeProposalProcessor();
            proposalDispatcher.set(RestPartitionScanner.LITERAL_BLOCK, literalBlocksProcessor);
            pAssistant.setContentAssistProcessor(literalBlocksProcessor, RestPartitionScanner.LITERAL_BLOCK);
            pAssistant.setContentAssistProcessor(proposalDispatcher, IDocument.DEFAULT_CONTENT_TYPE);
        }
        return pAssistant;
    }

    @Override
    public IContentFormatter getContentFormatter(final ISourceViewer aSourceViewer) {
        if (pDocFormatter == null) {
            pDocFormatter = new ContentFormatter();
            pDocFormatter.enablePartitionAwareFormatting(true);
            pDocFormatter.setDocumentPartitioning(RestPartitionScanner.PARTITIONING);
            pDocFormatter.setFormattingStrategy(new SectionFormattingStrategy(), RestPartitionScanner.SECTION_BLOCK);
            pDocFormatter.setFormattingStrategy(new GridTableFormattingStrategy(), RestPartitionScanner.GRID_TABLE_BLOCK);
        }
        if (pPreferenceStore.getBoolean(IEditorPreferenceConstants.EDITOR_SAVE_TRIM)) {
            pDocFormatter.setFormattingStrategy(new DefaultTextFormattingStrategy(), IDocument.DEFAULT_CONTENT_TYPE);
        } else {
            pDocFormatter.setFormattingStrategy(null, IDocument.DEFAULT_CONTENT_TYPE);
        }
        return pDocFormatter;
    }

    @Override
    public String[] getDefaultPrefixes(final ISourceViewer aSourceViewer, final String aContentType) {
        return getIndentPrefixes(aSourceViewer, aContentType);
    }

    /**
	 * Retrieves the instance unique token pDocScanner
	 * 
	 * @return The instance token pDocScanner
	 */
    protected ITokenScanner getDocumentScanner() {
        if (pDocScanner == null) {
            initProviders();
            pDocScanner = new RestScanner(pRuleProvider);
        }
        return pDocScanner;
    }

    @Override
    public String[] getIndentPrefixes(final ISourceViewer aSourceViewer, final String aContentType) {
        final int tabWidth = getTabWidth(aSourceViewer);
        final List<String> resultList = new ArrayList<String>();
        char[] array;
        for (int i = tabWidth; i > 0; i--) {
            array = new char[i];
            Arrays.fill(array, ' ');
            resultList.add(new String(array));
        }
        resultList.add("\t");
        resultList.add("");
        return resultList.toArray(new String[resultList.size()]);
    }

    @Override
    protected String[] getIndentPrefixesForTab(final int aTabWidth) {
        boolean useSpaces = pPreferenceStore.getBoolean(IEditorPreferenceConstants.EDITOR_TABS_TO_SPACES);
        if (useSpaces) {
            char[] array = new char[aTabWidth];
            Arrays.fill(array, ' ');
            return new String[] { new String(array) };
        }
        return new String[] { "\t" };
    }

    @Override
    public IPresentationReconciler getPresentationReconciler(final ISourceViewer aSourceViewer) {
        initProviders();
        RestLiteralBlockScanner literalBlockScanner = new RestLiteralBlockScanner(pRuleProvider);
        RestSectionBlockScanner sectionBlockScanner = new RestSectionBlockScanner(pRuleProvider);
        RestSourceBlockScanner sourceBlockScanner = new RestSourceBlockScanner(pTokenProvider);
        RestTableBlockScanner tableBlockScanner = new RestTableBlockScanner(pRuleProvider);
        PresentationReconciler reconciler = new PresentationReconciler();
        reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(aSourceViewer));
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getDocumentScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        dr = new DefaultDamagerRepairer(literalBlockScanner);
        reconciler.setDamager(dr, RestPartitionScanner.LITERAL_BLOCK);
        reconciler.setRepairer(dr, RestPartitionScanner.LITERAL_BLOCK);
        dr = new DefaultDamagerRepairer(sectionBlockScanner);
        reconciler.setDamager(dr, RestPartitionScanner.SECTION_BLOCK);
        reconciler.setRepairer(dr, RestPartitionScanner.SECTION_BLOCK);
        dr = new DefaultDamagerRepairer(sourceBlockScanner);
        reconciler.setDamager(dr, RestPartitionScanner.SOURCE_BLOCK);
        reconciler.setRepairer(dr, RestPartitionScanner.SOURCE_BLOCK);
        dr = new DefaultDamagerRepairer(tableBlockScanner);
        reconciler.setDamager(dr, RestPartitionScanner.GRID_TABLE_BLOCK);
        reconciler.setRepairer(dr, RestPartitionScanner.GRID_TABLE_BLOCK);
        return reconciler;
    }

    @Override
    public IReconciler getReconciler(final ISourceViewer aSourceViewer) {
        SpellingService selectedService = new SpellingService(pPreferenceStore);
        IReconcilingStrategy strategy = new RestSpellingReconcileStrategy(aSourceViewer, selectedService);
        return new MonoReconciler(strategy, false);
    }

    @Override
    public int getTabWidth(final ISourceViewer aSourceViewer) {
        return pPreferenceStore.getInt(IEditorPreferenceConstants.EDITOR_TABS_LENGTH);
    }

    @Override
    public ITextHover getTextHover(final ISourceViewer aSourceViewer, final String aContentType) {
        boolean engineEnabled = pPreferenceStore.getBoolean(SpellingService.PREFERENCE_SPELLING_ENABLED);
        if (pSpellCheckHover == null && engineEnabled) {
            SpellingService selectedService = new SpellingService(pPreferenceStore);
            try {
                SpellingEngineDescriptor engineDescriptor = selectedService.getActiveSpellingEngineDescriptor(pPreferenceStore);
                if (engineDescriptor != null) {
                    ISpellingEngine engine = engineDescriptor.createEngine();
                    if (engine != null) {
                        pSpellCheckHover = new RestTextHover(engine);
                    }
                } else {
                    RestPlugin.logWarning("No spell engin found");
                }
            } catch (CoreException e) {
                DebugPlugin.logMessage("Error preparing the spell engine", e);
            }
        }
        if (pSpellCheckHover != null) {
            pSpellCheckHover.enableSpellChecking(engineEnabled);
        }
        return pSpellCheckHover;
    }

    /**
	 * Initializes the token and rule providers if necessary
	 */
    private void initProviders() {
        if (pTokenProvider == null) {
            pTokenProvider = new TokenProvider();
        }
        if (pRuleProvider == null || pRuleProvider.getTokenProvider() != pTokenProvider) {
            pRuleProvider = new RuleProvider(pTokenProvider);
        }
    }

    /**
	 * On-save operations :
	 * 
	 * * Un-wrapping if needed
	 * 
	 * * Auto section markers normalization
	 * 
	 * * Auto formating when the editor saves the file
	 * 
	 * @param aSourceViewer
	 *            The editor source viewer
	 */
    public void onEditorPerformSave(final ISourceViewer aSourceViewer) {
        final IDocument document = aSourceViewer.getDocument();
        if (pPreferenceStore.getBoolean(IEditorPreferenceConstants.EDITOR_SAVE_RESET_MARKERS)) {
            RestContentOutlinePage outlinePage = null;
            if (pEditor != null) {
                outlinePage = pEditor.getOutlinePage();
            }
            if (outlinePage != null) {
                outlinePage.update();
                OutlineUtil.normalizeSectionsMarker(pEditor.getOutlinePage().getContentProvider().getRoot());
            }
        }
        if (pPreferenceStore.getBoolean(IEditorPreferenceConstants.EDITOR_SAVE_FORMAT)) {
            IRegion docRegion = new Region(0, document.getLength());
            Point currentLocation = aSourceViewer.getSelectedRange();
            pDocFormatter.format(document, docRegion);
            aSourceViewer.setSelectedRange(currentLocation.x, currentLocation.y);
        }
        if (LineWrapUtil.get().isActiveMode(LineWrapMode.SOFT)) {
            pAutoEditLineWrap.unregisterListener();
            pAutoEditLineWrap.removeWrapping();
        }
    }

    /**
	 * Post-save operations : re-wrapping if needed
	 * 
	 * @param aSourceViewer
	 *            The editor source viewer
	 */
    public void postEditorPerformSave(final ISourceViewer aSourceViewer) {
        final IDocument document = aSourceViewer.getDocument();
        if (LineWrapUtil.get().isActiveMode(LineWrapMode.SOFT)) {
            pAutoEditLineWrap.registerListener(document);
            pAutoEditLineWrap.wrapWholeDocument();
        }
    }

    /**
	 * Sets the document associated to the viewer configuration.
	 * 
	 * Updates the line wrapper, if needed.
	 * 
	 * @param aDocument
	 *            Document associated to the source viewer
	 */
    public void setDocument(final IDocument aDocument) {
        if (pAutoEditLineWrap != null && LineWrapUtil.get().isWrappingEnabled()) {
            pAutoEditLineWrap.registerListener(aDocument);
            pAutoEditLineWrap.wrapWholeDocument();
        }
    }
}
