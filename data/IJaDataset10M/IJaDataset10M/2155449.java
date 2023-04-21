package vademecum.rules.abstractions;

import org.jdom.Element;

public abstract class IConditions {

    public IConditions(Element element) {
    }

    ;

    public IConditions() {
    }

    ;

    public abstract boolean checkConditions(IValue[] datapoint);

    public abstract double checkConditionsFuzzy(IValue[] datapoint);

    public abstract Element toElement(Element element);
}
