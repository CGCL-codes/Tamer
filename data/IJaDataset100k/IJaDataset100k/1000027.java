package ar.com.fdvs.bean2bean.testbeans.performance;

import ar.com.fdvs.bean2bean.annotations.CopyFrom;
import ar.com.fdvs.bean2bean.interpreters.InterpreterType;

/**
 * 
 * @author D. Garcia
 */
public class Groovy_Nested2Nested_Mixed implements PerformanceTestBean {

    private String privateSource;

    @CopyFrom(value = "nested1.nested2.nested3.nested4.nested5.source", getterInterpreter = InterpreterType.GROOVY, setter = "_destino.nested1.nested2.nested3.nested4.nested5.destination = _valor", setterInterpreter = InterpreterType.GROOVY)
    private String privateDestination;

    public Groovy_Nested2Nested_Mixed nested1;

    private Groovy_Nested2Nested_Mixed privateProperty2;

    public Groovy_Nested2Nested_Mixed nested3;

    private Groovy_Nested2Nested_Mixed privateProperty4;

    public Groovy_Nested2Nested_Mixed nested5;

    public String getSource() {
        return privateSource;
    }

    public void setSource(String source) {
        this.privateSource = source;
    }

    public String getDestination() {
        return privateDestination;
    }

    public void setDestination(String destination) {
        this.privateDestination = destination;
    }

    public Groovy_Nested2Nested_Mixed getNested2() {
        return privateProperty2;
    }

    public void setNested2(Groovy_Nested2Nested_Mixed nested2) {
        this.privateProperty2 = nested2;
    }

    public Groovy_Nested2Nested_Mixed getNested4() {
        return privateProperty4;
    }

    public void setNested4(Groovy_Nested2Nested_Mixed nested4) {
        this.privateProperty4 = nested4;
    }

    public Groovy_Nested2Nested_Mixed() {
        this.nested1 = new Groovy_Nested2Nested_Mixed(false);
        this.nested1.privateProperty2 = new Groovy_Nested2Nested_Mixed(false);
        this.nested1.privateProperty2.nested3 = new Groovy_Nested2Nested_Mixed(false);
        this.nested1.privateProperty2.nested3.privateProperty4 = new Groovy_Nested2Nested_Mixed(false);
        this.nested1.privateProperty2.nested3.privateProperty4.nested5 = new Groovy_Nested2Nested_Mixed(false);
    }

    public Groovy_Nested2Nested_Mixed(boolean b) {
    }

    public void prepareValue(String string) {
        this.nested1 = new Groovy_Nested2Nested_Mixed();
        this.nested1.privateProperty2 = new Groovy_Nested2Nested_Mixed();
        this.nested1.privateProperty2.nested3 = new Groovy_Nested2Nested_Mixed();
        this.nested1.privateProperty2.nested3.privateProperty4 = new Groovy_Nested2Nested_Mixed();
        this.nested1.privateProperty2.nested3.privateProperty4.nested5 = new Groovy_Nested2Nested_Mixed();
        this.nested1.privateProperty2.nested3.privateProperty4.nested5.privateSource = string;
    }

    public String getSourceValue() {
        return this.nested1.privateProperty2.nested3.privateProperty4.nested5.privateSource;
    }

    public String getDestinationValue() {
        return this.nested1.privateProperty2.nested3.privateProperty4.nested5.privateDestination;
    }
}
