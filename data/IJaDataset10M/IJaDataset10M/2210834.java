package org.jadapter.guice;

import org.jadapter.Adapter;
import org.jadapter.JAdapter;
import org.jadapter.registry.TransformerRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

/**
 * A Guice module that simplifies setting up adapters.
 * The basic pattern is:
 * 
 * <pre>
 * public class MyAdapters extends AbstractAdaptersModule {
 * 
 *     protected void configureAdapters() {
 *         bindAdapter(MyAdapter.class);
 *         bindAdapter(MyOtherAdapter.class);
 *         
 *         bindTransformerRegistry();
 *     }
 * }
 * </pre>
 * 
 * The bindAdapter() helper registers an adapter of a given class or type.
 * One version accepts an adapter class that implements the adapted interface
 * and has a one-argument constructor that accepts the adapted type
 * (@see {@link org.jadapter.JAdapter}). The other version accepts an Adapter
 * instance, such as a pre-instantiated JAdapter.
 * 
 * The adapter factory class or Adapter instance may use <code>@Inject</code>
 * annotations to participate in Guice injection. They cannot use constructor
 * Injection, however, since we are in effect providing an adapter
 * <em>instance</em> in the case of <code>bindAdapter(Adapter)</code> and we rely on
 * a <code>org.jadapter.JAdapter</code> instance to do the construction in
 * the case of <code>bindAdapter(Class<?> factory)</code>.
 * 
 * The bindTransformerRegistry() helper simply binds TransformerRegistry.class
 * to a GuiceTransformerRegistry(), which will be injected with the set of
 * adapters as appropriate. You should obviously only use this once, and only
 * in the last module.
 *
 * Under the hood, AbstractAdaptersModule uses a Multibinder, so you must have
 * the Guice Multibinder extension on the classpath. Since multibinders support
 * merging of different sets across modules, you can safely create multiple
 * modules that register adapters, although you probably only want one of
 * them (in fact, the last of them) to use bindTransformerRegistry().
 * 
 */
public abstract class AbstractAdaptersModule extends AbstractModule {

    Multibinder<Adapter> adapterBinder;

    protected final void configure() {
        adapterBinder = Multibinder.newSetBinder(binder(), Adapter.class);
        configureAdapters();
    }

    /**
	 * Add a binding for an adapter created from a factory class
	 * @param factory The factory class. Should implement (only) the
	 *  interface provided by the adapter, and have a one-argument
	 *  constructor that takes as an argument the adapted type.
	 */
    protected void bindAdapter(Class<?> factory) {
        adapterBinder.addBinding().toInstance(new JAdapter(factory));
    }

    /**
	 * Add a binding for an adapter that implements the
	 * Adapter interface.
	 * @param adapter The adapter
	 */
    protected void bindAdapter(Adapter<?, ?> adapter) {
        adapterBinder.addBinding().toInstance(adapter);
    }

    /**
	 * Convenience method to bind TransformerRegistry.class to
	 * GuiceTransformerRegistry.class in the Singleton scope.
	 */
    protected void bindTransformerRegistry() {
        bind(TransformerRegistry.class).to(GuiceTransformerRegistry.class).in(Singleton.class);
    }

    /**
	 * Template method that must be implemented to
	 * configure adapters (or anything else in the module)
	 */
    protected abstract void configureAdapters();
}
