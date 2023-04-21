package org.jomc.model.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * Default {@code BootstrapContext} implementation.
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $Id: DefaultBootstrapContext.java 1750 2010-04-06 08:26:02Z schulte2005 $
 * @see BootstrapContext#createBootstrapContext(java.lang.ClassLoader)
 */
public class DefaultBootstrapContext extends BootstrapContext {

    /**
     * Classpath location searched for providers by default.
     * @see #getDefaultProviderLocation()
     */
    private static final String DEFAULT_PROVIDER_LOCATION = "META-INF/services";

    /**
     * Location searched for platform providers by default.
     * @see #getDefaultPlatformProviderLocation()
     */
    private static final String DEFAULT_PLATFORM_PROVIDER_LOCATION = new StringBuilder().append(System.getProperty("java.home")).append(File.separator).append("jre").append(File.separator).append("lib").append(File.separator).append("jomc.properties").toString();

    /**
     * Default bootstrap schema system id.
     * @see #getDefaultBootstrapSchemaSystemId()
     */
    private static final String DEFAULT_BOOTSTRAP_SCHEMA_SYSTEM_ID = "http://jomc.sourceforge.net/model/bootstrap/jomc-bootstrap-1.0.xsd";

    /** Default provider location. */
    private static volatile String defaultProviderLocation;

    /** Default platform provider location. */
    private static volatile String defaultPlatformProviderLocation;

    /** Default bootstrap schema system id. */
    private static volatile String defaultBootstrapSchemaSystemId;

    /** Provider location of the instance. */
    private String providerLocation;

    /** Platform provider location of the instance. */
    private String platformProviderLocation;

    /** Bootstrap schema system id of the instance. */
    private String bootstrapSchemaSystemId;

    /**
     * Creates a new {@code DefaultBootstrapContext} instance taking a class loader.
     *
     * @param classLoader The class loader of the context.
     */
    public DefaultBootstrapContext(final ClassLoader classLoader) {
        super(classLoader);
    }

    /**
     * Gets the default location searched for provider resources.
     * <p>The default provider location is controlled by system property
     * {@code org.jomc.model.bootstrap.DefaultBootstrapContext.defaultProviderLocation} holding the location to search
     * for provider resources by default. If that property is not set, the {@code META-INF/services} default is
     * returned.</p>
     *
     * @return The location searched for provider resources by default.
     *
     * @see #setDefaultProviderLocation(java.lang.String)
     */
    public static String getDefaultProviderLocation() {
        if (defaultProviderLocation == null) {
            defaultProviderLocation = System.getProperty("org.jomc.model.bootstrap.DefaultBootstrapContext.defaultProviderLocation", DEFAULT_PROVIDER_LOCATION);
        }
        return defaultProviderLocation;
    }

    /**
     * Sets the default location searched for provider resources.
     *
     * @param value The new default location to search for provider resources or {@code null}.
     *
     * @see #getDefaultProviderLocation()
     */
    public static void setDefaultProviderLocation(final String value) {
        defaultProviderLocation = value;
    }

    /**
     * Gets the location searched for provider resources.
     *
     * @return The location searched for provider resources.
     *
     * @see #getDefaultProviderLocation()
     * @see #setProviderLocation(java.lang.String)
     */
    public String getProviderLocation() {
        if (this.providerLocation == null) {
            this.providerLocation = getDefaultProviderLocation();
        }
        return this.providerLocation;
    }

    /**
     * Sets the location searched for provider resources.
     *
     * @param value The new location to search for provider resources or {@code null}.
     *
     * @see #getProviderLocation()
     */
    public void setProviderLocation(final String value) {
        this.providerLocation = value;
    }

    /**
     * Gets the default location searched for platform provider resources.
     * <p>The default platform provider location is controlled by system property
     * {@code org.jomc.model.bootstrap.DefaultBootstrapContext.defaultPlatformProviderLocation} holding the location to
     * search for platform provider resources by default. If that property is not set, the
     * {@code <java-home>/jre/lib/jomc.properties} default is returned.</p>
     *
     * @return The location searched for platform provider resources by default.
     *
     * @see #setDefaultPlatformProviderLocation(java.lang.String)
     */
    public static String getDefaultPlatformProviderLocation() {
        if (defaultPlatformProviderLocation == null) {
            defaultPlatformProviderLocation = System.getProperty("org.jomc.model.bootstrap.DefaultBootstrapContext.defaultPlatformProviderLocation", DEFAULT_PLATFORM_PROVIDER_LOCATION);
        }
        return defaultPlatformProviderLocation;
    }

