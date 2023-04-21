package zildo.monde.map;

import zildo.fwk.file.EasyBuffering;
import zildo.fwk.file.EasySerializable;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;
import zildo.monde.util.Zone;
import zildo.server.EngineZildo;

public class ChainingPoint implements EasySerializable {

    public enum MapLink {

        REGULAR, STAIRS_STRAIGHT, STAIRS_CORNER_LEFT, STAIRS_CORNER_RIGHT
    }

    private String mapname;

    private short px, py;

    private boolean vertical;

    private boolean border;

    private boolean single;

    private int orderX;

    private int orderY;

    private boolean done;

    private Zone zone;

    public String getMapname() {
        return mapname;
    }

    public void setMapname(String mapname) {
        this.mapname = mapname;
    }

    public short getPx() {
        return px;
    }

    public void setPx(short px) {
        this.px = px;
        zone = null;
    }

    public short getPy() {
        return py;
    }

    public void setPy(short py) {
        this.py = py;
        zone = null;
    }

    public int getOrderX() {
        return orderX;
    }

    public void setOrderX(int orderX) {
        this.orderX = orderX;
    }

    public int getOrderY() {
        return orderY;
    }

    public MapLink getLinkType() {
        int infomap = EngineZildo.mapManagement.getCurrentMap().readmap(px, py);
        switch(infomap) {
            case 183 + 768:
            case 184 + 768:
                return MapLink.STAIRS_CORNER_LEFT;
            case 187 + 768:
            case 188 + 768:
                return MapLink.STAIRS_CORNER_RIGHT;
            case 1024 + 249:
            case 1024 + 250:
                return MapLink.STAIRS_STRAIGHT;
            default:
                return MapLink.REGULAR;
        }
    }

    public void setOrderY(int orderY) {
        this.orderY = orderY;
    }

    public ChainingPoint() {
    }

    public boolean isCollide(int ax, int ay, boolean p_border) {
        if (single) {
            return ax == px && ay == py;
        }
        if (!border) {
            if (vertical) {
                if (ay >= py && ax == px && ay <= (py + 1)) {
                    return true;
                }
            } else {
                if (ax >= px && ay == py && ax <= (px + 1)) {
                    return true;
                }
            }
        } else if (p_border) {
            if (py == ay || px == ax) {
                return true;
            }
        }
        return false;
    }

    public Angle getAngle(int x, int y, Angle startAngle) {
        Angle angle = startAngle;
        if (border && (px == 0 || px == EngineZildo.mapManagement.getCurrentMap().getDim_x() - 1)) {
            if (x % 16 > 8) {
                angle = Angle.EST;
            } else {
                angle = Angle.OUEST;
            }
        } else if (border) {
            if ((y % 16) > 8) {
                angle = Angle.SUD;
            } else {
                angle = Angle.NORD;
            }
        }
        return angle;
    }

    public boolean isBorder() {
        return border;
    }

    public void setVertical(boolean p_verti) {
        vertical = p_verti;
        zone = null;
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setBorder(boolean p_border) {
        border = p_border;
        zone = null;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
        zone = null;
    }

    @Override
    public String toString() {
        return mapname;
    }

    /**
	 * Get the range, in pixel coordinates, taken for the point.
	 * 
	 * @return Zone
	 */
    public Zone getZone(Area p_map) {
        if (zone == null) {
            Point p1 = new Point(px, py);
            Point p2 = new Point(2, 1);
            if (isBorder()) {
                if (p1.x == 0 || p1.x == p_map.getDim_x() - 1) {
                    p1.y = 0;
                    p2.y = p_map.getDim_y();
                    p2.x = 1;
                } else {
                    p1.x = 0;
                    p2.x = p_map.getDim_x();
                }
            } else if (isVertical()) {
                p2.x = 1;
                p2.y = 2;
            } else if (isSingle()) {
                p2.x = 1;
                p2.y = 1;
            }
            zone = new Zone(16 * p1.x, 16 * p1.y, 16 * p2.x, 16 * p2.y);
        }
        return zone;
    }

    /**
	 * Deserialize a chaining point from a given buffer.
	 * 
	 * @param p_buffer
	 * @return ChainingPoint
	 */
    public static ChainingPoint deserialize(EasyBuffering p_buffer) {
        ChainingPoint pe = new ChainingPoint();
        pe.px = p_buffer.readUnsignedByte();
        pe.py = p_buffer.readUnsignedByte();
        String mapName = p_buffer.readString();
        pe.mapname = mapName;
        if ((pe.px & 64) != 0) {
            pe.single = true;
            pe.px &= 128 + 63;
        }
        if (pe.px > 127) {
            pe.vertical = true;
            pe.px &= 127;
        }
        if (pe.py > 127) {
            pe.border = true;
            pe.py &= 127;
        }
        return pe;
    }

    /**
	 * Serialize this chaining point.
	 */
    @Override
    public void serialize(EasyBuffering p_buffer) {
        int saveX = px;
        int saveY = py;
        if (single) {
            saveX |= 64;
        }
        if (vertical) {
            saveX |= 128;
        }
        if (border) {
            saveY |= 128;
        }
        p_buffer.put((byte) saveX);
        p_buffer.put((byte) saveY);
        p_buffer.put(mapname);
    }

    /**
	 * Useful for test cases.
	 */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChainingPoint)) {
            return false;
        }
        ChainingPoint other = (ChainingPoint) o;
        return (px == other.px && py == other.py && mapname.equals(other.mapname) && orderX == other.orderX && orderY == other.orderY);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
