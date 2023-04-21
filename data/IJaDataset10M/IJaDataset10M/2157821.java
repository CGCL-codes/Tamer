package l1j.server.server.model.map;

import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.types.Point;

public class L1V2Map extends L1Map {

    private final int _id;

    private final int _xLoc;

    private final int _yLoc;

    private final int _width;

    private final int _height;

    private final byte _map[];

    private boolean _isUnderwater;

    private boolean _isMarkable;

    private boolean _isTeleportable;

    private boolean _isEscapable;

    private boolean _isUseResurrection;

    private boolean _isUsePainwand;

    private boolean _isEnabledDeathPenalty;

    private boolean _isTakePets;

    private boolean _isRecallPets;

    private boolean _isUsableItem;

    private boolean _isUsableSkill;

    /**
	 * Mob���� ���� �Ұ����ϰ� �Ǵ� ������Ʈ�� Ÿ�ϻ� �����ϴ����� ��Ÿ���� ��Ʈ �÷���
	 */
    private static final byte BITFLAG_IS_IMPASSABLE = (byte) 128;

    private int offset(int x, int y) {
        return ((y - _yLoc) * _width * 2) + ((x - _xLoc) * 2);
    }

    private int accessOriginalTile(int x, int y) {
        return _map[offset(x, y)] & (~BITFLAG_IS_IMPASSABLE);
    }

    public L1V2Map(int id, byte map[], int xLoc, int yLoc, int width, int height, boolean underwater, boolean markable, boolean teleportable, boolean escapable, boolean useResurrection, boolean usePainwand, boolean enabledDeathPenalty, boolean takePets, boolean recallPets, boolean usableItem, boolean usableSkill) {
        _id = id;
        _map = map;
        _xLoc = xLoc;
        _yLoc = yLoc;
        _width = width;
        _height = height;
        _isUnderwater = underwater;
        _isMarkable = markable;
        _isTeleportable = teleportable;
        _isEscapable = escapable;
        _isUseResurrection = useResurrection;
        _isUsePainwand = usePainwand;
        _isEnabledDeathPenalty = enabledDeathPenalty;
        _isTakePets = takePets;
        _isRecallPets = recallPets;
        _isUsableItem = usableItem;
        _isUsableSkill = usableSkill;
    }

    @Override
    public int getHeight() {
        return _height;
    }

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public int getOriginalTile(int x, int y) {
        int lo = _map[offset(x, y)];
        int hi = _map[offset(x, y) + 1];
        return (lo | ((hi << 8) & 0xFF00));
    }

    @Override
    public int getTile(int x, int y) {
        return _map[offset(x, y)];
    }

    @Override
    public int getWidth() {
        return _width;
    }

    @Override
    public int getX() {
        return _xLoc;
    }

    @Override
    public int getY() {
        return _yLoc;
    }

    @Override
    public boolean isArrowPassable(Point pt) {
        return isArrowPassable(pt.getX(), pt.getY());
    }

    @Override
    public boolean isArrowPassable(int x, int y) {
        return (accessOriginalTile(x, y) != 1);
    }

    @Override
    public boolean isArrowPassable(Point pt, int heading) {
        return isArrowPassable(pt.getX(), pt.getY(), heading);
    }

    @Override
    public boolean isArrowPassable(int x, int y, int heading) {
        int tile;
        int newX;
        int newY;
        if (heading == 0) {
            tile = accessOriginalTile(x, y - 1);
            newX = x;
            newY = y - 1;
        } else if (heading == 1) {
            tile = accessOriginalTile(x + 1, y - 1);
            newX = x + 1;
            newY = y - 1;
        } else if (heading == 2) {
            tile = accessOriginalTile(x + 1, y);
            newX = x + 1;
            newY = y;
        } else if (heading == 3) {
            tile = accessOriginalTile(x + 1, y + 1);
            newX = x + 1;
            newY = y + 1;
        } else if (heading == 4) {
            tile = accessOriginalTile(x, y + 1);
            newX = x;
            newY = y + 1;
        } else if (heading == 5) {
            tile = accessOriginalTile(x - 1, y + 1);
            newX = x - 1;
            newY = y + 1;
        } else if (heading == 6) {
            tile = accessOriginalTile(x - 1, y);
            newX = x - 1;
            newY = y;
        } else if (heading == 7) {
            tile = accessOriginalTile(x - 1, y - 1);
            newX = x - 1;
            newY = y - 1;
        } else {
            return false;
        }
        if (isExistDoor(newX, newY)) {
            return false;
        }
        return (tile != 1);
    }

    @Override
    public boolean isCombatZone(Point pt) {
        return isCombatZone(pt.getX(), pt.getY());
    }

    @Override
    public boolean isCombatZone(int x, int y) {
        return (accessOriginalTile(x, y) == 8);
    }

    @Override
    public boolean isInMap(Point pt) {
        return isInMap(pt.getX(), pt.getY());
    }

