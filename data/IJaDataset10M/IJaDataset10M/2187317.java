package ar.com.fdvs.bean2bean.testbeans.performance;

import ar.com.fdvs.bean2bean.annotations.CopyFrom;
import ar.com.fdvs.bean2bean.interpreters.InterpreterType;

/**
 * 
 * @author D. Garcia
 */
public class Ognl_NoN2Nested_Property implements PerformanceTestBean {

    private String privateSource;

    @CopyFrom(value = "source", getterInterpreter = InterpreterType.OGNL, setter = "nested1.nested2.nested3.nested4.nested5.destination", setterInterpreter = InterpreterType.OGNL)
    private String privateDestination;

    private Ognl_NoN2Nested_Property privateNested1;

    private Ognl_NoN2Nested_Property privateNested2;

    private Ognl_NoN2Nested_Property privateNested3;

    private Ognl_NoN2Nested_Property privateNested4;

    private Ognl_NoN2Nested_Property privateNested5;

    public Ognl_NoN2Nested_Property() {
        this.privateNested1 = new Ognl_NoN2Nested_Property(false);
        this.privateNested1.privateNested2 = new Ognl_NoN2Nested_Property(false);
        this.privateNested1.privateNested2.privateNested3 = new Ognl_NoN2Nested_Property(false);
        this.privateNested1.privateNested2.privateNested3.privateNested4 = new Ognl_NoN2Nested_Property(false);
        this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5 = new Ognl_NoN2Nested_Property(false);
    }

    public Ognl_NoN2Nested_Property(boolean b) {
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

    public Ognl_NoN2Nested_Property getNested1() {
        return privateNested1;
    }

    public void setNested1(Ognl_NoN2Nested_Property nested1) {
        this.privateNested1 = nested1;
    }

    public Ognl_NoN2Nested_Property getNested2() {
        return privateNested2;
    }

    public void setNested2(Ognl_NoN2Nested_Property nested2) {
        this.privateNested2 = nested2;
    }

    public Ognl_NoN2Nested_Property getNested3() {
        return privateNested3;
    }

    public void setNested3(Ognl_NoN2Nested_Property nested3) {
        this.privateNested3 = nested3;
    }

    public Ognl_NoN2Nested_Property getNested4() {
        return privateNested4;
    }

    public void setNested4(Ognl_NoN2Nested_Property nested4) {
        this.privateNested4 = nested4;
    }

    public Ognl_NoN2Nested_Property getNested5() {
        return privateNested5;
    }

    public void setNested5(Ognl_NoN2Nested_Property nested5) {
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
