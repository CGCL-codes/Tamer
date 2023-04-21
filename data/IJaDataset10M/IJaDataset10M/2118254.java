package com.jcompressor.faces.annotations;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import com.jcompressor.faces.config.Configurator;

/**
 * @author Scott Carnett
 */
public class JcompressorResourceScanner {

    private final List<UIComponent> components;

    private final Configurator configurator;

    private final List<JcompressorResource> resources;

    /**
	 * The default constructor
	 * @param components the components
	 * @param configurator the configurator
	 */
    @SuppressWarnings("unchecked")
    public JcompressorResourceScanner(final List<UIComponent> components, final Configurator configurator) {
        this.components = components;
        this.configurator = configurator;
        this.resources = new ArrayList<JcompressorResource>();
        this.scan(JcompressorResources.class, JcompressorResource.class);
    }

    /**
     * Scans the components for some specified annotations
     * @param annotations the jcompressor annotations
     */
    private void scan(final Class<? extends Annotation>... annotations) {
        for (final UIComponent component : this.components) {
            final Class<?> clazz = component.getClass();
            for (final Annotation annotation : clazz.getAnnotations()) {
                if (JcompressorResource.class.isAssignableFrom(annotation.annotationType())) {
                    final JcompressorResource resource = (JcompressorResource) annotation;
                    if (!this.resources.contains(resource)) {
                        this.resources.add(resource);
                    }
                } else if (JcompressorResources.class.isAssignableFrom(annotation.annotationType())) {
                    for (final JcompressorResource resource : ((JcompressorResources) annotation).value()) {
                        if (!this.resources.contains(resource)) {
                            this.resources.add(resource);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Gets the configurator
	 * @return {@link com.jcompressor.config.Configurator} with the configurator
	 */
    public Configurator getConfigurator() {
        return this.configurator;
    }

    /**
	 * Gets the resources
	 * @return List<{@link com.jcompressor.faces.annotations.JcompressorResource}> with the resources
	 */
    public List<JcompressorResource> getResources() {
        return this.resources;
    }
}
