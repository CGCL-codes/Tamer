package org.argetr.resim.ui.component.math;

import org.argetr.resim.proc.math.ArithmeticPR;
import org.argetr.resim.proc.math.MathPR;
import org.argetr.resim.ui.model.ComponentItem;

public class DivideCI extends ComponentItem {

    private static final long serialVersionUID = 1;

    public DivideCI() {
        super();
        _name = "Divide";
        _description = "Divide images or image with constant";
        initProc();
    }

    protected void initDescriptorList() {
        super.initDescriptorList();
    }

    @Override
    public Class getComponentClass() {
        return DivideCI.class;
    }

    public void initProc() {
        if (_processor == null) {
            _processor = new ArithmeticPR();
            ((ArithmeticPR) _processor).setParameters(MathPR.DIV);
        }
    }

    public void updateProc() {
        if (_processor == null) {
            _processor = new ArithmeticPR();
            ((ArithmeticPR) _processor).setParameters(MathPR.DIV);
        }
    }
}
