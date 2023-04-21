package org.makumba.devel.eclipse.mdd.ui;

import org.eclipse.xtext.ui.DefaultUiModule;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Manual modifications go to {org.makumba.devel.eclipse.mdd.ui.MDDUiModule}
 */
@SuppressWarnings("all")
public abstract class AbstractMDDUiModule extends DefaultUiModule {

    public AbstractMDDUiModule(AbstractUIPlugin plugin) {
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
        return org.eclipse.xtext.ui.editor.FastDamagerRepairer.class;
    }

    public void configureHighlightingLexer(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.parser.antlr.Lexer.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.HIGHLIGHTING)).to(org.makumba.devel.eclipse.mdd.parser.antlr.internal.InternalMDDLexer.class);
    }

    public void configureHighlightingTokenDefProvider(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.parser.antlr.ITokenDefProvider.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.HIGHLIGHTING)).to(org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext.Factory> bindContentAssistContext$Factory() {
        return org.eclipse.xtext.ui.editor.contentassist.antlr.ParserBasedContentAssistContextFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.antlr.IContentAssistParser> bindIContentAssistParser() {
        return org.makumba.devel.eclipse.mdd.ui.contentassist.antlr.MDDParser.class;
    }

    public void configureContentAssistLexerProvider(com.google.inject.Binder binder) {
        binder.bind(org.makumba.devel.eclipse.mdd.ui.contentassist.antlr.internal.InternalMDDLexer.class).toProvider(org.eclipse.xtext.parser.antlr.LexerProvider.create(org.makumba.devel.eclipse.mdd.ui.contentassist.antlr.internal.InternalMDDLexer.class));
    }

    public void configureContentAssistLexer(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.ui.editor.contentassist.antlr.internal.Lexer.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.ui.LexerUIBindings.CONTENT_ASSIST)).to(org.makumba.devel.eclipse.mdd.ui.contentassist.antlr.internal.InternalMDDLexer.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.PrefixMatcher> bindPrefixMatcher() {
        return org.eclipse.xtext.ui.editor.contentassist.FQNPrefixMatcher.class;
    }

    public Class<? extends org.eclipse.jface.viewers.ILabelProvider> bindILabelProvider() {
        return org.makumba.devel.eclipse.mdd.ui.labeling.MDDLabelProvider.class;
    }

    public void configureResourceUIServiceLabelProvider(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.jface.viewers.ILabelProvider.class).annotatedWith(org.eclipse.xtext.ui.resource.ResourceServiceDescriptionLabelProvider.class).to(org.makumba.devel.eclipse.mdd.ui.labeling.MDDDescriptionLabelProvider.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.outline.transformer.ISemanticModelTransformer> bindISemanticModelTransformer() {
        return org.makumba.devel.eclipse.mdd.ui.outline.MDDTransformer.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.outline.actions.IContentOutlineNodeAdapterFactory> bindIContentOutlineNodeAdapterFactory() {
        return org.makumba.devel.eclipse.mdd.ui.outline.MDDOutlineNodeAdapterFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
        return org.makumba.devel.eclipse.mdd.ui.contentassist.MDDProposalProvider.class;
    }

    public void configureIResourceDescriptionsBuilderScope(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.resource.IResourceDescriptions.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.scoping.impl.AbstractGlobalScopeProvider.NAMED_BUILDER_SCOPE)).to(org.eclipse.xtext.builder.builderState.ShadowingResourceDescriptions.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.IXtextEditorCallback> bindIXtextEditorCallback() {
        return org.eclipse.xtext.builder.nature.NatureAddingEditorCallback.class;
    }

    public void configureIResourceDescriptionsPersisted(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.resource.IResourceDescriptions.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.builder.impl.PersistentDataAwareDirtyResource.PERSISTED_DESCRIPTIONS)).to(org.eclipse.xtext.builder.builderState.IBuilderState.class);
    }

    public Class<? extends org.eclipse.xtext.ui.editor.DocumentBasedDirtyResource> bindDocumentBasedDirtyResource() {
        return org.eclipse.xtext.builder.impl.PersistentDataAwareDirtyResource.class;
    }

    public Class<? extends org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider> bindIssueResolutionProvider() {
        return org.makumba.devel.eclipse.mdd.ui.quickfix.MDDQuickfixProvider.class;
    }
}
