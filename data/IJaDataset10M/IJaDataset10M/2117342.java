package org.peaseplate.templateengine.locator;

import java.util.Locale;
import org.peaseplate.templateengine.Template;
import org.peaseplate.templateengine.TemplateEngine;
import org.peaseplate.templateengine.TemplateException;
import org.peaseplate.utils.resolver.Locator;

/**
 * A template locator stores the resource of the template and is capable of loading it.
 */
public interface TemplateLocator extends Locator {

    /**
	 * Returns a char array with the raw template data Don't forget to update the timestamp and the rawSize properties
	 * when loading the data.
	 * 
	 * @return a char array with the raw template data
	 * @throws TemplateException on occasion
	 */
    public char[] load() throws TemplateException;

    /**
	 * Resolves the template with the specified name relative to this template. If it can't find the template it tries
	 * to resolve the template with the absolute name, usually by asking the engine.
	 * 
	 * @param engine the engine
	 * @param name the name, relative or absolute
	 * @param locale the locale
	 * @param encoding the locale for loading
	 * @return the template or null if not found
	 * @throws TemplateException on occasion
	 */
    public Template resolve(TemplateEngine engine, String name, Locale locale, String encoding) throws TemplateException;

    /**
	 * Create a highlight from the resource with the position at line and column highlighted :) May return null.
	 * 
	 * @param message TODO
	 * @param line the line
	 * @param column the column
	 * @return the highlight or null
	 */
    public String highlight(String message, int line, int column);
}
