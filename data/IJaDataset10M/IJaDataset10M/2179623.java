package com.mu.jacob.core.ant;

import com.google.inject.AbstractModule;
import com.mu.jacob.core.builder.AbstractBuilder;
import com.mu.jacob.core.builder.ElementBuilder;
import com.mu.jacob.core.builder.FileBuilder;
import com.mu.jacob.core.builder.IBuilder;
import com.mu.jacob.core.generator.Config;
import com.mu.jacob.core.generator.GeneratorException;
import com.mu.jacob.core.renderer.FreeMarkerRenderer;
import com.mu.jacob.core.renderer.IRenderer;
import com.mu.jacob.core.renderer.StringTemplateRenderer;
import com.mu.jacob.core.renderer.VelocityRenderer;

/**
 * Config module contains factories for builder and renderer initialization.
 * The factories are instantiated dynamically using Guice framework.
 * 
 * @author Adam Smyczek
 */
public class ConfigModule extends AbstractModule {

    /**
	 * Module configure method binds factory interfaces to concrete implementations
	 */
    public void configure() {
        bind(BuilderFctory.class).toInstance(new BuilderFctory() {

            public IBuilder createBuilder(final BuilderTask task, final Config config) {
                IRenderer renderer = config.getInstance(RendererFactory.class).createRenderer(task, config);
                IBuilder builder = null;
                switch(task.getType()) {
                    case FILE:
                        builder = config.getInstance(FileBuilder.class);
                        break;
                    case ELEMENT:
                        builder = config.getInstance(ElementBuilder.class);
                        break;
                    case CUSTOM:
                        builder = (IBuilder) config.getInstance(classForName(((CustomBuilderTask) task).getClassName()));
                        break;
                    default:
                        throw new GeneratorException("Not supported builder type " + task.getType());
                }
                if (builder instanceof AbstractBuilder) {
                    AbstractBuilder ab = (AbstractBuilder) builder;
                    ab.setRenderer(renderer);
                    ab.setProperties(task.getProperties());
                    for (ContextTask contextTask : task.getContextTasks()) {
                        ab.addContext(contextTask.getAlias(), config.getInstance(classForName(contextTask.getClassName())));
                    }
                }
                return builder;
            }
        });
        bind(RendererFactory.class).toInstance(new RendererFactory() {

            public IRenderer createRenderer(final BuilderTask task, final Config config) {
                switch(task.getRendererEngine()) {
                    case VELOCITY:
                        return config.getInstance(VelocityRenderer.class);
                    case FREEMARKER:
                        return config.getInstance(FreeMarkerRenderer.class);
                    case STRINGTEMPLATES:
                        return config.getInstance(StringTemplateRenderer.class);
                    case CUSTOM:
                        return (IRenderer) config.getInstance(classForName(task.getCustomRendererClassName()));
                }
                throw new GeneratorException("Not supported rendering engine " + task.getRendererEngine());
            }
        });
    }

    /**
	 * Builder factory interface
	 */
    public interface BuilderFctory {

        public IBuilder createBuilder(final BuilderTask task, final Config config);
    }

    /**
	 * Renderer factory interface
	 */
    public interface RendererFactory {

        public IRenderer createRenderer(final BuilderTask task, final Config config);
    }

    /**
     * Returns class for absolute class name
     * @param <T> the class type
     * @param name class name
     * @return class for class name in current class loader context
     * @throws GeneratorException if class for name does not exist
     */
    @SuppressWarnings("unchecked")
    protected <T> Class<T> classForName(final String name) throws GeneratorException {
        try {
            return (Class<T>) Class.forName(name, false, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new GeneratorException("Cannot find class " + name, e);
        }
    }
}
