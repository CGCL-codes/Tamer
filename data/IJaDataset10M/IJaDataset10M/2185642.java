package prjfbtypes;

import java.util.Hashtable;
import org.jdom.Element;
import prjfbtypes.InputPrimitive;
import prjfbtypes.OutputPrimitive;

public class ServiceTransaction {

    private InputPrimitive inputPrimitive = null;

    private Hashtable<Integer, OutputPrimitive> outputPrimitives = null;

    public ServiceTransaction(InputPrimitive inputPrimitive, Hashtable<Integer, OutputPrimitive> outputPrimitives) {
        this.inputPrimitive = inputPrimitive;
        this.outputPrimitives = outputPrimitives;
    }

    public String toString() {
        return "ServiceTransaction";
    }

    public Element toXML() {
        Element serviceTransactionElement = new Element("ServiceTransaction");
        if (inputPrimitive != null) {
            serviceTransactionElement.addContent(inputPrimitive.toXML());
        }
        if (outputPrimitives != null) {
            for (OutputPrimitive outputPrimitive : outputPrimitives.values()) {
                serviceTransactionElement.addContent(outputPrimitive.toXML());
            }
        }
        return serviceTransactionElement;
    }

    public void print() {
        inputPrimitive.print();
        if (outputPrimitives != null) {
            for (OutputPrimitive outputPrimitive : outputPrimitives.values()) {
                outputPrimitive.print();
            }
        }
    }

    public InputPrimitive getInputPrimitive() {
        return inputPrimitive;
    }

    public void setInputPrimitive(InputPrimitive inputPrimitive) {
        this.inputPrimitive = inputPrimitive;
    }

    public Hashtable<Integer, OutputPrimitive> getOutputPrimitives() {
        return outputPrimitives;
    }

    public void setOutputPrimitives(Hashtable<Integer, OutputPrimitive> outputPrimitives) {
        this.outputPrimitives = outputPrimitives;
    }
}
