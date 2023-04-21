package net.danielkvasnicka.flower.core.api;

import java.util.Map;
import net.danielkvasnicka.flower.core.WebAccessibleBeanMetadata;

/**
 * A bean resolver that decides which bean should be loaded,
 * based on the current URL and all available URL mappings.
 * 
 * @author Daniel Kvasnicka jr.
 */
public interface WebAccessibleBeanResolver {

    /**
	 * Resolves the bean metadata.
	 * 
	 * @param urlMapping	URL mappings that are loaded from application context when Spring is being initialized
	 * @param url			the current URL
	 * @return bean metadata ({@link WebAccessibleBeanMetadata})
	 */
    WebAccessibleBeanMetadata resolve(Map<String, String> urlMapping, String url);
}
