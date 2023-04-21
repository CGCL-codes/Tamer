package motejx.extensions.classic;

import java.awt.Point;
import motejx.extensions.nunchuk.AnalogStickEvent;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class LeftAnalogStickEvent extends AnalogStickEvent {

    public LeftAnalogStickEvent(Object source, Point point) {
        super(source, point);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof LeftAnalogStickEvent) {
            return obj.hashCode() == hashCode();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + source.hashCode();
        hash = hash * 31 + point.x;
        hash = hash * 31 + point.y;
        return hash;
    }
}