    /**
     * Sets the default location searched for platform provider resources.
     *
     * @param value The new default location to search for platform provider resources or {@code null}.
     *
     * @see #getDefaultPlatformProviderLocation()
     */
    public static void setDefaultPlatformProviderLocation(final String value) {
        defaultPlatformProviderLocation = value;
    }

    /**
     * Gets the location searched for platform provider resources.
     *
     * @return The location searched for platform provider resources.
     *
     * @see #getDefaultPlatformProviderLocation()
     * @see #setPlatformProviderLocation(java.lang.String)
     */
    public String getPlatformProviderLocation() {
        if (this.platformProviderLocation == null) {
            this.platformProviderLocation = getDefaultPlatformProviderLocation();
        }
        return this.platformProviderLocation;
    }

    /**
     * Sets the location searched for platform provider resources.
     *
     * @param value The new location to search for platform provider resources or {@code null}.
     *
     * @see #getPlatformProviderLocation()
     */
    public void setPlatformProviderLocation(final String value) {
        this.platformProviderLocation = value;
    }

    /**
     * Gets the default bootstrap schema system id.
     * <p>The default bootstrap schema system id is controlled by system property
     * {@code org.jomc.model.bootstrap.DefaultBootstrapContext.defaultBootstrapSchemaSystemId} holding a system id URI.
     * If that property is not set, the
     * {@code http://jomc.sourceforge.net/model/bootstrap/jomc-bootstrap-1.0.xsd} default is returned.</p>
     *
     * @return The default system id of the bootstrap schema.
     *
     * @see #setDefaultBootstrapSchemaSystemId(java.lang.String)
     */
    public static String getDefaultBootstrapSchemaSystemId() {
        if (defaultBootstrapSchemaSystemId == null) {
            defaultBootstrapSchemaSystemId = System.getProperty("org.jomc.model.bootstrap.DefaultBootstrapContext.defaultBootstrapSchemaSystemId", DEFAULT_BOOTSTRAP_SCHEMA_SYSTEM_ID);
        }
        return defaultBootstrapSchemaSystemId;
    }

    /**
     * Sets the default bootstrap schema system id.
     *
     * @param value The new default bootstrap schema system id or {@code null}.
     *
     * @see #getDefaultBootstrapSchemaSystemId()
     */
    public static void setDefaultBootstrapSchemaSystemId(final String value) {
        defaultBootstrapSchemaSystemId = value;
    }

    /**
     * Gets the bootstrap schema system id.
     *
     * @return The bootstrap schema system id.
     *
     * @see #getDefaultBootstrapSchemaSystemId()
     * @see #setBootstrapSchemaSystemId(java.lang.String)
     */
    public String getBootstrapSchemaSystemId() {
        if (this.bootstrapSchemaSystemId == null) {
            this.bootstrapSchemaSystemId = getDefaultBootstrapSchemaSystemId();
        }
        return this.bootstrapSchemaSystemId;
    }

    /**
     * Sets the bootstrap schema system id.
     *
     * @param value The new bootstrap schema system id or {@code null}.
     *
     * @see #getBootstrapSchemaSystemId()
     */
    public void setBootstrapSchemaSystemId(final String value) {
        this.bootstrapSchemaSystemId = value;
    }

