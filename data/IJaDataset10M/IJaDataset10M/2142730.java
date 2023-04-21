package org.peaseplate;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Locale;
import org.peaseplate.service.CacheService;
import org.peaseplate.service.ConversionService;
import org.peaseplate.service.DesignatorService;
import org.peaseplate.service.MacroService;
import org.peaseplate.service.ResolverService;
import org.peaseplate.service.TransformerService;

/**
 * <p>
 * This class represents the central engine of the Pease Plate engine.
 * </p>
 * <p>
 * Usually just create exactly one instance of this class for all your
 * rendering duties. It stores all global and reusable settings for 
 * template rendering and is thread safe. It contains a cache that
 * stores compiled templates. 
 * </p>
 * 
 * @author Manfred HANTSCHEL
 */
public interface TemplateEngine {

    /**
	 * Adds the specified class loaders to the facotry. The class loader
	 * is used for multiple purposes, like initializing the services.
	 * The there is a new class loader added, the services will
	 * get initialized with it.
	 * 
	 * @param classLoaders the class loaders
	 */
    public void addClassLoader(ClassLoader... classLoaders);

    /**
	 * Returns all the class loaders in an unmodifiable collection.
	 * 
     * @return the classLoaders
     */
    public Collection<ClassLoader> getClassLoaders();

    /**
	 * Returns the cache service used to cache templates and messages
	 * @return the cache service
	 */
    public CacheService getCacheService();

    /**
	 * Sets the cache service used to cache templates and messages
	 * @param cacheService the cache service
	 */
    public void setCacheService(CacheService cacheService);

    /**
	 * Returns the resolver service used to resolve templates and messages
	 * @return the resolver service
	 */
    public ResolverService getResolverService();

    /**
	 * Sets the resolver service used to resolver templates and messages
	 * @param resolverService the resolver service
	 */
    public void setResolverService(ResolverService resolverService);

    /**
	 * Returns the designator service used to resolve and handle designators
	 * @return the designator service
	 */
    public DesignatorService getDesignatorService();

    /**
	 * Sets the designator service used to resolve and handle designators
	 * @param designatorService the designator service
	 */
    public void setDesignatorService(DesignatorService designatorService);

    /**
	 * Returns the transformer service used to resolve and handle transformers
	 * @return the transformer service
	 */
    public TransformerService getTransformerService();

    /**
	 * Sets the transformer service used to resolve and handle transformers
	 * @param transformerService the transformer service
	 */
    public void setTransformerService(TransformerService transformerService);

    /**
	 * Returns the macro service used to resolve and handle predefined macros
	 * @return the macro service
	 */
    public MacroService getMacroService();

    /**
	 * Sets the macro service used to resolve and handle predefined macros
	 * @param macroService the macro service
	 */
    public void setMacroService(MacroService macroService);

    /**
	 * Returns the conversion service used to convert objects from one type to another
	 * @return the conversion service
	 */
    public ConversionService getConversionService();

    /**
	 * Sets the conversion service used to convert objects from one type to another
	 * @param conversionService the conversion service
	 */
    public void setConversionService(ConversionService conversionService);

    /**
	 * Returns the default locale used when no locale is specified somewhere where needed
	 * @return teh default locale
	 */
    public Locale getDefaultLocale();

    /**
	 * Sets the default locale used when no locale is specified somewhere where needed
	 * @param defaultLocale the default locale
	 */
    public void setDefaultLocale(Locale defaultLocale);

    /**
	 * Returns the default encoding used when no encoding is specified somewhere where needed
	 * @return the default encoding
	 */
    public String getDefaultEncoding();

    /**
	 * Sets the default encoding used when no encoding is specified somewhere where needed
	 * @param defaultEncoding the default encoding
	 */
    public void setDefaultEncoding(String defaultEncoding);

    /**
	 * Returns the default line separator
	 * @return the default line separator
	 */
    public String getDefaultLineSeparator();

    /**
	 * Sets the default line separator
	 * @param defaultLineSeparator the default line separator
	 */
    public void setDefaultLineSeparator(String defaultLineSeparator);

