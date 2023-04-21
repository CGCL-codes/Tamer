package it.imolinfo.jbi4ejb.test.wsdlgeneration.interfaces;

public class ComplexException extends Exception {

    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