    /**
     * Searches the context for schemas.
     * <p>This method loads {@code SchemaProvider} classes setup via the platform provider configuration file and
     * {@code <provider-location>/org.jomc.model.bootstrap.SchemaProvider} resources to return a list of schemas.</p>
     *
     * @return The schemas found in the context.
     *
     * @throws BootstrapException if searching schemas fails.
     *
     * @see #getProviderLocation()
     * @see #getPlatformProviderLocation()
     * @see SchemaProvider#findSchemas(org.jomc.model.bootstrap.BootstrapContext)
     */
    @Override
    public Schemas findSchemas() throws BootstrapException {
        try {
            final Schemas schemas = new Schemas();
            final Collection<Class<? extends SchemaProvider>> providers = this.loadProviders(SchemaProvider.class);
            for (Class<? extends SchemaProvider> provider : providers) {
                final SchemaProvider schemaProvider = provider.newInstance();
                final Schemas provided = schemaProvider.findSchemas(this);
                if (provided != null) {
                    schemas.getSchema().addAll(provided.getSchema());
                }
            }
            final javax.xml.validation.Schema bootstrapSchema = this.createSchema();
            final Validator validator = bootstrapSchema.newValidator();
            validator.validate(new JAXBSource(this.createContext(), new ObjectFactory().createSchemas(schemas)));
            return schemas;
        } catch (final InstantiationException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final JAXBException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final SAXException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    /**
     * Searches the context for services.
     * <p>This method loads {@code ServiceProvider} classes setup via the platform provider configuration file and
     * {@code <provider-location>/org.jomc.model.bootstrap.ServiceProvider} resources to return a list of services.</p>
     *
     * @return The services found in the context.
     *
     * @throws BootstrapException if searching services fails.
     *
     * @see #getProviderLocation()
     * @see #getPlatformProviderLocation()
     * @see ServiceProvider#findServices(org.jomc.model.bootstrap.BootstrapContext)
     */
    @Override
    public Services findServices() throws BootstrapException {
        try {
            final Services services = new Services();
            final Collection<Class<? extends ServiceProvider>> providers = this.loadProviders(ServiceProvider.class);
            for (Class<? extends ServiceProvider> provider : providers) {
                final ServiceProvider serviceProvider = provider.newInstance();
                final Services provided = serviceProvider.findServices(this);
                if (provided != null) {
                    services.getService().addAll(provided.getService());
                }
            }
            final javax.xml.validation.Schema bootstrapSchema = this.createSchema();
            final Validator validator = bootstrapSchema.newValidator();
            validator.validate(new JAXBSource(this.createContext(), new ObjectFactory().createServices(services)));
            return services;
        } catch (final InstantiationException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final JAXBException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final SAXException e) {
            throw new BootstrapException(e.getMessage(), e);
        } catch (final IOException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    @Override
    public javax.xml.validation.Schema createSchema() throws BootstrapException {
        try {
            return SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(this.getClass().getResource("/org/jomc/model/bootstrap/jomc-bootstrap-1.0.xsd"));
        } catch (final SAXException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    @Override
    public JAXBContext createContext() throws BootstrapException {
        try {
            return JAXBContext.newInstance(Schemas.class.getPackage().getName(), this.getClassLoader());
        } catch (final JAXBException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    @Override
    public Marshaller createMarshaller() throws BootstrapException {
        try {
            final Marshaller m = this.createContext().createMarshaller();
            m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://jomc.org/model/bootstrap " + this.getBootstrapSchemaSystemId());
            return m;
        } catch (final JAXBException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    @Override
    public Unmarshaller createUnmarshaller() throws BootstrapException {
        try {
            return this.createContext().createUnmarshaller();
        } catch (final JAXBException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    private <T> Collection<Class<? extends T>> loadProviders(final Class<T> providerClass) throws BootstrapException {
        try {
            final String providerNamePrefix = providerClass.getName() + ".";
            final Map<String, Class<? extends T>> providers = new TreeMap<String, Class<? extends T>>(new Comparator<String>() {

                public int compare(final String key1, final String key2) {
                    return key1.compareTo(key2);
                }
            });
            final File platformProviders = new File(this.getPlatformProviderLocation());
            if (platformProviders.exists()) {
                InputStream in = null;
                final java.util.Properties p = new java.util.Properties();
                try {
                    in = new FileInputStream(platformProviders);
                    p.load(in);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                for (Map.Entry e : p.entrySet()) {
                    if (e.getKey().toString().startsWith(providerNamePrefix)) {
                        final Class<?> provider = this.findClass(e.getValue().toString());
                        if (provider == null) {
                            throw new BootstrapException(getMessage("implementationNotFound", providerClass.getName(), e.getValue().toString(), platformProviders.getAbsolutePath()));
                        }
                        if (!providerClass.isAssignableFrom(provider)) {
                            throw new BootstrapException(getMessage("illegalImplementation", providerClass.getName(), e.getValue().toString(), platformProviders.getAbsolutePath()));
                        }
                        providers.put(e.getKey().toString(), provider.asSubclass(providerClass));
                    }
                }
            }
            final Enumeration<URL> classpathProviders = this.findResources(this.getProviderLocation() + '/' + providerClass.getName());
            while (classpathProviders.hasMoreElements()) {
                final URL url = classpathProviders.nextElement();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("#")) {
                        continue;
                    }
                    final Class<?> provider = this.findClass(line);
                    if (provider == null) {
                        throw new BootstrapException(getMessage("implementationNotFound", providerClass.getName(), line, url.toExternalForm()));
                    }
                    if (!providerClass.isAssignableFrom(provider)) {
                        throw new BootstrapException(getMessage("illegalImplementation", providerClass.getName(), line, url.toExternalForm()));
                    }
                    providers.put(providerNamePrefix + providers.size(), provider.asSubclass(providerClass));
                }
                reader.close();
            }
            return providers.values();
        } catch (final IOException e) {
            throw new BootstrapException(e.getMessage(), e);
        }
    }

    private static String getMessage(final String key, final Object... arguments) {
        return MessageFormat.format(ResourceBundle.getBundle(DefaultBootstrapContext.class.getName().replace('.', '/')).getString(key), arguments);
    }
}
