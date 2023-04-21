package org.jomc.sdk;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import org.jomc.model.Implementation;
import org.jomc.model.Implementations;
import org.jomc.model.Module;
import org.jomc.model.Modules;
import org.jomc.model.Properties;
import org.jomc.model.SpecificationReference;
import org.jomc.model.Specifications;
import org.jomc.model.Text;
import org.jomc.model.Texts;
import org.jomc.sdk.xml.ContextImplementation;
import org.jomc.sdk.xml.EntityResolverImplementation;
import org.jomc.sdk.xml.LSResourceResolverImplementation;
import org.jomc.sdk.xml.MarshallerImplementation;
import org.jomc.sdk.xml.SchemaImplementation;
import org.jomc.sdk.xml.UnmarshallerImplementation;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.EntityResolver;

@javax.annotation.Generated(value = "org.jomc.tools.JavaSources", comments = "See http://jomc.sourceforge.net/jomc/1.0-alpha-9/jomc-tools")
public class ModelProvider implements org.jomc.model.ModelProvider {

    /** Constant for the identifier of the SDK XML Model Specification. */
    private static final String XML_MODEL_SPECIFICATION = "org.jomc.sdk.xml.Model";

    public Modules getModules(final ClassLoader classLoader, final Modules modules) throws IOException {
        final Modules sdkModules = new Modules();
        sdkModules.setDocumentation(this.getTexts("modelProviderInfo"));
        final Implementations xmlImplementations = modules.getImplementations(XML_MODEL_SPECIFICATION);
        if (xmlImplementations != null) {
            final Module m = new Module();
            final Implementations impls = new Implementations();
            sdkModules.getModule().add(m);
            m.setName(this.getMessage("xmlModelsModuleName"));
            m.setVendor(this.getMessage("vendor"));
            m.setVersion(this.getMessage("version"));
            m.setImplementations(impls);
            m.setDocumentation(this.getTexts("modelProviderInfo"));
            impls.setDocumentation(this.getTexts("modelProviderInfo"));
            for (Implementation i : xmlImplementations.getImplementation()) {
                impls.getImplementation().add(this.createXmlImplementation(i, JAXBContext.class, ContextImplementation.class));
                impls.getImplementation().add(this.createXmlImplementation(i, Marshaller.class, MarshallerImplementation.class));
                impls.getImplementation().add(this.createXmlImplementation(i, Unmarshaller.class, UnmarshallerImplementation.class));
                impls.getImplementation().add(this.createXmlImplementation(i, Schema.class, SchemaImplementation.class));
                impls.getImplementation().add(this.createXmlImplementation(i, EntityResolver.class, EntityResolverImplementation.class));
                impls.getImplementation().add(this.createXmlImplementation(i, LSResourceResolver.class, LSResourceResolverImplementation.class));
            }
        }
        return sdkModules;
    }

    private Implementation createXmlImplementation(final Implementation implementation, final Class specification, final Class factory) {
        final Implementation i = new Implementation();
        final Specifications specs = new Specifications();
        final SpecificationReference ref = new SpecificationReference();
        i.setIdentifier(implementation.getIdentifier() + " " + specification.getSimpleName());
        i.setName(implementation.getName());
        i.setDocumentation(this.getTexts("modelProviderInfo"));
        i.setClazz(factory.getName());
        i.setFinal(Boolean.TRUE);
        i.setStateless(Boolean.TRUE);
        i.setVendor(this.getMessage("vendor"));
        i.setVersion(this.getMessage("version"));
        i.setSpecifications(specs);
        i.setDocumentation(this.getTexts("xmlImplementationDocumentation", specification.getName(), XML_MODEL_SPECIFICATION, implementation.getIdentifier()));
        ref.setDocumentation(this.getTexts("modelProviderInfo"));
        specs.setDocumentation(this.getTexts("modelProviderInfo"));
        i.getSpecifications().getReference().add(ref);
        ref.setFinal(Boolean.TRUE);
        ref.setIdentifier(specification.getName());
        if (implementation.getProperties() != null && implementation.getProperties().getProperty("schemas") != null) {
            final Properties properties = new Properties();
            i.setProperties(properties);
            properties.setDocumentation(this.getTexts("modelProviderInfo"));
            properties.getProperty().add(implementation.getProperties().getProperty("schemas"));
        }
        return i;
    }

    private Texts getTexts(final String key, final Object... arguments) {
        final Texts texts = new Texts();
        final Text text = new Text();
        texts.getText().add(text);
        texts.setDefaultLanguage(Locale.getDefault().getLanguage());
        text.setLanguage(texts.getDefaultLanguage());
        text.setValue(this.getMessage(key, arguments));
        return texts;
    }

    private String getMessage(final String key, final Object... arguments) {
        return MessageFormat.format(ResourceBundle.getBundle(ModelProvider.class.getName().replace('.', '/')).getString(key), arguments);
    }

    /** Creates a new {@code ModelProvider} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.JavaSources", comments = "See http://jomc.sourceforge.net/jomc/1.0-alpha-9/jomc-tools")
    public ModelProvider() {
        super();
    }
}
