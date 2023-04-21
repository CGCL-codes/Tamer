package org.apache.wsdl4reg;

public class OrganizationRegistrationException extends WSDL4RegException {

    public OrganizationRegistrationException() {
        super();
    }

    public OrganizationRegistrationException(Exception organizationRegistrationException) {
        super(organizationRegistrationException);
    }

    public OrganizationRegistrationException(String s) {
        super(s);
    }

    public OrganizationRegistrationException(String s, Exception organizationRegistrationException) {
        super(s, organizationRegistrationException);
    }
}
