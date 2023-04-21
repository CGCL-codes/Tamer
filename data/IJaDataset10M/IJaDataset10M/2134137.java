package jest.ui;

import jest.classfile.MethodDescriptor;
import java.awt.Label;
import java.awt.Panel;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;

public class MethodDescriptorDisplay extends Panel {

    public MethodDescriptorDisplay(MethodDescriptor fd) {
        BoxLayout lm;
        lm = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(lm);
        add(new Label("Method name index: " + fd.getNameIndex()));
        add(new Label("Method type index: " + fd.getTypeIndex()));
        add(new Label("Access specifier: 0x" + Integer.toString(fd.getAccess(), 16)));
        add(new JSeparator());
        add(new Label("Method: " + fd.getAccessString(false) + " " + fd.getReturnTypeString().replace('/', '.') + " " + fd.getName() + fd.getParameterString() + ";"));
    }
}
