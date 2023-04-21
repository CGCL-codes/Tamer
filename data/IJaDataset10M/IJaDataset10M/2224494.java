package org.sinaxe.runtime;

import org.sinaxe.*;
import org.dom4j.Element;

public class MapVariable extends MapBase {

    private SinaxeVariable variable = null;

    private RuntimeBase parent;

    public MapVariable(SinaxeVariable variable, RuntimeBase parent) {
        super(null);
        this.variable = variable;
        this.parent = parent;
    }

    public MapVariable(Element xmlElement, RuntimeBase parent) throws SinaxeException {
        super(xmlElement);
        this.parent = parent;
        String varStr = xmlElement.attributeValue("variable");
        variable = parent.registerVariable(varStr);
    }

    public int getType() {
        return MAPT_VARIABLE;
    }

    public void setType(int maptype, int datatype) throws org.sinaxe.SinaxeException {
        if ((maptype & MAPT_VARIABLE) != MAPT_VARIABLE || datatype > 0) throw new SinaxeException("reciever does not support map type!");
    }

    public SinaxeVariable getVariable() throws SinaxeException {
        return variable;
    }
}
