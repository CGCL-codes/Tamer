package ru.itbrains.jicard.wsdl;

import ru.itbrains.jicard.wspolicy.PolicyReference;

public class Operation {

    private String name;

    private Input input;

    private Output output;

    private ru.itbrains.jicard.soap12.Operation soapOperation;

    private PolicyReference policyRef;

    public Operation() {
    }

    public Operation(String name) {
        this.name = name;
    }

    public PolicyReference getPolicyRef() {
        return policyRef;
    }

    public void setPolicyRef(PolicyReference policyRef) {
        this.policyRef = policyRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }

    public ru.itbrains.jicard.soap12.Operation getSoapOperation() {
        return this.soapOperation;
    }

    public void setSoapOperation(ru.itbrains.jicard.soap12.Operation soapOperation) {
        this.soapOperation = soapOperation;
    }

    public String toString() {
        return "Operation{" + "name='" + name + '\'' + ", input=" + input + ", output=" + output + ", soapOperation=" + soapOperation + ", policyRef=" + policyRef + '}';
    }
}
