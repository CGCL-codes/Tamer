package trafficjams.model.classes;

import trafficjams.model.interfaces.IElementPosition;
import trafficjams.model.interfaces.IMapElement;
import trafficjams.model.interfaces.IRoad;
import trafficjams.model.interfaces.IVehicle;
import trafficjams.model.util.Point;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 17.11.11
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */
public class RoadPosition implements IElementPosition {

    private Float value = null;

    private Road element = null;

    public RoadPosition(float v, Road element) {
        value = new Float(v);
        this.element = element;
    }

    private RoadPosition() {
    }

    public Object getValue() {
        return value;
    }

    public boolean isNearPosition(IElementPosition finishPosition) {
        if (this.element.equals(finishPosition.getElement())) {
            float pos1 = element.getAbsolutePositionValue(value.floatValue());
            float pos2 = ((IRoad) finishPosition).getAbsolutePositionValue((Float) finishPosition.getValue());
            float t = pos1 - pos2;
            float minDist = element.getMap().getMinDist();
            return t < minDist && t > -minDist;
        } else {
            return false;
        }
    }

    public IMapElement getElement() {
        return element;
    }

    public Point getCoord() {
        float p = element.getAbsolutePositionValue(value);
        float d = p / (element.getLength() - p);
        float x = (element.getFCross().getCoord().getX() + d * element.getLCross().getCoord().getX()) / (1 + d);
        float y = (element.getFCross().getCoord().getY() + d * element.getLCross().getCoord().getY()) / (1 + d);
        return new Point(x, y);
    }

    public IElementPosition getNextPosition(IVehicle vehicle, float distance, IRoad nextElement) throws Exception {
        IElementPosition retVal = null;
        float pos = this.value;
        boolean needKeepCross = element.needKeepCross(vehicle);
        if (needKeepCross) {
            if (this.value >= 0) {
                retVal = element.getLCross().getNextPosition(vehicle, distance, nextElement);
                if (retVal != null) {
                    element.unregistrate(vehicle);
                }
                return retVal;
            } else {
                retVal = element.getFCross().getNextPosition(vehicle, distance, nextElement);
                if (retVal != null) {
                    element.unregistrate(vehicle);
                }
                return retVal;
            }
        } else {
            return retVal = element.getNextPosition(vehicle, distance, nextElement);
        }
    }

    public IElementPosition copy() {
        return new RoadPosition(value, element);
    }
}
