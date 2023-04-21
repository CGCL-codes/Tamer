package templates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.mwe.core.resources.AbstractResourceLoader;
import org.eclipse.emf.mwe.core.resources.ResourceLoader;
import org.eclipse.emf.mwe.core.resources.ResourceLoaderFactory;
import org.eclipse.emf.mwe.internal.core.MWEPlugin;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import de.abg.jreichert.serviceqos.ServiceQosPackage;
import de.abg.jreichert.serviceqos.ServiceQosStandaloneSetup;
import de.abg.jreichert.serviceqos.WsQualitySpecification;
import de.abg.jreichert.serviceqos.model.wsdl.xtext.ServiceQosWsdlStandaloneSetup;

public class WsAgreementTest extends AbstractXmlGeneratTest {

    private static Document doc = null;

    @BeforeClass
    public static void init() throws Exception {
        String fileStr = "model/logistics.sqos";
        String generatorPluginName = "de.abg.jreichert.serviceqos.generator";
        WsQualitySpecification root = getModelRoot(fileStr);
        EList<WsQualitySpecification> roots = new BasicEList<WsQualitySpecification>();
        roots.add(root);
        replaceResourceLoader(generatorPluginName);
        String content = executeTemplateTest("WSAgreement.xpt", "wsAgreement", roots);
        doc = getAsXML(content);
    }