    @Override
    public boolean isInMap(int x, int y) {
        return (_xLoc <= x && x < _xLoc + _width && _yLoc <= y && y < _yLoc + _height);
    }

    @Override
    public boolean isNormalZone(Point pt) {
        return isNormalZone(pt.getX(), pt.getY());
    }

    @Override
    public boolean isNormalZone(int x, int y) {
        return (!isCombatZone(x, y) && !isSafetyZone(x, y));
    }

    @Override
    public boolean isPassable(Point pt) {
        return isPassable(pt.getX(), pt.getY());
    }

    @Override
    public boolean isPassable(int x, int y) {
        int tile = accessOriginalTile(x, y);
        if (tile == 1 || tile == 9 || tile == 65 || tile == 69 || tile == 73) {
            return false;
        }
        if (0 != (_map[offset(x, y)] & BITFLAG_IS_IMPASSABLE)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isPassable(Point pt, int heading) {
        return isPassable(pt.getX(), pt.getY(), heading);
    }

    @Override
    public boolean isPassable(int x, int y, int heading) {
        int tile;
        if (heading == 0) {
            tile = accessOriginalTile(x, y - 1);
        } else if (heading == 1) {
            tile = accessOriginalTile(x + 1, y - 1);
        } else if (heading == 2) {
            tile = accessOriginalTile(x + 1, y);
        } else if (heading == 3) {
            tile = accessOriginalTile(x + 1, y + 1);
        } else if (heading == 4) {
            tile = accessOriginalTile(x, y + 1);
        } else if (heading == 5) {
            tile = accessOriginalTile(x - 1, y + 1);
        } else if (heading == 6) {
            tile = accessOriginalTile(x - 1, y);
        } else if (heading == 7) {
            tile = accessOriginalTile(x - 1, y - 1);
        } else {
            return false;
        }
        if (tile == 1 || tile == 9 || tile == 65 || tile == 69 || tile == 73) {
            return false;
        }
        if (0 != (_map[offset(x, y)] & BITFLAG_IS_IMPASSABLE)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSafetyZone(Point pt) {
        return isSafetyZone(pt.getX(), pt.getY());
    }

    @Override
    public boolean isSafetyZone(int x, int y) {
        return accessOriginalTile(x, y) == 4;
    }

    @Override
    public void setPassable(Point pt, boolean isPassable) {
        setPassable(pt.getX(), pt.getY(), isPassable);
    }

    @Override
    public void setPassable(int x, int y, boolean isPassable) {
        if (isPassable) {
            _map[offset(x, y)] &= (~BITFLAG_IS_IMPASSABLE);
        } else {
            _map[offset(x, y)] |= BITFLAG_IS_IMPASSABLE;
        }
    }

    @Override
    public boolean isUnderwater() {
        return _isUnderwater;
    }

    @Override
    public boolean isMarkable() {
        return _isMarkable;
    }

    @Override
    public boolean isTeleportable() {
        return _isTeleportable;
    }

    @Override
    public boolean isEscapable() {
        return _isEscapable;
    }

    @Override
    public boolean isUseResurrection() {
        return _isUseResurrection;
    }

    @Override
    public boolean isUsePainwand() {
        return _isUsePainwand;
    }

    @Override
    public boolean isEnabledDeathPenalty() {
        return _isEnabledDeathPenalty;
    }

    @Override
    public boolean isTakePets() {
        return _isTakePets;
    }

    @Override
    public boolean isRecallPets() {
        return _isRecallPets;
    }

    @Override
    public boolean isUsableItem() {
        return _isUsableItem;
    }

    @Override
    public boolean isUsableSkill() {
        return _isUsableSkill;
    }

    @Override
    public boolean isFishingZone(int x, int y) {
        return accessOriginalTile(x, y) == 16;
    }

    @Override
    public boolean isExistDoor(int x, int y) {
        for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
            if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
                continue;
            }
            if (door.isDead()) {
                continue;
            }
            int leftEdgeLocation = door.getLeftEdgeLocation();
            int rightEdgeLocation = door.getRightEdgeLocation();
            int size = rightEdgeLocation - leftEdgeLocation;
            if (size == 0) {
                if (x == door.getX() && y == door.getY()) {
                    return true;
                }
            } else {
                if (door.getDirection() == 0) {
                    for (int doorX = leftEdgeLocation; doorX <= rightEdgeLocation; doorX++) {
                        if (x == doorX && y == door.getY()) {
                            return true;
                        }
                    }
                } else {
                    for (int doorY = leftEdgeLocation; doorY <= rightEdgeLocation; doorY++) {
                        if (x == door.getX() && y == doorY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString(Point pt) {
        int tile = getOriginalTile(pt.getX(), pt.getY());
        return (tile & 0xFF) + " " + ((tile >> 8) & 0xFF);
    }
}
