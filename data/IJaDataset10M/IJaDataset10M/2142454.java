package net.saml2j.saml20.util;

public interface IConsentIdentifier {

    String UNSPECIFIED = "urn:oasis:names:tc:SAML:2.0:consent:unspecified";

    String OBTAINED = "urn:oasis:names:tc:SAML:2.0:consent:obtained";

    String PRIOR = "urn:oasis:names:tc:SAML:2.0:consent:prior";

    String IMPLICIT = "urn:oasis:names:tc:SAML:2.0:consent:current-implicit";

    String EXPLICIT = "urn:oasis:names:tc:SAML:2.0:consent:current-explicit";

    String UNAVILABLE = "urn:oasis:names:tc:SAML:2.0:consent:unavailable";

    String INAPPLICABLE = "urn:oasis:names:tc:SAML:2.0:consent:inapplicable";
}
