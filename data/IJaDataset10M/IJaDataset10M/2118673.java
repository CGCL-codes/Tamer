package org.argeproje.resim.ui.component.math;

import org.argeproje.resim.proc.math.ArithmeticPR;
import org.argeproje.resim.proc.math.MathPR;
import org.argeproje.resim.ui.model.ComponentItem;

public class SubtractCI extends ComponentItem {

    private static final long serialVersionUID = 1;

    public SubtractCI() {
        super();
        _name = "Subtract";
        _description = "Subtract images or image with constant";
        initProc();
    }

    protected void initDescriptorList() {
        super.initDescriptorList();
    }

    @Override
    public Class getComponentClass() {
        return SubtractCI.class;
    }

    public void initProc() {
        if (_processor == null) {
            _processor = new ArithmeticPR();
            ((ArithmeticPR) _processor).setParameters(MathPR.SUB);
        }
    }

    public void updateProc() {
        if (_processor == null) {
            _processor = new ArithmeticPR();
            ((ArithmeticPR) _processor).setParameters(MathPR.SUB);
        }
    }
}
