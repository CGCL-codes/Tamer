package hu.schmidtsoft.parser.language.impl;

import hu.schmidtsoft.parser.language.EType;
import hu.schmidtsoft.parser.language.ITerm;
import hu.schmidtsoft.parser.language.ITermReference;
import java.util.Map;

public class TermRef extends Term implements ITermReference {

    public String getReferenced() {
        return subsStr.get(0);
    }

    public TermRef(String name, String referenced) {
        super(name);
        subsStr.add(referenced);
    }

    @Override
    public EType getType() {
        return EType.reference;
    }

    @Override
    public String toString() {
        return "->" + getReferenced();
    }

    ITerm sub;

    public void initialize(Map<String, ITerm> termMap) {
        sub = termMap.get(getReferenced());
    }

    public ITerm getSub() {
        return sub;
    }
}
