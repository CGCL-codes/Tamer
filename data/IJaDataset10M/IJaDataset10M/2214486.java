package org.xmldap.saml;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import org.xmldap.exceptions.SerializationException;
import org.xmldap.util.RandomGUID;
import org.xmldap.util.XSDDateTime;
import org.xmldap.xml.Canonicalizable;
import org.xmldap.xml.XmlUtils;
import org.xmldap.xmldsig.AsymmetricKeyInfo;
import junit.framework.TestCase;

public class SAMLAssertionTest extends TestCase {

    X509Certificate cert = null;

    Calendar notBeforeCal = null;

    Calendar notOnOrAfterCal = null;

    Conditions conditions = null;

    String notBeforeString = "2006-09-27T12:58:26Z";

    String notOnOrAfterString = "2006-09-29T12:58:26Z";

    public void setUp() {
        notBeforeCal = XSDDateTime.parse(notBeforeString);
        notOnOrAfterCal = XSDDateTime.parse(notOnOrAfterString);
        conditions = new Conditions(notBeforeCal, notOnOrAfterCal);
        try {
            cert = org.xmldap.util.XmldapCertsAndKeys.getXmldapCert();
        } catch (CertificateException e) {
            assertTrue(false);
        }
    }

    public void testToXML() throws IOException {
        AsymmetricKeyInfo keyInfo = null;
        keyInfo = new AsymmetricKeyInfo(cert);
        Subject subject = new Subject(keyInfo, Subject.HOLDER_OF_KEY);
        Attribute given = new Attribute("givenname", "urn:namespace1", "Chuck");
        Attribute sur = new Attribute("surname", "urn:namespace2", "Mortimore");
        Attribute email = new Attribute("email", "urn:namespace3", "cmortspam@gmail.com");
        AttributeStatement statement = new AttributeStatement();
        statement.setSubject(subject);
        statement.addAttribute(given);
        statement.addAttribute(sur);
        statement.addAttribute(email);
        RandomGUID guidGen = new RandomGUID();
        SAMLAssertion assertion = new SAMLAssertion(guidGen);
        assertion.setConditions(conditions);
        assertion.setAttributeStatement(statement);
        assertion.setIssueInstant(XSDDateTime.parse("2007-03-12T12:19:01Z"));
        try {
            String expectedAsserion = "<saml:Assertion xmlns:saml=\"urn:oasis:names:tc:SAML:1.0:assertion\" AssertionID=\"uuid-" + guidGen.toString() + "\" IssueInstant=\"2007-03-12T12:19:01Z\" Issuer=\"http://schemas.xmlsoap.org/ws/2005/05/identity/issuer/self\" MajorVersion=\"1\" MinorVersion=\"1\"><saml:Conditions NotBefore=\"2006-09-27T12:58:26Z\" NotOnOrAfter=\"2006-09-29T12:58:26Z\"></saml:Conditions><saml:AttributeStatement><saml:Subject><saml:SubjectConfirmation><saml:ConfirmationMethod>urn:oasis:names:tc:SAML:1.0:cm:holder-of-key</saml:ConfirmationMethod><dsig:KeyInfo xmlns:dsig=\"http://www.w3.org/2000/09/xmldsig#\"><dsig:KeyName>Public Key for CN=xmldap.org, OU=infocard, O=xmldap, L=San Francisco, ST=California, C=US</dsig:KeyName><dsig:KeyValue><dsig:RSAKeyValue><dsig:Modulus>ANMnkVA4xfpG0bLos9FOpNBjHAdFahy2cJ7FUwuXd/IShnG+5qF/z1SdPWzRxTtpFFyodtXlBUEIbiT+IbYPZF1vCcBrcFa8Kz/4rBjrpPZgllgA/WSVKjnJvw8q4/tO6CQZSlRlj/ebNK9VyT1kN+MrKV1SGTqaIJ2l+7Rd05WHscwZMPdVWBbRrg76YTfy6H/NlQIArNLZanPvE0Vd5QfD4ZyG2hTh3y7ZlJAUndGJ/kfZw8sKuL9QSrh4eOTc280NQUmPGz6LP5MXNmu0RxEcomod1+ToKll90yEKFAUKuPYFgm9J+vYm4tzRequLy/njteRIkcfAdcAtt6PCYjU=</dsig:Modulus><dsig:Exponent>AQAB</dsig:Exponent></dsig:RSAKeyValue></dsig:KeyValue><dsig:X509Data><dsig:X509Certificate>MIIDXTCCAkUCBEQd+4EwDQYJKoZIhvcNAQEEBQAwczELMAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBGcmFuY2lzY28xDzANBgNVBAoTBnhtbGRhcDERMA8GA1UECxMIaW5mb2NhcmQxEzARBgNVBAMTCnhtbGRhcC5vcmcwHhcNMDYwMzIwMDA0NjU3WhcNMDYwNjE4MDA0NjU3WjBzMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZyYW5jaXNjbzEPMA0GA1UEChMGeG1sZGFwMREwDwYDVQQLEwhpbmZvY2FyZDETMBEGA1UEAxMKeG1sZGFwLm9yZzCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANMnkVA4xfpG0bLos9FOpNBjHAdFahy2cJ7FUwuXd/IShnG+5qF/z1SdPWzRxTtpFFyodtXlBUEIbiT+IbYPZF1vCcBrcFa8Kz/4rBjrpPZgllgA/WSVKjnJvw8q4/tO6CQZSlRlj/ebNK9VyT1kN+MrKV1SGTqaIJ2l+7Rd05WHscwZMPdVWBbRrg76YTfy6H/NlQIArNLZanPvE0Vd5QfD4ZyG2hTh3y7ZlJAUndGJ/kfZw8sKuL9QSrh4eOTc280NQUmPGz6LP5MXNmu0RxEcomod1+ToKll90yEKFAUKuPYFgm9J+vYm4tzRequLy/njteRIkcfAdcAtt6PCYjUCAwEAATANBgkqhkiG9w0BAQQFAAOCAQEAURtxiA7qDSq/WlUpWpfWiZ7HvveQrwTaTwV/Fk3l/I9e9WIRN51uFLuiLtZMMwR02BX7Yva1KQ/Gl999cm/0b5hptJ+TU29rVPZIlI32c5vjcuSVoEda8+BRj547jlC0rNokyWm+YtBcDOwfHSPFFwVPPVxyQsVEebsiB6KazFq6iZ8A0F2HLEnpsdFnGrSwBBbH3I3PH65ofrTTgj1Mjk5kA6EVaeefDCtlkX2ogIFMlcS6ruihX2mlCLUSrlPs9TH+M4j/R/LV5QWJ93/X9gsxFrxVFGg3b75EKQP8MZ111/jaeKd80mUOAiTO06EtfjXZPrjPN4e2l05i2EGDUA==</dsig:X509Certificate></dsig:X509Data></dsig:KeyInfo></saml:SubjectConfirmation></saml:Subject><saml:Attribute AttributeName=\"givenname\" AttributeNamespace=\"urn:namespace1\"><saml:AttributeValue>Chuck</saml:AttributeValue></saml:Attribute><saml:Attribute AttributeName=\"surname\" AttributeNamespace=\"urn:namespace2\"><saml:AttributeValue>Mortimore</saml:AttributeValue></saml:Attribute><saml:Attribute AttributeName=\"email\" AttributeNamespace=\"urn:namespace3\"><saml:AttributeValue>cmortspam@gmail.com</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>";
            byte[] canonicalAssertionBytes = XmlUtils.canonicalize(assertion.serialize(), Canonicalizable.EXCLUSIVE_CANONICAL_XML);
            assertEquals(expectedAsserion, new String(canonicalAssertionBytes));
        } catch (SerializationException e) {
            assertTrue(false);
        }
    }
}
