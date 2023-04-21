package net.sf.kfgodel.bean2bean.testbeans.performance;

import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.interpreters.InterpreterType;

/**
 * 
 * @author D. Garcia
 */
public class Groovy_Nested2Non_Property implements PerformanceTestBean {

    private String privateSource;

    @CopyFrom(value = "nested1.nested2.nested3.nested4.nested5.source", getterInterpreter = InterpreterType.GROOVY, setter = "_destino.destination = _valor", setterInterpreter = InterpreterType.GROOVY)
    private String privateDestination;

    private Groovy_Nested2Non_Property privateNested1;

    private Groovy_Nested2Non_Property privateNested2;

    private Groovy_Nested2Non_Property privateNested3;

    private Groovy_Nested2Non_Property privateNested4;

    private Groovy_Nested2Non_Property privateNested5;

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

    public Groovy_Nested2Non_Property getNested1() {
        return privateNested1;
    }

    public void setNested1(Groovy_Nested2Non_Property nested1) {
        this.privateNested1 = nested1;
    }

    public Groovy_Nested2Non_Property getNested2() {
        return privateNested2;
    }

    public void setNested2(Groovy_Nested2Non_Property nested2) {
        this.privateNested2 = nested2;
    }

    public Groovy_Nested2Non_Property getNested3() {
        return privateNested3;
    }

    public void setNested3(Groovy_Nested2Non_Property nested3) {
        this.privateNested3 = nested3;
    }

    public Groovy_Nested2Non_Property getNested4() {
        return privateNested4;
    }

    public void setNested4(Groovy_Nested2Non_Property nested4) {
        this.privateNested4 = nested4;
    }

    public Groovy_Nested2Non_Property getNested5() {
        return privateNested5;
    }

    public void setNested5(Groovy_Nested2Non_Property nested5) {
        this.privateNested5 = nested5;
    }

    public void prepareValue(String string) {
        this.privateNested1 = new Groovy_Nested2Non_Property();
        this.privateNested1.privateNested2 = new Groovy_Nested2Non_Property();
        this.privateNested1.privateNested2.privateNested3 = new Groovy_Nested2Non_Property();
        this.privateNested1.privateNested2.privateNested3.privateNested4 = new Groovy_Nested2Non_Property();
        this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5 = new Groovy_Nested2Non_Property();
        this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5.privateSource = string;
    }

    public String getSourceValue() {
        return this.privateNested1.privateNested2.privateNested3.privateNested4.privateNested5.privateSource;
    }

    public String getDestinationValue() {
        return privateDestination;
    }
}
