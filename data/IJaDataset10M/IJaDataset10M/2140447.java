package org.jomc.mojo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.apache.maven.plugin.assembly.filter.ContainerDescriptorHandler;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.ResourceIterator;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.StringUtils;
import org.jomc.model.ModelObject;
import org.jomc.model.Module;
import org.jomc.model.Modules;
import org.jomc.model.modlet.DefaultModelProvider;
import org.jomc.modlet.DefaultModelContext;
import org.jomc.modlet.DefaultModletProvider;
import org.jomc.modlet.ModelContext;
import org.jomc.modlet.ModelContextFactory;
import org.jomc.modlet.ModelException;
import org.jomc.modlet.Modlet;
import org.jomc.modlet.ModletObject;
import org.jomc.modlet.Modlets;

/**
 * Maven Assembly Plugin {@code ContainerDescriptorHandler} implementation for assembling JOMC resources.
 *
 * <p><b>Maven Assembly Plugin Usage</b><pre>
 * &lt;containerDescriptorHandler&gt;
 *   &lt;handlerName&gt;JOMC&lt;/handlerName&gt;
 *   &lt;configuration&gt;
 *     &lt;model&gt;http://jomc.org/model&lt;/model&gt;
 *     &lt;modelContextFactoryClassName&gt;class name&lt;/modelContextFactoryClassName&gt;
 *     &lt;modelContextAttributes&gt;
 *       &lt;modelContextAttribute&gt;
 *         &lt;key&gt;The name of the attribute&lt;/key&gt;
 *         &lt;value&gt;The name of the attribute&lt;/value&gt;
 *         &lt;type&gt;The name of the class of the object.&lt;/type&gt;
 *       &lt;/modelContextAttribute&gt;
 *     &lt;/modelContextAttributes/&gt;
 *     &lt;moduleEncoding&gt;${project.build.sourceEncoding}&lt;/moduleEncoding&gt;
 *     &lt;moduleName&gt;${project.name}&lt;/moduleName&gt;
 *     &lt;moduleVersion&gt;${project.version}&lt;/moduleVersion&gt;
 *     &lt;moduleVendor&gt;${project.organization.name}&lt;/moduleVendor&gt;
 *     &lt;moduleResource&gt;META-INF/custom-jomc.xml&lt;/moduleResource&gt;
 *     &lt;moduleResources&gt;
 *       &lt;moduleResource&gt;META-INF/jomc.xml&lt;/moduleResource&gt;
 *     &lt;/moduleResources&gt;
 *     &lt;moduleIncludes&gt;
 *       &lt;moduleInclude&gt;module name&lt;/moduleInclude&gt;
 *     &lt;/moduleIncludes&gt;
 *     &lt;moduleExcludes&gt;
 *       &lt;moduleExclude&gt;module name&lt;/moduleExclude&gt;
 *     &lt;/moduleExcludes&gt;
 *     &lt;modletEncoding&gt;${project.build.sourceEncoding}&lt;/modletEncoding&gt;
 *     &lt;modletName&gt;${project.name}&lt;/modletName&gt;
 *     &lt;modletVersion&gt;${project.version}&lt;/modletVersion&gt;
 *     &lt;modletVendor&gt;${project.organization.name}&lt;/modletVendor&gt;
 *     &lt;modletResource&gt;META-INF/custom-jomc-modlet.xml&lt;/modletResource&gt;
 *     &lt;modletResources&gt;
 *       &lt;modletResource&gt;META-INF/jomc-modlet.xml&lt;/modletResource&gt;
 *     &lt;/modletResources&gt;
 *     &lt;modletIncludes&gt;
 *       &lt;modletInclude&gt;modlet name&lt;/modletInclude&gt;
 *     &lt;/modletIncludes&gt;
 *     &lt;modletExcludes&gt;
 *       &lt;modletExclude&gt;modlet name&lt;/modletExclude&gt;
 *     &lt;/modletExcludes&gt;
 *     &lt;modelObjectStylesheet&gt;Location of a XSLT document to use for transforming the merged model document.&lt;/modelObjectStylesheet&gt;
 *     &lt;modletObjectStylesheet&gt;Location of a XSLT document to use for transforming the merged modlet document.&lt;/modletObjectStylesheet&gt;
 *     &lt;providerLocation&gt;META-INF/custom-services&lt;/providerLocation&gt;
 *     &lt;platformProviderLocation&gt;${java.home}/jre/lib/custom-jomc.properties&lt;/platformProviderLocation&gt;
 *     &lt;modletLocation&gt;META-INF/custom-jomc-modlet.xml&lt;/modletLocation&gt;
 *     &lt;modletSchemaSystemId&gt;http://custom.host.tld/custom/path/jomc-modlet-1.3.xsd&lt;/modletSchemaSystemId&gt;
 *   &lt;/configuration&gt;
 * &lt;/containerDescriptorHandler&gt;
 * </pre></p>
 *
 * @author <a href="mailto:schulte2005@users.sourceforge.net">Christian Schulte</a>
 * @version $JOMC$
 * @since 1.2
 * @plexus.component role="org.apache.maven.plugin.assembly.filter.ContainerDescriptorHandler"
 *                   role-hint="JOMC"
 */
