package org.icenigrid.gridsam.client.api;

public class UnknownJobFaultMessage extends Exception {

    private javax.xml.soap.SOAPElement parameters;

    public UnknownJobFaultMessage(javax.xml.soap.SOAPElement parameters) {
        this.parameters = parameters;
    }

    public javax.xml.soap.SOAPElement getParameters() {
        return parameters;
    }
}
