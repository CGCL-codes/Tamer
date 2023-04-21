package dsr.hud;

import java.awt.Point;
import dsr.AppletMain;
import dsr.data.EquipmentData;

public final class EquipmentIcon extends AbstractIcon {

    public static final int PICKUP = 1;

    public static final int CHANGE = 2;

    private static final long serialVersionUID = 1L;

    private int mode;

    private EquipmentData eq;

    public EquipmentIcon(AppletMain m, EquipmentData _eq, int _mode) {
        super(m, null, false, _eq.getName(true), AbstractIcon.green);
        mode = _mode;
        eq = _eq;
    }

    public boolean mouseClicked(Point p) {
        switch(mode) {
            case PICKUP:
                main.pickupEquipment(eq);
                break;
            case CHANGE:
                main.changeCurrentEquipment(eq);
                break;
            default:
                throw new RuntimeException("Unknown equipment command: " + mode);
        }
        return true;
    }
}