public class JomcContainerDescriptorHandler extends AbstractLogEnabled implements ContainerDescriptorHandler {

    /** Prefix prepended to log messages. */
    private static final String LOG_PREFIX = "[JOMC] ";

    /** The identifier of the model to process. */
    private String model = ModelObject.MODEL_PUBLIC_ID;

    /** The encoding of the assembled module. */
    private String moduleEncoding;

    /** The name of the assembled module. */
    private String moduleName;

    /** The version of the assembled module. */
    private String moduleVersion;

    /** The vendor of the assembled module. */
    private String moduleVendor;

    /** The resource name of the assembled module. */
    private String moduleResource = DefaultModelProvider.getDefaultModuleLocation();

    /** Names of resources to process. */
    private String[] moduleResources = { DefaultModelProvider.getDefaultModuleLocation() };

    /** Included modules. */
    private List<String> moduleIncludes;

    /** Excluded modules. */
    private List<String> moduleExcludes;

    /** The encoding of the assembled modlet. */
    private String modletEncoding;

    /** The name of the assembled modlet. */
    private String modletName;

    /** The version of the assembled modlet. */
    private String modletVersion;

    /** The vendor of the assembled modlet. */
    private String modletVendor;

    /** The resource name of the assembled modlet resources. */
    private String modletResource = DefaultModletProvider.getDefaultModletLocation();

    /** Names of modlet resources to process. */
    private String[] modletResources = { DefaultModletProvider.getDefaultModletLocation() };

    /** Included modlets. */
    private List<String> modletIncludes;

    /** Excluded modlets. */
    private List<String> modletExcludes;

    /** Location of a XSLT document to use for transforming the merged model document. */
    private String modelObjectStylesheet;

    /** Location of a XSLT document to use for transforming the merged modlet document. */
    private String modletObjectStylesheet;

    /** The location to search for providers. */
    private String providerLocation;

    /** The location to search for platform providers. */
    private String platformProviderLocation;

    /** The system id of the modlet schema. */
    private String modletSchemaSystemId;

    /** The location to search for modlets. */
    private String modletLocation;

    /** Name of the {@code ModelContextFactory} implementation class. */
    private String modelContextFactoryClassName;

    /** {@code ModelContext} attributes to apply. */
    private List<ModelContextAttribute> modelContextAttributes;

    /** Modlet resources. */
    private Modlets modlets = new Modlets();

    /** Model resources. */
    private Modules modules = new Modules();

    /** The JOMC JAXB marshaller of the instance. */
    private Marshaller jomcMarshaller;

    /** The JOMC JAXB unmarshaller of the instance. */
    private Unmarshaller jomcUnmarshaller;

    /** The modlet JAXB marshaller of the instance. */
    private Marshaller modletMarshaller;

    /** The modlet JAXB unmarshaller of the instance. */
    private Unmarshaller modletUnmarshaller;

    /** Creates a new {@code JomcContainerDescriptorHandler} instance. */
    public JomcContainerDescriptorHandler() {
        super();
    }

