package main.gui.components;

import java.awt.ComponentOrientation;
import javax.swing.JInternalFrame;

public class MyRealInternalFrame extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    public MyRealInternalFrame() {
        super();
        init();
    }

    public MyRealInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        init();
    }

    public MyRealInternalFrame(String arg0, boolean arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
        init();
    }

    public MyRealInternalFrame(String arg0, boolean arg1, boolean arg2) {
        super(arg0, arg1, arg2);
        init();
    }

    public MyRealInternalFrame(String arg0, boolean arg1) {
        super(arg0, arg1);
        init();
    }

    public MyRealInternalFrame(String arg0) {
        super(arg0);
        init();
    }

    private void init() {
        this.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }
}