    /**
	 * Returns the template with the specified name.
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}.
	 *  
	 * @param name the name of the template
	 * @return the template or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Template getTemplate(String name) throws TemplateException;

    /**
	 * Returns the template with the specified name.
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}.
	 *  
	 * @param name the name of the template
	 * @param locale the locale; uses the default locale if null
	 * @return the template or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Template getTemplate(String name, Locale locale) throws TemplateException;

    /**
	 * Returns the template with the specified name.
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}.
	 *  
	 * @param name the name of the template
	 * @param encoding the encoding; uses the default encoding if null
	 * @return the template or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Template getTemplate(String name, String encoding) throws TemplateException;

    /**
	 * Returns the template with the specified name.
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}.
	 *  
	 * @param name the name of the template
	 * @param locale the locale; uses the default locale if null
	 * @param encoding the encoding; uses the default encoding if null
	 * @return the template or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Template getTemplate(String name, Locale locale, String encoding) throws TemplateException;

    /**
	 * Returns the template described by the resource descriptor
	 *  
	 * @param key the key
	 * @return the template or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Template getTemplate(ResourceKey key) throws TemplateException;

    /**
	 * Returns the messages with the specified name.
	 * <br/><br/>
	 * The name is usually the template name. The extension will get replaced by ".properties".
	 * But internally it depends on the resolver, which you can enhance by implementing your own.
	 * 
	 * @param name the name of the template (The extension will get replaces by ".properties"
	 * @return the messages or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Messages getMessages(String name) throws TemplateException;

    /**
	 * Returns the messages with the specified name.
	 * <br/><br/>
	 * The name is usually the template name. The extension will get replaced by ".properties".
	 * But internally it depends on the resolver, which you can enhance by implementing your own.
	 * 
	 * @param name the name of the template (The extension will get replaces by ".properties"
	 * @param locale the locale; uses the default locale if null
	 * @return the messages or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Messages getMessages(String name, Locale locale) throws TemplateException;

    /**
	 * Returns the messages with the specified name.
	 * <br/><br/>
	 * The name is usually the template name. The extension will get replaced by ".properties".
	 * But internally it depends on the resolver, which you can enhance by implementing your own.
	 * 
	 * @param name the name of the template (The extension will get replaces by ".properties"
	 * @param encoding the encoding; uses the default encoding if null, but usually not needed, 
	 * 				   the messages are loaded from properties file, and properties files have their own
	 * 				   encoding
	 * @return the messages or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Messages getMessages(String name, String encoding) throws TemplateException;

    /**
	 * Returns the messages with the specified name.
	 * <br/><br/>
	 * The name is usually the template name. The extension will get replaced by ".properties".
	 * But internally it depends on the resolver, which you can enhance by implementing your own.
	 * 
	 * @param name the name of the template (The extension will get replaces by ".properties"
	 * @param locale the locale; uses the default locale if null
	 * @param encoding the encoding; uses the default encoding if null, but usually not needed, 
	 * 				   the messages are loaded from properties file, and properties files have their own
	 * 				   encoding
	 * @return the messages or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Messages getMessages(String name, Locale locale, String encoding) throws TemplateException;

    /**
	 * Returns the messages specified by the descriptor
	 * 
	 * @param key the key
	 * @return the messages or null if not found
	 * @throws TemplateException if anything goes wrong
	 */
    public Messages getMessages(ResourceKey key) throws TemplateException;

    /**
	 * Renders the template with the specified name to the specified writer.
	 * <br/><br/>
	 * The workingObject is just any object or bean, that contains the data
	 * that should be integrated in the template. 
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}. 
	 * 
	 * @param writer the writer
	 * @param workingObject the working object
	 * @param name the name of the template
	 * @throws TemplateException if anything goes wrong
	 * @throws IOException if it cannot write to the writer
	 */
    public void renderTemplate(Writer writer, Object workingObject, String name) throws TemplateException, IOException;

    /**
	 * Renders the template with the specified name to the specified writer.
	 * <br/><br/>
	 * The workingObject is just any object or bean, that contains the data
	 * that should be integrated in the template. 
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}. 
	 * 
	 * @param writer the writer
	 * @param workingObject the working object
	 * @param name the name of the template
	 * @param locale the locale; uses the default locale if null
	 * @throws TemplateException if anything goes wrong
	 * @throws IOException if it cannot write to the writer
	 */
    public void renderTemplate(Writer writer, Object workingObject, String name, Locale locale) throws TemplateException, IOException;

    /**
	 * Renders the template with the specified name to the specified writer.
	 * <br/><br/>
	 * The workingObject is just any object or bean, that contains the data
	 * that should be integrated in the template. 
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}. 
	 * 
	 * @param writer the writer
	 * @param workingObject the working object
	 * @param name the name of the template
	 * @param encoding the encoding; uses the default encoding if null
	 * @throws TemplateException if anything goes wrong
	 * @throws IOException if it cannot write to the writer
	 */
    public void renderTemplate(Writer writer, Object workingObject, String name, String encoding) throws TemplateException, IOException;

    /**
	 * Renders the template with the specified name to the specified writer.
	 * <br/><br/>
	 * The workingObject is just any object or bean, that contains the data
	 * that should be integrated in the template. 
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}. 
	 * 
	 * @param writer the writer
	 * @param workingObject the working object
	 * @param name the name of the template
	 * @param locale the locale; uses the default locale if null
	 * @param encoding the encoding; uses the default encoding if null
	 * @throws TemplateException if anything goes wrong
	 * @throws IOException if it cannot write to the writer
	 */
    public void renderTemplate(Writer writer, Object workingObject, String name, Locale locale, String encoding) throws TemplateException, IOException;

    /**
	 * Renders the template specified by the descriptor
	 * <br/><br/>
	 * The workingObject is just any object or bean, that contains the data
	 * that should be integrated in the template. 
	 * <br/><br/>
	 * The name of the template is usually some path on the file system or
	 * the resources but you can enhance this by implementing an own {@link Resolver}. 
	 * 
	 * @param writer the writer
	 * @param workingObject the working object
	 * @param key the key
	 * @throws TemplateException if anything goes wrong
	 * @throws IOException if it cannot write to the writer
	 */
    public void renderTemplate(Writer writer, Object workingObject, ResourceKey key) throws TemplateException, IOException;
}
