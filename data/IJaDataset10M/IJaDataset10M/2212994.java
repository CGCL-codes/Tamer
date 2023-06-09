package org.apache.axis2.jaxws.description.feature;

import junit.framework.TestCase;
import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.jaxws.description.DescriptionFactory;
import org.apache.axis2.jaxws.description.EndpointDescription;
import org.apache.axis2.jaxws.description.ServiceDescription;
import org.apache.axis2.util.Utils;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.Addressing;

public class AddressingFeatureTests extends TestCase {

    private static final String ns = "http://jaxws.axis2.apache.org/metadata/feature/addressing";

    private static final String defaultServicePortName = "DefaultServicePort";

    private static final String plainServicePortName = "PlainServicePort";

    private static final String disabledServicePortName = "DisabledServicePort";

    private static final String requiredServicePortName = "RequiredServicePort";

    public void testNoAnnotation() {
        ServiceDescription sd = DescriptionFactory.createServiceDescription(DefaultService.class);
        EndpointDescription ed = sd.getEndpointDescription(new QName(ns, defaultServicePortName));
        assertNotNull(ed);
        AxisService axisService = ed.getAxisService();
        Parameter versionParam = axisService.getParameter(AddressingConstants.WS_ADDRESSING_VERSION);
        Parameter disabledParam = axisService.getParameter(AddressingConstants.DISABLE_ADDRESSING_FOR_IN_MESSAGES);
        Parameter requiredParam = axisService.getParameter(AddressingConstants.ADDRESSING_REQUIREMENT_PARAMETER);
        assertNull(versionParam);
        assertNull(disabledParam);
        assertNull(requiredParam);
    }

    public void testPlainAnnotation() {
        ServiceDescription sd = DescriptionFactory.createServiceDescription(PlainService.class);
        EndpointDescription ed = sd.getEndpointDescription(new QName(ns, plainServicePortName));
        assertNotNull(ed);
        AxisService axisService = ed.getAxisService();
        Parameter versionParam = axisService.getParameter(AddressingConstants.WS_ADDRESSING_VERSION);
        Parameter disabledParam = axisService.getParameter(AddressingConstants.DISABLE_ADDRESSING_FOR_IN_MESSAGES);
        Parameter requiredParam = axisService.getParameter(AddressingConstants.ADDRESSING_REQUIREMENT_PARAMETER);
        String version = Utils.getParameterValue(versionParam);
        String disabled = Utils.getParameterValue(disabledParam);
        String required = Utils.getParameterValue(requiredParam);
        assertEquals(AddressingConstants.Final.WSA_NAMESPACE, version);
        assertEquals("false", disabled);
        assertEquals(AddressingConstants.ADDRESSING_UNSPECIFIED, required);
    }

    public void testDisabled() {
        ServiceDescription sd = DescriptionFactory.createServiceDescription(DisabledService.class);
        EndpointDescription ed = sd.getEndpointDescription(new QName(ns, disabledServicePortName));
        assertNotNull(ed);
        AxisService axisService = ed.getAxisService();
        Parameter versionParam = axisService.getParameter(AddressingConstants.WS_ADDRESSING_VERSION);
        Parameter disabledParam = axisService.getParameter(AddressingConstants.DISABLE_ADDRESSING_FOR_IN_MESSAGES);
        Parameter requiredParam = axisService.getParameter(AddressingConstants.ADDRESSING_REQUIREMENT_PARAMETER);
        String version = Utils.getParameterValue(versionParam);
        String disabled = Utils.getParameterValue(disabledParam);
        String required = Utils.getParameterValue(requiredParam);
        assertEquals(AddressingConstants.Submission.WSA_NAMESPACE, version);
        assertEquals("false", disabled);
        assertEquals(AddressingConstants.ADDRESSING_UNSPECIFIED, required);
    }

    public void testRequired() {
        ServiceDescription sd = DescriptionFactory.createServiceDescription(RequiredService.class);
        EndpointDescription ed = sd.getEndpointDescription(new QName(ns, requiredServicePortName));
        assertNotNull(ed);
        AxisService axisService = ed.getAxisService();
        Parameter versionParam = axisService.getParameter(AddressingConstants.WS_ADDRESSING_VERSION);
        Parameter disabledParam = axisService.getParameter(AddressingConstants.DISABLE_ADDRESSING_FOR_IN_MESSAGES);
        Parameter requiredParam = axisService.getParameter(AddressingConstants.ADDRESSING_REQUIREMENT_PARAMETER);
        String version = Utils.getParameterValue(versionParam);
        String disabled = Utils.getParameterValue(disabledParam);
        String required = Utils.getParameterValue(requiredParam);
        assertEquals(AddressingConstants.Final.WSA_NAMESPACE, version);
        assertEquals("false", disabled);
        assertEquals(AddressingConstants.ADDRESSING_REQUIRED, required);
    }

    @WebService(targetNamespace = ns, portName = defaultServicePortName)
    class DefaultService {

        public double getQuote(String symbol) {
            return 101.01;
        }
    }

    @WebService(targetNamespace = ns, portName = plainServicePortName)
    @Addressing
    class PlainService {

        public double getQuote(String symbol) {
            return 101.01;
        }
    }

    @WebService(targetNamespace = ns, portName = disabledServicePortName)
    @Addressing(enabled = false)
    class DisabledService {

        public double getQuote(String symbol) {
            return 101.01;
        }
    }

    @WebService(targetNamespace = ns, portName = requiredServicePortName)
    @Addressing(required = true)
    class RequiredService {

        public double getQuote(String symbol) {
            return 101.01;
        }
    }
}
