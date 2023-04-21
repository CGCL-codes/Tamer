package urban.ui;

import org.eclipse.xtext.ui.DefaultUiModule;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Manual modifications go to {urban.ui.KappaUiModule}
 */
@SuppressWarnings("all")
public abstract class AbstractKappaUiModule extends DefaultUiModule {

    public AbstractKappaUiModule(AbstractUIPlugin plugin) {
        super(plugin);
    }

    public com.google.inject.Provider<org.eclipse.xtext.resource.containers.IAllContainersState> provideIAllContainersState() {
        return org.eclipse.xtext.ui.shared.Access.getJavaProjectsState();
    }

    public Class<? extends org.eclipse.jface.text.rules.ITokenScanner> bindITokenScanner() {
        return org.eclipse.xtext.ui.editor.syntaxcoloring.antlr.AntlrTokenScanner.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IProposalConflictHelper> bindIProposalConflictHelper() {
        return org.eclipse.xtext.ui.editor.contentassist.antlr.AntlrProposalConflictHelper.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.IDamagerRepairer> bindIDamagerRepairer() {
        return org.eclipse.xtext.ui.editor.XtextDamagerRepairer.class;
    }

    public void configureHighlightingLexer(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.parser.antlr.Lexer.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.HIGHLIGHTING)).to(urban.parser.antlr.internal.InternalKappaLexer.class);
    }

    public void configureHighlightingTokenDefProvider(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.parser.antlr.ITokenDefProvider.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.HIGHLIGHTING)).to(org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher> bindPrefixMatcher() {
        return org.eclipse.xtext.ui.editor.contentassist.FQNPrefixMatcher.class;
    }

    public Class<? extends org.eclipse.jface.viewers.ILabelProvider> bindILabelProvider() {
        return urban.ui.labeling.KappaLabelProvider.class;
    }

    public void configureResourceUIServiceLabelProvider(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.jface.viewers.ILabelProvider.class).annotatedWith(org.eclipse.xtext.ui.resource.ResourceServiceDescriptionLabelProvider.class).to(urban.ui.labeling.KappaDescriptionLabelProvider.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.outline.transformer.ISemanticModelTransformer> bindISemanticModelTransformer() {
        return urban.ui.outline.KappaTransformer.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.outline.actions.IContentOutlineNodeAdapterFactory> bindIContentOutlineNodeAdapterFactory() {
        return urban.ui.outline.KappaOutlineNodeAdapterFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
        return urban.ui.contentassist.KappaProposalProvider.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext.Factory> bindContentAssistContext$Factory() {
        return org.eclipse.xtext.ui.editor.contentassist.antlr.ParserBasedContentAssistContextFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.antlr.IContentAssistParser> bindIContentAssistParser() {
        return urban.ui.contentassist.antlr.KappaParser.class;
    }

    public void configureContentAssistLexerProvider(com.google.inject.Binder binder) {
        binder.bind(urban.ui.contentassist.antlr.internal.InternalKappaLexer.class).toProvider(org.eclipse.xtext.parser.antlr.LexerProvider.create(urban.ui.contentassist.antlr.internal.InternalKappaLexer.class));
    }

    public void configureContentAssistLexer(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.CONTENT_ASSIST)).to(urban.ui.contentassist.antlr.internal.InternalKappaLexer.class);
    }

    public void configureIResourceDescriptionsBuilderScope(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.resource.IResourceDescriptions.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.scoping.impl.AbstractGlobalScopeProvider.NAMED_BUILDER_SCOPE)).to(org.eclipse.xtext.builder.builderState.ShadowingResourceDescriptions.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.IXtextEditorCallback> bindIXtextEditorCallback() {
        return org.eclipse.xtext.builder.nature.NatureAddingEditorCallback.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider> bindIssueResolutionProvider() {
        return urban.ui.quickfix.KappaQuickfixProvider.class;
    }
}