    @Test
    public void assertSdtCount() throws Exception {
        String xpathExpression = "//Terms/All/ServiceDescriptionTerm/@Name";
        String testDesc = "Existence of SDT with xpath expression " + xpathExpression;
        int expectedNodeCount = 8;
        int nodeIndex = 0;
        String expectedValue = "ServiceSV2";
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    @Test
    public void assertFunctionalSdtCorrectness() throws Exception {
        assertSdtExistence(doc, "ServiceSV2", "SV2", "service", "SV2");
        assertSdtExistence(doc, "InterfaceSV2__IF2", "SV2", "interface", "IF2");
        assertSdtExistence(doc, "OperationSV2__IF2__Op2", "SV2", "operation", "Op2");
        assertSdtExistence(doc, "EndpointSV2__EP2", "SV2", "endpoint", "EP2");
    }

    @Test
    public void assertNonFunctionalSdtCorrectness() throws Exception {
        assertSdtExistence(doc, "HttpsSupport_at_OperationSV2__IF2__Op2", "SV2", "HttpsSupport", "true", "OperationSV2__IF2__Op2");
        assertSdtExistence(doc, "AverageResponseTimeLastMonth_at_InterfaceSV2__IF2", "SV2", "AverageResponseTimeLastMonth", "20ms", "InterfaceSV2__IF2");
        assertSdtExistence(doc, "OnlyUsAreaSupported_at_OperationSV2__IF2__Op2", "SV2", "OnlyUsAreaSupported", "10ms", "OperationSV2__IF2__Op2");
        assertSdtExistence(doc, "DataLastUpdate_at_ServiceSV2", "SV2", "DataLastUpdate", "true", "ServiceSV2");
    }

    private void assertSdtExistence(Document doc, String sdtName, String serviceName, String componentType, String componentName) throws Exception {
        String xpathExpression = "//Terms/All/ServiceDescriptionTerm[@Name='" + sdtName + "' and @ServiceName='" + serviceName + "']/" + componentType + "/@name";
        String testDesc = "Existence of SDT with xpath expression " + xpathExpression;
        int expectedNodeCount = 1;
        int nodeIndex = 0;
        String expectedValue = componentName;
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    private void assertSdtExistence(Document doc, String sdtName, String serviceName, String nfpName, String nfpValue, String reference) throws Exception {
        String xpathExpression = "//Terms/All/ServiceDescriptionTerm[@Name='" + sdtName + "' and @ServiceName='" + serviceName + "']/nonfunctionalProperty[@name='" + nfpName + "' and @value='" + nfpValue + "']/serviceReference/@name";
        String testDesc = "Existence of SDT with xpath expression " + xpathExpression;
        int expectedNodeCount = 1;
        int nodeIndex = 0;
        String expectedValue = "//wsag:ServiceDescriptionTerm/@wsag:Name='" + reference + "'";
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    @Test
    public void assertServicePropertiesCorrectness() throws Exception {
        assertServicePropertiesCorrectness(doc, "PerformanceProperties", "SV2", "ResponseTime", "wsq:EndToEndResponseTime", "AverageResponseTimeLastMonth");
        assertServicePropertiesCorrectness(doc, "SecurityProperties", "SV2", "Confidentiality", "wsq:HttpsEndpointSupport", "HttpsSupport");
        assertServicePropertiesCorrectness(doc, "UpdatenessProperties", "SV2", "LastUpdate", "wsq:LastUpdateTimestamp", "DataLastUpdate");
        assertServicePropertiesCorrectness(doc, "InternationalityProperties", "SV2", "GermanySupport", "wsq:GermanyTargetAddressSupport", "OnlyUsAreaSupported");
    }

    @Test
    public void assertServicePropertiesCount() throws Exception {
        String xpathExpression = "//Terms/All/ServiceProperties/@Name";
        String testDesc = "Existence of ServiceProperty with xpath expression " + xpathExpression;
        int expectedNodeCount = 4;
        int nodeIndex = 0;
        String expectedValue = "PerformanceProperties";
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    private void assertServicePropertiesCorrectness(Document doc, String propName, String serviceName, String variable, String metric, String location) throws Exception {
        String xpathExpression = "//Terms/All/ServiceProperties[@Name='" + propName + "' and @ServiceName='" + serviceName + "']/Variables/Variable[@Name='" + variable + "' and @Metric='" + metric + "']/Location/text()";
        String testDesc = "Existence of ServiceProperty with xpath expression " + xpathExpression;
        int expectedNodeCount = 1;
        int nodeIndex = 0;
        String expectedValue = "//wsag:ServiceDescriptionTerm/@wsag:Name='" + location + "'";
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    @Test
    public void assertGuaranteeCorrectness() throws Exception {
        assertGuaranteeCorrectness(doc, "FastResponse", "SV2", "ResponseTime", "20ms", "lower");
        assertGuaranteeCorrectness(doc, "Encryption", "SV2", "Confidentiality", "true", "equal");
        assertGuaranteeCorrectness(doc, "RealTimeData", "SV2", "LastUpdate", "1d", "lower");
    }

    @Test
    public void assertGuaranteeCount() throws Exception {
        String xpathExpression = "//Terms/All/GuaranteeTerm/@Name";
        String testDesc = "Existence of guarantee with xpath expression " + xpathExpression;
        int expectedNodeCount = 3;
        int nodeIndex = 0;
        String expectedValue = "FastResponse";
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    private void assertGuaranteeCorrectness(Document doc, String guaranteeName, String serviceName, String qosParameter, String value, String predicate) throws Exception {
        String xpathExpression = "//Terms/All/GuaranteeTerm[@Name='" + guaranteeName + "' and ServiceScope/@ServiceName='" + serviceName + "']/ServiceLevelObjective/CustomServiceLevel/guarantee[@name='" + guaranteeName + "' and @qosParameter=\"//wsag:ServiceProperties/wsag:Variable/@wsag:Name='" + qosParameter + "'\" and @predicate=\"//wsq:predicateTypeCollection/predicate/@name='" + predicate + "'\"]/@value";
        String testDesc = "Existence of guarantee with xpath expression " + xpathExpression;
        int expectedNodeCount = 1;
        int nodeIndex = 0;
        String expectedValue = value;
        assertValue(doc, xpathExpression, testDesc, expectedNodeCount, nodeIndex, expectedValue);
    }

    private static WsQualitySpecification getModelRoot(String fileStr) throws IOException {
        ResourceSet rs = new ResourceSetImpl();
        ServiceQosStandaloneSetup.doSetup();
        ServiceQosWsdlStandaloneSetup.doSetup();
        rs.getPackageRegistry().put(ServiceQosPackage.eINSTANCE.getNsURI(), ServiceQosPackage.eINSTANCE);
        File file = new File(fileStr);
        Resource res = rs.createResource(URI.createFileURI(file.getAbsolutePath()));
        res.load(rs.getLoadOptions());
        return (WsQualitySpecification) res.getContents().get(0);
    }

    private static void replaceResourceLoader(final String generatorPluginName) {
        ResourceLoader loader = new AbstractResourceLoader() {

            @Override
            public InputStream getResourceAsStream(String uri) {
                File file = null;
                if (uri.endsWith(".ext")) {
                    file = new File("../" + generatorPluginName + "/src/" + uri);
                } else {
                    file = new File("../" + generatorPluginName + "/src/templates/" + uri);
                }
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return fis;
            }

            @Override
            protected Class<?> tryLoadClass(String clazzName) throws ClassNotFoundException {
                return MWEPlugin.loadClass(MWEPlugin.ID, clazzName);
            }
        };
        ResourceLoaderFactory.setCurrentThreadResourceLoader(loader);
    }
}
