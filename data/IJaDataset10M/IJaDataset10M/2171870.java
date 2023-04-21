package courselog;

import ewe.fx.Dimension;
import ewe.fx.Image;
import ewe.sys.Device;
import ewe.ui.Control;
import ewe.ui.ControlConstants;
import ewe.ui.Form;
import ewe.ui.mButton;

/**
 *
 * @author rigal
 */
public abstract class CourseLogAppForm extends Form {

    protected mButton bLeft = null;

    protected mButton bRight = null;

    protected Control defC;

    public static final DebugLogger dbgL = new DebugLogger();

    /**
     * Initialise Form setup of components. Call this after defining the
     * buttons and a default control values.
     */
    protected void courseLogAppFormSetup(mButton bL, mButton bR, Control d) {
        Dimension dScreen = Device.getScreenSize();
        Form.globalIcon = new Image("res/courseLog.png");
        setPreferredSize(dScreen.width, dScreen.height);
        defC = d;
        Control ctDbg = dbgL.getLogControl();
        int iWidthButton = dScreen.width / 2;
        int iHeightButton = (dScreen.height > 480 ? 16 : 8);
        bLeft = bL;
        bRight = bR;
        bLeft.set(ControlConstants.TakesKeyFocus, false);
        bLeft.setPreferredSize(iWidthButton, iHeightButton);
        bRight.set(ControlConstants.TakesKeyFocus, false);
        bRight.setPreferredSize(iWidthButton, iHeightButton);
        if (ctDbg != null) {
            addLast(ctDbg);
            ctDbg.modify(Control.NoFocus | Control.NotAnEditor, Control.TakesKeyFocus | Control.TakeControlEvents);
        }
        if (d != null) {
            d.setPreferredSize(dScreen.width, dScreen.height / 2 - iHeightButton);
            addLast(d);
        }
        addNext(bLeft);
        addLast(bRight);
        if (d != null) {
            d.takeFocus(Control.ByRequest);
        }
    }
}
