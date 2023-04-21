package org.orbeon.oxf.xml;

import org.orbeon.oxf.common.OXFException;
import org.orbeon.oxf.portlet.processor.PortletContainerProcessor;
import org.orbeon.oxf.portlet.processor.PortletIncludeGenerator;
import org.orbeon.oxf.processor.*;
import org.orbeon.oxf.processor.scope.ScopeProcessorBase;
import org.orbeon.oxf.processor.test.TestProcessor;
import org.orbeon.oxf.processor.scratchpad.PDFTemplateProcessor;
import org.orbeon.oxf.processor.converter.JFreeChartConverter;
import org.orbeon.oxf.processor.converter.XMLConverter;
import org.orbeon.oxf.processor.converter.XSLFOConverter;
import org.orbeon.oxf.processor.converter.ToXLSConverter;
import org.orbeon.oxf.processor.generator.*;
import org.orbeon.oxf.processor.pipeline.AggregatorProcessor;
import org.orbeon.oxf.processor.pipeline.PipelineProcessor;
import org.orbeon.oxf.processor.serializer.CachedSerializer;
import org.orbeon.oxf.processor.serializer.FileSerializer;
import org.orbeon.oxf.processor.serializer.HttpSerializer;
import org.orbeon.oxf.processor.serializer.legacy.JFreeChartSerializer;
import org.orbeon.oxf.processor.sql.SQLProcessor;
import org.orbeon.oxf.processor.tamino.TaminoProcessor;
import org.orbeon.oxf.processor.tamino.TaminoQueryProcessor;
import org.orbeon.oxf.processor.transformer.TraxTransformer;
import org.orbeon.oxf.processor.transformer.xslt.XSLT1Transformer;
import org.orbeon.oxf.processor.transformer.xslt.XSLTTransformer;
import org.orbeon.oxf.processor.validation.MSVValidationProcessor;
import org.orbeon.oxf.processor.xforms.Constants;
import org.orbeon.oxf.processor.xmldb.XMLDBProcessor;
import org.orbeon.oxf.resources.OXFProperties;
import org.orbeon.oxf.util.PipelineUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class SchemaRepository {

    private static final String SCHEMA_PREFIX = "oxf.schemas.";

    private static final String GLOBAL_SCHEMA_PATH = "/org/orbeon/oxf/xml/";

    private static final String PROCESSORS_SCHEMA_PATH = "/org/orbeon/oxf/processor/";

    private static final HashMap schemas = new HashMap();

    private static SchemaRepository instance;

    static {
        schemas.put(PipelineProcessor.PIPELINE_NAMESPACE_URI, "schemas/pipeline.rng");
        schemas.put(AggregatorProcessor.AGGREGATOR_NAMESPACE_URI, "schemas/aggregator.rng");
        schemas.put(DelegationProcessor.DELEGATION_NAMESPACE_URI, "schemas/delegation.rng");
        schemas.put(JavaProcessor.JAVA_CONFIG_NAMESPACE_URI, "schemas/java.rng");
        schemas.put(URLGenerator.URL_NAMESPACE_URI, "schemas/url-generator-config.rng");
        schemas.put(RedirectProcessor.REDIRECT_SCHEMA_URI, "schemas/redirect.rng");
        schemas.put(RequestGenerator.REQUEST_CONFIG_NAMESPACE_URI, "schemas/request-config.rng");
        schemas.put(ImageServer.IMAGE_SERVER_CONFIG_NAMESPACE_URI, "schemas/image-server-config.rng");
        schemas.put(ImageServer.IMAGE_SERVER_IMAGE_NAMESPACE_URI, "schemas/image-server-image.rng");
        schemas.put(OXFPropertiesSerializer.PROPERTIES_SCHEMA_URI, "schemas/properties.rng");
        schemas.put(RequestSecurityGenerator.REQUEST_SECURITY_NAMESPACE_URI, "schemas/request-security.rng");
        schemas.put(SignatureVerifierProcessor.SIGNATURE_DATA_URI, "schemas/signature.rng");
        schemas.put(SignatureVerifierProcessor.SIGNATURE_PUBLIC_KEY_URI, "schemas/public-key.rng");
        schemas.put(XMLProcessorRegistry.PROCESSOR_REGISTRY_CONFIG_NAMESPACE_URI, "schemas/processor-registry.rng");
        schemas.put(CachedSerializer.SERIALIZER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "serializer/legacy-serializer-config.rng");
        schemas.put(HttpSerializer.HTTP_SERIALIZER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "serializer/http-serializer-config.rng");
        schemas.put(FileSerializer.FILE_SERIALIZER_CONFIG_NAMESPACE_URI, "schemas/file-serializer-config.rng");
        schemas.put(XMLConverter.STANDARD_TEXT_CONVERTER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "converter/standard-text-converter-config.rng");
        schemas.put(JFreeChartSerializer.CHART_CONVERTER_CHART_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "converter/chart-converter-chart.rng");
        schemas.put(JFreeChartConverter.CHART_CONVERTER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "converter/chart-converter-config.rng");
        schemas.put(XSLFOConverter.XSLFO_CONVERTER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "converter/xslfo-converter-config.rng");
        schemas.put(ToXLSConverter.TO_XLS_CONVERTER_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "converter/to-xls-converter-config.rng");
        schemas.put(ScopeProcessorBase.SCOPE_CONFIG_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "scope/scope-config.rng");
        schemas.put(EmailProcessor.EMAIL_CONFIG_NAMESPACE_URI, "schemas/email.rng");
        schemas.put(BeanGenerator.BEAN_CONFIG_NAMESPACE_URI, "schemas/bean-generator-config.rng");
        schemas.put(ResourceServer.RESOURCE_SERVER_NAMESPACE_URI, "schemas/resource-server.rng");
        schemas.put(ResourceServer.MIMETYPES_NAMESPACE_URI, "schemas/mime-types.rng");
        schemas.put(LDAPProcessor.LDAP_CONFIG_NAMESPACE_URI, "schemas/ldap-config.rng");
        schemas.put(LDAPProcessor.LDAP_FILTER_NAMESPACE_URI, "schemas/ldap-filter.rng");
        schemas.put(SchedulerProcessor.SCHEDULER_CONFIG_NAMESPACE_URI, "schemas/scheduler-config.rng");
        schemas.put(ServletIncludeGenerator.SERVLET_INCLUDE_NAMESPACE_URI, "schemas/servlet-include-config.rng");
        schemas.put(PortletIncludeGenerator.PORTLET_INCLUDE_NAMESPACE_URI, "schemas/portlet-include-config.rng");
        schemas.put(PortletContainerProcessor.PORTLET_CONTAINER_NAMESPACE_URI, "schemas/portlet-container-config.rng");
        schemas.put(XSLTTransformer.XSLT_TRANSFORMER_CONFIG_NAMESPACE_URI, "schemas/xslt-transformer-config.rng");
        schemas.put(TraxTransformer.TRAX_TRANSFORMER_CONFIG_NAMESPACE_URI, "schemas/trax-transformer-config.rng");
        schemas.put(PageFlowControllerProcessor.CONTROLLER_NAMESPACE_URI, "schemas/page-flow-controller-runtime.xsd");
        schemas.put(MSVValidationProcessor.ORBEON_ERROR_NS, "schemas/validation-config.rng");
        schemas.put(DirectoryGenerator.DIRECTORY_GENERATOR_NAMESPACE_URI, "schemas/directory-generator-config.rng");
        schemas.put(SQLProcessor.SQL_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "sql/sql-processor-config.rng");
        schemas.put(SQLProcessor.SQL_DATASOURCE_URI, PROCESSORS_SCHEMA_PATH + "sql/sql-processor-datasource.rng");
        schemas.put(TaminoProcessor.TAMINO_CONFIG_URI, PROCESSORS_SCHEMA_PATH + "tamino/tamino-config.rng");
        schemas.put(TaminoQueryProcessor.TAMINO_QUERY_URI, PROCESSORS_SCHEMA_PATH + "tamino/tamino-query.rng");
        schemas.put(XMLDBProcessor.XMLDB_DATASOURCE_URI, PROCESSORS_SCHEMA_PATH + "xmldb/xmldb-processor-datasource.rng");
        schemas.put(XMLDBProcessor.XMLDB_QUERY_URI, PROCESSORS_SCHEMA_PATH + "xmldb/xmldb-processor-query.rng");
        schemas.put(TestProcessor.TEST_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "test/test-processor-config.rng");
        schemas.put(PDFTemplateProcessor.PDF_TEMPLATE_MODEL_NAMESPACE_URI, PROCESSORS_SCHEMA_PATH + "scratchpad/pdf-template-model.rng");
        schemas.put(Constants.XFORMS_NAMESPACE_URI + "/controls", "schemas/xforms-controls.rng");
        schemas.put(Constants.XFORMS_NAMESPACE_URI + "/model", "schemas/xforms-1_0.xsd");
        schemas.put("http://relaxng.org/ns/structure/1.0", "schemas/relaxng.rng");
        schemas.put("http://www.w3.org/2001/xml-events", "schemas/XML-Events-Schema.xsd");
        schemas.put("http://www.w3.org/2001/XMLSchema", "schemas/XMLSchema.xsd");
        schemas.put("http://www.w3.org/XML/1998/namespace", "schemas/xml.xsd");
        schemas.put(XSLT1Transformer.XSLT_1_0_PSEUDO_URI, "schemas/xslt-1_0.rng");
        schemas.put(XSLTTransformer.XSLT_URI, "schemas/xslt-2_0.xsd");
    }

    private SchemaRepository() {
    }

    public static SchemaRepository instance() {
        if (instance == null) instance = new SchemaRepository();
        return instance;
    }

    public Processor getResourceGenerator(String publicId) {
        return PipelineUtils.createURLGenerator(getSchema(publicId));
    }

    /**
     * Get a systemId by looking in the properties, then in the builtin Presentation Server
     * schemas. This is used only for user-defined validation.
     */
    public String getSchema(String publicId) {
        Set keys = OXFProperties.instance().keySet();
        if (keys != null) {
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                if (key.equals(SCHEMA_PREFIX + publicId)) {
                    return OXFProperties.instance().getPropertySet().getString(key);
                }
            }
        }
        String schemaFile = (String) schemas.get(publicId);
        if (schemaFile != null) {
            if (schemaFile.startsWith("/")) return "oxf:" + schemaFile; else return "oxf:" + GLOBAL_SCHEMA_PATH + schemaFile;
        }
        throw new OXFException("Can't load schema for URI: " + publicId);
    }
}
