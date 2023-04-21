package org.coode.lint.protege.loader.extensions;

import org.coode.lint.protege.LintProtegePluginInstance;
import org.coode.lint.protege.loader.AbstractLintPluginLoader;
import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.ProtegePlugin;
import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;

/**
 * @author Luigi Iannone
 * 
 */
public final class LoaderFactoryProtegePluginInstanceAdapter<O extends ProtegePlugin<E>, E extends LintProtegePluginInstance<?>> implements ProtegePluginInstance, LoaderFactory<O, E> {

    private final LoaderFactory<O, E> delegate;

    /**
	 * @param extension
	 * @param delegate
	 */
    public LoaderFactoryProtegePluginInstanceAdapter(IExtension extension, LoaderFactory<O, E> delegate) {
        if (delegate == null) {
            throw new NullPointerException("The loader factory cannot be null");
        }
        if (extension == null) {
            throw new NullPointerException("The extension cannot be null");
        }
        this.extension = extension;
        this.delegate = delegate;
    }

    private final IExtension extension;

    public void dispose() throws Exception {
    }

    public void initialise() throws Exception {
    }

    /**
	 * @return the extension
	 */
    public IExtension getExtension() {
        return this.extension;
    }

    /**
	 * @param owlEditorKit
	 * @return
	 * @see org.coode.lint.protege.loader.extensions.LoaderFactory#createLintPluginLoader(org.protege.editor.owl.OWLEditorKit)
	 */
    public AbstractLintPluginLoader<O, E> createLintPluginLoader(OWLEditorKit owlEditorKit) {
        return this.delegate.createLintPluginLoader(owlEditorKit);
    }

    public static <P extends ProtegePlugin<Q>, Q extends LintProtegePluginInstance<?>> LoaderFactoryProtegePluginInstanceAdapter<P, Q> buildLoaderFactoryProtegePluginInstanceAdapter(LoaderFactory<P, Q> delegate, IExtension extension) {
        return new LoaderFactoryProtegePluginInstanceAdapter<P, Q>(extension, delegate);
    }
}
