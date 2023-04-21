package org.jomc.sdk.model.support;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.ValidatorHandler;
import org.jomc.sdk.model.SchemaType;
import org.jomc.sdk.model.SchemasType;
import static org.jomc.sdk.model.modlet.SdkModelProvider.XML_SCHEMA_JAVA_CLASSPATH_ID_ATTRIBUTE;
import org.xml.sax.SAXException;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public final class JaxpValidatorHandlerFactory {

    public ValidatorHandler getObject() throws IOException, SAXException {
        ValidatorHandler validatorHandler = null;
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(this.getSchemas().getLanguageId());
        final SchemasType schemas = this.getSchemas();
        final List<Source> sources = new ArrayList<Source>(schemas.getSchema().size());
        for (SchemaType s : schemas.getSchema()) {
            final StreamSource source = new StreamSource();
            source.setPublicId(s.getPublicId());
            source.setSystemId(s.getSystemId());
            if (s.getOtherAttributes().containsKey(XML_SCHEMA_JAVA_CLASSPATH_ID_ATTRIBUTE)) {
                String absoluteLocation = s.getOtherAttributes().get(XML_SCHEMA_JAVA_CLASSPATH_ID_ATTRIBUTE);
                if (!absoluteLocation.startsWith("/")) {
                    absoluteLocation = '/' + absoluteLocation;
                }
                final URL resource = this.getClass().getResource(absoluteLocation);
                if (resource != null) {
                    source.setSystemId(resource.toExternalForm());
                    source.setInputStream(resource.openStream());
                }
            }
            sources.add(source);
        }
        if (!sources.isEmpty()) {
            validatorHandler = schemaFactory.newSchema(sources.toArray(new Source[sources.size()])).newValidatorHandler();
            if (this.isErrorHandler()) {
                validatorHandler.setErrorHandler(this.getErrorHandler());
            }
            if (this.isResourceResolver()) {
                validatorHandler.setResourceResolver(this.getResourceResolver());
            }
            for (Map.Entry<String, Boolean> e : this.getValidatorHandlerFeatures().entrySet()) {
                validatorHandler.setFeature(e.getKey(), e.getValue());
            }
            for (Map.Entry<String, Object> e : this.getValidatorHandlerProperties().entrySet()) {
                validatorHandler.setProperty(e.getKey(), e.getValue());
            }
        }
        return validatorHandler;
    }

    /** Creates a new {@code JaxpValidatorHandlerFactory} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public JaxpValidatorHandlerFactory() {
        super();
    }

    /**
     * Gets the {@code <errorHandler>} dependency.
     * <p>
     *   This method returns the {@code <JOMC :: JAXP Validator Handler Factory :: Default>} object of the {@code <org.xml.sax.ErrorHandler>} specification at any specification level.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <errorHandler>} dependency.
     * {@code null} if no object is available.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private org.xml.sax.ErrorHandler getErrorHandler() {
        return (org.xml.sax.ErrorHandler) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "errorHandler");
    }

    /**
     * Gets the {@code <resourceResolver>} dependency.
     * <p>
     *   This method returns the {@code <JOMC :: JAXP Validator Handler Factory :: Default>} object of the {@code <org.w3c.dom.ls.LSResourceResolver>} specification at any specification level.
     *   That specification does not apply to any scope. A new object is returned whenever requested and bound to this instance.
     * </p>
     * <dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl>
     * @return The {@code <resourceResolver>} dependency.
     * {@code null} if no object is available.
     * @throws org.jomc.ObjectManagementException if getting the dependency instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private org.w3c.dom.ls.LSResourceResolver getResourceResolver() {
        return (org.w3c.dom.ls.LSResourceResolver) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getDependency(this, "resourceResolver");
    }

    /**
     * Gets the value of the {@code <errorHandler>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Flag indicating the {@code errorHandler} dependency is ignored. See {@link javax.xml.validation.ValidatorHandler#setErrorHandler(org.xml.sax.ErrorHandler)}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private boolean isErrorHandler() {
        final java.lang.Boolean _p = (java.lang.Boolean) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "errorHandler");
        assert _p != null : "'errorHandler' property not found.";
        return _p.booleanValue();
    }

    /**
     * Gets the value of the {@code <resourceResolver>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return Flag indicating the {@code resourceResolver} dependency is ignored. See {@link javax.xml.validation.ValidatorHandler#setResourceResolver(org.w3c.dom.ls.LSResourceResolver)}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private boolean isResourceResolver() {
        final java.lang.Boolean _p = (java.lang.Boolean) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "resourceResolver");
        assert _p != null : "'resourceResolver' property not found.";
        return _p.booleanValue();
    }

    /**
     * Gets the value of the {@code <schemas>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return List of XML schemas ({@code schemas} element from XML namespace {@code http://jomc.org/sdk/model}).
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private org.jomc.sdk.model.SchemasType getSchemas() {
        final org.jomc.sdk.model.SchemasType _p = (org.jomc.sdk.model.SchemasType) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "schemas");
        assert _p != null : "'schemas' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <validatorHandlerFeatures>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return See {@link javax.xml.validation.ValidatorHandler#setFeature(java.lang.String, boolean)}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private java.util.Map<String, Boolean> getValidatorHandlerFeatures() {
        final java.util.Map<String, Boolean> _p = (java.util.Map<String, Boolean>) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "validatorHandlerFeatures");
        assert _p != null : "'validatorHandlerFeatures' property not found.";
        return _p;
    }

    /**
     * Gets the value of the {@code <validatorHandlerProperties>} property.
     * <p><dl>
     *   <dt><b>Final:</b></dt><dd>No</dd>
     * </dl></p>
     * @return See {@link javax.xml.validation.ValidatorHandler#setProperty(java.lang.String, java.lang.Object)}.
     * @throws org.jomc.ObjectManagementException if getting the property instance fails.
     */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    private java.util.Map<String, Object> getValidatorHandlerProperties() {
        final java.util.Map<String, Object> _p = (java.util.Map<String, Object>) org.jomc.ObjectManagerFactory.getObjectManager(this.getClass().getClassLoader()).getProperty(this, "validatorHandlerProperties");
        assert _p != null : "'validatorHandlerProperties' property not found.";
        return _p;
    }
}
