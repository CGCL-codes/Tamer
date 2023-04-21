package ucalgary.ebe.utils.converters.swt;

import ucalgary.ebe.ci.mice.events.ICIMouseEvent;

/**
 * @author hkolenda
 *
 */
public class SWTConv {

    public static int getMouseButton(int SWTButton) {
        int iciButton = 0;
        switch(SWTButton) {
            case 1:
                iciButton = ICIMouseEvent.LBUTTON;
                break;
            case 2:
                iciButton = ICIMouseEvent.MBUTTON;
                break;
            case 3:
                iciButton = ICIMouseEvent.RBUTTON;
                break;
        }
        return iciButton;
    }
}
