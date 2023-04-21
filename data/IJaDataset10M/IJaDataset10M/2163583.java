package net.sf.devtool.casaapp;

import net.sf.devtool.casaapp.GuiDSLRuntimeModule;

/**
 * Manual modifications go to {net.sf.devtool.casaapp.GuiDSLUiModule}
 */
public abstract class AbstractGuiDSLUiModule extends GuiDSLRuntimeModule {

    public Class<? extends org.eclipse.xtext.ui.core.ILocationInFileProvider> bindILocationInFileProvider() {
        return org.eclipse.xtext.ui.core.DefaultLocationInFileProvider.class;
    }

    public Class<? extends org.eclipse.jface.text.hyperlink.IHyperlinkDetector> bindIHyperlinkDetector() {
        return org.eclipse.xtext.ui.common.editor.hyperlinking.DefaultHyperlinkDetector.class;
    }

    public Class<? extends org.eclipse.jface.text.reconciler.IReconciler> bindIReconciler() {
        return org.eclipse.xtext.ui.core.editor.reconciler.XtextReconciler.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.toggleComments.ISingleLineCommentHelper> bindISingleLineCommentHelper() {
        return org.eclipse.xtext.ui.common.editor.toggleComments.DefaultSingleLineCommentHelper.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.outline.ITreeProvider> bindITreeProvider() {
        return org.eclipse.xtext.ui.common.editor.outline.transformer.TransformingTreeProvider.class;
    }

    public Class<? extends org.eclipse.ui.views.contentoutline.IContentOutlinePage> bindIContentOutlinePage() {
        return org.eclipse.xtext.ui.common.editor.outline.XtextContentOutlinePage.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.outline.actions.IActionBarContributor> bindIActionBarContributor() {
        return org.eclipse.xtext.ui.common.editor.outline.actions.IActionBarContributor.DefaultActionBarContributor.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.syntaxcoloring.IHighlightingHelper> bindIHighlightingHelper() {
        return org.eclipse.xtext.ui.common.editor.syntaxcoloring.HighlightingHelper.class;
    }

    public Class<? extends org.eclipse.emf.common.notify.AdapterFactory> bindAdapterFactory() {
        return org.eclipse.xtext.ui.core.InjectableAdapterFactory.class;
    }

    public Class<? extends org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider> bindAdapterFactoryLabelProvider() {
        return org.eclipse.xtext.ui.core.InjectableAdapterFactoryLabelProvider.class;
    }

    public org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry bindComposedAdapterFactory$Descriptor$RegistryToInstance() {
        return org.eclipse.emf.edit.provider.ComposedAdapterFactory.Descriptor.Registry.INSTANCE;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.IContentAssistantFactory> bindIContentAssistantFactory() {
        return org.eclipse.xtext.ui.common.editor.contentassist.DefaultContentAssistantFactory.class;
    }

    public Class<? extends org.eclipse.jface.text.contentassist.IContentAssistProcessor> bindIContentAssistProcessor() {
        return org.eclipse.xtext.ui.core.editor.contentassist.XtextContentAssistProcessor.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.ICompletionProposalPostProcessor> bindICompletionProposalPostProcessor() {
        return org.eclipse.xtext.ui.common.editor.contentassist.DefaultCompletionProposalPostProcessor.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.IFollowElementCalculator> bindIFollowElementCalculator() {
        return org.eclipse.xtext.ui.common.editor.contentassist.DefaultFollowElementCalculator.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.ITemplateProposalProvider> bindITemplateProposalProvider() {
        return org.eclipse.xtext.ui.common.editor.templates.DefaultTemplateProposalProvider.class;
    }

    public Class<? extends org.eclipse.jface.text.templates.persistence.TemplateStore> bindTemplateStore() {
        return org.eclipse.xtext.ui.common.editor.templates.XtextTemplateStore.class;
    }

    public Class<? extends org.eclipse.jface.text.templates.ContextTypeRegistry> bindContextTypeRegistry() {
        return org.eclipse.xtext.ui.common.editor.templates.XtextTemplateContextTypeRegistry.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.formatting.IContentFormatterFactory> bindIContentFormatterFactory() {
        return org.eclipse.xtext.ui.core.editor.formatting.ContentFormatterFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.utils.ValidationJob.Factory> bindValidationJob$Factory() {
        return org.eclipse.xtext.ui.core.editor.utils.DefaultValidationJobFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.IXtextResourceChecker> bindIXtextResourceChecker() {
        return org.eclipse.xtext.ui.core.editor.DefaultXtextResourceChecker.class;
    }

    public Class<? extends org.eclipse.jface.text.rules.ITokenScanner> bindITokenScanner() {
        return org.eclipse.xtext.ui.common.editor.syntaxcoloring.antlr.AntlrTokenScanner.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.contentassist.IProposalConflictHelper> bindIProposalConflictHelper() {
        return org.eclipse.xtext.ui.common.editor.contentassist.antlr.AntlrProposalConflictHelper.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.IDamagerRepairer> bindIDamagerRepairer() {
        return org.eclipse.xtext.ui.core.editor.XtextDamagerRepairer.class;
    }

    public Class<? extends org.eclipse.jface.viewers.ILabelProvider> bindILabelProvider() {
        return net.sf.devtool.casaapp.labeling.GuiDSLLabelProvider.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.outline.transformer.ISemanticModelTransformer> bindISemanticModelTransformer() {
        return net.sf.devtool.casaapp.outline.GuiDSLTransformer.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.outline.actions.IContentOutlineNodeAdapterFactory> bindIContentOutlineNodeAdapterFactory() {
        return net.sf.devtool.casaapp.outline.GuiDSLOutlineNodeAdapterFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.IContentProposalProvider> bindIContentProposalProvider() {
        return net.sf.devtool.casaapp.contentassist.GuiDSLProposalProvider.class;
    }

    public Class<? extends org.eclipse.xtext.ui.core.editor.contentassist.ContentAssistContext.Factory> bindContentAssistContext$Factory() {
        return org.eclipse.xtext.ui.common.editor.contentassist.antlr.ParserBasedContentAssistContextFactory.class;
    }

    public Class<? extends org.eclipse.xtext.ui.common.editor.contentassist.antlr.IContentAssistParser> bindIContentAssistParser() {
        return net.sf.devtool.casaapp.contentassist.antlr.GuiDSLParser.class;
    }
}
