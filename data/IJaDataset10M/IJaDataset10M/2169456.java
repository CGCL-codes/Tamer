package hudson.zipscript.parser.context;

import hudson.zipscript.ResourceContainer;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.resource.macrolib.MacroProvider;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * The context interface used for all variables to retrieve supplied business
 * data
 * 
 * @author Joe Hudson
 */
public interface ExtendedContext extends Context, MacroProvider {

    /**
	 * Return true if the context has ever been initialized
	 */
    public boolean isInitialized();

    /**
	 * Return true if the context has been initialized and false if not
	 */
    public boolean isInitialized(Element topLevelElement);

    /**
	 * Set the initialized status
	 * 
	 * @param val
	 *            the initialized status
	 */
    public void markInitialized(Element topLevelElement);

    /**
	 * Put a value in the context
	 * 
	 * @param key
	 *            the key name
	 * @param value
	 *            the value
	 * @param if
	 *            we can travel up, should we?
	 */
    public void put(Object key, Object value, boolean travelUp);

    /**
	 * Return the parsing session which is used by evaluators
	 */
    public ParsingSession getParsingSession();

    /**
	 * Set the parsing session which is used by evaluators
	 * 
	 * @param session
	 */
    public void setParsingSession(ParsingSession session);

    /**
	 * Return the locale
	 */
    public Locale getLocale();

    /**
	 * Set the locale
	 * 
	 * @param locale
	 */
    public void setLocale(Locale locale);

    /**
	 * Return the resource container
	 */
    public ResourceContainer getResourceContainer();

    /**
	 * Set the resource container
	 */
    public void setResourceContainer(ResourceContainer resourceContainer);

    /**
	 * Return the root context (AKA the global context)
	 */
    public ExtendedContext getRootContext();

    /**
	 * Append all nested macro attributes within this context
	 * 
	 * @param m
	 *            the map to add the elements to using the macro name as the map
	 *            key
	 */
    public void appendMacroNestedAttributes(Map m);

    /**
	 * Return all elements in the current execution scope
	 * 
	 * @return
	 */
    public void addToElementScope(List nestingStack);

    /**
	 * Add a macro import for the context
	 * 
	 * @param namespace
	 *            the namespace
	 * @param macroPath
	 *            the path for the macro
	 */
    public void addMacroImport(String namespace, String macroPath);

    /**
	 * Return true if templates should be modification checked
	 * 
	 * @return
	 */
    public boolean doRefreshTemplates();

    /**
	 * Return the context that is the most closest in scope but is not nested inside a macro directive
	 */
    public ExtendedContext getTemplateContext();
}
