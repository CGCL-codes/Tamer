package prjfbtypes;

import java.util.Hashtable;
import org.jdom.Element;
import prjfbtypes.ECState;
import prjfbtypes.ECTransition;

public class ECC {

    private Hashtable<String, ECState> ecStates;

    private Hashtable<String, ECTransition> ecTransitions;

    public ECC(Hashtable<String, ECState> ecStates, Hashtable<String, ECTransition> ecTransitions) {
        if (ecStates.isEmpty()) {
            System.err.println("ecStates in ECC is empty");
            System.exit(0);
        } else {
            this.ecStates = ecStates;
        }
        if (ecTransitions.isEmpty()) {
            System.err.println("ecTransitions in ECC is empty");
            System.exit(0);
        } else {
            this.ecTransitions = ecTransitions;
        }
    }

    public String toString() {
        return "ECC";
    }

    public Element toXML() {
        Element eccElement = new Element("ECC");
        for (ECState ecState : ecStates.values()) {
            eccElement.addContent(ecState.toXML());
        }
        for (ECTransition ecTransition : ecTransitions.values()) {
            eccElement.addContent(ecTransition.toXML());
        }
        return eccElement;
    }

    public void print() {
        for (ECState ecState : ecStates.values()) {
            ecState.print();
        }
        for (ECTransition ecTransition : ecTransitions.values()) {
            ecTransition.print();
        }
    }

    public Hashtable<String, ECState> getEcStates() {
        return ecStates;
    }

    public void setEcStates(Hashtable<String, ECState> ecStates) {
        this.ecStates = ecStates;
    }

    public Hashtable<String, ECTransition> getEcTransitions() {
        return ecTransitions;
    }

    public void setEcTransitions(Hashtable<String, ECTransition> ecTransitions) {
        this.ecTransitions = ecTransitions;
    }
}
