package org.argetr.resim.ui.component.datasources;

import org.argetr.resim.proc.input.ConstInputPR;
import org.argetr.resim.property.FloatPRP;
import org.argetr.resim.ui.model.ComponentItem;

public class ConstInputCI extends ComponentItem {

    private static final long serialVersionUID = 1;

    private static final String CONST_PROP = "ConstInputCI._value";

    private float _value;

    public ConstInputCI() {
        super();
        _name = "Constant\nInput";
        _description = "Holds a float value";
        initProc();
    }

    public void setValue(float value) {
        _value = value;
    }

    public float getValue() {
        return _value;
    }

    protected void initDescriptorList() {
        super.initDescriptorList();
        registerProperty(new FloatPRP(this, CONST_PROP, "Value", "getValue", "setValue"));
    }

    public Class<ConstInputCI> getComponentClass() {
        return ConstInputCI.class;
    }

    public void initProc() {
        if (_processor == null) {
            _processor = new ConstInputPR();
        }
        _value = ((ConstInputPR) _processor).getParamValue();
    }

    public void updateProc() {
        if (_processor == null) {
            _processor = new ConstInputPR();
        }
        ((ConstInputPR) _processor).setParameters(_value);
    }
}