    public void finalizeArchiveCreation(final Archiver archiver) throws ArchiverException {
        if (StringUtils.isEmpty(this.model)) {
            throw new ArchiverException(Messages.getMessage("mandatoryParameter", "model"));
        }
        if (StringUtils.isEmpty(this.modletName)) {
            throw new ArchiverException(Messages.getMessage("mandatoryParameter", "modletName"));
        }
        if (StringUtils.isEmpty(this.modletResource)) {
            throw new ArchiverException(Messages.getMessage("mandatoryParameter", "modletResource"));
        }
        if (StringUtils.isEmpty(this.moduleName)) {
            throw new ArchiverException(Messages.getMessage("mandatoryParameter", "moduleName"));
        }
        if (StringUtils.isEmpty(this.moduleResource)) {
            throw new ArchiverException(Messages.getMessage("mandatoryParameter", "moduleResource"));
        }
        try {
            for (final ResourceIterator it = archiver.getResources(); it.hasNext(); it.next()) ;
            if (!this.modules.getModule().isEmpty()) {
                if (this.moduleIncludes != null) {
                    for (final Iterator<Module> it = this.modules.getModule().iterator(); it.hasNext(); ) {
                        final Module m = it.next();
                        if (!this.moduleIncludes.contains(m.getName())) {
                            it.remove();
                            if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                                this.getLogger().info(LOG_PREFIX + Messages.getMessage("excludingModule", m.getName()));
                            }
                        }
                    }
                }
                if (this.moduleExcludes != null) {
                    for (String exclude : this.moduleExcludes) {
                        final Module excluded = this.modules.getModule(exclude);
                        if (excluded != null) {
                            this.modules.getModule().remove(excluded);
                            if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                                this.getLogger().info(LOG_PREFIX + Messages.getMessage("excludingModule", excluded.getName()));
                            }
                        }
                    }
                }
                if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                    for (Module m : this.modules.getModule()) {
                        this.getLogger().info(LOG_PREFIX + Messages.getMessage("includingModule", m.getName()));
                    }
                }
                final Module mergedModule = this.modules.getMergedModule(this.moduleName);
                mergedModule.setVersion(this.moduleVersion);
                mergedModule.setVendor(this.moduleVendor);
                final JAXBElement<Module> transformedModule = this.transformModelObject(new org.jomc.model.ObjectFactory().createModule(mergedModule), Module.class);
                final File moduleFile = File.createTempFile("maven-assembly-plugin", ".tmp");
                moduleFile.deleteOnExit();
                this.marshalModelObject(transformedModule, moduleFile);
                archiver.addFile(moduleFile, normalizeResourceName(this.moduleResource));
            }
            if (!this.modlets.getModlet().isEmpty()) {
                if (this.modletIncludes != null) {
                    for (final Iterator<Modlet> it = this.modlets.getModlet().iterator(); it.hasNext(); ) {
                        final Modlet m = it.next();
                        if (!this.modletIncludes.contains(m.getName())) {
                            it.remove();
                            if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                                this.getLogger().info(LOG_PREFIX + Messages.getMessage("excludingModlet", m.getName()));
                            }
                        }
                    }
                }
                if (this.modletExcludes != null) {
                    for (String exclude : this.modletExcludes) {
                        final Modlet excluded = this.modlets.getModlet(exclude);
                        if (excluded != null) {
                            this.modlets.getModlet().remove(excluded);
                            if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                                this.getLogger().info(LOG_PREFIX + Messages.getMessage("excludingModlet", excluded.getName()));
                            }
                        }
                    }
                }
                if (this.getLogger() != null && this.getLogger().isInfoEnabled()) {
                    for (Modlet m : this.modlets.getModlet()) {
                        this.getLogger().info(LOG_PREFIX + Messages.getMessage("includingModlet", m.getName()));
                    }
                }
                final Modlet mergedModlet = this.modlets.getMergedModlet(this.modletName, this.model);
                mergedModlet.setVendor(this.modletVendor);
                mergedModlet.setVersion(this.modletVersion);
                final JAXBElement<Modlet> transformedModlet = this.transformModletObject(new org.jomc.modlet.ObjectFactory().createModlet(mergedModlet), Modlet.class);
                final File modletFile = File.createTempFile("maven-assembly-plugin", ".tmp");
                modletFile.deleteOnExit();
                this.marshalModletObject(transformedModlet, modletFile);
                archiver.addFile(modletFile, normalizeResourceName(this.modletResource));
            }
        } catch (final InstantiationException e) {
            throw new ArchiverException(Messages.getMessage(e), e);
        } catch (final TransformerConfigurationException e) {
            String message = Messages.getMessage(e);
            if (message == null && e.getException() != null) {
                message = Messages.getMessage(e.getException());
            }
            throw new ArchiverException(message, e);
        } catch (final TransformerException e) {
            String message = Messages.getMessage(e);
            if (message == null && e.getException() != null) {
                message = Messages.getMessage(e.getException());
            }
            throw new ArchiverException(message, e);
        } catch (final JAXBException e) {
            String message = Messages.getMessage(e);
            if (message == null && e.getLinkedException() != null) {
                message = Messages.getMessage(e.getLinkedException());
            }
            throw new ArchiverException(message, e);
        } catch (final ModelException e) {
            throw new ArchiverException(Messages.getMessage(e), e);
        } catch (final IOException e) {
            throw new ArchiverException(Messages.getMessage(e), e);
        } catch (final URISyntaxException e) {
            throw new ArchiverException(Messages.getMessage(e), e);
        } finally {
            this.modlets = new Modlets();
            this.modules = new Modules();
            this.jomcMarshaller = null;
            this.jomcUnmarshaller = null;
            this.modletMarshaller = null;
            this.modletUnmarshaller = null;
        }
    }

    public void finalizeArchiveExtraction(final UnArchiver unarchiver) throws ArchiverException {
    }

    public List<String> getVirtualFiles() {
        final List<String> virtualFiles = new LinkedList<String>();
        if (!this.modlets.getModlet().isEmpty()) {
            virtualFiles.add(normalizeResourceName(this.modletResource));
        }
        if (!this.modules.getModule().isEmpty()) {
            virtualFiles.add(normalizeResourceName(this.moduleResource));
        }
        return virtualFiles.isEmpty() ? null : Collections.unmodifiableList(virtualFiles);
    }

    public boolean isSelected(final FileInfo fileInfo) throws IOException {
        try {
            boolean selected = true;
            final String name = normalizeResourceName(fileInfo.getName());
            if (this.moduleResources != null) {
                for (String r : this.moduleResources) {
                    if (name.equals(normalizeResourceName(r))) {
                        Object modelObject = this.unmarshalModelObject(fileInfo.getContents());
                        if (modelObject instanceof JAXBElement<?>) {
                            modelObject = ((JAXBElement<?>) modelObject).getValue();
                        }
                        if (modelObject instanceof Modules) {
                            this.modules.getModule().addAll(((Modules) modelObject).getModule());
                        }
                        if (modelObject instanceof Module) {
                            this.modules.getModule().add((Module) modelObject);
                        }
                        if (this.getLogger() != null && this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug(LOG_PREFIX + Messages.getMessage("processingModuleResource", name));
                        }
                        selected = false;
                        break;
                    }
                }
            }
            if (selected && this.modletResources != null) {
                for (String r : this.modletResources) {
                    if (name.equals(normalizeResourceName(r))) {
                        Object modletObject = this.unmarshalModletObject(fileInfo.getContents());
                        if (modletObject instanceof JAXBElement<?>) {
                            modletObject = ((JAXBElement<?>) modletObject).getValue();
                        }
                        if (modletObject instanceof Modlets) {
                            this.modlets.getModlet().addAll(((Modlets) modletObject).getModlet());
                        }
                        if (modletObject instanceof Modlet) {
                            this.modlets.getModlet().add((Modlet) modletObject);
                        }
                        if (this.getLogger() != null && this.getLogger().isDebugEnabled()) {
                            this.getLogger().debug(LOG_PREFIX + Messages.getMessage("processingModletResource", name));
                        }
                        selected = false;
                        break;
                    }
                }
            }
            if (selected && (name.equals(normalizeResourceName(this.modletResource)) || name.equals(normalizeResourceName(this.moduleResource)))) {
                if (this.getLogger() != null && this.getLogger().isWarnEnabled()) {
                    this.getLogger().warn(LOG_PREFIX + Messages.getMessage("overridingResource", name));
                }
                selected = false;
            }
            return selected;
        } catch (final InstantiationException e) {
            throw (IOException) new IOException(Messages.getMessage(e)).initCause(e);
        } catch (final JAXBException e) {
            String message = Messages.getMessage(e);
            if (message == null && e.getLinkedException() != null) {
                message = Messages.getMessage(e.getLinkedException());
            }
            throw (IOException) new IOException(message).initCause(e);
        } catch (final ModelException e) {
            throw (IOException) new IOException(Messages.getMessage(e)).initCause(e);
        }
    }

    /**
     * Creates an {@code URL} for a given resource location.
     * <p>This method first searches the class loader of the class for a single resource matching {@code location}. If
     * such a resource is found, the URL of that resource is returned. If no such resource is found, an attempt is made
     * to parse the given location to an URL. On successful parsing, that URL is returned. Failing that, the given
     * location is interpreted as a file name. If that file is found, the URL of that file is returned. Otherwise an
     * {@code IOException} is thrown.</p>
     *
     * @param location The location to create an {@code URL} from.
     *
     * @return An {@code URL} for {@code location}.
     *
     * @throws NullPointerException if {@code location} is {@code null}.
     * @throws IOException if creating an URL fails.
     */
    protected URL getResource(final String location) throws IOException {
        if (location == null) {
            throw new NullPointerException("location");
        }
        try {
            String absolute = location;
            if (!absolute.startsWith("/")) {
                absolute = "/" + location;
            }
            URL resource = this.getClass().getResource(absolute);
            if (resource == null) {
                try {
                    resource = new URL(location);
                } catch (final MalformedURLException e) {
                    if (this.getLogger() != null && this.getLogger().isDebugEnabled()) {
                        this.getLogger().debug(Messages.getMessage(e), e);
                    }
                    resource = null;
                }
            }
            if (resource == null) {
                final File f = new File(location);
                if (f.isFile()) {
                    resource = f.toURI().toURL();
                }
            }
            if (resource == null) {
                throw new IOException(Messages.getMessage("resourceNotFound", location));
            }
            return resource;
        } catch (final MalformedURLException e) {
            String m = Messages.getMessage(e);
            m = m == null ? "" : " " + m;
            throw (IOException) new IOException(Messages.getMessage("malformedLocation", location, m)).initCause(e);
        }
    }

    private Object unmarshalModelObject(final InputStream in) throws ModelException, JAXBException, InstantiationException {
        if (in == null) {
            throw new NullPointerException("in");
        }
        if (this.jomcUnmarshaller == null) {
            this.jomcUnmarshaller = this.createModelContext().createUnmarshaller(this.model);
        }
        return this.jomcUnmarshaller.unmarshal(in);
    }

    private void marshalModelObject(final JAXBElement<? extends ModelObject> element, final File file) throws ModelException, JAXBException, InstantiationException {
        if (element == null) {
            throw new NullPointerException("element");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (this.jomcMarshaller == null) {
            final ModelContext modelContext = this.createModelContext();
            this.jomcMarshaller = modelContext.createMarshaller(this.model);
            this.jomcMarshaller.setSchema(modelContext.createSchema(this.model));
            this.jomcMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (this.moduleEncoding != null) {
                this.jomcMarshaller.setProperty(Marshaller.JAXB_ENCODING, this.moduleEncoding);
            }
        }
        this.jomcMarshaller.marshal(element, file);
    }

    private <T> JAXBElement<T> transformModelObject(final JAXBElement<? extends ModelObject> element, final Class<T> boundType) throws ModelException, TransformerException, JAXBException, IOException, URISyntaxException, InstantiationException {
        if (element == null) {
            throw new NullPointerException("element");
        }
        if (!boundType.isInstance(element.getValue())) {
            throw new IllegalArgumentException(element.toString());
        }
        @SuppressWarnings("unchecked") JAXBElement<T> transformed = (JAXBElement<T>) element;
        if (this.modelObjectStylesheet != null) {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(this.getResource(this.modelObjectStylesheet).toURI().toASCIIString()));
            final ModelContext modelContext = this.createModelContext();
            final Marshaller marshaller = modelContext.createMarshaller(this.model);
            final Unmarshaller unmarshaller = modelContext.createUnmarshaller(this.model);
            final JAXBSource source = new JAXBSource(marshaller, element);
            final JAXBResult result = new JAXBResult(unmarshaller);
            for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
                transformer.setParameter(e.getKey().toString(), e.getValue());
            }
            transformer.transform(source, result);
            if (result.getResult() instanceof JAXBElement<?> && boundType.isInstance(((JAXBElement<?>) result.getResult()).getValue())) {
                @SuppressWarnings("unchecked") final JAXBElement<T> e = (JAXBElement<T>) result.getResult();
                transformed = e;
            } else {
                throw new ModelException(Messages.getMessage("illegalModuleTransformationResult", this.modelObjectStylesheet));
            }
        }
        return transformed;
    }

    private Object unmarshalModletObject(final InputStream in) throws ModelException, JAXBException, InstantiationException {
        if (in == null) {
            throw new NullPointerException("in");
        }
        if (this.modletUnmarshaller == null) {
            this.modletUnmarshaller = this.createModelContext().createUnmarshaller(ModletObject.MODEL_PUBLIC_ID);
        }
        return this.modletUnmarshaller.unmarshal(in);
    }

    private void marshalModletObject(final JAXBElement<? extends ModletObject> element, final File file) throws ModelException, JAXBException, InstantiationException {
        if (element == null) {
            throw new NullPointerException("element");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (this.modletMarshaller == null) {
            final ModelContext modletContext = this.createModelContext();
            this.modletMarshaller = modletContext.createMarshaller(ModletObject.MODEL_PUBLIC_ID);
            this.modletMarshaller.setSchema(modletContext.createSchema(ModletObject.MODEL_PUBLIC_ID));
            this.modletMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            if (this.modletEncoding != null) {
                this.modletMarshaller.setProperty(Marshaller.JAXB_ENCODING, this.modletEncoding);
            }
        }
        this.modletMarshaller.marshal(element, file);
    }

    private <T> JAXBElement<T> transformModletObject(final JAXBElement<? extends ModletObject> element, final Class<T> boundType) throws ModelException, TransformerException, JAXBException, IOException, URISyntaxException, InstantiationException {
        if (element == null) {
            throw new NullPointerException("element");
        }
        if (!boundType.isInstance(element.getValue())) {
            throw new IllegalArgumentException(element.toString());
        }
        @SuppressWarnings("unchecked") JAXBElement<T> transformed = (JAXBElement<T>) element;
        if (this.modletObjectStylesheet != null) {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(this.getResource(this.modletObjectStylesheet).toURI().toASCIIString()));
            final ModelContext modletContext = this.createModelContext();
            final Marshaller marshaller = modletContext.createMarshaller(ModletObject.MODEL_PUBLIC_ID);
            final Unmarshaller unmarshaller = modletContext.createUnmarshaller(ModletObject.MODEL_PUBLIC_ID);
            final JAXBSource source = new JAXBSource(marshaller, element);
            final JAXBResult result = new JAXBResult(unmarshaller);
            for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
                transformer.setParameter(e.getKey().toString(), e.getValue());
            }
            transformer.transform(source, result);
            if (result.getResult() instanceof JAXBElement<?> && boundType.isInstance(((JAXBElement<?>) result.getResult()).getValue())) {
                @SuppressWarnings("unchecked") final JAXBElement<T> e = (JAXBElement<T>) result.getResult();
                transformed = e;
            } else {
                throw new ModelException(Messages.getMessage("illegalModletTransformationResult", this.modletObjectStylesheet));
            }
        }
        return transformed;
    }

    private static String normalizeResourceName(final String name) {
        String normalized = name;
        if (normalized != null) {
            normalized = normalized.replace('\\', '/');
            if (normalized.startsWith("/")) {
                normalized = normalized.substring(1);
            }
            if (normalized.endsWith("/")) {
                normalized = normalized.substring(0, normalized.length());
            }
        }
        return normalized;
    }

    private ModelContext createModelContext() throws ModelException, InstantiationException {
        final ModelContextFactory modelContextFactory;
        if (this.modelContextFactoryClassName != null) {
            modelContextFactory = ModelContextFactory.newInstance(this.modelContextFactoryClassName);
        } else {
            modelContextFactory = ModelContextFactory.newInstance();
        }
        final ModelContext modelContext = modelContextFactory.newModelContext();
        modelContext.setModletSchemaSystemId(this.modletSchemaSystemId);
        if (this.providerLocation != null) {
            modelContext.setAttribute(DefaultModelContext.PROVIDER_LOCATION_ATTRIBUTE_NAME, this.providerLocation);
        }
        if (this.platformProviderLocation != null) {
            modelContext.setAttribute(DefaultModelContext.PLATFORM_PROVIDER_LOCATION_ATTRIBUTE_NAME, this.platformProviderLocation);
        }
        if (this.modletLocation != null) {
            modelContext.setAttribute(DefaultModletProvider.MODLET_LOCATION_ATTRIBUTE_NAME, this.modletLocation);
        }
        if (this.modelContextAttributes != null) {
            for (ModelContextAttribute e : this.modelContextAttributes) {
                final Object object = e.getObject();
                if (object != null) {
                    modelContext.setAttribute(e.getKey(), object);
                } else {
                    modelContext.clearAttribute(e.getKey());
                }
            }
        }
        return modelContext;
    }
}
