package ar.com.fdvs.bean2bean.testbeans.performance;

import ar.com.fdvs.bean2bean.annotations.CopyFrom;
import ar.com.fdvs.bean2bean.interpreters.InterpreterType;

/**
 * 
 * @author D. Garcia
 */
public class Groovy_NoN2Nested_Calls implements PerformanceTestBean {

    private String privateSource;

    @CopyFrom(value = "getSource()", getterInterpreter = InterpreterType.GROOVY, setter = "_destino.getNested1().getNested2().getNested3().getNested4().getNested5().setDestination(_valor)", setterInterpreter = InterpreterType.GROOVY)
    private String privateDestination;

    private Groovy_NoN2Nested_Calls privateNested1;

    private Groovy_NoN2Nested_Calls privateNested2;

    private Groovy_NoN2Nested_Calls privateNested3;

    private Groovy_NoN2Nested_Calls privateNested4;

    private Groovy_NoN2Nested_Calls privateNested5;

    public Groovy_NoN2Nested_Calls() {
        this.privateNested1 = new Groovy_NoN2Nested_Calls(false);
        this.privateNested1.privateNested2 = new Groovy_NoN2Nested_Calls(false);
        this.privateNested1.privateNested2.privateNested3 = new Groovy_NoN2Nested_Calls(false);
        this.privateNested1.privateNested2.privateNested3.privateNested4 = new Groovy_NoN2Nested_Calls(false);
        this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5 = new Groovy_NoN2Nested_Calls(false);
    }

    public Groovy_NoN2Nested_Calls(boolean b) {
    }

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

    public Groovy_NoN2Nested_Calls getNested1() {
        return privateNested1;
    }

    public void setNested1(Groovy_NoN2Nested_Calls nested1) {
        this.privateNested1 = nested1;
    }

    public Groovy_NoN2Nested_Calls getNested2() {
        return privateNested2;
    }

    public void setNested2(Groovy_NoN2Nested_Calls nested2) {
        this.privateNested2 = nested2;
    }

    public Groovy_NoN2Nested_Calls getNested3() {
        return privateNested3;
    }

    public void setNested3(Groovy_NoN2Nested_Calls nested3) {
        this.privateNested3 = nested3;
    }

    public Groovy_NoN2Nested_Calls getNested4() {
        return privateNested4;
    }

    public void setNested4(Groovy_NoN2Nested_Calls nested4) {
        this.privateNested4 = nested4;
    }

    public Groovy_NoN2Nested_Calls getNested5() {
        return privateNested5;
    }

    public void setNested5(Groovy_NoN2Nested_Calls nested5) {
        this.privateNested5 = nested5;
    }

    public void prepareValue(String string) {
        this.privateSource = string;
    }

    public String getSourceValue() {
        return privateSource;
    }

    public String getDestinationValue() {
        return this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5.privateDestination;
    }
}
