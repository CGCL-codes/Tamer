package jest.ui;

import jest.classfile.IntegerConstant;
import java.awt.Label;
import java.awt.Panel;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;

public class IntegerConstantDisplay extends Panel {

    public IntegerConstantDisplay(IntegerConstant cc) {
        BoxLayout lm;
        lm = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(lm);
        add(new Label("Constant index: " + cc.getIndex()));
        add(new Label("Constant type: Integer"));
        add(new JSeparator());
        add(new Label("Value: " + cc.getValue()));
    }
}
