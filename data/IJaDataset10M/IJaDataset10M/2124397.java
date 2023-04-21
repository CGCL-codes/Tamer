package fi.hip.gb.gateway.util.xml;

import fi.hip.gb.gateway.util.Util;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.log4j.Logger;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.ManageNameIDRequest;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.SubjectBuilder;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * Utility class for making SAML2.0 elements.
 * 
 * @author Henri Mikkonen <henri.mikkonen@hip.fi>
 *
 */
public class SAML20Util extends XMLUtil {

    private Logger logger = Logger.getLogger(SAML20Util.class.getName());

    /**
	 * Gets an AuthnRequest element from a string
	 * 
	 * @param samlRequestStr string containing the element
	 * @return the element
	 * @throws UnsupportedEncodingException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 */
    public static AuthnRequest getAuthnRequestFromString(String samlRequestStr) throws UnsupportedEncodingException, XMLParserException, UnmarshallingException {
        XMLUtil xmlUtil = new XMLUtil();
        return (AuthnRequest) xmlUtil.getXMLObjectFromString(samlRequestStr);
    }

    /**
	 * Gets a LogoutRequest element from a string.
	 * 
	 * @param logoutRequestStr string containing the element
	 * @return the element
	 * @throws UnsupportedEncodingException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 */
    public static LogoutRequest getLogoutRequestFromString(String logoutRequestStr) throws UnsupportedEncodingException, XMLParserException, UnmarshallingException {
        XMLUtil xmlUtil = new XMLUtil();
        return (LogoutRequest) xmlUtil.getXMLObjectFromString(logoutRequestStr);
    }

    /**
	 * Gets a ManageNameIDRequest element from a string.
	 * 
	 * @param nameIdRequestStr string containing the element
	 * @return the element
	 * @throws UnsupportedEncodingException
	 * @throws XMLParserException
	 * @throws UnmarshallingException
	 */
    public static ManageNameIDRequest getManageNameIDRequestFromString(String nameIdRequestStr) throws UnsupportedEncodingException, XMLParserException, UnmarshallingException {
        XMLUtil xmlUtil = new XMLUtil();
        return (ManageNameIDRequest) xmlUtil.getXMLObjectFromString(nameIdRequestStr);
    }

    /**
	 * Builds a new Assertion element.
	 * @return new Assertion element
	 */
    public static Assertion makeNewAssertion() {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        AssertionBuilder builder = (AssertionBuilder) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        return builder.buildObject();
    }

    /**
	 * Builds a new Issuer element containing the given name string as its value.
	 * 
	 * @param name body text of the Issuer element
	 * @return new Issuer element
	 */
    public static Issuer makeEntityIssuer(String name) {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        IssuerBuilder builder = (IssuerBuilder) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
        Issuer issuer = builder.buildObject();
        issuer.setFormat(Issuer.ENTITY);
        issuer.setValue(name);
        return issuer;
    }

    /**
	 * Builds a new Status element with the given status code string
	 * 
	 * @param statusCodeStr status code string
	 * @return new Status element
	 */
    public static Status makeStatus(String statusCodeStr) {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        StatusCodeBuilder scBuilder = (StatusCodeBuilder) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
        StatusCode statusCode = scBuilder.buildObject();
        statusCode.setValue(statusCodeStr);
        StatusBuilder sBuilder = (StatusBuilder) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
        Status status = sBuilder.buildObject();
        status.setStatusCode(statusCode);
        return status;
    }

    /**
	 * Builds a new Response element skeleton containing InResponseTo, ID and Destination attributes.
	 * 
	 * @param originalId Original identifier which will be the value of the InResponseTo attribute
	 * @param recipient value of the Destination attribute
	 * @return new Response element
	 */
    public static Response makeResponseSkeleton(String originalId, String recipient) {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        ResponseBuilder builder = (ResponseBuilder) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
        Response response = builder.buildObject();
        response.setInResponseTo(originalId);
        response.setID(Util.generateIdentifier());
        response.setDestination(recipient);
        return response;
    }

    /**
	 * Builds a new AttributeValue element with given string as the body text.
	 * 
	 * @param attrValue the body text string
	 * @return new AttributeValue element
	 */
    public static XSString makeNewAttributeValue(String attrValue) {
        XSStringBuilder stringBuilder = (XSStringBuilder) org.opensaml.Configuration.getBuilderFactory().getBuilder(XSString.TYPE_NAME);
        XSString stringValue = stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
        stringValue.setValue(attrValue);
        return stringValue;
    }

    /**
	 * Builds a new AttributeStatement element containing the given Attributes
	 * 
	 * @param attrs Attributes to include in the AttributeStatement
	 * @return new AttributeStatement
	 */
    public static AttributeStatement makeNewAttrStatement(List<Attribute> attrs) {
        XMLObjectBuilderFactory builderFactory = org.opensaml.Configuration.getBuilderFactory();
        AttributeStatementBuilder builder = (AttributeStatementBuilder) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
        AttributeStatement attrStatement = builder.buildObject();
        attrStatement.getAttributes().addAll(attrs);
        return attrStatement;
    }
}
